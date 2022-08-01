package client_gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.ReportHandleController;
import client.popMessageHandler;
import communication.Mission;
import communication.Response;
import communication.TransmissionPack;
import entities_users.ServiceExpert;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class SurveyReportController implements Initializable {

	@FXML
	private Button BackBtn;

	@FXML
	private StackedBarChart<String, Integer> barChart;

	@FXML
	private TextArea conclusionsText;

	@FXML
	private TextArea recommendationText;

	@FXML
	private Label reportTitle;

	@FXML
	private Button submitBtn;

    @FXML
    private Label userName;

    @FXML
    private Label role;

    @FXML
    private Label phoneNumber;

    @FXML
    private Label accountStatus;
	List<String> titleInfo = new ArrayList<>();

	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/SurveyReport.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Surver Report View Reports");
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ServiceExpertViewReportsController serviceExpertPage = new ServiceExpertViewReportsController();
		try {
			serviceExpertPage.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void submit(ActionEvent event) throws IOException {
		String conclusions = conclusionsText.getText();
		String recommendation = recommendationText.getText();
		System.out.println(conclusions);
		System.out.println(recommendation);
		WritableImage image = new WritableImage(75, 75);
		image = barChart.snapshot(new SnapshotParameters(), null);

		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		com.itextpdf.text.Image graph = null;

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput);
			graph = com.itextpdf.text.Image.getInstance(byteOutput.toByteArray());
		} catch (IOException | BadElementException e) {
			// TODO: handle exception here
		}
		// created PDF document instance
		Document doc = new Document();
		File file = null;
		try {
			// generate a PDF at the specified location
			 file=new File(titleInfo.get(2) + "-" + titleInfo.get(1)+"ExpertPDF.pdf");
			file.createNewFile();
			//String file = "C:/expert/"+titleInfo.get(2) + "/" + titleInfo.get(1)+"expertPdf.pdf";
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
			System.out.println("PDF created.");
			// opens the PDF
			doc.open();
			// adds paragraph to the PDF file
			doc.add(new Paragraph("                          Zerli" + titleInfo.get(3) + " - Survey For branch- "
					+ titleInfo.get(0) + " duration: " + titleInfo.get(2) + "/" + titleInfo.get(1)));
			doc.add(new Paragraph("The Survey Result: \n\n"));
			doc.add((com.itextpdf.text.Element) graph);
			doc.add(new Paragraph("\nThe expert conclusions: \n" + conclusions));
			doc.add(new Paragraph("\nThe expert recommendation: \n" + recommendation));
			// close the PDF file
			doc.close();
			// closes the writer
			writer.close();
		
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    final InputStream in = new FileInputStream(file);
	    final byte[] buffer = new byte[500];
	    int read = -1;
	    while ((read = in.read(buffer)) > 0) {
	        baos.write(buffer, 0, read);
	    }
	    in.close();

		List<Object> result=new ArrayList<>();
		result.add(baos.toByteArray());
		result.add((String)titleInfo.get(0));
		TransmissionPack tp=new TransmissionPack(Mission.INSERT_SURVEY_BY_EXPERT,null,null);
		tp.setInformation(result);
		ClientUI.chat.accept(tp);
		tp=ClientUI.chat.getObj();
		if(tp.getResponse()==Response.INSERT_SURVEY_REPORT_SUCCESS) {
   			popMessageHandler.setMessage("Report(PDF) created and store in the DataBase");
			popMessageHandler.setTitle("Report pdf creation");
			
			GenaralPopScroolBarUpController genaralPopScroolBarUpController = new GenaralPopScroolBarUpController();
			try {
				genaralPopScroolBarUpController.start(new Stage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else {
   			popMessageHandler.setMessage("Faild to create the PDF file.");
			popMessageHandler.setTitle("Faild To Create pdf");
			
			GenaralPopScroolBarUpController genaralPopScroolBarUpController = new GenaralPopScroolBarUpController();
			try {
				genaralPopScroolBarUpController.start(new Stage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, new Label(), role,
				((ServiceExpert) ClientController.user).toString());
		List<List<String>> surveyResult = new ArrayList<>();
		surveyResult = ReportHandleController.getSurveyReportResult();
		titleInfo = surveyResult.get(0);
		System.out.println(surveyResult);
		reportTitle.setText("Zerli - " + titleInfo.get(3) + "- Survey" + " For branch-" + titleInfo.get(0)
				+ " duration: " + titleInfo.get(2) + "/" + titleInfo.get(1));
		XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series3 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series4 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series5 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series6 = new XYChart.Series<>();
		series1.setName("Question 1");
		series2.setName("Question 2");
		series3.setName("Question 3");
		series4.setName("Question 4");
		series5.setName("Question 5");
		series6.setName("Question 6");
		int[] array = new int[6];
		for (int i = 1; i < surveyResult.size(); i++) {
			List<String> rowInfo = new ArrayList<>();
			rowInfo = surveyResult.get(i);
			for (int j = 0; j < rowInfo.size() - 1; j++)
				array[j] += Integer.valueOf(rowInfo.get(j));
		}
		series1.getData().add(new XYChart.Data<>("Question 1", array[0]));
		series2.getData().add(new XYChart.Data<>("Question 2", array[1]));
		series3.getData().add(new XYChart.Data<>("Question 3", array[2]));
		series4.getData().add(new XYChart.Data<>("Question 4", array[3]));
		series5.getData().add(new XYChart.Data<>("Question 5", array[4]));
		series6.getData().add(new XYChart.Data<>("Question 6", array[5]));
		barChart.getData().addAll(series1, series2, series3, series4, series5, series6);
	}

}
