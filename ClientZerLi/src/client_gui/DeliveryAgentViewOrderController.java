package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.DeliveriesController;
import entities_catalog.ProductInOrder;
import entities_general.DeliveryPreview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DeliveryAgentViewOrderController implements Initializable {

	@FXML
	private Button closeBtn;

	@FXML
	private TableView<ProductInOrder> customProductTable;

	@FXML
	private Label customSelectionDetailsLabel;
	
	@FXML
	private Label timer;

	@FXML
	private TableColumn<ProductInOrder, String> productNameCol;

	@FXML
	private TableColumn<ProductInOrder, Integer> quantityCol;

	@FXML
	private TableColumn<ProductInOrder, Double> priceCol;

	private DeliveryPreview dp = null;
	private ObservableList<ProductInOrder> products = FXCollections.observableArrayList();
	static boolean flag = false; // make sure that user can open only one page each time

	public void start(Stage stage) throws IOException {
		if (flag == false) { // if the page is closed we can open
			flag = true; // true - cant open one more
			DeliveryAgentViewDeliveriesController.showOrderFlag = true;
			Parent root = FXMLLoader.load(getClass().getResource("/client_gui/DeliveryAgentViewOrder.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("Delivery Agent View Order");
			stage.getIcons().add(new Image("/titleImg.jpg")); //main title
			stage.setScene(scene);
			stage.show();
	    	stage.setResizable(false);
			stage.setOnCloseRequest(event -> {
				products.clear();
				flag = false;
				DeliveryAgentViewDeliveriesController.showOrderFlag = false;
			});
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		productNameCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, String>("nameOfItem"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Integer>("productQuantityInCart"));
		priceCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Double>("price"));
		dp = (DeliveriesController.getDelivery());
		System.out.println("dp get items");
		for(int i =0; i < dp.getOrderProducts().size(); i++) {
			System.out.println(dp.getOrderProducts().get(i));
		}
		products.addAll(dp.getOrderProducts());
		customProductTable.setItems(products);

	}

	@FXML
	void Close(ActionEvent event) {
		flag = false;
		DeliveryAgentViewDeliveriesController.showOrderFlag = false;
		products.clear();
		Stage stage = (Stage) closeBtn.getScene().getWindow();
	    stage.close();
	}

}
