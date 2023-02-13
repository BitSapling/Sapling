package com.github.bitsapling.sapling.autoconfig;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@EnableMethodCache(basePackages = "com.github.bitsapling.sapling")
@EnableCreateCacheAnnotation
public class JetcacheConfig {

}
