DROP TABLE element_library;
DROP SEQUENCE sq_emId;
CREATE TABLE element_library (
  emId NUMBER NOT NULL ,
  labelName varchar2(100) NOT NULL ,
  fieldName varchar2(45) NOT NULL ,
  hiddenFieldName varchar2(45) DEFAULT NULL ,
  functionName varchar2(45) DEFAULT NULL ,
  status varchar2(10) NOT NULL ,
  createdDate DATE NOT NULL,
  grade varchar2(10) NOT NULL ,
  fieldType varchar2(10) DEFAULT '输入框' NOT NULL,
  fieldDataType varchar2(10) DEFAULT '字符串' NOT NULL,
  dataContent varchar2(100) DEFAULT NULL ,
  length NUMBER DEFAULT NULL ,
  functionBelongTo varchar2(10) DEFAULT NULL
);

COMMENT ON COLUMN element_library.emId IS '元素编号';
COMMENT ON COLUMN element_library.labelName IS '元素标签名称';
COMMENT ON COLUMN element_library.fieldName IS '元素字段名称';
COMMENT ON COLUMN element_library.hiddenFieldName IS '元素隐藏字段名';
COMMENT ON COLUMN element_library.functionName IS '元素方法名称';
COMMENT ON COLUMN element_library.status IS '状态';
COMMENT ON COLUMN element_library.createdDate IS '创建时间';
COMMENT ON COLUMN element_library.grade IS '级别（系统级、自定义）';
COMMENT ON COLUMN element_library.fieldType IS '字段类型';
COMMENT ON COLUMN element_library.fieldDataType IS '字段数据类型';
COMMENT ON COLUMN element_library.dataContent IS '值关联表值（适用于下拉框、多选框）';
COMMENT ON COLUMN element_library.length IS '字段长度，仅对字符串类型有效';
COMMENT ON COLUMN element_library.functionBelongTo IS '方法隶属于标签或元素';

ALTER TABLE element_library ADD (CONSTRAINT emId_PK PRIMARY KEY (emId));
CREATE SEQUENCE sq_emId START WITH 100 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;


DROP TABLE sys_configuration;
CREATE TABLE sys_configuration (
  name varchar2(50) NOT NULL,
  value varchar2(100) NOT NULL,
  note varchar2(100) DEFAULT NULL
);
ALTER TABLE sys_configuration ADD (CONSTRAINT name_PK PRIMARY KEY (name));

DROP TABLE table_biz_template;
CREATE TABLE table_biz_template (
  tempId NUMBER NOT NULL,  
  templateName varchar2(50) NOT NULL,
  note varchar2(50) NULL
);

COMMENT ON COLUMN table_biz_template.tempId IS '业务模板编号';
COMMENT ON COLUMN table_biz_template.templateName IS '业务模板名称';
COMMENT ON COLUMN table_biz_template.note IS '备注';

DROP TABLE table_brief;
DROP SEQUENCE sq_tbId;
CREATE TABLE table_brief (
  tbId NUMBER NOT NULL,
  name varchar(30) DEFAULT NULL,
  tableName varchar(50) DEFAULT NULL,
  template varchar(45) DEFAULT '表体' NOT NULL,
  cols NUMBER DEFAULT NULL ,
  status varchar(10) NOT NULL ,
  wfId NUMBER NULL,
  createdDate DATE NOT NULL,
  templateName varchar2(50) NULL,
  ALIAS varchar2(50) NULL
);
COMMENT ON COLUMN table_brief.tbId IS '表单编号';
COMMENT ON COLUMN table_brief.name IS '表单外名称';
COMMENT ON COLUMN table_brief.tableName IS '表单内名称';
COMMENT ON COLUMN table_brief.template IS '表单格式：表单式，bootstrap式';
COMMENT ON COLUMN table_brief.cols IS '表单列数';
COMMENT ON COLUMN table_brief.status IS '表单状态';
COMMENT ON COLUMN table_brief.wfId IS '流程编号';
COMMENT ON COLUMN table_brief.createdDate IS '表单创建时间';
COMMENT ON COLUMN table_brief.templateName IS '业务模板名称';
COMMENT ON COLUMN table_brief.alias IS '表单别名';

