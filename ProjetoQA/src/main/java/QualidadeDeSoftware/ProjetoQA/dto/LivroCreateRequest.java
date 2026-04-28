package QualidadeDeSoftware.ProjetoQA.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroCreateRequest(
        @NotBlank String titulo,
        @NotBlank String autor,
        @NotBlank String isbn,
        @NotNull Integer anoPublicacao,
        @Size(max = 50) String genero,
        String resumo
) {}