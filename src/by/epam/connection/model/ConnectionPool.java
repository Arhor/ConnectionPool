package by.epam.connection.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.runner.Runner;

public class ConnectionPool {
	private static final Logger LOG = LogManager.getLogger(ConnectionPool.class);
	
	public void initialize() {
		Connection connection = null;
		String dbAddress = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			dbAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			LOG.error("error: ", e);
		}

		try {
			java.sql.DriverManager.registerDriver(); // find real driver name
			// TODO: implement connection wrapper !!!
			connection = java.sql.DriverManager.getConnection(
					"jdbc:mysql:" + dbAddress + ":3306/admission_committee",
					"root",
					"dragonlance");
			
			Statement st = connection.createStatement();
		} finally {
			if (connection != null) {
				connection.close();
				// TODO: implement driver de-registration
			}
		}
		
	}

}
