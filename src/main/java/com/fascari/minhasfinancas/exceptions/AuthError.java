package com.fascari.minhasfinancas.exceptions;

public class AuthError extends RuntimeException {
    public AuthError(String mensagem) {
        super(mensagem);
    }
}