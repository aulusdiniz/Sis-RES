package model;

import exception.PatrimonyException;

public class Room extends Patrimonio {

	private String capacity;
	
		private final String CAPACITY_INVALID = "Capacidade Invalida.";
		private final String CAPACITY_BLANK = "Capacidade em Branco.";
		private final String CAPACITY_NULL = "Capacidade esta nula.";
			
		
	public Room(String code, String description, String capacity) throws PatrimonyException {
		super(code, description);
		this.setCapacity(capacity);
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) throws PatrimonyException {
		if(capacity == null)
			throw new PatrimonyException(CAPACITY_NULL);
		else if("".equals(capacity.trim()))
			throw new PatrimonyException(CAPACITY_BLANK);
		else if(capacity.matches("[\\d]+")){
				this.capacity = capacity;
		}
		else
		{
			throw new PatrimonyException(CAPACITY_INVALID);
		}
	}

	public boolean equals(Room room){
		if( super.equals(room) &&
			this.getCapacity().equals(room.getCapacity())){
			return true;
		}
		
		return false;
	}
}
