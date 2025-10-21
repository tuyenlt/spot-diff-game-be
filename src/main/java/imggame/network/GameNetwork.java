package imggame.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.List;

import imggame.controllers.UserController;
import imggame.models.User;
import imggame.network.packets.ErrorPacket;
import imggame.network.packets.GetPlayerListPacket;
import imggame.network.packets.LoginPacket;
import imggame.network.packets.RegisterPacket;
import io.github.cdimascio.dotenv.Dotenv;

public class GameNetwork {
	private final int PORT;
	private ServerSocket serverSocket;
	private UserController userController = new UserController();
	// private GameController gameController = GameController.getInstance();

	public GameNetwork() {
		Dotenv dotenv = Dotenv.load();
		this.PORT = Integer.parseInt(dotenv.get("GAME_MAIN_PORT"));

		try {
			this.serverSocket = new ServerSocket(PORT);
			System.out.println("Server started on port: " + PORT);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error starting the server", e);
		}
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void listen() {
		try {
			while (true) {
				var clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
				ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
				handleClient(input, output);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error accepting client connection", e);
		}
	}

	private void handleClient(ObjectInputStream input, ObjectOutputStream output) {
		new Thread(() -> {
			try {
				Object request;
				while ((request = input.readObject()) != null) {
					processRequest(request, output);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void processRequest(Object request, ObjectOutputStream output) throws Exception {
		try {
			if (request instanceof LoginPacket) {
				LoginPacket loginPacket = (LoginPacket) request;
				boolean success = userController.checkLogin(loginPacket.username, loginPacket.password);
				if (success) {
					output.writeObject("Login successful");
				} else {
					output.writeObject(new ErrorPacket("Invalid username or password"));
				}
			}

			if (request instanceof GetPlayerListPacket) {
				GetPlayerListPacket getPlayerListPacket = (GetPlayerListPacket) request;
				List<User> players = userController.getUserRankedList(getPlayerListPacket.pageSize,
						getPlayerListPacket.offset, getPlayerListPacket.isDESC);
				output.writeObject(players);
			}

			if (request instanceof RegisterPacket) {
				RegisterPacket registerPacket = (RegisterPacket) request;
				User user = new User(registerPacket.username, registerPacket.email, registerPacket.password);
				boolean success = userController.createUser(user);
				if (success) {
					output.writeObject("Registration successful");
				} else {
					output.writeObject(new ErrorPacket("Username or email already exists"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			output.writeObject(new ErrorPacket("Error processing request: " + e.getMessage()));
		} finally {
			output.flush();
		}
	}

}
