package client_gui;



import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ReportHandleController;
import entities_users.BranchManager;
import entities_users.NetworkManager;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * this class is handling with the monthly Order report .
 * 
 * @author Dvir Bublil
 *
 */
public class OrderReportsController implements Initializable {


    @FXML
    private Label accountStatus;
    @FXML
    private Label branchDetials;
    @FXML
    private Label role;

    @FXML
    private Label userName;
    @FXML
    private Label branchTitle;
    @FXML
    private Label headTitle;
    @FXML
    private Label phoneNumber;
	@FXML
	private Button BackBtn;

	@FXML
	private StackedBarChart<String, Number> barChart;

	@FXML
	private PieChart pieChartCustom;
	@FXML
	private Label worstSeller;
	@FXML
	private Label bestSeller;
	@FXML
	private PieChart pieChartRegular;
	@FXML
	private Label reportTitle;
	@FXML
	private Label timer;

	private static final DecimalFormat df = new DecimalFormat("0.000");
	List<List<String>> reportOnList = new ArrayList<>();
	List<String> branchInfo = new ArrayList<>();

	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/OrderReportsPage.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Order Report Page");
		stage.getIcons().add(new Image("/titleImg.jpg")); //main title
		stage.setScene(scene);
		stage.show();
    	stage.setResizable(false);
    	stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = addingTimer();
		time.start();
		switch (ReportHandleController.getUserReport().toString()) {
		case "Branch Manager": {
			
			ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, new Label(), role,
					((BranchManager) ClientController.user).toString());
			String branchName=ClientHandleTransmission.getBranchName(((BranchManager) ClientController.user).getBranchID().toString());
			if(branchName!=null)
				branchDetials.setText(branchName+" ("+((BranchManager) ClientController.user).getBranchID().toString()+")");
			headTitle.setText("BranchManager menu");
			branchTitle.setText("Branch");
			break;
		}
		case "Network Manager":{
			ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, new Label(), role,
					((NetworkManager) ClientController.user).toString());
			branchDetials.setDisable(true);
			branchTitle.setDisable(true);
			headTitle.setText("NetworkManager menu");
			break;
		}
		}
		ObservableList<PieChart.Data> pieChartData = null;
		ObservableList<PieChart.Data> pieChartData2 = null;
		reportOnList = ReportHandleController.getOrdersReportOnListMonth();
		insertTheOrdersReportDetails(pieChartData, pieChartData2);

	}

	@SuppressWarnings("unchecked")
	private void insertTheOrdersReportDetails(ObservableList<PieChart.Data> pieChartData,
			ObservableList<PieChart.Data> pieChartData2) {
		List<Double> amount = new ArrayList<>();
		double bouquet = 0, single = 0;
		System.out.println(reportOnList);
		branchInfo = reportOnList.get(0); // here we getting the branch information
		reportTitle.setText("Zerli " + branchInfo.get(1) + "(ID-" + branchInfo.get(0) + ")" + " - " + branchInfo.get(2)
				+ "-th orders based on products");
		// LineChart
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		series1.setName("single products");
		series2.setName("Boquet products");
		boolean flag = false, flag2 = false;
		amount = insertIntoChart(bouquet, single, series1, series2); // insert the products their information into the
																		// chart
		// in this loop we insert the data of the products , into the pie chart diagram.
		for (int i = 1; i < reportOnList.size(); i++) {
			List<String> productInfo = new ArrayList<>();
			productInfo = reportOnList.get(i);
			if (productInfo.get(0).equals("product") ||productInfo.get(0).equals("Product")) {
				StringBuilder name = new StringBuilder();
				for(int j=2;j<productInfo.size();j++) {
					name.append(productInfo.get(j) + " ");
				}
				if (flag == false) {
					pieChartData = FXCollections.observableArrayList(new PieChart.Data(name.toString(),
							((Integer.parseInt(productInfo.get(1)) / amount.get(0)))));

				} else {
					pieChartData.add(new PieChart.Data(name.toString(),
							((Integer.parseInt(productInfo.get(1)) / amount.get(0)))));
				}
				flag = true;
			}
			if (productInfo.get(0).equals("item") ||productInfo.get(0).equals("Item")) {
				StringBuilder name = new StringBuilder();
				for(int j=2;j<productInfo.size();j++) {
					name.append(productInfo.get(j) + " ");
				}
				if (flag2 == false) {
					pieChartData2 = FXCollections.observableArrayList(new PieChart.Data(name.toString(),
							((Integer.parseInt(productInfo.get(1)) / amount.get(0)))));

				} else {
					pieChartData2.add(new PieChart.Data(name.toString(),
							((Integer.parseInt(productInfo.get(1)) / amount.get(0)))));
				}
				flag2 = true;
			}
		}
		barChart.getData().addAll(series1, series2);
		pieChartCustom.setData(pieChartData);
		pieChartCustom.setStartAngle(90);
		pieChartRegular.setData(pieChartData2);
		pieChartRegular.setStartAngle(90);
		insertTheWorstAndBestSell();
	
//////////////////////---PieChart MouseEvent(regular + Custom)--//////////////////////////////////////////
		// display the values of slice inside custom PieChart.
		pieChartCustom.getData().forEach(data -> {
			data.getNode().addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Tooltip tooltipCustom = new Tooltip(data.getName() + " " + df.format(data.getPieValue()) + "%");
					Tooltip.install(data.getNode(), tooltipCustom);
				}
			});
		});

		// display the values of slice inside regular PieChart.
		pieChartRegular.getData().forEach(data -> {
			data.getNode().addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					Tooltip tooltipReguler = new Tooltip(data.getName() + " " + df.format(data.getPieValue()) + "%");
					Tooltip.install(data.getNode(), tooltipReguler);
				}
			});
		});
	}

	/**
	 * in this method we insert the worst and best sell of the month.
	 */
	private void insertTheWorstAndBestSell() {
		int bestSellerAmount = 0, worstSellerAmount = 99999;
		StringBuilder bestSellerName = new StringBuilder();
		StringBuilder worstSellerName = new StringBuilder();
		for (int i = 1; i < reportOnList.size(); i++) {
			List<String> productInfo = new ArrayList<>();
			productInfo = reportOnList.get(i);
			if (Integer.parseInt(productInfo.get(1)) > bestSellerAmount) {
				bestSellerAmount = Integer.parseInt(productInfo.get(1));
				 bestSellerName = new StringBuilder();
				for(int j=2;j<productInfo.size();j++) {
					bestSellerName.append(productInfo.get(j) + " ");
				}

			} else if (Integer.parseInt(productInfo.get(1)) < worstSellerAmount) {
				worstSellerAmount = Integer.parseInt(productInfo.get(1));
				worstSellerName = new StringBuilder();
					for(int j=2;j<productInfo.size();j++) {
						worstSellerName.append(productInfo.get(j) + " ");
					}
			}
		}
		worstSeller.setText(worstSellerName.toString());
		bestSeller.setText(bestSellerName.toString());
	}

	/**
	 * this method insert into the diagram the products and their value according to
	 * the report , that we got from the DB
	 * 
	 * @param bouquet
	 * @param single
	 * @param series1
	 * @param series2
	 * @return
	 */
	private List<Double> insertIntoChart(Double bouquet, Double single, XYChart.Series<String, Number> series1,
			XYChart.Series<String, Number> series2) {
		
		List<Double> amount = new ArrayList<>();
		for (int i = 1; i < reportOnList.size(); i++) {

			List<String> productInfo = new ArrayList<>();
			productInfo = reportOnList.get(i);
			if (productInfo.get(0).equals("product")||productInfo.get(0).equals("Product")) {
				StringBuilder name = new StringBuilder();
				for(int k=2;k<productInfo.size();k++) {
				System.out.println(productInfo.get(k));
					name.append(productInfo.get(k) + " ");
				}
				System.out.println(name +" "+productInfo.get(1));
				series2.getData().add(new XYChart.Data<>(name.toString(), Integer.parseInt(productInfo.get(1))));
				bouquet += Integer.parseInt(productInfo.get(1));

			}
			if (productInfo.get(0).equals("item") ||productInfo.get(0).equals("Item")) {
				StringBuilder name = new StringBuilder();
				for(int k=2;k<productInfo.size();k++) {
					name.append(productInfo.get(k) + " ");
				}
				System.out.println(name +" "+productInfo.get(1));
				series1.getData().add(new XYChart.Data<>(name.toString(), Integer.parseInt(productInfo.get(1))));
				single += Integer.parseInt(productInfo.get(1));

			}

		}
		amount.add(0, bouquet);
		amount.add(1, single);

		return amount;
	}

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
