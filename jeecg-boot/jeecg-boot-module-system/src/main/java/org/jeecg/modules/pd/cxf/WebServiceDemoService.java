package org.jeecg.modules.pd.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebServiceDemoService {

    @WebMethod
    String hello(@WebParam(name = "name")String name);

}
