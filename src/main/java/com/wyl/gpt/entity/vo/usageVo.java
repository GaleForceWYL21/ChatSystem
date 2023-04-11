package com.wyl.gpt.entity.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("usageVo")
@Data
public class usageVo implements Serializable {
    private Long prompt_tokens;
    private Long completion_tokens;
    private Long total_tokens;
}
