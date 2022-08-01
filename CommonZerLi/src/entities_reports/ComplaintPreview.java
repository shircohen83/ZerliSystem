package entities_reports;

import enums.ComplaintsStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
/**
 * class for holding the complaints for presenting it int the screen
 * it his subclass of the Complaint
 * @author Mor Ben Haim
 *
 */
public class ComplaintPreview extends Complaint{

	private static final long serialVersionUID = 1L;
	/**
	 * every ComplaintPreview have it own combobox with the specific state
	 */
	private  ComboBox<ComplaintsStatus> status = new ComboBox<>();
	/**
	 * every ComplaintPreview have it own ObservableList to be able to 
	 * Update in real time when other screen is updating this complaint
	 */
	private  ObservableList<ComplaintsStatus> box = FXCollections.observableArrayList(ComplaintsStatus.OPEN,	
			ComplaintsStatus.CLOSE);
	/**
	 * 
	 * @param complaintID
	 * @param customerID
	 * @param orderID
	 * @param customerServiceID
	 * @param description
	 * @param branchID
	 * @param complaintOpening
	 * @param treatmentUntil
	 * @param complainState
	 * @param complaintsStatus
	 */
	public ComplaintPreview(String complaintID, String customerID,String orderID, String customerServiceID, String description,
			String branchID, String complaintOpening, String treatmentUntil, ComplaintsStatus complainState,
			ComplaintsStatus complaintsStatus) {
		super(complaintID, customerID,orderID, customerServiceID, description, branchID, complaintOpening, treatmentUntil,
				complainState, complaintsStatus);
		
		status.setItems(box);

	}
	public ObservableList<ComplaintsStatus> getBox() {
		return box;
	}
	public void setBox(ObservableList<ComplaintsStatus> box) {
		this.box = box;
	}
	public ComboBox<ComplaintsStatus> getStatus() {
		return status;
	}
	public void setStatus(ComboBox<ComplaintsStatus> status) {
		this.status = status;
	}

}
