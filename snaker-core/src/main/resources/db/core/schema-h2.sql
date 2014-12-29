create table wf_process (
  id               VARCHAR(32) primary key not null,
  name             VARCHAR(100),
  display_Name     VARCHAR(200),
  type             VARCHAR(100),
  instance_Url     VARCHAR(200),
  state            integer,
  content          longvarbinary,
  version          integer,
  create_Time      VARCHAR(50),
  creator          VARCHAR(50)
);

create table wf_order (
  id               VARCHAR(32) not null primary key,
  process_Id       VARCHAR(32) not null,
  creator          VARCHAR(50),
  create_Time      VARCHAR(50) not null,
  expire_Time      VARCHAR(50),
  last_Update_Time VARCHAR(50),
  last_Updator     VARCHAR(50),
  priority         integer,
  parent_Id        VARCHAR(32),
  parent_Node_Name VARCHAR(100),
  order_No         VARCHAR(50),
  variable         VARCHAR(2000),
  version          integer
);

create table wf_task (
  id               VARCHAR(32) not null primary key,
  order_Id         VARCHAR(32) not null,
  task_Name        VARCHAR(100) not null,
  display_Name     VARCHAR(200) not null,
  task_Type        integer not null,
  perform_Type     integer,
  operator         VARCHAR(50),
  create_Time      VARCHAR(50) not null,
  finish_Time      VARCHAR(50),
  expire_Time      VARCHAR(50),
  action_Url       VARCHAR(200),
  parent_Task_Id   VARCHAR(32),
  variable         VARCHAR(2000),
  version          integer
);

create table wf_task_actor (
  task_Id          VARCHAR(32) not null,
  actor_Id         VARCHAR(50) not null
);

create table wf_hist_order (
  id               VARCHAR(32) not null primary key,
  process_Id       VARCHAR(32) not null,
  order_State      integer not null,
  creator          VARCHAR(50),
  create_Time      VARCHAR(50) not null,
  end_Time         VARCHAR(50),
  expire_Time      VARCHAR(50),
  priority         integer,
  parent_Id        VARCHAR(32),
  order_No         VARCHAR(50),
  variable         VARCHAR(2000)
);

create table wf_hist_task (
  id               VARCHAR(32) not null primary key,
  order_Id         VARCHAR(32) not null,
  task_Name        VARCHAR(100) not null,
  display_Name     VARCHAR(200) not null,
  task_Type        integer not null,
  perform_Type     integer,
  task_State       integer not null,
  operator         VARCHAR(50),
  create_Time      VARCHAR(50) not null,
  finish_Time      VARCHAR(50),
  expire_Time      VARCHAR(50),
  action_Url       VARCHAR(200),
  parent_Task_Id   VARCHAR(32),
  variable         VARCHAR(2000)
);

create table wf_hist_task_actor (
  task_Id          VARCHAR(32) not null,
  actor_Id         VARCHAR(50) not null
);

create table wf_surrogate (
  id                VARCHAR(32) not null primary key,
  process_Name      VARCHAR(100),
  operator          VARCHAR(50),
  surrogate         VARCHAR(50),
  odate             VARCHAR(64),
  sdate             VARCHAR(64),
  edate             VARCHAR(64),
  state             integer
);

create table wf_cc_order (
  order_Id        VARCHAR(32),
  actor_Id        VARCHAR(50),
  creator         VARCHAR(50),
  create_Time     VARCHAR(50),
  finish_Time    VARCHAR(50),
  status          integer
);
create index IDX_CCORDER_ORDER on wf_cc_order (order_Id);

create index IDX_PROCESS_NAME on wf_process (name);
create index IDX_ORDER_PROCESSID on wf_order (process_Id);
create index IDX_ORDER_NO on wf_order (order_No);
create index IDX_TASK_ORDER on wf_task (order_Id);
create index IDX_TASK_TASKNAME on wf_task (task_Name);
create index IDX_TASK_PARENTTASK on wf_task (parent_Task_Id);
create index IDX_TASKACTOR_TASK on wf_task_actor (task_Id);
create index IDX_HIST_ORDER_PROCESSID on wf_hist_order (process_Id);
create index IDX_HIST_ORDER_NO on wf_hist_order (order_No);
create index IDX_HIST_TASK_ORDER on wf_hist_task (order_Id);
create index IDX_HIST_TASK_TASKNAME on wf_hist_task (task_Name);
create index IDX_HIST_TASK_PARENTTASK on wf_hist_task (parent_Task_Id);
create index IDX_HIST_TASKACTOR_TASK on wf_hist_task_actor (task_Id);

alter table wf_task_actor
add constraint FK_TASK_ACTOR_TASKID foreign key (task_Id)
references wf_task (id);
alter table wf_task
add constraint FK_TASK_ORDERID foreign key (order_Id)
references wf_order (id);
alter table wf_order
add constraint FK_ORDER_PARENTID foreign key (parent_Id)
references wf_order (id);
alter table wf_order
add constraint FK_ORDER_PROCESSID foreign key (process_Id)
references wf_process (id);
alter table wf_hist_task_actor
add constraint FK_HIST_TASKACTOR foreign key (task_Id)
references wf_hist_task (id);
alter table wf_hist_task
add constraint FK_HIST_TASK_ORDERID foreign key (order_Id)
references wf_hist_order (id);
alter table wf_hist_order
add constraint FK_HIST_ORDER_PARENTID foreign key (parent_Id)
references wf_hist_order (id);
alter table wf_hist_order
add constraint FK_HIST_ORDER_PROCESSID foreign key (process_Id)
references wf_process (id);