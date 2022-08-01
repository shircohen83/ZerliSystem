package entities_general;

import java.util.List;

import entities_catalog.ProductInOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

/**
 * class for holding the Custom Orders for presenting it into the screen
 * it his subclass of the Order
 * @author shir cohen , almog mader
 *
 */
public class OrderCustomCartPreview {
	private ImageView imgSrc;
	private String name;
	private int quantity;
	public double totalprice = 0;
	private ObservableList<ProductInOrder> cartList = FXCollections.observableArrayList();
/**
 * 
 * @param imgSrc
 * @param name
 * @param quantity
 * @param price
 * @param cartList
 */
	public OrderCustomCartPreview(ImageView imgSrc, String name, int quantity, double price,
			List<ProductInOrder> cartList) {
		this.imgSrc = imgSrc;
		this.name = name;
		this.quantity = quantity;
		this.cartList.addAll(cartList);
		for (ProductInOrder p : cartList)
			totalprice += p.getPrice() * p.getProductQuantityInCart();
	}

	public ImageView getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(ImageView imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	public ObservableList<ProductInOrder> getCartList() {
		return cartList;
	}

	public void setCartList(ObservableList<ProductInOrder> cartList) {
		this.cartList = cartList;
	}

	public void removeProductInOrderInsideCustom(ObservableList<ProductInOrder> productsToRemove) {
		for (ProductInOrder p : productsToRemove) {
			totalprice -= p.getPrice() * p.getProductQuantityInCart();
		}
		cartList.removeAll(productsToRemove);

		System.out.println("totalPrice->" + totalprice + " cartList->" + cartList);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name.toString() + " " + totalprice;
	}

}
