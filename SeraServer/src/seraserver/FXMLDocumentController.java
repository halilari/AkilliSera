/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seraserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;





/**
 *
 * @author Halil
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    
  
    @FXML
    private TextArea msg_area;
    private TextField msg_text;
  
    ServerSocket serverSocket;
    String message = "";

    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.setDaemon(true); //terminate the thread when program end
        socketServerThread.start();
     
        
    }    

     private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                    .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                    .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                            + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return ip;
    }

 private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;
        int count = 0;

        @Override
        public void run() {
            try {
                Socket socket = null;
                
                serverSocket = new ServerSocket(SocketServerPORT);
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                       System.out.println("I'm waiting here: "
                            + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    count++;
                    
                    //Start another thread 
                    //to prevent blocked by empty dataInputStream
                    Thread acceptedThread = new Thread(
                        new ServerSocketAcceptedThread(socket, count));
                    acceptedThread.setDaemon(true); //terminate the thread when program end
                    acceptedThread.start();

                }
            } catch (IOException ex) {
                Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }
private class ServerSocketAcceptedThread extends Thread {

        Socket socket = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;
        int count;

        ServerSocketAcceptedThread(Socket s, int c) {
            socket = s;
            count = c;
        }

        @Override
        public void run() {
            try {
                dataInputStream = new DataInputStream(
                    socket.getInputStream());
                dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());

                //If dataInputStream empty, 
                //this thread will be blocked by readUTF(),
                //but not the others
                String messageFromClient = dataInputStream.readUTF();
                
                message += "#" + count +":"
                    + "Kullanıcdan Gelen: " + messageFromClient + "\n";
                
                Platform.runLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        msg_area.setText(message.toString().trim());
                    }
                });
                
                if (true) {
                    
                    String msgReply = count + ": " + msg_text.getText();
                    dataOutputStream.writeUTF(msgReply);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SeraServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

   
}
