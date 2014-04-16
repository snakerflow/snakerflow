create table SEC_MENU
(
  ID          numeric(19,0) not null,
  DESCRIPTION varchar(500),
  NAME        varchar(200) not null,
  PARENT_MENU numeric(19,0)
);
alter table SEC_MENU add primary key (ID);

create table SEC_RESOURCE
(
  ID     numeric(19,0) not null,
  NAME   varchar(200) not null,
  SOURCE varchar(200),
  MENU   numeric(19,0)
);
alter table SEC_RESOURCE add primary key (ID);
alter table SEC_RESOURCE add unique (NAME);
alter table SEC_RESOURCE add constraint FK_RESOURCE_MENU foreign key (MENU) references SEC_MENU (ID);

create table SEC_AUTHORITY
(
  ID          numeric(19,0) not null,
  DESCRIPTION varchar(500 ),
  NAME        varchar(200 ) not null
);
alter table SEC_AUTHORITY add primary key (ID);
alter table SEC_AUTHORITY add unique (NAME);

create table SEC_AUTHORITY_RESOURCE
(
  AUTHORITY_ID numeric(19,0) not null,
  RESOURCE_ID  numeric(19,0) not null
);
alter table SEC_AUTHORITY_RESOURCE add constraint FK_AUTHORITY_RESOURCE1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_AUTHORITY_RESOURCE add constraint FK_AUTHORITY_RESOURCE2 foreign key (RESOURCE_ID) references SEC_RESOURCE (ID);

create table SEC_ROLE
(
  ID          numeric(19,0) not null,
  DESCRIPTION varchar(500),
  NAME        varchar(200) not null
);
alter table SEC_ROLE add primary key (ID);
alter table SEC_ROLE add unique (NAME);

create table SEC_ROLE_AUTHORITY
(
  ROLE_ID      numeric(19,0) not null,
  AUTHORITY_ID numeric(19,0) not null
);
alter table SEC_ROLE_AUTHORITY add constraint FK_ROLE_AUTHORITY1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_ROLE_AUTHORITY add constraint FK_ROLE_AUTHORITY2 foreign key (ROLE_ID) references SEC_ROLE (ID);

create table SEC_ORG
(
  ID          numeric(19,0) not null,
  ACTIVE      varchar(255),
  DESCRIPTION varchar(500),
  FULLNAME    varchar(200),
  NAME        varchar(200) not null,
  TYPE        varchar(200),
  PARENT_ORG  numeric(19,0)
);
alter table SEC_ORG add primary key (ID);
alter table SEC_ORG add constraint FK_ORG_PARENT foreign key (PARENT_ORG) references SEC_ORG (ID);

create table SEC_USER
(
  ID       numeric(19,0) not null,
  ADDRESS  varchar(200),
  AGE      numeric(10,0),
  EMAIL    varchar(100),
  ENABLED  varchar(50),
  FULLNAME varchar(100),
  PASSWORD varchar(50),
  SEX      varchar(50),
  TYPE     numeric(10,0),
  USERNAME varchar(50) not null,
  ORG      numeric(19,0),
  SALT     varchar(255)
);
alter table SEC_USER add primary key (ID);
alter table SEC_USER add unique (USERNAME);
alter table SEC_USER add constraint FK_USER_ORG foreign key (ORG) references SEC_ORG (ID);

create table SEC_ROLE_USER
(
  USER_ID numeric(19,0) not null,
  ROLE_ID numeric(19,0) not null
);
alter table SEC_ROLE_USER add constraint FK_ROLE_USER1 foreign key (USER_ID) references SEC_USER (ID);
alter table SEC_ROLE_USER add constraint FK_ROLE_USER2 foreign key (ROLE_ID) references SEC_ROLE (ID);

create table SEC_USER_AUTHORITY
(
  USER_ID      numeric(19,0) not null,
  AUTHORITY_ID numeric(19,0) not null
);
alter table SEC_USER_AUTHORITY add constraint FK_USER_AUTHORITY1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_USER_AUTHORITY add constraint FK_USER_AUTHORITY2 foreign key (USER_ID) references SEC_USER (ID);

create table CONF_DICTIONARY
(
  ID          numeric(19,0) not null,
  DESCRIPTION varchar(500),
  NAME        varchar(200) not null,
  CN_NAME     varchar(200) not null
);
alter table CONF_DICTIONARY add primary key (ID);
alter table CONF_DICTIONARY add unique (NAME);

create table CONF_DICTITEM
(
  ID          numeric(19,0) not null,
  DESCRIPTION varchar(500),
  NAME        varchar(200) not null,
  ORDERBY     numeric(10,0),
  DICTIONARY  numeric(19,0),
  CODE        varchar(50)
);
alter table CONF_DICTITEM add primary key (ID);
alter table CONF_DICTITEM add unique (NAME);
alter table CONF_DICTITEM add constraint FK_DICTITEM_DICTIONARY foreign key (DICTIONARY) references CONF_DICTIONARY (ID);