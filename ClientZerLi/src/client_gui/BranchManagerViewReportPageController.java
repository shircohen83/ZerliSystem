package client_gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.ReportHandleController;
import client.popMessageHandler;
import communication.TransmissionPack;
import entities_reports.Report;
import entities_users.BranchManager;
import entities_users.ShopWorker;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * 
 * @author User
 *
 */
public class BranchManagerViewReportPageController implements Initializable {

	@FXML
	private Label MonthlyMonthLabel;

	@FXML
	private CheckBox MonthlyReportButton;

	@FXML
	private Label MonthlyYearLabel;

	@FXML
	private Label PickReportLabel;

	@FXML
	private CheckBox QuartelyReportButton;

	@FXML
	private Label QuaterlyLabel;

	@FXML
	private Label QuaterlyYearLabel;

	@FXML
	private Button BackBtn;

	@FXML
	private Button ViewButton;

	@FXML
	private ComboBox<String> pickMonthForMonthlyCB;

	@FXML
	private ComboBox<String> pickQuarterCB;

	@FXML
	private ComboBox<String> pickReportTypeForMonthlyCB;

	@FXML
	private ComboBox<String> pickYearForMonthlyCB;

	@FXML
	private ComboBox<String> pickYearForQuarterCB;
	
    @FXML
    private Label reportNotFoundLabel;
    @FXML
    private Label timer;

    @FXML
    private Label phoneNumber;
    @FXML
    private Label accountStatus;

    @FXML
    private Label role;
    @FXML
    private Label branch;

    @FXML
    private Label userName;
    
	private ObservableList<String> reportTypeList;
	private ObservableList<String> monthlyMonthList;
	private ObservableList<String> monthlyYearList;

	private ObservableList<String> quarterlyQuarterList;

	@FXML
	void Back(ActionEvent event) throws Exception {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerPageController branchPager = new BranchManagerPageController();
		branchPager.start(primaryStage);

	}
	
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/BranchManagerViewReportPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("View Report Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = addingTimer();
		time.start();
		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, new Label(), role,
				((BranchManager) ClientController.user).toString());
		String branchName=ClientHandleTransmission.getBranchName(((BranchManager) ClientController.user).getBranchID().toString());
		if(branchName!=null)
		branch.setText(branchName+" ("+((BranchManager) ClientController.user).getBranchID().toString()+")");
		
		pickMonthForMonthlyCB.setDisable(true);
		pickReportTypeForMonthlyCB.setDisable(true);
		pickYearForMonthlyCB.setDisable(true);
		pickQuarterCB.setDisable(true);
		pickYearForQuarterCB.setDisable(true);
		MonthlyMonthLabel.setDisable(true);
		MonthlyYearLabel.setDisable(true);
		PickReportLabel.setDisable(true);
		QuaterlyLabel.setDisable(true);
		QuaterlyYearLabel.setDisable(true);

		reportTypeList = FXCollections.observableArrayList("Income", "Orders");
		pickReportTypeForMonthlyCB.setItems(reportTypeList);

