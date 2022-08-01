package client;
/**
 * in this class we should handle the client listeners , but in fact we didnt use this class , we choose to implements the porpse in different way
 * @author Dvir bublil
 *
 */
public interface zerliClientListeners {
	default void ipConfirmedForClient() {
		return;
	}

	default void ipNotConfirmedForClient() {
		return;
	}

	default void userIsCustomer() {
		return;
	}
	default void userIsBranchManager() {
		return;
	}

	default void userIsCustomerService() {
		return;
	}

	default void userIsDeliveryAgent(){
		return;
	}

	default void userIsMarketingWorker(){
		return;
	}

	default void userIsNetworkManager(){
		return;
	}

	default void userIsServiceExpert(){
		return;
	}

	default void userIsShopWorker(){
		return;
	}

	default void notifyCustomerService() {
		return;
	}

}
