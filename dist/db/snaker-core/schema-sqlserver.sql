/**流程定义表*/
create table wf_process (
    id               nvarchar(100) primary key not null,
    parent_Id        nvarchar(100),
    name             nvarchar(100),
    display_Name     nvarchar(200),
    type             tinyint,
    query_Url        nvarchar(200),
    instance_Url     nvarchar(200),
    state            tinyint,
    content          varbinary(max),
    version          tinyint
);

/**流程实例表*/
create table wf_order (
    id               nvarchar(100) not null primary key,
    process_Id       nvarchar(100) not null,
    creator          nvarchar(100),
    create_Time      nvarchar(50) not null,
    expire_Time      nvarchar(50),
    last_Update_Time nvarchar(50),
    last_Updator     nvarchar(100),
    priority         tinyint,
    parent_Id        nvarchar(100),
    parent_Node_Name nvarchar(100),
    order_No         nvarchar(100),
    variable         nvarchar(2000),
    version          tinyint
);

/**任务表*/
create table wf_task (
    id               nvarchar(100) not null primary key,
    order_Id         nvarchar(100) not null,
    task_Name        nvarchar(100) not null,
    display_Name     nvarchar(200) not null,
    task_Type        tinyint not null,
    perform_Type     tinyint,
    operator         nvarchar(100),
    create_Time      nvarchar(50) not null,
    finish_Time      nvarchar(50),
    expire_Time      nvarchar(50),
    action_Url       nvarchar(200),
    parent_Task_Id   nvarchar(100),
    variable         nvarchar(2000),
    version          tinyint
);

/**任务参与者表*/
create table wf_task_actor (
    task_Id          nvarchar(100) not null,
    actor_Id         nvarchar(100) not null
);

/**历史流程实例表*/
create table wf_hist_order (
    id               nvarchar(100) not null primary key,
    process_Id       nvarchar(100) not null,
    order_State      tinyint not null,
    creator          nvarchar(100),
    create_Time      nvarchar(50) not null,
    end_Time         nvarchar(50),
    expire_Time      nvarchar(50),
    priority         tinyint,
    parent_Id        nvarchar(100),
    order_No         nvarchar(100),
    variable         nvarchar(2000)
);

/**历史任务表*/
create table wf_hist_task (
    id               nvarchar(100) not null primary key,
    order_Id         nvarchar(100) not null,
    task_Name        nvarchar(100) not null,
    display_Name     nvarchar(200) not null,
    task_Type        tinyint not null,
    perform_Type     tinyint,
    task_State       tinyint not null,
    operator         nvarchar(100),
    create_Time      nvarchar(50) not null,
    finish_Time      nvarchar(50),
    expire_Time      nvarchar(50),
    action_Url       nvarchar(200),
    parent_Task_Id   nvarchar(100),
    variable         nvarchar(2000)
);

/**历史任务参与者表*/
create table wf_hist_task_actor (
    task_Id          nvarchar(100) not null,
    actor_Id         nvarchar(100) not null
);

/**创建索引*/
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

/**增加外键关联*/
alter table WF_TASK_ACTOR
  add constraint FK_TASK_ACTOR_TASKID foreign key (TASK_ID)
  references WF_TASK (ID);
alter table WF_TASK
  add constraint FK_TASK_ORDERID foreign key (ORDER_ID)
  references WF_ORDER (ID);
alter table WF_ORDER
  add constraint FK_ORDER_PARENTID foreign key (PARENT_ID)
  references WF_ORDER (ID);
alter table WF_ORDER
  add constraint FK_ORDER_PROCESSID foreign key (PROCESS_ID)
  references WF_PROCESS (ID);
alter table WF_HIST_TASK_ACTOR
  add constraint FK_HIST_TASKACTOR foreign key (TASK_ID)
  references WF_HIST_TASK (ID);
alter table WF_HIST_TASK
  add constraint FK_HIST_TASK_ORDERID foreign key (ORDER_ID)
  references WF_HIST_ORDER (ID);
alter table WF_HIST_ORDER
  add constraint FK_HIST_ORDER_PARENTID foreign key (PARENT_ID)
  references WF_HIST_ORDER (ID);
alter table WF_HIST_ORDER
  add constraint FK_HIST_ORDER_PROCESSID foreign key (PROCESS_ID)
  references WF_PROCESS (ID);