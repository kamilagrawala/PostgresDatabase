import java.io.*;
import java.net.*;

public class Server {

	public ServerSocket create() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return serverSocket;
	}

	public boolean close(ServerSocket serverSocket) {
		try {
			serverSocket.close();
			System.out.printf("Server Socket Closed Successfully...\n");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
