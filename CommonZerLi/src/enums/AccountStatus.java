package enums;

/**
 * This Enum is used to describe status of a user in the system.
 * @author Omri Shalev
 */
public enum AccountStatus {
	CONFIRMED("CONFIRMED", 0),
	PENDING_APPROVAL("PENDING_APPROVAL", 1),
	FROZEN("FROZEN", 2);

	private AccountStatus(String string, final int serialNumber) {
	}
}
