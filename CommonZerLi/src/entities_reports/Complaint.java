package entities_reports;

import java.io.Serializable;

import enums.ComplaintsStatus;

/**
 * class for the complain
 * 
 * @author Mor Ben Haim
 *
 */
public class Complaint implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * every complaint have his complaintID, customerID,orderID, customerServiceID,
	 * description, branchID, complaintOpening, treatmentUntil,refoundAmount
	 */
	private String complaintID, customerID, orderID, customerServiceID, description, branchID, complaintOpening,
			treatmentUntil, refoundAmount;
	/**
	 * every complaint have his complaintsStatus, complainState
	 */
	private ComplaintsStatus complaintsStatus, complainState;
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
	public Complaint(String complaintID, String customerID, String orderID, String customerServiceID,
			String description, String branchID, String complaintOpening, String treatmentUntil,
			ComplaintsStatus complainState, ComplaintsStatus complaintsStatus) {
		super();
		this.complaintID = complaintID;
		this.customerID = customerID;
		this.orderID = orderID;
		this.customerServiceID = customerServiceID;
		this.description = description;
		this.branchID = branchID;
		this.complaintOpening = complaintOpening;
		this.treatmentUntil = treatmentUntil;
		this.complainState = complainState;
		this.complaintsStatus = complaintsStatus;
	}
	
	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getRefoundAmount() {
		return refoundAmount;
	}

	public void setRefoundAmount(String refoundAmount) {
		this.refoundAmount = refoundAmount;
	}

	public ComplaintsStatus getComplainState() {
		return complainState;
	}

	public void setComplainState(ComplaintsStatus complainState) {
		this.complainState = complainState;
	}

	public ComplaintsStatus getComplaintsStatus() {
		return complaintsStatus;
	}

	public void setComplaintsStatus(ComplaintsStatus complaintsStatus) {
		this.complaintsStatus = complaintsStatus;
	}

	public String getComplaintOpening() {
		return complaintOpening;
	}

	public void setComplaintOpening(String complaintOpening) {
		this.complaintOpening = complaintOpening;
	}

	public String getTreatmentUntil() {
		return treatmentUntil;
	}

	public void setTreatmentUntil(String treatmentUntil) {
		this.treatmentUntil = treatmentUntil;
	}

	public String getComplaintID() {
		return complaintID;
	}

	public void setComplaintID(String complaintID) {
		this.complaintID = complaintID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerServiceID() {
		return customerServiceID;
	}

	public void setCustomerServiceID(String customerServiceID) {
		this.customerServiceID = customerServiceID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((complaintID == null) ? 0 : complaintID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complaint other = (Complaint) obj;
		if (complaintID == null) {
			if (other.complaintID != null)
				return false;
		} else if (!complaintID.equals(other.complaintID))
			return false;
		return true;
	}

}
