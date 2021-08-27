package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * 2021/8/3
 */
public interface DicValueService {
    List<DicValue> queryAllValue();
    DicValue queryValueById(String id);
    int saveCreatValue(DicValue dicValue);
    int deleteValueById(String[] id);
    int saveEditValue(DicValue dicValue);
    int deleteDicValueByTypeCodes(String[] typeCodes);
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
