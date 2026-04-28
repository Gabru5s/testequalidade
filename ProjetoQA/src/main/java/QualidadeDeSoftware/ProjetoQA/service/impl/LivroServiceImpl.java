package QualidadeDeSoftware.ProjetoQA.service.impl;

import QualidadeDeSoftware.ProjetoQA.dto.LivroCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.LivroResponse;
import QualidadeDeSoftware.ProjetoQA.dto.LivroUpdateRequest;
import QualidadeDeSoftware.ProjetoQA.exception.LivroNaoEncontradoException;
import QualidadeDeSoftware.ProjetoQA.exception.RegraNegocioException;
import QualidadeDeSoftware.ProjetoQA.model.Livro;
import QualidadeDeSoftware.ProjetoQA.repository.LivroRepository;
import QualidadeDeSoftware.ProjetoQA.service.LivroService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public LivroResponse criar(String usuarioId, LivroCreateRequest request) {
        if (livroRepository.existsByIsbnAndUsuarioId(request.isbn(), usuarioId)) {
            throw new RegraNegocioException("Já existe um livro com este ISBN para este usuário.");
        }

        Livro livro = new Livro(
                null,
                request.titulo(),
                request.autor(),
                request.isbn(),
                request.anoPublicacao(),
                request.genero(),
                request.resumo(),
                false,
                usuarioId
        );

        return toResponse(livroRepository.save(livro));
    }

    @Override
    public LivroResponse atualizar(String usuarioId, String livroId, LivroUpdateRequest request) {
        Livro livro = livroRepository.findByIdAndUsuarioId(livroId, usuarioId)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado."));

        livro.setTitulo(request.titulo());
        livro.setAutor(request.autor());
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setGenero(request.genero());
        livro.setResumo(request.resumo());
        if (request.lido() != null) {
            livro.setLido(request.lido());
        }

        return toResponse(livroRepository.save(livro));
    }

    @Override
    public LivroResponse buscarPorId(String usuarioId, String livroId) {
        Livro livro = livroRepository.findByIdAndUsuarioId(livroId, usuarioId)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado."));
        return toResponse(livro);
    }

    @Override
    public List<LivroResponse> listarPorUsuario(String usuarioId) {
        return livroRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void remover(String usuarioId, String livroId) {
        Livro livro = livroRepository.findByIdAndUsuarioId(livroId, usuarioId)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado."));
        livroRepository.delete(livro);
    }

    private LivroResponse toResponse(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getGenero(),
                livro.getResumo(),
                livro.getLido()
        );
    }
}