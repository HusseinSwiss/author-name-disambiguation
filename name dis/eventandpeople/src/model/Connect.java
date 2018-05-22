package model;
import java.sql.*;

import constant.Cnst;
 

public class Connect {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public Connection connect() throws  Exception{
		
		String myDriver = Cnst.DRIVER;
        String myUrl = Cnst.DB_URL;
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, Cnst.USER_NAME, Cnst.PASSWORD);
		
		return conn;
		
	}

}
