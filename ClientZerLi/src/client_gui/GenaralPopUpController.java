package client_gui;

import java.net.URL;
import java.util.ResourceBundle;

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

public class GenaralPopUpController implements Initializable {
	
	    @FXML
	    private Button OKbutton;
	    @FXML
	    private Label labelOfPopUp;

	    private String headLine; 

	    private String mainLabel;
	    

	@FXML
	void okPressed(ActionEvent event) {
	 ((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
	}
	
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/GeneralPopUp.fxml"));

		Scene scene = new Scene(root);

		primaryStage.setTitle(popMessageHandler.getTitle());
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		popMessageHandler.getLabelText(labelOfPopUp);
	}
	
	
	
    public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLineStr) {
		this.headLine = headLineStr;
	}
	public String getMainLabel() {
		return mainLabel;
	}
	public void setMainLabel(String mainLabel) {
		this.mainLabel = mainLabel;
	}

}
















