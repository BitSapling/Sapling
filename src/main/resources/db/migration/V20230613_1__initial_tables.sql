SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcements
-- ----------------------------
CREATE TABLE `announcements`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The announcement id\r\n',
  `owner` int UNSIGNED NOT NULL COMMENT 'The announcement owner user id',
  `added_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The announcement added time',
  `ended_at` datetime NOT NULL COMMENT 'The announcement ended time',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The announcement title',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The announcement content',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `endtime_index`(`ended_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for audits
-- ----------------------------
CREATE TABLE `audits`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The audit event',
  `user` int UNSIGNED NULL DEFAULT NULL COMMENT 'The audit event user id',
  `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The audit event type',
  `created_at` datetime NOT NULL COMMENT 'The audit event created at',
  `ip` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The audit event user ip',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The audit event user agent',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'The audit event data',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `audit_user_index`(`user` ASC) USING BTREE,
  INDEX `audit_type_index`(`event_type` ASC) USING BTREE,
  INDEX `audit_create_date_index`(`created_at` ASC) USING BTREE,
  INDEX `audit_ip_index`(`ip` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
CREATE TABLE `categories`  (
  `id` int UNSIGNED NOT NULL COMMENT 'The category id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category name',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category icon url',
  `css_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The custom category title css class name',
  `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The category access permission',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exam_plans
-- ----------------------------
CREATE TABLE `exam_plans`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The exam plan id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The exam plan name',
  `duration` bigint UNSIGNED NOT NULL COMMENT 'The exam duration',
  `target_uploaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target uploaded',
  `target_downloaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target downloaded',
  `target_real_uploaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target real uploaded',
  `target_real_downloaded` bigint UNSIGNED NOT NULL COMMENT 'The exam target real downloaded',
  `target_karma` bigint UNSIGNED NOT NULL COMMENT 'The exam target target',
  `target_share_ratio` decimal(10, 2)  NOT NULL DEFAULT 0.00 COMMENT 'The exam target share ratio',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exams
-- ----------------------------
CREATE TABLE `exams`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The exam entires id',
  `user` int UNSIGNED NOT NULL COMMENT 'The exam user id',
  `exam_plan` int UNSIGNED NOT NULL COMMENT 'The exam plan id',
  `started_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The exam started at',
  `end_at` datetime NOT NULL COMMENT 'The exam should end at',
  `status` tinyint UNSIGNED NOT NULL COMMENT 'The exam status',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_unique`(`user` ASC) USING BTREE,
  INDEX `end_at_index`(`end_at` ASC) USING BTREE,
  INDEX `status_index`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
