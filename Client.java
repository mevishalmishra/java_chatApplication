import java.io.*;
import java.net.*;

public class Client {
    Socket socket;

    BufferedReader bReader;
    PrintWriter pWriter;

    public Client() {

        try {
            System.out.println("Sending request to server at " + 8080);
            socket = new Socket("127.0.0.1", 8080);
            System.out.println("Connection to server is successful.");

            bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // concept of multithreading is used to run both the things simultaneously
    public void startReading() {
        // this will continue reading data

        // runnable is multihthreading concept using lambda expresssion of single line
        // arrow

        Runnable rInstanceOne = () -> {
            System.out.println("Reader started...");
            while (true) {
                try {
                    String msg = bReader.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat.");
                        break;
                    }

                    // write msg on console, as it is sent by client

                    System.out.println("Server : " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(rInstanceOne).start();
    }

    public void startWriting() {
        // this will continue writing

        // runnable is multihthreading concept using lambda expresssion of single line
        // arrow

        Runnable rInstanceTwo = () -> {
            System.out.println("Writer started...");

            // use while loop to read again and again
            while (true) {
                try {

                    // before writing we need data from user of say user type on console we take the
                    // data and write

                    BufferedReader breaderTwo = new BufferedReader(new InputStreamReader(System.in));
                    String content = breaderTwo.readLine();
                    pWriter.println(content);
                    pWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new Thread(rInstanceTwo).start();
    }

    public static void main(String[] args) {
        System.out.println("Client started...");
        new Client();
    }
}
