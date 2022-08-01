package entities_reports;

import java.io.Serializable;
import java.time.LocalDate;

import enums.ReportDuration;
import enums.ReportType;

/**
 * this class describes the report object
 * 
 * @author Dvir Bublil
 */
public class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * every report has report id
	 */
	private String reportID;
	/**
	 * every report has report type
	 */
	private ReportType reportType;
	/**
	 * every report has branch id
	 */
	private String branchID;
	/**
	 * every report has report creator
	 */
	private String reportCreator;
	/**
	 * every report has report duration
	 */
	private ReportDuration reportDuration;
	/**
	 * every report has report date
	 */
	private LocalDate reportDate;
	/**
	 * every report is saved in DB as blob file .
	 */
	private byte[] blob;

	/**
	 * 
	 * @param reportID
	 * @param reportType
	 * @param branchID
	 * @param reportCreator
	 * @param reportDuration
	 * @param blob
	 * @param reportDate
	 */
	public Report(String reportID, ReportType reportType, String branchID, String reportCreator,
			ReportDuration reportDuration, byte[] blob, LocalDate reportDate) {
		super();
		this.reportID = reportID;
		this.reportType = reportType;
		this.branchID = branchID;
		this.reportCreator = reportCreator;
		this.reportDuration = reportDuration;
		this.blob = blob;
		this.reportDate = reportDate;

	}

	/**
	 * return report id
	 * 
	 * @return
	 */
	public String getReportID() {
		return reportID;
	}

	/**
	 * 
	 * @param reportID
	 */
	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	/**
	 * return the reportType
	 * 
	 * @return
	 */
	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getReportCreator() {
		return reportCreator;
	}

	public void setReportCreator(String reportCreator) {
		this.reportCreator = reportCreator;
	}

	public ReportDuration getReportDuration() {
		return reportDuration;
	}

	public void setReportDuration(ReportDuration reportDuration) {
		this.reportDuration = reportDuration;
	}

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	public LocalDate getReportDate() {
		return reportDate;
	}

	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}

	@Override
	public String toString() {
		return "Report [reportID=" + reportID + ", reportType=" + reportType + ", branchID=" + branchID
				+ ", reportCreator=" + reportCreator + ", reportDuration=" + reportDuration + ", reportDate="
				+ reportDate + ", blob=" + blob + "]";
	}
}
