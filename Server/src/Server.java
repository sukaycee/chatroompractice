import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {

    private final int port;
    private List<ServerWorker> workerList;

    public Server(int port){
        this.port = port;
        workerList = new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            ServerSocket socket = new ServerSocket(port);

            while(true) {
                Socket client = socket.accept();
                ServerWorker worker = new ServerWorker(this, client);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ServerWorker> getWorkerList() {
        return this.workerList;
    }
}
