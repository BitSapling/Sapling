package com.github.bitsapling.sapling.controller.feed;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.config.SiteBasicConfig;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIErrorCode;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.service.AuthenticationService;
import com.github.bitsapling.sapling.service.SettingService;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.service.UserService;
import com.github.bitsapling.sapling.util.IPUtil;
import com.rometools.rome.feed.rss.Category;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedOutput;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feed")
@Slf4j
public class FeedController {
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/subscribe")
    public String feed(@RequestParam Map<String, String> params) throws FeedException {
        String passkey = params.get("passkey");
        if (StringUtils.isEmpty(passkey)) {
            throw new APIGenericException(APIErrorCode.MISSING_PARAMETERS, "Passkey is required");
        }
        User user = authenticationService.authenticate(passkey, IPUtil.getRequestIp(request));
        if (user == null) {
            throw new APIGenericException(APIErrorCode.USER_NOT_FOUND, "Unauthorized");
        }
        if (!StpUtil.hasPermission(user.getId(), "feed:subscribe")) {
            throw new NotPermissionException("feed:subscribe");
        }
        int entries = Math.min(Integer.parseInt(params.getOrDefault("entries", "50")), 300);
        String categorySlug = params.get("category");
        String[] categorySlugs = categorySlug != null ? categorySlug.split(",") : new String[0];
        String promotionSlug = params.get("promotion");
        String[] promotionSlugs = promotionSlug != null ? promotionSlug.split(",") : new String[0];
        String tag = params.get("tag");
        String[] tags = tag != null ? tag.split(",") : new String[0];
        List<Torrent> torrentList = torrentService
                .search("", Arrays.stream(categorySlugs).toList()
                        , Arrays.stream(promotionSlugs).toList(),
                        Arrays.stream(tags).toList(), Pageable.ofSize(entries))
                .getContent();
        return makeFeed(passkey, torrentList, false);
    }

    private String makeFeed(String passkey, List<Torrent> torrentList, boolean canSeeAnonymous) throws FeedException {
        SiteBasicConfig basicConfig = settingService.get(SiteBasicConfig.getConfigKey(), SiteBasicConfig.class);
        Channel channel = new Channel("rss_2.0");
        channel.setTitle(basicConfig.getSiteName() + " - " + basicConfig.getSiteSubName());
        channel.setGenerator("Sapling RSS Generator - v1.0");
        channel.setEncoding("UTF-8");
        channel.setPubDate(new Date());
        channel.setDescription(basicConfig.getSiteDescription());
        channel.setLink(basicConfig.getSiteBaseURL());
        List<Item> items = new ArrayList<>();
        for (Torrent torrent : torrentList) {
            try {
                Item item = new Item();
                item.setAuthor(torrent.getUsernameWithAnonymous(canSeeAnonymous));
                StringBuilder titleBuilder = new StringBuilder(torrent.getTitle());
                appendTorrentTitle(torrent, titleBuilder);
                item.setTitle(titleBuilder.toString());
                item.setPubDate(torrent.getCreatedAt());
                item.setLink(basicConfig.getSiteBaseURL() + "/torrent/" + torrent.getInfoHash());
                Guid guid = new Guid();
                guid.setValue(torrent.getInfoHash());
                item.setGuid(guid);
                Category category = new Category();
                category.setValue(torrent.getCategory().getName());
                item.setCategories(List.of(category));
                Enclosure torrentClosure = new Enclosure();
                torrentClosure.setType("application/x-bittorrent");
                torrentClosure.setUrl(basicConfig.getSiteBaseURL() + "/torrent/download/" + torrent.getInfoHash() + "?passkey=" + passkey);
                item.setEnclosures(List.of(torrentClosure));
                Description description = new Description();
                item.setDescription(description);
                items.add(item);
            } catch (Exception e) {
                log.error("Error when generating RSS item for torrent: {}", torrent, e);
            }
        }
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }

    private void appendTorrentTitle(Torrent torrent, StringBuilder titleBuilder) {
        titleBuilder.append(" ");
        titleBuilder.append("[");
        titleBuilder.append(torrent.getPromotionPolicy().getDisplayName());
        titleBuilder.append("]");
        titleBuilder.append(" ");
    }

}
