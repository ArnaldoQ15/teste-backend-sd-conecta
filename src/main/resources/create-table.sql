create table crm (
       crm_id bigint generated by default as identity,
        crm varchar(45),
        uf varchar(255),
        specialty varchar(255),
        user_id bigint,
        primary key (crm_id)
    )

create table user_system (
       user_id bigint generated by default as identity,
       name varchar(255),
       surname varchar(255),
       password varchar(255),
       admin boolean,
       email varchar(255),
       mobile_phone varchar(255),
       authorization_status varchar(255),
       primary key (user_id)
    )

alter table crm
       add constraint fk_user_id
       foreign key (user_id)
       references user_system