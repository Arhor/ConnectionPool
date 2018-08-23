package by.epam.connection.runner;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.pool.ConnectionPool;

public class Runner {
	
	private static final Logger LOG = LogManager.getLogger(Runner.class);

	public static void main(String[] args) {
		ConnectionPool cp = ConnectionPool.SINGLTONE;
//		cp.getFaculties();
		
		
		for (int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					Connection connection = cp.getConnection();
					try {
						Thread.sleep((int)(Math.random() * 1500 + 1500));
						cp.releaseConnection(connection);
					} catch (InterruptedException e) {
						LOG.error("Interrupted exception: ", e);
						Thread.currentThread().interrupt();
					}
				}
			}.start();
		}
		
		cp.closeConnections();
	}
}
