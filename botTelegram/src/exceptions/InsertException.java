package exceptions;

public class InsertException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getMessage()
	{
		return "Não foi possivel inserir";
	}

}
