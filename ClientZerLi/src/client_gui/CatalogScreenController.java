package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.OrderHandleController;
import entities_catalog.Product;
import entities_catalog.ProductInOrder;
import entities_users.Customer;
import enums.AccountStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * this controller handle with the catalog screen(gui) and all the catalog situation 
 * @author almog madar
 *
 */
public class CatalogScreenController implements Initializable {

	@FXML
	private VBox ChosenItemCard;

	@FXML
	private Button addToCartBtn;

	@FXML
	private Button backBtn;

	@FXML
	private GridPane grid;

	@FXML
	private Label itemCardNameLable;

	@FXML
	private Label itemCardPriceLable;

	@FXML
	private ComboBox<String> itemColorComboBox;

	@FXML
	private ImageView itemImageCard;

	@FXML
	private ImageView cartPageImage;

	@FXML
	private ComboBox<String> itemPriceComboBox;

	@FXML
	private ComboBox<String> itemTypeComboBox;

	@FXML
	private Button minusBtn;

	@FXML
	private Button pluseBtn;

	@FXML
	private TextField quantityTextLable;

    @FXML
    private Button infoBtn;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private RadioButton customClickRadioBtn;

	@FXML
	private Label cartItemCounter;

	@FXML
	private Button updateBtn;

	@FXML
	private Button clearBtn;

	@FXML
	private HBox vboxAddToCustom;

	@FXML
	private Button addToCustomBtn;
	@FXML
	private TextField customTextField;

	private String CURRENCY = "₪";
	private Image imageCardTmp;
	private MyListenerCatalog myListener;
	private List<Product> itemInCatalog = new ArrayList<Product>();
	private ObservableList<String> colorFilter;
	private ObservableList<String> priceFilter;
	private ObservableList<String> typeFilter;
	private static ProductInOrder productInOrder;
	private int cartCounter = 0;

	/**
	 * 
	 * @param primaryStage main of catalog screen
	 * @throws Exception if there is problem with start of this stage
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CatalogScreen.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("ZerLi Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * Get Products data information to preview on screen. Can bring Product with
	 * Filter or with them.
	 * 
	 * @param color filter of color (blue,green..)
	 * @param price filter of price (10 to 100 .. )
	 * @param type  filter of type (Product or Item)
	 * @return List of Product by this parameters
	 */
	private List<Product> getDataItems(String color, String price, String type) {
		if (color.equals("None") && price.equals("None") && type.equals("None"))
			return ClientHandleTransmission.getDataProduct();

		return ClientHandleTransmission.getDataProductByFilter(color, price, type);
	}

	private void setChosenItemCard(Product item) {
		itemCardNameLable.setText(item.getName());
		if(item.getIsOnSale())
			itemCardPriceLable.setText(CURRENCY + item.getFixPrice());
		else
			itemCardPriceLable.setText(CURRENCY + item.getPrice());
		imageCardTmp = new Image(getClass().getResourceAsStream(item.getImgSrc()));
		itemImageCard.setImage(imageCardTmp);
		ChosenItemCard.setStyle("-fx-background-color: #" + item.getbackGroundColor() + "; -fx-background-radius: 30;");
	}

