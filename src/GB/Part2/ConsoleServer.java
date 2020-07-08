package GB.Part2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleServer {
    ServerSocket server = null;
    Socket socket = null;
    Scanner scanner = new Scanner(System.in);

    public ConsoleServer() {

        try {
            server = new ServerSocket(8765);
            System.out.println("Сервер запущен");

            socket = server.accept();
            System.out.println("Клиент подключился");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str;
                            str = in.readUTF();

                            if (str.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                System.out.println("Клиент принудительно разорвал связь...");
                                break;
                            }
                            System.out.println("Сообщение от клиента: " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            server.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                        //out.writeUTF("echo " + str);
                    }
                }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        System.out.println("Можете писать сообщения клиенту:");
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
