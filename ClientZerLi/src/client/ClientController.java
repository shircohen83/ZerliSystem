// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;

import java.io.IOException;

import communication.ChatIF;
import communication.TransmissionPack;
import entities_users.User;
import javafx.scene.control.Label;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 * @author mor ben haim
 */

public class ClientController implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;
	public static TransmissionPack obj; // we creating static obj that will handle the
										// response/mission/information(TransmissionPack)
	// Instance variables **********************************************
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;
	
	/**general user for all type of client in zerLi*/
	public static User user;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
			
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void accept(Object str) {
		client.handleMessageFromClientUI(str);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}

	/**
	 * In this method returning the TransmissionPack obj
	 *
	 */
	public TransmissionPack getObj() {
		return obj;
	}

	/**
	 * In this method we setting the TransmissonPack 'value'
	 * 
	 * @param obj
	 */
	public static void setObj(TransmissionPack obj) {
		ClientController.obj = obj;
	}
    public static void initalizeUserDetails(Label employeeName,Label phoneNumber, Label accountStatus,Label entryGreeting,Label employeeType,String user) {
		
		if(entryGreeting!=null) {
			entryGreeting.setText(entryGreeting.getText()+" "+ClientController.user.getFirstName()+" !");
		}
	
		employeeName.setText(ClientController.user.getFirstName()+" "+ClientController.user.getLastName());
		phoneNumber.setText(ClientController.user.getPhoneNumber());
		accountStatus.setText(ClientController.user.getAccountStatus().name());
		employeeType.setText(user);
		
		
	}
}
//End of ConsoleChat class
