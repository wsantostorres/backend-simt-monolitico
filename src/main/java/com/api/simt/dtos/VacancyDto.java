package com.api.simt.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VacancyDto (@NotBlank @Size(max = 100) String title, @NotBlank String closingDate, @NotBlank @Size(max = 500) String description, @NotNull int type, @NotNull int morning, @NotNull int afternoon, @NotNull int night){

}
