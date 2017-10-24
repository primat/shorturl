-- create table if not exists short_url (id bigint identity primary key not null, url varchar(255) not null)

CREATE TABLE IF NOT EXISTS SHORT_URL
(
    ID bigint PRIMARY KEY NOT NULL IDENTITY,
    URL varchar(255) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS UNIQUE_URL_ON_SHORT_URL ON SHORT_URL (URL)
