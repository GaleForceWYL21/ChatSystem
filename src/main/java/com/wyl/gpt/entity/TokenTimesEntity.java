package com.wyl.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Alias("tokenTimesEntity")
@Data
@ApiModel("token使用次数")
@TableName("own.token_times")
public class TokenTimesEntity implements Serializable {

    @TableId()
    private int id;

    private String token;

    private String date;

    private int times;

    private Long totalTimes;
}
