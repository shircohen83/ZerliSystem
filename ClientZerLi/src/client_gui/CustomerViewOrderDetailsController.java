package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientHandleTransmission;
import client.OrderHandleController;
import entities_catalog.ProductInOrder;
import entities_general.OrderCartPreview;
import entities_general.OrderCustomCartPreview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CustomerViewOrderDetailsController implements Initializable{

    @FXML
    private TableColumn<OrderCartPreview, ImageView> ImgColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, ImageView> ImgCustomColTbl;

    @FXML
    private TableColumn<OrderCartPreview, String> ItemNameColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, String> ItemNameCustomColTbl;

    @FXML
    private TableColumn<OrderCartPreview, Number> QuantityColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, Number> QuantityCustomColTbl;

    @FXML
    private TableColumn<OrderCartPreview, Double> priceColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, Double> priceCustomColTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, Button> showCustomTbl;

    @FXML
    private TableView<OrderCustomCartPreview> tableCustom;

    @FXML
    private TableView<OrderCartPreview> tableRegular;
    
    @FXML
    private Label orderDetailsLabel;
    
    
	private ObservableList<OrderCustomCartPreview> listViewCustom = FXCollections.observableArrayList();
	private ObservableList<OrderCartPreview> listViewRegular = FXCollections.observableArrayList();
    
	/**
	 * 
	 * @param primaryStage main of catalog screen
	 * @throws Exception if there is problem with start of this stage
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CustomerViewOrderDetails.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Customer Order Details");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			OrderHandleController.setDetailsAllreadyOpen2(false);
		});
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		//only one screen open 
		OrderHandleController.setDetailsAllreadyOpen2(true);
		
		//set label in top of the page 
		orderDetailsLabel.setText("Order ("+OrderHandleController.getCustomerOrderView().getOrderID()+") Details");
		
		
		// show button function
		showCustomTbl.setCellFactory(ShowButtonTableCell.<OrderCustomCartPreview>forTableColumn("Details", (OrderCustomCartPreview o) -> {

			if(!OrderHandleController.isDetailsAllreadyOpen())
			{
				//disable remove button on screen 
				OrderHandleController.setDisableRemoveCustomButton(true);
				
				// new windows add send him the productInOrder list with the info
				Stage primaryStage = new Stage();
				CustomerViewCustomProductInfoController customProductDetails = new CustomerViewCustomProductInfoController();
				try {
					System.out.println(o.getCartList());
					customProductDetails.setProductDetails(o);
					customProductDetails.start(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("detials allreaday open popup");
			}
			
			return o;
		}));
		
		
		//Cart Table Initialize custom
		ImgCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, ImageView>("imgSrc"));
		ItemNameCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, String>("name"));
		QuantityCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, Number>("quantity"));
		priceCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, Double>("totalprice"));
		//Cart Table Initialize regular
		ImgColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, ImageView>("imgSrc"));
		ItemNameColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, String>("name"));
		QuantityColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, Number>("quantity"));
		priceColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, Double>("price"));
		
		
		Map<String, List<ProductInOrder>> ordersDetails = OrderHandleController.getCustomerOrderDetails();
		//add all Custom product to screen 
		
		if(ordersDetails.size()>0)
		{
			for(String customName : ordersDetails.keySet())
			{
				if(!customName.equals("Regular")) {
					Image image1 = new Image("/javafx_images/CustomOrderPicture.png", 60, 60, true, true);
					ImageView imageView1 = new ImageView(image1);
					imageView1.setImage(image1);
					listViewCustom.add(new OrderCustomCartPreview(imageView1,customName, 1, 0, ordersDetails.get(customName)));
				}
			}
			//add all regular product to regular list
			for(ProductInOrder p : ordersDetails.get("Regular"))
			{
				Image image1 = new Image(p.getImgSrc(), 60, 60, true, true);
				ImageView imageView1 = new ImageView(image1);
				imageView1.setImage(image1);
				listViewRegular.add(new OrderCartPreview(imageView1, p.getName(),(int)p.getProductQuantityInCart(),p.getPrice() ,p));
			}
			
			//set tables to show products 
			tableRegular.setItems(listViewRegular);
			tableCustom.setItems(listViewCustom);
		}
	}
    
	
	
    
    

}