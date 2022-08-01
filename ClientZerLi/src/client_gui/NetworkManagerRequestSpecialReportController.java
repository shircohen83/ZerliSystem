package client_gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NetworkManagerRequestSpecialReportController {

    @FXML
    private Button BackBtn;

    @FXML
    private TextField reportTypeTxt;

    @FXML
    private ComboBox<?> selectBranchComboBox;

    @FXML
    private Button submitBtn;

    /**
     * 
     * @param stage
     * @throws IOException
     */
    public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/NetworkManagerRequestSpecialReport.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Network Manager Menu");
		stage.setScene(scene);
		stage.show();
	}
    
    /**
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void Back(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		NetworkManagerPageController networkManagerPageController = new NetworkManagerPageController();
		networkManagerPageController.start(primaryStage);
    }

    @FXML
    void Submit(ActionEvent event) {

    }

}
