package com.mask.ssm.task.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("store")
@Data
public class Store {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 店名
     */
    @TableField("store_name")
    private String storeName;

    /**
     * 店 url
     */
    @TableField("store_url")
    private String storeUrl;
}
