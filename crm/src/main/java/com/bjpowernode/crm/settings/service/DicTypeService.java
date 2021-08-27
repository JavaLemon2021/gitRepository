package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * 2021/8/2 0002
 */
public interface DicTypeService {
    List<DicType> queryAllDicTypes();
    DicType queryDicTypeByCode(String code);
    int saveCreateDicType(DicType dicType);
    int saveEditDicType(DicType dicType);
    int deleteDicTypeByCodes(String[] codes);
}
