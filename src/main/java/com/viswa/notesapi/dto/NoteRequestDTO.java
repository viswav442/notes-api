package com.viswa.notesapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class NoteRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "title cannot exceed 100 characters")
    private String title;

    @Size(max = 5000, message = "content cannot exceed 5000 characters")
    private String content;
}
