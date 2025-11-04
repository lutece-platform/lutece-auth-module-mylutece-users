--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'MYLUTECE_USERS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('MYLUTECE_USERS_MANAGEMENT','module.mylutece.users.adminFeature.myLuteceUsersManagement.name',2,'jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceSearchUsers.jsp','module.mylutece.users.adminFeature.myLuteceUsersManagement.description',0,'mylutece-users',NULL,NULL,NULL,4);
--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'MYLUTECE_USERS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('MYLUTECE_USERS_MANAGEMENT',1);
