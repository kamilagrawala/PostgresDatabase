import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
	public static void main(String[] argv) {
		Server server = new Server();
		Instructions inst = new Instructions();
		Scanner scan = new Scanner(System.in);
		System.out.printf("Staring Server in Main....\n");
		ServerSocket socket = server.create();
		System.out.println("What would you like to do?");
		String myLine = scan.nextLine();
		while (inst.getResult(myLine) == true) {
			System.out.printf("\nWhat would you like to do?\n");
			myLine = scan.nextLine();
		}
		scan.close();
		server.close(socket);
	}
}