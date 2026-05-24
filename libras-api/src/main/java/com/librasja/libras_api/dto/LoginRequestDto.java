package com.librasja.libras_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {

    @NotBlank(message = "Nome de login é obrigatório (use o nome cadastrado)")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
