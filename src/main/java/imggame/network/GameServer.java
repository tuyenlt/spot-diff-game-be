package imggame.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import imggame.config.AppConfig;
import imggame.controllers.UserController;
import imggame.models.User;
import imggame.network.packets.BasePacket;
import imggame.network.packets.ErrorResponse;
import imggame.network.packets.GetPlayerListRequest;
import imggame.network.packets.LoginRequest;
import imggame.network.packets.RegisterRequest;

public class GameServer {
	private ServerSocket serverSocket;
	private UserController userController = new UserController();
	private ExecutorService threadPool;
	private Map<Integer, ClientHandler> connectedClients = new ConcurrentHashMap<>();
	private volatile boolean running = true;

	public GameServer() {
		int serverPort = AppConfig.GAME_MAIN_PORT;
		this.threadPool = Executors.newCachedThreadPool();

		try {
			this.serverSocket = new ServerSocket(serverPort);
			System.out.println("Server started on port: " + serverPort);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error starting the server", e);
		}
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void listen() {
		System.out.println("Server listening for connections...");

		while (running) {
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " +
						clientSocket.getInetAddress().getHostAddress());

				ClientHandler handler = new ClientHandler(clientSocket);
				threadPool.submit(handler);

			} catch (IOException e) {
				if (running) {
					System.err.println("Error accepting client connection: " + e.getMessage());
				}
			}
		}
	}

	public void shutdown() {
		running = false;
		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			threadPool.shutdown();

			connectedClients.values().forEach(ClientHandler::close);
			connectedClients.clear();

			System.out.println("Server shutdown complete");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ClientHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream input;
		private ObjectOutputStream output;
		private Integer userId;
		private volatile boolean active = true;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				output.flush();
				input = new ObjectInputStream(socket.getInputStream());

				while (active && !socket.isClosed()) {
					try {
						Object request = input.readObject();

						if (request == null) {
							break;
						}

						processRequest(request);

					} catch (ClassNotFoundException e) {
						System.err.println("Unknown packet type: " + e.getMessage());
						sendError("Unknown packet type");
					}
				}

			} catch (IOException e) {
				if (active) {
					System.err.println("Client connection error: " + e.getMessage());
				}
			} finally {
				close();
			}
		}

		private void processRequest(Object request) {
			try {
				Object response = null;

				if (request instanceof LoginRequest) {
					LoginRequest loginPacket = (LoginRequest) request;
					response = userController.handleLogin(loginPacket);
					if (!(response instanceof ErrorResponse)) {
						this.userId = ((User) response).getId();
						connectedClients.put(userId, this);
					}
				} else if (request instanceof GetPlayerListRequest) {
					GetPlayerListRequest getPlayerListPacket = (GetPlayerListRequest) request;
					response = userController.handleGetPlayerList(getPlayerListPacket);
				} else if (request instanceof RegisterRequest) {
					RegisterRequest registerPacket = (RegisterRequest) request;
					response = userController.handleRegister(registerPacket);
				} else {
					response = new ErrorResponse("Unknown request type");
				}

				if (response != null) {
					sendResponse(response);
				}

			} catch (Exception e) {
				e.printStackTrace();
				sendError("Error processing request: " + e.getMessage());
			}
		}

		private void sendResponse(Object response) {
			try {
				synchronized (output) {
					output.writeObject(response);
					output.flush();
				}
			} catch (IOException e) {
				System.err.println("Error sending response: " + e.getMessage());
				close();
			}
		}

		private void sendError(String message) {
			sendResponse(new ErrorResponse(message));
		}

		public void close() {
			active = false;

			if (userId != null) {
				connectedClients.remove(userId);
			}

			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Client disconnected: " +
					(userId != null ? userId : socket.getInetAddress().getHostAddress()));
		}
	}

	public void sendToClient(int userId, BasePacket packet) {
		ClientHandler handler = connectedClients.get(userId);
		if (handler != null) {
			handler.sendResponse(packet);
		}
	}

	public void broadcast(BasePacket packet) {
		connectedClients.values().forEach(handler -> handler.sendResponse(packet));
	}
}