ALTER TABLE table_brief ADD (CONSTRAINT tbId_PK PRIMARY KEY (tbId));
CREATE SEQUENCE sq_tbId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE table_layout;
CREATE TABLE table_layout (
  tbId NUMBER NOT NULL,
  scope varchar(10) NOT NULL,  
  cols NUMBER NOT NULL ,
  stbId NUMBER DEFAULT NULL,
  border varchar(2) DEFAULT '是' not null
);
COMMENT ON COLUMN table_layout.tbId IS '表单编号';
COMMENT ON COLUMN table_layout.scope IS '表单区域';
COMMENT ON COLUMN table_layout.stbId IS '子表单编号';
COMMENT ON COLUMN table_layout.cols IS '表单列数';
COMMENT ON COLUMN table_layout.border IS '表单边框';


DROP TABLE table_element;
DROP SEQUENCE sq_id;
CREATE TABLE table_element (
  id NUMBER NOT NULL,
  tbId NUMBER NOT NULL,  
  newLabelName varchar2(100) DEFAULT NULL ,
  newFunctionName varchar2(50) DEFAULT NULL ,
  functionBelongTo varchar2(10) DEFAULT NULL ,
  newHiddenFieldName varchar2(50) DEFAULT NULL ,
  rowes NUMBER DEFAULT '1' NOT NULL ,
  cols NUMBER  DEFAULT '1' NOT NULL ,
  width NUMBER DEFAULT NULL ,
  scope varchar2(10) NOT NULL ,
  formula varchar2(50) DEFAULT NULL ,
  newDataContent varchar2(50) DEFAULT NULL ,
  newFieldType varchar2(10) DEFAULT '输入框' ,
  newFieldDataType varchar2(10) DEFAULT 'String' ,
  seq NUMBER NOT NULL ,
  list varchar2(10) DEFAULT '无效' ,
  constraint varchar2(10) DEFAULT NULL ,
  newLength NUMBER DEFAULT NULL ,
  defaultValue varchar2(45) DEFAULT NULL ,
  defaultValueFrom varchar2(100) DEFAULT NULL ,
  status varchar2(10) DEFAULT '有效' NOT NULL ,
  createdDate DATE NOT NULL,
  stbId NUMBER NULL,
  newFieldName varchar2(50) DEFAULT NULL,
  newUnit varchar2(6) DEFAULT NULL,
  position varchar2(6) DEFAULT NULL
);
COMMENT ON COLUMN table_element.id IS '表单元素编号';
COMMENT ON COLUMN table_element.tbId IS '表单编号';
COMMENT ON COLUMN table_element.newLabelName IS '字段标签名称';
COMMENT ON COLUMN table_element.newFunctionName IS '事件名称';
COMMENT ON COLUMN table_element.functionBelongTo IS '事件方法所属（标签或元素）';
COMMENT ON COLUMN table_element.newHiddenFieldName IS '隐藏项';
COMMENT ON COLUMN table_element.rowes IS '跨行';
COMMENT ON COLUMN table_element.cols IS '跨列';
COMMENT ON COLUMN table_element.width IS '宽度';
COMMENT ON COLUMN table_element.scope IS '位置：表头、表体、表尾';
COMMENT ON COLUMN table_element.formula IS '计算公式';
COMMENT ON COLUMN table_element.newDataContent IS '多选数据的信息';
COMMENT ON COLUMN table_element.newFieldType IS '字段操作类型';
COMMENT ON COLUMN table_element.newFieldDataType IS '字段类型';
COMMENT ON COLUMN table_element.seq IS '顺序号';
COMMENT ON COLUMN table_element.list IS '显示在列表上';
COMMENT ON COLUMN table_element.constraint IS '必输项';
COMMENT ON COLUMN table_element.newLength IS '对字段为String的生效。';
COMMENT ON COLUMN table_element.defaultValue IS '字段默认初始值';
COMMENT ON COLUMN table_element.defaultValueFrom IS '初始值来源，目前仅支持根据指定的类的属性获取';
COMMENT ON COLUMN table_element.status IS '有效';
COMMENT ON COLUMN table_element.createdDate IS '创建时间';
COMMENT ON COLUMN table_element.stbId IS '子表单编号';
COMMENT ON COLUMN table_element.newFieldName IS '字段名称';
COMMENT ON COLUMN table_element.newUnit IS '字段单位';
COMMENT ON COLUMN table_element.position IS '标签位置';

