--
-- Structure for table mylutece_users_localuser
--
DROP TABLE IF EXISTS mylutece_users_localuser;
CREATE TABLE mylutece_users_localuser (
connect_id int AUTO_INCREMENT,
login varchar(255) default '' NOT NULL,
given_name varchar(255) default '' NOT NULL,
last_name varchar(255) default '' NOT NULL,
email varchar(255) default '',
connect_id_provider varchar(255) default '',
PRIMARY KEY (connect_id)
);
--
-- Structure for table mylutece_users_luteceuser_role
--
DROP TABLE IF EXISTS mylutece_users_luteceuser_role;
CREATE TABLE mylutece_users_luteceuser_role (
id_role int AUTO_INCREMENT,
id_localuser int default '0' NOT NULL,
role_key varchar(255) default '' NOT NULL,
PRIMARY KEY (id_role)
);

--
-- Structure for table mylutece_users_attribute_mapping
--
DROP TABLE IF EXISTS mylutece_users_attribute_mapping;
CREATE TABLE mylutece_users_attribute_mapping (
id_mylutece_attribute int default '0' NOT NULL,
id_provider_attribute long varchar NOT NULL,
PRIMARY KEY (id_mylutece_attribute)
);
