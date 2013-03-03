package model;

import exception.ClientException;


/*Para fazer uma melhor validacoa e captura do dados
 * se pega todos os dados como string.
 * 
 * 
 */
public abstract class Client {
	
	private String name;
	private String cpf;
	private String phone;
	private String email;
	protected String registration;
		
	private final String NAME_INVALID = "Nome Invalido.";
	private final String NAME_BLANK = "Nome em Branco.";
	private final String NAME_NULL = "Nome esta Nulo.";
	private final String CPF_INVALID = "CPF Invalido.";
	private final String CPF_BLANK = "CPF em Branco.";
	private final String CPF_NULL = "CPF esta Nulo.";
	private final String PHONE_INVALID = "Telefone Invalido.";
	private final String PHONE_NULL = "Telefone esta Nulo.";
	private final String EMAIL_NULL = "E-mail esta Nulo.";
	
	
	public Client(String name, String cpf, String registration, String phone, 
			String email) throws ClientException{
		this.setName(name);
		this.setCpf(cpf);
        this.setRegistration(registration);
		this.setPhone(phone);
		this.setEmail(email);
	}

	public String getName() {
		return name;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}
	
	public String getRegistration() {
		return registration;
	}
	
	public void setName(String name) throws ClientException{
		if(name == null) {
			throw new ClientException(NAME_NULL);
		}
		else
			if("".equals(name.trim())) {
				throw new ClientException(NAME_BLANK);
			}
		else
			if(name.trim().matches("[a-zA-Z][a-zA-Z\\s]+")) {
				this.name = name.trim();
			}
		else
			throw new ClientException(NAME_INVALID);
	}
	
	public void setCpf(String cpf) throws ClientException {
		if(cpf == null) {
			throw new ClientException(CPF_NULL);
		}
		else 
			if("".equals(cpf)) {
				throw new ClientException(CPF_BLANK);
			}
		else 
			if(cpf.matches("[\\d]{3,3}.[\\d]{3,3}.[\\d]{3,3}-[\\d]{2,2}$")) {
				if(this.validateCpf(cpf.split("[\\. | -]")[0] + cpf.split("[\\. | -]")[1] + 
						cpf.split("[\\. | -]")[2] + cpf.split("[\\. | -]")[3])) {
					this.cpf = cpf;
				}
				else
					throw new ClientException(CPF_INVALID);
				}
				else
					throw new ClientException(CPF_INVALID);
	}
	
	public void setPhone(String phone) throws ClientException {
		if(phone == null) {
			throw new ClientException(PHONE_NULL);
		}
		else
			if("".equals(phone)) {
				this.phone = phone;
			}
			else
				if(phone.matches("(\\([ ]*[\\d]{2,3}[ ]*\\))?[ ]*[\\d]{4,4}[ ]*-?[ ]*[\\d]{4,4}[ ]*$")) {
					this.phone = phone.replaceAll(" ", "");
				}
				else
					throw new ClientException(PHONE_INVALID);
	}
	
	public void setEmail(String email) throws ClientException {
		if(email == null) {
			throw new ClientException(EMAIL_NULL);
		}
		else
			this.email = email;
	}
	
	public abstract void setRegistration(String registration) throws ClientException;
	
	@Override
	public String toString() {
		return "Nome: " + name + "\nCpf: " + cpf + "\nTelefone: " + phone +
				"\nEmail: " + email + "\nMatricula: " + registration;
	}

	public boolean equals(Client target) {
		
		if(this.getName().equals(target.getName()) && this.getCpf().equals(target.getCpf()) &&
				this.getRegistration().equals(target.getRegistration()) && this.getPhone().equals(target.getPhone()) &&
				this.getEmail().equals(target.getEmail())) {
			return true;
		}
		return false;
	}
	
	private boolean validateCpf(String cpf) {

		int d1, d2; //variáveis responsáveis por guardar o valor da validação.
		int digit1, digit2, rest;
		int digitCPF;
		String	nDigResult;

		d1 = d2 = 0;
		digit1 = digit2 = rest = 0;

		for (int nCount = 1; nCount < cpf.length()-1; nCount++) {
			 digitCPF = Integer.valueOf (cpf.substring(nCount-1, nCount)).intValue();
			 d1 = d1 + ( 11 - nCount ) * digitCPF;
			 d2 = d2 + ( 12 - nCount ) * digitCPF;
		};

		rest = (d1 % 11);
		
		if (rest < 2) {
			 digit1 = 0;
		}
		else {
			 digit1 = 11 - rest;
		}
		
		d2 += 2 * digit1;
		rest = (d2 % 11);
		
		if (rest < 2) {
			 digit2 = 0;
		}
		else {
			 digit2 = 11 - rest;
		}
		
		String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());
		nDigResult = String.valueOf(digit1) + String.valueOf(digit2);
		
		return nDigVerific.equals(nDigResult);
	}
}
