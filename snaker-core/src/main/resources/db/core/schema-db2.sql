create table wf_process (
    id               VARCHAR(32) primary key not null,
    name             VARCHAR(100),
    display_Name     VARCHAR(200),
    type             VARCHAR(100),
    instance_Url     VARCHAR(200),
    state            SMALLINT(1),
    content          blob,
    version          SMALLINT(2),
    create_Time      VARCHAR(50),
    creator          VARCHAR(50)
);

comment on table wf_process is '流程定义表';
comment on column wf_process.id is '主键ID';
comment on column wf_process.name is '流程名称';
comment on column wf_process.display_Name is '流程显示名称';
comment on column wf_process.type is '流程类型';
comment on column wf_process.instance_Url is '实例url';
comment on column wf_process.state is '流程是否可用';
comment on column wf_process.content is '流程模型定义';
comment on column wf_process.version is '版本';
comment on column wf_process.create_Time is '创建时间';
comment on column wf_process.creator is '创建人';

create table wf_order (
    id               VARCHAR(32) not null primary key,
    process_Id       VARCHAR(32) not null,
    creator          VARCHAR(50),
    create_Time      VARCHAR(50) not null,
    expire_Time      VARCHAR(50),
    last_Update_Time VARCHAR(50),
    last_Updator     VARCHAR(50),
    priority         SMALLINT(1),
    parent_Id        VARCHAR(32),
    parent_Node_Name VARCHAR(100),
    order_No         VARCHAR(50),
    variable         VARCHAR(2000),
    version          SMALLINT(3)
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

create table wf_task (
    id               VARCHAR(32) not null primary key,
    order_Id         VARCHAR(32) not null,
    task_Name        VARCHAR(100) not null,
    display_Name     VARCHAR(200) not null,
    task_Type        SMALLINT(1) not null,
    perform_Type     SMALLINT(1),
    operator         VARCHAR(50),
    create_Time      VARCHAR(50) not null,
    finish_Time      VARCHAR(50),
    expire_Time      VARCHAR(50),
    action_Url       VARCHAR(200),
    parent_Task_Id   VARCHAR(32),
    variable         VARCHAR(2000),
    version          SMALLINT(1)
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

create table wf_task_actor (
    task_Id          VARCHAR(32) not null,
    actor_Id         VARCHAR(50) not null
);
comment on table wf_task_actor is '任务参与者表';
comment on column wf_task_actor.task_Id is '任务ID';
comment on column wf_task_actor.actor_Id is '参与者ID';

create table wf_hist_order (
    id               VARCHAR(32) not null primary key,
    process_Id       VARCHAR(32) not null,
    order_State      SMALLINT(1) not null,
    creator          VARCHAR(50),
    create_Time      VARCHAR(50) not null,
    end_Time         VARCHAR(50),
    expire_Time      VARCHAR(50),
    priority         SMALLINT(1),
    parent_Id        VARCHAR(32),
    order_No         VARCHAR(50),
    variable         VARCHAR(2000)
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

create table wf_hist_task (
    id               VARCHAR(32) not null primary key,
    order_Id         VARCHAR(32) not null,
    task_Name        VARCHAR(100) not null,
    display_Name     VARCHAR(200) not null,
    task_Type        SMALLINT(1) not null,
    perform_Type     SMALLINT(1),
    task_State       SMALLINT(1) not null,
    operator         VARCHAR(50),
    create_Time      VARCHAR(50) not null,
    finish_Time      VARCHAR(50),
    expire_Time      VARCHAR(50),
    action_Url       VARCHAR(200),
    parent_Task_Id   VARCHAR(32),
    variable         VARCHAR(2000)
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

create table wf_hist_task_actor (
    task_Id          VARCHAR(32) not null,
    actor_Id         VARCHAR(50) not null
);
comment on table wf_hist_task_actor is '历史任务参与者表';
comment on column wf_hist_task_actor.task_Id is '任务ID';
comment on column wf_hist_task_actor.actor_Id is '参与者ID';

create table wf_surrogate (
    id                VARCHAR(32) not null primary key,
    process_Name      VARCHAR(100),
    operator          VARCHAR(50),
    surrogate         VARCHAR(50),
    odate             VARCHAR(64),
    sdate             VARCHAR(64),
    edate             VARCHAR(64),
    state             SMALLINT(1)
);
comment on table wf_surrogate is '委托代理表';
comment on column wf_surrogate.id is '主键ID';
comment on column wf_surrogate.process_Name is '流程名称';
comment on column wf_surrogate.operator is '授权人';
comment on column wf_surrogate.surrogate is '代理人';
comment on column wf_surrogate.odate is '操作时间';
comment on column wf_surrogate.sdate is '开始时间';
comment on column wf_surrogate.edate is '结束时间';
comment on column wf_surrogate.state is '状态';
create index IDX_SURROGATE_OPERATOR on wf_surrogate (operator);

create table wf_cc_order (
    order_Id        VARCHAR(32),
    actor_Id        VARCHAR(50),
    creator         VARCHAR(50),
    create_Time     VARCHAR(50),
    finish_Time    VARCHAR(50),
    status          SMALLINT(1)
);
comment on table wf_cc_order is '抄送实例表';
comment on column wf_cc_order.order_Id is '流程实例ID';
comment on column wf_cc_order.actor_Id is '参与者ID';
comment on column wf_cc_order.creator is '发起人';
comment on column wf_cc_order.create_Time is '发起时间';
comment on column wf_cc_order.finish_Time is '完成时间';
comment on column wf_cc_order.status is '状态';
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