CREATE SEQUENCE sq_id START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE table_summary;
CREATE TABLE table_summary (
  summaryId NUMBER NOT NULL,
  bizId NUMBER NOT NULL ,
  tbId NUMBER NOT NULL ,
  tableName varchar2(30) NOT NULL,
  wfId NUMBER NOT NULL ,
  title varchar2(100) NOT NULL ,
  createdUserId NUMBER NOT NULL ,
  createdUserName varchar2(45) NOT NULL ,
  createdOrgId NUMBER NOT NULL ,
  createdOrgName varchar2(45) NOT NULL ,
  curUserId varchar2(50) DEFAULT NULL ,
  curUserName varchar2(45) DEFAULT NULL ,
  createdDate DATE NOT NULL ,
  modifiedDate DATE DEFAULT NULL ,
  finishedDate DATE DEFAULT NULL ,
  status varchar2(10) NOT NULL ,
  action varchar2(20) DEFAULT NULL,
  defId NUMBER DEFAULT NULL
);
COMMENT ON COLUMN table_summary.summaryId IS '概述编号';
COMMENT ON COLUMN table_summary.bizId IS '业务编号';
COMMENT ON COLUMN table_summary.tbId IS '表单编号';
COMMENT ON COLUMN table_summary.tableName IS '业务表名';
COMMENT ON COLUMN table_summary.wfId IS '流程编号';
COMMENT ON COLUMN table_summary.title IS '业务名称';
COMMENT ON COLUMN table_summary.createdUserId IS '业务创建人编号';
COMMENT ON COLUMN table_summary.createdUserName IS '业务创建人名称';
COMMENT ON COLUMN table_summary.createdOrgId IS '业务创建人所在组织编号';
COMMENT ON COLUMN table_summary.createdOrgName IS '业务创建人所在组织名称';
COMMENT ON COLUMN table_summary.curUserId IS '当前办理人编号';
COMMENT ON COLUMN table_summary.curUserName IS '当前办理人名称';
COMMENT ON COLUMN table_summary.createdDate IS '业务创建时间';
COMMENT ON COLUMN table_summary.modifiedDate IS '业务修改时间';
COMMENT ON COLUMN table_summary.finishedDate IS '业务完成时间';
COMMENT ON COLUMN table_summary.status IS '业务办理状态';
COMMENT ON COLUMN table_summary.action IS '业务触发事件';

ALTER TABLE table_summary ADD (CONSTRAINT summaryId_PK PRIMARY KEY (summaryId));
CREATE SEQUENCE sq_summaryId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;


DROP TABLE table_keys;
CREATE TABLE table_keys (
  tableName varchar2(40) NOT NULL,
  zkey NUMBER NOT NULL,
  stableName varchar2(40) NOT NULL,
  skey NUMBER NOT NULL
);
COMMENT ON COLUMN table_keys.tableName IS '主表名称';
COMMENT ON COLUMN table_keys.zkey IS '主表关键字';
COMMENT ON COLUMN table_keys.stableName IS '子表表名';
COMMENT ON COLUMN table_keys.skey IS '子表关键字';



DROP TABLE workflow;
DROP SEQUENCE sq_wfId;
CREATE TABLE workflow (
  wfId NUMBER NOT NULL,
  wfName varchar2(45) DEFAULT NULL,
  tbId NUMBER DEFAULT NULL ,
  status varchar2(10) NOT NULL,
  createdDate DATE NOT NULL,
  type varchar2(10) NOT NULL
);
COMMENT ON COLUMN workflow.wfId IS '流程序号';
COMMENT ON COLUMN workflow.wfName IS '流程名称';
COMMENT ON COLUMN workflow.tbId IS '表单编号';
COMMENT ON COLUMN workflow.status IS '流程状态';
COMMENT ON COLUMN workflow.createdDate IS '创建时间';
COMMENT ON COLUMN workflow.type IS '流程类型';

