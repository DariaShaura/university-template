package com.epam.rd.izh.exception;

import lombok.Getter;

@Getter
public class IncorrectDataException extends Exception {
    private Object incorrectObject;

    public IncorrectDataException(String message, Object incorrectObject) {
        super(message);

        this.incorrectObject = incorrectObject;
    }
}
