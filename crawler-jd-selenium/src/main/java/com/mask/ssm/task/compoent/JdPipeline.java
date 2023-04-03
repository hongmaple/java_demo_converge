package com.mask.ssm.task.compoent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mask.ssm.task.pojo.Classify;
import com.mask.ssm.task.pojo.Item;
import com.mask.ssm.task.mapper.ItemMapper;
import com.mask.ssm.task.pojo.Store;
import com.mask.ssm.task.service.ClassifyService;
import com.mask.ssm.task.service.StoreService;
import com.mask.ssm.task.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author: Mask.m
 * @create: 2021/07/17 10:33
 * @description: 自定义pipeline保存数据库
 */
@Component
public class JdPipeline implements Pipeline {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private HttpUtils httpUtils;

    /**
     * 保存数据库
     *
     * @param resultItems 返回的结果集
     * @param task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Item> itemList = resultItems.get("itemList");
        if (itemList != null){
            // 说明是列表页保存的数据
            for (Item item : itemList) {
                itemMapper.insert(item);
            }
        }else {
            Store store = storeService.add(resultItems.get("store"));

            Classify classify1 = classifyService.add(createClassify(resultItems.get("classify1"),0L,0));
            Classify classify2 = classifyService.add(createClassify(resultItems.get("classify2"),classify1.getId(),1));
            Classify classify3 = classifyService.add(createClassify(resultItems.get("classify3"),classify2.getId(),2));

            // 没有的话就是更新数据
            Item item = resultItems.get("item");
            item.setCid(classify3.getId());
            item.setStoreId(store.getId());
            // 商品评价
            try {
                String jsonPrice = httpUtils.doGetHtml("https://club.jd.com/comment/productCommentSummaries.action?referenceIds="+item.getSku()+"&categoryIds=9987,653,655&callback=jQuery325832&_=1679321407894");
                JSONObject priceObject = (JSONObject) JSONArray.parse(jsonPrice.substring(13,jsonPrice.length()-2));
                JSONObject commentsCount = (JSONObject) JSONArray.parseArray(priceObject.get("CommentsCount").toString()).get(0);
                item.setShowCountStr(commentsCount.getString("ShowCountStr"));
                item.setCommentCountStr(commentsCount.getString("CommentCountStr"));
                item.setDefaultGoodCountStr(commentsCount.getString("DefaultGoodCountStr"));
                item.setGoodCountStr(commentsCount.getString("GoodCountStr"));
                item.setAfterCountStr(commentsCount.getString("AfterCountStr"));
                item.setVideoCountStr(commentsCount.getString("VideoCountStr"));
                item.setGoodRateShow(commentsCount.getString("GoodRateShow"));
                item.setPoorCountStr(commentsCount.getString("PoorCountStr"));
            } catch (Exception e) {
                System.out.println(e);
            }
            itemMapper.update(item, Wrappers.<Item>lambdaUpdate().eq(Item::getSku,item.getSku()));
        }
    }

    private Classify createClassify(String name,Long prentId,int type) {
        Classify classify = new Classify();
        classify.setName(name);
        classify.setPrentId(prentId);
        classify.setType(type);
        return classify;
    }
}
