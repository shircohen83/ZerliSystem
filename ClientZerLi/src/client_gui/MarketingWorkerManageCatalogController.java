package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.itextpdf.text.log.SysoCounter;

import client.ClientController;
import client.ClientHandleTransmission;
import entities_catalog.Product;
import entities_users.MarketingWorker;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * @author User Shir Cohen
 */
public class MarketingWorkerManageCatalogController implements Initializable {

	@FXML
	private Label successFailLbl;

	@FXML
	private Label timer;

	@FXML
	private Button saveBtn;

	@FXML
	private Button BackBtn;

	@FXML
	private Label accountStatus;

	@FXML
	private Button addButton;

	@FXML
	private Label branchLbl;

	@FXML
	private Button editButton;

	@FXML
	private Label employeeName;

	@FXML
	private Label employeeType;

	@FXML
	private Label entryGreeting;

	@FXML
	private TableColumn<Product, Boolean> isOnSaleCol;

	@FXML
	private Label phoneNumber;

	@FXML
	private TableColumn<Product, Double> priceAfterDiscountCol;

	@FXML
	private TableColumn<Product, Double> priceCol;

	@FXML
	private TableColumn<Product, String> productIDCol;

	@FXML
	private TableColumn<Product, String> productNameCol;

	@FXML
	private TableColumn<Product, String> dominateColorCol;

	@FXML
	private TableColumn<Product, String> productTypeCol;

	@FXML
	private TableView<Product> productsTable;

	@FXML
	private Label responseLabel;

	@FXML
	private TableColumn<Product, Integer> quantityCol;

	@FXML
	private Button removeButton;

	private ObservableList<Product> productsListView = FXCollections.observableArrayList();

	private Product product = null;

	/**
	 * flag = boolean flag that gives a sign that there is a sale on a product.
	 * productIsValid = when we want to add new product we check if it is valid.
	 */
	private boolean saleFlag = false, removeBtnLock = false;

	/**
	 * firstMaxID = the max id we get at the beginning id = id that we can use as we
	 * needed currMaxID = the id we use to increase each time
	 */
	private String firstMaxIDstr, id;
	private int currMaxID, firstMaxIDint;
	/**
	 * removeProductsListString: the list will contain the products that the user
	 * will remove from the catalog
	 */
	private List<String> removeProductsListString = new ArrayList<>();
	/**
	 * removeProductsList: using this list to keep the removed products and use them
	 * later to create the editedProductsList
	 */
	private List<Product> removeProductsList = new ArrayList<>();

	/**
	 * editedProductsList: the list will contain the products that the user will
	 * edit from the current catalog
	 */
	private List<Product> editedProductsList = new ArrayList<>();
	/**
	 * addProductsList: the list will contain the products that the user will add to
	 * the catalog
	 */
	private List<Product> addProductsList = new ArrayList<>();
//	private List<Product> tempAddProductsList = new ArrayList<>()
	/**
	 * backUpList: the list will contain the products that we will get from the DB
	 * at first get when we upload this page
	 */
	private List<Product> backUpList = new ArrayList<>();
	/**
	 * beginingProductsID: used to save all the existing products IDs
	 */
	private List<String> beginingProductsID = new ArrayList<>();
	/**
	 * listOfProducts: the list is the one that got the data from the DB
	 */
	private List<Product> listOfProducts;

