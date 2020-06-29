package org.jeecg.modules.external.ngalain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.jeecg.modules.pd.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JxsHlwYyPtInterface {

    @Autowired
    private IPdProductService pdProductService;
    @Autowired
    private IPdUsePackageService pdUsePackageService;

    private static String NAME_SPACE = "";
    private static String UPLOAD_MATERIAL_LIST = "";//医疗耗材目录推送接口
    private static String UPLOAD_CHECK_ITEM_LIST = "";//检验检查项目推送接口

    /**
     * 医疗耗材目录备案
     * @return
     */
    public JSONObject uploadMaterialList(){
        JSONObject returnJson = new JSONObject();
        List<PdProduct> pdProductList = pdProductService.selectList(new PdProduct());
        if(pdProductList!=null &&pdProductList.size()>0){
            List<Map<String,Object>> mapList =new ArrayList<>();
            for(PdProduct pdProduct :pdProductList){
                Map<String,Object> map = new HashMap<>();
                map.put("organId","");
                map.put("materialId","");
                map.put("materialName",pdProduct.getName());
                map.put("model",pdProduct.getSpec());
                map.put("unit",pdProduct.getUnitName());
                map.put("hospMaterialManuf",pdProduct.getVenderName());
                map.put("materialPrice",pdProduct.getSellingPrice());
                map.put("useFlag",pdProduct.getDelFlag());
                map.put("updateTime",pdProduct.getUpdateTime());
                mapList.add(map);
            }
            returnJson = HttpUtil.httpPost(NAME_SPACE + UPLOAD_MATERIAL_LIST, JSONArray.toJSONString(mapList));
            return returnJson;
        }else{
            returnJson.put("code", "-200");
            returnJson.put("msg", "系统异常");
            return returnJson;
        }
    }

    /**
     * 检验检查项目备案
     * @return
     */
    public JSONObject uploadCheckItemList(){
        JSONObject returnJson = new JSONObject();
        List<PdUsePackage> pdUsePackageList = pdUsePackageService.queryList(new PdUsePackage());
        if(pdUsePackageList!=null &&pdUsePackageList.size()>0){
            List<Map<String,Object>> mapList =new ArrayList<>();
            for(PdUsePackage pdUsePackage :pdUsePackageList){
                Map<String,Object> map = new HashMap<>();
                map.put("organId","");
                map.put("itemId",pdUsePackage.getCode());
                map.put("classCode","");
                map.put("deptId",pdUsePackage.getDepartIds());
                map.put("itemName",pdUsePackage.getName());
                map.put("itemPrice","");
                map.put("considerations",pdUsePackage.getRemarks());
                map.put("updateTime",pdUsePackage.getUpdateTime());
                mapList.add(map);
            }
            returnJson = HttpUtil.httpPost(NAME_SPACE + UPLOAD_CHECK_ITEM_LIST, JSONArray.toJSONString(mapList));
            return returnJson;
        }else{
            returnJson.put("code", "-200");
            returnJson.put("msg", "系统异常");
            return returnJson;
        }
    }
}
