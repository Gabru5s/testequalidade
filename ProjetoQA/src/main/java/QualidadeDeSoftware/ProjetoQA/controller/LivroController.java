package QualidadeDeSoftware.ProjetoQA.controller;

import QualidadeDeSoftware.ProjetoQA.dto.LivroCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.LivroResponse;
import QualidadeDeSoftware.ProjetoQA.dto.LivroUpdateRequest;
import QualidadeDeSoftware.ProjetoQA.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public LivroResponse criar(@RequestHeader("X-Usuario-Id") String usuarioId,
                               @Valid @RequestBody LivroCreateRequest request) {
        return livroService.criar(usuarioId, request);
    }

    @GetMapping
    public List<LivroResponse> listar(@RequestHeader("X-Usuario-Id") String usuarioId) {
        return livroService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@RequestHeader("X-Usuario-Id") String usuarioId,
                                @PathVariable String id) {
        return livroService.buscarPorId(usuarioId, id);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(@RequestHeader("X-Usuario-Id") String usuarioId,
                                   @PathVariable String id,
                                   @Valid @RequestBody LivroUpdateRequest request) {
        return livroService.atualizar(usuarioId, id, request);
    }

    @DeleteMapping("/{id}")
    public void remover(@RequestHeader("X-Usuario-Id") String usuarioId,
                        @PathVariable String id) {
        livroService.remover(usuarioId, id);
    }
}