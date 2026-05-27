package br.com.fiap.fintech.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountRequestDTO {

    @NotBlank(message = "O nome da conta é obrigatório")
    private String name;

    public AccountRequestDTO() {}

    public AccountRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
