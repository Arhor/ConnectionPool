package by.epam.connection.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
			LOG.info(" id |             faculty\n");
			LOG.info("----+------------------------------------------------\n");
			while (rs.next()) {
				int id = rs.getInt(1);
				String faculty = rs.getString(2);
				LOG.info(id + " | " + faculty + "\n");
			}
		} catch (SQLException e) {
			LOG.debug("SQL exception: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
					LOG.info("connection closed\n");
				} catch (SQLException e) {
					LOG.debug("SQL exception: ", e);
				}
				// TODO: implement driver de-registration
			}
		}
		
	}

}
