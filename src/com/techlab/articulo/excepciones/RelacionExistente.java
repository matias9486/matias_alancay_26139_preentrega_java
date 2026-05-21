package com.techlab.articulo.excepciones;

public class RelacionExistente extends RuntimeException {
    public RelacionExistente(String message) {
        super(message);
    }
}
