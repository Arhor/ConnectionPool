package by.epam.connection.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Properties;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.service.PropertiesHandler;

public enum ConnectionPool {
	
	SINGLTONE;
	
	private final Logger LOG = LogManager.getLogger(ConnectionPool.class);
	
	public static final String DB_URL = "db.url";
	public static final String DB_USER = "db.user";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_POOLSIZE = "db.poolsize";
	
	public final int POOL_SIZE;
	
	private Properties prop;
	private com.mysql.cj.jdbc.Driver driver;
	
	ArrayBlockingQueue<Connection> availibleConnections;
	ArrayDeque<Connection> usedConnections;
	
	ConnectionPool() {
		try {
			prop = PropertiesHandler.readProperties("resources/connectionpool.properties");
			LOG.info("properties successfully loaded...\n");
		} catch (IOException e) {
			LOG.debug("loading properties error: ", e);
		}
		POOL_SIZE = Integer.parseInt(prop.getProperty(DB_POOLSIZE));
		availibleConnections = new ArrayBlockingQueue<Connection>(POOL_SIZE);
		usedConnections = new ArrayDeque<Connection>();
		
		try {
			driver = new com.mysql.cj.jdbc.Driver();
			java.sql.DriverManager.registerDriver(driver);
			LOG.debug("driver registred...\n");
		} catch (SQLException e) {
			LOG.debug("SQL exception: ", e);
		}
		
		for (int i = 0; i < POOL_SIZE; i++) {
			Connection connection = null;
			try {
				// TODO: implement connection wrapper !!!
				connection = java.sql.DriverManager.getConnection(
						prop.getProperty(DB_URL),
						prop.getProperty(DB_USER),
						prop.getProperty(DB_PASSWORD));
				LOG.debug("connection created...\n");
				availibleConnections.offer(connection);
			} catch (SQLException e) {
				LOG.debug("SQL exception: ", e);
			}
		}
	}
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = availibleConnections.take();
			usedConnections.offer(connection);
			LOG.info(String.format("connection given away: availible - %d, used - %d%n",
					availibleConnections.size(), 
					usedConnections.size()));
		} catch (InterruptedException e) {
			LOG.error("Interrupted exception: ", e);
			Thread.currentThread().interrupt();
		}
		return connection;
	}
	
	public void releaseConnection(Connection connection) {
		usedConnections.remove(connection);
		try {
			availibleConnections.put(connection);
			LOG.info(String.format("connection released: availible - %d, used - %d%n",
					availibleConnections.size(), 
					usedConnections.size()));
		} catch (InterruptedException e) {
			LOG.error("Interrupted exception: ", e);
			Thread.currentThread().interrupt();
		}
	}
	
	public void getFaculties() {
		Connection connection = getConnection();
		Statement st = null;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM `faculties`");
			LOG.info("----+----------------------------------------------------+\n");
			LOG.info(" id |                       faculty                      |\n");
			LOG.info("----+----------------------------------------------------+\n");
			while (rs.next()) {
				int id = rs.getInt(1);
				String faculty = rs.getString(2);
				LOG.info(String.format("%d | %-50s |%n", id, faculty));
			}
			LOG.info("----+----------------------------------------------------+\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				releaseConnection(connection);
			}
		}
	}
	
	public void closeConnections() {
		for (Connection connection : availibleConnections) {
			if (connection != null) {
				try {
					connection.close();
					LOG.debug("connection closed...\n");
				} catch (SQLException e) {
					LOG.debug("SQL exception: ", e);
				}
			}
		}
		try {
			java.sql.DriverManager.deregisterDriver(driver);
			LOG.debug("driver deregistred...\n");
		} catch (SQLException e) {
			LOG.debug("SQL exception: ", e);
		}
	}
}
