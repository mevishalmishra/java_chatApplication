import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;

    // 2 variables to read and write

    BufferedReader bReader;
    PrintWriter pWriter;

    public Server() {
        try {
            int PORT = 8080;

            server = new ServerSocket(PORT);
            System.out.println("Server is up, and ready to accept connection on " + PORT + "port");
            System.out.println("Waiting...");

            // till now server is initialised and waiting to accept connection now it should
            // accept the connection on PORT
            socket = server.accept(); // accepts the connection of client and returns the object of client only

            // socket gives input and output stream to read and write data

            /*
             * bReader takes the input stream to read the data, socket.getInputStream
             * sends data in bytes inputsteamreader converts into char then makes a buffer
             */

            bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
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
                        System.out.println("Client terminated the chat.");
                        break;
                    }

                    // write msg on console, as it is sent by client

                    System.out.println("Client : " + msg);
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
        System.out.println("Server is going to start....");
        new Server();

    }
}