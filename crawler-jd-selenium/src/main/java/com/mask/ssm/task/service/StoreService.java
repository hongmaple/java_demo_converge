package com.mask.ssm.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mask.ssm.task.mapper.StoreMapper;
import com.mask.ssm.task.pojo.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StoreService {

    @Autowired
    private StoreMapper storeMapper;

    public Store add(Store store) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_name",store.getStoreName());
        Store store1 = storeMapper.selectOne(queryWrapper);
        if (Objects.isNull(store1)) {
            storeMapper.insert(store);
            return store;
        }
        storeMapper.updateById(store1);
        return store1;
    }

}
