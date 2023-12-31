package ru.practicum.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends NoSuchElementException {
    public EventNotFoundException(Long id) {
        super("Событие с Id " + id + " не найдено.");
    }
}
