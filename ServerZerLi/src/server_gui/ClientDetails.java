package server_gui;

import java.util.Objects;

/** This class is util class for TableView that will be on the server screen
 * The use for observable-list
 *
 */
public class ClientDetails {
	private String ip, hostName, status;

	public ClientDetails(final String ip, final String hostName, final String status) {
		this.ip=ip;
		this.hostName=hostName;
		this.status=status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientDetails other = (ClientDetails) obj;
		return Objects.equals(hostName, other.hostName) && Objects.equals(ip, other.ip)
				&& Objects.equals(status, other.status);
	}

}
