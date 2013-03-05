package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import exception.ReserveException;

public class Reserve {

	private String hour;
	private String date;
	
		private final String HOUR_NULL = "A hour esta nula.";
		private final String HOUR_INVALID = "A hour eh invalida.";
		private final String HOUR_BLANK = "A hour esta em branco.";
		private final String HOUR_PATTERN = "^[012]?[\\d]:[0-5][\\d]$";
		private final String DATE_NULA = "A date esta nula.";
		private final String DATE_INVALID = "A date eh invalida.";
		private final String DATE_BLANK = "A date esta em branco.";
		private final String DATE_PATTERN = "^[0123]?[\\d]([./-])[01]?[\\d]\\1[\\d]{2,4}$";
	
	public Reserve(String date, String hour) throws ReserveException {
		this.setDate(date);
		this.setHour(hour);
	}

	public String getHour() {
		return this.hour;
	}

	public String getDate() {
		return this.date;
	}

	public void setHour(String hour) throws ReserveException {
		if(hour == null){
			throw new ReserveException(HOUR_NULL);
		}
		else{
			//nothing here
		}
		
		hour = hour.trim();
		if(hour.equals(""))
			throw new ReserveException(HOUR_BLANK);
		else if(hour.matches(HOUR_PATTERN)){
			if(hour.length() == 4)
				this.hour = "0" + hour;
			else
				this.hour = hour;
		}
		else{
			throw new ReserveException(HOUR_INVALID);
			}
}

	public void setDate(String date) throws ReserveException {
		if(date == null){
			throw new ReserveException(DATE_NULA);
		}
		else{
			//nothing here
		}
		date = date.trim();
		if(date.equals(""))
			throw new ReserveException(DATE_BLANK);
		else if(date.matches(DATE_PATTERN)){
			this.date = padronizeDate(date);
		}
		else
			throw new ReserveException(DATE_INVALID);
		
	}

	public boolean equals(Reserve reserve) {
		return (this.hour.equals(reserve.getHour()) &&
			this.date.equals(reserve.getDate()));
	}
	
	@Override
	public String toString() {
		return "\nHora=" + this.hour 
			+ "\nData=" + this.date;
	}
	
	private static String padronizeDate(String date){
		String actualDate[] = ActualDate().split("[./-]");
		String fractions[] = date.split("[./-]");
		String dateFormat = "";
		
		for(int i = 0; i < 3; i++){
			if(i == 0)
				dateFormat += actualDate[i].substring(0, 
						actualDate[i].length() - fractions[i].length()) + fractions[i];
			else
				dateFormat +=  "/" + actualDate[i].substring(0, 
						actualDate[i].length() - fractions[i].length()) + fractions[i];
				
		}
		
		return dateFormat;
	}
	
	private static String ActualDate(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		return formater.format(date);
	}
}
