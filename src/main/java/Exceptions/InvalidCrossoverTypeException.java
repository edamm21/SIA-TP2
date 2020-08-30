package Exceptions;

public class InvalidCrossoverTypeException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.println("Invalid crossover type! Check config file.");
    }
}
