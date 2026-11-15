# Sistema de Gerenciamento de Senhas
## Sobre o projeto
Este projeto foi desenvolvido em Java como atividade em grupo do curso de Análise e Desenvolvimento de Sistemas, esse arquivo em específico foi feito pelo aluno Marriton, a pessoa 4 responsavel por criar um serviço capaz de gerar e verificar senhas de maneira segura, evitando que elas sejam armazenadas diretamente em seu formato original.
## Como funciona
O sistema utiliza um processo de geração de Salt e Hash para proteger as senhas. Primeiro, é criado um valor aleatório chamado Salt, utilizando a classe SecureRandom do Java. Em seguida, esse valor é utilizado junto à senha para gerar um Hash, que representa a senha de forma protegida.
O sistema também permite verificar se uma senha informada corresponde à senha cadastrada, realizando novamente o processamento e comparando os resultados. Dessa forma, é possível identificar senhas corretas e incorretas sem precisar armazenar a senha original.
## Tecnologias utilizadas
O projeto utiliza Java para o desenvolvimento, Maven para a compilação e organização e Visual Studio Code como ambiente de programação. Também são utilizados recursos de segurança disponíveis na própria plataforma Java.
## Desenvolvimento e fontes de consulta
Durante o desenvolvimento, foram utilizados materiais de estudo disponíveis no YouTube e a ferramenta de inteligência artificial Claude Code, como apoio à tutorização, compreensão do código e resolução de dúvidas.
Os materiais utilizados como referência foram:
 https://www.youtube.com/watch?v=--tnZMuoK3E
  https://www.youtube.com/watch?v=U_hWBcdRU94
 https://www.youtube.com/watch?v=tqFncRgUWko
 https://www.youtube.com/watch?v=cPh0Jo0auVA
Também foram consultadas as documentações oficiais do Java e do Maven:
 Java: https://docs.oracle.com/en/java/
 Java Cryptography: https://docs.oracle.com/en/java/javase/
 Maven: https://maven.apache.org/guides/

## Execução
Para compilar o projeto, é necessário ter Java JDK e Maven instalados. abra a pasta principal do projeto e execute esses comandos no terminal:

mvn clean compile

Para executar a aplicação:

java -cp target/classes projetopoo.App

## Informações acadêmicas
Curso: Análise e Desenvolvimento de Sistemas
Instituição:SENAC
Desenvolvedor:Marriton Santos Dias Junior
Ano:2026