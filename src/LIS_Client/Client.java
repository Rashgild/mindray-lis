package LIS_Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by rkurbanov on 27.02.2017.
 */
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            new Client();
            break;
        }
    }

    //const
    public Client() {
        Scanner scan = new Scanner(System.in);
      //String ip ="192.168.7.19"; //scan.nextLine();
       String ip ="127.0.0.1"; //scan.nextLine();
        try {
            socket = new Socket(ip, 9876);
            in =  new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(
            socket.getOutputStream(), StandardCharsets.UTF_8), true);
            Resender resend = new Resender();
            resend.start();
            String str = "";

            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }


    private class Resender extends Thread {
        private boolean stoped;
        public void setStop() {
            stoped = true;
        }
        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }
}
