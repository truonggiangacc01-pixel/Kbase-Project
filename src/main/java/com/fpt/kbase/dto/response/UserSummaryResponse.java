package com.fpt.kbase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String fullName;
    private String email;
}
