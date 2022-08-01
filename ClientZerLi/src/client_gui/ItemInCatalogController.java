package client_gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import client.OrderHandleController;
import entities_catalog.Product;
import entities_catalog.ProductInOrder;
import entities_users.Customer;
import enums.AccountStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemInCatalogController {

    @FXML
    private ImageView itemImageScrollArea;

    @FXML
    private Label itemNameScrollArea;

    @FXML
    private Label itemPriceScrollArea;
    
    @FXML
    private ImageView saleImage;
      
    private Product item;
    private String CURRENCY="₪";
    private MyListenerCatalog myListener; 
   
    @FXML
    private void clickItem(MouseEvent mouseEvent) {
    	if(((Customer) ClientController.user).getAccountStatus()==AccountStatus.CONFIRMED) {
    	myListener.onClickListener(item);
    	}
    
    }
    
    
    public void setDataItem(Product tmpItem , MyListenerCatalog tmpMyListner) {
    	item=tmpItem;
    	myListener=tmpMyListner;
    	itemNameScrollArea.setText(item.getName());
    	if(tmpItem.getIsOnSale())
    		itemPriceScrollArea.setText(CURRENCY+item.getFixPrice());
    	else
    		itemPriceScrollArea.setText(CURRENCY+item.getPrice());
    	
    	Image image =  new Image(getClass().getResourceAsStream(item.getImgSrc()));
    	itemImageScrollArea.setImage(image);
    	
    	if(item.getIsOnSale()) {
        	Image image2 =  new Image(getClass().getResourceAsStream("/javafx_images/Catalog/saleCatalog.png"));
        	saleImage.setImage(image2);
        	itemPriceScrollArea.setStyle("-fx-text-fill: #D25555;");
    	}

    }
}
