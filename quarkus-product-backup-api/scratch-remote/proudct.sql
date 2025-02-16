    create table Product (
        id bigint not null,
        code varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id)
    )

    create sequence Product_SEQ start with 1 increment by 50