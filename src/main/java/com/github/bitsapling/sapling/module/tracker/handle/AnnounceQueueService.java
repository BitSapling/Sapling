package com.github.bitsapling.sapling.module.tracker.handle;

import com.github.bitsapling.sapling.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnnounceQueueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnounceQueueService.class);
    @Autowired
    private StringRedisTemplate redisClient;

    @Nullable
    public Long insertTask(@NotNull AnnounceTask announceTask) {
        String json = JsonUtil.getGson().toJson(announceTask);
        Long length = redisClient.opsForList().rightPush("announce_queue", json);
        LOGGER.debug("Inserted the task {} into the announce queue, queue length: {}", json, length);
        return length;
    }

    @Nullable
    public AnnounceTask pullTask() {
        String json = redisClient.opsForList().leftPop("announce_queue");
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JsonUtil.getGson().fromJson(json, AnnounceTask.class);
    }
}
