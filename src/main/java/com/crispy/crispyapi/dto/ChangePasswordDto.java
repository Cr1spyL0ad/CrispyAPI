package com.crispy.crispyapi.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String newPassword;
    private String newPasswordConfirm;
}
