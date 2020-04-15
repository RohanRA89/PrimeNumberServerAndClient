package application;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
  // I/O streams
  DataOutputStream toServer = null;
  DataInputStream fromServer = null;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Pane to hold the label and text field
    BorderPane paneForTextField = new BorderPane();
    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
    paneForTextField.setStyle("-fx-border-color: green");
    paneForTextField.setLeft(new Label("Enter number to test as Prime Number: "));
    
    TextField tf = new TextField();
    tf.setAlignment(Pos.BOTTOM_RIGHT);
    paneForTextField.setCenter(tf);
    
    BorderPane mainPane = new BorderPane();
    // Text area to display contents
    TextArea ta = new TextArea();
    mainPane.setCenter(new ScrollPane(ta));
    mainPane.setTop(paneForTextField);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    //No buttons so action e is enter key on Client
    tf.setOnAction(e -> {
      try {
        // Get the number from the textfield
        int numberToTest = Integer.parseInt(tf.getText().trim());
  
        // Send the number to the server
        toServer.writeInt(numberToTest);
        toServer.flush();
  
        // Get area from the server
        //Using Boolean value as method in Server class returns a boolean value
        boolean isPrime = fromServer.readBoolean();
  
        // Display to the text area
        ta.appendText("The number sent is " + numberToTest + "\n");
        ta.appendText("Is " + numberToTest + " a prime number:  "
          + isPrime + '\n');
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    });
  
    try {
      // Create a socket to connect to the server
      //Had to switch this from 8080 as it would run in ECLIPSE but not when used as a server value 
      //when executing as a .jar file.
      Socket socket = new Socket("localhost", 6000);


      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (IOException ex) {
      ta.appendText(ex.toString() + '\n');
    }
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}

