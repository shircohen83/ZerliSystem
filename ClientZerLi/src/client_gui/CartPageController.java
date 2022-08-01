package client_gui;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

import client.ClientController;
import client.ClientHandleTransmission;
import client.OrderHandleController;
import entities_catalog.ProductInOrder;
import entities_general.OrderCartPreview;
import entities_general.OrderCustomCartPreview;
import entities_users.Customer;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
public class CartPageController implements Initializable {

    // IDorder / (nameOfProdcut) zerli dvir / (nameOfItem) rose bouget /
    // IDorder / regular / rose / 
	// OrderCustomCartPreview - > Image , Name , Quantity , Description 
	
    @FXML
    private TableColumn<OrderCartPreview, ImageView> ImgColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, ImageView> ImgCustomColTbl;

    @FXML
    private TableColumn<OrderCartPreview, String> ItemNameColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, String> ItemNameCustomColTbl;

    @FXML
    private TableColumn<OrderCartPreview, Double> QuantityColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, Integer> QuantityCustomColTbl;

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button removeCustomProductBtn;

    @FXML
    private Button removeRegularBtn;
    
    @FXML
    private Label massageLabel;
    
    @FXML
    private Label massageLabelRegular;
    
    @FXML
    private Label priceLabel;
    
    @FXML
    private Label accountStatus;
    
    @FXML
    private Label employeeName;
    
    @FXML
    private Label employeeType;
    
    @FXML
    private Label phoneNumber;
    
    @FXML
    private Label newCustomerLabel;
    
    @FXML
    private Button infoBtn;
    
    @FXML
    private Label timer;
    
    
    @FXML
    private ProgressIndicator progressIndicator;
 
	@FXML
    private TableColumn<OrderCustomCartPreview, Button> showCustomTbl;

    @FXML
    private TableView<OrderCustomCartPreview> tableCustom;

    @FXML
    private TableView<OrderCartPreview> tableRegular;

    @FXML
    private TableColumn<OrderCartPreview, Double> priceColRegularTbl;

    @FXML
    private TableColumn<OrderCustomCartPreview, Double> priceCustomColTbl;
    

    DecimalFormat df = new DecimalFormat("#,###.##");
	public static ObservableList<OrderCustomCartPreview> listViewCustom = FXCollections.observableArrayList();
	private ObservableList<OrderCartPreview> listViewRegular = FXCollections.observableArrayList();

