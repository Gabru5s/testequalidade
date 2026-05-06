package QualidadeDeSoftware.ProjetoQA.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroUpdateRequest(
        @NotBlank String titulo,
        @NotBlank String autor,
        @NotBlank String isbn,
        @NotNull Integer anoPublicacao
) {}