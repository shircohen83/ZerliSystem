package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ReportHandleController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PopupSecondIncomeQuarterlyReportsController  implements Initializable {

    @FXML
    private StackedBarChart<String, Double> IncomeLineChart;

    @FXML
    private Label bestMonth;

    @FXML
    private Label incomeQuarterTitle;

    @FXML
    private Label worstMonth;

    List<List<String>> reportOnList = new ArrayList<>();
	List<String> reportInfo = new ArrayList();
    public void start(Stage primaryStage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/client_gui/SecondIncomeQuarterlyReportsPage.fxml"));
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("Income second quarterly Report Page");
    	primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
    	primaryStage.setResizable(false);
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	primaryStage.setOnCloseRequest(event -> {
			ReportHandleController.setDualReport(false);
		});
    	
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reportOnList = ReportHandleController.getOrdersReportOnListQuarter();
		// LineChart
		XYChart.Series series = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		XYChart.Series series3 = new XYChart.Series();
		
		ReportHandleController.insertTheDeatilsForTheCartQurateryReport(worstMonth,bestMonth,incomeQuarterTitle,reportOnList,series, series2, series3);
		IncomeLineChart.getData().addAll(series, series2, series3);
		IncomeLineChart.setCategoryGap(0);
		
		
	}
	

}
