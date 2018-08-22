package by.epam.connection.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.runner.Runner;

public class ConnectionPool {
	private static final Logger LOG = LogManager.getLogger(ConnectionPool.class);
	
	public void initialize() {
		Connection connection = null;
		String url = "jdbc:mysql://localhost:3306/admission_committee"+
                "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";
		try {
			java.sql.DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver()); // find real driver name
			// TODO: implement connection wrapper !!!
			connection = java.sql.DriverManager.getConnection(
					url,
					"root",
					"dragonlance");
			LOG.info("Connection established\n");
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM `faculties`");
			while (rs.next()) {
				String faculty = rs.getString("name_en");
				LOG.info(faculty + "\n");
			}
		} catch (SQLException e) {
			LOG.debug("SQL exception: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOG.debug("SQL exception: ", e);
				}
				// TODO: implement driver de-registration
			}
		}
		
	}

}
