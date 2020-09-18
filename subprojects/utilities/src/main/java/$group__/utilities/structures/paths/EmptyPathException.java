package $group__.utilities.structures.paths;

import java.util.Collection;

public class EmptyPathException
		extends RuntimeException {
	private static final long serialVersionUID = 760193064178512586L;

	/**
	 * Constructs a new runtime exception with {@code null} as its
	 * detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 *
	 * @since 0.0.1
	 */
	public EmptyPathException() {
		super();
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 *
	 * @since 0.0.1
	 */
	public EmptyPathException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and
	 * cause.  <p>Note that the detail message associated with
	 * {@code cause} is <i>not</i> automatically incorporated in
	 * this runtime exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method).  (A <tt>null</tt> value is
	 *                permitted, and indicates that the cause is nonexistent or
	 *                unknown.)
	 *
	 * @since 0.0.1
	 */
	public EmptyPathException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new runtime exception with the specified cause and a
	 * detail message of <tt>(cause==null ? null : cause.toString())</tt>
	 * (which typically contains the class and detail message of
	 * <tt>cause</tt>).  This constructor is useful for runtime exceptions
	 * that are little more than wrappers for other throwables.
	 *
	 * @param cause the cause (which is saved for later retrieval by the
	 *              {@link #getCause()} method).  (A <tt>null</tt> value is
	 *              permitted, and indicates that the cause is nonexistent or
	 *              unknown.)
	 *
	 * @since 0.0.1
	 */
	public EmptyPathException(Throwable cause) {
		super(cause);
	}

	public static void checkPathData(Collection<?> data)
			throws EmptyPathException {
		if (data.size() <= 1)
			throw new EmptyPathException();
	}
}
