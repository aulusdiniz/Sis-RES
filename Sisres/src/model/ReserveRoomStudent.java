package model;

import exception.ReserveException;

public class ReserveRoomStudent extends ReserveRoom{
	
	private Student student;
	private String reservedChairs;
	
		private final String STUDENT_NULL = "O student esta nulo.";
		private final String CHAIRS_NULL = "O numero de cadeiras esta nulo.";
		private final String CHAIRS_BLANK = "O numero de cadeiras esta em branco.";
		private final String CHAIRS_INVALID = "O numero de cadeira eh invalido.";
		private final String CHAIRS_OVER_LIMIT = "A room nao possui este numero de cadeiras para reservar.";
		private final String CHAIRS_PATTERN = "^[\\d]+$";

	public ReserveRoomStudent(String data, String hora, Room room,
			String finalidade, String reservedChairs, Student student) throws ReserveException {
		super(data, hora, room, finalidade);
		this.setStudent(student);
		this.setReservedChairs(reservedChairs);
	}
	
	public Student getStudent() {
		return this.student;
	}

	public String getReservedChairs() {
		return this.reservedChairs;
	}

	public void setStudent(Student student) throws ReserveException {
		if(student == null)
			throw new ReserveException(STUDENT_NULL);
		this.student = student;
	}

	public void setReservedChairs(String reservedChairs) throws ReserveException {
		String c = reservedChairs;
		if(c == null)
			throw new ReserveException(CHAIRS_NULL);
		c = c.trim();
		if(c.equals(""))
			throw new ReserveException(CHAIRS_BLANK);
		else if(c.matches(CHAIRS_PATTERN)){
			if(Integer.parseInt(super.getRoom().getCapacity()) < Integer.parseInt(reservedChairs))
				throw new ReserveException(CHAIRS_OVER_LIMIT);
			else
				this.reservedChairs = reservedChairs;
		}
		else
			throw new ReserveException(CHAIRS_INVALID);
	}


	public boolean equals(ReserveRoomStudent reserveRoomStudent) {
		return (super.equals(reserveRoomStudent) &&
				this.getStudent().equals(reserveRoomStudent.getStudent()) &&
				this.getReservedChairs().equals(reserveRoomStudent.getReservedChairs())
				);
	}

	@Override
	public String toString() {
		return "student: " + this.getStudent().toString()
			+ "\nCadeiras Reservadas: " + this.getReservedChairs() 
			+ super.toString();
	}

}
