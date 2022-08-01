package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ReportHandleController;
import entities_users.BranchManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ComplaintsReportController implements Initializable {

	@FXML
	private Label accountStatus;

	@FXML
	private Label branch;

	@FXML
	private Label phoneNumber;

	@FXML
	private Label userName;

	@FXML
	private Label userRole;

	@FXML
	private Button BackBtn;

	@FXML
	private StackedBarChart<String, Number> complaintsLineChart;

	@FXML
	private Label complaintsQuarterTitle;

	@FXML
	private Label satisfactionPercentage;

	private String branchID, branchName;

	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		switch (ReportHandleController.getUserReport().toString()) {
		case "Branch Manager": {
			BranchManagerViewReportPageController branchPagerViewReport = new BranchManagerViewReportPageController();
			try {
				branchPagerViewReport.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case "Network Manager": {
			NetworkManagerViewReportsController networkManagerViewReport = new NetworkManagerViewReportsController();
			try {
				networkManagerViewReport.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		}
	}

	List<List<String>> reportOnList = new ArrayList<>();
	List<String> reportInfo = new ArrayList();

	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ComplaintsReportPage.fxml"));
		Scene scene = new Scene(root);
		stage.getIcons().add(new Image("/titleImg.jpg")); // main title
		stage.setTitle("Complaints Quarterly Report Page");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	// @Override
	public void initialize(URL location, ResourceBundle resources) {

		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, null, userRole,
				((BranchManager) ClientController.user).toString());

		branchID = ((BranchManager) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(branchID);
		branch.setText(" " + branchName + " (" + branchID + ")");

		reportOnList = ReportHandleController.getComplaintsReportResult();

		// LineChart
		XYChart.Series series = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		XYChart.Series series3 = new XYChart.Series();
		series.setName("0-1(month)");
		series2.setName("1-2(month)");
		series3.setName("2-3(month)");
		// load data to LineChart
		ReportHandleController.insertTheDetailsForTheComplaintsReport(satisfactionPercentage, complaintsQuarterTitle,
				reportOnList, series, series2, series3);

		// add to regular(series) and Custom(series2)
		complaintsLineChart.getData().addAll(series, series2, series3);
	}

}
