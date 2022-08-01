package client;

import java.util.ArrayList;
import java.util.List;

import communication.TransmissionPack;
 /**
  * this class should be used for notify all the clients for specific action , but during the implementation we found out that 
  * its more comftrable to us to work if different way. (using clientHandleTransmission and screen listeners)
  * @author 
  *
  */
public class MissionAnalyzeClient {
	private static final List<zerliClientListeners> clientlisteners = new ArrayList<zerliClientListeners>();

	public static boolean MissionsAnalyzeClient(TransmissionPack obj) {
		ClientController.setObj(obj);
		switch (obj.getResponse()) {
		case UPDATE_CONNECTION_SUCCESS: {
			for (zerliClientListeners client : clientlisteners) {
				client.ipConfirmedForClient();
			}
			break;
		}
		case UPDATE_CONNECTION_FAILD:{
			for (zerliClientListeners client : clientlisteners) {
				client.ipNotConfirmedForClient();
			}
			break;
		}
		case NOTIFY_CUSTOMER_SERVICE:{
			for (zerliClientListeners client : clientlisteners) {
				client.notifyCustomerService();
			}
			break;
			
		}
		case UPDATE_DISCONNECTION_SUCCESS: {

		}
		case FOUND_ORDER: {

		}
		case DIDNT_FOUND_ORDER: {

		}
		case INSERT_ORDER_SUCCESS: {

		}
		case INSERT_ORDER_FAILD: {

		}
		case EDIT_ORDER_SUCCESS: {

			
		}
		case EDIT_ORDER_FAILD: {
			
		}

		case REMOVE_ORDER_FAILD:
			break;
		case REMOVE_ORDER_SUCCESS:
			break;
		case USER_EXIST:{
			notifyAllSpecificListners(obj);
			break;

		}
		case FOUND_COLORS:{
		break;	
		}
		case DID_NOT_FIND_COLORS:{
			break;
		}
		
			
		case USER_NAME_OR_PASSWORD_INCORRECT:
			break;
		case USER_NOT_EXIST:
			break;
		default:
			break;
		}return false;

	}

	private static void notifyAllSpecificListners(TransmissionPack obj) {
		switch(obj.getInformation().toString()) {
		case "Customer": {
			System.out.println(obj.getInformation());
		for (zerliClientListeners client : clientlisteners) {
			client.userIsCustomer();
		}
		break;
		}
		case "Branch Manager": {
			for (zerliClientListeners client : clientlisteners) {
				client.userIsBranchManager();
			}
			break;
			}
		case "Customer Service": {
			System.out.println(obj.getInformation());
		for (zerliClientListeners client : clientlisteners) {
			client.userIsCustomerService();
		}
		break;
		}

		}
	}

	public static void addClientListener(zerliClientListeners listener) {
		clientlisteners.add(listener);
	}

	public static void removeClientListener(zerliClientListeners listener) {
		clientlisteners.remove(listener);
	}
}
