package com.fpt.kbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {
    
    @NotBlank(message = "Tên dự án không được để trống")
    private String name;
    
    private String description;
}
