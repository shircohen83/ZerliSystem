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
import communication.TransmissionPack;
import entities_reports.Report;
import entities_users.NetworkManager;
import entities_users.ServiceExpert;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServiceExpertViewReportsController implements Initializable {
	@FXML
	private Label timer;
	  @FXML
	    private Button BackBtn;

	    @FXML
	    private ComboBox<Branches> PickBranch;

	    @FXML
	    private Label accountStatusLbl;

	    @FXML
	    private Label networkManagerNameLbl;

	    @FXML
	    private Label phoneNumberLbl;

	    @FXML
	    private ComboBox<String> pickMonthCB;

	    @FXML
	    private ComboBox<String> pickSurveyCB;

	    @FXML
	    private ComboBox<String> pickYearCB;

	    @FXML
	    private Button submitBtn;

	    @FXML
	    private Label userRoleLbl;

	    @FXML
	    private Label welcomeBackUserNameLbl;
	    @FXML
	    private Label reportNotFoundLabel;

    private ObservableList<String> monthList;
	private ObservableList<String> yearList;
	private ObservableList<Branches> branchList;
	private ObservableList<String> surveyList;

    public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ServiceExpertViewReports.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Service Expert View Reports");
		stage.setScene(scene);
		stage.show();
	}
    @FXML
    void Back(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ServiceExpertPageController serviceExpertPage = new ServiceExpertPageController();
		try {
			serviceExpertPage.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void Submit(ActionEvent event) {
    	if(String.valueOf(PickBranch.getValue().getNumber()) != null && pickYearCB.getValue() !=null &&pickMonthCB.getValue() != null&&pickSurveyCB.getValue()!=null) {
    	if (ClientHandleTransmission.getServiceReport(String.valueOf(PickBranch.getValue().getNumber()), pickYearCB.getValue(),pickMonthCB.getValue(),pickSurveyCB.getValue()))
    	{
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
			Stage primaryStage = new Stage();
				SurveyReportController surveyReport = new SurveyReportController();
				try {
					surveyReport.start(primaryStage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    	}
    	else {
    		reportNotFoundLabel.setText("The Requested Report Missing");
    	}
    	
    	}
    	else {
    		
    		reportNotFoundLabel.setText("Some Information Missing");
    	}
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = addingTimer();
		time.start();
		ClientController.initalizeUserDetails(networkManagerNameLbl, phoneNumberLbl, accountStatusLbl, new Label(), userRoleLbl,
				((ServiceExpert) ClientController.user).toString());
		monthList = FXCollections.observableArrayList();
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				monthList.add("0" + i);
			else
				monthList.add("" + i);
		}
		pickMonthCB.setItems(monthList);
		branchList = FXCollections.observableArrayList();
		surveyList = FXCollections.observableArrayList("Customer Service","TBD");
		pickSurveyCB.setItems(surveyList);
		
		
		// need to add the branches after merge geting almog method.
		List<Branches> brances=ClientHandleTransmission.getBranches();
		if(brances.size() != 0) {
			branchList.addAll(brances);
		}
		PickBranch.setItems(branchList);
		
		List<String> querterYear = ClientHandleTransmission.getYearsForComboBox("MONTHLY", "reports");
		if (querterYear.size() > 0) {
			yearList = FXCollections.observableArrayList(querterYear);
		} else {
			yearList = FXCollections.observableArrayList();
		}
		pickYearCB.setItems(yearList);
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
