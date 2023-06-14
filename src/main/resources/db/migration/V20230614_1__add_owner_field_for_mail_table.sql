ALTER TABLE `mails`
    ADD COLUMN `owner` int UNSIGNED NOT NULL COMMENT 'The mail receiver (owner holder)' AFTER `id`;