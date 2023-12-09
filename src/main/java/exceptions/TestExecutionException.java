package exceptions;

public class TestExecutionException extends RuntimeException {
    public TestExecutionException(String message, Exception e) {
        super(message, e);
    }

    public TestExecutionException(Exception e) {
        super(e);
    }
}
