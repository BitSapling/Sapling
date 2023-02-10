package com.github.bitsapling.sapling;

import cn.dev33.satoken.SaManager;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.util.PasswordHash;
import com.github.bitsapling.sapling.util.TorrentParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
@EnableCaching
//@Import({SaBeanRegister.class, SaBeanInject.class})
@Slf4j
public class SaplingApplication {

    public static void main(String[] args) throws IOException, TorrentException {
        System.out.println("Hash: " + PasswordHash.hash("testtest"));
        System.out.println(new UUID(1, 0).toString().replace("_", ""));
        System.out.println(new UUID(2, 0).toString().replace("_", ""));
        SpringApplication.run(SaplingApplication.class, args);
        //SaManager.setSaTokenContext(new SaTokenContextForSpring());
        SaManager.getConfig();
        TorrentParser torrentParserImproving = new TorrentParser(new File("1.torrent"));
        torrentParserImproving.getFileList().forEach((key, value) -> log.info("{} {}", key, value));
        log.info("Total size: {}", torrentParserImproving.getTorrentFilesSize());
        log.info("InfoHash: {}", torrentParserImproving.getInfoHash());

    }

}
