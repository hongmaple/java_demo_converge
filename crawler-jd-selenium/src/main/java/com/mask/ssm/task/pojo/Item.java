package com.mask.ssm.task.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("jd_item")
@Data
public class Item {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("cid")
    private Long cid;
    @TableField("store_id")
    private Long storeId;
    @TableField("spu")
    private Long spu;
    @TableField("sku")
    private Long sku;
    @TableField("title")
    private String title;
    @TableField("price")
    private Float price;
    @TableField("pic")
    private String pic;
    @TableField("url")
    private String url;
    @TableField("standard")
    private String standard;
    @TableField("show_count_str")
    private String showCountStr;
    @TableField("comment_count_str")
    private String commentCountStr;
    @TableField("default_good_countStr")
    private String defaultGoodCountStr;
    @TableField("good_count_str")
    private String goodCountStr;
    @TableField("after_count_str")
    private String afterCountStr;
    @TableField("video_count_str")
    private String videoCountStr;
    @TableField("good_rate_show")
    private String goodRateShow;
    @TableField("poor_count_str")
    private String poorCountStr;
    @TableField("created")
    private Date created;
    @TableField("updated")
    private Date updated;
}