package com.milan.videogamestore.model.dto;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewForm {
    @NotBlank
    @Size(min = 3, max = 2000)
    private String comment;

    @Min(1)
    @Max(5)
    private Integer rating;
}
