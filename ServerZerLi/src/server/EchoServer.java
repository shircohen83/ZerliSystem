package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
/** new dvir Bublil 12321 command*/
import java.io.IOException;

import DataBase.DBController;
import communication.TransmissionPack;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server_gui.ServerScreenController;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 */
public class EchoServer extends AbstractServer {

	// Class variables *******

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5556;

	// Constructors ******

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ******

	/**
	 * This method handles any operations(by sending TresmissionPack) received from
	 * the client. using the DBcontroller.ParsingToDate to analyze and execute the
	 * operation
	 *
	 * @param msg    The TrensmissionPack obj received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		TransmissionPack mission1 = (TransmissionPack) msg;

		DBController.parsingToData(mission1);

		try {

			client.sendToClient(mission1);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {

		ServerScreenController.SetMsg("Server listening for connections on port " + getPort());// adding the msg into
																								// his place on the
																								// server screen
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
//		System.out.println("Server has stopped listening for connections.");
		ServerScreenController.SetMsg("Server has stopped listening for connections.");// adding the msg into his place
																						// on the server screen
	}

}
