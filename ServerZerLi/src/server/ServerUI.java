package server;



import java.io.IOException;
import java.util.List;
import java.util.Timer;

import DataBase.DBController;
import DataBase.TimerRunner;
import javafx.application.Application;
import javafx.stage.Stage;
import server_gui.ServerScreenController;


/** In this class we launch the server , connecting into the db, and also listening to specific port.
 *
 */

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5556;
	public static EchoServer sv;
	static DBController DBC;
	

	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	/** in this method we loading the server screen
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
						  		
		ServerScreenController server = new ServerScreenController(); 
		server.start(primaryStage);
		
	}
	
	/** in this method we running the server (listening to specific port and connecting to the db)
	 * @param p - the port
	 * @param data - the host information
	 * @return if its success or not 
	 */
	public static boolean runServer(String p,List <String> data)
	{
		 int port = 0; //Port to listen on
		if( DBController.connectToDB(data)) { 
	        try
	        {
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
//	        	System.out.println("ERROR - Could not connect!");
	        	ServerScreenController.SetMsg("ERROR - Could not connect!");
	        	return false;
	        }
	        System.out.println("start run aoutomated");
	    	 Timer timer=new Timer();
	    	 TimerRunner runner=new TimerRunner();
	    	 timer.scheduleAtFixedRate(runner, 0, 600000);
	         sv = new EchoServer(port);
	        
	        try 
	        {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
//	          System.out.println("ERROR - Could not listen for clients!");
	          ServerScreenController.SetMsg("ERROR - Could not listen for clients!");
	          	return false;
	        }
	      return true;
		}
		return false;
	}

	/**In this method we stop the server from listening and closing the port.
	 * 
	 */
	public static void stopServer() {
		sv.stopListening(); //stopping listening to the port 
		try {
			sv.close(); //closing the port.
		} catch (IOException e) { 
		
			e.printStackTrace();
		}
	}
   

}
