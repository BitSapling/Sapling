package com.github.bitsapling.sapling.torrentparser;

import com.github.bitsapling.sapling.torrentparser.types.TorrentFileEntry;
import com.github.bitsapling.sapling.torrentparser.types.TorrentRevision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ITorrentParser {
    /**
     * 获取 .torrent 文件的版本
     *
     * @return .torrent 文件的版本
     */
    @NotNull
    TorrentRevision getTorrentRevision();

    /**
     * 获取 【有序的】 .torrent 文件的文件列表
     *
     * @return 【有序的】 .torrent 文件的文件列表
     */
    @NotNull
    Collection<TorrentFileEntry> getTorrentFiles();

    /**
     * 获取该 .torrent 的种子长度
     *
     * @return 该 .torrent 的种子长度
     */
    long getTorrentLength();

    /**
     * 获取该 .torrent 的分块长度
     *
     * @return 该 .torrent 的分块长度
     */
    long getPiecesLength();

    /**
     * 获取该 .torrent 的info_hash
     *
     * @param revision 不同 Revision 采用不同的摘要算法，指定一个 Revision 以生成其对应的 info_hash
     * @return 该 .torrent 的 info_hash
     */
    @NotNull
    String getInfoHash(@NotNull TorrentRevision revision);

    /**
     * 获取 dict 根下的 publish-website 字段内容
     * 这是一个自定义字段，BitSapling、BitComet 和其它部分 BT 客户端可能会添加它
     *
     * @return dict 根下的 publish-website 字段内容，如果不存在该字段则返回 null
     */
    @Nullable
    String getPublishWebsite();

    /**
     * 获取 dict 根下的 publisher 字段内容
     * 这是一个自定义字段，BitSapling、BitComet 和其它部分 BT 客户端可能会添加它
     *
     * @return dict 根下的 publisher 字段内容，如果不存在该字段则返回 null
     */
    @Nullable
    String getPublisher();

    /**
     * 获取 dict 根下的 publisher-url 字段内容
     * 这是一个自定义字段，BitSapling、BitComet 和其它部分 BT 客户端可能会添加它
     *
     * @return dict 根下的 publisher-url 字段内容，如果不存在该字段则返回 null
     */
    @Nullable
    String getPublisherUrl();


    /**
     * 重写该 .torrent 文件
     *
     * @param trackers       提供一个 【有序的】 的【带有空字符串】的 Trackers 列表，定义如下
     *                       使用 【空字符串】 分隔多个 【层】，此举主要是为兼容 BEP 0012
     *                       List 内容：["t1", "t2", "t3", "", "t4", "t5", "t6", "", "t7", "t8", "t9"]
     *                       转换后结果
     *                       [[t1,t2,t3],[t4,t5,t6],[t7,t8,t9]]
     * @param publishWebsite 向 dict 的根放置一个 "publish-website"
     * @param publisher      向 dict 的根放置一个 "publisher"
     * @param publisherUrl   向 dict 的根放置一个 "publisher-url"
     * @return 重写后的 .torrent 文件字节数组
     */
    byte[] rewrite(@NotNull List<String> trackers, @Nullable String publishWebsite, @Nullable String publisher,
                   @Nullable String publisherUrl);
}
