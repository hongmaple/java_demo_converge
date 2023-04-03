package com.mask.ssm.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mask.ssm.task.mapper.ClassifyMapper;
import com.mask.ssm.task.pojo.Classify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;

    public Classify add(Classify classify) {
        QueryWrapper<Classify> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",classify.getName());
        Classify classify1 = classifyMapper.selectOne(queryWrapper);
        if (Objects.isNull(classify1)) {
            classifyMapper.insert(classify);
            return classify;
        }
        classifyMapper.updateById(classify1);
        return classify1;
    }
}
