package cn.ideal.wfpf.exception;

public class WfNotFoundException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8306289236934519617L;

	public WfNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public WfNotFoundException(String message){
		super(message);
	}

}