		monthlyMonthList = FXCollections.observableArrayList();
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				monthlyMonthList.add("0" + i);
			else
				monthlyMonthList.add("" + i);
		}
		pickMonthForMonthlyCB.setItems(monthlyMonthList);

		quarterlyQuarterList = FXCollections.observableArrayList("1", "2", "3", "4");
		pickQuarterCB.setItems(quarterlyQuarterList);
		List<String> monthlyYear = ClientHandleTransmission.getYearsForComboBox("MONTHLY", "reports");
		if (monthlyYear.size() > 0) {
			monthlyYearList = FXCollections.observableArrayList(monthlyYear);
		} else {
			monthlyYearList = FXCollections.observableArrayList();
		}
		pickYearForQuarterCB.setItems(monthlyYearList);
		pickYearForMonthlyCB.setItems(monthlyYearList);

	}

	// this action will hide & show the specific buttons for the MonthlyReport
	public void showAction1(ActionEvent event) {
		if (MonthlyReportButton.isSelected() && !QuartelyReportButton.isSelected()) {
			pickMonthForMonthlyCB.setDisable(false);
			pickReportTypeForMonthlyCB.setDisable(false);
			pickYearForMonthlyCB.setDisable(false);
			MonthlyMonthLabel.setDisable(false);
			MonthlyYearLabel.setDisable(false);
			PickReportLabel.setDisable(false);
			QuartelyReportButton.setDisable(true);

		} else {
			QuartelyReportButton.setDisable(false);
			pickMonthForMonthlyCB.setDisable(true);
			pickReportTypeForMonthlyCB.setDisable(true);
			pickYearForMonthlyCB.setDisable(true);
			MonthlyMonthLabel.setDisable(true);
			MonthlyYearLabel.setDisable(true);
			PickReportLabel.setDisable(true);

		}

	}

	// this action will hide & show the specific buttons for the QuartelyReport
	public void showAction2(ActionEvent event) {
		if (QuartelyReportButton.isSelected() && !MonthlyReportButton.isSelected()) {
			pickQuarterCB.setDisable(false);
			pickYearForQuarterCB.setDisable(false);
			QuaterlyLabel.setDisable(false);
			QuaterlyYearLabel.setDisable(false);
			MonthlyReportButton.setDisable(true);

		} else {
			MonthlyReportButton.setDisable(false);
			pickQuarterCB.setDisable(true);
			pickYearForQuarterCB.setDisable(true);
			QuaterlyLabel.setDisable(true);
			QuaterlyYearLabel.setDisable(true);

		}
	}

	@FXML
	void ViewAction(ActionEvent event) throws Exception {
		if (MonthlyReportButton.isSelected()) {
			String branchID = ClientHandleTransmission.getBranchID();
			if (branchID != null && pickYearForMonthlyCB.getValue() !=null && pickMonthForMonthlyCB.getValue() !=null && pickReportTypeForMonthlyCB.getValue() !=null) {

				if (ClientHandleTransmission.getMonthlyReport(branchID, pickYearForMonthlyCB.getValue(),
						pickMonthForMonthlyCB.getValue(), pickReportTypeForMonthlyCB.getValue())) {
					TransmissionPack tp = ClientUI.chat.getObj();
					Report returned = ((Report) tp.getInformation());
					ReportHandleController.setUserReport((BranchManager) ClientController.user); // down cast
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
					Stage primaryStage = new Stage();
					switch (returned.getReportType()) {
					case ORDERS: {
						OrderReportsController orderReport = new OrderReportsController();
						orderReport.start(primaryStage);
						return;
					}
					case INCOME: {
						IncomeReportController incomeReport = new IncomeReportController();
						incomeReport.start(primaryStage);
						return;
					}
					}

				} else {
					reportNotFoundLabel.setText("The Requested Report Missing");
				}
			}
			else {
				reportNotFoundLabel.setText("Some Information Missing");
			}

		} else if (QuartelyReportButton.isSelected()) {
			String branchID = ClientHandleTransmission.getBranchID();
			if (branchID != null && pickQuarterCB.getValue() !=null && pickYearForQuarterCB.getValue() !=null) {
				if (ClientHandleTransmission.getComliantsQuarterlyReport(branchID,
						pickQuarterCB.getValue(), pickYearForQuarterCB.getValue())) {
					
					ReportHandleController.setUserReport((BranchManager) ClientController.user); // down cast
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
					Stage primaryStage = new Stage();
					ComplaintsReportController complaintReport = new ComplaintsReportController();
					complaintReport.start(primaryStage);
				}else {
					reportNotFoundLabel.setText("The Requested Report Missing");
				}
					
			}else
				reportNotFoundLabel.setText("Some Information Missing");
			
		}
	}
	/**
	 * add Thread timer that give the current Time on the screen
	 * @return
	 */
	private AnimationTimer addingTimer() {
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		return time;
	}
}
