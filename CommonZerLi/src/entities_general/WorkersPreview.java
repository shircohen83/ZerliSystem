package entities_general;

import entities_users.ShopWorker;
import enums.AccountStatus;
import enums.ShopworkerRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
/**
 * class for holding the workers for presenting it into the screen
 * it his subclass of the ShopWorker
 * @author shir cohen 
 *
 */
public class WorkersPreview extends ShopWorker
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * every WorkersPreview have it own combobox with the specific state
	 */
	private ComboBox<ShopworkerRole> activityStatus=new ComboBox<>();
	
	
	public ComboBox<ShopworkerRole> getActivityStatusCB() {
		return activityStatus;
	}

	public void setActivityStatusCB(ComboBox<ShopworkerRole> activityStatus) {
		this.activityStatus = activityStatus;
	}


/**
 * 
 * @param iD
 * @param firstName
 * @param lastName
 * @param email
 * @param phoneNumber
 * @param accountStatus
 * @param isLoggedIn
 * @param branchID
 * @param activityStatus
 */
	public WorkersPreview(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn, String branchID,ShopworkerRole activityStatus) {
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn, branchID,activityStatus);
		
		ObservableList<ShopworkerRole> activityList=FXCollections.observableArrayList(ShopworkerRole.SURVEY,ShopworkerRole.GENERAL);
		this.activityStatus.setItems(activityList);
		this.activityStatus.setValue(activityStatus);
		
	
	}
	
	
	

}
