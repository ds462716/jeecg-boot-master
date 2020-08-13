package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.IPdStatisticalReportService;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.jeecg.modules.pd.vo.RpInAndOutDetailReportPage;
import org.jeecg.modules.pd.vo.RpReDetailReportPage;
import org.jeecg.modules.pd.vo.RpUseDetailReportPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: 统计报表
* @Author:
* @Date:   2020-08-13
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdStatisticalReport")
@Slf4j
public class PdStatisticalReportController extends JeecgController<PdStatisticalReport, IPdStatisticalReportService> {

    @Autowired
    private IPdStatisticalReportService pdStatisticalReportService;

    /**
     * zxh出入库明细统计报表
     * @param inDetail
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpInDetailReport")
    public Result<?> rpInDetailReport(PdStockRecordDetail inDetail,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        inDetail.setDepartParentId(sysUser.getDepartParentId());
        Page<PdStockRecordDetail> inPageDetail = new Page<PdStockRecordDetail>(pageNo, pageSize);
        IPage<PdStockRecordDetail> inPageDetailList = pdStatisticalReportService.rpInDetailReport(inPageDetail, inDetail);
        List<PdStockRecordDetail> inList = inPageDetailList.getRecords();
        List<RpInAndOutDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(inList), RpInAndOutDetailReportPage.class);
        Page<RpInAndOutDetailReportPage> inPage = new Page<RpInAndOutDetailReportPage>(pageNo, pageSize);
        inPage.setTotal(inPageDetailList.getTotal());
        inPage.setSize(inPageDetailList.getSize());
        inPage.setCurrent(inPageDetailList.getCurrent());
        inPage.setRecords(inReportList);
        return Result.ok(inPage);
    }


    /**
     * zxh用量明细统计报表
     * @param rpUseDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpUseDetailReport")
    public Result<?> rpUseDetailReport(RpUseDetailReportPage rpUseDetailReportPage,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpUseDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpUseDetailReportPage> usePageDetail = new Page<RpUseDetailReportPage>(pageNo, pageSize);
        IPage<RpUseDetailReportPage> usePageDetailList = pdStatisticalReportService.rpUseDetailReport(usePageDetail, rpUseDetailReportPage);
        List<RpUseDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpUseDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpUseDetailReportPage.class);
        Page<RpUseDetailReportPage> usePage = new Page<RpUseDetailReportPage>(pageNo, pageSize);
        usePage.setTotal(usePageDetailList.getTotal());
        usePage.setSize(usePageDetailList.getSize());
        usePage.setCurrent(usePageDetailList.getCurrent());
        usePage.setRecords(inReportList);
        return Result.ok(usePage);
    }

    /**
     * zxh用量明细统计报表
     * @param rpReDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpReDetailReport")
    public Result<?> rpReDetailReport(RpReDetailReportPage rpReDetailReportPage,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpReDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpReDetailReportPage> rePageDetail = new Page<RpReDetailReportPage>(pageNo, pageSize);
        IPage<RpReDetailReportPage> usePageDetailList = pdStatisticalReportService.rpReDetailReport(rePageDetail, rpReDetailReportPage);
        List<RpReDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpReDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpReDetailReportPage.class);
        Page<RpReDetailReportPage> rePage = new Page<RpReDetailReportPage>(pageNo, pageSize);
        rePage.setTotal(usePageDetailList.getTotal());
        rePage.setSize(usePageDetailList.getSize());
        rePage.setCurrent(usePageDetailList.getCurrent());
        rePage.setRecords(inReportList);
        return Result.ok(rePage);
    }

}
