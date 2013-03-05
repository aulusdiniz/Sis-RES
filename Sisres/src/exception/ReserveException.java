package exception;

@SuppressWarnings("serial")
public class ReserveException extends Exception{
	
	public ReserveException() {
		super();
	}
	
	public ReserveException(String message) {
		super(message);
	}
}