ALTER TABLE workflow ADD (CONSTRAINT wfId_PK PRIMARY KEY (wfId));
CREATE SEQUENCE sq_wfId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE workflow_action;
DROP SEQUENCE sq_actionId;
CREATE TABLE workflow_action (
  actionId NUMBER NOT NULL,
  actionName varchar2(45) NOT NULL ,
  actionCodeName varchar2(45) NOT NULL ,
  type varchar2(10) NOT NULL ,
  status varchar2(10) DEFAULT '有效' NOT NULL,
  createdDate DATE NOT NULL
); 
COMMENT ON COLUMN workflow_action.actionId IS '行为编号';
COMMENT ON COLUMN workflow_action.actionName IS '行为名称';
COMMENT ON COLUMN workflow_action.actionCodeName IS '行为代码名称';
COMMENT ON COLUMN workflow_action.type IS '行为类型（流程，行为）';
COMMENT ON COLUMN workflow_action.status IS '状态';
COMMENT ON COLUMN workflow_action.createdDate IS '创建时间';

ALTER TABLE workflow_action ADD (CONSTRAINT action_PK PRIMARY KEY (actionId,actionName));
CREATE SEQUENCE sq_actionId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE workflow_brief;
CREATE TABLE workflow_brief (
  bizId NUMBER NOT NULL ,
  flowId NUMBER NOT NULL ,
  stepId NUMBER DEFAULT NULL ,
  nodeName varchar2(20) NOT NULL ,
  actionName varchar2(20) DEFAULT NULL ,
  dispatchUserId varchar2(50) DEFAULT NULL ,
  unitId NUMBER DEFAULT NULL ,
  createdDate DATE NOT NULL ,
  modifiedDate DATE DEFAULT NULL ,
  finishedDate DATE DEFAULT NULL ,
  wfId NUMBER NOT NULL ,
  status varchar2(20) NOT NULL 
);
COMMENT ON COLUMN workflow_brief.bizId IS '业务序号';
COMMENT ON COLUMN workflow_brief.flowId IS '流程序号';
COMMENT ON COLUMN workflow_brief.stepId IS '流程办理序号';
COMMENT ON COLUMN workflow_brief.nodeName IS '节点名称';
COMMENT ON COLUMN workflow_brief.actionName IS '操作名称';
COMMENT ON COLUMN workflow_brief.dispatchUserId IS '办理人编号';
COMMENT ON COLUMN workflow_brief.unitId IS '办理单位';
COMMENT ON COLUMN workflow_brief.createdDate IS '创建时间';
COMMENT ON COLUMN workflow_brief.modifiedDate IS '修改时间';
COMMENT ON COLUMN workflow_brief.finishedDate IS '完成时间';
COMMENT ON COLUMN workflow_brief.wfId IS '模块编号';
COMMENT ON COLUMN workflow_brief.status IS '状态';

ALTER TABLE workflow_brief ADD (CONSTRAINT flowId_PK PRIMARY KEY (flowId));

