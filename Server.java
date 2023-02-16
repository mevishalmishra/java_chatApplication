import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

class Server extends JFrame {

    ServerSocket server;
    Socket socket;

    // 2 variables to read and write

    BufferedReader bReader;
    PrintWriter pWriter;

    // component declaration for gui-swing
    private JFrame chatServerFrame = new JFrame("Client");
    private JLabel chatSeverHeading = new JLabel("Client");
    private JTextArea chatServerTextArea = new JTextArea();
    private JTextField chatServerMessage = new JTextField();
    private Font chatServerFont = new Font("Roboto", Font.PLAIN, 20);

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
            createServerSideChatGUI();
            handleChatEvents();
            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // handle chat events
    private void handleChatEvents() {
        chatServerMessage.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("ok");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("ok");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.getKeyCode() == 10) {
                    String messageToSend = chatServerMessage.getText();
                    System.out.println(messageToSend);
                    chatServerTextArea.append("You: " + messageToSend + "\n");
                    pWriter.println(messageToSend);
                    pWriter.flush();
                    chatServerMessage.setText("");
                    chatServerMessage.requestFocus();
                }
            }
        });

    }

    // method to call client chat gui
    private void createServerSideChatGUI() {

        // this.chatClientHeading = new JLabel("Client Chat [END]");
        this.setTitle("Server Chat [END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        // set font
        chatServerFrame.setFont(chatServerFont);
        chatSeverHeading.setFont(chatServerFont);
        chatServerMessage.setFont(chatServerFont);
        chatServerTextArea.setFont(chatServerFont);

        // set img icon
        ImageIcon imageIcon = new ImageIcon("logo.png");
        Image scaledImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        chatSeverHeading.setIcon(new ImageIcon(scaledImage));

        // // set headingtext to below of icon
        chatSeverHeading.setHorizontalTextPosition(SwingConstants.CENTER);
        chatSeverHeading.setVerticalTextPosition(SwingConstants.BOTTOM);

        // layout
        this.setLayout(new BorderLayout());

        // adding components to frame
        this.add(chatSeverHeading, BorderLayout.NORTH);
        this.add(chatServerTextArea, BorderLayout.CENTER);
        this.add(chatServerMessage, BorderLayout.SOUTH);

        // alignement
        chatSeverHeading.setHorizontalAlignment(SwingConstants.CENTER);
        chatSeverHeading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
                        JOptionPane.showMessageDialog(this, "Client terminated the chat.");
                        break;
                    }

                    // write msg on console, as it is sent by client

                    chatServerTextArea.append("Client says: " + msg + "\n");
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