package DataBase;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import communication.Response;
import communication.TransmissionPack;
import entities_reports.Report;
import enums.ReportDuration;
import enums.ReportType;

/**
 * In this class we handling with the reportsQuaries that requaird approach to
 * the DB .
 * 
 * @author Dvir Bublil
 *
 */
public class ReportsQuaries {

	/**
	 * in this method we getting the monthlyReport and insert it into report
	 * instance we convert the blob into byte array then we send it back to the
	 * client .
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void getMonthlyReport(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<String> reportDetails = new ArrayList<>();
			reportDetails = (List<String>) obj.getInformation();

			String branchID = reportDetails.get(0);
			String year = reportDetails.get(1);
			String month = reportDetails.get(2);
			String reportType = reportDetails.get(3).toUpperCase();
			String duration = "MONTHLY";
			try {
				gettingTheMonthlyReportFromTheDB(obj, con, branchID, year, month, reportType, duration);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			obj.setResponse(Response.NOT_FOUND_MONTHLY_REPORT);
			obj.setInformation(null);
		}
	}

	/**
	 * in this method we getting the report from the data base and insert it into
	 * our report obj.
	 * 
	 * @param obj
	 * @param con
	 * @param branchID
	 * @param year
	 * @param month
	 * @param reportType
	 * @param duration
	 * @throws SQLException
	 */
	private static void gettingTheMonthlyReportFromTheDB(TransmissionPack obj, Connection con, String branchID,
			String year, String month, String reportType, String duration) throws SQLException {
		Report returnReport = null;
		ResultSet rs;
		Statement stmt;
		String query = "SELECT * FROM zerli.reports WHERE reportDuration='" + duration + "' AND branchID='" + branchID
				+ "' AND reportType='" + reportType + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			Blob b = rs.getBlob(6);
			byte[] buff = b.getBytes(1, (int) b.length());
			if (getMonthFormat(rs.getDate(7).toLocalDate().getMonth().getValue()).equals(month)
					&& rs.getDate(7).toLocalDate().getYear() == Integer.parseInt(year)) {
				returnReport = new Report(rs.getString(1), ReportType.valueOf(rs.getString(2)), rs.getString(3),
						rs.getString(4), ReportDuration.valueOf(rs.getString(5)), buff, rs.getDate(7).toLocalDate());
			}
		}
		rs.close();
		if (returnReport != null) {
			obj.setResponse(Response.FOUND_MONTHLY_REPORT);
			obj.setInformation(returnReport);
			return;
		} else {
			obj.setResponse(Response.NOT_FOUND_MONTHLY_REPORT);
			obj.setInformation(null);
		}
	}

	/**
	 * this method gets a month number and convert it into string .
	 * 
	 * @param month
	 * @return
	 */
	@SuppressWarnings("unused")
	protected static String getMonthFormat(int month) {
		String fixMonth;
		return fixMonth = month < 9 ? fixMonth = ("0" + (month)) : (fixMonth = String.valueOf(month));
	}

	/**
	 * in this method we getting the list of lists that contains all the products
	 * that been orders on specific branch
	 * 
	 * @param tp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<List<String>> gettingOrderMonthlyData(TransmissionPack tp) {
		List<List<String>> productsFromAllTheOrders = new ArrayList<>();
		ResultSet rs;
		Statement stmt;
		List<Object> information = new ArrayList<>();
		information = (List<Object>) tp.getInformation();
		String branchID = (String) information.get(0);
		String month = (String) information.get(1);
		String year = (String) information.get(2);
		List<String> ordersID = new ArrayList<>();
		Connection con = DBController.getCon();
		gettingTheRelavantOrdersID(branchID, month, year, ordersID, con);

		if (ordersID.size() > 0) {
			for (int i = 0; i < ordersID.size(); i++) {
				String getBranchOrderProucts = "SELECT itemType,productQuantityInOrder,nameOfItem from zerli.productinorder WHERE orderID='"
						+ ordersID.get(i) + "';";

				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery(getBranchOrderProucts);
					while (rs.next()) {
						List<String> productsFromOrder = new ArrayList<>();
						productsFromOrder.add(rs.getString(1));
						productsFromOrder.add(rs.getString(2));
						productsFromOrder.add(rs.getString(3));
						productsFromAllTheOrders.add(productsFromOrder);
					}
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productsFromAllTheOrders;

		}
		return productsFromAllTheOrders;
	}

	/**
	 * in this method we creating monthly report for specific branch and specific
	 * report
	 * 
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	public static void createMonthlyReport(List<Object> object) {
		String branchID = (String) object.get(0);
		String Date = (String) object.get(2);
		String type = (String) object.get(3);
		Connection con = DBController.getCon();
		PreparedStatement stmt;
		List<List<String>> orders = (List<List<String>>) object.get(1);
		String branchName = getBranchNamebyBranchID(branchID, con);
		StringBuilder stringBuilder = new StringBuilder();
		/**
		 * this if statement are for different type of reports. (in our case its
		 * orders/income , its can be modify easy in the future).
		 */
		if (branchName != null) {
			stringBuilder.append(branchID + " " + branchName + " " + Date);
			if (type.equals(ReportType.ORDERS.name())) {
				for (List<String> row : orders) {
					stringBuilder.append("\n" + row.get(0) + " " + row.get(1) + " " + row.get(2));
				}
			} else {
				for (List<String> row : orders)
					stringBuilder.append("\n" + row.get(0) + " " + row.get(1) + " " + row.get(2) + " " + row.get(3));
			}

			byte[] byteConent = stringBuilder.toString().getBytes();
			Blob blob = null;
			String query = "INSERT INTO zerli.reports(reportID, reportType, branchID, reportCreator, reportDuration, reportFile, reportDate) VALUES (?,?,?,?,?,?,?)";
			try {
				blob = new SerialBlob(byteConent);
				stmt = con.prepareStatement(query);
				stmt.setString(1, null);
				stmt.setString(2, type);
				stmt.setString(3, branchID);
				stmt.setString(4, "System");
				stmt.setString(5, ReportDuration.MONTHLY.name());
				stmt.setBlob(6, blob);
				stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * this method return the branch name by using the branchID
	 * 
	 * @param ID
	 * @param con
	 * @return
	 */
	protected static String getBranchNamebyBranchID(String ID, Connection con) {
		ResultSet rs;
		Statement stmt;
		String branchName = null;
		String getBranchName = "SELECT branchName FROM zerli.branchs WHERE branchID='" + ID + "';";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getBranchName);
			if (rs.next() == true)
				branchName = rs.getString(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branchName;
	}

	/**
	 * in this method we getting all the monthly report information from the DB
	 * 
	 * @param tp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<List<String>> gettingIncomeMonthlyData(TransmissionPack tp) {
		List<List<String>> incomeFromAllOrders = new ArrayList<>();
		ResultSet rs;
		Statement stmt;
		List<Object> information = new ArrayList<>();
		information = (List<Object>) tp.getInformation();
		String branchID = (String) information.get(0);
		String date = (String) information.get(1);
		String year = (String) information.get(2);
		List<String> ordersID = new ArrayList<>();
		Connection con = DBController.getCon();
		/**
		 * getting all the orders id to the ordersID list and in parallel with getting
		 * the orders date's
		 */
		List<LocalDate> ordersDate = gettingTheRelavantOrdersID(branchID, date, year, ordersID, con);

		if (ordersID.size() > 0) {
			for (int i = 0; i < ordersID.size(); i++) {
				String getBranchIncomeProucts = "SELECT itemType,productQuantityInOrder,price from zerli.productinorder WHERE orderID='"
						+ ordersID.get(i) + "';";
				LocalDate LocalDate = ordersDate.get(i);

				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery(getBranchIncomeProucts);
					while (rs.next()) {
						List<String> productsFromOrder = new ArrayList<>();
						productsFromOrder.add(rs.getString(1));
						productsFromOrder.add(Integer.toString(LocalDate.getDayOfMonth()));
						productsFromOrder.add(rs.getString(2));
						productsFromOrder.add(rs.getString(3));
						incomeFromAllOrders.add(productsFromOrder);
					}
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return incomeFromAllOrders;

		}
		return incomeFromAllOrders;
	}

	/*
	 * this method getting the orders id's by branchID and the relevant date (for
	 * example all the orders in 05 month) and also returning the ordersID dates.
	 */
	private static List<LocalDate> gettingTheRelavantOrdersID(String branchID, String month, String year,
			List<String> ordersID, Connection con) {
		ResultSet rs;
		Statement stmt;
		List<LocalDate> ordersDate = new ArrayList<>();
		String getBrnachOrders = "SELECT orderID,orderDate from zerli.order WHERE status='APPROVE_WITH_IMIDATE_DELIVERY' OR status='APPROVE_WITH_DELIVERY' OR status='APPROVE' AND branchID='"
				+ branchID + "';";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getBrnachOrders);
			while (rs.next()) {
				if (getMonthFormat(rs.getDate(2).toLocalDate().getMonth().getValue()).equals(month)
						&& rs.getDate(2).toLocalDate().getYear() == Integer.parseInt(year)) {
					ordersID.add(rs.getString(1));
					ordersDate.add(rs.getDate(2).toLocalDate());
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ordersDate;

	}

	/**
	 * in this method we creating the QuarterReport. here there is implementation of
	 * income and order report .
	 * 
	 * @param obj
	 * @param con
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static boolean createQuarterReportInformation(TransmissionPack obj) {
		if (obj instanceof TransmissionPack) {
			Connection con = DBController.getCon();
			List<String> reportDetails = new ArrayList<>();
			reportDetails = (List<String>) obj.getInformation();
			String branch = reportDetails.get(0);
			String year = reportDetails.get(1);
			String quarter = reportDetails.get(2);
			String reportType = reportDetails.get(3);
			List<List<String>> InfoFirstMonth = new ArrayList<>();
			List<List<String>> InfoSecondMonth = new ArrayList<>();
			List<List<String>> InfoThirdMonth = new ArrayList<>();
			TransmissionPack Month = new TransmissionPack(null, null, null);
			int countMonths = 3;
			switch (Integer.parseInt(quarter)) {
			case 1: {
				InfoFirstMonth = returnMonthInfoForReport(branch, "01", year);
				if (InfoFirstMonth.size() == 0) {
					countMonths--;
				}
				InfoSecondMonth = returnMonthInfoForReport(branch, "02", year);
				if (InfoSecondMonth.size() == 0) {
					countMonths--;
				}
				InfoThirdMonth = returnMonthInfoForReport(branch, "03", year);
				if (InfoThirdMonth.size() == 0) {
					countMonths--;
				}
				break;
			}
			case 2: {
				InfoFirstMonth = returnMonthInfoForReport(branch, "04", year);
				if (InfoFirstMonth.size() == 0) {
					countMonths--;
				}
				InfoSecondMonth = returnMonthInfoForReport(branch, "05", year);
				if (InfoSecondMonth.size() == 0) {
					countMonths--;
				}
				InfoThirdMonth = returnMonthInfoForReport(branch, "06", year);
				if (InfoThirdMonth.size() == 0) {
					countMonths--;
				}
				break;
			}
			case 3: {
				InfoFirstMonth = returnMonthInfoForReport(branch, "07", year);
				if (InfoFirstMonth.size() == 0) {
					countMonths--;
				}
				InfoSecondMonth = returnMonthInfoForReport(branch, "08", year);
				if (InfoSecondMonth.size() == 0) {
					countMonths--;
				}
				InfoThirdMonth = returnMonthInfoForReport(branch, "09", year);
				if (InfoThirdMonth.size() == 0) {
					countMonths--;
				}
				break;
			}
			case 4: {
				InfoFirstMonth = returnMonthInfoForReport(branch, "10", year);
				if (InfoFirstMonth.size() == 0) {
					countMonths--;
				}
				InfoSecondMonth = returnMonthInfoForReport(branch, "11", year);
				if (InfoSecondMonth.size() == 0) {
					countMonths--;
				}
				InfoThirdMonth = returnMonthInfoForReport(branch, "12", year);
				if (InfoThirdMonth.size() == 0) {
					countMonths--;
				}
				break;
			}
			}
			StringBuilder stringBuilder = new StringBuilder();
			String branchName = getBranchNamebyBranchID(branch, con);
			stringBuilder.append(branch + " " + branchName + " " + quarter + " " + year);

			if (reportType.equals(ReportType.ORDERS.name()) && countMonths > 0) {
				if (InfoFirstMonth != null) {
					for (List<String> row : InfoFirstMonth)
						stringBuilder.append("\nmonth1 " + row.get(0) + " " + row.get(1) + " " + row.get(2));
				}
				if (InfoSecondMonth != null) {
					for (List<String> row : InfoSecondMonth)
						stringBuilder.append("\nmonth2 " + row.get(0) + " " + row.get(1) + " " + row.get(2));
				}
				if (InfoThirdMonth != null) {
					for (List<String> row : InfoThirdMonth)
						stringBuilder.append("\nmonth3 " + row.get(0) + " " + row.get(1) + " " + row.get(2));
				}
			} else { // income report
				if (InfoFirstMonth != null) {
					for (List<String> row : InfoFirstMonth)
						stringBuilder.append(
								"\nmonth1 " + row.get(0) + " " + row.get(1) + " " + row.get(2) + " " + row.get(3));
				}
				if (InfoSecondMonth != null) {
					for (List<String> row : InfoSecondMonth)
						stringBuilder.append(
								"\nmonth2 " + row.get(0) + " " + row.get(1) + " " + row.get(2) + " " + row.get(3));
				}
				if (InfoThirdMonth != null) {
					for (List<String> row : InfoThirdMonth)
						stringBuilder.append(
								"\nmonth3 " + row.get(0) + " " + row.get(1) + " " + row.get(2) + " " + row.get(3));
				}
			}
			if (countMonths > 0) {
				PreparedStatement stmt;
				byte[] byteConent = stringBuilder.toString().getBytes();
				Blob blob = null;
				String query = "INSERT INTO zerli.reports(reportID, reportType, branchID, reportCreator, reportDuration, reportFile, reportDate) VALUES (?,?,?,?,?,?,?)";
				try {
					blob = new SerialBlob(byteConent);
					stmt = con.prepareStatement(query);
					stmt.setString(1, null);
					stmt.setString(2, reportType);
					stmt.setString(3, branch);
					stmt.setString(4, "System");
					stmt.setString(5, ReportDuration.QUARTERLY.name());
					stmt.setBlob(6, blob);
					stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * this method returning the monthly report information to be use on the
	 * function above
	 * 
	 * @param branchID
	 * @param month
	 * @param year
	 * @return
	 */
	public static List<List<String>> returnMonthInfoForReport(String branchID, String month, String year) {
		TransmissionPack Month = new TransmissionPack(null, null, null);
		List<List<String>> incomeInfo = new ArrayList<>();
		List<Object> information = new ArrayList<>();
		information.add(branchID);
		information.add(month);
		information.add(year);
		Month.setInformation(information);
		incomeInfo = gettingIncomeMonthlyData(Month);
		return incomeInfo;
	}

	/**
	 * in this method we getting the income quarter report from the DB.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void getQuarterIncomeReport(TransmissionPack obj, Connection con) {

		if (obj instanceof TransmissionPack) {
			List<String> reportRequest = new ArrayList<>();
			reportRequest = (List<String>) obj.getInformation();
			String branchID = reportRequest.get(0);
			String year = reportRequest.get(1);
			String quater = reportRequest.get(2);
			String month = getMonthForQuater(quater);
			Report returnReport = null;
			ResultSet rs;
			Statement stmt;

			String query = "SELECT * FROM zerli.reports WHERE branchID='" + branchID
					+ "' AND reportDuration='QUARTERLY' AND reportType='INCOME'";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next() == true) {

					if (getMonthFormat(rs.getDate(7).toLocalDate().getMonth().getValue()).equals(month)
							&& rs.getDate(7).toLocalDate().getYear() == Integer.parseInt(year)) {
						Blob b = rs.getBlob(6);
						byte[] buff = b.getBytes(1, (int) b.length());
						returnReport = new Report(rs.getString(1), ReportType.valueOf(rs.getString(2)), rs.getString(3),
								rs.getString(4), ReportDuration.valueOf(rs.getString(5)), buff,
								rs.getDate(7).toLocalDate());

					}
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (returnReport != null) {

				obj.setResponse(Response.FOUND_QUARENTY_INCOME_REPORT);
				obj.setInformation(returnReport);
				return;
			} else {
				obj.setResponse(Response.NOT_FOUND_QUARENTY_INCOME_REPORT);
				obj.setInformation(null);
			}
		}
		obj.setResponse(Response.NOT_FOUND_QUARENTY_INCOME_REPORT);
		obj.setInformation(null);
	}

	/**
	 * in this method we getting the month that will represent the quarter
	 * 
	 * @param quater
	 * @return
	 */
	private static String getMonthForQuater(String quater) {
		String returnMonth = null;
		switch (Integer.parseInt(quater)) {
		case 1: {
			return returnMonth = "03";
		}
		case 2: {
			return returnMonth = "06";
		}
		case 3: {
			return returnMonth = "09";
		}
		case 4: {
			return returnMonth = "12";
		}
		}
		return returnMonth;

	}

	/**
	 * this method getting the years that there is reports on them , to show in the
	 * combox
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void getYears(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<String> opreation = (List<String>) obj.getInformation();
			List<String> returnYears = new ArrayList<>();
			String table = opreation.get(1);
			String duration = opreation.get(0);
			String getYearsFromREPORTS = "SELECT reportDate from zerli." + table + " WHERE reportDuration='" + duration
					+ "';";
			System.out.println(getYearsFromREPORTS);
			ResultSet rs;
			Statement stmt;
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(getYearsFromREPORTS);
				while (rs.next()) {
					int year = rs.getDate(1).toLocalDate().getYear();
					if (!returnYears.contains(String.valueOf(year)))
						returnYears.add(String.valueOf(year));
				}
				rs.close();
			} catch (SQLException e) {
				obj.setInformation(null);
				obj.setResponse(Response.REPORT_YEARS_NOT_FOUND);
				e.printStackTrace();
			}
			if (returnYears.size() > 0) {
				obj.setInformation(returnYears);
				obj.setResponse(Response.REPORT_YEARS_FOUND);
				return;
			}
		} else {
			obj.setInformation(null);
			obj.setResponse(Response.REPORT_YEARS_NOT_FOUND);

		}

	}

	/**
	 * in this method we getting the survey report information from the DB, by
	 * specific topic.
	 * 
	 * @param obj
	 * @param con
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void getSurveyReport(TransmissionPack obj, Connection con)
			throws NumberFormatException, SQLException {
		if (obj instanceof TransmissionPack) {
			List<String> opreation = (List<String>) obj.getInformation();
			List<List<String>> surveyResults = new ArrayList<>();
			ResultSet rs = null;
			Statement stmt;
			String branchID = opreation.get(0);
			String Year = opreation.get(1);
			String Month = opreation.get(2);
			String surveyType = opreation.get(3);
			surveyResults.add(opreation);
			String getSurveyQuarie = "SELECT * from zerli.surveys WHERE branchID='" + branchID + "' AND topic='"
					+ surveyType + "';";

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(getSurveyQuarie);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (rs.next()) {
				if (getMonthFormat(rs.getDate(17).toLocalDate().getMonth().getValue()).equals(Month)
						&& rs.getDate(17).toLocalDate().getYear() == Integer.parseInt(Year)) {
					surveyResults.add(Arrays.asList(String.valueOf(rs.getString(10)), String.valueOf(rs.getString(11)),
							String.valueOf(rs.getString(12)), String.valueOf(rs.getString(13)),
							String.valueOf(rs.getString(14)), String.valueOf(rs.getString(15)),
							String.valueOf(rs.getString(16))));
				}
			}
			rs.close();
			if (surveyResults.size() > 1) {
				obj.setInformation(surveyResults);
				obj.setResponse(Response.SURVEY_REPORT_FOUND);
				return;
			}

		} else {
			obj.setInformation(null);
			obj.setResponse(Response.SURVEY_REPORT_NOT_FOUND);
		}

		obj.setInformation(null);
		obj.setResponse(Response.SURVEY_REPORT_NOT_FOUND);
	}

	/**
	 * in this method we handling with the report that we from the service expert
	 * (pdf) and insert it into the DB.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public static void insertSurveyResult(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Object> parameters = new ArrayList<>();
			parameters = (List<Object>) obj.getInformation();
			System.out.println((String) parameters.get(1).toString());
			PreparedStatement stmt;
			Blob blob = null;
			String query = "INSERT INTO zerli.reports(reportID, reportType, branchID, reportCreator, reportDuration, reportFile, reportDate) VALUES (?,?,?,?,?,?,?)";
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(((byte[]) parameters.get(0)));
				blob = new SerialBlob((byte[]) parameters.get(0));
				stmt = con.prepareStatement(query);
				stmt.setString(1, null);
				stmt.setString(2, "SURVEY");
				stmt.setString(3, (String) parameters.get(1));
				stmt.setString(4, "Service Expert");
				stmt.setString(5, ReportDuration.MONTHLY.name());
				stmt.setBlob(6, bais);
				stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setInformation(null);
				obj.setResponse(Response.INSERT_SURVEY_REPORT_FAILD);
			}
			obj.setResponse(Response.INSERT_SURVEY_REPORT_SUCCESS);
			return;
		} else {
			obj.setInformation(null);
			obj.setResponse(Response.INSERT_SURVEY_REPORT_FAILD);
		}
	}

	/**
	 * get the complaints quarter report
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void getQuarterComplaintsReport(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<String> reportRequest = new ArrayList<>();
			reportRequest = (List<String>) obj.getInformation();
			System.out.println(reportRequest);
			String branchID = reportRequest.get(0);
			String year = reportRequest.get(1);
			String quater = reportRequest.get(2);
			String month = getMonthForQuater(quater);
			Report returnReport = null;
			ResultSet rs;
			Statement stmt;

			String query = "SELECT * FROM zerli.reports WHERE branchID='" + branchID
					+ "' AND reportDuration='QUARTERLY' AND reportType='COMPLAINTS'";

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next() == true) {

					if (getMonthFormat(rs.getDate(7).toLocalDate().getMonth().getValue()).equals(month)
							&& rs.getDate(7).toLocalDate().getYear() == Integer.parseInt(year)) {
						Blob b = rs.getBlob(6);
						byte[] buff = b.getBytes(1, (int) b.length());
						returnReport = new Report(rs.getString(1), ReportType.valueOf(rs.getString(2)), rs.getString(3),
								rs.getString(4), ReportDuration.valueOf(rs.getString(5)), buff,
								rs.getDate(7).toLocalDate());

					}
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (returnReport != null) {

				obj.setResponse(Response.FOUND_QUARENTY_COMPLAINTS_REPORT);
				obj.setInformation(returnReport);
				return;
			} else {
				obj.setResponse(Response.NOT_FOUND_QUARENTY_COMPLAINTS_REPORT);
				obj.setInformation(null);
			}
		}
		obj.setResponse(Response.NOT_FOUND_QUARENTY_COMPLAINTS_REPORT);
		obj.setInformation(null);
	}

	/**
	 * in this method we create the complaints report in the DB , with the amount of
	 * complaints for each month, total complaints in quarter and satisfaction
	 * ratio.
	 * 
	 * @param branchID
	 * @param quarter
	 * @param year
	 */
	public static void getRelavantComplaints(String branchID, String month, String year) {
		Connection con = DBController.getCon();
		List<Object> info = new ArrayList<>();
		String quarter = null;
		Integer firstMonthComplaints, secondMonthComplaints, thirdMonthComplaints, totalComplaints, totalOrders;
		Double ratio;
		switch(month) {
		case "03":{
			quarter="1";
			break;
			}
		case "06":{
			quarter="2";
			break;
			
		}
		case "09":{
			quarter="3";
			break;
			
		}
		case "12":{
			quarter="4";
			break;
			
		}
		}
		totalComplaints = getTotalComplaintsInQuarter(branchID, quarter, year, con);// total complaints per quarter

		totalOrders = getTotalOrdersInQuarter(branchID, quarter, year, con);// total orders per quarter

		// complaints amount per month
		firstMonthComplaints = getFirstMonthComplaints(branchID, quarter, year, con);

		secondMonthComplaints = getSecondMonthComplaints(branchID, quarter, year, con);

		thirdMonthComplaints = getThirdMonthComplaints(branchID, quarter, year, con);

		if (totalOrders != 0)
			ratio = (1 - ((double) totalComplaints / (double) totalOrders)) * 100;// calculating the ratio between the
																					// complaints per orders
		else
			return;
		info.add(branchID);
		info.add(firstMonthComplaints);
		info.add(secondMonthComplaints);
		info.add(thirdMonthComplaints);
		info.add(quarter);
		info.add(year);
		info.add(ratio);

		if (firstMonthComplaints != 0 || secondMonthComplaints != 0 || thirdMonthComplaints != 0)
			createQuarterlyComplaintsReport(info);
	}

	/**
	 * this method is the logic that creates the report in blob type .
	 * 
	 * @param info
	 */
	public static void createQuarterlyComplaintsReport(List<Object> info) {
		String branchID = (String) info.get(0);
		Integer firstMonthComplaints = (Integer) info.get(1);
		Integer secondMonthComplaints = (Integer) info.get(2);
		Integer thirdMonthComplaints = (Integer) info.get(3);
		String quarter = (String) info.get(4);
		String year = (String) info.get(5);
		Double ratio = (Double) info.get(6);

		Connection con = DBController.getCon();
		PreparedStatement stmt;
		String branchName = getBranchNamebyBranchID(branchID, con);
		StringBuilder stringBuilder = new StringBuilder();
		if (branchName != null) {
			stringBuilder.append(branchID + " " + branchName + " " + quarter + " " + year);// 1010 haifa 2 2020
			stringBuilder.append("\n" + firstMonthComplaints + " " + secondMonthComplaints + " " + thirdMonthComplaints
					+ " " + ratio);

			byte[] byteConent = stringBuilder.toString().getBytes();
			Blob blob = null;
			String query = "INSERT INTO zerli.reports(reportID, reportType, branchID, reportCreator, reportDuration, reportFile, reportDate) VALUES (?,?,?,?,?,?,?)";
			try {
				blob = new SerialBlob(byteConent);
				stmt = con.prepareStatement(query);
				stmt.setString(1, null);// null
				stmt.setString(2, ReportType.COMPLAINTS.name());// complaint
				stmt.setString(3, branchID);// same
				stmt.setString(4, "System");// same
				stmt.setString(5, ReportDuration.QUARTERLY.name());// quaterly
				stmt.setBlob(6, blob);
				stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));// same
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * this method returns the amount of complaints in the third month
	 * 
	 * @param branchID
	 * @param quarter
	 * @param year
	 * @param con
	 * @return
	 */
	private static Integer getThirdMonthComplaints(String branchID, String quarter, String year, Connection con) {

		ResultSet rs;
		Statement stmt;
		Integer returnValue = null;
		StringBuilder from = new StringBuilder(), to = new StringBuilder();
		from.append(year);
		from.append("-");
		to.append(year);
		to.append("-");
		switch (quarter) {
		case "1":
			from.append("03-01");
			to.append("04-01");
			break;

		case "2":
			from.append("06-01");
			to.append("07-01");
			break;

		case "3":
			from.append("09-01");
			to.append("10-01");
			break;

		case "4":
			to=new StringBuilder();
			int temp= Integer.valueOf(year)+1;
			to.append(String.valueOf(temp));
			to.append("-");
			from.append("12-01");
			to.append("01-01");
			break;
		}
		String getNumOfComplaintsInSecondMonth = "SELECT count(complaintID)  FROM zerli.complaints WHERE branchID='"
				+ branchID + "' AND complaintOpening>='" + from + "' AND  complaintOpening < '" + to + "';";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getNumOfComplaintsInSecondMonth);
			if (rs.next() != false) {
				returnValue = rs.getInt(1);// return the number of complaints in the first month

			}
			rs.close();
			return returnValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method returns the amount of complaints in the second month
	 * 
	 * @param branchID
	 * @param quarter
	 * @param year
	 * @param con
	 * @return
	 */
	private static Integer getSecondMonthComplaints(String branchID, String quarter, String year, Connection con) {

		ResultSet rs;
		Statement stmt;
		Integer returnValue = null;
		StringBuilder from = new StringBuilder(), to = new StringBuilder();
		from.append(year);
		from.append("-");
		to.append(year);
		to.append("-");
		switch (quarter) {
		case "1":
			from.append("02-01");
			to.append("03-01");
			break;

		case "2":
			from.append("05-01");
			to.append("06-01");
			break;

		case "3":
			from.append("08-01");
			to.append("09-01");
			break;

		case "4":
			from.append("11-01");
			to.append("12-01");
			break;
		}
		String getNumOfComplaintsInSecondMonth = "SELECT count(complaintID)  FROM zerli.complaints WHERE branchID='"
				+ branchID + "' AND complaintOpening>='" + from + "' AND  complaintOpening < '" + to + "';";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getNumOfComplaintsInSecondMonth);
			if (rs.next() != false) {
				return rs.getInt(1);// return the number of complaints in the first month
			}
			rs.close();
			return returnValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method returns the amount of complaints in the first month
	 * 
	 * @param branchID
	 * @param quarter
	 * @param year
	 * @param con
	 * @return
	 */
	private static Integer getFirstMonthComplaints(String branchID, String quarter, String year, Connection con) {
		ResultSet rs;
		Statement stmt;
		Integer returnValue = null;
		StringBuilder from = new StringBuilder(), to = new StringBuilder();
		from.append(year);
		from.append("-");
		to.append(year);
		to.append("-");
		switch (quarter) {
		case "1":
			from.append("01-01");
			to.append("02-01");
			break;

		case "2":
			from.append("04-01");
			to.append("05-01");
			break;

		case "3":
			from.append("07-01");
			to.append("08-01");
			break;

		case "4":
			from.append("10-01");
			to.append("11-01");
			break;
		}
		String getNumOfComplaintsInFirstMonth = "SELECT count(complaintID)  FROM zerli.complaints WHERE branchID='"
				+ branchID + "' AND complaintOpening>='" + from + "' AND  complaintOpening < '" + to + "';";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getNumOfComplaintsInFirstMonth);
			if (rs.next() != false) {
				returnValue = rs.getInt(1);// return the number of complaints in the first month
			}
			rs.close();
			return returnValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method returns the total amount of complaints in the quarter for
	 * calculating satisfaction ratio
	 */
	private static Integer getTotalComplaintsInQuarter(String branchID, String quarter, String year, Connection con) {
		ResultSet rs;
		Statement stmt;
		Integer returnValue = null;
		StringBuilder from = new StringBuilder(), to = new StringBuilder();
		from.append(year);
		from.append("-");
		to.append(year);
		to.append("-");
		switch (quarter) {
		case "1":
			from.append("01-01");
			to.append("04-01");
			break;

		case "2":
			from.append("04-01");
			to.append("07-01");
			break;

		case "3":
			from.append("07-01");
			to.append("10-01");
			break;

		case "4":
			to = new StringBuilder();
			int temp = Integer.valueOf(year) + 1;
			to.append(String.valueOf(temp));
			to.append("-");
			from.append("10-01");
			to.append("01-01");
			break;
		}
		String getNumOfComplaintsInQuarter = "SELECT count(complaintID)  FROM zerli.complaints WHERE branchID='"
				+ branchID + "' AND complaintOpening>='" + from + "' AND  complaintOpening < '" + to + "';";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getNumOfComplaintsInQuarter);
			if (rs.next() != false) {

				returnValue = rs.getInt(1);// return the number of complaints in the wanted branch and quarter
			}
			rs.close();
			return returnValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method returns the total amount of orders in the quarter for calculating
	 * satisfaction ratio
	 */
	private static Integer getTotalOrdersInQuarter(String branchID, String quarter, String year, Connection con) {
		ResultSet rs;
		Statement stmt;
		Integer returnValue = null;
		StringBuilder from = new StringBuilder(), to = new StringBuilder();
		from.append(year);
		from.append("-");
		to.append(year);
		to.append("-");
		switch (quarter) {
		case "1":
			from.append("01-01");
			to.append("04-01");
			break;

		case "2":
			from.append("04-01");
			to.append("07-01");
			break;

		case "3":
			from.append("07-01");
			to.append("10-01");
			break;

		case "4":
			to = new StringBuilder();
			int temp = Integer.valueOf(year) + 1;
			to.append(String.valueOf(temp));
			to.append("-");
			from.append("10-01");
			to.append("01-01");
			break;

		}
		String getNumOfOrdersInQuarter = "SELECT count(orderID)  FROM zerli.order WHERE branchID='" + branchID
				+ "' AND orderDate>='" + from + "' AND  orderDate < '" + to + "';";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getNumOfOrdersInQuarter);

			if (rs.next() != false) {

				returnValue = rs.getInt(1);// return the number of orders in the wanted branch and quarter
			}
			rs.close();
			return returnValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
