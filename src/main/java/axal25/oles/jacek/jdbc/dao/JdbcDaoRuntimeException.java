package axal25.oles.jacek.jdbc.dao;

public class JdbcDaoRuntimeException extends RuntimeException {

    JdbcDaoRuntimeException(String message) {
        super(message);
    }

    JdbcDaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