DROP TABLE workflow_flow;
DROP SEQUENCE sq_flowId;
CREATE TABLE workflow_flow (
  flowId NUMBER NOT NULL ,
  flowParentId NUMBER DEFAULT NULL,
  nodeName varchar2(20) NOT NULL ,
  actionName varchar2(10) DEFAULT NULL,
  status varchar2(10) NOT NULL ,
  createdDate DATE NOT NULL ,
  finishedDate DATE DEFAULT NULL ,
  timeDiffer NUMBER DEFAULT NULL ,
  bizId NUMBER NOT NULL ,
  wfId NUMBER NOT NULL
);
COMMENT ON COLUMN workflow_flow.flowId IS '流程序号';
COMMENT ON COLUMN workflow_flow.flowParentId IS '流程父序号';
COMMENT ON COLUMN workflow_flow.nodeName IS '节点名称';
COMMENT ON COLUMN workflow_flow.actionName IS '操作名称';
COMMENT ON COLUMN workflow_flow.status IS '状态';
COMMENT ON COLUMN workflow_flow.createdDate IS '收到时间';
COMMENT ON COLUMN workflow_flow.finishedDate IS '结束时间';
COMMENT ON COLUMN workflow_flow.timeDiffer IS '办理时间差';
COMMENT ON COLUMN workflow_flow.bizId IS '业务序号';
COMMENT ON COLUMN workflow_flow.wfId IS '流程编号';

ALTER TABLE workflow_flow ADD (CONSTRAINT wfflowId_PK PRIMARY KEY (flowId));
CREATE SEQUENCE sq_flowId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE workflow_node;
DROP SEQUENCE sq_nodeId;
CREATE TABLE workflow_node (
  nodeId NUMBER NOT NULL ,
  nodeName varchar2(20) NOT NULL ,
  timeLimit NUMBER DEFAULT NULL ,
  uType varchar2(10) NOT NULL ,
  nType varchar2(10) NOT NULL ,
  status varchar2(10) NOT NULL ,
  createdDate DATE NOT NULL ,
  modifiedDate DATE NOT NULL ,
  wfId NUMBER DEFAULT '1' NOT NULL  
);
COMMENT ON COLUMN workflow_node.nodeId IS '节点序号';
COMMENT ON COLUMN workflow_node.nodeName IS '节点名称';
COMMENT ON COLUMN workflow_node.timeLimit IS '节点时限';
COMMENT ON COLUMN workflow_node.uType IS '节点操作的类型：角色、用户';
COMMENT ON COLUMN workflow_node.nType IS '节点类型（并行、串行等）';
COMMENT ON COLUMN workflow_node.status IS '节点状态';
COMMENT ON COLUMN workflow_node.createdDate IS '创建时间';
COMMENT ON COLUMN workflow_node.modifiedDate IS '修改时间';
COMMENT ON COLUMN workflow_node.wfId IS '业务类型，默认1。';

ALTER TABLE workflow_node ADD (CONSTRAINT nodeId_PK PRIMARY KEY (nodeId));
CREATE SEQUENCE sq_nodeId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE workflow_node_action;
CREATE TABLE workflow_node_action (
  nodeId NUMBER NOT NULL ,
  actionCodeName varchar2(45) NOT NULL ,
  type varchar2(10) NOT NULL ,
  createdDate DATE NOT NULL
);
COMMENT ON COLUMN workflow_node_action.nodeId IS '节点编号';
COMMENT ON COLUMN workflow_node_action.actionCodeName IS '流程操作';
COMMENT ON COLUMN workflow_node_action.type IS '操作类型';
COMMENT ON COLUMN workflow_node_action.createdDate IS '创建时间';

DROP TABLE workflow_node_nodes;
CREATE TABLE workflow_node_nodes (
  nodeId NUMBER NOT NULL ,
  suf_nodeId NUMBER NOT NULL ,
  type varchar2(10) DEFAULT '直接连接' NOT NULL,
  necessary varchar2(2) DEFAULT '否' NOT NULL,
  createdDate DATE NOT NULL
);
COMMENT ON COLUMN workflow_node_nodes.nodeId IS '节点编号';
COMMENT ON COLUMN workflow_node_nodes.suf_nodeId IS '后续节点编号';
COMMENT ON COLUMN workflow_node_nodes.type IS '节点间连接的方式：直接连接、间接连接';
COMMENT ON COLUMN workflow_node_nodes.necessary IS '必经节点';
COMMENT ON COLUMN workflow_node_nodes.createdDate IS '创建时间';

