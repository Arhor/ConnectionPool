package by.epam.connection.model;

public class User extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8340115208595108089L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Role role;
	

	public User(int id) {
		super(id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public String getPassword() {
//		return password;
//	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public enum Role {
		ADMIN, USER;
	}

}
