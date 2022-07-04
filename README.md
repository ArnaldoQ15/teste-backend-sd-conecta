<h2>Teste CRUD com integração web de usuários e CRM's</h2>

O primeiro passo é criar as tabelas no banco de dados (recomendavelmente H2) a partir dos scripts dentro do package ```testebackend.scripts```.

---

Seu banco de dados estará cru. O próximo passo é criar um usuário com perfil de administrador, pois sem esta autorização, não poderá fazer nada além de login e buscar os outros usuários médicos.
Para isso, deverá acessar a rota ```/user/new-admin``` e inserir os dados do usuário.

Existem campos obrigatórios ao cadastrar um usuário, como nome, email, senha e CRM. Este último precisa ter um número CRM e um estado brasileiro (ex: SP para São Paulo).

Desta forma você possuirá um usuário administrador capaz de realizar funções de persistência, alteração e exclusão de usuários.

---

O token de acesso é necessário para realizar as requisições ao ambiente de desenvolvimento da SD Conecta. Para continuar, é necessário gerar através do link ```https://beta.sdconecta.com/oauth2/token``` passando como body (x-www-form-urlencoded) o seu client_id, client_secret e grant_type.

---

Com o usuário autenticado na SD Conecta, é possível fazer o login utilizando os e-mails com permissão de acesso através da rota ```/user/login``` passando o e-mail e senha cadastrados previamente. Tendo sucesso, lhe retornará o access_token e o refresh_token (obtidos do ambiente de desenvolvimento da SD Conecta) para o seu usuário.

---

Cada usuário possui apenas um registro de token no banco de dados (tabela ```user_token```). Se já existir um registro de token nesta tabela, ele se manterá caso não esteja expirado ou se renovará caso seja feito um novo login com as devidas permissões no ambiente de desenvolvimento da SD Conecta.

---

Os testes unitários das classes ```User``` e ```Crm``` são realizados através do package ```testebackend.tests```.

---

A documentação com os bodys, params e demais informações sobre os endpoints da API estão disponíveis no link: https://www.postman.com/collections/c391f9ce304a00851e18.

Passo-a-passo: 
- abrir o Postman;
- clicar em 'import' logo ao lado do nome da Workspace;
- na aba que abrir, clicar em 'Link' e em seguida colar o link acima.