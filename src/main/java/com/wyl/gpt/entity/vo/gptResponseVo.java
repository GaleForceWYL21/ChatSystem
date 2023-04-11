package com.wyl.gpt.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("gptResponseVo")
@Data
@ApiModel("返回内容")
public class gptResponseVo implements Serializable {
    private String id;
    private String object;
    private Long created;
    private String model;
    private choiceVo choices;
    private usageVo usage;


}
