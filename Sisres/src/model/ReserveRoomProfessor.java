package model;

import exception.ReserveException;

public class ReserveRoomProfessor extends ReserveRoom{

	private Professor professor;
	
	//Mensagens
		private final String PROFESSOR_NULO = "O professor esta nulo.";
	
	public ReserveRoomProfessor(String data, String hora, Room room,
			String finalidade, Professor professor) throws ReserveException {
		super(data, hora, room, finalidade);
		this.setProfessor(professor);
	}
	
	public Professor getProfessor() {
		return this.professor;
	}

	public void setProfessor(Professor professor) throws ReserveException {
		if(professor == null)
			throw new ReserveException(PROFESSOR_NULO);
		this.professor = professor;
	}

	public boolean equals(ReserveRoomProfessor obj) {
		return (super.equals(obj) &&
				this.getProfessor().equals(obj.getProfessor())
				);
	}

	@Override
	public String toString() {
		return "ReserveRoomProfessor [professor=" + this.getProfessor().toString() + ", toString()="
				+ super.toString() + "]";
	}

}
