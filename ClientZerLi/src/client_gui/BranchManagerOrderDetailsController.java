package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.OrderHandleController;
import entities_catalog.ProductInOrder;
import entities_general.OrderPreview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * this class is presenting to the branch manager the relevant products in order 
 * @author Mor Ben Haim
 * @author Dvir Bulil
 *
 */

public class BranchManagerOrderDetailsController implements Initializable{

    @FXML
    private TableView<ProductInOrder> Orders;

    @FXML
    private TableColumn<ProductInOrder, String> nameCol;

    @FXML
    private TableColumn<ProductInOrder, Double> priceCol;

    @FXML
    private TableColumn<ProductInOrder, String> productIDCol;

    @FXML
    private TableColumn<ProductInOrder, Integer> quantityInCartCol;

    @FXML
    private TableColumn<ProductInOrder, Integer> totalquantityCol;
    
    @SuppressWarnings("unused")
	private ObservableList<ProductInOrder> listView = FXCollections.observableArrayList();
    
    



	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/BranchManagerOrderDetails.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Order Details");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}


	/**
	 * this method is initialize the relevant products in the order details
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		productIDCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, String>("iD"));
		nameCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, String>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Double>("price"));
		totalquantityCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Integer>("quantity"));
		quantityInCartCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Integer>("productQuantityInCart"));//need to change the column name from cartID
		
		OrderPreview order=OrderHandleController.getOrder();
		System.out.println(order.getItems());
		List<ProductInOrder>products=new ArrayList<>();
		/**
		 *adding relevant product to the observable table 
		 */
		for(String key:order.getItems().keySet()) {
			products.addAll(order.getItems().get(key));
		}
		

			listView.addAll(products);
			Orders.setItems(listView);
			
		
	}

}
