package clueGame;

public class BadConfigFormatException extends Throwable {

	/**
	 * Eclipse complained about the Exception not having a UID, so here's a default
	 */
	private static final long serialVersionUID = 1L;

	public BadConfigFormatException() {
		System.out.println("Bad file config, fix files and try again");
	}
	
	public BadConfigFormatException(String exceptionType) {
		System.out.println("Bad config format exception: " + exceptionType);
	}

}
