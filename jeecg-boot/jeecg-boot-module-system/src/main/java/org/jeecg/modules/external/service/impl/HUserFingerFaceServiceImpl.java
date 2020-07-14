package org.jeecg.modules.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.HUserFingerFace;
import org.jeecg.modules.external.mapper.HUserFingerFaceMapper;
import org.jeecg.modules.external.service.IHUserFingerFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
@Service
public class HUserFingerFaceServiceImpl extends ServiceImpl<HUserFingerFaceMapper, HUserFingerFace> implements IHUserFingerFaceService {
    @Autowired
    private HUserFingerFaceMapper userFingerFaceMapper;

    /**
     * 获取人脸/指纹相关信息
     * @param userFingerFace
     * @return
     */
    @Override
    public List<HUserFingerFace> queryHuserFingerFaceList(HUserFingerFace userFingerFace) {
        return userFingerFaceMapper.queryHuserFingerFaceList(userFingerFace);
    }

    /**
     * 注册人脸/指纹相关信息
     * @param arr
     * @throws RuntimeException
     */
    @Override
    @Transactional
    public void saveList(JSONArray arr){
        List<HUserFingerFace> list = JSONArray.parseArray(arr.toJSONString(),HUserFingerFace.class);
        //1.入SPD库
        for (HUserFingerFace face : list) {
            HUserFingerFace hForcer=new HUserFingerFace();
            hForcer.setUserId(face.getUserId());
            hForcer.setImage(face.getImage());
            hForcer.setType(face.getType());
            hForcer.setIsDisable("1");
            super.save(hForcer);
        }
    }
}
