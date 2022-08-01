package client_gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import entities_general.CustomersPreview;
import entities_general.WorkersPreview;
import entities_users.BranchManager;
import entities_users.Customer;
import entities_users.ShopWorker;
import enums.AccountStatus;
import enums.ShopworkerRole;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BranchManagerEditUserController implements Initializable {

//need to update colomns in gui
	@FXML
	private TableColumn<CustomersPreview, ComboBox<AccountStatus>> accountStatusCol;

	@FXML
	private TableColumn<WorkersPreview, ComboBox<ShopworkerRole>> activityStatusCol;

	@FXML
	private Label editResultCustomerLabel;

	@FXML
	private Button approveCustomerEditBTN;

	@FXML
	private Button approveWorkerEditBTN;

	@FXML
	private Label editResultWorkerLabel;

	@FXML
	private Button backBTN;

	@FXML
	private TableColumn<CustomersPreview, String> balanceCol;

	@FXML
	private TableColumn<WorkersPreview, String> branchIDcol;

	@FXML
	private TableColumn<CustomersPreview, String> customerIDCol;

	@FXML
	private TableColumn<CustomersPreview, String> emailCol;// of customer

	@FXML
	private TableColumn<WorkersPreview, String> firstNameCol;// of worker

	@FXML
	private TableColumn<WorkersPreview, String> lastNameCol;

	@FXML
	private TableColumn<WorkersPreview, String> shopWorkerIDCol;

	@FXML
	private TableView<WorkersPreview> workers;

	@FXML
	private TableView<CustomersPreview> customers;

	@FXML
    private Button info;
	
	@FXML
    private Label timer;

    @FXML
    private Label userRoleLbl;

    @FXML
    private Label welcomeUserNameLbl;
    
    @FXML
    private Label phoneNumberLbl;
    
    @FXML
    private Label branchLbl;
	
    @FXML
    private Label branchManagerNameLbl;
    
    @FXML
    private Label accountStatusLbl;

	String branchID, branchName;

	private ObservableList<WorkersPreview> workersListView = FXCollections.observableArrayList();
	private ObservableList<CustomersPreview> customersListView = FXCollections.observableArrayList();

	@FXML
	void back(ActionEvent event) throws Exception {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerPageController branchPage = new BranchManagerPageController();
		branchPage.start(primaryStage);
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/BranchManagerEditUserPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("edit user's deatails Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ClientController.initalizeUserDetails(branchManagerNameLbl, phoneNumberLbl, accountStatusLbl, welcomeUserNameLbl, userRoleLbl,
				((BranchManager) ClientController.user).toString());

		branchID = ((BranchManager) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(branchID);
		branchLbl.setText(" " + branchName + " (" + branchID + ")");
		
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
				
		/* each cell will show a value of a field from the type we defined on the right
		 * and the object we defined on the left
		 */
		firstNameCol.setCellValueFactory(new PropertyValueFactory<WorkersPreview, String>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<WorkersPreview, String>("lastName"));
		shopWorkerIDCol.setCellValueFactory(new PropertyValueFactory<WorkersPreview, String>("ID"));
		branchIDcol.setCellValueFactory(new PropertyValueFactory<WorkersPreview, String>("branchID"));
		activityStatusCol.setCellValueFactory(
				new PropertyValueFactory<WorkersPreview, ComboBox<ShopworkerRole>>("activityStatusCB"));
		List<ShopWorker> listOfShopWorkers = ClientHandleTransmission.getShopWorkers();
		if (listOfShopWorkers.size() > 0) {
			for (ShopWorker sw : listOfShopWorkers) {
				workersListView.add(new WorkersPreview(sw.getID(), sw.getFirstName(), sw.getLastName(), sw.getEmail(),
						sw.getPhoneNumber(), sw.getAccountStatus(), sw.getIsLoggedIn(), sw.getBranchID(),
						sw.getActivityStatus()));
			}

			workers.setItems(workersListView);// adding to the table view and display
		}
		else {
			editResultWorkerLabel.setText("no workers");
			approveWorkerEditBTN.setDisable(true);
		}

		// accountStatusCol.setCellFactory(

		// myListener=new myListenerStatus(accountStatusCol);
		customerIDCol.setCellValueFactory(new PropertyValueFactory<CustomersPreview, String>("ID"));
		emailCol.setCellValueFactory(new PropertyValueFactory<CustomersPreview, String>("email"));
		balanceCol.setCellValueFactory(new PropertyValueFactory<CustomersPreview, String>("balance"));
		accountStatusCol.setCellValueFactory(
				new PropertyValueFactory<CustomersPreview, ComboBox<AccountStatus>>("accountStatusCB"));
		List<Customer> listOfCustomers = ClientHandleTransmission.getApprovedCustomers();
		if (listOfCustomers.size() > 0) {
			for (Customer customer : listOfCustomers) {
				// ComboBox<AccountStatus> accountStatusDisplay
				customersListView.add(new CustomersPreview(customer.getID(), customer.getFirstName(),
						customer.getLastName(), customer.getEmail(), customer.getPhoneNumber(),
						customer.getAccountStatus(), customer.getIsLoggedIn(), customer.getBalance(),
						customer.getIsNewCustomer(), customer.getCreditCard()));
			}
			customers.setItems(customersListView);
		}
		else {
			editResultCustomerLabel.setText("no customers");
			approveCustomerEditBTN.setDisable(true);
		}
	}

	@FXML
	void approvCustomerEdit(ActionEvent event) {

		if (ClientHandleTransmission.sendEditedCustomersFromBranchManager(customersListView))
			editResultCustomerLabel.setText("customer edit succeeded");
		else
			editResultCustomerLabel.setText("customer edit failed");
	}

	@FXML
	void approvWorkerEdit(ActionEvent event) {
		if (ClientHandleTransmission.sendEditedWorkersFromBranchManager(workersListView))
			editResultWorkerLabel.setText("worker edit succeeded");
		else
			editResultWorkerLabel.setText("worker edit failed");
	}

}
