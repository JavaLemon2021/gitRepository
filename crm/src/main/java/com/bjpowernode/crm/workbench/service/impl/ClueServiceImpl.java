package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 2021/8/10 0010
 */
@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;


    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Autowired
    private TranMapper tranMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        //取一些参数
        String clueId=(String)map.get("clueId");
        User user=(User)map.get("sessionUser");
        //是否创建交易
        String isCreateTran=(String)map.get("isCreateTran");

        //（1）获取到线索id，通过线索id获取线索对象
        Clue clue=clueMapper.selectClueById(clueId);
        System.out.println("ok");
        //(2）通过线索对象提取客户信息(与公司）
        Customer customer=new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setAddress(clue.getAddress());
        customer.setDescription(clue.getDescription());

        //插入客户
        customerMapper.insertCustomer(customer);
        System.out.println("Ok");

        //（3）通过线索对象提取联系人信息，保存联系人(个人）
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setSource(clue.getSource());
        contacts.setFullName(clue.getFullName());
        contacts.setAppellation(clue.getAppellation());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contactsMapper.insertContacts(contacts);

        System.out.println("ok");

        //（4） 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList=clueRemarkMapper.selectClueRemarkByClueId(clueId);

        if(clueRemarkList!=null&&clueRemarkList.size()>0){
            CustomerRemark cur=null; //客户备注
            ContactsRemark cor=null; //联系人备注

            //两个集合存放客户备注和联系人备注
            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> corList=new ArrayList<>();

            for(ClueRemark cr:clueRemarkList){
                //创建客户备注
                cur=new CustomerRemark();
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                cur.setCreateBy(user.getId());
                cur.setCreateTime(DateUtils.formatDateTime(new Date()));
                cur.setCustomerId(customer.getId());

                curList.add(cur);

                //创建联系人备注
                cor=new ContactsRemark();
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                cor.setCreateBy(user.getId());
                cor.setCreateTime(DateUtils.formatDateTime(new Date()));
                cor.setContactsId(contacts.getId());
                corList.add(cor);
            }

            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);

        }

        //“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> carList=clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        if(carList!=null&&carList.size()>0){
            //联系人和市场活动对象
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            for(ClueActivityRelation car:carList){
                coar=new ContactsActivityRelation();
                coar.setId(UUIDUtils.getUUID());
                coar.setContactsId(contacts.getId());
                coar.setActivityId(car.getActivityId());

                coarList.add(coar);
            }

            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);

        }

        //（6）如果有创建交易需求，创建一条交易
        if("true".equals(isCreateTran)){
            Tran tran=new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String)map.get("amountOfMoney"));
            tran.setName((String)map.get("tradeName"));
            tran.setExpectedDate((String)map.get("expectedClosingDate"));
            tran.setCustomerId(customer.getId());
            tran.setContactsId(contacts.getId());
            tran.setStage((String)map.get("stage"));

            tranMapper.insert(tran);

            //（7）如果创建了交易，要将线索下所有的备注转到交易备注
            if(clueRemarkList!=null&&clueRemarkList.size()>0){
                //交易备注
                TranRemark tr=null;
                List<TranRemark> trList=new ArrayList<>();
                for(ClueRemark cr:clueRemarkList){
                    tr=new TranRemark();
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());

                    trList.add(tr);
                }

                tranRemarkMapper.insertTranRemarkByList(trList);
            }

        }

        //（8） 删除线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //（9）删除线索和市场活动的关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //（10）删除线索
        clueMapper.deleteClueById(clueId);

    }


}
