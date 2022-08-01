package DataBase;

import communication.Response;
import communication.TransmissionPack;
import server_gui.ServerScreenController;

/**
 * This class will be utils functions
 *
 */

public class Utils {
	/**
	 * This method will add the user that connected to the server , into his window
	 * on the server (by calling ServerScreenController.setObser and updating the
	 * obj.response if its success or not.
	 */
	public static void updateConnectionWindow(TransmissionPack obj) {

		if (obj.getInformation() != null && ServerScreenController.SetObser(obj)) {
			obj.setResponse(Response.UPDATE_CONNECTION_SUCCESS);
		} else
			obj.setResponse(Response.UPDATE_CONNECTION_FAILD);
	}

	// in this method we removing client from the server
	public static void updateDisConnectionWindow(TransmissionPack obj) {
		if (obj.getInformation() != null) {
			obj.setResponse(Response.UPDATE_DISCONNECTION_SUCCESS);
			ServerScreenController.RemoveObser(obj);
		} else
			obj.setResponse(Response.UPDATE_DISCONNECTION_FAILD);
	}

}
