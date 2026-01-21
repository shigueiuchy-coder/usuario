package com.javanatal.usuario.infrastructure.exception;

public class ResorceNotFoundException  extends RuntimeException{

    public ResorceNotFoundException(String mensagem){
        super (mensagem);
    }
    public ResorceNotFoundException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
