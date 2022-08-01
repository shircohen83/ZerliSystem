package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entities_users.User;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
/**
 * this class is handling with the reports , here we handling with the
 * reports the main part of this class is to share common
 * information between different screens
 * 
 * @author Dvir bublil
 *
 */
public class ReportHandleController {
	/**
	 * this list holds the monthly report information
	 */
	private static List<List<String>> OrdersReportOnListMonth= new ArrayList<>();
	/**
	 * this list holds the quarter report information
	 */
	private static List<List<String>> OrdersReportOnListQuarter=new ArrayList<>();
	/**
	 * this list holds the monthly survey information
	 */
	private static List<List<String>> SurveyReportResult=new ArrayList<>();
	/**
	 * this list holds the monthly Complaints information
	 */
	private static List<List<String>> ComplaintsReportResult=new ArrayList<>();
	private static User userReport;
	private static boolean dualReport=false;
	
	
	public static List<List<String>> getComplaintsReportResult() {
		return ComplaintsReportResult;
	}

	public static void setComplaintsReportResult(List<List<String>> complaintsReportResult) {
		ComplaintsReportResult = complaintsReportResult;
	}

	public static boolean isDualReport() {
		return dualReport;
	}

	public static void setDualReport(boolean dualReport) {
		ReportHandleController.dualReport = dualReport;
	}

	public static List<List<String>> getOrdersReportOnListMonth() {
		return OrdersReportOnListMonth;
	}

	public static void setOrdersReportOnListMonth(List<List<String>> ordersReportOnListMonth) {
		OrdersReportOnListMonth = ordersReportOnListMonth;
	}

	public static List<List<String>> getOrdersReportOnListQuarter() {
		return OrdersReportOnListQuarter;
	}

	public static void setOrdersReportOnListQuarter(List<List<String>> ordersReportOnListQuarter) {
		OrdersReportOnListQuarter = ordersReportOnListQuarter;
	}

	public static List<List<String>> getSurveyReportResult() {
		return SurveyReportResult;
	}
	
	public static void setSurveyReportResult(List<List<String>> surveyReportResult) {
		SurveyReportResult = surveyReportResult;
	}
	public static User getUserReport() {
		return userReport;
	}

