package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * 2021/8/12 0012
 */
public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> relationList);
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
