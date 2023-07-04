SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcements
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements`  (
                                  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The announcement id\r\n',
                                  `owner` int UNSIGNED NOT NULL COMMENT 'The announcement owner user id',
                                  `added_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The announcement added time',
                                  `ended_at` datetime NOT NULL COMMENT 'The announcement ended time',
                                  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The announcement title',
                                  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The announcement content',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `endtime_index`(`ended_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of announcements
-- ----------------------------
INSERT INTO `announcements` VALUES (1, 1, '2023-07-04 17:29:15', '2156-01-01 17:28:55', 'Default Announcement', 'Thanks for you choose BitSapling!');

-- ----------------------------
-- Table structure for audits
-- ----------------------------
DROP TABLE IF EXISTS `audits`;
CREATE TABLE `audits`  (
                           `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The audit event',
                           `user` int UNSIGNED NULL DEFAULT NULL COMMENT 'The audit event user id',
                           `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The audit event type',
                           `created_at` datetime NOT NULL COMMENT 'The audit event created at',
                           `ip` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The audit event user ip',
                           `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The audit event user agent',
                           `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'The audit event data',
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `audit_user_index`(`user`) USING BTREE,
                           INDEX `audit_type_index`(`event_type`) USING BTREE,
                           INDEX `audit_create_date_index`(`created_at`) USING BTREE,
                           INDEX `audit_ip_index`(`ip`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of audits
-- ----------------------------

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                               `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The category id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category name',
                               `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category icon url',
                               `css_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The custom category title css class name',
                               `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category access permission',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, 'Default Category', 'https://example.com/noimage.jpg', 'category_default', 'category-perm:default');

-- ----------------------------
-- Table structure for exam_plans
-- ----------------------------
DROP TABLE IF EXISTS `exam_plans`;
CREATE TABLE `exam_plans`  (
                               `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The exam plan id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The exam plan name',
                               `duration` bigint UNSIGNED NOT NULL COMMENT 'The exam duration',
                               `target_uploaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target uploaded',
                               `target_downloaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target downloaded',
                               `target_real_uploaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target real uploaded',
                               `target_real_downloaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target real downloaded',
                               `target_karma` bigint UNSIGNED NOT NULL COMMENT 'The exam target target',
                               `target_share_ratio` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'The exam target share ratio',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_plans
-- ----------------------------
INSERT INTO `exam_plans` VALUES (1, 'Default Exam', 1296000000, 16106127360, 16106127360, 0, 0, 0, 0.00);

-- ----------------------------
-- Table structure for exams
-- ----------------------------
DROP TABLE IF EXISTS `exams`;
CREATE TABLE `exams`  (
                          `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The exam entires id',
                          `user` int UNSIGNED NOT NULL COMMENT 'The exam user id',
                          `exam_plan` int UNSIGNED NOT NULL COMMENT 'The exam plan id',
                          `started_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The exam started at',
                          `end_at` datetime NOT NULL COMMENT 'The exam should end at',
                          `status` tinyint UNSIGNED NOT NULL COMMENT 'The exam status',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `user_unique`(`user`) USING BTREE,
                          INDEX `end_at_index`(`end_at`) USING BTREE,
                          INDEX `status_index`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exams
-- ----------------------------

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
                                          `installed_rank` int NOT NULL,
                                          `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                          `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                          `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                          `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                          `checksum` int NULL DEFAULT NULL,
                                          `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                          `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          `execution_time` int NOT NULL,
                                          `success` tinyint NOT NULL,
                                          PRIMARY KEY (`installed_rank`) USING BTREE,
                                          INDEX `flyway_schema_history_s_idx`(`success`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
                           `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The group id',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group name',
                           `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group icon url',
                           `css_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group custom css class name',
                           `promotion` int UNSIGNED NOT NULL COMMENT 'The promotion rule will appy to this group',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `group_name_unique`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES (1, 'Default', 'https://example.com/noimage.jpg', 'group_default', 1);

-- ----------------------------
-- Table structure for login_providers
-- ----------------------------
DROP TABLE IF EXISTS `login_providers`;
CREATE TABLE `login_providers`  (
                                    `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The id',
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The login provider name',
                                    `provider` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The login provider class name',
                                    `enabled` tinyint NOT NULL COMMENT 'The login provider enabled status',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_providers
-- ----------------------------

-- ----------------------------
-- Table structure for mails
-- ----------------------------
DROP TABLE IF EXISTS `mails`;
CREATE TABLE `mails`  (
                          `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The mail id',
                          `owner` int UNSIGNED NOT NULL COMMENT 'The mail receiver (owner holder)',
                          `sender` int UNSIGNED NOT NULL COMMENT 'The mail sender',
                          `sender_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The mail sender name, only works if mail is a system mail',
                          `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The mail title',
                          `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The mail description',
                          `created_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The mail created at',
                          `readed_at` datetime NULL DEFAULT NULL COMMENT 'The mail readed at',
                          `deleted_at` datetime NULL DEFAULT NULL COMMENT 'The mail deleted at',
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `sender_index`(`sender`) USING BTREE,
                          INDEX `readed_at_index`(`readed_at`) USING BTREE,
                          INDEX `title_index`(`title`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mails
-- ----------------------------
INSERT INTO `mails` VALUES (1, 1, 0, 'BitSapling System', 'Welcome to BitSapling Tracker', 'Welcome to BitSapling Tracker!', '2023-07-04 17:34:44', NULL, NULL);

-- ----------------------------
-- Table structure for peers
-- ----------------------------
DROP TABLE IF EXISTS `peers`;
CREATE TABLE `peers`  (
                          `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The peers id',
                          `torrent_id` int UNSIGNED NOT NULL COMMENT 'The torrent id',
                          `peer_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The peer id that generate by BitTorrent client',
                          `user_id` int UNSIGNED NOT NULL COMMENT 'The peer belongs to the user\'s id',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The peer ip address',
  `port` smallint NOT NULL COMMENT 'The peer listening port',
  `uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The peer total uploaded bytes for this torrent',
  `downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The peer total downloaded bytes for this torrent',
  `to_go` bigint UNSIGNED NULL DEFAULT NULL COMMENT 'The bytes of the torrent remains to download',
  `started_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The peer task started at the datetime',
  `last_action` datetime NULL DEFAULT NULL COMMENT 'The peer last action at',
  `prev_action` datetime NULL DEFAULT NULL COMMENT 'The peer previous action at',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The peer\'s client user agent',
                          `finished_at` datetime NOT NULL COMMENT 'The datetime that the peer finished download',
                          `downloaded_offset` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The offset that peer downloaded bytes',
                          `uploaded_offset` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The offset that peer uploaded bytes',
                          `connectable` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Is this peer are connectable with tracker server',
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `torrent_id_index`(`torrent_id`) USING BTREE,
                          INDEX `user_id_index`(`user_id`) USING BTREE,
                          INDEX `peer_unique`(`torrent_id`, `peer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of peers
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
                                `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The permission id',
                                `group_` int NOT NULL COMMENT 'The group id',
                                `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The permission name',
                                `value` tinyint UNSIGNED NOT NULL COMMENT 'The permission status',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `group_permission_index`(`group_`, `permission`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for promotions
-- ----------------------------
DROP TABLE IF EXISTS `promotions`;
CREATE TABLE `promotions`  (
                               `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The promotion id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The promotion name',
                               `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The promotion icon url',
                               `upload_multiplier` decimal(10, 2) NOT NULL COMMENT 'The multiplier of upload',
                               `download_multiplier` decimal(10, 2) NOT NULL COMMENT 'The multiplier of download',
                               `is_default` tinyint UNSIGNED NOT NULL COMMENT 'Is this promotion is global default promotion',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of promotions
-- ----------------------------
INSERT INTO `promotions` VALUES (1, 'Default (no promotion)', 'https://example.com/noimage.jpg', 1.00, 1.00, 1);

-- ----------------------------
-- Table structure for settings
-- ----------------------------
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The setting id',
                             `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The setting key',
                             `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'The setting value',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `key_unique`(`key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of settings
-- ----------------------------
INSERT INTO `settings` VALUES (1, 'general.site_name', 'Another Sapling Tracker');
INSERT INTO `settings` VALUES (2, 'general.site_url', 'http://localhost:8080');
INSERT INTO `settings` VALUES (3, 'general.site_description', 'Upload and share your files! Powered by Sapling Tracker.');
INSERT INTO `settings` VALUES (4, 'general.site_keywords', 'sapling,bittorrent,tracker');
INSERT INTO `settings` VALUES (5, 'tracker.torrent_prefix', '[Sapling]');
INSERT INTO `settings` VALUES (6, 'tracker.min_announce_interval', '56000');
INSERT INTO `settings` VALUES (7, 'tracker.max_announce_interval', '162000');
INSERT INTO `settings` VALUES (8, 'tracker.allow_upload_bittorrent_v1', 'true');
INSERT INTO `settings` VALUES (9, 'tracker.allow_upload_bittorrent_v2', 'true');
INSERT INTO `settings` VALUES (10, 'tracker.allow_bittorrent_mixed', 'true');
INSERT INTO `settings` VALUES (11, 'security.max_login_attempts', '10');
INSERT INTO `settings` VALUES (12, 'security.login_attempt_ban', '900');
INSERT INTO `settings` VALUES (13, 'mail.smtp.host', 'localhost');
INSERT INTO `settings` VALUES (14, 'mail.smtp.port', '25');
INSERT INTO `settings` VALUES (15, 'mail.smtp.user', 'user');
INSERT INTO `settings` VALUES (16, 'mail.smtp.pass', 'passwd');
INSERT INTO `settings` VALUES (17, 'mail.smtp.address', 'noreply@localhost');
INSERT INTO `settings` VALUES (18, 'mail.smtp.encryption', 'plain');
INSERT INTO `settings` VALUES (19, 'mail.enabled', 'true');
INSERT INTO `settings` VALUES (20, 'security.reportemail', 'adminreport@localhost');
INSERT INTO `settings` VALUES (21, 'tracker.max_peers_announce_returns', '300');
INSERT INTO `settings` VALUES (22, 'tracker.trackers', '[https://localhost:8080/announce,https://backuptracker.example.com:8080/announce]');

-- ----------------------------
-- Table structure for storage_files
-- ----------------------------
DROP TABLE IF EXISTS `storage_files`;
CREATE TABLE `storage_files`  (
                                  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The file id',
                                  `provider` int UNSIGNED NOT NULL COMMENT 'The file storage provider',
                                  `identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The file location on storage provider',
                                  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The original file name',
                                  `size` bigint NOT NULL COMMENT 'The file size',
                                  `hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The file hash',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `identifier_provider_unique`(`provider`, `identifier`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storage_files
-- ----------------------------

-- ----------------------------
-- Table structure for storage_providers
-- ----------------------------
DROP TABLE IF EXISTS `storage_providers`;
CREATE TABLE `storage_providers`  (
                                      `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Storage Provider ID',
                                      `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Storage Provider DisplayName',
                                      `storage_driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Storage Driver',
                                      `config` blob NOT NULL COMMENT 'Storage Driver Configuration',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE INDEX `driver_name_index`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storage_providers
-- ----------------------------

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
                         `id` int NOT NULL COMMENT 'The tag id\r\n',
                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The tag name',
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE INDEX `team_name_unique`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tags
-- ----------------------------

-- ----------------------------
-- Table structure for teams
-- ----------------------------
DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams`  (
                          `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The team id',
                          `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The team name',
                          `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The team access permission node',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `team_name_unique`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teams
-- ----------------------------

-- ----------------------------
-- Table structure for torrent_comments
-- ----------------------------
DROP TABLE IF EXISTS `torrent_comments`;
CREATE TABLE `torrent_comments`  (
                                     `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The comment id',
                                     `torrent` int UNSIGNED NOT NULL COMMENT 'The torrent id',
                                     `owner` int UNSIGNED NOT NULL COMMENT 'The comment holder',
                                     `reply_to` int UNSIGNED NULL DEFAULT NULL COMMENT 'The comment is reply to which one comment',
                                     `created_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The comment is created at',
                                     `edited_at` datetime NULL DEFAULT NULL COMMENT 'This comment is edited at',
                                     `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The comment description',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `torrent_index`(`torrent`) USING BTREE,
                                     INDEX `owner_index`(`owner`) USING BTREE,
                                     INDEX `reply_to_index`(`reply_to`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of torrent_comments
-- ----------------------------

-- ----------------------------
-- Table structure for torrent_metadata
-- ----------------------------
DROP TABLE IF EXISTS `torrent_metadata`;
CREATE TABLE `torrent_metadata`  (
                                     `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The torrent metrics id',
                                     `torrent_id` int UNSIGNED NOT NULL COMMENT 'The torrent id',
                                     `seeders` int UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The torrent seeders',
                                     `leechers` int NOT NULL COMMENT 'The torrent leechers',
                                     `times_completed` int NOT NULL COMMENT 'The torrent completed times',
                                     `times_file_downloaded` int NOT NULL COMMENT 'The times of this torrent file downloaded from server',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of torrent_metadata
-- ----------------------------

-- ----------------------------
-- Table structure for torrents
-- ----------------------------
DROP TABLE IF EXISTS `torrents`;
CREATE TABLE `torrents`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The torrent unique id',
                             `info_hash_v1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The torrent info_hash (bittorrent v1)',
                             `info_hash_v2` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The torrent info_hash (bittorrent v2)',
                             `uploader` int UNSIGNED NOT NULL COMMENT 'The torrent uploader id',
                             `file_id` int UNSIGNED NOT NULL COMMENT 'The torrent storage provider id (e.g file)',
                             `is_review` tinyint NOT NULL COMMENT 'Is the torrent under the moderator reviews',
                             `is_banned` tinyint NOT NULL COMMENT 'Is the torrent has been banned',
                             `is_draft` tinyint NOT NULL COMMENT 'Is this torrent is a draft',
                             `added_at` datetime NOT NULL COMMENT 'The torrent added time',
                             `anonymous` tinyint NOT NULL COMMENT 'Is this torrent is a anonymous torrent',
                             `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The torrent title',
                             `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The torrent subtitle',
                             `category` int NOT NULL COMMENT 'The torrent category',
                             `description` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The torrent description',
                             `promotion` int UNSIGNED NOT NULL COMMENT 'The promotion will apply to this torrent',
                             `promotion_end_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The promotion will end at this datetime and set back to default promotion',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `info_hash_v1_unique`(`info_hash_v1`) USING BTREE,
                             UNIQUE INDEX `info_hash_v2_unique`(`info_hash_v2`) USING BTREE,
                             INDEX `title_index`(`title`) USING BTREE,
                             INDEX `subtitle_index`(`subtitle`) USING BTREE,
                             INDEX `uploader_index`(`uploader`) USING BTREE,
                             INDEX `file_id_index`(`file_id`) USING BTREE,
                             INDEX `category_index`(`category`) USING BTREE,
                             INDEX `status_index`(`is_review`, `is_banned`, `is_draft`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of torrents
-- ----------------------------

-- ----------------------------
-- Table structure for ua_control
-- ----------------------------
DROP TABLE IF EXISTS `ua_control`;
CREATE TABLE `ua_control`  (
                               `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The client ua id',
                               `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The client user agent',
                               `match_type` tinyint UNSIGNED NOT NULL COMMENT 'The match type',
                               `is_enabled` tinyint NOT NULL COMMENT 'Is this rule has been enabled',
                               `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The rule description',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `unique_idx`(`user_agent`, `match_type`) USING BTREE,
                               INDEX `is_enabled_idx`(`is_enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ua_control
-- ----------------------------
INSERT INTO `ua_control` VALUES (1, 'Azureus', 1, 1, 'Azureus Family');
INSERT INTO `ua_control` VALUES (2, 'uTorrent', 1, 1, 'uTorrent Family, Not recommend to allow it since it not support newer BitTorrent BEPs');
INSERT INTO `ua_control` VALUES (3, 'Bittorrent', 1, 0, 'Unknown Client, is Annoymous mode has been enabled on client?');
INSERT INTO `ua_control` VALUES (4, 'Deluge', 1, 1, 'Deluge Family');
INSERT INTO `ua_control` VALUES (5, 'Transmission', 1, 1, 'Transmission Family');
INSERT INTO `ua_control` VALUES (6, 'RTorrent', 1, 1, 'RTorrent Family');
INSERT INTO `ua_control` VALUES (7, 'Rufus', 1, 1, 'Rufus Family');
INSERT INTO `ua_control` VALUES (8, 'BitRocket', 1, 1, 'BitRocket Family');
INSERT INTO `ua_control` VALUES (9, 'MLDonkey', 1, 1, 'MLDonkey Family');
INSERT INTO `ua_control` VALUES (10, 'SymTorrent', 1, 1, 'SymTorrent Family');
INSERT INTO `ua_control` VALUES (11, 'qBittorrent', 1, 1, 'qBittorrent Family');
INSERT INTO `ua_control` VALUES (12, 'BitComet', 1, 1, 'BitComet Family');
INSERT INTO `ua_control` VALUES (13, 'Aria', 1, 1, 'Aria2 Family');
INSERT INTO `ua_control` VALUES (14, 'WebTorrent', 1, 1, 'WebTorrent (https://webtorrent.io)');
INSERT INTO `ua_control` VALUES (15, 'Brave', 1, 0, 'WebBrowser that allowed to torrent in browser');
INSERT INTO `ua_control` VALUES (16, 'Vuze', 1, 1, 'Vuze (aka. Azureus)');

-- ----------------------------
-- Table structure for user_metadata
-- ----------------------------
DROP TABLE IF EXISTS `user_metadata`;
CREATE TABLE `user_metadata`  (
                                  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The user metric record id',
                                  `user` int UNSIGNED NOT NULL COMMENT 'The user id',
                                  `downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user downloaded bytes',
                                  `uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user uploaded bytes',
                                  `real_downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real downloaded bytes',
                                  `real_uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real uploaded bytes',
                                  `karma` decimal(30, 2) NOT NULL COMMENT 'The user karma',
                                  `total_seeding_time` bigint UNSIGNED NOT NULL COMMENT 'The user seeding time',
                                  `total_downloading_time` bigint UNSIGNED NOT NULL COMMENT 'The user downloading time',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_metadata
-- ----------------------------
INSERT INTO `user_metadata` VALUES (1, 1, 0, 0, 0, 0, 0.00, 0, 0);

-- ----------------------------
-- Table structure for user_torrent_metadata
-- ----------------------------
DROP TABLE IF EXISTS `user_torrent_metadata`;
CREATE TABLE `user_torrent_metadata`  (
                                          `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The user metric record id',
                                          `user` int UNSIGNED NOT NULL COMMENT 'The user id',
                                          `torrent` int UNSIGNED NOT NULL COMMENT 'The torrent id',
                                          `downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user downloaded bytes',
                                          `uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user uploaded bytes',
                                          `real_downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real downloaded bytes',
                                          `real_uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real uploaded bytes',
                                          `seeding_time` bigint UNSIGNED NOT NULL COMMENT 'The user seeding time',
                                          `downloading_time` bigint UNSIGNED NOT NULL COMMENT 'The user downloading time',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_torrent_metadata
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` int NOT NULL AUTO_INCREMENT COMMENT 'The user identifier',
                          `passkey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user passkey, used for login in BitTorrent client etc.',
                          `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user user name, can used for identification purpose',
                          `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The user display name, not for identification purposes',
                          `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user password hashes',
                          `login_provider` int NULL DEFAULT NULL COMMENT 'The 3rd-party login provider identifier that user used',
                          `login_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The 3rd-party login provider user identifier',
                          `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user email',
                          `email_confirmed` tinyint UNSIGNED NOT NULL COMMENT 'The user email has been confirmed',
                          `group` int NOT NULL COMMENT 'The group that user at',
                          `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The user avatar',
                          `joined_at` datetime NOT NULL COMMENT 'The user register date time',
                          `last_seen_at` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The user login date time of last login',
                          `register_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user ip address of the register',
                          `site_lang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user language that used for this site',
                          `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user bio',
                          `is_banned` tinyint NOT NULL COMMENT 'Is the user has been banned from this site',
                          `preferences` blob NULL COMMENT 'The user preferences',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `users_username_unique`(`username`) USING BTREE,
                          UNIQUE INDEX `users_email_unique`(`email`) USING BTREE,
                          INDEX `users_nickname_index`(`nickname`) USING BTREE,
                          INDEX `users_banned_index`(`is_banned`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '00000000-0000-0000-0000-000000000000', 'system', 'System', '$argon2id$v=19$m=16384,t=2,p=1$XB/H84P2cvao1/w62ds4Zw$szk9oeSDaQE1icSQ0zU2ZhMdhzF6BXMAU5PJZfZfq5M', 0, NULL, 'system@bitsapling.github.io', 1, 1, 'https://example.com/noimage.jpg', '2023-07-04 17:25:10', '2023-07-04 09:36:51', '127.0.0.1', 'en_us', 'Sapling default user', 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
