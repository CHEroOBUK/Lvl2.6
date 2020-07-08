package GB.Part2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleClient{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    final String IP_ADDRESS = "localhost";
    final int PORT = 8765;
    Scanner scanner = new Scanner(System.in);

    public ConsoleClient() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str;
                            str = in.readUTF();
                            if (str.equalsIgnoreCase("/serverClosed")) {
                                System.out.println(str + "\n");
                                break;
                            }
                            System.out.println("Сообщение от сервера: " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Можете писать сообщения серверу:");
                        while (true) {
                            String msg = scanner.nextLine();
                            out.writeUTF(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
