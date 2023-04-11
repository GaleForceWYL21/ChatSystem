package com.wyl.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.gpt.entity.TokenTimesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TokenTimesDao extends BaseMapper<TokenTimesEntity> {
}
