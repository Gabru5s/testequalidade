package QualidadeDeSoftware.ProjetoQA;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import QualidadeDeSoftware.ProjetoQA.model.Livro;
import QualidadeDeSoftware.ProjetoQA.repository.LivroRepository;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    private LivroRepository repository;

    @Test
    void deveSalvarLivro() {

        Livro livro = new Livro();
        livro.setTitulo("Clean Code");
        livro.setAutor("Robert Martin");

        Livro salvo = repository.save(livro);

        assertNotNull(salvo.getId());
        assertEquals("Clean Code", salvo.getTitulo());
    }

    @Test
    void deveBuscarLivroPorId() {

        Livro livro = new Livro();
        livro.setTitulo("DDD");
        livro.setAutor("Evans");

        Livro salvo = repository.save(livro);

        Optional<Livro> encontrado = repository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("DDD", encontrado.get().getTitulo());
    }

    @Test
    void deveListarLivros() {

        Livro livro1 = new Livro();
        livro1.setTitulo("Livro A");

        Livro livro2 = new Livro();
        livro2.setTitulo("Livro B");

        repository.save(livro1);
        repository.save(livro2);

        assertTrue(repository.findAll().size() >= 2);
    }

    @Test
    void deveExcluirLivro() {

        Livro livro = new Livro();
        livro.setTitulo("Temp");

        Livro salvo = repository.save(livro);

        repository.deleteById(salvo.getId());

        assertFalse(repository.findById(salvo.getId()).isPresent());
    }
}
