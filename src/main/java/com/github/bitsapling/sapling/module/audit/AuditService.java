package com.github.bitsapling.sapling.module.audit;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.springframework.stereotype.Service;

@Service
public class AuditService extends ServiceImpl<AuditMapper, Audit> implements CommonService<Audit> {

}
