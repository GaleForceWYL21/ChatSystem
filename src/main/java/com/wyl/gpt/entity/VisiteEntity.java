package com.wyl.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * (Visite)实体类
 *
 * @author makejava
 * @since 2023-03-26 19:11:33
 */
@Alias("VisiteEntity")
@Data
@ApiModel("token使用次数")
@TableName("own.visite")
public class VisiteEntity implements Serializable {
    private static final long serialVersionUID = 611531366239744106L;
    
    private String date;
    
    private Integer times;
    
    private Long keyTimes;

    private Long promptTokens;

    private Long completionTokens;


}