DROP TABLE workflow_node_role;
CREATE TABLE workflow_node_role (
  nodeId NUMBER NOT NULL ,
  roleId NUMBER NOT NULL ,
  roleName varchar2(45) NOT NULL ,
  unitId NUMBER DEFAULT NULL ,
  unitName varchar2(45) DEFAULT NULL ,
  createdDate DATE NOT NULL
);
COMMENT ON COLUMN workflow_node_role.nodeId IS '节点编号';
COMMENT ON COLUMN workflow_node_role.roleId IS '角色编号';
COMMENT ON COLUMN workflow_node_role.roleName IS '角色名称';
COMMENT ON COLUMN workflow_node_role.unitId IS '角色所在单位编号';
COMMENT ON COLUMN workflow_node_role.unitName IS '角色所在单位名称';
COMMENT ON COLUMN workflow_node_role.createdDate IS '创建时间';

ALTER TABLE workflow_node_role ADD (CONSTRAINT wfnoderole_nodeId_PK PRIMARY KEY (nodeId));

DROP TABLE workflow_node_user;
CREATE TABLE workflow_node_user (
  nodeId NUMBER NOT NULL ,
  userId NUMBER NOT NULL ,
  userName varchar2(20) NOT NULL ,
  unitId NUMBER DEFAULT NULL ,
  unitName varchar2(45) DEFAULT NULL ,
  createdDate DATE NOT NULL 
);
COMMENT ON COLUMN workflow_node_user.nodeId IS '节点编号';
COMMENT ON COLUMN workflow_node_user.userId IS '操作用户（包括角色、用户、单位）';
COMMENT ON COLUMN workflow_node_user.userName IS '用户名称';
COMMENT ON COLUMN workflow_node_user.unitId IS '单位编号';
COMMENT ON COLUMN workflow_node_user.unitName IS '单位名称';
COMMENT ON COLUMN workflow_node_user.createdDate IS '创建时间';

DROP TABLE workflow_step;
DROP SEQUENCE sq_stepId;
CREATE TABLE workflow_step (
  stepId NUMBER NOT NULL ,
  flowId NUMBER NOT NULL ,
  dispatchUserId NUMBER DEFAULT NULL ,
  dispatchUserName varchar2(20) DEFAULT NULL ,
  unitId NUMBER NOT NULL ,
  unitName varchar2(50) NOT NULL ,
  actionName varchar2(20) NOT NULL ,
  status varchar2(10) NOT NULL ,
  createdDate DATE NOT NULL ,
  finishedDate DATE DEFAULT NULL ,
  timeDiffer NUMBER DEFAULT NULL ,
  serial NUMBER DEFAULT '0' NOT NULL ,
  executeUserName varchar2(20) DEFAULT NULL ,
  executeUserId NUMBER DEFAULT NULL,
  reason varchar2(100) DEFAULT NULL
);
COMMENT ON COLUMN workflow_step.stepId IS '流程操作序号';
COMMENT ON COLUMN workflow_step.flowId IS '流程序号';
COMMENT ON COLUMN workflow_step.dispatchUserId IS '员工号';
COMMENT ON COLUMN workflow_step.dispatchUserName IS '员工名称';
COMMENT ON COLUMN workflow_step.unitId IS '组织序号';
COMMENT ON COLUMN workflow_step.unitName IS '组织名称';
COMMENT ON COLUMN workflow_step.actionName IS '操作名称';
COMMENT ON COLUMN workflow_step.status IS '状态';
COMMENT ON COLUMN workflow_step.createdDate IS '收到时间';
COMMENT ON COLUMN workflow_step.finishedDate IS '结束时间';
COMMENT ON COLUMN workflow_step.timeDiffer IS '办理时间差';
COMMENT ON COLUMN workflow_step.serial IS '业务序号';
COMMENT ON COLUMN workflow_step.executeUserName IS '执行员工姓名';
COMMENT ON COLUMN workflow_step.executeUserId IS '执行员工号';
COMMENT ON COLUMN workflow_step.reason IS '推进理由';

ALTER TABLE workflow_step ADD (CONSTRAINT stepId_PK PRIMARY KEY (stepId));
CREATE SEQUENCE sq_stepId START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE workflow_table_element;
CREATE TABLE workflow_table_element (
  wfId NUMBER NOT NULL,
  nodeId NUMBER NOT NULL,
  nodeName varchar2(20) NOT NULL,
  id NUMBER NOT NULL,
  required varchar2(5) NULL
);

