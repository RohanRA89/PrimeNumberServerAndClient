package application;
	
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Text area for displaying contents
    TextArea ta = new TextArea();

    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("Server"); 
    primaryStage.setScene(scene); 
    primaryStage.show(); 
    
    new Thread( () -> {
      try {
        // Create a server socket
    	//switched from 8080 to 6000 just because the .jar file execution stated
    	//Server already in use by JVM. Will upload a screen shot showing this error.
        ServerSocket serverSocket = new ServerSocket(6000);
        Platform.runLater(() ->
          ta.appendText("Server started at " + new Date() + '\n'));
  
        // Listen for a connection request
        Socket socket = serverSocket.accept();
  
        // Create data input and output streams
        DataInputStream inputFromClient = new DataInputStream(
          socket.getInputStream());
        DataOutputStream outputToClient = new DataOutputStream(
          socket.getOutputStream());
  
        while (true) {
          // Receive number
         int numberToTest  = inputFromClient.readInt();
  
          // use method to compute if number is prime or not
          boolean isPrime = isPrimeNumberTest(numberToTest);
  
          // Send result
          outputToClient.writeBoolean(isPrime);
  
          Platform.runLater(() -> {
            ta.appendText("Number recieved from client " 
              + numberToTest + '\n');
            ta.appendText("Prime number: " + isPrime + '\n'); 
          });
        }
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
  
  //method used to determine if a number is prime
  public static boolean isPrimeNumberTest(int inputNumber) {
	  //if there is no remainder for the prime number it is likely it is not prime
	  int remainder;
	  boolean isPrime = true;
	  int numberToCheck = inputNumber;
	  
	  for(int i = 2; i<=numberToCheck/2;i++) {
		  remainder = numberToCheck%i;
		  if(remainder == 0) {
			  isPrime = false;
			  break;
		  }
	  }
	  if(isPrime) {
		  return true;
	  }
	  else {
	  return false;
	  }
  }
}
