/**流程定义表*/
create table wf_process (
    id               varchar2(100) primary key not null,
    parent_Id        varchar2(100),
    name             varchar2(100),
    display_Name     varchar2(200),
    type             number(1),
    query_Url        varchar2(200),
    instance_Url     varchar2(200),
    state            number(1),
    content          blob,
    version          number(1)
);
comment on table wf_process is '流程定义表';
comment on column wf_process.id is '主键ID';
comment on column wf_process.parent_Id is '父流程ID';
comment on column wf_process.name is '流程名称';
comment on column wf_process.display_Name is '流程显示名称';
comment on column wf_process.type is '流程类型';
comment on column wf_process.query_Url is '查询url';
comment on column wf_process.instance_Url is '实例url';
comment on column wf_process.state is '流程是否可用';
comment on column wf_process.content is '流程模型定义';
comment on column wf_process.version is '版本';

/**流程实例表*/
create table wf_order (
    id               varchar2(100) not null primary key,
    process_Id       varchar2(100) not null,
    creator          varchar2(100),
    create_Time      varchar2(50) not null,
    expire_Time      varchar2(50),
    last_Update_Time varchar2(50),
    last_Updator     varchar2(100),
    priority         number(1),
    parent_Id        varchar2(100),
    parent_Node_Name varchar2(100),
    order_No         varchar2(100),
    variable         varchar2(2000),
    version          number(1)
);
comment on table wf_order is '流程实例表';
comment on column wf_order.id is '主键ID';
comment on column wf_order.parent_Id is '父流程ID';
comment on column wf_order.process_Id is '流程定义ID';
comment on column wf_order.creator is '发起人';
comment on column wf_order.create_Time is '发起时间';
comment on column wf_order.expire_Time is '期望完成时间';
comment on column wf_order.last_Update_Time is '上次更新时间';
comment on column wf_order.last_Updator is '上次更新人';
comment on column wf_order.priority is '优先级';
comment on column wf_order.parent_Node_Name is '父流程依赖的节点名称';
comment on column wf_order.order_No is '流程实例编号';
comment on column wf_order.variable is '流程实例附属变量';
comment on column wf_order.version is '版本';

/**任务表*/
create table wf_task (
    id               varchar2(100) not null primary key,
    order_Id         varchar2(100) not null,
    task_Name        varchar2(100) not null,
    display_Name     varchar2(200) not null,
    task_Type        number(1) not null,
    perform_Type     number(1),
    operator         varchar2(100),
    create_Time      varchar2(50) not null,
    finish_Time      varchar2(50),
    expire_Time      varchar2(50),
    action_Url       varchar2(200),
    parent_Task_Id   varchar2(100),
    variable         varchar2(2000),
    version          number(1)
);
comment on table wf_task is '任务表';
comment on column wf_task.id is '主键ID';
comment on column wf_task.order_Id is '流程实例ID';
comment on column wf_task.task_Name is '任务名称';
comment on column wf_task.display_Name is '任务显示名称';
comment on column wf_task.task_Type is '任务类型';
comment on column wf_task.perform_Type is '参与类型';
comment on column wf_task.operator is '任务处理人';
comment on column wf_task.create_Time is '任务创建时间';
comment on column wf_task.expire_Time is '任务期望完成时间';
comment on column wf_task.finish_Time is '任务完成时间';
comment on column wf_task.action_Url is '任务处理的url';
comment on column wf_task.parent_Task_Id is '父任务ID';
comment on column wf_task.variable is '附属变量json存储';
comment on column wf_task.version is '版本';

/**任务参与者表*/
create table wf_task_actor (
    task_Id          varchar2(100) not null,
    actor_Id         varchar2(100) not null
);
comment on table wf_task_actor is '任务参与者表';
comment on column wf_task_actor.task_Id is '任务ID';
comment on column wf_task_actor.actor_Id is '参与者ID';


/**历史流程实例表*/
create table wf_hist_order (
    id               varchar2(100) not null primary key,
    process_Id       varchar2(100) not null,
    order_State      number(1) not null,
    creator          varchar2(100),
    create_Time      varchar2(50) not null,
    end_Time         varchar2(50),
    expire_Time      varchar2(50),
    priority         number(1),
    parent_Id        varchar2(100),
    order_No         varchar2(100),
    variable         varchar2(2000)
);
comment on table wf_hist_order is '历史流程实例表';
comment on column wf_hist_order.id is '主键ID';
comment on column wf_hist_order.parent_Id is '父流程ID';
comment on column wf_hist_order.process_Id is '流程定义ID';
comment on column wf_hist_order.order_State is '状态';
comment on column wf_hist_order.priority is '优先级';
comment on column wf_hist_order.creator is '发起人';
comment on column wf_hist_order.create_Time is '发起时间';
comment on column wf_hist_order.expire_Time is '期望完成时间';
comment on column wf_hist_order.end_Time is '完成时间';
comment on column wf_hist_order.order_No is '流程实例编号';
comment on column wf_hist_order.variable is '流程实例附属变量';

/**历史任务表*/
create table wf_hist_task (
    id               varchar2(100) not null primary key,
    order_Id         varchar2(100) not null,
    task_Name        varchar2(100) not null,
    display_Name     varchar2(200) not null,
    task_Type        number(1) not null,
    perform_Type     number(1),
    task_State       number(1) not null,
    operator         varchar2(100),
    create_Time      varchar2(50) not null,
    finish_Time      varchar2(50),
    expire_Time      varchar2(50),
    action_Url       varchar2(200),
    parent_Task_Id   varchar2(100),
    variable         varchar2(2000)
);
comment on table wf_hist_task is '历史任务表';
comment on column wf_hist_task.id is '主键ID';
comment on column wf_hist_task.order_Id is '流程实例ID';
comment on column wf_hist_task.task_Name is '任务名称';
comment on column wf_hist_task.display_Name is '任务显示名称';
comment on column wf_hist_task.task_Type is '任务类型';
comment on column wf_hist_task.perform_Type is '参与类型';
comment on column wf_hist_task.task_State is '任务状态';
comment on column wf_hist_task.operator is '任务处理人';
comment on column wf_hist_task.create_Time is '任务创建时间';
comment on column wf_hist_task.expire_Time is '任务期望完成时间';
comment on column wf_hist_task.finish_Time is '任务完成时间';
comment on column wf_hist_task.action_Url is '任务处理的url';
comment on column wf_hist_task.parent_Task_Id is '父任务ID';
comment on column wf_hist_task.variable is '附属变量json存储';

/**历史任务参与者表*/
create table wf_hist_task_actor (
    task_Id          varchar2(100) not null,
    actor_Id         varchar2(100) not null
);
comment on table wf_hist_task_actor is '历史任务参与者表';
comment on column wf_hist_task_actor.task_Id is '任务ID';
comment on column wf_hist_task_actor.actor_Id is '参与者ID';

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