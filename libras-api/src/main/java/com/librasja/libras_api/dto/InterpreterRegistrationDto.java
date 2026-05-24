package com.librasja.libras_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterpreterRegistrationDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String password;

    @NotBlank(message = "Especialidades são obrigatórias")
    @Size(max = 255, message = "Especialidades deve ter no máximo 255 caracteres")
    private String especialidades;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricaoCurta;

    @Size(max = 50, message = "Disponibilidade deve ter no máximo 50 caracteres")
    private String disponivel;
}
