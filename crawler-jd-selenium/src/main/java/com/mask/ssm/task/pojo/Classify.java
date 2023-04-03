package com.mask.ssm.task.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("classify")
@Data
public class Classify {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 父id
     */
    @TableField("prent_id")
    private Long prentId;

    @TableField("name")
    private String name;

    @TableField("type")
    private Integer type;
}
