package ru.otus.testframework;

public class TestResult {

    private final boolean success;
    private final String error_msg;

    private static final int BORDER = 30;

    TestResult(boolean success) throws IllegalArgumentException {
        if (!success) {
            throw new IllegalArgumentException("Failed TestResult must have error message");
        }
        this.success = success;
        this.error_msg = "";
    }

    TestResult(boolean success, String error_msg) {
        this.success = success;
        this.error_msg = error_msg;
    }

    TestResult(boolean success, Throwable error) {
        this.success = success;
        this.error_msg = getRootCause(error).toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFail(){
        return !success;
    }

    public String getError_msg() {
        return error_msg;
    }

    public String toString() {
        if (success) {
            return "ok";
        }
        return "fail, " + error_msg;
    }

    private static Throwable getRootCause(Throwable exc) {
        Throwable cause;
        Throwable result = exc;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
