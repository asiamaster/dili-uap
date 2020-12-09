package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Position;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-01 15:23:34.
 */
public interface PositionService extends BaseService<Position, Long> {

    /**
     * 保存职位信息
     *
     * @param position 职位信息对象
     * @return
     */
    BaseOutput save(Position position);

    /**
     * 删除职位信息
     *
     * @param id 职位id
     * @return
     */
    BaseOutput deletePosition(Long id);
}