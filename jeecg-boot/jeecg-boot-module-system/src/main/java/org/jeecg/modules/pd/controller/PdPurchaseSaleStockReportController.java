package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.service.IPdPurchaseSaleStockReportService;
import org.jeecg.modules.pd.vo.PdPurchaseSaleStockReportPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
