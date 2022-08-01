package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import communication.Response;
import communication.TransmissionPack;
import entities_catalog.Product;
import entities_catalog.ProductInBranch;
import entities_catalog.ProductInOrder;
import entities_general.Cancellation;
import entities_general.CreditCard;
import entities_general.Deliveries;
import entities_general.Login;
import entities_general.Order;
import entities_general.Question;
import entities_reports.Complaint;
import entities_users.BranchManager;
import entities_users.Customer;
import entities_users.CustomerService;
import entities_users.DeliveryAgent;
import entities_users.MarketingWorker;
import entities_users.NetworkManager;
import entities_users.ServiceExpert;
import entities_users.ShopWorker;
import entities_users.User;
import enums.AccountStatus;
import enums.Branches;
import enums.ComplaintsStatus;
import enums.DeliveryStatus;
import enums.OrderStatus;
import enums.ShopworkerRole;

/**
 * In this class there are all the server quarries
 * 
 * @author Mor Ben Haim
 *
 */
public class ServerQuaries {

	public static void Login(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			Login user = (Login) obj.getInformation();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT userName , password , userType, ID FROM login;");
				while (rs.next()) {
					System.out.println(rs.getString(4));
					if (user.getUserName().equals(rs.getString(1)) && user.getPassword().equals(rs.getString(2))) {
						if (checkIfLoggedin(user, rs.getString(3), rs.getString(4), obj, con) == false) {

							obj.setResponse(Response.USER_EXIST);

							return;
						} else {
							obj.setResponse(Response.USER_ALREADY_LOGGEDIN);
							return;
						}
					}
					if (user.getUserName().equals(rs.getString(1)) && !user.getPassword().equals(rs.getString(2))) {
						obj.setResponse(Response.USER_NAME_OR_PASSWORD_INCORRECT);
						return;
					}
				}

				obj.setResponse(Response.USER_NOT_EXIST);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.USER_NOT_EXIST);
				return;
			}
		}
	}

	/**
	 * checking if login if yes we don't do anything , else we updating that he can
	 * login and update the table that he logged in
	 * 
	 * @param user
	 * @param type
	 * @param userID
	 * @param obj
	 * @param con
	 * @return
	 * @throws SQLException
	 */

	public static boolean checkIfLoggedin(Login user, String type, String userID, TransmissionPack obj, Connection con)
			throws SQLException {
		boolean isLogin = false;
		String table, userRow, updateSpecificRow;
		ResultSet rs;
		PreparedStatement pstmt, pstmt2;
		table = "zerli." + type;
		userRow = "SELECT isLoggedIn FROM" + " " + table + " WHERE " + type + "ID=" + "'" + userID + "'" + ";";
		updateSpecificRow = "UPDATE" + " " + table + " SET isLoggedIn=? WHERE " + type + "ID=" + "'" + userID + "'"
				+ ";";
		pstmt = con.prepareStatement(userRow);
		rs = pstmt.executeQuery(userRow);
		rs.next();
		if (rs.getString(1).equals("1"))
			isLogin = true;
		else if (rs.getString(1).equals("0")) {
			isLogin = false;
			pstmt2 = con.prepareStatement(updateSpecificRow);
			pstmt2.setString(1, "1");
			pstmt2.executeUpdate();
			/**
			 * Update the obj to specific userType and his specific information
			 */
			upadateUserInformation(type, userID, obj, pstmt);

		}

		return isLogin;

	}

	/**
	 * this method is updating the specific userType and his specific information it
	 * will be update on the client side as well using trancimissionPack
	 * setInformation method
	 * 
	 * @param type
	 * @param userID
	 * @param obj
	 * @param pstmt
	 * @throws SQLException
	 */
	private static void upadateUserInformation(String type, String userID, TransmissionPack obj,
			PreparedStatement pstmt) throws SQLException {
		ResultSet rs;
		switch (type) {
		case "customer": {

			rs = getRowFromTable(userID, type, pstmt);
			Customer customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7), rs.getString(8),
					rs.getBoolean(9), (new CreditCard(rs.getString(6), null, null)));
			obj.setInformation(customer);
			break;
		}
		case "branchmanager": {

			rs = getRowFromTable(userID, type, pstmt);
			BranchManager branchmanager = new BranchManager(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7),
					rs.getString(8));
			obj.setInformation(branchmanager);
			break;
		}
		case "customerservice": {
			rs = getRowFromTable(userID, type, pstmt);
			CustomerService customerservice = new CustomerService(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7));
			obj.setInformation(customerservice);
			break;
		}
		case "deliveryagent": {
			rs = getRowFromTable(userID, type, pstmt);

			DeliveryAgent deliveryagent = new DeliveryAgent(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7),
					rs.getString(8));
			obj.setInformation(deliveryagent);
			break;
		}
		case "marketingworker": {
			rs = getRowFromTable(userID, type, pstmt);

			MarketingWorker marketingworker = new MarketingWorker(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7));
			obj.setInformation(marketingworker);
			break;
		}
		case "networkmanager": {
			rs = getRowFromTable(userID, type, pstmt);

			NetworkManager networkmanager = new NetworkManager(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7));
			obj.setInformation(networkmanager);
			break;
		}
		case "serviceexpert": {
			rs = getRowFromTable(userID, type, pstmt);

			ServiceExpert serviceexpert = new ServiceExpert(rs.getString(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7));
			obj.setInformation(serviceexpert);
			break;
		}
		case "shopworker": {
			rs = getRowFromTable(userID, type, pstmt);

			ShopWorker shopworker = new ShopWorker(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7), rs.getString(8),
					ShopworkerRole.valueOf(rs.getString(9)));
			obj.setInformation(shopworker);
			break;
		}
		}
	}

	/**
	 * this method return specific row of user from his table .
	 * 
	 * @param userID
	 * @param type
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	private static ResultSet getRowFromTable(String userID, String type, PreparedStatement pstmt) throws SQLException {
		ResultSet rs;
		String getrow = "SELECT * FROM zerli." + type + " WHERE " + type + "ID='" + userID + "'";
		rs = pstmt.executeQuery(getrow);
		rs.next();
		return rs;
	}

	/**
	 * In this method we logout the user from the system , by changing his login
	 * value to zero(our flag) on the DB.
	 * 
	 * @param obj
	 * @param con
	 */
	public static void logout(TransmissionPack obj, Connection con) {
		ResultSet rs;
		PreparedStatement pstmt;
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "SELECT userType FROM login WHERE ID='" + ((User) obj.getInformation()).getID() + "'";
			rs = stmt.executeQuery(query);
			rs.next();
			String table = "zerli." + rs.getString(1);
			String table2 = rs.getString(1) + "ID";

			String query2 = "UPDATE" + " " + table + " SET isLoggedIn=? WHERE " + table2 + "='"
					+ ((User) obj.getInformation()).getID() + "'";
			pstmt = con.prepareStatement(query2);
			pstmt.setString(1, "0");
			pstmt.executeUpdate();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * the method gets workers from the specific branch of the connected user(branch
	 * manager), from DB
	 * 
	 * @param obj
	 * @param con
	 */
	public static void GetShopWorkersFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<ShopWorker> list = new ArrayList<>();
			Statement stmt;
			User user = (User) obj.getInformation();
			String branchID = getBranchId(user, con);
			if (branchID == null) {
				obj.setResponse(Response.SHOP_WORKER_NOT_ARRIVED);
				return;
			}
			try {
				stmt = con.createStatement();
				ResultSet rs;
				String getColumns = "SELECT * FROM zerli.shopworker WHERE branchID='" + branchID + "';";
				rs = stmt.executeQuery(getColumns);
				while (rs.next()) {
					ShopWorker sw = new ShopWorker(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7),
							rs.getString(8), ShopworkerRole.valueOf(rs.getString(9)));
					list.add(sw);
				}
				rs.close();
				if (list.size() > 0) {
					obj.setResponse(Response.SHOP_WORKER_ARRIVED);
					obj.setInformation(list);
				} else {
					obj.setResponse(Response.SHOP_WORKER_NOT_ARRIVED);
				obj.setInformation(list);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.SHOP_WORKER_NOT_ARRIVED);
				obj.setInformation(list);
				return;
			}
		} else {
			obj.setResponse(Response.SHOP_WORKER_NOT_ARRIVED);
			obj.setInformation(null);
		}
	}

	/**
	 * This method getting the products from the products table from our DB.
	 * 
	 * @param obj
	 * @param con
	 */
	public static void GetProducts(TransmissionPack obj, Connection con) {
		// TODO Auto-generated method stub
		if (obj instanceof TransmissionPack) {
			List<Product> list = new ArrayList<>();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM product;");
				while (rs.next()) {
					Product product = new Product(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4),
							rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getBoolean(9),
							rs.getDouble(10));
					list.add(product);
				}
				rs.close();

				if (list.size() == 0)
					throw new SQLException();

				obj.setInformation(list);
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.FAILD_DATA_PRODUCTS);
				return;
			}
			obj.setResponse(Response.GET_DATA_PRODUCTS);
		}

		else
			obj.setResponse(Response.FAILD_DATA_PRODUCTS);
	}

	/**
	 * this method getting all the products from the DB , by specific filter(color ,
	 * type, price)
	 * 
	 * @param obj
	 * @param con
	 */
	public static void GetProductsByFilter(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			@SuppressWarnings("unchecked")
			Map<String, String> filters = (Map<String, String>) obj.getInformation();
			List<Product> products = new ArrayList<>();
			int lowValuePrice = 0, highValuePrice = 0;
			String colorFilter, priceFilter, typeFilter;
			String query;

			// get Filters from HashMap
			colorFilter = filters.get("color");
			priceFilter = filters.get("price");
			typeFilter = filters.get("type");

			// split price to 2 section lowValuePrice and highValuePrice
			if (!priceFilter.equals("None")) {
				String[] tmp = priceFilter.split("-");
				int lastIndex = tmp[1].length() - 1;
				lowValuePrice = Integer.parseInt(tmp[0]);
				highValuePrice = Integer.parseInt(tmp[1].substring(0, lastIndex));
			}

			// query of color + price + filter
			if (!colorFilter.equals("None") && !priceFilter.equals("None") && !typeFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE dominateColor = '" + colorFilter + "' AND price > "
						+ lowValuePrice + " AND price<" + highValuePrice + " AND itemType = '" + typeFilter + "' ;";
			}
			// color + price
			else if (!colorFilter.equals("None") && !priceFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE dominateColor = '" + colorFilter + "' AND price > "
						+ lowValuePrice + " AND price<" + highValuePrice + ";";
			}
			// color + type
			else if (!colorFilter.equals("None") && !typeFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE dominateColor = '" + colorFilter + "' AND itemType = '"
						+ typeFilter + "' ;";
			}
			// price + type
			else if (!priceFilter.equals("None") && !typeFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE price > " + lowValuePrice + " AND price<" + highValuePrice
						+ " AND itemType = '" + typeFilter + "' ;";
			}
			// color
			else if (!colorFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE dominateColor = '" + colorFilter + "';";
			}
			// price
			else if (!priceFilter.equals("None")) {
				query = "SELECT * FROM zerli.product WHERE  price > " + lowValuePrice + " AND price<" + highValuePrice
						+ ";";
			}
			// type
			else
				query = "SELECT * FROM zerli.product WHERE itemType = '" + typeFilter + "' ;";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					Product product = new Product(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4),
							rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getBoolean(9),
							rs.getDouble(10));

					products.add(product);
				}

				if (products.size() > 0) {
				obj.setInformation(products);
				obj.setResponse(Response.GET_DATA_PRODUCTS_BY_FILTER);
				
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.FAILD_DATA_PRODUCTS_BY_FILTER);
				return;
			}
		} else
			obj.setResponse(Response.FAILD_DATA_PRODUCTS_BY_FILTER);
	}

	/**
	 * this method is getting the colors for the catalog screen for choosing
	 * dominate color for filtering
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getColors(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs;
			Statement stmt;
			List<String> colors = new ArrayList<>();
			/**
			 * Distinct query to getting colors from product table
			 */
			String query = "SELECT DISTINCT dominateColor FROM zerli.product";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					colors.add(rs.getString(1));/** add the color to the the arrayList */

				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (colors.size() > 0) {

				obj.setInformation(colors);
				obj.setResponse(Response.FOUND_COLORS);
			} else {
				obj.setResponse(Response.DID_NOT_FIND_COLORS);
			}

		}

	}

	/**
	 * this method is insert order that customer performed and save it to wate for
	 * branch manger for improving it save also the order details that in progress
	 * 
	 * @param TransmissionPack obj - (Mission) + null + (Order)
	 * @param Connection       con - dataBase connection
	 * @author Almog Madar , Mor Ben-Haim
	 */
	public static void addOrderInDB(TransmissionPack obj, Connection con) {

		if (obj instanceof TransmissionPack) {

			if (obj.getInformation() instanceof Order) {
				PreparedStatement pstmt;
				int orderID = 0;
				Statement stmt1;
				ResultSet rs1;
				Order order = (Order) obj.getInformation();
				Map<String, List<ProductInOrder>> productInOrderFinallCart = order.getItems();
				String query = "INSERT INTO zerli.order(orderID, customerID, branchID,price, greetingCard,status, orderDate,expectedDelivery) "
						+ "VALUES (?,?,?,?,?,?,?,?);";
				String query2 = "SELECT MAX(orderID) FROM zerli.order;";
				String query3 = "INSERT INTO zerli.productinorder(productID, orderID , nameOfproduct, price, backGroundColor, picture, quantity, itemType, dominateColor, productQuantityInOrder, nameOfItem) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				String query4 = "UPDATE zerli.customer SET isNewCustomer = '0' WHERE (customerID = (?));";

				try {
					// insert order to database
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, null);
					pstmt.setInt(2, Integer.parseInt(order.getCustomerID()));
					pstmt.setString(3, order.getBranchID());
					pstmt.setString(4, String.valueOf(order.getPrice()));
					pstmt.setString(5, order.getGreetingCard());
					pstmt.setString(6, order.getStatus().name());
					pstmt.setString(7, order.getOrderDate());
					pstmt.setString(8, order.getExpectedDelivery());
					pstmt.executeUpdate();

					// get orderID
					stmt1 = con.createStatement();
					rs1 = stmt1.executeQuery(query2);
					while (rs1.next()) {
						orderID = rs1.getInt(1);
					}
					rs1.close();

					// insert (List<productInOrder>) products to table productInOrder
					for (Map.Entry<String, List<ProductInOrder>> entry : productInOrderFinallCart.entrySet()) {
						for (ProductInOrder productInOr : entry.getValue()) {
							pstmt = con.prepareStatement(query3);
							pstmt.setString(1, productInOr.getID());
							pstmt.setString(2, String.valueOf(orderID));
							pstmt.setString(3, entry.getKey()); // Regular or Custom key
							pstmt.setString(4, String.valueOf(productInOr.getPrice()));
							pstmt.setString(5, productInOr.getBackGroundColor());
							pstmt.setString(6, productInOr.getImgSrc());
							pstmt.setString(7, String.valueOf(productInOr.getQuantity()));
							pstmt.setString(8, productInOr.getItemType());
							pstmt.setString(9, productInOr.getDominateColor());
							pstmt.setString(10, String.valueOf(productInOr.getProductQuantityInCart()));
							pstmt.setString(11, productInOr.getNameOfItem());
							pstmt.executeUpdate();
						}

					}

					// if new customer clear flag in database
					pstmt = con.prepareStatement(query4);
					pstmt.setString(1, order.getCustomerID());
					pstmt.executeUpdate();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					obj.setResponse(Response.INSERT_ORDER_FAILD);
					return;
				}
				obj.setInformation(orderID);
				obj.setResponse(Response.INSERT_ORDER_SUCCESS);
				return;
			}
		}
		obj.setResponse(Response.INSERT_ORDER_FAILD);

	}

	/**
	 * In this method we getting all the orders that on pending or cancellation
	 * status
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getOrders(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs, rs2;
			Statement stmt, stmt2;
			List<Order> orders = new ArrayList<>();
			String query = "SELECT * FROM zerli.order WHERE status='PENDING' OR status='PENDING_WITH_DELIVERY' OR status='CANCEL_ORDER_DELIVERY_BY_CUSTOMER' OR status='CANCEL_ORDER_BY_CUSTOMER' OR status='PENDING_WITH_IMIDATE_DELIVERY'";
			String query1 = "SELECT * FROM zerli.productinorder WHERE orderID='";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					Map<String, List<ProductInOrder>> products = new HashMap<>();
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery(query1 + rs.getString(1) + "'");
					while (rs2.next()) {
						ProductInOrder newProduct = new ProductInOrder(rs2.getString(1), rs2.getString(2),
								rs2.getString(3), rs2.getDouble(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7),
								rs2.getString(8), rs2.getString(9), rs2.getInt(10), rs2.getString(11), false, 0.0);

						if (!products.containsKey(rs2.getString(3))) {
							List<ProductInOrder> product = new ArrayList<>();
							product.add(newProduct);
							products.put(rs2.getString(3), product);
						} else {
							products.get(rs2.getString(3)).add(newProduct);
						}

					}
					rs2.close();

					Order order = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
							rs.getString(5), rs.getString(7), rs.getString(8), products);
					order.setStatus(OrderStatus.valueOf(rs.getString(6)));

					orders.add(order);
				}
				rs.close();

				if (orders.size() > 0) {
					obj.setResponse(Response.FOUND_ORDER);
					obj.setInformation(orders);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * in this method we getting the branchID , by the user ID
	 * 
	 * @param user
	 * @param con
	 * @return
	 */
	protected static String getBranchId(User user, Connection con) {

		ResultSet rs;
		Statement stmt;
		String branchId = null;
		String getBranchID = "SELECT branchID FROM zerli.branchmanager WHERE branchmanagerID='" + user.getID() + "';";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getBranchID);
			rs.next();
			branchId = rs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branchId;
	}

	/**
	 * in this method we getting all the branches ID
	 * 
	 * @param user
	 * @param con
	 * @return
	 */
	public static List<String> getAllBranchId(Connection con) {
		ResultSet rs;
		Statement stmt;
		List<String> branchesId = new ArrayList<>();
		String getBranchID = "SELECT branchID FROM zerli.branchs;";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getBranchID);
			while (rs.next()) {
				branchesId.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branchesId;
	}

	/*
	 * * Get all the customers that their status is "PENDING_APPROVAL" and collect
	 * them in a list
	 * 
	 * @param obj
	 * 
	 * @param con
	 */
	public static void getPendingCustomersFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Customer> PendingcustomersList = new ArrayList<Customer>();
			ResultSet rs;
			Statement stmt;
			String getCustomers = "SELECT * FROM zerli.customer WHERE accountStatus ='PENDING_APPROVAL';"; // The query
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(getCustomers);
				while (rs.next()) {
					// create new customer with the details from DB
					Customer customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), AccountStatus.valueOf(rs.getString(6)), rs.getBoolean(7), rs.getString(8),
							rs.getBoolean(9), new CreditCard(rs.getString(10), null, null));
					// add to the pending customers list
					PendingcustomersList.add(customer);
				}
				rs.close();
				obj.setInformation(PendingcustomersList);
				obj.setResponse(Response.GET_PENDING_CUSTOMERS_SUCCESS);
				return;
			} catch (SQLException e) {
				obj.setResponse(Response.GET_PENDING_CUSTOMERS_FAILED);
				return;
			}
		}
		obj.setResponse(Response.GET_PENDING_CUSTOMERS_FAILED);
	}

	/**
	 * in this method we getting all the customer's creditCards from the Db
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getCreditCardsFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs;
			Statement stmt;
			List<String> cardsNumbers = new ArrayList<>();
			String getCards = "SELECT creditCardNumber FROM zerli.creditcards;";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(getCards);
				while (rs.next()) {
					String card = rs.getString(1);
					cardsNumbers.add(card);
				}
				rs.close();
				obj.setInformation(cardsNumbers);
				obj.setResponse(Response.GET_CREDIT_CARDS_SUCCESS);
				return;
			} catch (SQLException e) {
				obj.setResponse(Response.GET_CREDIT_CARDS_FAILED);
				return;
			}
		}
		obj.setResponse(Response.GET_CREDIT_CARDS_FAILED);
	}

	/**
	 * in this method we approve a new customer , and updating the DB.
	 * 
	 * @param obj
	 * @param con
	 */
	public static void approveNewCustomerToDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			Customer customer = (Customer) obj.getInformation(); // get the customer updated
			String approveCuStomer = "UPDATE zerli.customer SET accountStatus= 'CONFIRMED', balance= '0', isNewCustomer= '1', creditCardNumber="
					+ customer.getCreditCard().getCreditCardNumber() + " WHERE customerID=" + customer.getID() + ";";
			String addCreditCard = String.format(
					"INSERT INTO zerli.creditcards(creditCardNumber, creditCardCvvCode, creditCardDateOfExpiration) VALUES ('%s','%s', '%s');",
					customer.getCreditCard().getCreditCardNumber(), customer.getCreditCard().getCreditCardCvvCode(),
					customer.getCreditCard().getCreditCardDateOfExpiration());
			try {
				PreparedStatement pstmt1 = con.prepareStatement(approveCuStomer);
				PreparedStatement pstmt2 = con.prepareStatement(addCreditCard);
				if (pstmt1.executeUpdate() == 0) { // check if the query failed
					obj.setResponse(Response.APPROVE_NEW_CUSTOMER_FAILED);
					return;
				} else if (pstmt2.executeUpdate() == 0) { // check if the query failed
					obj.setResponse(Response.APPROVE_NEW_CUSTOMER_FAILED);
					return;
				} else {
					obj.setResponse(Response.APPROVE_NEW_CUSTOMER_SUCCESS);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.APPROVE_NEW_CUSTOMER_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.APPROVE_NEW_CUSTOMER_FAILED);
		}
	}

	/**
	 * the method updates the customers that we got from the gui after that the
	 * branchmanager edit them.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void updateCustomersAfterEdit(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			PreparedStatement pstmt;
			List<Customer> listAfterEdit = (ArrayList<Customer>) obj.getInformation();
			try {
				for (Customer c : listAfterEdit) {
					String getApprovedCustomers = "UPDATE zerli.customer SET accountStatus='" + c.getAccountStatus()
							+ "' WHERE customerID='" + c.getID() + "';";
					pstmt = con.prepareStatement(getApprovedCustomers);
					pstmt.executeUpdate(getApprovedCustomers);
				}
				obj.setResponse(Response.CUSTOMER_EDITS_UPDATED);
				return;

			} catch (SQLException e) {
				obj.setResponse(Response.CUSTOMER_EDITS_FAILED);
				obj.setInformation(null);
				return;
			}
		}

	}

	/**
	 * in this method updates the workers that we got from the gui after that the
	 * branchmanager edit them.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void updateWorkersAfterEdit(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			PreparedStatement pstmt;
			List<ShopWorker> listAfterEdit = (ArrayList<ShopWorker>) obj.getInformation();
			try {
				for (ShopWorker sw : listAfterEdit) {
					String getApprovedWorkers = "UPDATE zerli.shopworker SET acctivityStatus='" + sw.getActivityStatus()
							+ "' WHERE shopworkerID='" + sw.getID() + "';";
					pstmt = con.prepareStatement(getApprovedWorkers);
					pstmt.executeUpdate(getApprovedWorkers);
				}
				obj.setResponse(Response.WORKER_EDITS_UPDATED);
				return;

			} catch (SQLException e) {
				obj.setResponse(Response.WORKER_EDITS_FAILED);
				obj.setInformation(null);
				return;
			}
		}
	}

	/**
	 * this method gets workers from the specific branch of the connected
	 * user(branch manager), from DB
	 * 
	 * @param obj
	 * @param con
	 */
	public static void GetCustomersFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Customer> listOfCustomers = new ArrayList<>();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs;
				String getApprovedCustomers = "SELECT * FROM zerli.customer WHERE accountStatus='CONFIRMED' OR accountStatus='FROZEN'";
				rs = stmt.executeQuery(getApprovedCustomers);

				while (rs.next()) {
					CreditCard cc = getCreditCard(rs.getString(10), con);
					if (cc == null) {
						obj.setResponse(Response.CUSTOMER_NOT_ARRIVED);
						obj.setInformation(null);
						return;
					}
					Customer customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), (AccountStatus.valueOf(rs.getString(6))), rs.getBoolean(7),
							rs.getString(8), rs.getBoolean(9), cc);

					listOfCustomers.add(customer);
				}
				rs.close();
				if (listOfCustomers.size() > 0) {
					obj.setResponse(Response.CUSTOMER_ARRIVED);
					obj.setInformation(listOfCustomers);// updating the mission info to the wanted list of customers
				} else {
					obj.setResponse(Response.CUSTOMER_NOT_ARRIVED);
					obj.setInformation(listOfCustomers);
				}
			} catch (SQLException e) {
				obj.setResponse(Response.CUSTOMER_NOT_ARRIVED);
				obj.setInformation(listOfCustomers);
			}
		}else {
			obj.setResponse(Response.CUSTOMER_NOT_ARRIVED);
			obj.setInformation(null);
		}
	}

	/**
	 * In this method we getting a credit card information ,by his number.
	 * 
	 * @param cardNum
	 * @param con
	 * @return
	 */
	private static CreditCard getCreditCard(String cardNum, Connection con) {
		ResultSet rs;
		Statement stmt;
		CreditCard cc = null;
		String getCardDetails = "SELECT creditCardCvvCode,creditCardDateOfExpiration FROM zerli.creditcards WHERE creditCardNumber='"
				+ cardNum + "';";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getCardDetails);
			rs.next();
			cc = new CreditCard(cardNum, rs.getString(1), rs.getString(2));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cc;
	}

	/**
	 * in this method we update all the complaint status in the DB, and if its
	 * necessary also his balance ( for refund and etc). we using an private method
	 * that make this method more general.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void updateComplaints(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Complaint> complaintsToUpdate = (List<Complaint>) obj.getInformation();
			String updateSpecificRow = "UPDATE zerli.complaints SET status=? WHERE complaintID='";
			for (Complaint c : complaintsToUpdate) {

				try {
					updateComlaint(con, updateSpecificRow, c);
					if (c.getRefoundAmount() != null) {
						insertNewRefund(con, c);
						updateRefundInSpecificCustomer(con, c);

					}
				} catch (SQLException e) {
					obj.setResponse(Response.COMPLAINTS_UPDATE_FAILED);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			obj.setResponse(Response.COMPLAINTS_UPDATE_SUCCEED);

		} else {
			obj.setResponse(Response.COMPLAINTS_UPDATE_FAILED);
		}

	}

	/**
	 * in this method we updating the complaint in a specific row , by a complaint
	 * ID.
	 * 
	 * @param con
	 * @param updateSpecificRow
	 * @param c
	 * @throws SQLException
	 */
	private static void updateComlaint(Connection con, String updateSpecificRow, Complaint c) throws SQLException {
		PreparedStatement pstmt;
		pstmt = con.prepareStatement(updateSpecificRow + c.getComplaintID() + "'");
		pstmt.setString(1, c.getComplainState().name());
		pstmt.executeUpdate();
	}

	/**
	 * in this method we update the customer balance with the refund if its
	 * necessary.
	 * 
	 * @param con
	 * @param c
	 * @throws SQLException
	 */
	private static void updateRefundInSpecificCustomer(Connection con, Complaint c) throws SQLException {
		PreparedStatement pstmt3;
		String query2 = "UPDATE zerli.customer SET balance=balance+? WHERE customerID='" + c.getCustomerID() + "'";
		pstmt3 = con.prepareStatement(query2);
		pstmt3.setString(1, c.getRefoundAmount());
		pstmt3.executeUpdate();
	}

	/**
	 * this method open a new refund row on the refund table on our DB , in
	 * situation that its needed.
	 * 
	 * @param con
	 * @param c
	 * @throws SQLException
	 */
	private static void insertNewRefund(Connection con, Complaint c) throws SQLException {
		PreparedStatement pstmt2;
		String query = "INSERT INTO zerli.refunds(refundID,orderID, customerID, ammount, reason, date) VALUES (?,?,?,?,?,?)";
		pstmt2 = con.prepareStatement(query);
		pstmt2.setString(1, null);
		pstmt2.setString(2, c.getOrderID());
		pstmt2.setString(3, c.getCustomerID());
		pstmt2.setString(4, c.getRefoundAmount());
		pstmt2.setString(5, "Complaint");
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		pstmt2.setString(6, dateFormat.format(Calendar.getInstance().getTime()));
		pstmt2.executeUpdate();
	}

	/**
	 * this method insert a new complaint to the DB
	 * 
	 * @param obj
	 * @param con
	 * @throws ParseException
	 */
	public static void openComplaint(TransmissionPack obj, Connection con) throws ParseException {
		if (obj instanceof TransmissionPack) {
			Complaint c = (Complaint) obj.getInformation();
			PreparedStatement pstmt;
			try {
				String query = "INSERT INTO zerli.complaints(complaintID, customerID, orderID, customerserviceID, description, branchID, complaintOpening, treatmentUntil, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, null);
				pstmt.setString(2, c.getCustomerID());
				pstmt.setString(3, c.getOrderID());
				pstmt.setString(4, c.getCustomerServiceID());
				pstmt.setString(5, c.getDescription());
				pstmt.setString(6, c.getBranchID());
				pstmt.setString(7, c.getComplaintOpening());
				Date d = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				d = sdf.parse(c.getComplaintOpening());

				Calendar cl = Calendar.getInstance();
				cl.setTime(d);
				cl.add(Calendar.DATE, 1);
				Date currentDatePlusOne = cl.getTime();
				pstmt.setString(8, sdf.format(currentDatePlusOne));
				pstmt.setString(9, c.getComplainState().name());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				obj.setResponse(Response.OPEN_COMPLAINT_FAILED);
				e.printStackTrace();
			}

			obj.setResponse(Response.OPEN_COMPLAINT_SUCCEED);

		} else {
			obj.setResponse(Response.OPEN_COMPLAINT_FAILED);
		}

	}

	/**
	 * in this method we getting the complaints from the db , by the
	 * customerServiceID only if the complaints still open.
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getComlaints(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs;
			Statement stmt;
			List<Complaint> complaints = new ArrayList<>();
			String query = "SELECT * FROM zerli.complaints WHERE customerserviceID='" + obj.getInformation()
					+ "' AND status='OPEN'";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					Complaint complaint = new Complaint(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
							ComplaintsStatus.valueOf(rs.getString(9)), getStatus(rs.getString(7)));

					complaints.add(complaint);
				}
				rs.close();
				if (complaints.size() > 0) {
					obj.setResponse(Response.FOUND_COMPLAINTS);
					obj.setInformation(complaints);
				} else {
					obj.setResponse(Response.DID_NOT_FOUND_COMPLAINTS);
					obj.setInformation(complaints);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * In this method we check if the complaint still opening after 24hrs.
	 * 
	 * @param start_date
	 * @return
	 */
	private static ComplaintsStatus getStatus(String start_date) {
		long difference_In_Days = 0;
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = sdf.parse(start_date);
			Date d2 = Calendar.getInstance().getTime();
			long difference_In_Time = d2.getTime() - d1.getTime();

			difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (difference_In_Days >= 1) {
			return ComplaintsStatus.DELAY;
		}

		return ComplaintsStatus.STILL_GOT_TIME;
	}

	/*
	 * In this method we getting all branches form the db.
	 * 
	 * @author Almog Madar
	 */

	public static void getBranches(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs;
			Statement stmt;
			List<Branches> branches = new ArrayList<>();
			String query = "SELECT * FROM zerli.branchs;";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					branches.add(Branches.valueOf(rs.getString(3).toUpperCase()));
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (branches.size() > 0) {

				obj.setInformation(branches);
				obj.setResponse(Response.FOUND_BRANCHES);
			} else {
				obj.setResponse(Response.NOT_FOUND_BRANCHES);
			}

		}
	}

	/*
	 * in this method we getting all the products of a specific branch, by branchID.
	 * 
	 * @author Almog Madar
	 */
	public static void getProductInBranch(TransmissionPack obj, Connection con) {
		// TODO Auto-generated method stub
		if (obj instanceof TransmissionPack) {
			ResultSet rs;
			Statement stmt;
			Branches branch = (Branches) obj.getInformation();
			List<ProductInBranch> productsInBranch = new ArrayList<>();
			String query = "SELECT * FROM zerli.productinbranch where branchID='" + branch.getNumber() + "';";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					ProductInBranch product = new ProductInBranch(rs.getString(1), rs.getString(2), rs.getInt(3));
					productsInBranch.add(product);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (productsInBranch.size() > 0) {
				obj.setInformation(productsInBranch);
				obj.setResponse(Response.FOUND_PRODUCT_IN_BRANCH);
			} else {
				obj.setResponse(Response.NOT_FOUND_PRODUCT_IN_BRANCH);
			}

		} else {
			obj.setResponse(Response.NOT_FOUND_PRODUCT_IN_BRANCH);
		}

	}

	/*
	 * in this method we adding delivery of order and update order price = regular
	 * price + shipment price .
	 * 
	 * @ author Almog Madar
	 */
	public static void addDelivery(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			if (obj.getInformation() instanceof Deliveries) {
				Statement stmt1;
				ResultSet rs1;
				PreparedStatement pstmt;
				Deliveries deliveries = (Deliveries) obj.getInformation();
				String query = "INSERT INTO zerli.deliveries (deliveryID, orderID, branchID, customerID,price,orderDate, expectedDelivery, arrivedDate, receiverName, address, phoneNumber, status) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ? , ? , ? , ? , ? , ? );";
				try {
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, null);
					pstmt.setString(2, deliveries.getOrderID());
					pstmt.setString(3, deliveries.getBranchID());
					pstmt.setString(4, deliveries.getCustomerID());
					pstmt.setDouble(5, deliveries.getPrice());
					pstmt.setString(6, deliveries.getOrderDate());
					pstmt.setString(7, deliveries.getExpectedDelivery());
					pstmt.setString(8, "");
					pstmt.setString(9, deliveries.getReceiverName());
					pstmt.setString(10, deliveries.getAddress());
					pstmt.setString(11, deliveries.getPhoneNumber());
					pstmt.setString(12, deliveries.getDeliveryStatus().name());
					pstmt.executeUpdate();

					// get price and update after delivery
					String query2 = "SELECT price FROM zerli.order where orderID=" + deliveries.getOrderID() + ";";
					double previusPrice = 0;
					stmt1 = con.createStatement();
					rs1 = stmt1.executeQuery(query2);
					while (rs1.next()) {
						previusPrice = rs1.getDouble(1);
					}
					rs1.close();

					// update price in order with delivery
					String query3 = "UPDATE zerli.order SET price = (?) WHERE orderID = '" + deliveries.getOrderID()
							+ "' AND customerID = '" + deliveries.getCustomerID() + "' AND branchID = '"
							+ deliveries.getBranchID() + "';";
					pstmt = con.prepareStatement(query3);
					pstmt.setDouble(1, previusPrice + deliveries.getPrice());
					pstmt.executeUpdate();

				} catch (SQLException e) {
					obj.setResponse(Response.FAILD_ADD_DELIVERY);
					e.printStackTrace();
					return;
				} catch (NullPointerException e) {
					obj.setResponse(Response.FAILD_ADD_DELIVERY);
					e.printStackTrace();
					return;
				}
				obj.setResponse(Response.ADD_DELIVERY_SUCCEED);
				return;
			} else {
				obj.setResponse(Response.FAILD_ADD_DELIVERY);
			}
		} else {
			obj.setResponse(Response.FAILD_ADD_DELIVERY);
		}
	}

	/**
	 * in this method we getting customer orders for cancellation process and
	 * products view of relevant orders
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getCustomerOrdersCancelation(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			ResultSet rs, rs2;
			Statement stmt, stmt2;
			List<Order> orders = new ArrayList<>();
			String customerID;
			if (obj.getInformation() == null)
				throw new NullPointerException();
			else
				customerID = (String) obj.getInformation();
			String query = "SELECT * FROM zerli.order  WHERE (customerID = '" + customerID
					+ "') AND ((status = 'PENDING') OR (status = 'PENDING_WITH_DELIVERY') OR (status = 'APPROVE_WITH_DELIVERY') OR (status = 'PENDING_WITH_IMIDATE_DELIVERY') OR (status = 'APPROVE_WITH_IMIDATE_DELIVERY'));";
			String query1 = "SELECT * FROM zerli.productinorder WHERE orderID='";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {

					Map<String, List<ProductInOrder>> products = new HashMap<>();

					stmt2 = con.createStatement();

					rs2 = stmt2.executeQuery(query1 + rs.getString(1) + "';");
					while (rs2.next()) {

						ProductInOrder newProduct = new ProductInOrder(rs2.getString(1), rs2.getString(2),
								rs2.getString(3), rs2.getDouble(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7),
								rs2.getString(8), rs2.getString(9), rs2.getInt(10), rs2.getString(11), false, 0);
						if (!products.containsKey(rs2.getString(3))) {
							List<ProductInOrder> product = new ArrayList<>();
							product.add(newProduct);
							products.put(rs2.getString(3), product);
						} else {
							products.get(rs2.getString(3)).add(newProduct);
						}

					}
					rs2.close();
					Order order = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
							rs.getString(5), rs.getString(7), rs.getString(8), products);
					order.setStatus(OrderStatus.valueOf(rs.getString(6)));
					orders.add(order);
				}
				rs.close();
				if (orders.size() > 0) {
					obj.setInformation(orders);
					obj.setResponse(Response.GET_CUSTOMER_ORDERS_SUCCESS);
					return;
				}
			} catch (SQLException e) {

				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			} catch (NullPointerException e) {

				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			}

		}
		obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
	}

	/**
	 * in this method we getting customer orders for history of orders and products
	 * view of relevant orders
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getCustomerOrdersHistory(TransmissionPack obj, Connection con) {

		if (obj instanceof TransmissionPack) {
			ResultSet rs, rs2;
			Statement stmt, stmt2;
			List<Order> orders = new ArrayList<>();
			String customerID;

			if (obj.getInformation() == null)
				throw new NullPointerException();
			else
				customerID = (String) obj.getInformation();

			String query = "SELECT * FROM zerli.order  WHERE (customerID = '" + customerID
					+ "') AND (status = 'ARRIVED') OR (status = 'APPROVE') OR (status = 'APPROVE_ORDER_CANCELATION') OR ('DECLINE_ORDER_CANCELATION') OR "
					+ " (status = 'APPROVE_ORDER_DELIVERY_CANCELATION') OR (status = 'DECLINE_ORDER_DELIVERY_CANCELATION') ;";
			String query1 = "SELECT * FROM zerli.productinorder WHERE orderID='";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					Map<String, List<ProductInOrder>> products = new HashMap<>();
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery(query1 + rs.getString(1) + "';");
					while (rs2.next()) {

						ProductInOrder newProduct = new ProductInOrder(rs2.getString(1), rs2.getString(2),
								rs2.getString(3), rs2.getDouble(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7),
								rs2.getString(8), rs2.getString(9), rs2.getInt(10), rs2.getString(11), false, 0);
						if (!products.containsKey(rs2.getString(3))) {
							List<ProductInOrder> product = new ArrayList<>();
							product.add(newProduct);
							products.put(rs2.getString(3), product);
						} else {
							products.get(rs2.getString(3)).add(newProduct);
						}

					}
					rs2.close();
					Order order = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
							rs.getString(5), rs.getString(7), rs.getString(8), products);
					order.setStatus(OrderStatus.valueOf(rs.getString(6)));
					orders.add(order);
				}
				rs.close();
				if (orders.size() > 0) {
					obj.setInformation(orders);
					obj.setResponse(Response.GET_CUSTOMER_ORDERS_SUCCESS);
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			}

		}
		obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
	}

	/**
	 * In this method we update the order status in the DB
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void updateOrder(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Order> order = (List<Order>) obj.getInformation();
			String updateOrderID = "UPDATE zerli.order SET status=?, expectedDelivery=? WHERE orderID='";
			String updateDeliveryStatus = "UPDATE zerli.deliveries SET status='READY_TO_GO' WHERE orderID='";
			String getRefund = "SELECT expectedRefund FROM zerli.cancelation WHERE orderID='";
			String updateCusomerBalance = "UPDATE zerli.customer SET balance=balance+? WHERE customerID='";
			String updateQuantityInAllZerLi = "UPDATE zerli.product SET quantity=quantity-? WHERE productID='";
			String updateQuantityInBranch = "UPDATE zerli.productinbranch SET quantity=quantity-? WHERE productID='";
			String deleteProductformOrder = "DELETE FROM zerli.productinorder WHERE orderID='";
			String deleteProductformDelivery = "DELETE FROM zerli.deliveries WHERE orderID='";

			for (Order o : order) {
				try {
					switch (o.getStatus()) {
					case APPROVE: {
						updateOrderIDStatus(con, updateOrderID, o);
						/**
						 * loop run on all the product in the specific order
						 */
						updateProducts(con, updateQuantityInAllZerLi, updateQuantityInBranch, o);
						break;
					}
					case APPROVE_WITH_DELIVERY: {
						updateDelivryStatusToReadyToGo(con, updateDeliveryStatus, o);
						updateOrderIDStatus(con, updateOrderID, o);
						updateProducts(con, updateQuantityInAllZerLi, updateQuantityInBranch, o);
						break;
					}
					case APPROVE_WITH_IMIDATE_DELIVERY: {
						Date d = new Date();
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							d = sdf.parse(o.getOrderDate());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Calendar cl = Calendar.getInstance();
						cl.setTime(d);
						cl.add(Calendar.HOUR_OF_DAY, 3);
						Date currentDatePlusOne = cl.getTime();
						o.setExpectedDelivery(sdf.format(currentDatePlusOne));

						updateDelivryStatusToReadyToGo(con, updateDeliveryStatus, o);
						updateOrderIDStatus(con, updateOrderID, o);
						updateProducts(con, updateQuantityInAllZerLi, updateQuantityInBranch, o);
						break;
					}
					case CANCEL_WITH_DELIVERY: {
						deleteDelivery(con, deleteProductformDelivery, o);
						updateOrderIDStatus(con, updateOrderID, o);
						break;
					}

					case CANCEL: {
						updateOrderIDStatus(con, updateOrderID, o);
						deleteProductInOrder(con, deleteProductformOrder, o);
						break;
					}
					case DECLINE_ORDER_CANCELATION: {
						updateOrderIDStatus(con, updateOrderID, o);
						/**
						 * loop run on all the product in the specific order
						 */
						updateProducts(con, updateQuantityInAllZerLi, updateQuantityInBranch, o);
						break;
					}
					case DECLINE_ORDER_DELIVERY_CANCELATION: {
						updateDelivryStatus(con, updateDeliveryStatus, o);
						updateOrderIDStatus(con, updateOrderID, o);
						updateProducts(con, updateQuantityInAllZerLi, updateQuantityInBranch, o);
						break;
					}
					case APPROVE_ORDER_DELIVERY_CANCELATION: {
						updateOrderIDStatus(con, updateOrderID, o);
						updateCustomerBalance(con, getRefund, updateCusomerBalance, o);
						deleteDelivery(con, deleteProductformDelivery, o);
						deleteProductInOrder(con, deleteProductformOrder, o);
						break;
					}
					case APPROVE_ORDER_CANCELATION: {
						updateCustomerBalance(con, getRefund, updateCusomerBalance, o);
						updateOrderIDStatus(con, updateOrderID, o);
						deleteProductInOrder(con, deleteProductformOrder, o);

						break;
					}
					default:
						break;
					}
				} catch (SQLException e) {
					obj.setResponse(Response.UPDATE_ORDER_FAILED);
					e.printStackTrace();
				}

			}
			obj.setResponse(Response.UPDATE_ORDER_SUCCEED);
		} else {
			obj.setResponse(Response.UPDATE_ORDER_FAILED);
		}

	}

	/**
	 * this method get all customers details that there order were cancel or approve
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void getCustomerDetailsForNotify(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<List<String>> cutomers = (List<List<String>>) obj.getInformation();
			List<List<User>> cutomersCallBack = new ArrayList<>();
			Statement stmt;
			for (List<String> list : cutomers) {
				List<User> users = new ArrayList<>();
				for (String detail : list) {
					try {
						stmt = con.createStatement();
						ResultSet rs;
						String getDetails = "SELECT firstName, email, phoneNumber FROM zerli.customer WHERE customerID='"
								+ detail + "';";
						rs = stmt.executeQuery(getDetails);
						while (rs.next() != false) {
							User u = new User(null, rs.getString(1), null, rs.getString(2), rs.getString(3), null,
									false);
							users.add(u);
						}
						rs.close();
					} catch (SQLException e) {
						obj.setResponse(Response.GET_ALL_CUTOMERS_TO_NOTIFIY_FAILED);
						e.printStackTrace();
					}
				}
				cutomersCallBack.add(users);
			}
			obj.setInformation(cutomersCallBack);
			obj.setResponse(Response.GET_ALL_CUTOMERS_TO_NOTIFIY_SUCCEED);

		} else {
			obj.setResponse(Response.GET_ALL_CUTOMERS_TO_NOTIFIY_FAILED);
		}
	}

	/**
	 * in this method we update the customer balance if the specific customer cancel
	 * request was approve and he is deserves it
	 * 
	 * @param con
	 * @param getRefund
	 * @param updateCusomerBalance
	 * @param o
	 * @throws SQLException
	 */
	private static void updateCustomerBalance(Connection con, String getRefund, String updateCusomerBalance, Order o)
			throws SQLException {
		Statement stmt;
		PreparedStatement pstmt;
		ResultSet rs;
		stmt = con.createStatement();
		rs = stmt.executeQuery(getRefund + o.getOrderID() + "'");
		while (rs.next()) {
			pstmt = con.prepareStatement(updateCusomerBalance + o.getCustomerID() + "'");
			pstmt.setString(1, rs.getString(1));
			pstmt.executeUpdate();
		}
	}

	/**
	 * in this method we update the delivery status if the status was change from
	 * pending to cancel
	 * 
	 * @param con
	 * @param updateDeliveryStatus
	 * @param o
	 * @throws SQLException
	 */
	private static void updateDelivryStatus(Connection con, String updateDeliveryStatus, Order o) throws SQLException {
		PreparedStatement pstmt1;
		pstmt1 = con.prepareStatement(updateDeliveryStatus + o.getOrderID() + "'");
		pstmt1.setString(1, o.getStatus().name());
		pstmt1.executeUpdate();
	}

	/**
	 * in this method we checking and updating if the order delivery was approve it
	 * change his status to ready to go for the delivery agent
	 * 
	 * @param con
	 * @param updateDeliveryStatus
	 * @param o
	 * @throws SQLException
	 */
	private static void updateDelivryStatusToReadyToGo(Connection con, String updateDeliveryStatus, Order o)
			throws SQLException {
		PreparedStatement pstmt1;
		pstmt1 = con.prepareStatement(updateDeliveryStatus + o.getOrderID() + "'");
		pstmt1.executeUpdate();
	}

	/**
	 * in this method we Update the orders quantity if the orders was approve it
	 * update the product quantity
	 * 
	 * @param con
	 * @param updateQuantityInAllZerLi
	 * @param updateQuantityInBranch
	 * @param o
	 * @throws SQLException
	 */
	private static void updateProducts(Connection con, String updateQuantityInAllZerLi, String updateQuantityInBranch,
			Order o) throws SQLException {
		for (String key : o.getItems().keySet()) {
			for (ProductInOrder p : o.getItems().get(key)) {
				updateProductQuentity(con, updateQuantityInAllZerLi, p);
				updateProductQuentity(con, updateQuantityInBranch, p);
			}
		}
	}

	/**
	 * in this method we checking if the delivery request wasn't approve it delete
	 * the specific order from the the delivery table
	 * 
	 * @param con
	 * @param deleteProductformDelivery
	 * @param o
	 * @throws SQLException
	 */
	private static void deleteDelivery(Connection con, String deleteProductformDelivery, Order o) throws SQLException {
		PreparedStatement pstmt3;
		pstmt3 = con.prepareStatement(deleteProductformDelivery + o.getOrderID() + "'");
		pstmt3.executeUpdate();
	}

	/**
	 * in this method we delete the product form the order if the branch meager
	 * cancel the customer order
	 * 
	 * @param con
	 * @param query3
	 * @param o
	 * @throws SQLException
	 */
	private static void deleteProductInOrder(Connection con, String query3, Order o) throws SQLException {
		deleteDelivery(con, query3, o);
	}

	/**
	 * this method update quantity in the relevant table if the branch manager
	 * approve the order
	 * 
	 * @param con
	 * @param query2
	 * @param p
	 * @throws SQLException
	 */
	private static void updateProductQuentity(Connection con, String query2, ProductInOrder p) throws SQLException {
		PreparedStatement pstmt2;
		pstmt2 = con.prepareStatement(query2 + p.getID() + "'");
		pstmt2.setInt(1, p.getProductQuantityInCart());
		pstmt2.executeUpdate();
	}

	/**
	 * this method update the order status according to the branch manager decision
	 * 
	 * @param con
	 * @param query
	 * @param o
	 * @throws SQLException
	 */
	private static void updateOrderIDStatus(Connection con, String query, Order o) throws SQLException {
		PreparedStatement pstmt1;

		pstmt1 = con.prepareStatement(query + o.getOrderID() + "'");
		pstmt1.setString(1, o.getStatus().name());
		pstmt1.setString(2, o.getExpectedDelivery());
		pstmt1.executeUpdate();
	}

	/**
	 * in this method we getting the Survey questions from the DB, by the topic .
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getSurvyQuestions(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			Statement stmt;
			ResultSet rs;
			List<Question> questions = new ArrayList<>();
			String topic = (String) obj.getInformation();
			String getquestion = "SELECT * FROM zerli.surveyquestions WHERE topic='" + topic + "'";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(getquestion);
				while (rs.next()) {
					Question question = new Question(rs.getString(1), rs.getInt(2), rs.getString(3));
					questions.add(question);
				}
				rs.close();
				if (questions.size() > 0) {
					obj.setInformation(questions);
					obj.setResponse(Response.GET_SURVY_QUESTION_SUCCEED);
				} else {
					obj.setResponse(Response.GET_SURVY_QUESTION_FAILED);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			obj.setResponse(Response.GET_SURVY_QUESTION_FAILED);
		}

	}

	/**
	 * in this method we insert the survey result of the specific survey
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void insertSurvy(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Question> questions = (List<Question>) obj.getInformation();
			String query = "INSERT INTO zerli.surveys(surveysresultsID, branchID, topic, questionNumber1, questionNumber2, questionNumber3, questionNumber4, questionNumber5, questionNumber6, answerNumber1, answerNumber2, answerNumber3, answerNumber4, answerNumber5, answerNumber6, targetAudience, date) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				insertSurveyResult(con, questions, query);
				obj.setResponse(Response.ISERT_SURVEY_SUCCEED);

			} catch (SQLException e) {
				obj.setResponse(Response.ISERT_SURVEY_FAILED);
				e.printStackTrace();
			}

		} else {
			obj.setResponse(Response.ISERT_SURVEY_FAILED);
		}

	}

	/**
	 * in this method we insert all the survey to the DB
	 * 
	 * @param con
	 * @param questions
	 * @param query
	 * @throws SQLException
	 */
	private static void insertSurveyResult(Connection con, List<Question> questions, String query) throws SQLException {
		PreparedStatement pstmt;
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, null);
		pstmt.setString(2, questions.get(0).getBranchID());
		pstmt.setString(3, questions.get(0).getTopic());

		pstmt.setInt(4, questions.get(0).getQuestionNumber());
		pstmt.setInt(5, questions.get(1).getQuestionNumber());
		pstmt.setInt(6, questions.get(2).getQuestionNumber());
		pstmt.setInt(7, questions.get(3).getQuestionNumber());
		pstmt.setInt(8, questions.get(4).getQuestionNumber());
		pstmt.setInt(9, questions.get(5).getQuestionNumber());
		pstmt.setInt(10, questions.get(0).getAnswer());
		pstmt.setInt(11, questions.get(1).getAnswer());
		pstmt.setInt(12, questions.get(2).getAnswer());
		pstmt.setInt(13, questions.get(3).getAnswer());
		pstmt.setInt(14, questions.get(4).getAnswer());
		pstmt.setInt(15, questions.get(5).getAnswer());
		pstmt.setString(16, questions.get(0).getTargetAudience());
		pstmt.setString(17, questions.get(0).getDate());
		pstmt.executeUpdate();
	}

	/**
	 * This method getting all the deliveries from the DB,only delivery's that are
	 * ready(status) or approved by the manager
	 * 
	 * @param obj
	 * @param con
	 */
	public static void GetDeliveriesFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			String branchID = (String) obj.getInformation();
			List<Deliveries> deliveries = new ArrayList<>();
			List<ProductInOrder> orderProducts = null;
			Statement stmt1, stmt2;
			try {
				stmt1 = con.createStatement();
				ResultSet rs1, rs2;
				String getDeliveries = "SELECT * FROM zerli.deliveries WHERE status = 'READY_TO_GO' AND branchID ='"
						+ branchID + "';";
				rs1 = stmt1.executeQuery(getDeliveries);
				while (rs1.next()) {
					// nameOfproduct, productQuantityInOrder, price
					Deliveries delivery = new Deliveries(rs1.getInt(1), rs1.getString(2), rs1.getString(3),
							rs1.getString(4), rs1.getDouble(5), rs1.getString(6), rs1.getString(7), rs1.getString(8),
							rs1.getString(9), rs1.getString(10), rs1.getString(11),
							DeliveryStatus.valueOf(rs1.getString(12)), null);
					deliveries.add(delivery);
					String getProductsInOrder = "SELECT * FROM zerli.productInOrder WHERE orderID = '"
							+ rs1.getString(2) + "';";
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery(getProductsInOrder);
					orderProducts = new ArrayList<>();
					while (rs2.next()) {
						ProductInOrder p = new ProductInOrder(rs2.getString(1), rs2.getString(2), rs2.getString(3),
								rs2.getDouble(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7), rs2.getString(8),
								rs2.getString(9), rs2.getInt(10), rs2.getString(11), false, 0);
						orderProducts.add(p);
						delivery.setOrderProducts(orderProducts);
					}
					rs2.close();
				}
				rs1.close();
				obj.setInformation(deliveries);
				obj.setResponse(Response.FOUND_DELIVERIES);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				obj.setResponse(Response.NOT_FOUND_DELIVERIES);
				obj.setInformation(deliveries);
				return;
			}
		}
		obj.setResponse(Response.NOT_FOUND_DELIVERIES);
		obj.setInformation(null);
	}

	/**
	 * in this method we updating that the delivery arrived to the customer/s .
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void UpdateDeliveriesStatusesInDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Deliveries> deliveries = (List<Deliveries>) obj.getInformation();
			try {
				for (Deliveries d : deliveries) {
					String updateStatuses = "UPDATE zerli.deliveries SET status=?, arrivedDate =? WHERE deliveryID='"
							+ d.getDeliveryID() + "';";
					String updateOrderArrived = "UPDATE zerli.order SET status=? WHERE orderID='" + d.getOrderID()
							+ "';";
					PreparedStatement pstmt1 = con.prepareStatement(updateStatuses);
					pstmt1.setString(1, d.getDeliveryStatus().name());
					pstmt1.setString(2, d.getArrivedDate());
					pstmt1.executeUpdate(); // check if the query failed
					if(d.getDeliveryStatus() == DeliveryStatus.ARRIVED) {	
						PreparedStatement pstmt2 = con.prepareStatement(updateOrderArrived);
						pstmt2.setString(1, d.getDeliveryStatus().name());
						pstmt2.executeUpdate(); // check if the query failed
					}
				}
				obj.setResponse(Response.UPDATE_DELIVERIES_STATUS_SUCCESS);
			} catch (Exception e) {
				obj.setResponse(Response.UPDATE_DELIVERIES_STATUS_FAILED);
				e.printStackTrace();
				return;
			}
		} else {
			obj.setResponse(Response.UPDATE_DELIVERIES_STATUS_FAILED);
			return;
		}
	}

	/**
	 * this method request to cancel order by user , create cancellation and update
	 * order status to CANCEL_ORDER_BY_CUSTOMER
	 * 
	 * @param obj
	 * @param con
	 */
	public static void cancelOrderByCustomer(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			if (obj.getInformation() instanceof Cancellation) {
				PreparedStatement pstmt;
				Cancellation info = (Cancellation) obj.getInformation();
				String query = "UPDATE zerli.order SET status = ? WHERE orderID = ?;";
				String query2 = "INSERT INTO zerli.cancelation (CancelationID, orderID,customerID,expectedRefund) VALUES (?,?,?,?);";
				try {
					// update order status
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, info.getStatus().name());
					pstmt.setString(2, info.getOrderID());

					pstmt.executeUpdate();

					// refund information setup
					pstmt = con.prepareStatement(query2);
					pstmt.setString(1, null);
					pstmt.setString(2, info.getOrderID());
					pstmt.setString(3, info.getCustomerID());
					pstmt.setDouble(4, info.getExpectedRefund());
					pstmt.executeUpdate();

				} catch (SQLException e) {
					obj.setResponse(Response.CANCEL_ORDER_BY_CUSTOMER_FAILD);
					e.printStackTrace();
					return;
				}
				obj.setResponse(Response.CANCEL_ORDER_BY_CUSTOMER_SUCCESS);
				return;
			} else {
				obj.setResponse(Response.CANCEL_ORDER_BY_CUSTOMER_FAILD);
			}
		} else {
			obj.setResponse(Response.CANCEL_ORDER_BY_CUSTOMER_FAILD);
		}

	}

	/**
	 * this method updating the DB, that the delivery was late.
	 * 
	 * @param obj
	 * @param con
	 */

	public static void UpdateDeliveryWasLateDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			Deliveries deliveries = (Deliveries) obj.getInformation();
			Statement stmt;
			double currentBalance, newBalance;
			String currBalance = null, getCurrentBalance;
			try {
				stmt = con.createStatement();
				ResultSet rs;
				/**
				 * get the current balance of the customer that we will be able to update it
				 * correct
				 */
				getCurrentBalance = "SELECT balance FROM zerli.customer WHERE customerID='" + deliveries.getCustomerID()
						+ "';";
				rs = stmt.executeQuery(getCurrentBalance);
				while (rs.next()) {
					currBalance = rs.getString(1); /* the current balance of the customer */
				}
				rs.close();
				currentBalance = Double.parseDouble(currBalance); /* parse the current balance to double */
				newBalance = currentBalance + deliveries.getPrice(); /* the real new balance of the customer */

				/**
				 * Update the customer table - add the amount of the refund to the balance of
				 * the specific customer
				 */
				DeliveryLateRefundBalance(con, deliveries, newBalance);
				/**
				 * Update refund table with the refund details
				 */
				updateRefundsTable(con, deliveries);
				obj.setResponse(Response.UPDATE_DELIVERY_LATE_REFUND_SUCCESS);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				obj.setResponse(Response.UPDATE_DELIVERY_LATE_REFUND_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.UPDATE_DELIVERY_LATE_REFUND_FAILED);
			return;
		}
	}

	/**
	 * in this method we update the refund table
	 * 
	 * @param con
	 * @param deliveries
	 * @throws SQLException
	 */
	private static void updateRefundsTable(Connection con, Deliveries deliveries) throws SQLException {
		String updateRefundTable;

		updateRefundTable = "INSERT INTO zerli.refunds(refundID, orderID, customerID, ammount, reason, date)VALUES(?,?,?,?,?,?);";
		PreparedStatement pstmt = con.prepareStatement(updateRefundTable);

		pstmt.setString(1, null);
		pstmt.setString(2, deliveries.getOrderID());
		pstmt.setString(3, deliveries.getCustomerID());
		pstmt.setString(4, String.valueOf(deliveries.getPrice()));

		pstmt.setString(5, "Delivery");

		Calendar c = Calendar.getInstance();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(c.getTimeInMillis());
		pstmt.setTimestamp(6, timestamp);
		pstmt.executeUpdate();
	}

	/**
	 * in this method we update the balance on the customer table by the refund that
	 * he deserve
	 * 
	 * @param con
	 * @param deliveries
	 * @param newBalance
	 * @throws SQLException
	 */
	private static void DeliveryLateRefundBalance(Connection con, Deliveries deliveries, double newBalance)
			throws SQLException {
		String giveRefund;
		giveRefund = "UPDATE zerli.customer SET balance=(?) WHERE customerID='" + deliveries.getCustomerID() + "';";
		PreparedStatement pstmt = con.prepareStatement(giveRefund);
		pstmt.setString(1, String.valueOf(newBalance));
		pstmt.executeUpdate();
	}

	/**
	 * in this method we getting the customer information , that will be used for
	 * the mail progress
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getCustomerEmailAndPhoneFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			String customerID = (String) obj.getInformation();
			List<String> details = new ArrayList<>();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs;

				String getDetails = "SELECT firstName, email, phoneNumber FROM zerli.customer WHERE customerID='"
						+ customerID + "';";
				rs = stmt.executeQuery(getDetails);
				while (rs.next() != false) {
					details.add(rs.getString(1));
					details.add(rs.getString(2));
					details.add(rs.getString(3));

				}
				rs.close();
				obj.setInformation(details);

				obj.setResponse(Response.GET_CUSTOMER_DETAILS_SUCCESS);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_DETAILS_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.GET_CUSTOMER_DETAILS_FAILED);
		}
	}

	/**
	 * In this method getting the customer orders that waiting for cancellation
	 * approved by the manager.
	 * 
	 * @param obj
	 * @param con
	 */

	public static void getCustomerOrdersCancelationWaiting(TransmissionPack obj, Connection con) {
		// TODO Auto-generated method stub
		if (obj instanceof TransmissionPack) {
			ResultSet rs, rs2;
			Statement stmt, stmt2;
			List<Order> orders = new ArrayList<>();
			String customerID;

			if (obj.getInformation() == null)
				throw new NullPointerException();
			else
				customerID = (String) obj.getInformation();

			String query = "SELECT * FROM zerli.order  WHERE (customerID = '" + customerID
					+ "') AND (status = 'CANCEL_ORDER_BY_CUSTOMER') OR (status = 'CANCEL_ORDER_DELIVERY_BY_CUSTOMER');";
			String query1 = "SELECT * FROM zerli.productinorder WHERE orderID='";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					Map<String, List<ProductInOrder>> products = new HashMap<>();
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery(query1 + rs.getString(1) + "';");
					while (rs2.next()) {

						ProductInOrder newProduct = new ProductInOrder(rs2.getString(1), rs2.getString(2),
								rs2.getString(3), rs2.getDouble(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7),
								rs2.getString(8), rs2.getString(9), rs2.getInt(10), rs2.getString(11), false, 0);
						if (!products.containsKey(rs2.getString(3))) {
							List<ProductInOrder> product = new ArrayList<>();
							product.add(newProduct);
							products.put(rs2.getString(3), product);
						} else {
							products.get(rs2.getString(3)).add(newProduct);
						}

					}
					rs2.close();

					Order order = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
							rs.getString(5), rs.getString(7), rs.getString(8), products);
					order.setStatus(OrderStatus.valueOf(rs.getString(6)));

					orders.add(order);
				}

				rs.close();
				if (orders.size() > 0) {
					obj.setInformation(orders);
					obj.setResponse(Response.GET_CUSTOMER_ORDERS_SUCCESS);
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
				return;
			}

		}
		obj.setResponse(Response.GET_CUSTOMER_ORDERS_FAILD);
	}

	/**
	 * in this method we getting the max product id from the DB .
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getMaxProductIDFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			String maxID = null;
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs;
				String getMaxID = "SELECT MAX(CAST(productID AS SIGNED)) FROM zerli.product;";
				rs = stmt.executeQuery(getMaxID);
				if (rs.next()) {
					maxID = rs.getString(1);
				}
				rs.close();
				System.out.println(maxID);
				obj.setInformation(maxID);
				obj.setResponse(Response.GET_MAX_PRODUCT_ID_SUCCESS);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_MAX_PRODUCT_ID_FAILED);
				return;
			}
		}
		obj.setResponse(Response.GET_MAX_PRODUCT_ID_FAILED);
	}

	/**
	 * in this method we editing an exist product on the catalog, by its ID.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void marketingWorkerEditCatalog(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Product> productsToAdd = new ArrayList<>();
			productsToAdd = (List<Product>) obj.getInformation();
			int countRemoving = 0, quantityInBranch, remaining;
			List<String> branchList = getAllBranchId(con);
			List<ProductInBranch> productInBranchs = new ArrayList<>();
			String updateBranchesSupply = "UPDATE zerli.productinbranch SET quantity= (?) WHERE branchID =(?) AND productID = (?);";
			PreparedStatement pstmt, pstmt2;
			try {
				for (int i = 0; i < productsToAdd.size(); i++) {
					String updateStatuses = "UPDATE zerli.product SET name=(?), price =(?), backGroundColor=(?), picture=(?), quantity=(?), itemType=(?), dominateColor=(?), isOnSale=(?), fixPrice=(?) WHERE productID='"
							+ productsToAdd.get(i).getID() + "';";
					pstmt = con.prepareStatement(updateStatuses);
					pstmt.setString(1, productsToAdd.get(i).getName());
					pstmt.setDouble(2, productsToAdd.get(i).getPrice());
					pstmt.setString(3, productsToAdd.get(i).getbackGroundColor());
					pstmt.setString(4, productsToAdd.get(i).getImgSrc());
					pstmt.setInt(5, productsToAdd.get(i).getQuantity());
					pstmt.setString(6, productsToAdd.get(i).getItemType());
					pstmt.setString(7, productsToAdd.get(i).getDominateColor());
					pstmt.setBoolean(8, productsToAdd.get(i).getIsOnSale());
					pstmt.setDouble(9, productsToAdd.get(i).getFixPrice());
					if (pstmt.executeUpdate() != 0) {
						countRemoving++;
					}
					quantityInBranch = Integer.valueOf(productsToAdd.get(i).getQuantity()) / branchList.size();
					remaining = Integer.valueOf(productsToAdd.get(i).getQuantity()) % branchList.size();
					for (int j = 0; j < branchList.size(); j++) {
						ProductInBranch p = new ProductInBranch(branchList.get(j), productsToAdd.get(i).getID(),
								quantityInBranch);
						productInBranchs.add(p);
					}
					if (remaining > 0) { // if there is a remaining we give it to the first branch.
						productInBranchs.get(0).setQuantity(productInBranchs.get(0).getQuantity() + remaining);
					}
					for (int k = 0; k < productInBranchs.size(); k++) {
						pstmt2 = con.prepareStatement(updateBranchesSupply);
						pstmt2.setString(1, String.valueOf(productInBranchs.get(k).getQuantity()));
						pstmt2.setString(2, productInBranchs.get(k).getBranchID());
						pstmt2.setString(3, productInBranchs.get(k).getProductID());
						if (pstmt2.executeUpdate() == 0) {
							obj.setResponse(Response.EDIT_PRODUCTS_ON_THE_CATALOG_FAILED);
							return;
						}
					}
					if (countRemoving == productsToAdd.size()) {
						obj.setResponse(Response.EDIT_PRODUCTS_ON_THE_CATALOG_SUCCESS);
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.EDIT_PRODUCTS_ON_THE_CATALOG_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.EDIT_PRODUCTS_ON_THE_CATALOG_FAILED);
		}

	}

	/**
	 * in this method we adding new products into the catalog, we getting them on
	 * list of products and, adding one by one.
	 * 
	 * @param obj
	 * @param con
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void marketingWorkerAddToCatalog(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<Product> productsToAdd = new ArrayList<>();
			productsToAdd = (List<Product>) obj.getInformation();
			int countInseration = 0;
			String insertNewItem = "INSERT INTO zerli.product(productID, name, price, backGroundColor, picture, quantity, itemType, dominateColor, isOnSale, fixPrice)VALUES(?,?,?,?,?,?,?,?,?,?);";
			List<String> branchList = getAllBranchId(con);
			String divideProductsToBranches = "INSERT INTO zerli.productinbranch (branchID , productID, quantity) VALUES (?,?,?);";
			int quantityInBranch, remaining;
			List<ProductInBranch> productInBranchs = new ArrayList<>();

			try {
				for (int i = 0; i < productsToAdd.size(); i++) {
					PreparedStatement pstmt, pstmt2;
					pstmt = con.prepareStatement(insertNewItem);
					pstmt.setString(1, productsToAdd.get(i).getID());
					pstmt.setString(2, productsToAdd.get(i).getName());
					pstmt.setDouble(3, productsToAdd.get(i).getPrice());
					pstmt.setString(4, productsToAdd.get(i).getbackGroundColor());
					pstmt.setString(5, productsToAdd.get(i).getImgSrc());
					pstmt.setInt(6, productsToAdd.get(i).getQuantity());
					pstmt.setString(7, productsToAdd.get(i).getItemType());
					pstmt.setString(8, productsToAdd.get(i).getDominateColor());
					pstmt.setBoolean(9, productsToAdd.get(i).getIsOnSale());
					pstmt.setDouble(10, productsToAdd.get(i).getFixPrice());
					if (pstmt.executeUpdate() != 0) {
						countInseration++;
					}

					quantityInBranch = Integer.valueOf(productsToAdd.get(i).getQuantity()) / branchList.size();
					remaining = Integer.valueOf(productsToAdd.get(i).getQuantity()) % branchList.size();
					for (int j = 0; j < branchList.size(); j++) {
						ProductInBranch p = new ProductInBranch(branchList.get(j), productsToAdd.get(i).getID(),
								quantityInBranch);
						productInBranchs.add(p);
					}
					if (remaining > 0) { // if there is a remaining we give it to the first branch.
						productInBranchs.get(0).setQuantity(productInBranchs.get(0).getQuantity() + remaining);
					}

					for (int k = 0; k < productInBranchs.size(); k++) {
						pstmt2 = con.prepareStatement(divideProductsToBranches);
						pstmt2.setString(1, productInBranchs.get(k).getBranchID());
						pstmt2.setString(2, productInBranchs.get(k).getProductID());
						pstmt2.setString(3, String.valueOf(productInBranchs.get(k).getQuantity()));
						if (pstmt2.executeUpdate() == 0) {
							obj.setResponse(Response.ADDING_TO_THE_CATALOG_FAILED);
							return;
						}
					}
					if (countInseration == productsToAdd.size()) {
						obj.setResponse(Response.ADDING_TO_THE_CATALOG_SUCCESS);
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.ADDING_TO_THE_CATALOG_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.ADDING_TO_THE_CATALOG_FAILED);
		}
	}

	/**
	 * in this method we remove the items that the marketing worker send to us ,by
	 * the product id.
	 * 
	 * @param obj
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public static void marketingWorkerRemoveFromCatalog(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			List<String> productsToRemove = new ArrayList<>();
			productsToRemove = (List<String>) obj.getInformation();
			int removeSucess = 0;
			String removeProductsFromBranches = "DELETE FROM zerli.productinbranch WHERE productID= ? ";
			PreparedStatement pstmt1, pstmt2;
			try {
				for (int i = 0; i < productsToRemove.size(); i++) {
					pstmt1 = con.prepareStatement("DELETE FROM zerli.product WHERE productID= ?");
					pstmt1.setString(1, productsToRemove.get(i));
					if (pstmt1.executeUpdate() != 0) {
						removeSucess++;
					}
					pstmt2 = con.prepareStatement(removeProductsFromBranches);
					pstmt2.setString(1, productsToRemove.get(i));
					if (pstmt2.executeUpdate() == 0) {
						obj.setResponse(Response.REMOVE_FROM_THE_CATALOG_FAILED);
						return;
					}
					if (removeSucess == productsToRemove.size()) {
						obj.setResponse(Response.REMOVE_FROM_THE_CATALOG_SUCCESS);
						return;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setResponse(Response.REMOVE_FROM_THE_CATALOG_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.REMOVE_FROM_THE_CATALOG_FAILED);
		}
	}

	/**
	 * in this method we getting the customer phone , name, email .
	 * 
	 * @param obj
	 * @param con
	 */
	public static void getCustomerDetailsFromDB(TransmissionPack obj, Connection con) {
		if (obj instanceof TransmissionPack) {
			String customerID = (String) obj.getInformation();
			System.out.println(customerID);
			List<String> details = new ArrayList<>();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs;
				String getDetails = "SELECT firstName, email, phoneNumber FROM zerli.customer WHERE customerID='"
						+ customerID + "';";
				rs = stmt.executeQuery(getDetails);
				while (rs.next() != false) {
					details.add(rs.getString(1));
					details.add(rs.getString(2));
					details.add(rs.getString(3));
				}
				rs.close();
				System.out.println(details);
				obj.setInformation(details);
				obj.setResponse(Response.GET_CUSTOMER_DETAILS_SUCCESS);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_DETAILS_FAILED);
				return;
			}
		} else {
			obj.setResponse(Response.GET_CUSTOMER_DETAILS_FAILED);
		}
	}

}
