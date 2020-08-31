package Exceptions;

public class InvalidArgument extends Exception {
    private String name;

    public InvalidArgument(String name) {
        this.name = name;
    }

    @Override
    public void printStackTrace() {
        System.out.println("Error in config file! Wrong input: " + name);
    }
}
