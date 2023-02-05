package com.github.bitsapling.sapling;

import cn.dev33.satoken.SaManager;
import com.github.bitsapling.sapling.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Import({SaBeanRegister.class, SaBeanInject.class})
@Slf4j
public class SaplingApplication {

    public static void main(String[] args) {
        System.out.println("Hash: "+PasswordHash.hash("testtest"));
        SpringApplication.run(SaplingApplication.class, args);
        //SaManager.setSaTokenContext(new SaTokenContextForSpring());
        SaManager.getConfig();
    }

}
