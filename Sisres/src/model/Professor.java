package model;

import exception.ClientException;

public class Professor extends Client {
	
	private final String REGISTRATION_BLANK = "Matricula em Branco.";
	private final String REGISTRATION_NULL = "Matricula esta Nula.";
		
	
	public Professor(String name, String cpf, String registration,
			String phone, String email) throws ClientException {
		super(name, cpf, registration, phone, email);
	}

	public void setRegistration(String registration) throws ClientException {
		if(registration == null) {
			throw new ClientException(REGISTRATION_NULL);
		}
		else
			if("".equals(registration.trim())) {
				throw new ClientException(REGISTRATION_BLANK);
			}
		super.registration = registration;
	}
	
}
