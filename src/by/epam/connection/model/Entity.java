package by.epam.connection.model;

import java.io.Serializable;

public abstract class Entity implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1825429275979947811L;
	
	private int id;
	
	public Entity(int id) {
		this.setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
