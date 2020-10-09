package org.jeecg.modules.external.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.HUserFingerFace;

import java.util.List;

/**
 * @Description:
 * @Author: MCB
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface IHUserFingerFaceService extends IService<HUserFingerFace> {

    /**
     * 获取人脸/指纹相关信息
     * @param userFingerFace
     * @return
     */
    public List<HUserFingerFace> queryHuserFingerFaceList(HUserFingerFace userFingerFace);

    /**
     * 注册人脸/指纹相关信息
     * @param arr
     */

    public void saveList(JSONArray arr);

}
