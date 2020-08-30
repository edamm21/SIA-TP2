package Exceptions;

public class InvalidCharacterTypeException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid character type! Check config file.");
    }
}
