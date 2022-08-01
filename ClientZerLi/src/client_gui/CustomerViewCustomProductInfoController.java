package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

import client.ClientHandleTransmission;
import client.OrderHandleController;
import entities_catalog.ProductInOrder;
import entities_general.OrderCartPreview;
import entities_general.OrderCustomCartPreview;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CustomerViewCustomProductInfoController implements Initializable {

    @FXML
    private TableView<ProductInOrder> customProductTable;

    @FXML
    private TableColumn<ProductInOrder, Double> priceCol;

    @FXML
    private TableColumn<ProductInOrder, String> productNameCol;

    @FXML
    private TableColumn<ProductInOrder, Integer> quantityInCartCol;

    @FXML
    private Button removeBtn;
    
    @FXML
    private Label massageLabel;
    
    @FXML
    private Label customSelectionDetailsLabel;


    private static ObservableList<ProductInOrder> productDetails = FXCollections.observableArrayList();
    private static OrderCustomCartPreview orderCustomCartPreview;
    private static String customName;
    
    
	//Cart is the publisher , orderHandelController is the subscriber
	private List<OrderHandleController> subscribers = new ArrayList<>();
    
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CustomerViewCustomProductInfo.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); // main title
		primaryStage.setTitle(customName+" - custom Product Details Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);

		
		//need to update list of product inside OrderHandleController 
		primaryStage.setOnCloseRequest(event ->{
			//clear static list
			productDetails.clear();
			
			//release the screen control , somebody else can take control 
			OrderHandleController.setDetailsAllreadyOpen(false);
			
			// remove all listener in background - orderHandle 
			removeSubscribers();
			});	
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		//check if need to invisible remove button
		if(OrderHandleController.isDisableRemoveCustomButton()) {
			removeBtn.setVisible(false);
			OrderHandleController.setDisableRemoveCustomButton(false);
		}
		
		//Cart Table Initialize
		productNameCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, String>("name"));
		quantityInCartCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Integer>("productQuantityInCart"));
		priceCol.setCellValueFactory(new PropertyValueFactory<ProductInOrder, Double>("price"));
		customProductTable.setItems(productDetails);
		customSelectionDetailsLabel.setText(customName+" Selection Details");
		
		
		//red color for problematic quantity
		customProductTable.setRowFactory(tv -> new TableRow<ProductInOrder>() {
			@SuppressWarnings("unused")
			@Override
			protected void updateItem(ProductInOrder item, boolean empty) {
				Set<String> problemticProducts = OrderHandleController.getProblemticProducts();
				
				super.updateItem(item, empty);
				if(problemticProducts.size()>0)
				if (item == null ) {
					
				} else if (problemticProducts.contains(item.getName())) {
							setStyle("-fx-background-color: #FC655B;");
				}
				
			}
		});
		
		//take the screen control , only me can view 
		OrderHandleController.setDetailsAllreadyOpen(true);
		
		//add listener to OrderHandleController to perform remove on background
		addSubscriber(new OrderHandleController());
	}
	
	
	@FXML
    void remove(ActionEvent event) {
		
		ObservableList<ProductInOrder> productSelected , allProducts;
		allProducts=customProductTable.getItems();
		productSelected=customProductTable.getSelectionModel().getSelectedItems();
		
		if(allProducts.isEmpty())
			massageLabel.setText("Table Allready Empty");
		try {
			
			notifyRemoveProductInOrderInsideCustom(productSelected);
			CartPageController.removeProductFromListViewCustom(orderCustomCartPreview, productSelected);
			productSelected.forEach(allProducts::remove);
		} catch (NoSuchElementException e) {
			massageLabel.setText("Table empty!!");
		}
		
		OrderHandleController.updateTotalPrice();
	
		//OrderHandleController.getPriceLabel().setText(String.valueOf(orderCustomCartPreview.getTotalprice()));
    }
    
    public ObservableList<ProductInOrder> getProductDetails() {
		return productDetails;
	}


	public void setProductDetails(OrderCustomCartPreview orderCustomCartPreview) {
		
		// set ObservableList of productDeatails
		CustomerViewCustomProductInfoController.productDetails.addAll(orderCustomCartPreview.getCartList());
		CustomerViewCustomProductInfoController.customName=orderCustomCartPreview.getName();
		CustomerViewCustomProductInfoController.orderCustomCartPreview=orderCustomCartPreview;
		
		//set table to show products 
		System.out.println("Custom:"+customName+"-->"+CustomerViewCustomProductInfoController.productDetails);
	}


    // add Subscriber
	public void addSubscriber(OrderHandleController s) {
		subscribers.add(s);
	}
	
	// remove Subscriber
	public void removeSubscribers() {
		subscribers.clear();
	
	}

	// notify all subscribers to remove productSelected list from there local list
	public void notifyRemoveProductInOrderInsideCustom(ObservableList<ProductInOrder> productSelected) {
		for( OrderHandleController s : subscribers)
			s.removeProductInOrderInsideCustom(productSelected,customName);
	}


}