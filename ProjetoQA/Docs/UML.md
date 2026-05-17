# DIAGRAMAS UML DE SEQUÊNCIA
**REQUISITOS FUNCIONAIS**

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
