package QualidadeDeSoftware.ProjetoQA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import QualidadeDeSoftware.ProjetoQA.dto.LivroCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.LivroResponse;
import QualidadeDeSoftware.ProjetoQA.dto.LivroUpdateRequest;
import QualidadeDeSoftware.ProjetoQA.exception.LivroNaoEncontradoException;
import QualidadeDeSoftware.ProjetoQA.exception.RegraNegocioException;
import QualidadeDeSoftware.ProjetoQA.repository.LivroRepository;
import QualidadeDeSoftware.ProjetoQA.service.LivroService;

@SpringBootTest
class LivroServiceTest {

        @Autowired
        private LivroService service;

        @Autowired
        private LivroRepository repository;

        @BeforeEach
        void limparBanco() {
                repository.deleteAll();
        }

        /*
         * Testa a criação de um livro com os dados corretos e verifica
         * se o livro é salvo corretamente e se os campos retornados estao certo
         */
        @Test
        void deveCriarLivro() {
                LivroCreateRequest request = new LivroCreateRequest(
                                "Clean Code",
                                "Robert Martin",
                                "111",
                                2008);

                LivroResponse response = service.criar("1", request);

                assertNotNull(response.id());
                assertEquals("Clean Code", response.titulo());
        }

        /*
         * teste que verifica se o sistema nao permite cadastrar 2 livros com o mesmo
         * ISBN para o mesmo usuario e tem que lançar a exceção de regra de negocio
         */
        @ParameterizedTest
        @ValueSource(strings = { "111", "222", "ABC123", "ISBN-999" })
        void naoDevePermitirIsbnDuplicadoParaMesmoUsuario(String isbn) {
                LivroCreateRequest request = new LivroCreateRequest(
                                "Livro Teste",
                                "Autor",
                                isbn,
                                2020);

                service.criar("user1", request);

                assertThrows(RegraNegocioException.class, () -> service.criar("user1", request));
        }

        // Testa se o sistema impede a criação de livros com título vazio ou inválido.
        @ParameterizedTest
        @ValueSource(strings = { "", " ", "   " })
        void naoDeveCriarLivroComTituloInvalido(String titulo) {
                LivroCreateRequest request = new LivroCreateRequest(
                                titulo,
                                "Autor",
                                "999",
                                2020);

                assertThrows(RegraNegocioException.class, () -> service.criar("1", request));
        }

        @ParameterizedTest
        @CsvSource({
                        "'', 'Autor'",
                        "'Livro', ''",
                        "'', ''"
        })

        /*
         * verifica múltiplos cenários de dados inválidos,
         * como título ou autor vazios. Deve lançar exceção de regra de negócio
         */

        void naoDeveCriarLivroComDadosInvalidos(String titulo, String autor) {
                LivroCreateRequest request = new LivroCreateRequest(
                                titulo,
                                autor,
                                "888",
                                2020);

                assertThrows(RegraNegocioException.class, () -> service.criar("1", request));
        }

        /* valida anos de publicação invalidos para ver se o sistema aceita */
        @ParameterizedTest
        @ValueSource(ints = { 0, -1 })
        void naoDeveCriarLivroComAnoInvalido(int ano) {
                LivroCreateRequest request = new LivroCreateRequest(
                                "Livro",
                                "Autor",
                                "777" + ano, // evita duplicação
                                ano);

                assertThrows(RegraNegocioException.class, () -> service.criar("1", request));
        }

        // TESTES DE LISTAGEM

        // garante que os livros criados estão sendo listados
        @Test
        void deveListarLivrosDoUsuario() {
                service.criar("77",
                                new LivroCreateRequest("A", "AA", "1", 2020));

                service.criar("77",
                                new LivroCreateRequest("B", "BB", "2", 2021));

                List<LivroResponse> lista = service.listarPorUsuario("77");

                assertTrue(lista.size() >= 2);
        }

        // TESTES DE ATUALIZAÇÃO

        // verifica se os dados que foram alterados estão corretos após a atualização
        @Test
        void deveAtualizarLivro() {
                LivroResponse criado = service.criar("50",
                                new LivroCreateRequest("Velho", "Autor", "300", 2010));

                LivroUpdateRequest update = new LivroUpdateRequest(
                                "Novo Titulo",
                                "Novo Autor",
                                "300",
                                2024);

                LivroResponse atualizado = service.atualizar("50", criado.id(), update);

                assertEquals("Novo Titulo", atualizado.titulo());
        }

        @Test
        void deveAtualizarLidoQuandoInformado() {
                LivroResponse criado = service.criar("user1",
                                new LivroCreateRequest("Livro", "Autor", "123", 2020));

                LivroUpdateRequest update = new LivroUpdateRequest(
                                "Livro", "Autor", "123", 2020);

                LivroResponse atualizado = service.atualizar("user1", criado.id(), update);

                assertEquals("Livro", atualizado.titulo());
        }

