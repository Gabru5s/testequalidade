package QualidadeDeSoftware.ProjetoQA.dto;

public record LivroResponse(
        String id,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        String genero,
        String resumo,
        Boolean lido
) {}