	//Cart is the publisher , orderHandelController is the subscriber
	private List<OrderHandleController> subscribers = new ArrayList<>();

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CartPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Cart Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event ->{
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
			});	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus,null, employeeType,
				((Customer) ClientController.user).toString());
		
		// show button function
		showCustomTbl.setCellFactory(ShowButtonTableCell.<OrderCustomCartPreview>forTableColumn("Details", (OrderCustomCartPreview o) -> {

			if(!OrderHandleController.isDetailsAllreadyOpen())
			{
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
		QuantityCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, Integer>("quantity"));
		priceCustomColTbl.setCellValueFactory(new PropertyValueFactory<OrderCustomCartPreview, Double>("totalprice"));
		//Cart Table Initialize regular
		ImgColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, ImageView>("imgSrc"));
		ItemNameColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, String>("name"));
		QuantityColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, Double>("quantity"));
		priceColRegularTbl.setCellValueFactory(new PropertyValueFactory<OrderCartPreview, Double>("price"));

		//red color for problematic quantity
		tableRegular.setRowFactory(tv -> new TableRow<OrderCartPreview>() {
			@SuppressWarnings("unused")
			@Override
			protected void updateItem(OrderCartPreview item, boolean empty) {
				Set<String> problemticProducts = OrderHandleController.getProblemticProducts();
				super.updateItem(item, empty);
				if(problemticProducts.size()>0)
				if (item == null ) {
					
				} else if (OrderHandleController.getProblemticProducts().contains(item.getName())) {
					setStyle("-fx-background-color: #FC655B;");
				}
			}
		});
		
		//red color for problematic quantity
		tableCustom.setRowFactory(tv -> new TableRow<OrderCustomCartPreview>() {
			@SuppressWarnings("unused")
			@Override
			protected void updateItem(OrderCustomCartPreview item, boolean empty) {
				Set<String> problemticProducts = OrderHandleController.getProblemticProducts();
				
				super.updateItem(item, empty);
				if(problemticProducts.size()>0)
				if (item == null ) {
					
				} else {
					List<ProductInOrder> customProductInOrder = item.getCartList();
					for(ProductInOrder p : customProductInOrder)
						if (problemticProducts.contains(p.getName())) {
							setStyle("-fx-background-color: #FC655B;");
						}
				}
			}
		});
		
		
		//add all Custom product to screen 
		Map<String,List<ProductInOrder>> customProductInOrderFinallCart = OrderHandleController.getCustomProductInOrderFinallCart();
		for(String customName : customProductInOrderFinallCart.keySet())
		{
			Image image1 = new Image("/javafx_images/CustomOrderPicture.png", 60, 60, true, true);
			ImageView imageView1 = new ImageView(image1);
			imageView1.setImage(image1);
			listViewCustom.add(new OrderCustomCartPreview(imageView1,customName, 1, 0, customProductInOrderFinallCart.get(customName)));
		}
				
		//add all regular product to regular list
		for(ProductInOrder p : OrderHandleController.getProductInOrder())
		{
			Image image1 = new Image(p.getImgSrc(), 60, 60, true, true);
			ImageView imageView1 = new ImageView(image1);
			imageView1.setImage(image1);
			listViewRegular.add(new OrderCartPreview(imageView1, p.getName(),(int)p.getProductQuantityInCart(),p.getPrice() ,p));
		}
		
		//set tables to show products 
		tableRegular.setItems(listViewRegular);
		tableCustom.setItems(listViewCustom);
		
		
		//priceLabel.setText(OrderHandleController.getTotalPrice()+"");
        //priceLabel.setText(OrderHandleController.getTotalPrice()+"");
		
		OrderHandleController.setNewCustomer(newCustomerLabel);
		OrderHandleController.setPriceLabel(priceLabel);
		OrderHandleController.updateTotalPrice();
		
		// Progress bar state - 70%
		progressIndicator.setStyle("-fx-color: #D0F6DD ; -fx-accent: green;");
		progressIndicator.setProgress(0.70f);
		
		//timer on screen 
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		//info button 
		infoBtn.setOnMouseMoved(event -> {
			Tooltip tooltipCustom = new Tooltip("Dear Customer\n"
					+"Here You Can:\n"
					+ "1.View Cart Selection.\n"
					+ "2.Remove Regualr Products.\n"
					+ "3.Remove Custom Products.\n"
					+ "4.View Custom Selection Details.");
			tooltipCustom.setStyle("-fx-font-size: 20");
			Tooltip.install(infoBtn,tooltipCustom);
			
		});
		
		
		//add listener to OrderHandleController to perform remove on background
		addSubscriber(new OrderHandleController());
	}
	
	
	@FXML
	void back(ActionEvent event) {
		
		// remove all listener in background 
		removeSubscribers();
		
		//clear static screen
		listViewCustom.clear();

		
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CatalogScreenController catalogPage = new CatalogScreenController();
		try {
			catalogPage.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@FXML
	void confirm(ActionEvent event) throws Exception {
		
		if(OrderHandleController.getCartCounter()>0)
		{
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
			Stage primaryStage = new Stage();
			OrderPageController orderPage = new OrderPageController();
			orderPage.start(primaryStage);
		}
	}

    @FXML
    void removeCustom(ActionEvent event) {
    	ObservableList<OrderCustomCartPreview> productSelected , allProducts;
		allProducts=tableCustom.getItems();
		productSelected=tableCustom.getSelectionModel().getSelectedItems();
		
		System.out.println("productSelected regular->"+productSelected);
		
		if(allProducts.isEmpty())
			massageLabel.setText("Custom Cart Allready Empty");
		try {
			
			//remove all custom product in orderHandler
			notifyRemoveCustomProduct(productSelected);
		
			//remove preview on screen
			productSelected.forEach(allProducts::remove);
			System.out.println("productSelected regular->"+productSelected);
			
		} catch (NoSuchElementException e) {
			massageLabel.setText("Custom Cart Empty!!");
		}
		
		//System.out.println("totalPrice->"+OrderHandleController.getTotalPrice());
	    if(OrderHandleController.getCartCounter()==0)
	    	OrderHandleController.setTotalPrice(0);
			
		OrderHandleController.updateTotalPrice();
			
    }
    
 
    @FXML
    void removeRegular(ActionEvent event) {
    	ObservableList<OrderCartPreview> productSelected , allProducts;
		allProducts=tableRegular.getItems();
		productSelected=tableRegular.getSelectionModel().getSelectedItems();
		
		System.out.println("productSelected regular->"+productSelected);
		
		if(allProducts.isEmpty())
			massageLabelRegular.setText("Regular Cart Allready Empty");
		try {
			
			//remove all regular productInOrder in orderHandler
			notifyRemoveRegularProductInOrder(productSelected);
			//remove preview on screen
			productSelected.forEach(allProducts::remove);
			System.out.println("productSelected regular->"+productSelected);
			
			System.out.println("totalPrice->"+OrderHandleController.getTotalPrice());
			OrderHandleController.updateTotalPrice();

		} catch (NoSuchElementException e) {
			massageLabelRegular.setText("Regular Cart Empty!!");
		}
		
		//System.out.println("totalPrice->"+OrderHandleController.getTotalPrice());
		
		
	    if(OrderHandleController.getCartCounter()==0)
	    	OrderHandleController.setTotalPrice(0);
		
		OrderHandleController.updateTotalPrice();

    }
    
    
    
   // action to remove product selected on details tiny screen in cartPage also
    public static void removeProductFromListViewCustom (OrderCustomCartPreview oCustomCP , ObservableList<ProductInOrder> productSelected) {
    	for(OrderCustomCartPreview customUpdate:listViewCustom) {
    		if(customUpdate.getName()==oCustomCP.getName())
    		{
    			customUpdate.removeProductInOrderInsideCustom(productSelected);
    			
    			//remove and add to refresh object on screen 
    			listViewCustom.remove(customUpdate);
    			listViewCustom.add(customUpdate);
    			
    			//if object without product release him 
    			if(customUpdate.getCartList().size()==0)
    				listViewCustom.remove(customUpdate);
    			break;
    		}
    	}
    	
	    if(OrderHandleController.getCartCounter()==0)
	    	OrderHandleController.setTotalPrice(0);
    	
    	OrderHandleController.updateTotalPrice();
    	//OrderHandleController.getPriceLabel();
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
	public void notifyRemoveRegularProductInOrder(ObservableList<OrderCartPreview> productSelected) {
		for( OrderHandleController s : subscribers)
			s.removeFromOrderRegular(productSelected);
	}
	
	// notify all subscribers to remove productSelected list from there local hashMap
	public void notifyRemoveCustomProduct(ObservableList<OrderCustomCartPreview> productSelected) {
		for( OrderHandleController s : subscribers)
			s.removeFromOrderCustom(productSelected);
	}
    
}