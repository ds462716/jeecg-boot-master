package org.jeecg.modules.external.cxf.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.external.cxf.WebServiceService;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.jws.WebService;

@Service
@WebService(serviceName = "WebServiceService", // 与接口中指定的name一致
        targetNamespace = "http://webservice.business.mixpay.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "org.jeecg.modules.external.cxf.WebServiceService" // 接口地址
)
public class WebServiceServiceImpl implements WebServiceService {

    @Autowired
    private IExInspectionItemsService exInspectionItemsService;

    @Override
    public String saveExInspectionItems(String patientName, String patientSex,
                                        String patientAge, String cardId,
                                        String barCode, String applyDoctor,
                                        String applyDepartment, String testDoctor,
                                        String testDepartment, String patientType,
                                        String groupBy, String receiveDate,
                                        String testDate, String specimenType,
                                        String state, String data
                                       ) {
        JSONObject jsonResult = new JSONObject();
        try{
            ExInspectionItems exInspectionItems = new ExInspectionItems();
            exInspectionItems.setPatientName(patientName);
            exInspectionItems.setPatientSex(patientSex);
            exInspectionItems.setPatientAge(patientAge);
            exInspectionItems.setCardId(cardId);
            exInspectionItems.setBarCode(barCode);
            exInspectionItems.setApplyDoctor(applyDoctor);
            exInspectionItems.setApplyDepartment(applyDepartment);
            exInspectionItems.setTestDoctor(testDoctor);
            exInspectionItems.setTestDepartment(testDepartment);
            exInspectionItems.setPatientType(patientType);
            exInspectionItems.setGroupBy(groupBy);
            exInspectionItems.setReceiveDate(receiveDate);
            exInspectionItems.setTestDate(testDate);
            exInspectionItems.setSpecimenType(specimenType);
            exInspectionItems.setState(state);
            exInspectionItems.setId(UUIDUtil.getUuid());
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.put("code","-1");
            jsonResult.put("msg","参数异常");
        }
        jsonResult.put("code","0");
        jsonResult.put("msg","成功");
        return jsonResult.toJSONString();
    }
}
