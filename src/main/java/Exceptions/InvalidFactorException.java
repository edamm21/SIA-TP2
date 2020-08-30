package Exceptions;

public class InvalidFactorException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid stop factor! Check config file.");
    }
}
