package entities_catalog;

import java.io.Serializable;
import java.util.Objects;

/* this class represent a Product In Branch because any branch has different supplies management
 * @Author Almog Madar
 */

public class ProductInBranch implements Serializable {
	
	@Override
	public int hashCode() {
		return Objects.hash(branchID, productID, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductInBranch other = (ProductInBranch) obj;
		return Objects.equals(branchID, other.branchID) && Objects.equals(productID, other.productID)
				&& quantity == other.quantity;
	}

	private static final long serialVersionUID = 1L;
	private String branchID;
	private  String productID;
	private int quantity;
	/**
	 * 
	 * @param branchID
	 * @param productID
	 * @param quantity
	 */
	public ProductInBranch(String branchID, String productID, int quantity) {
		this.branchID = branchID;
		this.productID = productID;
		this.quantity = quantity;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID: "+getBranchID()+" ProductID :"+getProductID()+" Quantity :"+getQuantity();
	}
	
	
	
	
}
