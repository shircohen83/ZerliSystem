// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import java.io.IOException;

import communication.ChatIF;
import communication.TransmissionPack;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * @author mor ben haim
 * 
 */
public class ChatClient extends AbstractClient {
	// Instance variables ******

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	public static boolean awaitResponse;
	// Constructors ******

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ******

	/**
	 * This method handles all data that comes in from the server . Then we analyze
	 * the response that come from the server and doing the operation that needed
	 * 
	 * @param msg - Object of TransmissionPack .
	 */
	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		if (msg instanceof TransmissionPack) {
			TransmissionPack tr = (TransmissionPack) msg;
			ClientController.setObj(tr);
			MissionAnalyzeClient.MissionsAnalyzeClient((TransmissionPack) msg);
			clientUI.display(tr.getResponse().toString()); // print the response to the client console
		}
	}

	/**
	 * This method handles all data coming from the UI We sending the data to the
	 * server and waiting to his response
	 * 
	 * @param message The TransmissionPack obj from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {

		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class