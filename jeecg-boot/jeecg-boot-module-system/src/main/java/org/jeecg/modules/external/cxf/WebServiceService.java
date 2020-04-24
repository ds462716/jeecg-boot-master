package org.jeecg.modules.external.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebServiceService {

    @WebMethod
    String saveExInspectionItems(@WebParam(name = "patientName") String patientName,
                                 @WebParam(name = "patientSex") String patientSex,
                                 @WebParam(name = "patientAge") String patientAge,
                                 @WebParam(name = "cardId") String cardId,
                                 @WebParam(name = "barCode") String barCode,
                                 @WebParam(name = "applyDoctor") String applyDoctor,
                                 @WebParam(name = "applyDepartment") String applyDepartment,
                                 @WebParam(name = "testDoctor") String testDoctor,
                                 @WebParam(name = "testDepartment") String testDepartment,
                                 @WebParam(name = "patientType") String patientType,
                                 @WebParam(name = "groupBy") String groupBy,
                                 @WebParam(name = "receiveDate") String receiveDate,
                                 @WebParam(name = "testDate") String testDate,
                                 @WebParam(name = "specimenType") String specimenType,
                                 @WebParam(name = "state") String state,
                                 @WebParam(name = "data") String data);

}
