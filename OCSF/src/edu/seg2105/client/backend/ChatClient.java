// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  private String loginID;


  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */



  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    

  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
      try
      {
          if (message.startsWith("#")) {
              handleCommand(message);  // Handle commands like #quit or #logoff
          } else {
              sendToServer(message); 
          }
      }
      catch(IOException e)
      {
          clientUI.display("Could not send message to server. Terminating client.");
          quit();
      }
  }
  
  

  private void handleCommand(String command){
    if(command.equals("#quit")){
      quit();

    }
    else if(command.equals("#logoff")){
      try {
        if(isConnected()){

        
        closeConnection();}
        else{
          clientUI.display("error, no cient logged in" );

        }
      } catch (IOException e) {
        clientUI.display("error" );
      }
    }
    else if(command.equals("#sethost")){
      if(!isConnected()){
        String newHost = command.substring(9).trim();
        clientUI.display("new host assigned");
      setHost(newHost);}
      else{
        clientUI.display("client is still connected");
      }

    }
    else if(command.equals("#setport")){
      if(!isConnected()){
        int newport = Integer.parseInt(command.substring(9).trim());
        clientUI.display(("new port assigned" ));
      setPort(newport);}
      {
        clientUI.display("client is still connected");
      }
    }
    else if(command.equals("#login")){
      if(!isConnected()){
        try {
          openConnection();
          clientUI.display("login successfull");
        } catch (IOException e) {
          clientUI.display("error" );

        }
      }
      else{
        clientUI.display("Error");
      }
        }
    else if(command.equals("#gethost")){
      clientUI.display(getHost());
    }
    else if(command.equals("#getport")){
      int integer_value = getPort();
      clientUI.display(String.valueOf(integer_value));
        }
    


  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  	/**
	 * Impliments the hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
    clientUI.display("The server has Shut down");
    quit();
	}

  	/**
	 * Impliment Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
    clientUI.display("Connection Closed");
	}

  @Override
protected void connectionEstablished() {
    try {
        sendToServer("#login " + loginID); // Send #login command with login ID
    } catch (IOException e) {
        clientUI.display("Error: Could not send login message to server.");
    }
}


  
}
//End of ChatClient class
