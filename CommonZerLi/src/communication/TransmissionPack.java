package communication;

import java.io.Serializable;

/**This class will create instance's that will be the 'Package' that passing by the ocsf from the server
 * to the client and also from the client to the server.
 * each instance will have a mission(from client) response(from the server) and information(the data that will
 * be pass between the server and the client)
 * this class implement command design pattern we will use it to transfer the data from the client to the server 
 * @author mor ben haim
 *
 */
public class TransmissionPack implements Serializable{
	private static final long serialVersionUID = 1L;
	private Mission mission;
	private Response response;
	private Object information;

	public TransmissionPack(Mission mission, Response response, Object information) {

		this.mission = mission;
		this.response = response;
		this.information = information;
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Object getInformation() {
		return information;
	}

	public void setInformation(Object information) {
		this.information = information;
	}
	@Override
	public String toString() {
		return "You try to : " +getMission()+"  "+ "The mission is : "+getResponse();
	}
}
