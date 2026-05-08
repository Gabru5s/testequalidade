package qualidadedesoftware.projetoqa.dto;

public record LivroResponse(
        String id,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao
) {}