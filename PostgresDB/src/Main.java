import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class Main {
	public static void main(String[] argv) throws BindException {
		final Server server = new Server();
		final Instructions inst = new Instructions();
		HttpServer socket;
		Scanner scan = new Scanner(System.in);
		System.out.printf("Staring Server in Main....\n");
		try {
			socket = server.create();
		} catch (Exception e) {
			System.out.println("####Bind Exception Hit Here ######");
			socket = server.connectExisting();
		}

		if (Boolean.parseBoolean(System.getenv("RUNNING_IN_ECLIPSE")) == true) {
			System.out
					.println("You're using Eclipse; click in this console and     "
							+ "press ENTER to call System.exit() and run the shutdown routine.");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		System.out.println("What would you like to do?");
		String myLine = scan.nextLine();
		while (inst.getResult(myLine) == true) {
			System.out.printf("\nWhat would you like to do?\n");
			myLine = scan.nextLine();
		}
		scan.close();
		try {
			server.close(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}