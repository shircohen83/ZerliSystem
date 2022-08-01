package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientHandleTransmission;
import client.OrderHandleController;
import client.popMessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GenaralPopScroolBarUpController implements Initializable {

	/**	Pop up with Scroll Bar Massage 
	 *  @author Almog-Madar
	 */
	
	
	@FXML
	private Button OKbutton;

	@FXML
	private Label labelOfPopUp;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/GeneralPopScroolBarUp.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setTitle(popMessageHandler.getTitle());
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.show();

	}

	@FXML
	void okPressed(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		OrderHandleController.setCloseEvent(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		popMessageHandler.getLabelText(labelOfPopUp);

	}

}
