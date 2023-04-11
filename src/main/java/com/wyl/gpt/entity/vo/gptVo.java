package com.wyl.gpt.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("gptVo")
@Data
@ApiModel("内容")
public class gptVo {
    private String msg;
    private String preMsg;
}
