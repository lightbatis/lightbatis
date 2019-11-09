/**
 * 
 */
package titan.lightbatis.exception;

/**
 * @author lifei114@126.com
 *
 */
public class LightbatisException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -468078635860702165L;

	/**
	 * 
	 */
	public LightbatisException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public LightbatisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LightbatisException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public LightbatisException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public LightbatisException(Throwable cause) {
		super(cause);
	}

}
