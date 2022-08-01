package DataBase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Class Description: class that run thread every 10 minutes that check if there
 * is new month it create new report for each branch and after 24 hours it
 * notify to all customer service that still don't close there complaint that
 * are still open
 * 
 * @author Mor Ben Haim
 */
public class TimerRunner extends TimerTask {

	Date current = new Date();

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Date newDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(newDate);
		c.add(Calendar.MONTH, -1);
		Date prvMonth=c.getTime();
		c.add(Calendar.MONTH, -2);
		Date prvQuarter=c.getTime();
		if (current.getMonth() != newDate.getMonth()) {
			// TBD : Add actions after one month.
			List<String> reportCreate = ServerQuaries.getAllBranchId(DBController.getCon());
			for (String b : reportCreate) {
				String month = ReportsQuaries.getMonthFormat(prvMonth.getMonth() + 1);
				createReports.monthlyIncome(b, month, String.valueOf(current.getYear()));
				createReports.monthlyOrders(b, month, String.valueOf(current.getYear()));
			}

		}
		if ((current.getMonth() / 3) + 1 != (newDate.getMonth() / 3) + 1) {
			// TBD : Add actions after one quarter.
			List<String> reportCreate = ServerQuaries.getAllBranchId(DBController.getCon());
			for (String b : reportCreate) {
				String month = ReportsQuaries.getMonthFormat(prvQuarter.getMonth() + 1);
				createReports.quarterIncomeReport(b, month, String.valueOf(current.getYear()));
				ReportsQuaries.getRelavantComplaints(b, month, String.valueOf(current.getYear()));
			}
		}
		current = newDate;
	}
}
