create table SEC_MENU
(
  ID          NUMBER(19) not null,
  DESCRIPTION VARCHAR2(500 CHAR),
  NAME        VARCHAR2(200 CHAR) not null,
  PARENT_MENU NUMBER(19)
);
alter table SEC_MENU add primary key (ID);

create table SEC_RESOURCE
(
  ID     NUMBER(19) not null,
  NAME   VARCHAR2(200 CHAR) not null,
  SOURCE VARCHAR2(200 CHAR),
  MENU   NUMBER(19)
);
alter table SEC_RESOURCE add primary key (ID);
alter table SEC_RESOURCE add unique (NAME);
alter table SEC_RESOURCE add constraint FK_RESOURCE_MENU foreign key (MENU) references SEC_MENU (ID);

create table SEC_AUTHORITY
(
  ID          NUMBER(19) not null,
  DESCRIPTION VARCHAR2(500 CHAR),
  NAME        VARCHAR2(200 CHAR) not null
);
alter table SEC_AUTHORITY add primary key (ID);
alter table SEC_AUTHORITY add unique (NAME);

create table SEC_AUTHORITY_RESOURCE
(
  AUTHORITY_ID NUMBER(19) not null,
  RESOURCE_ID  NUMBER(19) not null
);
alter table SEC_AUTHORITY_RESOURCE add constraint FK_AUTHORITY_RESOURCE1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_AUTHORITY_RESOURCE add constraint FK_AUTHORITY_RESOURCE2 foreign key (RESOURCE_ID) references SEC_RESOURCE (ID);

create table SEC_ROLE
(
  ID          NUMBER(19) not null,
  DESCRIPTION VARCHAR2(500 CHAR),
  NAME        VARCHAR2(200 CHAR) not null
);
alter table SEC_ROLE add primary key (ID);
alter table SEC_ROLE add unique (NAME);

create table SEC_ROLE_AUTHORITY
(
  ROLE_ID      NUMBER(19) not null,
  AUTHORITY_ID NUMBER(19) not null
);
alter table SEC_ROLE_AUTHORITY add constraint FK_ROLE_AUTHORITY1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_ROLE_AUTHORITY add constraint FK_ROLE_AUTHORITY2 foreign key (ROLE_ID) references SEC_ROLE (ID);

create table SEC_ORG
(
  ID          NUMBER(19) not null,
  ACTIVE      VARCHAR2(255 CHAR),
  DESCRIPTION VARCHAR2(500 CHAR),
  FULLNAME    VARCHAR2(200 CHAR),
  NAME        VARCHAR2(200 CHAR) not null,
  TYPE        VARCHAR2(200 CHAR),
  PARENT_ORG  NUMBER(19)
);
alter table SEC_ORG add primary key (ID);
alter table SEC_ORG add constraint FK_ORG_PARENT foreign key (PARENT_ORG) references SEC_ORG (ID);

create table SEC_USER
(
  ID       NUMBER(19) not null,
  ADDRESS  VARCHAR2(200 CHAR),
  AGE      NUMBER(10),
  EMAIL    VARCHAR2(100 CHAR),
  ENABLED  VARCHAR2(50 CHAR),
  FULLNAME VARCHAR2(100 CHAR),
  PASSWORD VARCHAR2(50 CHAR),
  SEX      VARCHAR2(50 CHAR),
  TYPE     NUMBER(10),
  USERNAME VARCHAR2(50 CHAR) not null,
  ORG      NUMBER(19),
  SALT     VARCHAR2(255 CHAR)
);
alter table SEC_USER add primary key (ID);
alter table SEC_USER add unique (USERNAME);
alter table SEC_USER add constraint FK_USER_ORG foreign key (ORG) references SEC_ORG (ID);

create table SEC_ROLE_USER
(
  USER_ID NUMBER(19) not null,
  ROLE_ID NUMBER(19) not null
);
alter table SEC_ROLE_USER add constraint FK_ROLE_USER1 foreign key (USER_ID) references SEC_USER (ID);
alter table SEC_ROLE_USER add constraint FK_ROLE_USER2 foreign key (ROLE_ID) references SEC_ROLE (ID);

create table SEC_USER_AUTHORITY
(
  USER_ID      NUMBER(19) not null,
  AUTHORITY_ID NUMBER(19) not null
);
alter table SEC_USER_AUTHORITY add constraint FK_USER_AUTHORITY1 foreign key (AUTHORITY_ID) references SEC_AUTHORITY (ID);
alter table SEC_USER_AUTHORITY add constraint FK_USER_AUTHORITY2 foreign key (USER_ID) references SEC_USER (ID);

create table CONF_DICTIONARY
(
  ID          NUMBER(19) not null,
  DESCRIPTION VARCHAR2(500 CHAR),
  NAME        VARCHAR2(200 CHAR) not null,
  CN_NAME     VARCHAR2(200 CHAR) not null
);
alter table CONF_DICTIONARY add primary key (ID);
alter table CONF_DICTIONARY add unique (NAME);

create table CONF_DICTITEM
(
  ID          NUMBER(19) not null,
  DESCRIPTION VARCHAR2(500 CHAR),
  NAME        VARCHAR2(200 CHAR) not null,
  ORDERBY     NUMBER(10),
  DICTIONARY  NUMBER(19),
  CODE        VARCHAR2(50 CHAR)
);
alter table CONF_DICTITEM add primary key (ID);
alter table CONF_DICTITEM add unique (NAME);
alter table CONF_DICTITEM add constraint FK_DICTITEM_DICTIONARY foreign key (DICTIONARY) references CONF_DICTIONARY (ID);

create sequence HIBERNATE_SEQUENCE minvalue 1 maxvalue 9999999999999999999999 start with 1000 increment by 1 cache 20;