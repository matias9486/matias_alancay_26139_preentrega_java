package com.techlab.articulo.excepciones;

public class PropiedadInvalida extends RuntimeException {
    public PropiedadInvalida(String message) {
        super(message);
    }
}
