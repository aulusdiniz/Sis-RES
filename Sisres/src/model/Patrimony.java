package model;

import exception.PatrimonyException;

public class Patrimony {

	private String code;
	private String description;
	private final String CODE_BLANK = "Codigo em Branco.";
	private final String CODE_NULL = "Codigo esta Nulo.";
	private final String DESCRIPTION_BLANK = "Descricao em Branco.";
	private final String DESCRIPTION_NULL = "Descricao esta Nula.";

	public Patrimony(String code, String description) throws PatrimonyException {
		this.setCode(code);
		this.setDescription(description);
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public void setCode(String code) throws PatrimonyException {
		if(code == null)
			throw new PatrimonyException(CODE_NULL);
		else if ("".equals(code.trim())) 
			throw new PatrimonyException(CODE_BLANK);
		this.code = code;
	}

	public void setDescription(String description) throws PatrimonyException {
		if(description == null)
			throw new PatrimonyException(DESCRIPTION_NULL);
		else if ("".equals(description.trim())) 
			throw new PatrimonyException(DESCRIPTION_BLANK);
		this.description = description;
	}

	public boolean equals(Patrimony e){
		if( this.getCode().equals(e.getCode()) && 
			this.getDescription().equals(e.getDescription()))
			return true;
		else{
			//nothing here
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Codigo=" + code +
			"\nDescricao=" + description;
	}
}
