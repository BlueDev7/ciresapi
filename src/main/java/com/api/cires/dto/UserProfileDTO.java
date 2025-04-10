package com.api.cires.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String city;
    private String country;
    private String avatar;
    private String company;
    private String jobPosition;
}
