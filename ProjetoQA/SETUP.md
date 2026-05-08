# 🔧 Setup Guide - GitHub Actions e SonarQube

## 1️⃣ Configurar Secrets no GitHub

### No repositório GitHub:

1. Acesse: **Settings** → **Secrets and variables** → **Actions**
2. Clique em **New repository secret**
3. Adicione o secret `SONAR_TOKEN`:
   - **Name**: `SONAR_TOKEN`
   - **Value**: `27955be2df40fda82dd27b6f25f2f416bbac3581`

## 2️⃣ GitHub Actions Workflow

O arquivo `.github/workflows/ci.yml` já está configurado para:

✅ Fazer checkout do código  
✅ Compilar o projeto  
✅ Rodar testes com cobertura JaCoCo  
✅ Verificar cobertura mínima (80%)  
✅ Enviar relatórios para SonarCloud  
✅ Comentar resultado em PRs  

### Triggers Automáticos:
- Push em `main` ou `develop`
- Pull Requests para `main` ou `develop`

## 3️⃣ SonarQube Cloud

### Configurações:
- **Project Key**: `Bloodborne2_ProjetoQA`
- **Organization**: `bloodborne2`
- **URL**: https://sonarcloud.io

### Visualizar Resultados:
```
https://sonarcloud.io/project/overview?id=Bloodborne2_ProjetoQA
```

## 4️⃣ JaCoCo Report

Após rodar testes localmente:

```bash
./mvnw clean test jacoco:report
```

Abrir relatório:
```
target/site/jacoco/index.html
```

## 5️⃣ Verificar Cobertura Mínima

Para ativar a verificação de 80% de cobertura:

```bash
./mvnw clean test -Pcoverage-check
```

## 📊 Métricas Monitoradas

✅ **Cobertura de Linha**: Mínimo 80%  
✅ **Complexidade**: Monitorada pelo SonarQube  
✅ **Code Smells**: Reportados pelo SonarQube  
✅ **Vulnerabilidades**: Escaneadas  
✅ **Hotspots de Segurança**: Identificados  

## 🚀 Deploy Requirements

Todos os requisitos devem passar para fazer push para `main`:

- ✅ Testes: 29/29 passando
- ✅ Build: Sucesso
- ✅ Cobertura: ≥80% (meta)
- ✅ SonarQube: Gates de qualidade

## 📝 Arquivos de Configuração

```
ProjetoQA/
├── .github/
│   └── workflows/
│       └── ci.yml                    # GitHub Actions CI
├── sonar-project.properties          # Configuração SonarQube
├── pom.xml                           # Maven + JaCoCo + Sonar
└── README.md                         # Documentação
```

## ❌ Troubleshooting

### SonarQube não encontra token?
- Verificar se o secret `SONAR_TOKEN` está correto em GitHub
- Conferir se está usando a sintaxe correta: `${{ secrets.SONAR_TOKEN }}`

### Cobertura não atinge 80%?
- Adicionar mais testes
- Usar o perfil: `-Pcoverage-check` para verificar localmente
- Ver relatório em: `target/site/jacoco/index.html`

### Testes falhando em GitHub Actions?
- Verificar logs do workflow
- Executar testes localmente: `./mvnw clean test`
- Verificar if Docker está rodando (MongoDB Testcontainers)

## 📚 Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [SonarQube Cloud](https://sonarcloud.io)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
