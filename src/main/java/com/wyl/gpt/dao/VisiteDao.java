package com.wyl.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.gpt.entity.VisiteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface VisiteDao extends BaseMapper<VisiteEntity> {


}

