package com.dili.uap.domain.dto;

import com.dili.uap.domain.User;

public interface UserDto extends User{
    String getOldPassword();

    void setOldPassword(String oldPassword);
    
    String getNewPassword();

    void setNewPassword(String newPassword);
    
    String getConfirmPassword();

    void setConfirmPassword(String confirmPassword);
}
