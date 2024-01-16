package br.com.guilchaves.dscatalog.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public class CustomErrorDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public CustomErrorDTO(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
