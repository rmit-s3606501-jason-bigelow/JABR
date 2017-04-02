package org.jabst.jabs;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.hsqldb.HsqlException;


public class SessionManager extends Application {
	// Fields
	private DatabaseManager dbm;

	public enum Window {
		LOGIN, REGISTER, BUSINESSMENU, CUSTOMERMENU
	}

	// ++++++++++++THIS IS YOUR NEW MAIN++++++++++++++++
	@Override
	public void start(Stage primaryStage) {
		Window currentWindow = Window.LOGIN;
		String username = "";
		String password = "";

		for(;;) {
			switch(currentWindow) {
				case LOGIN:
					LoginInfo lInfo = LoginGUI.display(this, username, password);
					username = lInfo.username;
					password = lInfo.password;
					if(lInfo.button == LoginInfo.Buttons.LOGIN) {
						// open respective window
						currentWindow = Window.BUSINESSMENU;
						currentWindow = Window.CUSTOMERMENU;
// TODO: select the correct window for the user
					} else if(lInfo.button == LoginInfo.Buttons.REGISTER) {
						// open register window
						currentWindow = Window.REGISTER;
					}
				break;
				case REGISTER:
					RegisterInfo rInfo = RegisterGUI.display(this, username, password);
					username = rInfo.username;
					password = rInfo.password;
					if(rInfo.button == RegisterInfo.Buttons.REGISTER) {
						// open respective window
						load_database();
						currentWindow = Window.LOGIN;
					} else if(rInfo.button == RegisterInfo.Buttons.LOGIN) {
						// open register window
						currentWindow = Window.LOGIN;
					}
				break;
				case BUSINESSMENU:
					BusinessInfo bInfo = BusinessMenuGUI.display(this);
					if(bInfo.button == BusinessInfo.Buttons.OK) {
						System.exit(0);
						return;
					}
				break;
				case CUSTOMERMENU:
					CustomerInfo cInfo = CustomerMenuGUI.display(this);
					if(cInfo.button == CustomerInfo.Buttons.OK) {
						System.exit(0);
						return;
					}
				break;
			}
		}

	}
	
	public void load_database() {
		try {
			dbm = new DatabaseManager(DatabaseManager.dbDefaultFileName);
		} catch (SQLException sqle) {
			System.err.println("FATAL: SessionManager: Could not open DatabaseManager");
			System.exit(1);
		}
	}
	
	public SessionManager() {
		load_database();
	}
	
	// Methods
	public boolean loginUser(String username, String password){
		try {
			return dbm.checkUser(username, password);
		} catch (SQLException sqle) {
			return false;
		}
	}

	public boolean registerUser(String username, String password,
		String name, String address, String phone)
	{
		// If it throws an exception, it failed. Otherwise it succeeded
		try {
			dbm.addUser(username, password, name, address, phone);
			System.out.println("SessionManager: Successfully added user with dbm");
			return true;
		} catch (SQLException sqle) {
			// false if user exists or the database is somehow broken
			if (sqle instanceof SQLIntegrityConstraintViolationException) {
				System.err.println(
					"SessionManager: Adding user failed: Already a user with that username"
				);
			}
			else {
				System.err.println(
					"SessionManager: Adding user failed: Other database error"
				);
				sqle.printStackTrace();
			}
			return false;
		}
	}
	
	public void populateCustomer(String username){
		// Request object data from DatabaseManager
		// create new customer object
	}
	
	public void populateBusiness(String username){
		// Request object data from DatabaseManager
		// create new business object
	}
	
	void launchCustomerMenu(){
		// NYI
		//Application.launch(CustomerMenu.class);
	}
	
	void launchBusinessMenu(){
		// NYI
		//Application.launch(BusinessMenu.class);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}