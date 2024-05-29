create table contas (
    id bigserial not null primary key,
    descricao varchar(255) not null,
    valor decimal(10,2) not null,
    data_vencimento date not null,
    data_pagamento date,
    situacao VARCHAR(255),
    tipo serial not null
);

create table usuarios (
    id bigserial not null primary key,
    username varchar(255) not null,
    password varchar(255) not null
);