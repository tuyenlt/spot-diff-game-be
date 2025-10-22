
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import imggame.config.AppConfig;
import imggame.models.User;
import imggame.network.packets.ErrorResponse;
import imggame.network.packets.LoginRequest;
import imggame.network.packets.RegisterRequest;

import java.lang.reflect.Field;

class ObjectPrinter {

	/**
	 * Prints all declared fields and their current values for a given object.
	 * Includes private fields. Does NOT print inherited fields.
	 */
	public static void printFields(Object obj) {
		if (obj == null) {
			System.out.println("Object is null.");
			return;
		}

		Class<?> clazz = obj.getClass();
		System.out.println("--- Fields for Class: " + clazz.getName() + " ---");

		// Use getDeclaredFields() to get all fields declared in this class,
		// regardless of access modifier (public, private, protected).
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// This is the crucial step to access private fields
			field.setAccessible(true);

			try {
				// Get the name of the field
				String fieldName = field.getName();

				// Get the value of the field for the given object instance
				Object fieldValue = field.get(obj);

				// Print the field name and its value
				System.out.println(fieldName + " = " + fieldValue);

			} catch (IllegalAccessException e) {
				// This catch block is generally only for when setAccessible(true) fails,
				// which is rare in standard Java environments.
				System.err.println("Could not access field " + field.getName() + ": " + e.getMessage());
			}
		}
	}
}

public class TestClient {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", AppConfig.GAME_MAIN_PORT);
		System.out.println("Connected to server: " + socket.getRemoteSocketAddress());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		testLogin(out, in);
		socket.close();
	}

	public static void testRegister(ObjectOutputStream out, ObjectInputStream in) throws Exception {
		RegisterRequest registerRequest = new RegisterRequest("testuser", "test@mail.com", "password123");
		out.writeObject(registerRequest);
		out.flush();
		Object response = in.readObject();
		LogResponse(response, User.class);
	}

	public static void testLogin(ObjectOutputStream out, ObjectInputStream in) throws Exception {
		LoginRequest loginRequest = new LoginRequest("testuser", "password123");
		out.writeObject(loginRequest);
		out.flush();
		Object response = in.readObject();
		LogResponse(response, User.class);
	}

	public static <T> void LogResponse(Object response, Class<T> expectedType) {
		if (response == null) {
			System.out.println("No response received from server.");
			return;
		}
		if (expectedType.isInstance(response)) {
			System.out.println("Response is of expected type: " + response.getClass().getName());
			ObjectPrinter.printFields(response);
			return;
		}
		if (response instanceof ErrorResponse) {
			ErrorResponse error = (ErrorResponse) response;
			System.out.println("Error: " + error.message);
			return;
		}
		System.out.println("Response: " + response.toString());
	}
}