DROP TABLE workflow_comment;
CREATE TABLE workflow_comment (
  tbId NUMBER NOT NULL,
  bizId NUMBER NOT NULL,
  fieldName varchar2(20) NOT NULL,
  remark varchar2(200) NOT NULL,
  remarkDate varchar2(20) NOT NULL,
  userId NUMBER NOT NULL,
  userName varchar2(20) not null
);

COMMENT ON COLUMN workflow_comment.tbId IS '表单编号';
COMMENT ON COLUMN workflow_comment.bizId IS '业务数据编号';
COMMENT ON COLUMN workflow_comment.fieldName IS '字段名称';
COMMENT ON COLUMN workflow_comment.remark IS '审批意见';
COMMENT ON COLUMN workflow_comment.remarkDate IS '审批时间';
COMMENT ON COLUMN workflow_comment.userId IS '审批人';
COMMENT ON COLUMN workflow_comment.userName IS '审批人';

DROP TABLE table_user_defination;
CREATE TABLE table_user_defination (
  defId number not null,
  tbId number NOT NULL,
  wfId number NOT NULL ,
  userId number NOT NULL ,
  createdDate DATE NOT NULL ,
  tableName VARCHAR2(50) NOT NULL ,
  notification1 VARCHAR2(10) NULL ,
  notification2 VARCHAR2(10) NULL ,
  notification3 VARCHAR2(10) NULL ,
  action1 VARCHAR2(10) NULL ,
  action2 VARCHAR2(10) NULL ,
  action3 VARCHAR2(10) NULL ,
  type varchar2(20) null
 );

COMMENT ON COLUMN table_user_defination.defId IS '自定义编号';
COMMENT ON COLUMN table_user_defination.tbId IS '表单编号';
COMMENT ON COLUMN table_user_defination.wfId IS '流程编号';
COMMENT ON COLUMN table_user_defination.userId IS '自定义用户编号';
COMMENT ON COLUMN table_user_defination.createdDate IS '创建时间';
COMMENT ON COLUMN table_user_defination.tableName IS '表单名称';
COMMENT ON COLUMN table_user_defination.notification1 IS '提醒方式1';
COMMENT ON COLUMN table_user_defination.notification2 IS '提醒方式2';
COMMENT ON COLUMN table_user_defination.notification3 IS '提醒方式3';
COMMENT ON COLUMN table_user_defination.action1 IS '协同操作1';
COMMENT ON COLUMN table_user_defination.action2 IS '协同操作2';
COMMENT ON COLUMN table_user_defination.action3 IS '协同操作3';
COMMENT ON COLUMN table_user_defination.type IS '业务类型：使用默认流程,设置自定义流程,不使用流程';

ALTER TABLE table_user_defination ADD (CONSTRAINT defId_PK PRIMARY KEY (defId));
CREATE SEQUENCE sq_defId START WITH 100 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

DROP TABLE table_authority;
CREATE TABLE table_authority (
  tbId INT NOT NULL,
  userId INT NOT NULL ,
  createdDate timestamp NOT NULL
);
COMMENT ON COLUMN table_authority.tbId IS '业务表单授权'; 
COMMENT ON COLUMN table_authority.userId is '业务表单编号';
COMMENT ON COLUMN table_authority.createdDate is '用户编号';

--分发
DROP TABLE workflow_dispenser;
create table workflow_dispenser
(
  DISPENSEID       NUMBER not null,
  TYPEID           NUMBER not null,
  TBID             NUMBER not null,
  BIZID            NUMBER NOT NULL,
  WFID             NUMBER NOT NULL,
  DISPENSEUSERID   NUMBER not null,
  DISPENSEUSERNAME VARCHAR2(20) not null,
  RECEIVEUSERIDS   VARCHAR2(200) not null,
  RECEIVEUSERNAMES VARCHAR2(200) not null,
  TYPE             VARCHAR2(6) not null,
  CONTENT          VARCHAR2(100) null,
  CREATEDDATE      DATE not null
);

