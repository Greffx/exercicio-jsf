package br.com.evento.domain.exception;

import java.io.Serial;
import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = -8934951257848734266L;

    public BusinessException(String message) {
        super(message);
    }
}
