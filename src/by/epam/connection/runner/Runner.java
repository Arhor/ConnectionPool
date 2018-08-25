package by.epam.connection.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.pool.ConnectionPool;
import by.epam.connection.pool.ProxyConnection;

public class Runner {
	
	private static final Logger LOG = LogManager.getLogger(Runner.class);

	public static void main(String[] args) {
		ConnectionPool cp = ConnectionPool.POOL;		
		
		for (int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					ProxyConnection connection = cp.getConnection();
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
