package customUtils.exceptions;

public class ClientException extends Exception {

	private static final long serialVersionUID = -5968458424882621164L;

	public ClientException() {
		super();
	}
	
	public ClientException(String error) {
		super(error);
	}
}
