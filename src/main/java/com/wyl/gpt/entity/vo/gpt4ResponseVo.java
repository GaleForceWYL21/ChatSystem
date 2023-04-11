package com.wyl.gpt.entity.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("gpt4ResponseVo")
@Data
@ApiModel("gpt4响应")
public class gpt4ResponseVo {
    private  String id;
    private String object;
    private String model;
    private Long created;
    private usageVo usage;
    private choiceVo[] choices;
}
