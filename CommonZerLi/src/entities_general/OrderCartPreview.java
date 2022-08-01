package entities_general;

import entities_catalog.ProductInOrder;
import javafx.scene.image.ImageView;
/**
 * class for holding the regular products on the cart for presenting it into the screen
 * it his subclass of the Order
 * @author almog mader
 *
 */
public class OrderCartPreview {
	private ImageView imgSrc;
	private String name;
	private int quantity;
	private double price;
	private ProductInOrder product;
	public static double totalprice;
	/**
	 * 
	 * @param imgSrc
	 * @param name
	 * @param quantity
	 * @param price
	 * @param product
	 */
	public OrderCartPreview(ImageView imgSrc, String name, int quantity, double price,
			ProductInOrder product) {
		this.imgSrc = imgSrc;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.product=product;
		OrderCartPreview.totalprice+=price*quantity;
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

	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public ProductInOrder getProduct() {
		return product;
	}

	public void setProduct(ProductInOrder product) {
		this.product = product;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName().toString();
	}

	public double getTotalprice() {
		return totalprice;
	}


	public void setTotalprice(double totalprice) {
		OrderCartPreview.totalprice = totalprice;
	}
	
}
