package test.test;

public class RecognizerExeption extends Exception {

    public RecognizerExeption() {
        super();
    }
    
    public RecognizerExeption(String message) {
        super(message);
    }
    
    public RecognizerExeption(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RecognizerExeption(Throwable cause) {
        super(cause);
    }

}
