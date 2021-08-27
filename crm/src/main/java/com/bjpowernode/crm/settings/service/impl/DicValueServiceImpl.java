package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2021/8/3
 */
@Service
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryAllValue() {
        return dicValueMapper.selectAllDicValues();
    }

    @Override
    public DicValue queryValueById(String id) {
        return dicValueMapper.selectDicValueById(id);
    }

    @Override
    public int saveCreatValue(DicValue dicValue) {
        return dicValueMapper.insertDicValue(dicValue);
    }

    @Override
    public int deleteValueById(String[] id) {
        return dicValueMapper.deleteDicValueByIds(id);
    }

    @Override
    public int saveEditValue(DicValue dicValue) {
        return dicValueMapper.updateDicValue(dicValue);
    }

    @Override
    public int deleteDicValueByTypeCodes(String[] typeCodes) {
        return dicValueMapper.deleteDicValueByTypeCodes(typeCodes);
    }

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
