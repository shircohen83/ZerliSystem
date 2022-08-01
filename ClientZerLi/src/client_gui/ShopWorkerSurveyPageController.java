package client_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.SurveyHandle;
import communication.Response;
import entities_general.Question;
import entities_users.ShopWorker;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class Description: this class is a general ControllerPage for all the Survey
 * Type
 * 
 * @author Mor Ben Haim
 *
 */
public class ShopWorkerSurveyPageController implements Initializable {
	@FXML
	private Label accountStatus;
	@FXML
	private Label entryGreeting;
	@FXML
	private Label feedBackMsg;

	@FXML
	private ComboBox<Integer> answer1;

	@FXML
	private ComboBox<Integer> answer3;

	@FXML
	private ComboBox<Integer> answer4;

	@FXML
	private ComboBox<Integer> answer5;

	@FXML
	private ComboBox<Integer> answer6;

	@FXML
	private ComboBox<Integer> answer2;
	List<ComboBox<Integer>> comboxes = new ArrayList<>();
	@FXML
	private ObservableList<Integer> anwers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

	@FXML
	private Button backBtn;
	@FXML
	private Label branchName;

	@FXML
	private Label employeeName;

	@FXML
	private Label employeeType;

	@FXML
	private Label phoneNumber;

	@FXML
	private Label question1;

	@FXML
	private Label question2;

	@FXML
	private Label question3;

	@FXML
	private Label question4;

	@FXML
	private Label question6;

	@FXML
	private Label question5;
	int i;

	private List<Label> qurstios = new ArrayList<>();
	List<Question> question = new ArrayList<>();

	@FXML
	private Label timer;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ShopWorkerSurveyPage.fxml"));

		Scene scene = new Scene(root);

		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Insert Survey Answers Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * go back the customerService menu
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ShopWorkerPageController shopWorkerPageController = new ShopWorkerPageController();
		shopWorkerPageController.start(primaryStage);

	}

	/**
	 * inserting the survey answer rate form the customer
	 * 
	 * @param event
	 */
	@FXML
	void Insert(ActionEvent event) {
		int i;
		boolean flag = false;
		flag = validationCheck(flag);
		if (flag) {
			return;
		}
		checkInsertResponse();
	}

	/**
	 * send mission to the server after the shop worker adding the customer answer
	 * to the survey
	 */
	private void checkInsertResponse() {
		if (ClientHandleTransmission.insertSurvey(question) == Response.ISERT_SURVEY_SUCCEED) {
			feedBackMsg.setText("Insert Succeed");
			feedBackMsg.setTextFill(Color.GREEN);
		} else {
			feedBackMsg.setText("Insert Falied");
			feedBackMsg.setTextFill(Color.RED);
		}
	}

	/**
	 * loop run all combobox in the screen if one of them is empty it mark him in
	 * red
	 * 
	 * @param flag
	 * @return
	 */
	private boolean validationCheck(boolean flag) {
		int i;
		for (i = 0; i < question.size(); i++) {
			if (comboxes.get(i).getValue() == null) {
				comboxes.get(i).setStyle("-fx-border-color: red");
				flag = true;

			} else {

				question.get(i).setAnswer(comboxes.get(i).getValue());
				question.get(i).setDate(timer.getText());
				question.get(i).setBranchID(((ShopWorker) ClientController.user).getBranchID());
				question.get(i).setTargetAudience(SurveyHandle.getTargetAudience());
			}
		}
		return flag;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = addingTimer();
		time.start();
		branchName.setText(ClientHandleTransmission.getBranchName(((ShopWorker) ClientController.user).getBranchID())+"("+((ShopWorker) ClientController.user).getBranchID()+")");

		qurstios = Arrays.asList(question1, question2, question3, question4, question5, question6);
		comboxes = Arrays.asList(answer1, answer2, answer3, answer4, answer5, answer6);
		question = ClientHandleTransmission.getSurveyQuestion();
		addLisnterToCombobox();
		setQuestionsAndAnswers();

		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((ShopWorker) ClientController.user).toString());

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

	/**
	 * set the question that were get from the DB
	 */
	private void setQuestionsAndAnswers() {
		for (i = 0; i < question.size(); i++) {
			qurstios.get(i).setText(question.get(i).getQuestion());
			comboxes.get(i).setItems(anwers);

		}
	}

	/**
	 * add Listener for each combobox after validCheck
	 */
	private void addLisnterToCombobox() {
		for (ComboBox<Integer> cmb : comboxes) {
			cmb.valueProperty().addListener(new ChangeListener<Integer>() {

				@Override
				public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
					if (oldValue != newValue) {
						cmb.setStyle("-fx-border-color: transparent");

						// Color.TRANSPARENT
					}

				}
			});
		}
	}

}
