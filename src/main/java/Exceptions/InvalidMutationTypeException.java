package Exceptions;

public class InvalidMutationTypeException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid mutation type! Check config file.");
    }
}
