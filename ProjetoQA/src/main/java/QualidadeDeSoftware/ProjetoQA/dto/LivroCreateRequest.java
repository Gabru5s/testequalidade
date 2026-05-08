package qualidadedesoftware.projetoqa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroCreateRequest(
        @NotBlank String titulo,
        @NotBlank String autor,
        @NotBlank String isbn,
        @NotNull Integer anoPublicacao
) {}