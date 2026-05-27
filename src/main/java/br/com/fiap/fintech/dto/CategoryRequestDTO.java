package br.com.fiap.fintech.dto;

import br.com.fiap.fintech.values.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private CategoryType type;

    public CategoryRequestDTO() {}

    public CategoryRequestDTO(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
