package qualidadedesoftware.projetoqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import qualidadedesoftware.projetoqa.model.Livro;

public interface LivroRepository extends MongoRepository<Livro, String> {
    List<Livro> findByUsuarioId(String usuarioId);
    Optional<Livro> findByIdAndUsuarioId(String id, String usuarioId);
    boolean existsByIsbnAndUsuarioId(String isbn, String usuarioId);
}