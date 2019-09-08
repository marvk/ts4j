package net.marvk.ts4j.repl;

public class Error {
    private final String message;
    private final int code;

    public Error(final String message, final int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
