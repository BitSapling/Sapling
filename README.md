# Sapling

## Tracker Demo

我们在 Vultr 5$/mo 计划上运行了一个 BitSapling Tracker Demo，您可以在下方的页面查看其运行情况。

http://sapling.ghostchu-services.top/

**Demo站主要用于测试 WebUI，因此使用的版本可能并不是最新的，可能和 API 文档对不上！**

**因开发测试需要，Demo 站经常会清空数据，因此不要真的当 PT 使用！**

## 关于 BitSapling

BitSapling 是一个使用 Java 语言基于 Spring Boot 的 BitTorrent Private Tracker。此程序旨在创建一个安全，高性能的 PT 程序，
以代替老旧的 NexusPHP。

尽管目前该项目很年轻，但我们正在逐步完善其功能。  
项目目前处于：<b>理论验证阶段</b>，请勿在生产环境中使用。

## 运行环境要求

* Web 服务器: Nginx
* 缓存: Redis 3.0+
* 数据库: MySQL 5.7+/MariaDB 10.2+/PostgreSQL 9.4+
* Java: 17+

## API 端点

BitSapling 是一个典型的前后端分离设计，所有的操作均通过 JsonAPI 交互完成。  
默认情况下，API 端点位于 `/api` 下，例如：`http://localhost:8081/api/user/login`。

## BEP 进展

### 已实现的 BitTorrent BEP

* [BEP 0003 - The BitTorrent Protocol Specification](http://bittorrent.org/beps/bep_0003.html)
* [BEP 0007 - IPv6 Tracker Extension](http://bittorrent.org/beps/bep_0007.html)
* [BEP 0012 - Multitracker Metadata Extension](http://bittorrent.org/beps/bep_0012.html)
* [BEP 0021 - Extension for partial seeds](https://www.bittorrent.org/beps/bep_0021.html)
* [BEP 0023 - Tracker Returns Compact Peer Lists](http://bittorrent.org/beps/bep_0023.html)
* [BEP 0027 - Private Torrents](http://bittorrent.org/beps/bep_0027.html)
* [BEP 0031 - Failure Retry Extension](https://www.bittorrent.org/beps/bep_0031.html)
* [BEP 0036 - Torrent RSS feeds](https://www.bittorrent.org/beps/bep_0036.html)
* [BEP 0048 - Tracker Protocol Extension: Scrape](https://www.bittorrent.org/beps/bep_0048.html)

## 正在着手实现的 BEP

* [BEP 0047 - Padding files and extended file attributes](https://www.bittorrent.org/beps/bep_0047.html)
* [BEP 0052 - The BitTorrent Protocol Specification v2](http://bittorrent.org/beps/bep_0052.html)

## 以下 BEP 将不被考虑，除非有大量用户确实需要

* [BEP 0008 - Tracker Peer Obfuscation](http://bittorrent.org/beps/bep_0008.html)
* [BEP 0053 - Magnet URI extension - Select specific file indices for download](http://bittorrent.org/beps/bep_0053.html)
* [BEP 0019 - WebSeed - HTTP/FTP Seeding (GetRight style)](https://www.bittorrent.org/beps/bep_0019.html)
* [BEP 0049 - Distributed Torrent Feeds](https://www.bittorrent.org/beps/bep_0049.html)

## 功能TODO

* [x] Tracker
    * [x] 种子上传
    * [x] 种子注册
    * [x] Peers 追踪 (completed, incomplete, downloaders, finishes)
    * [x] 上传/下载 统计和计算
    * [x] 私有种子
    * [x] IPV6 支持
    * [x] 种子促销
        * [x] 自定义促销规则
        * [ ] 条件自动促销
    * [x] 失效 Peers 清理
    * [ ] User-Agent 客户端控制
    * [ ] 反作弊
    * [ ] SeedBox
  * [ ] 速度限制
  * [x] 同伴查看
  * [x] 说谢谢
* [x] 用户管理
    * [x] 用户登录
    * [x] 用户注册
    * [x] 权限控制
        * [x] 权限节点
        * [x] 用户组
        * [ ] 权限管理
            * [ ] 以其他用户身份登录
            * [ ] 用户关联
* [x] 站点安全
    * [x] bCrypt 加密
    * [ ] 登录过程 RSA 非对称加密
    * [x] 账户与 Passkey 防暴力破解
    * [ ] IP 控制
    * [ ] 小号控制
* [x] 发种
    * [ ] 自定义模板
* [x] 种子分区
* [x] 种子标签
* [ ] 字幕
* [ ] 考核
* [ ] Hit&Run
* [ ] 认领
* [ ] 魔力值
* [ ] 申诉
* [ ] 多国语言
* [ ] 插件系统
* [ ] 用户邀请
* [ ] 排行
* [ ] 统计
* [x] RSS 订阅
* [ ] 隐私设定
