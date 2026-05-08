package qualidadedesoftware.projetoqa.service;



import java.util.List;

import qualidadedesoftware.projetoqa.dto.LivroCreateRequest;
import qualidadedesoftware.projetoqa.dto.LivroResponse;
import qualidadedesoftware.projetoqa.dto.LivroUpdateRequest;

public interface LivroService {
    LivroResponse criar(String usuarioId, LivroCreateRequest request);
    LivroResponse atualizar(String usuarioId, String livroId, LivroUpdateRequest request);
    LivroResponse buscarPorId(String usuarioId, String livroId);
    List<LivroResponse> listarPorUsuario(String usuarioId);
    void remover(String usuarioId, String livroId);
}