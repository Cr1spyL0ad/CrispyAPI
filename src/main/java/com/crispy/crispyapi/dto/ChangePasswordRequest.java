package com.crispy.crispyapi.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String newPassword;
    private String newPasswordConfirm;
}
