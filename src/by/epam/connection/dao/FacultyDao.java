package by.epam.connection.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import by.epam.connection.model.Faculty;
import by.epam.connection.pool.ConnectionPool;
import by.epam.connection.pool.ProxyConnection;

public class FacultyDao extends AbstractDao<Integer, Faculty> {

	@Override
	public List<Faculty> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Faculty findEntityById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Faculty entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean create(Faculty entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Faculty update(Faculty entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showAll() {
		ProxyConnection proxyConnection = ConnectionPool.POOL.getConnection();
		Statement st = null;
		try {
			st = proxyConnection.createStatement();
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
			LOG.error("SQL exception: ", e);
		} finally {
			if (proxyConnection != null) {
				ConnectionPool.POOL.releaseConnection(proxyConnection);
			}
		}
	}

}
