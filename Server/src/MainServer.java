import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) throws IOException {
        int port = 8818;
        Server server = new Server(port);
        server.start();
    }
}
