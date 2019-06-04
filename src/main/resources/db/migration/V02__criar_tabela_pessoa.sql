CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	ativo int(1) NOT NULL,
	logradouro VARCHAR (50),
	numero VARCHAR (10),
	complemento VARCHAR (50),
	bairro VARCHAR (50),
	cep VARCHAR (20),
	cidade VARCHAR (20),
	estado VARCHAR (10)

)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Cleber Batista Graciano', 1,'Rua dos Liririos', '255', '', 'Mirandopolis', '19915280', 'São Paulo', 'SP');
