package org.jeecg.modules.external.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.pd.entity.PdNumericalInf;
import org.jeecg.modules.pd.service.IPdNumericalInfService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Api(tags="耗材/试剂使用统计报表")
@RestController
@RequestMapping("/external/pdNumericalInf")
@Slf4j
public class PdNumericalInfController extends JeecgController<PdNumericalInf, IPdNumericalInfService> {



}
