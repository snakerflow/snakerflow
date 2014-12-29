CREATE TABLE wf_process (
    id                VARCHAR(32) PRIMARY KEY NOT NULL,
    name              VARCHAR(100),
    display_Name      VARCHAR(200),
    type              VARCHAR(100),
    instance_Url      VARCHAR(200),
    state             smallint,
    content           text,
    version           smallint,
    create_Time       VARCHAR(50),
    creator           VARCHAR(50)
);
COMMENT ON TABLE wf_process IS '流程定义表';
COMMENT ON COLUMN wf_process.id IS '主键ID';
COMMENT ON COLUMN wf_process.name IS '流程名称';
COMMENT ON COLUMN wf_process.display_Name IS '流程显示名称';
COMMENT ON COLUMN wf_process.type IS '流程类型';
COMMENT ON COLUMN wf_process.instance_Url IS '实例url';
COMMENT ON COLUMN wf_process.state IS '流程是否可用';
COMMENT ON COLUMN wf_process.content IS '流程模型定义';
COMMENT ON COLUMN wf_process.version IS '版本';

CREATE TABLE wf_order (
    id                VARCHAR(32) NOT NULL PRIMARY KEY,
    parent_Id         VARCHAR(32),
    process_Id        VARCHAR(32) NOT NULL,
    creator           VARCHAR(50),
    create_Time       VARCHAR(50) NOT NULL,
    expire_Time       VARCHAR(50),
    last_Update_Time  VARCHAR(50),
    last_Updator      VARCHAR(50),
    priority          smallint,
    parent_Node_Name  VARCHAR(100),
    order_No          VARCHAR(50),
    variable          VARCHAR(2000),
    version           smallint
);
COMMENT ON TABLE wf_order IS '流程实例表';
COMMENT ON COLUMN wf_order.id IS '主键ID';
COMMENT ON COLUMN wf_order.parent_Id IS '父流程ID';
COMMENT ON COLUMN wf_order.process_Id IS '流程定义ID';
COMMENT ON COLUMN wf_order.creator IS '发起人';
COMMENT ON COLUMN wf_order.create_Time IS '发起时间';
COMMENT ON COLUMN wf_order.expire_Time IS '期望完成时间';
COMMENT ON COLUMN wf_order.last_Update_Time IS '上次更新时间';
COMMENT ON COLUMN wf_order.last_Updator IS '上次更新人';
COMMENT ON COLUMN wf_order.priority IS '优先级';
COMMENT ON COLUMN wf_order.parent_Node_Name IS '父流程依赖的节点名称';
COMMENT ON COLUMN wf_order.order_No IS '流程实例编号';
COMMENT ON COLUMN wf_order.variable IS '附属变量json存储';
COMMENT ON COLUMN wf_order.version IS '版本';

CREATE TABLE wf_task (
    id                VARCHAR(32) NOT NULL PRIMARY KEY,
    order_Id          VARCHAR(32) NOT NULL,
    task_Name         VARCHAR(100) NOT NULL,
    display_Name      VARCHAR(200) NOT NULL,
    task_Type         smallint NOT NULL,
    perform_Type      smallint,
    operator          VARCHAR(50),
    create_Time       VARCHAR(50),
    finish_Time       VARCHAR(50),
    expire_Time       VARCHAR(50),
    action_Url        VARCHAR(200),
    parent_Task_Id    VARCHAR(32),
    variable          VARCHAR(2000),
    version           smallint
);
COMMENT ON TABLE wf_task IS '任务表';
COMMENT ON COLUMN wf_task.id IS '主键ID';
COMMENT ON COLUMN wf_task.order_Id IS '流程实例ID';
COMMENT ON COLUMN wf_task.task_Name IS '任务名称';
COMMENT ON COLUMN wf_task.display_Name IS '任务显示名称';
COMMENT ON COLUMN wf_task.task_Type IS '任务类型';
COMMENT ON COLUMN wf_task.perform_Type IS '参与类型';
COMMENT ON COLUMN wf_task.operator IS '任务处理人';
COMMENT ON COLUMN wf_task.create_Time IS '任务创建时间';
COMMENT ON COLUMN wf_task.finish_Time IS '任务完成时间';
COMMENT ON COLUMN wf_task.expire_Time IS '任务期望完成时间';
COMMENT ON COLUMN wf_task.action_Url IS '任务处理的url';
COMMENT ON COLUMN wf_task.parent_Task_Id IS '父任务ID';
COMMENT ON COLUMN wf_task.variable IS '附属变量json存储';
COMMENT ON COLUMN wf_task.version IS '版本';

CREATE TABLE wf_task_actor (
    task_Id           VARCHAR(32) not null,
    actor_Id          VARCHAR(50) not null
);
COMMENT ON TABLE wf_task_actor IS '任务参与者表';
COMMENT ON COLUMN wf_task_actor.task_Id IS '任务ID';
COMMENT ON COLUMN wf_task_actor.actor_Id IS '参与者ID';

