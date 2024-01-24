package com.DL.data;

public class DataBaseException extends Exception {
    public DataBaseException() {
        super("Erreur concernant la base de données");
    }

    public DataBaseException(Throwable ex) {
        super(ex);
    }
}