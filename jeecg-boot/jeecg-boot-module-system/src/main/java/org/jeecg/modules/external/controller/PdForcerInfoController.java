package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.external.entity.HForcerInfo;
import org.jeecg.modules.external.service.IHforcerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 智能柜管理
 * @Author: mcb
 * @Date: 2020-03-13
 * @Version: V1.0
 */
@Api(tags = "智能柜信息表")
@RestController
@RequestMapping("/pd/pdForcerInfo")
@Slf4j
public class PdForcerInfoController {
    
    @Autowired
    private IHforcerInfoService forcerInfoService;

    /**
     * 分页列表查询
     *
     * @param hForcerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(HForcerInfo hForcerInfo,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<HForcerInfo> page = new Page<HForcerInfo>(pageNo, pageSize);
        IPage<HForcerInfo> pageList = forcerInfoService.selectList(page, hForcerInfo);
        return Result.ok(pageList);
    }


    /**
     *  编辑
     *
     * @param forcerInfo
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody HForcerInfo forcerInfo) {
        HForcerInfo info=  forcerInfoService.getById(forcerInfo.getId());
        info.setIsDisable(forcerInfo.getIsDisable());
        forcerInfoService.updateById(info);
        return Result.ok("编辑成功!");
    }
}
