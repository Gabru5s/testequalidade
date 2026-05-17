## DIAGRAMAS UML DE SEQUÊNCIA
#REQUISITOS FUNCIONAIS

**RF01/RF02/RF03**
```mermaid
sequenceDiagram
    participant User as Ator/Navegador
    participant C as AuthController
    participant S as UsuarioService
    participant R as UsuarioRepository
    participant DB as MongoDB
    participant Sec as PasswordEncoder

    User->>C: POST /Cadastro (nome, email, senha)
    Note right of C: Recebe dados do formulário

    C->>S: cadastrar(requestDTO)
    S->>R: existsByEmail(email)
    R-->>S: true/false

    alt E-mail já cadastrado (RF02)
        S-->>S: throw RegraNegocioException("E-mail duplicado")
        S-->>C: erro capturado
        C-->>User: Redireciona para /cadastro?error
    else E-mail único (RF01, RF03)
        S->>Sec: encode(senha plano)
        Sec-->>S: senha hash bcrypt
        S->>DB: save(new UsuarioEntity(..., hash))
        DB-->>S: salvo com sucesso
        S-->>C: UsuarioResponse
        C-->>User: Salva sessão e redireciona para /livros
    end
```
<br>

**RF04**
```mermaid
sequenceDiagram
    participant User as Ator/Navegador
    participant C as AuthController
    participant S as UsuarioService
    participant R as UsuarioRepository
    participant Sec as PasswordEncoder

    User->>C: POST /Login (email, senha)
    C->>S: autenticar(loginRequest)
    S->>R: findByEmail(email)
    R-->>S: optional (user or empty)

    alt Usuário não encontrado ou senha inválida (RF04 - Erro)
        S-->>C: throw RegraNegocioException("Credenciais inválidas")
        C-->>User: Redireciona para /login?error
    else Usuário encontrado (RF04 - Sucesso)
        S->>Sec: matches(senha plano, hash do banco)
        Sec-->>S: true (sucesso)
        S-->>C: UsuarioResponse
        Note over C: Sucesso! Ver fluxo de sessão (RF05)
        C-->>User: Redireciona para /livros
    end
```
<br>

**RF05**
```mermaid
sequenceDiagram
    participant C as AuthController
    participant S as UsuarioService
    participant Session as HttpSession

    Note over C: Pós-validação de credenciais (RF04)
    C->>S: autenticar(...)
    S-->>C: UsuarioResponse (contém ID, email)
    C->>Session: setAttribute("usuarioId", userRes.id)
    C->>Session: setAttribute("usuarioEmail", userRes.email)
    C-->>C: Redirect to /livros
 ```
<br>

**RF06**
```mermaid
sequenceDiagram
    participant User as Ator
    participant C as AuthController
    participant Session as HttpSession

    User->>C: POST /Logout
    C->>Session: invalidate()
    Note right of Session: Sessão é destruída
    C-->>User: Redirect to /login
```
<br>

**RF07/RF0/RF09/RF10**
```mermaid
sequenceDiagram
    participant User as Navegador/JS
    participant Session as HttpSession
    participant C as LivroController
    participant S as LivroService
    participant R as LivroRepository
    participant DB as MongoDB

    User->>C: POST /api/livros (JSON: tít, aut, isbn, ano)
    C->>Session: getAttribute("usuarioId")
    Session-->>C: loggedInUserId

    alt Usuário não logado (RNF04 - Segurança)
        C-->>User: 401 Unauthorized
    else Usuário logado (RF07)
        C->>S: criar(loggedInUserId, requestDTO)

        alt Título/Autor/ISBN vazios (RF08)
            S-->>C: throw RegraNegocioException
            C-->>User: 400 Bad Request / erro JSON
        else Ano futuro ou <= 0 (RF09)
            S-->>C: throw RegraNegocioException
            C-->>User: 400 Bad Request / erro JSON
        else
            S->>R: existsByIsbnAndUsuarioId(isbn, userId)
            R-->>S: true/false
            alt ISBN duplicado p/ usuário (RF10)
                S-->>C: throw RegraNegocioException
                C-->>User: 400 Bad Request / erro JSON
            else Validação sucesso
                S->>DB: save(new LivroEntity(..., userId))
                DB-->>S: salvo
                S-->>C: LivroResponse
                C-->>User: 201 Created com JSON
            end
        end
    end
 ```
