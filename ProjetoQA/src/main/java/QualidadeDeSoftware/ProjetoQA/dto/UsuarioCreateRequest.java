package QualidadeDeSoftware.ProjetoQA.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCreateRequest(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String senha
) {}