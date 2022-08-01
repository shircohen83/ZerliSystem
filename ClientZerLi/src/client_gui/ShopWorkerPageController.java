package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.SurveyHandle;
import communication.Mission;
import communication.TransmissionPack;
import entities_users.DeliveryAgent;
import entities_users.ShopWorker;
import enums.AccountStatus;
import enums.Branches;
import enums.ShopworkerRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * class discrition:
 * this class repersent the shop worker main page controller
 * @author Mor Ben Haim
 *
 */
public class ShopWorkerPageController implements Initializable{
	@FXML
	private Button logOutBtn;
	@FXML
	private Label employeeName;
	@FXML
	private Label entryGreeting;


	@FXML
	private Label accountStatus;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label employeeType;

    @FXML
    private Label branchName;

    @FXML
    private Button branchSurveyBtn;

    @FXML
    private Button customerServiceBtn;

    @FXML
    private Button SalesSurvey;

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ShopWorkerPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Shop Worker Menu");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});

	}

	@FXML
	void logOut(ActionEvent event) throws Exception {
		TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		LoginController login = new LoginController();
		login.start(primaryStage);
	}

  /**
   * survey for customer service
   * @param event
   * @throws Exception
   */
    @FXML
    void CustomerServiceSurveyBtn(ActionEvent event) throws Exception {
    	SurveyHandle.setTopic("Customer Service");
    	SurveyHandle.setTargetAudience("Customer");
    	loadSurveyScreen(event);
    }


    /**
     * survey for customer who buy in Some Sales
     * @param event
     * @throws Exception
     */
    @FXML
    void SalsesSurveyBtn(ActionEvent event) throws Exception {
    	SurveyHandle.setTopic("Sales");
    	SurveyHandle.setTargetAudience("Buy flower On Sales");
    	
    	loadSurveyScreen(event);
    }
    
    /**
     * survey for customer who buy in specific branch
     * @param event
     * @throws Exception
     */
    @FXML
    void BranchSurveyBtn(ActionEvent event) throws Exception {
    	SurveyHandle.setTopic("Specific Branch");
    	SurveyHandle.setTargetAudience("Buy flower On this branch");
    	loadSurveyScreen(event);
    }
    /**
     * load the specific survey screen
     * @param event
     * @throws Exception
     */
	private void loadSurveyScreen(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ShopWorkerSurveyPageController shopWorkerSurveyPageController = new ShopWorkerSurveyPageController();
		shopWorkerSurveyPageController.start(primaryStage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		branchName.setText(ClientHandleTransmission.getBranchName(((ShopWorker) ClientController.user).getBranchID())+"("+((ShopWorker) ClientController.user).getBranchID()+")");
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((ShopWorker) ClientController.user).toString());

		if (((ShopWorker) ClientController.user).getActivityStatus() == ShopworkerRole.GENERAL) {
			branchSurveyBtn.setDisable(true);
			customerServiceBtn.setDisable(true);
			SalesSurvey.setDisable(true);
		}
	}
}
