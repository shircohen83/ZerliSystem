package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientHandleTransmission;
import client.ReportHandleController;
import client.popMessageHandler;
import enums.Branches;
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

public class AnoterQuarterlyReportPopupController implements Initializable {

	
	@FXML
	private ComboBox<Branches> PickBranch;

	@FXML
	private ComboBox<String> pickQuarterQuarterlyCB;

	@FXML
	private ComboBox<String> pickTypeQuarterlyCB;

	@FXML
	private ComboBox<String> pickYearQuarterlyCB;

	@FXML
	private Button submitBtn;

    @FXML
    private Label reportNotFoundLabel;

	private ObservableList<String> quarterlyQuarterList;
	private ObservableList<String> quarterlyYearList;
	private ObservableList<Branches> branchesObser;
	private ObservableList<String> reportTypeList;

	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/AnoterQuarterlyReportPopupPage.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Network Manager View Reports");
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void Submit(ActionEvent event) throws IOException {
		if (PickBranch.getValue() != null && pickYearQuarterlyCB.getValue() != null
				&& pickQuarterQuarterlyCB.getValue() != null && ReportHandleController.isDualReport() == false) {
			if (ClientHandleTransmission.getQuarterIncomeReport(String.valueOf(PickBranch.getValue().getNumber()), pickYearQuarterlyCB.getValue(),
					pickQuarterQuarterlyCB.getValue())) {
				ReportHandleController.setDualReport(true);
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
				Stage primaryStage = new Stage();
				PopupSecondIncomeQuarterlyReportsController secondIncomeQuarterly = new PopupSecondIncomeQuarterlyReportsController();
				secondIncomeQuarterly.start(primaryStage);
			}
			else {
				reportNotFoundLabel.setText("The Requested Report Missing");
			}
		} else if (!ReportHandleController.isDualReport())
			reportNotFoundLabel.setText("Some Information Missing");
		else {
			reportNotFoundLabel.setText("You cant open more then two reports in parallel!");
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		
		reportTypeList = FXCollections.observableArrayList("Income");
		pickTypeQuarterlyCB.setItems(reportTypeList);
		
		List<Branches> brances=ClientHandleTransmission.getBranches();
		if(brances.size() != 0) {
			branchesObser.addAll(brances);
		}
		PickBranch.setItems(branchesObser);
	}

}
