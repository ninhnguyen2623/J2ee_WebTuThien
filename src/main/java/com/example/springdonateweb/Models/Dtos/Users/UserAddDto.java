package com.example.springdonateweb.Models.Dtos.Users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddDto implements Serializable {
    
    private int id;
    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải trên 6 kí tự")
    private String password;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10,11}", message = "Số điện thoại chỉ được chứa 10 hoặc 11 ký tự và chỉ được là ký tự số")
    private String phoneNumber;
    private int roleId;


}
