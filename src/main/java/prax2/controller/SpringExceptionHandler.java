package prax2.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prax2.orderpojo.ErrorsJson;
import prax2.orderpojo.Mistake;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class SpringExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsJson handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception) {
        ErrorsJson json = new ErrorsJson();
        List<Mistake> mistakes = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(e -> mistakes.add(
                new Mistake(e.getCodes()[0],
                        Stream.of(e.getArguments()).filter(arg -> !(arg instanceof DefaultMessageSourceResolvable))
                                .map(String::valueOf).collect(Collectors.toList()))));
        json.setErrors(mistakes);
        json.getErrors().forEach(System.out::println);
        return json;
    }
}
