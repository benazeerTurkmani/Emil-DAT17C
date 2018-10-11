package MandatoryAssignment1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String [] args) throws IOException {
        System.out.println("Client");
        int port;
        String ip;
        String userName;
        OutputStream outToServer;
        InputStream inFromServer;
        boolean UNaccept = true;




        System.out.println("starting TCPClient main");
        Scanner input = new Scanner(System.in);


            System.out.println("Write server ip: ");
            ip = input.next();
            System.out.println("Write port:");
            port = input.nextInt();
            do{
            System.out.println("Write Username:");
            userName = input.next();
            if(true) {
                System.out.println("trying to connect");

                String joinCMD = "JOIN " + userName + ", " + ip + ":" + port;

                String finalIp = ip;
                Socket clientSocket = new Socket(finalIp, port);
                outToServer = clientSocket.getOutputStream();
                inFromServer = clientSocket.getInputStream();
                byte[] join = joinCMD.getBytes();
                byte[] accept = new byte[1024];
                outToServer.write(join);
                inFromServer.read(accept);
                String userNameAccept = new String(accept);
                userNameAccept.trim();
                if (userNameAccept.equals("J_OK"))UNaccept = false;
            }
        }while(UNaccept);
        OutputStream finalOutToServer = outToServer;
        Thread send = new Thread(()->{
            try {
                boolean test = true;
                do {
                    Scanner clientInput = new Scanner(System.in);
                    String inFromUser;
                    inFromUser = clientInput.nextLine();
                        finalOutToServer.write(inFromUser.getBytes());

                    if(inFromUser.equalsIgnoreCase("quit"))test = false;
                }while(test);
                Thread.currentThread().interrupt();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        InputStream finalInFromServer = inFromServer;
        Thread receive = new Thread(()->{
            try{
                boolean test = true;
                do{
                    byte[] dataIn = new byte[1024];
                    finalInFromServer.read(dataIn);
                    String msgIn = new String(dataIn);
                    msgIn = msgIn.trim();


                    System.out.println(msgIn);
                    if(msgIn.equalsIgnoreCase("QUIT"))test=false;
                }while(test);
                Thread.currentThread().interrupt();
            }catch (Exception e){

            }
        });

        OutputStream finalOutToServer1 = outToServer;
        Thread IMAV = new Thread(()->{
           while(send.isAlive()){
               try {
                   Thread.sleep(10000);
                   finalOutToServer1.write("IMAV".getBytes());
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
            Thread.currentThread().interrupt();
        });

        send.start();
        receive.start();
        IMAV.start();
        try {
            send.join();
            receive.join();
            IMAV.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }







    }
}
