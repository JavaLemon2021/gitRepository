package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

/**
 * 2021/8/10 0010
 */
public interface ClueService {
    //保存线索
    int saveCreateClue(Clue clue);

    //根据clueid查询线索的详情
    Clue queryClueForDetailById(String id);

    //转换线索
    void saveConvert(Map<String,Object> map);
}
