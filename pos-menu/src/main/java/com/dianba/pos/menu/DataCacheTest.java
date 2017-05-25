package com.dianba.pos.menu;

import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.metadata.item.ItemType;

import java.util.List;

public class DataCacheTest {


    public static void main(String[] args) {
        //这两个方法只需加载时执行一次
        DataCacheApplicationContextLoaderNotify.setItemRemoteServiceURL("http://120.25.199.108:8081/");
        ItemDataCacheService.initItemCache();

        //从内存中读取
        List<ItemType> itemTypes = ItemDataCacheService.getItemTypes();
        for (ItemType itemType : itemTypes) {
            System.out.println(itemType.getId());
        }
    }
}
