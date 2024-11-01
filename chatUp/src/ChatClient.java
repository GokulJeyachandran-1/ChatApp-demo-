import java.io.*;
import java.net.*;

public class ChatClient {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public ChatClient(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Thread to listen for incoming messages
        new Thread(new IncomingMessagesHandler()).start();

        // Read messages from the console and send them to the server
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = consoleInput.readLine()) != null) {
            out.println(message);
        }
    }

    private class IncomingMessagesHandler implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Connection error: " + e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Enter the server address: ");
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String serverAddress = consoleInput.readLine();

        new ChatClient(serverAddress);
    }
}
