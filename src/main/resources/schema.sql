create sequence if not exists hibernate_sequence start with 1 increment by 1;
create table if not exists short_url (id bigint not null, url varchar(255) not null, primary key (id))
