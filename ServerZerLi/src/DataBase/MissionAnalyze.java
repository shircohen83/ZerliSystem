package DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import communication.TransmissionPack;
import entities_users.User;

/**
 * In this class we analyze the mission that we got from the client , and then
 * we go into the right queries(ServerQuries) by switch case. according to the
 * specific mission each case will handle specific mission
 */
public class MissionAnalyze {
	public static ArrayList<String> list = new ArrayList<String>();

	@SuppressWarnings("incomplete-switch")
	public static void MissionsAnalyze(TransmissionPack obj, Connection con) {

		switch (obj.getMission()) {
		case ADD_ORDER: {
			ServerQuaries.addOrderInDB(obj, con);

			break;
		}
		case EDIT_ORDER: {
			break;
		}
		case GET_ORDER: {
			ServerQuaries.getOrders(obj, con);
			break;
		}

		case SEND_CONNECTION_DETAILS: {

			Utils.updateConnectionWindow(obj);
			break;
		}
		case SEND_DISCONNECT_DETAILS: {

			Utils.updateDisConnectionWindow(obj);
			break;
		}

		case USER_LOGIN: {
			ServerQuaries.Login(obj, con);
			break;
		}
		case USER_LOGOUT: {
			ServerQuaries.logout(obj, con);
			break;

		}
		case GET_SHOP_WORKERS: {
			ServerQuaries.GetShopWorkersFromDB(obj, con);
			break;

		}
		case DATA_PRODUCTS: {
			ServerQuaries.GetProducts(obj, con);
			break;
		}
		case DATA_PRODUCTS_BY_FILTER: {
			ServerQuaries.GetProductsByFilter(obj, con);
			break;
		}
		case GET_COLORS: {
			ServerQuaries.getColors(obj, con);
			break;
		}
		case GET_PENDING_CUSTOMERS: {
			ServerQuaries.getPendingCustomersFromDB(obj, con);
			break;
		}
		case APPROVE_NEW_CUSTOMER: {
			ServerQuaries.approveNewCustomerToDB(obj, con);
			break;
		}
		case GET_CREDIT_CARDS: {
			ServerQuaries.getCreditCardsFromDB(obj, con);
			break;
		}
		case GET_APPROVED_CUSTOMERS: {
			ServerQuaries.GetCustomersFromDB(obj, con);
			break;
		}
		case UPDATE_EDITED_CUSTOMERS: {
			ServerQuaries.updateCustomersAfterEdit(obj, con);
			break;
		}

		case UPDATE_EDITED_WORKERS: {
			ServerQuaries.updateWorkersAfterEdit(obj, con);
			break;
		}
		case GET_COMPLAINTS: {
			ServerQuaries.getComlaints(obj, con);
			break;
		}
		case UPDATE_COMPLAINTS: {
			ServerQuaries.updateComplaints(obj, con);
			break;
		}

		case OPEN_COMPLAINT: {
			try {
				ServerQuaries.openComplaint(obj, con);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;

		}
		case GET_MONTHLY_REPORT: {
			ReportsQuaries.getMonthlyReport(obj, con);
			break;
		}
		case GET_QUARTER_INCOME_REPORT: {
			ReportsQuaries.getQuarterIncomeReport(obj, con);
			break;
		}
		case GET_BRANCHID_BY_USER: {

			String branchID = null;
			branchID = ServerQuaries.getBranchId((User) obj.getInformation(), con);
			obj.setInformation(branchID);
			break;
		}
		case GET_YEARS_FOR_COMOBOX: {
			ReportsQuaries.getYears(obj, con);
			break;
		}
		case GET_SURVEY_REPORT: {
			try {
				ReportsQuaries.getSurveyReport(obj, con);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case INSERT_SURVEY_BY_EXPERT: {
			ReportsQuaries.insertSurveyResult(obj, con);
			break;
		}
		case GET_QUARTER_COMPLAINTS_REPORT: {
			ReportsQuaries.getQuarterComplaintsReport(obj, con);

			break;
		}

		case GET_BRANCHES: {
			ServerQuaries.getBranches(obj, con);
			break;
		}

		case GET_DELIVERIES: {
			ServerQuaries.GetDeliveriesFromDB(obj, con);
			break;
		}
		case UPDATE_DELIVERIES_STATUSES: {
			ServerQuaries.UpdateDeliveriesStatusesInDB(obj, con);
			break;
		}

		case UPADTE_ORDER: {
			ServerQuaries.updateOrder(obj, con);
			break;
		}
		case GET_SURVEY_QUESTIONS: {
			ServerQuaries.getSurvyQuestions(obj, con);
			break;
		}
		case INSERT_SURVY: {
			ServerQuaries.insertSurvy(obj, con);
			break;
		}

		case GET_PRODUCT_IN_BRANCH: {
			ServerQuaries.getProductInBranch(obj, con);
			break;
		}
		case ADD_DELIVERY: {
			ServerQuaries.addDelivery(obj, con);
			break;
		}
		case GET_CUSTOMER_ORDERS_CANCELATION: {
			ServerQuaries.getCustomerOrdersCancelation(obj, con);
			break;
		}
		case GET_CUSTOMER_ORDERS_HISTORY: {
			ServerQuaries.getCustomerOrdersHistory(obj, con);
			break;
		}
		case EXTERNAL_IMPOART:{
			ExternalDBquaries.ImportUsersData(obj,con);
			break;
		}
		case DELIVERY_LATE_REFUND: {
			ServerQuaries.UpdateDeliveryWasLateDB(obj, con);
			break;
		}
		case GET_BRANCH_NAME_BY_ID:{
			String branchNameString = ReportsQuaries.getBranchNamebyBranchID((String)obj.getInformation(),con);
			obj.setInformation(branchNameString);
			break;
		}
		case GET_CUSTOMER_EMAIL_PHONE:{
			ServerQuaries.getCustomerEmailAndPhoneFromDB(obj,con);
			break;
		}


		case GET_CUSTOMER_DETAILS:{
			ServerQuaries.getCustomerDetailsFromDB(obj, con);
			break;
		}
		case GET_CUTOMER_TO_NOTIFY:{
			ServerQuaries.getCustomerDetailsForNotify(obj, con);
			break;
		}

		case CANCEL_ORDER_BY_CUSTOMER: {
			ServerQuaries.cancelOrderByCustomer(obj, con);
			break;
		}
		case GET_CUSTOMER_ORDERS_WAITING_CANCELATION: {
			ServerQuaries.getCustomerOrdersCancelationWaiting(obj, con);

			break;
		}


		case GET_MAX_PRODUCT_ID:{
			ServerQuaries.getMaxProductIDFromDB(obj,con);
			break;
		}
		case REMOVE_PRODUCTS_FROM_CATALOG:{
			ServerQuaries.marketingWorkerRemoveFromCatalog(obj, con);
			break;
		}
		case ADD_PRODUCTS_TO_CATALOG:{
			ServerQuaries.marketingWorkerAddToCatalog(obj, con);
			break;
			
		}
		case EDIT_PRODUCTS_IN_CATALOG:{
			ServerQuaries.marketingWorkerEditCatalog(obj, con);
			break;
		}

		}

	}
}