package br.com.guilchaves.dscatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

    @NotBlank(message = "Required field")
    @Email(message = "Invalid email")
    private String email;

	public EmailDTO(){}

	public EmailDTO(String email){
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
