package client_gui;

import client.ClientHandleTransmission;
import client.MissionAnalyzeClient;
import client.zerliClientListeners;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** In this class we creating the connect to server controller , we handling with all the screen operations
 * 
 *
 */
public class ConnectToServerController {
	
	@FXML
	private Label ConnectStatusLabel;

	@FXML
	private TextField IpTxt;
	@FXML
	private Button ConfirmDetails;

	@FXML
	private TextField PortTxt;

	/** In this method , when the button confirm has been clicked we , connecting the user into the
	 * client Gui (making the communication between the server and the client), sending the logic into
	 * the ClientHandleTrasmission.CONNECT_TO_SERVER
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void ConfirmClick(ActionEvent event) throws Exception {
		
			ClientHandleTransmission.CONNECT_TO_SERVER(event, IpTxt.getText(), PortTxt.getText());
	}
		/**in this method we loading the screen ConnectToServer
		 * 
		 * @param primaryStage
		 * @throws Exception
		 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ConnectToServer.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("ZerLi Connect To Server");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		MissionAnalyzeClient.addClientListener(new zerliClientListeners() {
			@Override
			public void ipConfirmedForClient() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void ipNotConfirmedForClient() {
				//TBD: open pop up window
			
			}
		});
		
	}

}
