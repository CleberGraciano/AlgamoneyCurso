package com.algaworks.algamoney.api.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//ResponseEntityExceptionHandler captura exceções de respostas das entidades
@ControllerAdvice //Adiocionar essa anotação pois essa classe precisa observar toda a aplicação
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    //Metodo para personalizar mensagem de Erro quando mandar valor invalido
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.getCause().toString();
        // Passando o metodo em uma lista
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    //Metodo usado quando um atributo não está valido, seja ele nulo ou não é valido


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);

    }

    //Criando uma lista de erros para retornar
    //Em BindingResult, é que possui a lista de todos os erros
    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        //getFieldErrors() para percorrer todos os erros que aconteceram nos campos de uma entidade
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemDoUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()); // Mensagem Padrão no idioma Local
            String mensagemDoDesenvolvedor = fieldError.toString(); // Mensagem Tecnica
            erros.add(new Erro(mensagemDoUsuario, mensagemDoDesenvolvedor));
        }
        return erros;
    }

    //Classe apenas para guaradr as mesnagens
    public static class Erro {
        private String menagemUsuario;
        private String mensagemDesenvolvedor;

        public Erro(String menagemUsuario, String mensagemDesenvolvedor) {
            this.menagemUsuario = menagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }

        public String getMenagemUsuario() {
            return menagemUsuario;
        }

        public String getMensagemDesenvolvedor() {
            return mensagemDesenvolvedor;
        }
    }

}
