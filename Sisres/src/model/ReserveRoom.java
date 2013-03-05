package model;

import exception.ReserveException;

public class ReserveRoom extends Reserve{

	private Room room;
	private String finality;
	
		private final String ROOM_NULL = "A room esta nula.";
		private final String FINALITY_NULL = "A finality esta nula.";
		private final String FINALITY_BLANK = "A finality esta em branco.";
				
	
	public ReserveRoom(String date, String hour, Room room, String finality) throws ReserveException {
		super(date, hour);
		this.setRoom(room);
		this.setFinality(finality);
	}

	public Room getRoom() {
		return this.room;
	}

	public String getFinality() {
		return this.finality;
	}

	public void setRoom(Room room) throws ReserveException {
		if(room == null)
			throw new ReserveException(ROOM_NULL);
		this.room = room;
	}

	public void setFinality(String finality) throws ReserveException {
		if(finality == null)
			throw new ReserveException(FINALITY_NULL);
		
		finality = finality.trim();
		if(finality.equals(""))
			throw new ReserveException(FINALITY_BLANK);
		else
			this.finality = finality;
	}

	public boolean equals(ReserveRoom room) {
		return (super.equals(room) && 
			this.getRoom().equals(room.getRoom())&&
			this.getFinality().equals(room.getFinality()));
	}
	
	@Override
	public String toString() {
		return "\n" + this.getRoom().toString() 
			+ "\nFinalidade=" + this.getFinality() 
			+ super.toString();
	}

}
