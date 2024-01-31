package br.com.guilchaves.dscatalog.dto;

import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Required field")
    @Size(min = 8, message = "Must have at least 8 characters")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
