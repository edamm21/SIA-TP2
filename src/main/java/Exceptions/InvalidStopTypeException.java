package Exceptions;

public class InvalidStopTypeException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid stop type! Check config file.");
    }
}
