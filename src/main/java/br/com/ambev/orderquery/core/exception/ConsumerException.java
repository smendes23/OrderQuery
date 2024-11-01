package br.com.ambev.orderquery.core.exception;

public class ConsumerException extends RuntimeException{
    public ConsumerException(String mensagem){
        super("Erro ao consumir mensagem! Causa: "+mensagem);
    }
}
