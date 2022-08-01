package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.ReportHandleController;
import communication.TransmissionPack;
import entities_reports.Report;
import entities_users.BranchManager;
import entities_users.DeliveryAgent;
import entities_users.NetworkManager;
import enums.Branches;
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

public class NetworkManagerViewReportsController implements Initializable {
	@FXML
	private Label timer;

	@FXML
	private Button BackBtn;

	@FXML
	private CheckBox monthlyReportsRadioBtn;

	@FXML
	private ComboBox<String> pickMonthMonthlyCB;

	@FXML
	private ComboBox<Branches> PickBranch;

	@FXML

	private ComboBox<String> pickQuarterQuarterlyCB;

	@FXML
	private ComboBox<String> pickTypeMonthlyCB;

	@FXML
	private ComboBox<String> pickTypeQuarterlyCB;

	@FXML
	private ComboBox<String> pickYearForMonthlyCB;

	@FXML
	private ComboBox<String> pickYearQuarterlyCB;

	@FXML
	private CheckBox quarterlyReportsRadioBtn;

	@FXML
	private Button submitBtn;

	@FXML
	private Label userRoleLbl;

	@FXML
	private Label welcomeBackUserNameLbl;

	@FXML
	private Label networkManagerNameLbl;

	@FXML
	private Label phoneNumberLbl;

	@FXML
	private Label accountStatusLbl;

	/**
	 * load the page to the screen
	 * 
	 * @param stage
	 * @throws IOException
	 */
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/NetworkManagerViewReports.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Network Manager View Reports");
		stage.getIcons().add(new Image("/titleImg.jpg")); // main title
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	private ObservableList<String> reportTypeList;
	private ObservableList<String> monthlyMonthList;
	private ObservableList<String> monthlyYearList;

	private ObservableList<String> quarterlyQuarterList;

	private ObservableList<String> pickTypeQuarterly;
	private ObservableList<String> quarterlyYearList;
	private ObservableList<Branches> branchesObser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(networkManagerNameLbl, phoneNumberLbl, accountStatusLbl, null,
				userRoleLbl, ((NetworkManager) ClientController.user).toString());

		AnimationTimer time = addingTimer();
		time.start();
		pickMonthMonthlyCB.setDisable(true);

		pickQuarterQuarterlyCB.setDisable(true);
		pickTypeMonthlyCB.setDisable(true);
		pickTypeQuarterlyCB.setDisable(true);
		pickYearForMonthlyCB.setDisable(true);
		pickYearQuarterlyCB.setDisable(true);

		quarterlyReportsRadioBtn.setDisable(false);

		monthlyReportsRadioBtn.setDisable(false);

		reportTypeList = FXCollections.observableArrayList("Income", "Orders");
		pickTypeMonthlyCB.setItems(reportTypeList);
		monthlyMonthList = FXCollections.observableArrayList();
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				monthlyMonthList.add("0" + i);
			else
				monthlyMonthList.add("" + i);
		}
		pickMonthMonthlyCB.setItems(monthlyMonthList);

		List<String> monthlyYear = ClientHandleTransmission.getYearsForComboBox("MONTHLY", "reports");
		if (monthlyYear.size() > 0) {
			monthlyYearList = FXCollections.observableArrayList(monthlyYear);
		} else {
			monthlyYearList = FXCollections.observableArrayList();
		}
		pickYearForMonthlyCB.setItems(monthlyYearList);
		quarterlyQuarterList = FXCollections.observableArrayList("1", "2", "3", "4");
		pickQuarterQuarterlyCB.setItems(quarterlyQuarterList);

		List<String> querterYear = ClientHandleTransmission.getYearsForComboBox("QUARTERLY", "reports");
		if (querterYear.size() > 0) {
			quarterlyYearList = FXCollections.observableArrayList(querterYear);
		} else {
			quarterlyYearList = FXCollections.observableArrayList();
		}
		pickYearQuarterlyCB.setItems(quarterlyYearList);

		branchesObser = FXCollections.observableArrayList();

		pickTypeQuarterly = FXCollections.observableArrayList("Income");
		pickTypeQuarterlyCB.setItems(pickTypeQuarterly);
		// need to add the branches after merge geting almog method.
		List<Branches> brances = ClientHandleTransmission.getBranches();
		if (brances.size() != 0) {
			branchesObser.addAll(brances);
		}
		PickBranch.setItems(branchesObser);

	}

	/**
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
	void ShowMonthlyReports(ActionEvent event) {

		if (monthlyReportsRadioBtn.isSelected() && !quarterlyReportsRadioBtn.isSelected()) {

			pickMonthMonthlyCB.setDisable(false);
			pickTypeMonthlyCB.setDisable(false);
			pickYearForMonthlyCB.setDisable(false);
			quarterlyReportsRadioBtn.setDisable(true);

		} else {
			pickMonthMonthlyCB.setDisable(true);
			pickTypeMonthlyCB.setDisable(true);
			pickYearForMonthlyCB.setDisable(true);
			quarterlyReportsRadioBtn.setDisable(false);

		}
	}

	@FXML
	void ShowQuarterlyReports(ActionEvent event) {

		if (!monthlyReportsRadioBtn.isSelected() && quarterlyReportsRadioBtn.isSelected()) {
			pickTypeQuarterlyCB.setDisable(false);
			pickYearQuarterlyCB.setDisable(false);
			pickQuarterQuarterlyCB.setDisable(false);

			monthlyReportsRadioBtn.setDisable(true);
		} else {

			pickTypeQuarterlyCB.setDisable(true);
			pickYearQuarterlyCB.setDisable(true);
			pickQuarterQuarterlyCB.setDisable(true);
			monthlyReportsRadioBtn.setDisable(false);

		}
	}

	@FXML

	void Submit(ActionEvent event) throws IOException {
		if (quarterlyReportsRadioBtn.isSelected()) {
			if (ClientHandleTransmission.getQuarterIncomeReport(String.valueOf(PickBranch.getValue().getNumber()),
					pickYearQuarterlyCB.getValue(), pickQuarterQuarterlyCB.getValue().toUpperCase())) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
				Stage primaryStage = new Stage();
				IncomeQuarterlyReportsController orderReport = new IncomeQuarterlyReportsController();
				orderReport.start(primaryStage);
			} else {
				ClientHandleTransmission.popUp("There is no avaliable report yet!\nPlease choose different one!",
						"No Report Avaliable");
			}
		} else {
			if (monthlyReportsRadioBtn.isSelected()) {
				if (ClientHandleTransmission.getMonthlyReport(String.valueOf(PickBranch.getValue().getNumber()),
						pickYearForMonthlyCB.getValue(), pickMonthMonthlyCB.getValue(), pickTypeMonthlyCB.getValue())) {
					TransmissionPack tp = ClientUI.chat.getObj();
					Report returned = ((Report) tp.getInformation());
					ReportHandleController.setUserReport((NetworkManager) ClientController.user); // down cast
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
					ClientHandleTransmission.popUp("There is no avaliable report yet!\nPlease choose different one!",
							"No Report Avaliable");
				}
			}

		}
	}

	/**
	 * add Thread timer that give the current Time on the screen
	 * 
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