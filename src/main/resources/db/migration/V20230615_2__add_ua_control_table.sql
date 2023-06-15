SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `ua_control`
(
    `id`          int UNSIGNED                                                  NOT NULL COMMENT 'The client ua id',
    `user_agent`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The client user agent',
    `match_type`  tinyint UNSIGNED                                              NOT NULL COMMENT 'The match type',
    `is_enabled`  tinyint                                                       NOT NULL COMMENT 'Is this rule has been enabled',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT 'The rule description',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_idx` (`user_agent` ASC, `match_type` ASC) USING BTREE,
    INDEX `is_enabled_idx` (`is_enabled` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;
