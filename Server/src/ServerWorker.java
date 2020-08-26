import java.awt.event.InputEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerWorker extends Thread {
    private final Socket worker;
    private final Server server;
    private List<ServerWorker> list;
    public String login;

    public ServerWorker(Server server, Socket worker) throws IOException {
        this.worker = worker;
        this.server = server;
        this.list = server.getWorkerList();
        login = null;
    }

    @Override
    public void run(){
        try {
            runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        InputStream input = worker.getInputStream();
        OutputStream output = worker.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;

        while ( (line = reader.readLine()) != null) {
            String[] array = line.split("\\s+");

            if (array != null && array.length > 0) {

                String first = array[0];

                if ("quit".equalsIgnoreCase(first)) {
                    break;
                }
                else if (login == null && "login".equalsIgnoreCase(first) && array.length == 3){
                    handleLogin(array);
                }
                else if (login != null && "msg".equalsIgnoreCase(first) ){

                    if (list.size() == 1) {
                        output.write("There is no one else in this Chat Room\n ".getBytes());
                    }else{
                        String[] newArray = line.split(" ", 2);
                        handleMsg(newArray);
                    }
                }else{
                    String msg = "Invalid msg : \"" + line + "\"\n ";
                    output.write(msg.getBytes());
                }
            }
        }
        worker.close();
    }

    private void handleMsg(String[] newArray) throws IOException {
        for (ServerWorker worker : list) {
            OutputStream output = worker.worker.getOutputStream();
            output.write((login + " : " + newArray[1] + " ").getBytes());
        }
    }

    private void handleLogin(String[] array) throws IOException {
        String log = array[1];
        String pw = array[2];

        if ( (log.equals("kc") && pw.equals("kc")) || (log.equals("guest") && pw.equals("guest")) || (log.equals("jepai") && pw.equals("jepai")) ) {
            this.login = log;

            for (ServerWorker worker : list) {
                OutputStream output = worker.worker.getOutputStream();
                output.write((this.login + " has just logged in! \n").getBytes());
            }
        }else{
            OutputStream output = worker.getOutputStream();
            output.write("Invalid login! ".getBytes());
        }
    }
}