CREATE TABLE `groups`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The group id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group name',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group icon url',
  `css_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The group custom css class name',
  `promotion` int UNSIGNED NOT NULL COMMENT 'The promotion rule will appy to this group',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mails
-- ----------------------------
CREATE TABLE `mails`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The mail id',
  `sender` int UNSIGNED NOT NULL COMMENT 'The mail sender',
  `sender_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The mail sender name, only works if mail is a system mail',
  `direction` tinyint NOT NULL COMMENT 'The mail direction',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The mail title',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The mail description',
  `created_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The mail created at',
  `readed_at` datetime NULL DEFAULT NULL COMMENT 'The mail readed at',
  `deleted_at` datetime NULL DEFAULT NULL COMMENT 'The mail deleted at',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sender_index`(`sender` ASC) USING BTREE,
  INDEX `readed_at_index`(`readed_at` ASC) USING BTREE,
  INDEX `title_index`(`title` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for peers
-- ----------------------------
CREATE TABLE `peers`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The peers id',
  `torrent_id` int UNSIGNED NOT NULL COMMENT 'The torrent id',
  `peer_id` binary(20) NOT NULL COMMENT 'The peer id that generate by BitTorrent client',
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
  INDEX `torrent_id_index`(`torrent_id` ASC) USING BTREE,
  INDEX `user_id_index`(`user_id` ASC) USING BTREE,
  INDEX `peer_unique`(`torrent_id` ASC, `peer_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
CREATE TABLE `permissions`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The permission id',
  `group` int NOT NULL COMMENT 'The group id',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The permission name',
  `value` tinyint UNSIGNED NOT NULL COMMENT 'The permission status',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_permission_index`(`group` ASC, `permission` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for promotions
-- ----------------------------
CREATE TABLE `promotions`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The promotion id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The promotion name',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The promotion icon url',
  `upload_multiplier` decimal(10, 2) NOT NULL COMMENT 'The multiplier of upload',
  `download_multiplier` decimal(10, 2) NOT NULL COMMENT 'The multiplier of download',
  `is_default` tinyint UNSIGNED NOT NULL COMMENT 'Is this promotion is global default promotion',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settings
-- ----------------------------
CREATE TABLE `settings`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The setting id',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The setting key',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'The setting value',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key_unique`(`key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for storage_files
-- ----------------------------
CREATE TABLE `storage_files`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The file id',
  `provider` int UNSIGNED NOT NULL COMMENT 'The file storage provider',
  `identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The file location on storage provider',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The original file name',
  `size` bigint NOT NULL COMMENT 'The file size',
  `hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The file hash',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `identifier_provider_unique`(`provider` ASC, `identifier` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for storage_providers
-- ----------------------------
CREATE TABLE `storage_providers`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Storage Provider ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Storage Provider DisplayName',
  `storage_driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Storage Driver',
  `config` blob NOT NULL COMMENT 'Storage Driver Configuration',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `driver_name_index`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
CREATE TABLE `tags`  (
  `id` int NOT NULL COMMENT 'The tag id\r\n',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The tag name',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `team_name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for teams
-- ----------------------------
CREATE TABLE `teams`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The team id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The team name',
  `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The team access permission node',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `team_name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for torrent_comments
-- ----------------------------
CREATE TABLE `torrent_comments`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The comment id',
  `torrent` int UNSIGNED NOT NULL COMMENT 'The torrent id',
  `owner` int UNSIGNED NOT NULL COMMENT 'The comment holder',
  `reply_to` int UNSIGNED NULL DEFAULT NULL COMMENT 'The comment is reply to which one comment',
  `created_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The comment is created at',
  `edited_at` datetime NULL DEFAULT NULL COMMENT 'This comment is edited at',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The comment description',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `torrent_index`(`torrent` ASC) USING BTREE,
  INDEX `owner_index`(`owner` ASC) USING BTREE,
  INDEX `reply_to_index`(`reply_to` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for torrent_metadata
-- ----------------------------
CREATE TABLE `torrent_metadata`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The torrent metrics id',
  `torrent_id` int UNSIGNED NOT NULL COMMENT 'The torrent id',
  `seeders` int UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The torrent seeders',
  `leechers` int NOT NULL COMMENT 'The torrent leechers',
  `times_completed` int NOT NULL COMMENT 'The torrent completed times',
  `times_file_downloaded` int NOT NULL COMMENT 'The times of this torrent file downloaded from server',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for torrents
-- ----------------------------
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
  UNIQUE INDEX `info_hash_v1_unique`(`info_hash_v1` ASC) USING BTREE,
  UNIQUE INDEX `info_hash_v2_unique`(`info_hash_v2` ASC) USING BTREE,
  INDEX `title_index`(`title` ASC) USING BTREE,
  INDEX `subtitle_index`(`subtitle` ASC) USING BTREE,
  INDEX `uploader_index`(`uploader` ASC) USING BTREE,
  INDEX `file_id_index`(`file_id` ASC) USING BTREE,
  INDEX `category_index`(`category` ASC) USING BTREE,
  INDEX `status_index`(`is_review` ASC, `is_banned` ASC, `is_draft` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_metadata
-- ----------------------------
CREATE TABLE `user_metadata`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'The user metric record id',
  `user` int UNSIGNED NOT NULL COMMENT 'The user id',
  `downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user downloaded bytes',
  `uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user uploaded bytes',
  `real_downloaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real downloaded bytes',
  `real_uploaded` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The user real uploaded bytes',
  `karma` decimal(30, 2)  NOT NULL COMMENT 'The user karma',
  `total_seeding_time` bigint UNSIGNED NOT NULL COMMENT 'The user seeding time',
  `total_downloading_time` bigint UNSIGNED NOT NULL COMMENT 'The user downloading time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_torrent_metadata
-- ----------------------------
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
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
  `group` int UNSIGNED NOT NULL COMMENT 'The group that user at',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'The user avatar',
  `joined_at` datetime NOT NULL COMMENT 'The user register date time',
  `last_seen_at` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'The user login date time of last login',
  `register_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user ip address of the register',
  `site_lang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user language that used for this site',
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The user bio',
  `is_banned` tinyint NOT NULL COMMENT 'Is the user has been banned from this site',
  `preferences` blob NULL COMMENT 'The user preferences',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `users_username_unique`(`username` ASC) USING BTREE,
  UNIQUE INDEX `users_email_unique`(`email` ASC) USING BTREE,
  INDEX `users_nickname_index`(`nickname` ASC) USING BTREE,
  INDEX `users_banned_index`(`is_banned` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
