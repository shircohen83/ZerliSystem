package client;

import java.util.List;

import entities_reports.ComplaintPreview;
import enums.ComplaintsStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * handler to enable multiple screen to communicated with the current values
 * @author Mor Ben Haim
 *
 */
public class ComplaintsDataHandle {
	/**
	 * Complain from the specific row 
	 * in the TableView 
	 */
	private static ComplaintPreview complaint;
	/**
	 * List of all the complaints from the DB
	 */
	private static List<ComplaintPreview> comlaints;
	/**
	 * combobox that contains the specific status of
	 * the complaints
	 */
	private static ComboBox<ComplaintsStatus> status = new ComboBox<>();
	/**
	 * Observer List that react to change of the specific complaints
	 * if it change in other screen it will be update it on the screen also
	 */
	private static ObservableList<ComplaintsStatus> box = FXCollections.observableArrayList(ComplaintsStatus.OPEN,
			ComplaintsStatus.CLOSE);
	/**
	 * get all the complaints
	 * @return
	 */
	public static List<ComplaintPreview> getComlaints() {
		return comlaints;
	}
	/**
	 * set all the complaints
	 * @return
	 */
	public static void setComlaints(List<ComplaintPreview> comlaints) {
		ComplaintsDataHandle.comlaints = comlaints;
	}
	/**
	 * get the combobox
	 * @return
	 */
	public static ComboBox<ComplaintsStatus> getStatus() {
		return status;
	}
	/**
	 * set the combobox
	 * @param status
	 */
	public static void setStatus(ComboBox<ComplaintsStatus> status) {
		ComplaintsDataHandle.status = status;
	}
	/**
	 * get ObservableList 
	 * @return
	 */
	public static ObservableList<ComplaintsStatus> getBox() {
		return box;
	}
	/**
	 * set the ObservableList
	 * @param box
	 */
	public static void setBox(ObservableList<ComplaintsStatus> box) {
		ComplaintsDataHandle.box = box;
	}

	/**
	 * get the specific complaint
	 * @return
	 */
	public static ComplaintPreview getComplaint() {
		return complaint;
	}
	/**
	 * set the specific complain
	 * @param complaint
	 */
	public static void setComplaint(ComplaintPreview complaint) {
		ComplaintsDataHandle.complaint = complaint;
	}

}
