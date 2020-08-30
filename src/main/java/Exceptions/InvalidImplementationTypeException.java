package Exceptions;

public class InvalidImplementationTypeException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid implementation type! Check config file.");
    }
}
