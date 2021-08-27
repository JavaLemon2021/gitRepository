package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 2021/8/16 0016
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;


    @Override
    public boolean saveCreateTran(Map<String, Object> map) {
        boolean flag=false;
        Tran tran=(Tran)map.get("tran");
        //customerId来判读是否是新的客户
        String customerId=tran.getCustomerId();
        String customername=(String)map.get("customerName");
        User user=(User)map.get("sessionUser");
        int ret=0;

        //是否需要创建用户
        if(customerId==null||customerId.trim().length()==0){
            Customer customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customername);

            ret=customerMapper.insertCustomer(customer);
            //因为customer的id在插入customer表之后才有
            tran.setCustomerId(customer.getId());
        }

        //保存交易
        tranMapper.insert(tran);

        //保存交易历史
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());

        tranHistoryMapper.insertTranHistory(tranHistory);
        flag=true;

        return flag;
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
