package br.com.nextlog.exception;

public class AuthException extends NegocioException {
    public AuthException(String mensagem) { super(mensagem); }
}