package org.godseop.cherry.core.model;

import lombok.Getter;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.exception.SystemException;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Result {

    private String code;
    private String message;

    private Map<String, Object> data;

    public Result() {
        data = new HashMap<>();
        this.put(Error.OK);
    }

    public Result(Error error) {
        this.put(error);
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public void put(Error error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public <E extends Exception> void put(E exception) {
        if (exception instanceof CherryException) {
            this.code = ((CherryException) exception).getCode();
            this.message = exception.getMessage();
        } else if (exception instanceof SystemException) {
            this.put(Error.SERVICE_UNAVAILABLE);
        } else {
            this.put(Error.INTERNAL_SERVER_ERROR);
        }
    }
}