	/**
	 * initialize Screen and all Features to preview in start Catalog Products ,
	 * ComboBox of filters , setChosenItemCard on left side
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initialize setChosenItemCard
		vboxAddToCustom.setVisible(false);
		customTextField.setDisable(true);
		quantityTextLable.setDisable(true);
		System.out.println("cartCounter->" + OrderHandleController.getCartCounter());
		cartCounter = OrderHandleController.getCartCounter();
		cartItemCounter.setText("" + cartCounter);

		// filter ComboBox section - color , price , type
		colorFilter = FXCollections.observableArrayList("None");
		colorFilter.addAll(ClientHandleTransmission.getColorsForFilter());
		itemColorComboBox.setItems(colorFilter);
		itemColorComboBox.setValue("None");
		priceFilter = FXCollections.observableArrayList("None", "10-100₪", "100-200₪", "200-500₪");
		itemPriceComboBox.setItems(priceFilter);
		itemPriceComboBox.setValue("None");
		typeFilter = FXCollections.observableArrayList("None", "Product", "Item");
		itemTypeComboBox.setItems(typeFilter);
		itemTypeComboBox.setValue("None");

		// Close Button until first chosen of Product
		addToCartBtn.setDisable(true);
		addToCustomBtn.setDisable(true);

		// change Add to button to Add to (Text)
		customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			// System.out.println("textfield changed from " + oldValue + " to " + newValue);
			addToCustomBtn.setText("ADD TO " + customTextField.getText().toUpperCase());
		});

		// Progress bar state - 50%
		progressIndicator.setStyle("-fx-color: #D0F6DD ; -fx-accent: green;");
		progressIndicator.setProgress(0.50f);

		// catalog item initialize
		InitilizeProductGrid("None", "None", "None");
		
		
		infoBtn.setOnMouseMoved(event -> {
			Tooltip tooltipCustom = new Tooltip("Dear Customer\n"
					+"Here You Can :\n"
					+ "1.View Product.\n"
					+ "2.Build and Add Custom Product.\n"
					+ "3.Add Regular Product.");
			tooltipCustom.setStyle("-fx-font-size: 20");
			Tooltip.install(infoBtn,tooltipCustom);
			
		});
		
		/**
		 * customer permissions 
		 */
		if(((Customer) ClientController.user).getAccountStatus()==AccountStatus.FROZEN) {
			addToCustomBtn.setDisable(true);
			addToCartBtn.setDisable(true);
			cartPageImage.setDisable(true);
			vboxAddToCustom.setVisible(false);
		}

	}

	/**
	 * Initialize Products Grid by filter or without. Grid of 3 Products in each
	 * line .
	 * 
	 * @param color filter of color (blue,green..)
	 * @param price filter of price (10 to 100 .. )
	 * @param type  filter of type (Product or Item)
	 */
	private void InitilizeProductGrid(String color, String price, String type) {
		try {
			itemInCatalog.addAll(getDataItems(color, price, type));
		} catch (NullPointerException e) {
			// here will be popup massage no products !!
		}

		if (itemInCatalog.size() > 0) {
			Product p = new Product("0", "No Item Choosen", 0, "172D42", "/javafx_images/CustomOrderPicture.png", 0,
					"Product", "Blue", false, 0);
			// setChosenItemCard(itemInCatalog.get(0));
			setChosenItemCard(p);

			myListener = new MyListenerCatalog() {
				@Override
				public void onClickListener(Product item) {

					// open addToCart button first time
					if(((Customer) ClientController.user).getAccountStatus()==AccountStatus.CONFIRMED) {
						addToCartBtn.setDisable(false);
						addToCustomBtn.setDisable(false);
					}
					
					// load chosenCard and ProductInOrder that chosen
					setChosenItemCard(item);
					
					if(item.getIsOnSale())
					{
						productInOrder = new ProductInOrder(item.getID(), null, null, item.getFixPrice(),
								item.getbackGroundColor(), item.getImgSrc(), item.getQuantity(), item.getItemType(),
								item.getDominateColor(), Integer.valueOf(quantityTextLable.getText()), item.getName(),
								item.getIsOnSale(), item.getFixPrice());
					}
					else
					{
						productInOrder = new ProductInOrder(item.getID(), null, null, item.getPrice(),
								item.getbackGroundColor(), item.getImgSrc(), item.getQuantity(), item.getItemType(),
								item.getDominateColor(), Integer.valueOf(quantityTextLable.getText()), item.getName(),
								item.getIsOnSale(), item.getFixPrice());
					}


				}
			};

		}

		int col = 0;
		int row = 1;
		try {
			for (int i = 0; i < itemInCatalog.size(); i++) {
				FXMLLoader fxmlLoder = new FXMLLoader();
				fxmlLoder.setLocation(getClass().getResource("/client_gui/ItemInCatalog.fxml"));
				AnchorPane anchorPane = fxmlLoder.load();
				ItemInCatalogController itemInCatalogController = fxmlLoder.getController();
				itemInCatalogController.setDataItem(itemInCatalog.get(i), myListener);

				if (col == 3) { // Position 3x3
					col = 0;
					row++;
				}
				grid.add(anchorPane, col++, row); // matrix (child , column , row);

				// set item grid width
				grid.setMinWidth(Region.USE_COMPUTED_SIZE);
				grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
				grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

				// set item grid height
				grid.setMinHeight(Region.USE_COMPUTED_SIZE);
				grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
				grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

				GridPane.setMargin(anchorPane, new Insets(10)); // topRightBottomLeft

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Click On Radio Button on open the option for custom order.
	 * 
	 * @param event click on Custom Button
	 */
	@FXML
	void customClickRadio(ActionEvent event) {
		if (customClickRadioBtn.isSelected() == true) {
			customTextField.setDisable(false);
			addToCustomBtn.setVisible(true);
			addToCustomBtn.setText("ADD TO ...");
			vboxAddToCustom.setVisible(true);

		} else {
			customTextField.setDisable(true);
			vboxAddToCustom.setVisible(false);
			addToCustomBtn.setVisible(false);

		}
	}

	/**
	 * This Back Button will bring us Back to previous screen
	 * 
	 * @param event back to previous screen .
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CustomerPageController customerPage = new CustomerPageController();
		customerPage.start(primaryStage);
	}

	/**
	 * will be using in the future for searching .
	 * 
	 * @param event
	 */
	@FXML
	void Search(ActionEvent event) {

	}

	/**
	 * Add to Cart the custom\regular product
	 * 
	 * @param event click on Add to cart button
	 */
	@FXML
	void addToCart(ActionEvent event) {
		if(!quantityTextLable.getText().matches("[0-9]+") || Integer.parseInt(quantityTextLable.getText())>99 || (customTextField.getText().isEmpty() && customClickRadioBtn.isSelected())) {
			quantityTextLable.setText("0");
			return;
		}
		Integer howMuchQuantityToAdd = Integer.parseInt(quantityTextLable.getText());

		if (howMuchQuantityToAdd > 0)
			if (!customClickRadioBtn.isSelected()) {
				// regular ProductInOrder
				cartCounter += howMuchQuantityToAdd;
				OrderHandleController.quantityOfRegularProducts += howMuchQuantityToAdd;

				// regular and exist
				productInOrder.setProductQuantityInCart(Integer.valueOf(quantityTextLable.getText()));
				System.out.println("regularInsideClickAddToCart->" + productInOrder);
				System.out.println("here: "+OrderHandleController.getProductInOrder());
				System.out.println("here2: "+productInOrder);
				if (OrderHandleController.getProductInOrder().contains(productInOrder)) {
					OrderHandleController.addToExistItemOnListNotCustom(productInOrder);
				} else {
					// regular and new
					OrderHandleController.addProductInOrder(productInOrder);
				}

				// animation on set Chosen Item card
				setChosenItemCardAddToCartRegular();

			} else {
				cartCounter += 1;
				OrderHandleController.quantityOfCustomProducts++;

				List<ProductInOrder> moveToCart = new ArrayList<>();

				moveToCart = OrderHandleController.getCustomProductInOrder()
						.get(customTextField.getText().toUpperCase());
				System.out.println("more -> " + moveToCart);
				OrderHandleController.addCustomProductInOrderFinallCart(customTextField.getText().toUpperCase(),
						moveToCart);
				// System.out.println(OrderHandleController.getCustomProductInOrderFinallCart().toString());

				// clear custom selection
				vboxAddToCustom.setVisible(false);
				customTextField.setText("");
				customTextField.setDisable(true);
				customClickRadioBtn.setSelected(false);

				// animation on set Chosen Item card
				setChosenItemCardAddToCartTotalCustom();
			}

		// cartCounterUpdate
		cartItemCounter.setText("" + cartCounter);

		// print
		System.out.println("custom-hashMap->" + OrderHandleController.getCustomProductInOrderFinallCart());
		System.out.println("regular-list->" + OrderHandleController.getProductInOrder());
		System.out.println("custom->" + OrderHandleController.quantityOfCustomProducts);
		System.out.println("regualr->" + OrderHandleController.quantityOfRegularProducts);
		System.out.println("totalPrice->" + OrderHandleController.getTotalPrice());

	}

	/**
	 * click on minus button to decrease product quantity value
	 * 
	 * @param event click on minus button
	 */
	@FXML
	void minusBtnClick(ActionEvent event) {
		int quantityValue = Integer.valueOf(quantityTextLable.getText());

		if (quantityValue != 0) {
			quantityValue -= 1;
			quantityTextLable.setText(Integer.toString(quantityValue));
		}
	}

	/**
	 * click on minus button to increase product quantity value
	 * 
	 * @param event click on minus button
	 */
	@FXML
	void pluseBtnClick(ActionEvent event) {
		int quantityValue = Integer.valueOf(quantityTextLable.getText());

		if (quantityValue != 100) {
			quantityValue += 1;
			quantityTextLable.setText(Integer.toString(quantityValue));
		}
	}

	/**
	 * Update products on catalog by filter that collected or without him
	 * 
	 * @param event update the products on catalog
	 */
	@FXML
	void update(ActionEvent event) {
		ObservableList<Node> productCards = grid.getChildren();
		productCards.clear();
		itemInCatalog.clear();
		InitilizeProductGrid(itemColorComboBox.getValue(), itemPriceComboBox.getValue(), itemTypeComboBox.getValue());
	}

	/**
	 * Clear and Update screen to all Products in Network
	 * 
	 * @param event clear previous and load all Product
	 */
	@FXML
	void clear(ActionEvent event) {
		itemColorComboBox.setValue("None");
		itemPriceComboBox.setValue("None");
		itemTypeComboBox.setValue("None");
		update(event);
	}

	/**
	 * Open Cart Page screen with all Products preview.
	 * 
	 * @param event click on cart will open here .
	 * @throws Exception when page will not bring well.
	 */
	@FXML
	void cartPageMove(MouseEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CartPageController cartPage = new CartPageController();
		cartPage.start(primaryStage);

	}

	/**
	 * Add product to dynamic Map the store all custom products. Only when user will
	 * add the custom product to cart it will move the relevant one to the cart Map.
	 * 
	 * @param event add products to custom product .
	 */
	@FXML
	void addToCustom(ActionEvent event) {
		if(!quantityTextLable.getText().matches("[0-9]+") || Integer.parseInt(quantityTextLable.getText())>99 ||customTextField.getText().isEmpty()) {
			quantityTextLable.setText("0");
			return;
		}
		int customQuantity = Integer.valueOf(quantityTextLable.getText());

		if (customQuantity > 0) {
			productInOrder.setProductQuantityInCart(customQuantity);
			if (OrderHandleController.getCustomProductInOrder().containsKey(customTextField.getText().toUpperCase())) {
				OrderHandleController.addToExistItemOnList(customTextField.getText().toUpperCase(), productInOrder);

			} else {
				List<ProductInOrder> productInOrderList = new ArrayList<>();
				productInOrderList.add(productInOrder);
				OrderHandleController.addCustomProductInOrder(customTextField.getText().toUpperCase(),
						productInOrderList);
			}
			setChosenItemCardAddToBouqet(customTextField.getText().toUpperCase());
		}
	}

	// add to cart preview
	public void setChosenItemCardAddToCartRegular() {
		Product p = new Product("0", "Regular added", 0, "172D42", "/javafx_images/productAddToCart.png", 0, "Product",
				"Blue", false, 0);
		setChosenItemCard(p);
		quantityTextLable.setText("0");
	}

	// add to cart preview
	public void setChosenItemCardAddToCartTotalCustom() {
		Product p = new Product("0", "Custom added", 0, "172D42", "/javafx_images/customProdcutAdd.png", 0, "Product",
				"Blue", false, 0);
		setChosenItemCard(p);
		quantityTextLable.setText("0");
		addToCartBtn.setDisable(true);
		addToCustomBtn.setDisable(true);
	}

	// add to bouqet preview
	public void setChosenItemCardAddToBouqet(String nameTotalCustomName) {
		Product p = new Product("0", " added to " + nameTotalCustomName, 0, "172D42",
				"/javafx_images/addItemToCustom.png", 0, "Product", "Blue", false, 0);
		setChosenItemCard(p);
	}

}