package by.epam.connection.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Properties;

import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.jdbc.Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum ConnectionPool {
	
	POOL;
	
	private final Logger LOG = LogManager.getLogger(ConnectionPool.class);
	
	public static final String DB_URL = "db.url";
	public static final String DB_USER = "db.user";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_POOLSIZE = "db.poolsize";
	
	public final int POOL_SIZE;
	
	private Properties prop;
	private Driver driver;
	
	LinkedBlockingQueue<ProxyConnection> availibleConnections;
	ArrayDeque<ProxyConnection> usedConnections;
	
	ConnectionPool() {
		prop = PropertiesHandler.readProperties();
		POOL_SIZE = Integer.parseInt(prop.getProperty(DB_POOLSIZE));
		availibleConnections = new LinkedBlockingQueue<ProxyConnection>();
		usedConnections = new ArrayDeque<ProxyConnection>();
		try {
			driver = new Driver();
			DriverManager.registerDriver(driver);
			LOG.info("driver " + driver + " registred...\n");
		} catch (SQLException e) {
			LOG.error("SQL exception: ", e);
		}
		
		for (int i = 0; i < POOL_SIZE; i++) {
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(
						prop.getProperty(DB_URL),
						prop.getProperty(DB_USER),
						prop.getProperty(DB_PASSWORD));
				ProxyConnection proxyConn = new ProxyConnection(connection);
				LOG.info("connection created...\n");
				availibleConnections.offer(proxyConn);
			} catch (SQLException e) {
				LOG.error("SQL exception: ", e);
			}
		}
	}
	
	public ProxyConnection getConnection() {
		ProxyConnection proxyConnection = null;
		try {
			proxyConnection = availibleConnections.take();
			usedConnections.offer(proxyConnection);
			LOG.info(String.format("connection given away: availible - %d, used - %d%n",
					availibleConnections.size(), 
					usedConnections.size()));
		} catch (InterruptedException e) {
			LOG.error("Interrupted exception: ", e);
			Thread.currentThread().interrupt();
		}
		return proxyConnection;
	}
	
	public void releaseConnection(ProxyConnection proxyConnection) {
		usedConnections.remove(proxyConnection);
		try {
			availibleConnections.put(proxyConnection);
			LOG.info(String.format(
					"connection released: availible - %d, used - %d%n",
					availibleConnections.size(), 
					usedConnections.size()));
		} catch (InterruptedException e) {
			LOG.error("Interrupted exception: ", e);
			Thread.currentThread().interrupt();
		}
	}
	
	public void closeConnections() {
		for (int i = 0; i < POOL_SIZE; i++) {
			try {
				availibleConnections.take().close();
				LOG.info("connection closed...left: " 
				+ (availibleConnections.size() 
				+ usedConnections.size()) +"\n");
			} catch (SQLException e) {
				LOG.error("SQL exception: ", e);
			} catch (InterruptedException e) {
				LOG.error("Interrupted exception:", e);
				Thread.currentThread().interrupt();
			}
		}
		LOG.info("\nall connections closed...left: " 
				+ (availibleConnections.size() 
				+ usedConnections.size()) +"\n");
		
		deregisterDriver();
	}
	
	private void deregisterDriver() {
		Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			try {
				java.sql.Driver driver = drivers.nextElement();
				DriverManager.deregisterDriver(driver);
				LOG.info("driver " + driver + " deregistred...\n");
			} catch (SQLException e) {
				LOG.error("Drivers have not been registered", e);
			}
		}
	}
}