        @Test
        void naoDeveAlterarLidoQuandoForNull() {
                LivroResponse criado = service.criar("user1",
                                new LivroCreateRequest("Livro", "Autor", "1234", 2020));

                LivroUpdateRequest update = new LivroUpdateRequest(
                                "Livro", "Autor", "1234", 2020);

                LivroResponse atualizado = service.atualizar("user1", criado.id(), update);

                assertEquals("Livro", atualizado.titulo());
        }

        // TESTES DE REMOÇÃO

        // verifica se o livro deletado realmente não aparece mais na listagem

        @Test
        void deveRemoverLivro() {
                LivroResponse criado = service.criar("88",
                                new LivroCreateRequest("Temp", "Autor", "700", 2020));

                service.remover("88", criado.id());

                List<LivroResponse> lista = service.listarPorUsuario("88");

                assertTrue(lista.stream().noneMatch(l -> l.id().equals(criado.id())));
        }

        // CAIXA BRANCA (EXCEÇÕES)

        @Test
        void deveLancarExcecaoAoBuscarLivroInexistente() {
                assertThrows(LivroNaoEncontradoException.class, () -> service.buscarPorId("user1", "id-invalido"));
        }

        @Test
        void deveLancarExcecaoAoRemoverLivroInexistente() {
                assertThrows(LivroNaoEncontradoException.class, () -> service.remover("user1", "id-invalido"));
        }

        @Test
        void deveLancarExcecaoAoAtualizarLivroInexistente() {
                LivroUpdateRequest request = new LivroUpdateRequest(
                                "Novo", "Autor", "123", 2020);

                assertThrows(LivroNaoEncontradoException.class,
                                () -> service.atualizar("user1", "id-invalido", request));
        }

        // ISOLAMENTO ENTRE USUÁRIOS

        @Test
        void usuarioNaoDeveAcessarLivroDeOutroUsuario() {
                LivroResponse criado = service.criar("user1",
                                new LivroCreateRequest("Livro", "Autor", "9999", 2020));

                assertThrows(LivroNaoEncontradoException.class, () -> service.buscarPorId("user2", criado.id()));
        }

        // CAIXA PRETA

        @Test
        void caixaPretaDeveCriarLivroComDadosValidos() {
                LivroCreateRequest request = new LivroCreateRequest(
                                "Dom Casmurro",
                                "Machado de Assis",
                                "ISBN-CP-001",
                                2000);

                LivroResponse response = service.criar("user1", request);

                assertNotNull(response.id());
                assertEquals("Dom Casmurro", response.titulo());
                assertEquals("Machado de Assis", response.autor());
                assertEquals("ISBN-CP-001", response.isbn());
        }

        @Test
        void caixaPretaNaoDevePermitirIsbnDuplicadoParaMesmoUsuario() {
                service.criar("user1",
                                new LivroCreateRequest("Livro 1", "Autor", "ISBN-CP-002", 2020));

                assertThrows(RegraNegocioException.class, () -> service.criar("user1",
                                new LivroCreateRequest("Livro 2", "Autor", "ISBN-CP-002", 2021)));
        }

        @Test
        void caixaPretaDevePermitirMesmoIsbnParaUsuariosDiferentes() {
                LivroCreateRequest request = new LivroCreateRequest(
                                "Livro",
                                "Autor",
                                "ISBN-CP-003",
                                2020);

                service.criar("user1", request);

                assertDoesNotThrow(() -> service.criar("user2", request));
        }

        @Test
        void caixaPretaDeveListarApenasLivrosDoUsuario() {
                service.criar("user1",
                                new LivroCreateRequest("Livro A", "Autor", "ISBN-CP-004", 2020));

                service.criar("user2",
                                new LivroCreateRequest("Livro B", "Autor", "ISBN-CP-005", 2020));

                List<LivroResponse> livros = service.listarPorUsuario("user1");

                assertEquals(1, livros.size());
                assertEquals("Livro A", livros.get(0).titulo());
        }

        @Test
        void caixaPretaUsuarioNaoDeveAcessarLivroDeOutroUsuario() {
                LivroResponse livro = service.criar("user1",
                                new LivroCreateRequest("Privado", "Autor", "ISBN-CP-006", 2020));

                assertThrows(LivroNaoEncontradoException.class, () -> service.buscarPorId("user2", livro.id()));
        }

        @Test
        void caixaPretaDeveAtualizarLivroCorretamente() {
                LivroResponse criado = service.criar("user1",
                                new LivroCreateRequest("Antigo", "Autor", "ISBN-CP-007", 2020));

                LivroUpdateRequest update = new LivroUpdateRequest(
                                "Novo",
                                "Novo Autor",
                                "ISBN-CP-007",
                                2021);

                LivroResponse atualizado = service.atualizar("user1", criado.id(), update);

                assertEquals("Novo", atualizado.titulo());
        }

        @Test
        void caixaPretaDeveRemoverLivro() {
                LivroResponse criado = service.criar("user1",
                                new LivroCreateRequest("Remover", "Autor", "ISBN-CP-008", 2020));

                service.remover("user1", criado.id());

                assertThrows(LivroNaoEncontradoException.class, () -> service.buscarPorId("user1", criado.id()));
        }
}