<br>

**RF11**
```mermaid
sequenceDiagram
    participant User as Navegador/JS
    participant Session as HttpSession
    participant C as LivroController
    participant S as LivroService
    participant R as LivroRepository

    User->>C: GET /api/livros
    C->>Session: getAttribute("usuarioId")
    Session-->>C: loggedInUserId

    Note over C: Obrigatório validar login (RNF04)
    C->>S: listarPorUsuario(loggedInUserId)
    S->>R: findByUsuarioId(loggedInUserId)
    Note over R: Filtro é feito aqui (RF11)
    R-->>S: List<LivroEntity>
    S-->>C: List<LivroResponseDTO>
    C-->>User: 200 OK com Lista JSON
```
<br>

**RF12/RF13**
```mermaid
sequenceDiagram
    participant User as Navegador/JS
    participant Session as HttpSession
    participant C as LivroController
    participant S as LivroService
    participant R as LivroRepository
    participant DB as MongoDB

    User->>C: DELETE /api/livros/{id}
    C->>Session: getAttribute("usuarioId")
    Session-->>C: loggedInUserId

    Note over C: Obrigatório validar login (RNF04)
    C->>S: remover(loggedInUserId, livroId)

    S->>R: findByIdAndUsuarioId(livroId, loggedInUserId)
    Note over R: Valida se o livro pertence ao usuário (RF12)
    R-->>S: optional (entity or empty)

    alt Livro não encontrado p/ usuário
        S-->>S: throw LivroNaoEncontradoException
        S-->>C: erro capturado
        C-->>User: 404 Not Found / erro JSON
    else Livro encontrado e pertence ao usuário (RF13 Sucesso)
        S->>DB: delete(foundEntity)
        DB-->>S: excluído
        S-->>C: void sucess
        C-->>User: 200/204 No Content
    end
```
<br>

#REQUISITOS NÃO FUNCIONAIS
**RNF01/RNF03/RNF05**

```mermaid
sequenceDiagram
    participant Dev as Desenvolvedor
    participant GH as GitHub Actions
    participant J as JaCoCo
    participant S as SonarQube
    
    Dev->>GH: Push do Código
    Note over GH: Inicia Build Automático (RNF05)
    
    GH->>J: Executa Maven Verify
    J-->>GH: Relatório de Cobertura > 80% (RNF01)
    
    GH->>S: Envia dados para Análise Estática
    Note over S: Verifica Bugs e Dívida Técnica (RNF03)
    S-->>GH: Quality Gate: PASS
    
    GH-->>Dev: Pipeline finalizada com sucesso
```
<br>

**RNF02/RNF07**
```mermaid
sequenceDiagram
    participant T as Testes de Integração
    participant TC as Testcontainers (MongoDB)
    participant VCR as VCR (Cassette)
    participant API as API Externa
    
    T->>TC: Levanta Container Docker (RNF02)
    Note over T, TC: Teste de persistência sem Mocks
    
    T->>VCR: Chama serviço externo
    alt Primeira execução (Record)
        VCR->>API: Requisição Real
        API-->>VCR: Resposta
        VCR->>VCR: Grava arquivo YAML (RNF07)
    else Execuções seguintes (Replay)
        VCR->>VCR: Lê arquivo YAML gravado
    end
    VCR-->>T: Retorna dados para o teste
```
<br>

**RNF04**
```mermaid
sequenceDiagram
    participant S as UsuarioService
    participant Sec as Spring Security (BCrypt)
    participant DB as MongoDB
    
    S->>Sec: encode(senha_plana)
    Note right of Sec: Aplica Algoritmo de Hash (RNF04)
    Sec-->>S: senha_criptografada
    S->>DB: save(usuario_com_hash)
```
<br>

**RNF06/RNF08**
```mermaid
sequenceDiagram
    participant U as Usuário
    participant UI as Interface (CSS/Thymeleaf)
    participant Session as HttpSession
    
    U->>UI: Acessa via Dispositivo Mobile
    Note over UI: Ajusta layout (RNF06 - Responsivo)
    
    U->>UI: Navega entre abas
    UI->>Session: Verifica usuarioId (RNF08)
    Session-->>UI: Retorna dados da sessão ativa
    UI-->>U: Página carregada com usuário logado
```
