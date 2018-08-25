package by.epam.connection.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.connection.model.Entity;

public abstract class AbstractDao<K, T extends Entity> {
	
	protected final Logger LOG = LogManager.getLogger(AbstractDao.class);

	public abstract List<T> findAll();
	
	public abstract T findEntityById(K id);
	
	public abstract boolean delete(K id);
	
	public abstract boolean delete(T entity);
	
	public abstract boolean create(T entity);
	
	public abstract T update(T entity);
	
	// only for test purposes
	public abstract void showAll();
}
