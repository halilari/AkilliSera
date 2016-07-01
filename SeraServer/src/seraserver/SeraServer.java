/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seraserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author Halil
 */
public class SeraServer extends Application {
         public Socket s;
     public DataInputStream din;
 public DataOutputStream dout;
      
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.setResizable(false);
        stage.setWidth(850);
        stage.setHeight(650);
        stage.show();
           
        
  
     
           
           
                
    
        
      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
    
   
        launch(args);
      
      
        
        
        
    }
    
}
