package br.com.guilchaves.dscatalog.dto;

import br.com.guilchaves.dscatalog.services.validation.UserUpdateValid;

import java.io.Serial;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
    @Serial
    private static final long serialVersionUID = 1L;
}
