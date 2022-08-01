package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ReportHandleController;
import entities_users.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class IncomeQuarterlyReportsController implements Initializable {


    @FXML
    private Label phoneNumber;

    @FXML
    private Label userName;
    @FXML
    private Label accountStatus;

	@FXML
	private Button BackBtn;
	@FXML
	private Label role;

	@FXML
	private StackedBarChart<String, Double> IncomeLineChart;

 
	@FXML
	private Label bestMonth;
    @FXML
    private Button addAnotherReportBTN;

	@FXML
	private Label incomeQuarterTitle;

	@FXML
	private Label worstMonth;
	List<List<String>> reportOnList = new ArrayList<>();
	List<String> reportInfo = new ArrayList();

	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/IncomeQuarterlyReports.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Income Quarterly Report Page");

		stage.setScene(scene);
		stage.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, new Label(), role,
				((NetworkManager) ClientController.user).toString());
		reportOnList = ReportHandleController.getOrdersReportOnListQuarter();
		
		XYChart.Series series = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		XYChart.Series series3 = new XYChart.Series();
		
		ReportHandleController.insertTheDeatilsForTheCartQurateryReport(worstMonth,bestMonth,incomeQuarterTitle,reportOnList,series, series2, series3);
		IncomeLineChart.getData().addAll(series, series2, series3);
		
	
		IncomeLineChart.setCategoryGap(0);
		
	}


	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		NetworkManagerPageController branchPagerViewReport = new NetworkManagerPageController();
		try {
			branchPagerViewReport.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @FXML
    void addAnotherReport(ActionEvent event) {
    	
    	Stage primaryStage = new Stage();
    	AnoterQuarterlyReportPopupController AnoterQuarterlyReportPopupPage = new AnoterQuarterlyReportPopupController();
		try {
			AnoterQuarterlyReportPopupPage.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
