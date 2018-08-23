package by.epam.connection.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.service.PropertiesHandler;

public class ConnectionPool {
	private static final Logger LOG = LogManager.getLogger(ConnectionPool.class);
	
	public static final String DB_URL = "db.url";
	public static final String DB_USER = "db.user";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_POOLSIZE = "db.poolsize";
	
	public void initialize() {
		Properties prop = null;
		try {
			prop = PropertiesHandler.readProperties("resources/connectionpool.properties");
			LOG.info("properties successfully loaded\n");
		} catch (IOException e) {
			LOG.debug("loading properties error: ", e);
		}
		Connection connection = null;
		com.mysql.cj.jdbc.Driver driver = null;
		try {
			driver = new com.mysql.cj.jdbc.Driver();
			java.sql.DriverManager.registerDriver(driver);
			// TODO: implement connection wrapper !!!
			connection = java.sql.DriverManager.getConnection(
					prop.getProperty(DB_URL),
					prop.getProperty(DB_USER),
					prop.getProperty(DB_PASSWORD));
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
			}
			if (driver != null) {
				try {
					java.sql.DriverManager.deregisterDriver(driver);
					LOG.info("driver deregistred\n");
				} catch (SQLException e) {
					LOG.debug("SQL exception: ", e);
				}
			}
		}
		
	}

}
