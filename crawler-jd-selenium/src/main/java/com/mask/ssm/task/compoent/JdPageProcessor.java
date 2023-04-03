package com.mask.ssm.task.compoent;

import com.mask.ssm.task.pojo.Item;
import com.mask.ssm.task.pojo.Store;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Mask.m
 * @create: 2021/07/17 10:02
 * @description: 解析页面
 */
@Component
public class JdPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        String level = page.getRequest().getExtra("level").toString();
        switch (level){
            case "list":
                parseList(page);
                break;
            case "detail":
                praseDetail(page);
                break;
        }


    }

    /**
     * 解析详情页
     *
     * @param page
     */
    private void praseDetail(Page page) {
        Html html = page.getHtml();
        String title = html.$("div.master .p-name").xpath("///allText()").get();
        String priceStr = html.$("div.summary-price-wrap .p-price span.price").xpath("///allText()").get();
        String pic = "https:"+html.$("#spec-img").xpath("///@src").get();
        String url = "https:"+html.$("div.master .p-name a").xpath("///@href").get();
        String sku = html.$("a.notice.J-notify-sale").xpath("///@data-sku").get();
        // 规格
        String standard = html.css("div#choose-attr-2 div.selected", "data-value").toString();

        //String haoping = html.css("div.small > ul > li:nth-child(3) > a > em", "text").toString();
        // 店铺名称
        String storeName = html.css("div.popbox-inner a", "text").toString();
        //店铺url
        String storeUrl = "https:"+html.$("div.popbox-inner a").xpath("///@href").get();
        Store store = new Store();
        store.setStoreName(storeName);
        store.setStoreUrl(storeUrl);

        String classify1 = html.css("div.crumb > div:nth-child(1) a","text").toString();

        String classify2 = html.css("div.crumb > div:nth-child(3) a","text").toString();

        String classify3 = html.css("div.crumb > div:nth-child(5) a","text").toString();

        Item item = new Item();
        try {
            item.setTitle(title);
            item.setPic(pic);
            item.setPrice(Float.valueOf(priceStr));
            item.setUrl(url);
            item.setUpdated(new Date());
            item.setSku(StringUtils.isNotBlank(sku)?Long.valueOf(sku) : null);
            item.setStandard(standard);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        // 单条数据塞入
        page.putField("item", item);
        page.putField("store", store);
        page.putField("classify1", classify1);
        page.putField("classify2", classify2);
        page.putField("classify3", classify3);
    }

    /**
     * 解析列表页
     * @param page
     */
    private void parseList(Page page) {
        Html html = page.getHtml();
        // 这里拿到sku 和 spu 并交给pipeline
        List<Selectable> nodes = html.$("ul.gl-warp.clearfix > li").nodes();
        List<Item> itemList = new ArrayList<>();
        for (Selectable node : nodes) {
            // 拿到sku和spu
            String sku = node.$("li").xpath("///@data-sku").get();
            String spu = node.$("li").xpath("///@data-spu").get();
            String href = "https:" + node.$("div.p-img a").xpath("///@href").get();

            Item item = new Item();
            item.setSku(Long.valueOf(sku));
            item.setSpu(StringUtils.isNotBlank(spu) ? Long.parseLong(spu) : 0);
            item.setCreated(new Date());
            itemList.add(item);

            // 同时还需要把链接加到详情页 加到队列
            Request request = new Request(href);
            request.putExtra("level", "detail");
            request.putExtra("pageNum", page.getRequest().getExtra("pageNum"));
            request.putExtra("detailUrl", href);
            page.addTargetRequest(request);
        }

        // 以集合的方式存入
        page.putField("itemList", itemList);

        // 同时还要去做分页
        String pageNum = page.getRequest().getExtra("pageNum").toString();
        if ("1".equals(pageNum)){
            Request request = new Request("https://nextpage.com");
            request.putExtra("level", "page"); // 标识去分页
            request.putExtra("pageNum", (Integer.valueOf(pageNum) + 1) + "");// 页码要+1 接下来要的是第二页
            // 添加到队列
            page.addTargetRequest(request);
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
