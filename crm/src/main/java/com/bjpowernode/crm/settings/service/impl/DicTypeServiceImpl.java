package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2021/8/2 0002
 */
@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Autowired
    private DicTypeMapper dicTypeMapper;

    @Override
    public List<DicType> queryAllDicTypes() {
        return dicTypeMapper.selectAllDicTypes();
    }

    @Override
    public DicType queryDicTypeByCode(String code) {
        return dicTypeMapper.selectDicTypeCode(code);
    }

    @Override
    public int saveCreateDicType(DicType dicType) {
        return dicTypeMapper.insertDicType(dicType);
    }

    @Override
    public int saveEditDicType(DicType dicType) {
        return dicTypeMapper.updateDicType(dicType);
    }

    @Override
    public int deleteDicTypeByCodes(String[] codes) {
        return dicTypeMapper.deleteDicTypeByCodes(codes);
    }
}
