# ProjetoQA - Biblioteca de Livros

Projeto Spring Boot com foco em qualidade e testes automatizados.

## 📋 Requisitos

- Java 21+
- Maven 3.9+
- Docker (para MongoDB em testes)

## 🧪 Testes

### Rodar todos os testes

```bash
./mvnw clean test
```

### Rodar testes específicos

```bash
./mvnw test -Dtest=LivroServiceTest
```

## 📊 Cobertura de Código com JaCoCo

### Gerar relatório de cobertura

```bash
./mvnw clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

### Verificar cobertura mínima (80%)

```bash
./mvnw jacoco:check
```

Se a cobertura estiver abaixo de 80%, o build falhará.

## 🔍 SonarQube

### Prerequisitos

- Ter uma conta em [SonarCloud](https://sonarcloud.io)
- Token de autenticação gerado

### Configuração Local

1. Configure as propriedades no `sonar-project.properties` ou use variáveis de ambiente

2. Rode análise local:

```bash
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=Bloodborne2_ProjetoQA \
  -Dsonar.organization=bloodborne2 \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=YOUR_SONAR_TOKEN
```

### Via GitHub Actions

O pipeline CI é automaticamente acionado em:
- Push para `main` ou `develop`
- Pull Requests para `main` ou `develop`

Pipeline realiza:
1. ✅ Compilação do código
2. ✅ Execução de testes
3. ✅ Geração de cobertura com JaCoCo
4. ✅ Verificação de cobertura mínima (80%)
5. ✅ Envio de relatórios para SonarCloud
6. ✅ Comentário automático em PRs com cobertura

## 📁 Estrutura de Testes

```
src/test/java/
├── LivroServiceTest.java         # Testes do serviço (29 testes)
├── LivroRepositoryTest.java      # Testes do repositório
├── TestProjetoQaApplication.java # Configuração de testes
└── TestcontainersConfiguration.java # Docker containers para testes
```

## 🎯 Estratégia de Testes

- **Unitários/Integração**: Com Testcontainers para MongoDB
- **Parametrizados**: Múltiplos cenários por teste
- **Caixa Branca**: Lógica interna e exceções
- **Caixa Preta**: Teste E2E com dados válidos/inválidos
- **Isolamento**: Entre usuários e dados

## ✅ Cobertura Mínima: 80%

- JaCoCo garante 80% de cobertura
- Relatório em `target/site/jacoco/`
- SonarQube valida qualidade

## 🚀 Deploy

Todos os builds devem passar nas verificações:
- Testes: ✅ 29/29 passando
- Cobertura: ✅ Mínimo 80%
- SonarQube: ✅ Gates de qualidade

## 📚 Referências

- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Testcontainers](https://www.testcontainers.org/)