comment on column workflow_dispenser.DISPENSEID is '分发编号';
comment on column workflow_dispenser.TYPEID is '流程中传阅记录流程id，传阅中传阅记录传阅id';
comment on column workflow_dispenser.TBID is '表单编号';
comment on column workflow_dispenser.BIZID is '业务编号';
comment on column workflow_dispenser.WFID is '流程编号';
comment on column workflow_dispenser.DISPENSEUSERID is '分发者';
comment on column workflow_dispenser.RECEIVEUSERIDS is '接收者';
comment on column workflow_dispenser.TYPE  is '分发类型：流程中，传阅中';
comment on column workflow_dispenser.CONTENT is '留言';
comment on column workflow_dispenser.CREATEDDATE is '分发时间';
comment on column workflow_dispenser.DISPENSEUSERNAME is '分发者名称';
comment on column workflow_dispenser.RECEIVEUSERNAMES is '接收者名称';

ALTER TABLE workflow_dispenser ADD (CONSTRAINT dispenseId_PK PRIMARY KEY (dispenseId));
CREATE SEQUENCE sq_dispenseId START WITH 100 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER;

--传阅的接收人
DROP TABLE workflow_receiver;
create table workflow_receiver
(
  DISPENSEID      NUMBER not null,    
  RECEIVEUSERID   NUMBER not null,
  RECEIVEUSERNAME VARCHAR2(20) not null,
  CONTENT         VARCHAR2(100) null,
  CREATEDDATE     DATE not null
);

comment on column workflow_receiver.DISPENSEID is '分发编号';
comment on column workflow_receiver.RECEIVEUSERID is '接收者编号';
comment on column workflow_receiver.CONTENT is '留言';
comment on column workflow_receiver.CREATEDDATE is '接收时间';
comment on column workflow_receiver.RECEIVEUSERNAME is '接收者名称';

INSERT INTO element_library VALUES 
(1,'办理状态','action',NULL,NULL,'有效',sysdate,'列表级','列表','String',NULL,NULL,NULL);
INSERT INTO element_library VALUES 
(2,'办理人','curusername',NULL,NULL,'有效',sysdate,'列表级','列表','String',NULL,NULL,NULL);
INSERT INTO element_library VALUES 
(3,'办理时间','modifieddate',NULL,NULL,'有效',sysdate,'列表级','列表','String',NULL,NULL,NULL);

INSERT INTO workflow_action VALUES 
(1,'流程推进','PassAction','流程','有效',sysdate);
INSERT INTO workflow_action VALUES 
(2,'流程办结','EndAction','流程','有效',sysdate);
INSERT INTO workflow_action VALUES 
(3,'流程暂缓','PostPhoneAction','行为','有效',sysdate);
INSERT INTO workflow_action VALUES 
(4,'退回发起人','ReturnAction','行为消息','有效',sysdate);
INSERT INTO workflow_action VALUES 
(5,'流程终止','TerminateAction','行为','有效',sysdate);
INSERT INTO workflow_action VALUES 
(6,'调度到他人','DispatchAction','行为','有效',sysdate);
INSERT INTO workflow_action VALUES 
(7,'流程收回','CallbackAction','行为','有效',sysdate);
INSERT INTO workflow_action VALUES 
(8,'退回到创建人','ReturnToCreatorAction','行为消息','有效',sysdate);
INSERT INTO workflow_action VALUES 
(9,'传阅','DispenseAction','应答','有效',sysdate);
INSERT INTO workflow_action VALUES 
(10,'返回发起人','AutoBackAction','流程','有效',sysdate);

INSERT INTO table_biz_template VALUES(1,'财务类',null);
INSERT INTO table_biz_template VALUES(2,'车辆类',null);
INSERT INTO table_biz_template VALUES(3,'人事类',null);
INSERT INTO table_biz_template VALUES(4,'请假类',null);
INSERT INTO table_biz_template VALUES(5,'活动类',null);