	public static void setUserReport(User user) {
		ReportHandleController.userReport = user;
	}
	/**
	 * in this method we insert the quarterly report information into the chart and also initialize the right labels
	 * also calculate the best month and the worst month , and setting the right title of the report according to the information we got.
	 * @param worstMonth
	 * @param bestMonth
	 * @param incomeQuarterTitle
	 * @param reportOnList
	 * @param series
	 * @param series2
	 * @param series3
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void insertTheDeatilsForTheCartQurateryReport(Label worstMonth, Label bestMonth, Label incomeQuarterTitle, List<List<String>> reportOnList, Series series, Series series2, Series series3) {
		List<String> reportInfo = new ArrayList<>();
		reportInfo = reportOnList.get(0);
		incomeQuarterTitle.setText("Zerli " + reportInfo.get(1) + "(" + reportInfo.get(0) + ") -"
				+ reportInfo.get(2) + "st Quarter Income report "+reportInfo.get(3));
		series.setName("First month");
		series2.setName("Second month");
		series3.setName("Third month");
		reportOnList.remove(0);
		Collections.sort(reportOnList,new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				
				 if(Integer.valueOf(o1.get(2)).compareTo(Integer.valueOf(o2.get(2))) >0) {
					 return 1;
				 }
				 if(Integer.valueOf(o1.get(2)).compareTo(Integer.valueOf(o2.get(2))) <0) {
					 return -1;
				 }
				 return 0;
				
			}
		});
		double [] MaxMin=new double[4];
		for (int i = 0; i < reportOnList.size(); i++) {
			List<String> productInfo = new ArrayList<>();
			productInfo = reportOnList.get(i);
			if (productInfo.get(0).equals("month1")) {
				MaxMin[1]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
			} else if (productInfo.get(0).equals("month2")) {
				MaxMin[2]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));

			} else if (productInfo.get(0).equals("month3")) {
				MaxMin[3]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));

			}
		}

		double [] firstMonth=new double[31];
		double [] secondMonth=new double[31];
		double [] thridMonth=new double[31];
		for(int i = 0; i<reportOnList.size();i++) {
			List<String> productInfo = new ArrayList<>();
			productInfo = reportOnList.get(i);
			if(productInfo.get(0).equals("month1")) {
				 if(Integer.valueOf(productInfo.get(2)) <=7) {
					 firstMonth[1]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=14 && Integer.valueOf(productInfo.get(2)) >7) {
					 firstMonth[2]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=21 && Integer.valueOf(productInfo.get(2)) >14) {
					 firstMonth[3]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=30 && Integer.valueOf(productInfo.get(2)) >21) {
					 firstMonth[4]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
			}else
			if(productInfo.get(0).equals("month2")) {
				 if(Integer.valueOf(productInfo.get(2)) <=7) {
					 secondMonth[5]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=14 && Integer.valueOf(productInfo.get(2)) >7) {
					 secondMonth[6]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=21 && Integer.valueOf(productInfo.get(2)) >14) {
					 secondMonth[7]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 if(Integer.valueOf(productInfo.get(2)) <=30 && Integer.valueOf(productInfo.get(2)) >21) {
					 secondMonth[8]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
				 }
				 
			}
			else
				if(productInfo.get(0).equals("month3")) {
					 if(Integer.valueOf(productInfo.get(2)) <=7) {
						 thridMonth[9]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
					 }
					 if(Integer.valueOf(productInfo.get(2)) <=14 && Integer.valueOf(productInfo.get(2)) >7) {
						 thridMonth[10]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
					 }
					 if(Integer.valueOf(productInfo.get(2)) <=21 && Integer.valueOf(productInfo.get(2)) >14) {
						 thridMonth[11]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
					 }
					 if(Integer.valueOf(productInfo.get(2)) <=30 && Integer.valueOf(productInfo.get(2)) >21) {
						 thridMonth[12]+=Integer.parseInt(productInfo.get(3)) * Double.parseDouble(productInfo.get(4));
					 }
		}
		}
		StringBuilder day=new StringBuilder();
		day.append("0 - 1(Month)");
		for(int i=1;i<31;i++) {
			if(firstMonth[i]>0) {
			series.getData().add(new XYChart.Data("Week " +i,(firstMonth[i])));
			}
			if(secondMonth[i]>0) {
				series2.getData().add(new XYChart.Data("Week " +i,(secondMonth[i])));
				}
			if(thridMonth[i]>0) {
				series3.getData().add(new XYChart.Data("Week " +i,(thridMonth[i])));
				}
		}
		
		if(MaxMin[1]>MaxMin[2] && MaxMin[2]<=MaxMin[3]) {
			bestMonth.setText("First month of this querter");
			worstMonth.setText("Second month of this querter");
		}
		else if(MaxMin[1]>MaxMin[2]) {
			bestMonth.setText("First month of this querter");
			worstMonth.setText("Third month of this querter");
		}
		else if(MaxMin[2]>MaxMin[1] && MaxMin[1]<=MaxMin[3]) {
			bestMonth.setText("Second month of this querter");
			worstMonth.setText("First month of this querter");
		}
		else if(MaxMin[2]>MaxMin[1] ) {
			bestMonth.setText("Second month of this querter");
			worstMonth.setText("Third month of this querter");
		}
		else if(MaxMin[3]>MaxMin[1] && MaxMin[1]<=MaxMin[2]) {
			bestMonth.setText("Third month of this querter");
			worstMonth.setText("First month of this querter");
		}
		else if(MaxMin[3]>MaxMin[1] ) {
			bestMonth.setText("Third month of this querter");
			worstMonth.setText("Second month of this querter");
		}
		
	

	}
/**
 * in this method we insert the complaints report into the chart and also we insert data into the labels and to the title.
 * @param satisfactionPercentage
 * @param complaintsQuarterTitle
 * @param reportOnList
 * @param series
 * @param series2
 * @param series3
 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertTheDetailsForTheComplaintsReport(Label satisfactionPercentage,
			Label complaintsQuarterTitle, List<List<String>> reportOnList, Series series, Series series2,
			Series series3) {
		List<String> reportInfo = new ArrayList();
		List<String> complaintsInfo=new ArrayList();
		complaintsInfo=reportOnList.get(1);
		reportInfo = reportOnList.get(0);
		complaintsQuarterTitle.setText("Zerli " + reportInfo.get(1) + "(" + reportInfo.get(0) + ") -"
				+ reportInfo.get(2) + "st Quarter Compliants report "+reportInfo.get(3));
		StringBuilder day=new StringBuilder();
		day.append("0 - 1(Month)");
		series.getData().add(new XYChart.Data(day.toString(),
				(Integer.valueOf(complaintsInfo.get(0)))));
		 day=new StringBuilder();
		day.append("1 - 2(Month)");
		series2.getData().add(new XYChart.Data(day.toString(),
				(Integer.valueOf(complaintsInfo.get(1)))));
		 day=new StringBuilder();
		day.append("2 - 3(Month)");
		series3.getData().add(new XYChart.Data(day.toString(),
				(Integer.valueOf(complaintsInfo.get(2)))));
		satisfactionPercentage.setText("The ratio between the Complaints to the Orders is: "+Double.valueOf(complaintsInfo.get(3))+"%");
		
	}

	
}
