package br.com.guilchaves.dscatalog.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO extends CustomErrorDTO {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationErrorDTO(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addErrros(String fieldName, String message) {
        errors.removeIf(e -> e.getFieldName().equals(fieldName));
        errors.add(new FieldMessage(fieldName, message));
    }

}
