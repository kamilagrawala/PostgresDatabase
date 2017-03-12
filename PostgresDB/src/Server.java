import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

	public HttpServer create() throws IOException, BindException {
		HttpServer httpServer = null;
		try {
			httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
		} catch (BindException E) {
			Logger.getLogger(Server.class.getName()).log(Level.INFO,
					"Server already running on port 8080, using that connection (BindException)");
			return httpServer;
		}
		httpServer.createContext("/", new MyHandler());
		httpServer.setExecutor(null);
		httpServer.start();

		return httpServer;
	}

	public HttpServer connectExisting() {
		HttpServer httpServer = null;
		try {
			httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
			httpServer.createContext("/", new MyHandler());
			httpServer.setExecutor(null);
			httpServer.getAddress();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName())
					.log(Level.SEVERE, null, ex);
		}
		return httpServer;
	}

	public boolean close(HttpServer httpServer) throws IOException {
		httpServer.stop(0);
		System.out.printf("Server Socket Closed Successfully...\n");
		return true;
	}

	static class MyHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			int responseCode_OK = 200;
			String response = "Server is Running....";
			he.sendResponseHeaders(responseCode_OK, response.length());
			OutputStream outputStream = he.getResponseBody();
			outputStream.write(response.getBytes());
			outputStream.close();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
