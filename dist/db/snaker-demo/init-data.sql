/**菜单表初始化数据*/
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (1, '', '流程管理', NULL);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (2, '', '功能模块', NULL);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (3, '', '配置管理', NULL);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (4, '', '系统管理', NULL);

INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (11, '', '待办任务', 1);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (12, '', '流程实例', 1);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (13, '', '历史任务', 1);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (14, '', '流程定义', 1);

INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (21, '', '通知公告', 2);

INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (31, '', '数据字典', 3);

INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (41, '', '用户管理', 4);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (42, '', '部门管理', 4);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (43, '', '角色管理', 4);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (44, '', '权限管理', 4);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (45, '', '资源管理', 4);
INSERT INTO sec_menu (ID, DESCRIPTION, NAME, PARENT_MENU) VALUES (46, '', '菜单管理', 4);

/**资源表初始化数据*/
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (101, '待办任务', '/snaker/task/active', 11);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (102, '流程实例', '/snaker/order', 12);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (103, '历史任务', '/snaker/task/history', 13);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (104, '流程定义', '/snaker/process/list', 14);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (105, '流程部署', '/snaker/process/deploy/**;/snaker/process/add/**', NULL);

INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (301, '字典查询', '/config/dictionary', 31);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (302, '字典查看', '/config/dictionary/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (303, '字典编辑', '/config/dictionary/update/**;/config/dictionary/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (304, '字典删除', '/config/dictionary/delete/**', NULL);

INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (401, '用户查询', '/security/user', 41);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (402, '部门查询', '/security/org', 42);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (403, '角色查询', '/security/role', 43);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (404, '权限查询', '/security/authority', 44);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (405, '资源查询', '/security/resource', 45);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (406, '菜单查询', '/security/menu', 46);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (411, '用户查看', '/security/user/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (412, '部门查看', '/security/org/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (413, '角色查看', '/security/role/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (414, '权限查看', '/security/authority/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (415, '资源查看', '/security/resource/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (416, '菜单查看', '/security/menu/view/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (421, '用户删除', '/security/user/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (422, '部门删除', '/security/org/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (423, '角色删除', '/security/role/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (424, '权限删除', '/security/authority/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (425, '资源删除', '/security/resource/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (426, '菜单删除', '/security/menu/delete/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (431, '用户编辑', '/security/user/update/**;/security/user/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (432, '部门编辑', '/security/org/update/**;/security/org/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (433, '角色编辑', '/security/role/update/**;/security/role/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (434, '权限编辑', '/security/authority/update/**;/security/authority/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (435, '资源编辑', '/security/resource/update/**;/security/resource/create/**', NULL);
INSERT INTO sec_resource (ID, NAME, SOURCE, MENU) VALUES (436, '菜单编辑', '/security/menu/update/**;/security/menu/create/**', NULL);

/**权限表初始化数据*/
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (101, '待办任务', 'ACTIVETASK');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (102, '流程实例', 'ORDER');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (103, '历史任务', 'HISTORYTASK');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (104, '流程定义', 'PROCESSLIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (105, '流程部署', 'PROCESSDEPLOY');

INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (301, '字典查询', 'DICTLIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (302, '字典查看', 'DICTVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (303, '字典编辑', 'DICTEDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (304, '字典删除', 'DICTDELETE');

INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (401, '用户查询', 'USERLIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (402, '部门查询', 'ORGLIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (403, '角色查询', 'ROLELIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (404, '权限查询', 'AUTHORITYLIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (405, '资源查询', 'RESOURCELIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (406, '菜单查询', 'MENULIST');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (411, '用户查看', 'USERVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (412, '部门查看', 'ORGVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (413, '角色查看', 'ROLEVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (414, '权限查看', 'AUTHORITYVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (415, '资源查看', 'RESOURCEVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (416, '菜单查看', 'MENUVIEW');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (421, '用户删除', 'USERDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (422, '部门删除', 'ORGDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (423, '角色删除', 'ROLEDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (424, '权限删除', 'AUTHORITYDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (425, '资源删除', 'RESOURCEDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (426, '菜单删除', 'MENUDELETE');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (431, '用户编辑', 'USEREDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (432, '部门编辑', 'ORGEDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (433, '角色编辑', 'ROLEEDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (434, '权限编辑', 'AUTHORITYEDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (435, '资源编辑', 'RESOURCEEDIT');
INSERT INTO sec_authority (ID, DESCRIPTION, NAME) VALUES (436, '菜单编辑', 'MENUEDIT');

/**权限资源关联表初始化数据*/
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (101, 101);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (102, 102);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (103, 103);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (104, 104);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (105, 105);

INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (301, 301);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (302, 302);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (303, 303);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (304, 304);

INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (401, 401);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (402, 402);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (403, 403);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (404, 404);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (405, 405);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (406, 406);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (411, 411);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (412, 412);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (413, 413);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (414, 414);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (415, 415);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (416, 416);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (421, 421);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (422, 422);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (423, 423);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (424, 424);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (425, 425);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (426, 426);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (431, 431);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (432, 432);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (433, 433);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (434, 434);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (435, 435);
INSERT INTO sec_authority_resource (AUTHORITY_ID, RESOURCE_ID) VALUES (436, 436);


INSERT INTO sec_role (ID, DESCRIPTION, NAME) VALUES (1, '系统管理员', 'Admin');
INSERT INTO sec_role (ID, DESCRIPTION, NAME) VALUES (2, '普通用户', 'General');

INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 101);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 102);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 103);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 104);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 105);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 301);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 302);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 303);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 304);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 401);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 402);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 403);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 404);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 405);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 406);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 411);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 412);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 413);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 414);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 415);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 416);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 421);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 422);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 423);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 424);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 425);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 426);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 431);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 432);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 433);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 434);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 435);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (1, 436);

INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 101);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 102);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 103);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 104);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 301);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 302);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 401);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 402);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 403);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 404);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 405);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 406);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 411);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 412);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 413);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 414);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 415);
INSERT INTO sec_role_authority (ROLE_ID, AUTHORITY_ID) VALUES (2, 416);


/**用户表初始化数据*/
INSERT INTO sec_user (ID, ADDRESS, AGE, EMAIL, ENABLED, FULLNAME, PASSWORD, SEX, TYPE, USERNAME, ORG, SALT)
VALUES (1, '', NULL, '', '1', '系统管理员', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', '1', NULL, 'admin', NULL, 'e97e0cea2389225f');
INSERT INTO sec_user (ID, ADDRESS, AGE, EMAIL, ENABLED, FULLNAME, PASSWORD, SEX, TYPE, USERNAME, ORG, SALT)
VALUES (2, '', NULL, '', '1', 'test', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', '1', NULL, 'test', NULL, 'e97e0cea2389225f');
INSERT INTO sec_user (ID, ADDRESS, AGE, EMAIL, ENABLED, FULLNAME, PASSWORD, SEX, TYPE, USERNAME, ORG, SALT)
VALUES (3, '', NULL, '', '1', 'snaker', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', '1', NULL, 'snaker', NULL, 'e97e0cea2389225f');

INSERT INTO sec_role_user (USER_ID, ROLE_ID) VALUES (1, 1);
INSERT INTO sec_role_user (USER_ID, ROLE_ID) VALUES (2, 2);
INSERT INTO sec_role_user (USER_ID, ROLE_ID) VALUES (3, 2);

/**数据字典表初始化数据*/
INSERT INTO conf_dictionary (ID, DESCRIPTION, NAME, CN_NAME) VALUES (1, '', 'yesNo', '是否');
INSERT INTO conf_dictionary (ID, DESCRIPTION, NAME, CN_NAME) VALUES (2, '', 'sex', '性别');

INSERT INTO conf_dictitem (ID, DESCRIPTION, NAME, ORDERBY, DICTIONARY, CODE) VALUES (11, '', '是', 1, 1, '1');
INSERT INTO conf_dictitem (ID, DESCRIPTION, NAME, ORDERBY, DICTIONARY, CODE) VALUES (12, '', '否', 2, 1, '2');
INSERT INTO conf_dictitem (ID, DESCRIPTION, NAME, ORDERBY, DICTIONARY, CODE) VALUES (21, '', '男', 1, 2, '1');
INSERT INTO conf_dictitem (ID, DESCRIPTION, NAME, ORDERBY, DICTIONARY, CODE) VALUES (22, '', '女', 2, 2, '2');

