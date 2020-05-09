package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.service.IPdPurchaseSaleStockReportService;
import org.jeecg.modules.pd.vo.PdPurchaseSaleStockReportPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiangxz
 * @description 进销存报表
 * @date 2020-5-9
 */
@RestController
@RequestMapping("/pd/pdPurchaseSaleStockReport")
@Slf4j
public class PdPurchaseSaleStockReportController extends JeecgController<PdPurchaseSaleStockReportPage, IPdPurchaseSaleStockReportService> {

    @Autowired
    private IPdPurchaseSaleStockReportService pdPurchaseSaleStockReportService;

    /**
     * 进销存报表
     * @param vo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdPurchaseSaleStockReportPage vo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdPurchaseSaleStockReportPage> page = new Page<PdPurchaseSaleStockReportPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        vo.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdPurchaseSaleStockReportPage> pageList = pdPurchaseSaleStockReportService.selectList(page,vo);
        return Result.ok(pageList);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPurchaseSaleStockReportPage vo) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        vo.setDepartParentId(sysUser.getDepartParentId());

        List<PdPurchaseSaleStockReportPage> list =  pdPurchaseSaleStockReportService.selectList(vo);
        List<PdPurchaseSaleStockReportPage> exportList = JSON.parseArray(JSON.toJSONString(list), PdPurchaseSaleStockReportPage.class);

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "进销存报表");
        mv.addObject(NormalExcelConstants.CLASS, PdPurchaseSaleStockReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("进销存报表数据", "导出人:" + sysUser.getRealname(), "进销存报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
}
