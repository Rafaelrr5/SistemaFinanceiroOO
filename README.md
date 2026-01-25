# Sistema de Gestão de Carteiras Financeiras

Sistema de gerenciamento de carteiras de investimentos desenvolvido em Java, utilizando conceitos de Orientação a Objetos.

## 📋 Descrição

O sistema permite gerenciar investidores e seus ativos financeiros, incluindo:
- **Ações** (nacionais, renda variável)
- **FIIs** - Fundos de Investimento Imobiliário (nacionais, renda variável)
- **Tesouro Direto** (nacional, renda fixa)
- **Stocks** - Ações internacionais (internacionais, renda variável)
- **Criptoativos** (internacionais, renda variável)

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Maven** (gerenciamento de dependências)
- **JUnit 5** (testes unitários)
- **OpenCSV** (leitura de arquivos CSV)

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/financeiro/
│   │   ├── exception/      # Exceções personalizadas
│   │   ├── model/          # Classes de domínio (Ativo, Investidor, etc.)
│   │   ├── repository/     # Camada de persistência em memória
│   │   ├── service/        # Regras de negócio
│   │   └── ui/             # Interface de console
│   └── resources/          # Arquivos CSV de dados
└── test/
    └── java/               # Testes unitários
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+

### Compilar o projeto
```bash
mvn clean compile
```

### Executar o sistema
```bash
mvn exec:java -Dexec.mainClass="com.financeiro.Main"
```

Ou compile e execute diretamente:
```bash
cd src/main/java
javac -d ../../../target/classes com/financeiro/Main.java
cd ../../../target/classes
java com.financeiro.Main
```

### Executar testes
```bash
mvn test
```

## 📖 Funcionalidades

### Menu Principal
1. **Ativos** - Gerenciamento de ativos financeiros
2. **Investidores** - Gerenciamento de investidores

### Menu Ativos
- Cadastrar ativo (individual ou em lote)
- Editar ativo
- Excluir ativo (propaga para carteiras)
- Relatórios por tipo de ativo

### Menu Investidores
- Cadastrar investidor (PF ou PJ)
- Cadastrar em lote via arquivo CSV
- Listar todos investidores
- Excluir investidores (por lista de CPF/CNPJ)
- Selecionar investidor para operações

### Menu do Investidor Selecionado
- Editar informações
- Visualizar carteira de ativos
- Ver totais (gasto vs atual)
- Ver percentuais (renda fixa/variável, nacional/internacional)
- Salvar relatório em JSON
- Comprar/Vender ativos
- Importar movimentações em lote

## 📊 Regras de Negócio

### Perfis de Investidor (Pessoa Física)
| Perfil | Pode operar |
|--------|-------------|
| Conservador | Ações, FIIs, Tesouro |
| Moderado | + Stocks |
| Arrojado | + Criptoativos |

### Investidor Qualificado
- Patrimônio ≥ R$ 1.000.000,00
- Pode operar ativos marcados como "qualificados"

### Investidor Institucional (PJ)
- Pode operar qualquer tipo de ativo

## 📂 Formato dos Arquivos CSV

### investidores.csv
```
TIPO;NOME;IDENTIFICADOR;TELEFONE;EMAIL;ENDERECO;PATRIMONIO;PERFIL_OU_RAZAO
PF;João Silva;12345678901;11999998888;joao@email.com;Rua A, SP;50000.00;CONSERVADOR
PJ;Empresa ABC;12345678000199;11955554444;contato@abc.com;Av Paulista, SP;5000000.00;ABC LTDA
```

### transacoes.csv
```
TICKER;TIPO;QUANTIDADE;PRECO;INSTITUICAO
PETR3;C;100;35.50;NuInvest
ITUB4;V;20;30.50;Clear Corretora
```

## 👥 Autores

- Desenvolvido para a disciplina de Programação Orientada a Objetos

## 📄 Licença

Este projeto é de uso acadêmico.