package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;


import communication.TransmissionPack;
/**
 * This class handle with the external database , we taking all the users from external db and transfer them to our data base to the relevant tables, according
 * to the permissions.
 * @author Dvir Bublil
 *
 */
public class ExternalDBquaries {
/**
 * in this method we Divide the information according to the relevant table in our db . 
 * we using siwtch case on the type of user that we got from the external db , then we taking all his information and insert it into our tables on the db.
 * @param obj
 * @param externalDB
 */
	public static void ImportUsersData(TransmissionPack obj, Connection externalDB) {
		if (obj instanceof TransmissionPack) {
			Statement stmt;
			try {
				stmt = externalDB.createStatement(); // externalDB con
				Connection con = DBController.getCon(); // zerli con

				ResultSet rs = stmt.executeQuery("SELECT * FROM externaldb.users;");
				while (rs.next()) {
					PreparedStatement pstmt;
					String quareyTable;
					String quareyCredit;
					switch (rs.getString(4)) {
					case "customer": {

						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = "INSERT INTO zerli.customer (customerID,firstName,lastName,email,phoneNumber,accountStatus,isLoggedIn,balance,isNewCustomer,creditCardNumber)"
										+ "SELECT " + "'" + rs.getString(1) + "'," + "'" + rs.getString(2) + "'," + "'"
										+ rs.getString(3) + "'," + "'" + rs.getString(6) + "'," + "'" + rs.getString(5)
										+ "'," + "'" + rs.getString(12) + "'," + "'" + rs.getString(13) + "'," + "'0',"
										+ "'1'," + "'" + rs.getString(9) + "' "
										+ "FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli.customer "
										+ "WHERE customerID=" + "'" + rs.getString(1) + "'" + "AND firstName=" + "'"
										+ rs.getString(2) + "'" + "AND lastName=" + "'" + rs.getString(3) + "');";
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();

								quareyCredit = "INSERT INTO zerli.creditcards (creditCardNumber,creditCardCvvCode,creditCardDateOfExpiration) SELECT "
										+ "'" + rs.getString(9) + "'," + "'" + rs.getString(10) + "'," + "'"
										+ rs.getString(11) + "';";
								pstmt = con.prepareStatement(quareyCredit);
								pstmt.executeUpdate();
							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "branchmanager": {
						pstmt = loginTableQuarie(con, rs);
						try {

							if (pstmt.executeUpdate() == 1) {
								quareyTable = "INSERT INTO zerli.branchmanager (branchmanagerID, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn, branchID)"
										+ "SELECT " + "'" + rs.getString(1) + "'," + "'" + rs.getString(2) + "'," + "'"
										+ rs.getString(3) + "'," + "'" + rs.getString(6) + "'," + "'" + rs.getString(5)
										+ "'," + "'" + rs.getString(12) + "'," + "'" + rs.getString(13) + "'" + ",'"
										+ rs.getString(14) + "' "
										+ "FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli.branchmanager "
										+ "WHERE branchmanagerID=" + "'" + rs.getString(1) + "'" + "AND firstName="
										+ "'" + rs.getString(2) + "'" + "AND lastName=" + "'" + rs.getString(3) + "');";
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();

							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "customerservice": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = sameTableStructreQuarie("customerservice", "customerserviceID", rs);
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();

							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "deliveryagent": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = "INSERT INTO zerli.deliveryagent (deliveryagentID, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn, branchID)"
										+ "SELECT " + "'" + rs.getString(1) + "'," + "'" + rs.getString(2) + "'," + "'"
										+ rs.getString(3) + "'," + "'" + rs.getString(6) + "'," + "'" + rs.getString(5)
										+ "'," + "'" + rs.getString(12) + "'," + "'" + rs.getString(13) + "'" + ",'"
										+ rs.getString(14) + "' "
										+ "FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli.deliveryagent "
										+ "WHERE deliveryagentID=" + "'" + rs.getString(1) + "'" + "AND firstName="
										+ "'" + rs.getString(2) + "'" + "AND lastName=" + "'" + rs.getString(3) + "');";
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();

							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "marketingworker": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = sameTableStructreQuarie("marketingworker", "marketingworkerID", rs);
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();
							} else {
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}

					case "networkmanager": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = sameTableStructreQuarie("networkmanager", "networkManagerID", rs);
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();

							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "serviceexpert": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = sameTableStructreQuarie("serviceexpert", "serviceexpertID", rs);
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();
								System.out.println(quareyTable);
							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}
					case "shopworker": {
						pstmt = loginTableQuarie(con, rs);
						try {
							if (pstmt.executeUpdate() == 1) {
								quareyTable = "INSERT INTO zerli.shopworker (shopworkerID, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn,branchID,acctivityStatus)"
										+ "SELECT " + "'" + rs.getString(1) + "'," + "'" + rs.getString(2) + "'," + "'"
										+ rs.getString(3) + "'," + "'" + rs.getString(6) + "'," + "'" + rs.getString(5)
										+ "'," + "'" + rs.getString(12) + "'," + "'" + rs.getString(13) + "'," + "'"
										+ rs.getString(14) + "'," + "'SURVEY'"
										+ " FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli.shopworker "
										+ "WHERE shopworkerID=" + "'" + rs.getString(1) + "'" + "AND firstName=" + "'"
										+ rs.getString(2) + "'" + "AND lastName=" + "'" + rs.getString(3) + "');";
								pstmt = con.prepareStatement(quareyTable);
								pstmt.executeUpdate();
							} else {
								System.out.println("Login contains this details allready");
							}
						} catch (SQLIntegrityConstraintViolationException e) {
							System.out.println(
									"The import successed but, there are some info that was duplicated so they not copy.");
						}
						break;
					}

					}
				}
			} catch (SQLException e) {
				System.out.println("Faild to import");
				e.printStackTrace();
			}

		}
	}
/**
 * in this method we returning the login transfer information from the external db to our db
 * @param con
 * @param rs
 * @return
 * @throws SQLException
 */
	private static PreparedStatement loginTableQuarie(Connection con, ResultSet rs) throws SQLException {
		PreparedStatement pstmt;
		String quareyLogin;
		quareyLogin = "INSERT INTO zerli.login (ID,userName,password,userType) SELECT " + "'" + rs.getString(1) + "',"
				+ "'" + rs.getString(7) + "'," + "'" + rs.getString(8) + "'," + "'" + rs.getString(4) + "' "
				+ "FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli.login WHERE userName=" + "'" + rs.getString(7)
				+ "')";
		pstmt = con.prepareStatement(quareyLogin);
		return pstmt;
	}
/**
 * in this method we creating the String that will use as to the quarey , that will insert the spesifc info to the spesifc table that we got in the constractur;
 * @param tableName
 * @param IDcolmName
 * @param rs
 * @return
 * @throws SQLException
 */
	private static String sameTableStructreQuarie(String tableName, String IDcolmName, ResultSet rs)
			throws SQLException {
		String quarey = "INSERT INTO zerli." + tableName + "(" + IDcolmName
				+ ",firstName,lastName,email,phoneNumber,accountStatus,isLoggedIn)" + "SELECT " + "'" + rs.getString(1)
				+ "'," + "'" + rs.getString(2) + "'," + "'" + rs.getString(3) + "'," + "'" + rs.getString(6) + "',"
				+ "'" + rs.getString(5) + "'," + "'" + rs.getString(12) + "'," + "'" + rs.getString(13) + "'"
				+ " FROM DUAL WHERE NOT EXISTS (SELECT * FROM zerli." + tableName + " WHERE " + IDcolmName + "='"
				+ rs.getString(1) + "'" + "AND firstName=" + "'" + rs.getString(2) + "'" + "AND lastName=" + "'"
				+ rs.getString(3) + "');";
		return quarey;
	}
}
