import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Client extends JFrame {

    // chat
    Socket socket;
    BufferedReader bReader;
    PrintWriter pWriter;

    // component declaration for gui-swing
    private JFrame chatClientFrame = new JFrame("Client");
    private JLabel chatClientHeading = new JLabel("Client");
    private JTextArea chatClientTextArea = new JTextArea();
    private JTextField chatClientMessage = new JTextField();
    private Font chatClientFont = new Font("Roboto", Font.PLAIN, 20);

    public Client() {

        try {
            System.out.println("Sending request to server at " + 8080);
            socket = new Socket("127.0.0.1", 8080);
            System.out.println("Connection to server is successful.");

            bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pWriter = new PrintWriter(socket.getOutputStream());
            createCLientSideChatGUI();
            handleChatEvents();
            startReading();
            startWriting();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // handle chat events
    private void handleChatEvents() {
        chatClientMessage.addKeyListener(new KeyListener() {

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
                    String messageToSend = chatClientMessage.getText();
                    System.out.println(messageToSend);
                    chatClientTextArea.append("You: " + messageToSend + "\n");
                    pWriter.println(messageToSend);
                    pWriter.flush();
                    chatClientMessage.setText("");
                    chatClientMessage.requestFocus();
                }
            }
        });

    }

    // method to call client chat gui
    private void createCLientSideChatGUI() {

        // this.chatClientHeading = new JLabel("Client Chat [END]");
        this.setTitle("Client Chat [END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        // set font
        chatClientFrame.setFont(chatClientFont);
        chatClientHeading.setFont(chatClientFont);
        chatClientMessage.setFont(chatClientFont);
        chatClientTextArea.setFont(chatClientFont);

        // set img icon
        ImageIcon imageIcon = new ImageIcon("logo.png");
        Image scaledImage = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        chatClientHeading.setIcon(new ImageIcon(scaledImage));

        // // set headingtext to below of icon
        chatClientHeading.setHorizontalTextPosition(SwingConstants.CENTER);
        chatClientHeading.setVerticalTextPosition(SwingConstants.BOTTOM);

        // layout
        this.setLayout(new BorderLayout());

        // adding components to frame
        this.add(chatClientHeading, BorderLayout.NORTH);
        this.add(chatClientTextArea, BorderLayout.CENTER);
        this.add(chatClientMessage, BorderLayout.SOUTH);

        // alignement
        chatClientHeading.setHorizontalAlignment(SwingConstants.CENTER);
        chatClientHeading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

                        JOptionPane.showMessageDialog(this, "Server terminated the chat.");
                        chatClientMessage.setEnabled(false);
                        break;
                    }

                    // write msg on console, as it is sent by client

                    // System.out.println("Server : " + msg);

                    chatClientTextArea.append("Server says: " + msg + "\n");
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
