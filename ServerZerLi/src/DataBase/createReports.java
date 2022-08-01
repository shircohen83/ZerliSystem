package DataBase;

import java.util.ArrayList;
import java.util.List;

import communication.Mission;
import communication.TransmissionPack;
import enums.ReportType;
/**
 * this class handling with creating the reports 
 * @author Dvir Bublil
 *
 */
public class createReports {
	/*
	 * in this method creating order monthly report by spesifc branch and month (getting the month on number 2 digit )
	 */

	public static void monthlyOrders(String branchID,String month,String year) {

		List<List<String>> orders=new ArrayList<>();
		List<Object> orderFilter=new ArrayList<>();
		orderFilter.add(branchID);
		orderFilter.add(month);
		orderFilter.add(year);
		TransmissionPack tp=new TransmissionPack(Mission.GET_MONTHLY_REPORT,null,orderFilter);
		orders=ReportsQuaries.gettingOrderMonthlyData(tp);
		
		if(orders != null) {
			List<Object> orderInfoToParse=new ArrayList<>();
			orderInfoToParse.add(branchID);
			orderInfoToParse.add(orders);
			orderInfoToParse.add(month);
			String type=ReportType.ORDERS.name();
			orderInfoToParse.add(type);
			ReportsQuaries.createMonthlyReport(orderInfoToParse);
		}
		
	}
/*
 * in this method we creating income monthly report by spesifc branch and spesifc month.
 */
	public static void monthlyIncome(String branchID, String month,String year) {
		List<List<String>> incomeInfo=new ArrayList<>();
		List<Object> incomeFilter=new ArrayList<>();
		incomeFilter.add(branchID);
		incomeFilter.add(month);
		incomeFilter.add(year);
		TransmissionPack tp=new TransmissionPack(Mission.GET_MONTHLY_REPORT,null,incomeFilter);
		incomeInfo=ReportsQuaries.gettingIncomeMonthlyData(tp);
		if(incomeInfo != null) {
			List<Object> incomeInfoToParse=new ArrayList<>();
			incomeInfoToParse.add(branchID);
			incomeInfoToParse.add(incomeInfo);
			incomeInfoToParse.add(month);
			String type=ReportType.INCOME.name();
			incomeInfoToParse.add(type);
			ReportsQuaries.createMonthlyReport(incomeInfoToParse);
		}
		
	}
	/**
	 * in this method we creating incomeQuarterReport. this method also can create ordersQuarter report but its not asked.
	 * @param branchID
	 * @param year
	 * @param quarter
	 */
	public static void quarterIncomeReport(String branchID,String year,String quarter) {
		boolean created=false;
		TransmissionPack tp=new TransmissionPack(null, null, null);
		List<String> reportDetails = new ArrayList<>();
		reportDetails.add(branchID);
		reportDetails.add(year);
		reportDetails.add(quarter);
		reportDetails.add(ReportType.INCOME.name());
		tp.setInformation(reportDetails);
		created=ReportsQuaries.createQuarterReportInformation(tp);
		if(!created) {
			System.out.println("quarterReport creation failed");
		}
	}
	
}
