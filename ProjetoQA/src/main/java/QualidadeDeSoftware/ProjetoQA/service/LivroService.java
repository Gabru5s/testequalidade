package QualidadeDeSoftware.ProjetoQA.service;

import QualidadeDeSoftware.ProjetoQA.dto.LivroCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.LivroResponse;
import QualidadeDeSoftware.ProjetoQA.dto.LivroUpdateRequest;

import java.util.List;

public interface LivroService {
    LivroResponse criar(String usuarioId, LivroCreateRequest request);
    LivroResponse atualizar(String usuarioId, String livroId, LivroUpdateRequest request);
    LivroResponse buscarPorId(String usuarioId, String livroId);
    List<LivroResponse> listarPorUsuario(String usuarioId);
    void remover(String usuarioId, String livroId);
}