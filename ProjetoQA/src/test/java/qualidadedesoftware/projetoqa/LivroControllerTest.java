package qualidadedesoftware.projetoqa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import qualidadedesoftware.projetoqa.repository.LivroRepository;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveRetornarForbiddenQuandoNaoAutenticado() throws Exception {

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveBloquearAutenticacaoSomenteViaHeader() throws Exception {

        mockMvc.perform(get("/api/livros")
                        .header("X-Usuario-Id", "user-header"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveCriarLivroComSucesso() throws Exception {

        String json = """
                {
                    "titulo": "Clean Code",
                    "autor": "Robert Martin",
                    "isbn": "ISBN-001",
                    "anoPublicacao": 2008
                }
                """;

        mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarLivrosDoUsuario() throws Exception {

        String json = """
                {
                    "titulo": "Livro Teste",
                    "autor": "Autor Teste",
                    "isbn": "ISBN-002",
                    "anoPublicacao": 2020
                }
                """;

        mockMvc.perform(post("/api/livros")
                .with(csrf())
                .sessionAttr("usuarioId", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/api/livros")
                        .sessionAttr("usuarioId", "user1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarLivroPorId() throws Exception {

        String json = """
                {
                    "titulo": "Livro Busca",
                    "autor": "Autor Busca",
                    "isbn": "ISBN-003",
                    "anoPublicacao": 2020
                }
                """;

        String response = mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/livros/" + id)
                        .sessionAttr("usuarioId", "user1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarLivroComSucesso() throws Exception {

        String createJson = """
                {
                    "titulo": "Livro Original",
                    "autor": "Autor Original",
                    "isbn": "ISBN-004",
                    "anoPublicacao": 2020
                }
                """;

        String response = mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        String updateJson = """
                {
                    "titulo": "Livro Atualizado",
                    "autor": "Autor Atualizado",
                    "isbn": "ISBN-004",
                    "anoPublicacao": 2021
                }
                """;

        mockMvc.perform(put("/api/livros/" + id)
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    void deveRemoverLivroComSucesso() throws Exception {

        String json = """
                {
                    "titulo": "Livro Remover",
                    "autor": "Autor Remover",
                    "isbn": "ISBN-005",
                    "anoPublicacao": 2020
                }
                """;

        String response = mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(delete("/api/livros/" + id)
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {

        String json = """
                {
                    "titulo": "",
                    "autor": "",
                    "isbn": "",
                    "anoPublicacao": -1
                }
                """;

        mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestAoAtualizarComDadosInvalidos() throws Exception {

        String json = """
                {
                    "titulo": "",
                    "autor": "",
                    "isbn": "",
                    "anoPublicacao": -1
                }
                """;

        mockMvc.perform(put("/api/livros/id-invalido")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundQuandoLivroNaoExiste() throws Exception {

        mockMvc.perform(get("/api/livros/id-invalido")
                        .sessionAttr("usuarioId", "user1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarNotFoundAoRemoverLivroInexistente() throws Exception {

        mockMvc.perform(delete("/api/livros/id-invalido")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDevePermitirAcessoLivroDeOutroUsuario() throws Exception {

        String json = """
                {
                    "titulo": "Privado",
                    "autor": "Autor",
                    "isbn": "ISBN-006",
                    "anoPublicacao": 2020
                }
                """;

        String response = mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/livros/" + id)
                        .sessionAttr("usuarioId", "user2"))
                .andExpect(status().isNotFound());
    }
}