	/**
	 * 
	 * @param primaryStage
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/MarketingWorkerManageCatalog.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("catalog managment page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * get the user details to the left side of the screen
		 */
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((MarketingWorker) ClientController.user).toString());
		
		/**
		 * create a living clock on the screen
		 */
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		/**
		 * at start lock the remove product button
		 */
		if (removeBtnLock == false) {
			removeButton.setDisable(true);
		}

		productIDCol.setCellValueFactory(new PropertyValueFactory<Product, String>("ID"));

		productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		productNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

		priceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
			@Override
			public Double fromString(String value) {
				try {
					return super.fromString(value);
				} catch (NumberFormatException e) {
					return Double.NaN; /* if max_value this is a sign for us we have an error */
				}
			}
		}));

		productTypeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("itemType"));
		productTypeCol.setCellFactory(
				(param) -> new ComboBoxTableCell<>(FXCollections.observableArrayList("Item", "Product")));

		quantityCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
		quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
			@Override
			public Integer fromString(String value) {
				try {
					return super.fromString(value);
				} catch (NumberFormatException e) {
					return Integer.BYTES; /* if max_value this is a sign for us we have an error */
				}
			}
		}));
		List<String> colors = new ArrayList<>();
		colors.addAll(ClientHandleTransmission.getColorsForFilter());
		dominateColorCol.setCellValueFactory(new PropertyValueFactory<Product, String>("dominateColor"));
		if (colors != null) {
			dominateColorCol
					.setCellFactory((param) -> new ComboBoxTableCell<>(FXCollections.observableArrayList(colors)));
		}

		isOnSaleCol.setCellValueFactory(new PropertyValueFactory<Product, Boolean>("isOnSale"));
		isOnSaleCol.setCellFactory((param) -> new ComboBoxTableCell<>(FXCollections.observableArrayList(true, false)));

		priceAfterDiscountCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("fixPrice"));
		priceAfterDiscountCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
			@Override
			public Double fromString(String value) {
				try {
					return super.fromString(value);
				} catch (NumberFormatException e) {
					return Double.NaN; // An abnormal value
				}
			}
		}));
		/**
		 * get the products from the DB
		 */
		listOfProducts = ClientHandleTransmission.getDataProduct();
		/**
		 * save a backup list of the products we get from the DB 
		 * we will use this list after.
		 */
		backUpList.addAll(listOfProducts);
		System.out.println("backup list: \n" + backUpList);
		/**
		 * get the highest productID 
		 * we need it when we add new product
		 */
		firstMaxIDstr = ClientHandleTransmission.getMaxProductID();
		firstMaxIDint = Integer.valueOf(firstMaxIDstr);

		/**
		 * save the ID of the products we get at the start we will use that to collect
		 * all the
		 */
		for (Product p : listOfProducts) {
			beginingProductsID.add(p.getID());
		}

		System.out.println(listOfProducts);
		if (listOfProducts == null) {
			responseLabel.setText("no products");
			saveBtn.setDisable(true);
			editButton.setDisable(true);
			removeButton.setDisable(true);
		} else {
			for (Product p : listOfProducts) {
				productsListView.add(new Product(p.getID(), p.getName(), p.getPrice(), p.getbackGroundColor(),
						p.getImgSrc(), p.getQuantity(), p.getItemType(), p.getDominateColor(), p.getIsOnSale(),
						p.getFixPrice()));
			}
			productsTable.setItems(productsListView);
			productsTable.setEditable(true);
			/**
			 * Edit the isOnSale combo box to True or False depends on the marketing worker
			 */
			isOnSaleCol.setOnEditCommit(event1 -> {
				System.out.println("sales: " + event1.getNewValue());
				event1.getRowValue().setOnSale(event1.getNewValue());
				/**
				 * if there is a sale on the specific product we update the flag as true else we
				 * update it as false.
				 */
				if (event1.getRowValue().getIsOnSale() == true) {
					System.out.println("is on sale = true");
					saleFlag = true;
				} else {
					System.out.println("is on sale = false");
					event1.getRowValue().setFixPrice(event1.getRowValue().getPrice());
					saleFlag = false;
					System.out.println("dont set new price");
				}
				productsTable.refresh();
			});

			/**
			 * edit the price if there is discount else, the price remain the same
			 */
			priceAfterDiscountCol.setOnEditCommit(event2 -> {
				System.out.println("got in event 2");
				if (saleFlag == true) { // there is a sale on this product
					System.out.println("flag is true");
					/**
					 * if the price after discount is bigger then the real price or the new price is
					 * less then 1 or the user entered letters we set the price as the original
					 * price
					 */
					System.out.println("old" + event2.getOldValue());

					System.out.println("new" + event2.getNewValue());

					if (event2.getNewValue() > event2.getRowValue().getPrice() || event2.getNewValue() < 1) {
						event2.getRowValue()
								.setFixPrice(event2.getRowValue().getPrice()); /* set as the original price */
						System.out.println(event2.getRowValue().getPrice());
						System.out.println("1-dont set new price");
					} else if (!(String.valueOf(event2.getNewValue()).matches("[0-9.]+[0-9]"))) {
						event2.getRowValue()
								.setFixPrice(event2.getRowValue().getPrice()); /* set as the original price */
						System.out.println("2-dont set new price");
					} else if (event2.getNewValue().isNaN()) {
						event2.getRowValue()
								.setFixPrice(event2.getRowValue().getPrice()); /* set as the original price */
						System.out.println("3-dont set new price");
					} else { // update the new price
						event2.getRowValue().setFixPrice(event2.getNewValue());
						System.out.println("set new price");
					}
				}
				productsTable.refresh();
			});

			/**
			 * product type combo box edit we choose the type of the product.
			 */
			productTypeCol.setOnEditCommit(event3 -> {
				System.out.println("type: " + event3.getNewValue());
				event3.getRowValue().setItemType(event3.getNewValue());
				productsTable.refresh();
			});

			quantityCol.setOnEditCommit(event4 -> {
				if (event4.getNewValue() < 0 || (!(String.valueOf(event4.getNewValue()).matches("[0-9]+"))
						|| event4.getNewValue() == Integer.BYTES)) {
					System.out.println("dont update new qunatity");
					event4.getRowValue().setQuantity(event4.getOldValue());
				} else {
					System.out.println("update new qunatity");
					event4.getRowValue().setQuantity(event4.getNewValue());
				}
				productsTable.refresh();
			});

			/**
			 * if the price after edit is less then the 1 or the user entered letters we set
			 * the price as the original price, else we update the new price
			 */
			priceCol.setOnEditCommit(event5 -> {
				System.out.println("got in event 5");
				if (event5.getNewValue() < 1 || (!(String.valueOf(event5.getNewValue()).matches("[0-9.]+[0-9]")))
						|| event5.getNewValue().isNaN()) {
					event5.getRowValue().setPrice(event5.getOldValue()); /* set as the original price */
					System.out.println("dont set new price");
				} else { // update the new price
					event5.getRowValue().setPrice(event5.getNewValue());
					System.out.println("set new price");
				}
				productsTable.refresh();
			});

			/**
			 * The marketing worker can edit the products name as he wants.
			 */
			productNameCol.setOnEditCommit(event6 -> {
				System.out.println("got in event 6");
				event6.getRowValue().setName(event6.getNewValue());
				productsTable.refresh();
			});

			dominateColorCol.setOnEditCommit(event7 -> {
				System.out.println("dominate color: " + event7.getNewValue());
				event7.getRowValue().setDominateColor(event7.getNewValue());
				productsTable.refresh();
			});
		}
	}

	@FXML
	void Back(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		MarketingWorkerOpeningController marketingWorkerPage = new MarketingWorkerOpeningController();
		marketingWorkerPage.start(primaryStage);

	}

	@FXML
	void chooseProduct(MouseEvent event) {
		try {
			removeBtnLock = true;
			product = productsTable.getSelectionModel().getSelectedItem();
			if (product != null && removeBtnLock == true) {
				System.out.println(product);
				removeButton.setDisable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void RemoveProductFromCatalog(ActionEvent event) {
		removeBtnLock = false;
		if (product == null || removeBtnLock == false) {
			removeButton.setDisable(true);
		}
		/**
		 * we saved the products we got from the DB now, if we remove a product from the
		 * catalog that was in the DB we save it in removeProductsList.
		 */
		String productID = product.getID();
		for (String p : beginingProductsID) {
			if (p.equals(productID)) {
				removeProductsListString.add(productID);
				removeProductsList.add(product);
			}
			productsListView.remove(product);
		}
	}

	@FXML
	void AddNewProductToCatalog(ActionEvent event) {
		currMaxID = Integer.valueOf(ClientHandleTransmission.getMaxProductID());
		if (firstMaxIDint < currMaxID) {
			firstMaxIDint = currMaxID;
		}
		firstMaxIDint++;
		System.out.println(firstMaxIDint);
		id = String.valueOf(firstMaxIDint);
		System.out.println(id);
		Product generic = new Product(id, "Name", 0.0, "172D42", "/javafx_images/CustomOrderPicture.png", 0, "Type",
				"UNDEFINED", false, 0.0);
		productsListView.add(generic);
		productsTable.setItems(productsListView);
		addProductsList.add(generic);
	}

	@FXML
	void SaveUpdates(ActionEvent event) {
		boolean add = true, remove = true, edit = true;
		/**
		 * First, we are sending the products that was removed from by the marketing
		 * worker to the DB
		 */
		if (!removeProductsListString.isEmpty()) {
			System.out.println("String remove list:\n " + removeProductsListString);
			System.out.println("Products remove list:\n " + removeProductsList);
			remove = ClientHandleTransmission.RemoveProductsFromCatalog(removeProductsListString);
			removeProductsListString.clear();

		}
		/**
		 * Second, we are sending the edited original products list to the DB
		 */
		System.out.println("edit list at start:\n " + editedProductsList);
		editedProductsList.addAll((List<Product>) productsListView);
		System.out.println("edit list after add the backup list:\n " + editedProductsList);
		editedProductsList.removeAll(addProductsList);
		System.out.println("edit list after remove the removed products:\n " + editedProductsList);
		if (!editedProductsList.isEmpty()) {
			System.out.println("the edit list that sent to DB:\n " + editedProductsList);
			edit = ClientHandleTransmission.EditProductsInCatalog(editedProductsList);
			editedProductsList.clear();
		}
		System.out.println("finish");
		if (add == false || remove == false || edit == false) {
			successFailLbl.setText("Saved Changes Failed");
			successFailLbl.setTextFill(Color.RED);
		} else {
			successFailLbl.setText("Saved Changes Success");
			successFailLbl.setTextFill(Color.GREEN);
		}
		/**
		 * Third, we are sending all the products that we will add to the catalog to
		 * the DB
		 */
		System.out.println("add list after press save\n" + addProductsList);
		if (!addProductsList.isEmpty()) {
			for (int i = 0; i < addProductsList.size(); i++) {
				if (addProductsList.get(i).getName().equals("Name") || backUpList.contains(addProductsList.get(i))) {
					productsListView.remove(addProductsList.get(i));
					addProductsList.remove(addProductsList.get(i));
				} else if (addProductsList.get(i).getPrice() == 0) {
					System.out.println("price is 0.0");
					productsListView.remove(addProductsList.get(i));
					addProductsList.remove(addProductsList.get(i));

				} else if (addProductsList.get(i).getItemType().equals("Type")) {
					System.out.println("didnt chose type");
					productsListView.remove(addProductsList.get(i));
					addProductsList.remove(addProductsList.get(i));
				} else {
					System.out.println(addProductsList.get(i) + " is good product");
				}
			}
			System.out.println("final add list:\n " + addProductsList);
			add = ClientHandleTransmission.AddNewProductsToCatalog(addProductsList);
			addProductsList.clear();
		}
		return;
	}
}
