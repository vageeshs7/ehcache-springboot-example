package org.vag.sag.terracottademo.exceptions;

public class PersonNotFoundException extends Exception{
    public PersonNotFoundException() {
    }

    public PersonNotFoundException(String s) {
        super(s);
    }

    public PersonNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PersonNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public PersonNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
