package com.wyl.gpt.entity.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("choiceVo")
@Data
public class choiceVo implements Serializable {
    private String text;
    private int index;
    private messageVo message;
    private String logprobs;
    private String finish_resaon;
}
