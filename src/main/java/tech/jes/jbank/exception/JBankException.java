package tech.jes.jbank.exception;

public abstract class JBankException extends RuntimeException {

    public JBankException(String message) {
        super(message);
    }

    public JBankException(Throwable cause) {
        super(cause);
    }
}
