create table wf_process (
    id               nvarchar(32) primary key not null,
    name             nvarchar(100),
    display_Name     nvarchar(200),
    type             nvarchar(100),
    instance_Url     nvarchar(200),
    state            tinyint,
    content          varbinary(max),
    version          tinyint,
    create_Time      nvarchar(50),
    creator          nvarchar(50)
);

create table wf_order (
    id               nvarchar(32) not null primary key,
    process_Id       nvarchar(32) not null,
    creator          nvarchar(50),
    create_Time      nvarchar(50) not null,
    expire_Time      nvarchar(50),
    last_Update_Time nvarchar(50),
    last_Updator     nvarchar(50),
    priority         tinyint,
    parent_Id        nvarchar(32),
    parent_Node_Name nvarchar(100),
    order_No         nvarchar(50),
    variable         nvarchar(2000),
    version          tinyint
);

create table wf_task (
    id               nvarchar(32) not null primary key,
    order_Id         nvarchar(32) not null,
    task_Name        nvarchar(100) not null,
    display_Name     nvarchar(200) not null,
    task_Type        tinyint not null,
    perform_Type     tinyint,
    operator         nvarchar(50),
    create_Time      nvarchar(50) not null,
    finish_Time      nvarchar(50),
    expire_Time      nvarchar(50),
    action_Url       nvarchar(200),
    parent_Task_Id   nvarchar(32),
    variable         nvarchar(2000),
    version          tinyint
);

create table wf_task_actor (
    task_Id          nvarchar(32) not null,
    actor_Id         nvarchar(50) not null
);

create table wf_hist_order (
    id               nvarchar(32) not null primary key,
    process_Id       nvarchar(32) not null,
    order_State      tinyint not null,
    creator          nvarchar(50),
    create_Time      nvarchar(50) not null,
    end_Time         nvarchar(50),
    expire_Time      nvarchar(50),
    priority         tinyint,
    parent_Id        nvarchar(32),
    order_No         nvarchar(50),
    variable         nvarchar(2000)
);

create table wf_hist_task (
    id               nvarchar(32) not null primary key,
    order_Id         nvarchar(32) not null,
    task_Name        nvarchar(100) not null,
    display_Name     nvarchar(200) not null,
    task_Type        tinyint not null,
    perform_Type     tinyint,
    task_State       tinyint not null,
    operator         nvarchar(50),
    create_Time      nvarchar(50) not null,
    finish_Time      nvarchar(50),
    expire_Time      nvarchar(50),
    action_Url       nvarchar(200),
    parent_Task_Id   nvarchar(32),
    variable         nvarchar(2000)
);

create table wf_hist_task_actor (
    task_Id          nvarchar(32) not null,
    actor_Id         nvarchar(50) not null
);

create table wf_surrogate (
    id                nvarchar(32) not null primary key,
    process_Name      nvarchar(100),
    operator          nvarchar(50),
    surrogate         nvarchar(50),
    odate             nvarchar(64),
    sdate             nvarchar(64),
    edate             nvarchar(64),
    state             tinyint
);
create index IDX_SURROGATE_OPERATOR on wf_surrogate (operator);

create table wf_cc_order (
    order_Id        nvarchar(32),
    actor_Id        nvarchar(50),
    creator         nvarchar(50),
    create_Time     nvarchar(50),
    finish_Time     nvarchar(50),
    status          tinyint
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