create table wf_hist_order (
    id                VARCHAR(32) not null primary key,
    process_Id        VARCHAR(32) not null,
    order_State       smallint not null,
    creator           VARCHAR(50),
    create_Time       VARCHAR(50) not null,
    end_Time          VARCHAR(50),
    expire_Time       VARCHAR(50),
    priority          smallint,
    parent_Id         VARCHAR(32),
    order_No          VARCHAR(50),
    variable          VARCHAR(2000)
);
COMMENT ON TABLE wf_hist_order IS '历史流程实例表';
COMMENT ON COLUMN wf_hist_order.id IS '主键ID';
COMMENT ON COLUMN wf_hist_order.process_Id IS '流程定义ID';
COMMENT ON COLUMN wf_hist_order.order_State IS '状态';
COMMENT ON COLUMN wf_hist_order.creator IS '发起人';
COMMENT ON COLUMN wf_hist_order.create_Time IS '发起时间';
COMMENT ON COLUMN wf_hist_order.end_Time IS '完成时间';
COMMENT ON COLUMN wf_hist_order.expire_Time IS '期望完成时间';
COMMENT ON COLUMN wf_hist_order.priority IS '优先级';
COMMENT ON COLUMN wf_hist_order.parent_Id IS '父流程ID';
COMMENT ON COLUMN wf_hist_order.order_No IS '流程实例编号';
COMMENT ON COLUMN wf_hist_order.variable IS '附属变量json存储';

create table wf_hist_task (
    id                VARCHAR(32) not null primary key,
    order_Id          VARCHAR(32) not null,
    task_Name         VARCHAR(100) not null,
    display_Name      VARCHAR(200) not null,
    task_Type         smallint not null,
    perform_Type      smallint,
    task_State        smallint not null,
    operator          VARCHAR(50),
    create_Time       VARCHAR(50) not null,
    finish_Time       VARCHAR(50),
    expire_Time       VARCHAR(50),
    action_Url        VARCHAR(200),
    parent_Task_Id    VARCHAR(32),
    variable          VARCHAR(2000)
);
COMMENT ON TABLE wf_hist_task IS '历史任务表';
COMMENT ON COLUMN wf_hist_task.id IS '主键ID';
COMMENT ON COLUMN wf_hist_task.order_Id IS '流程实例ID';
COMMENT ON COLUMN wf_hist_task.task_Name IS '任务名称';
COMMENT ON COLUMN wf_hist_task.display_Name IS '任务显示名称';
COMMENT ON COLUMN wf_hist_task.task_Type IS '任务类型';
COMMENT ON COLUMN wf_hist_task.perform_Type IS '参与类型';
COMMENT ON COLUMN wf_hist_task.task_State IS '任务状态';
COMMENT ON COLUMN wf_hist_task.operator IS '任务处理人';
COMMENT ON COLUMN wf_hist_task.create_Time IS '任务创建时间';
COMMENT ON COLUMN wf_hist_task.finish_Time IS '任务完成时间';
COMMENT ON COLUMN wf_hist_task.expire_Time IS '任务期望完成时间';
COMMENT ON COLUMN wf_hist_task.action_Url IS '任务处理url';
COMMENT ON COLUMN wf_hist_task.parent_Task_Id IS '父任务ID';
COMMENT ON COLUMN wf_hist_task.variable IS '附属变量json存储';

create table wf_hist_task_actor (
    task_Id           VARCHAR(32) not null,
    actor_Id          VARCHAR(50) not null
);
COMMENT ON TABLE wf_hist_task_actor IS '历史任务参与者表';
COMMENT ON COLUMN wf_hist_task_actor.task_Id IS '任务ID';
COMMENT ON COLUMN wf_hist_task_actor.actor_Id IS '参与者ID';

create table wf_surrogate (
    id                VARCHAR(32) not null primary key,
    process_Name      VARCHAR(100),
    operator          VARCHAR(50),
    surrogate         VARCHAR(50),
    odate             VARCHAR(64),
    sdate             VARCHAR(64),
    edate             VARCHAR(64),
    state             smallint
);
COMMENT on table wf_surrogate is '委托代理表';
COMMENT on column wf_surrogate.id is '主键ID';
COMMENT on column wf_surrogate.process_Name is '流程名称';
COMMENT on column wf_surrogate.operator is '授权人';
COMMENT on column wf_surrogate.surrogate is '代理人';
COMMENT on column wf_surrogate.odate is '操作时间';
COMMENT on column wf_surrogate.sdate is '开始时间';
COMMENT on column wf_surrogate.edate is '结束时间';
COMMENT on column wf_surrogate.state is '状态';
create index IDX_SURROGATE_OPERATOR on wf_surrogate (operator);

create table wf_cc_order (
    order_Id        VARCHAR(32),
    actor_Id        VARCHAR(50),
    creator         VARCHAR(50),
    create_Time     VARCHAR(50),
    finish_Time    VARCHAR(50),
    status          smallint
);
COMMENT on table wf_cc_order is '抄送实例表';
COMMENT on column wf_cc_order.order_Id is '流程实例ID';
COMMENT on column wf_cc_order.actor_Id is '参与者ID';
COMMENT on column wf_cc_order.status is '状态';
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