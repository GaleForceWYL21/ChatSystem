package com.wyl.gpt.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("messageVo")
@Data
@ApiModel("gpt3.5内容")
public class messageVo implements Serializable {
    private String role;
    private String content;
}
