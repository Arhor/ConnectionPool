package by.epam.connection.model;

public class Faculty extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5662696934735451425L;
	
	private String nameRu;
	private String nameEn;
	private short seatsTotal;
	private short seatsBudget;

	public Faculty(int id) {
		super(id);
	}

	public String getNameRu() {
		return nameRu;
	}

	public void setNameRu(String nameRu) {
		this.nameRu = nameRu;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public short getSeatsTotal() {
		return seatsTotal;
	}

	public void setSeatsTotal(short seatsTotal) {
		this.seatsTotal = seatsTotal;
	}

	public short getSeatsBudget() {
		return seatsBudget;
	}

	public void setSeatsBudget(short seatsBudget) {
		this.seatsBudget = seatsBudget;
	}

}
