package by.epam.connection.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.model.ConnectionPool;

public class Runner {
	
	private static final Logger LOG = LogManager.getLogger(Runner.class);

	public static void main(String[] args) {
		ConnectionPool cp = new ConnectionPool();
		cp.initialize();
	}
}
