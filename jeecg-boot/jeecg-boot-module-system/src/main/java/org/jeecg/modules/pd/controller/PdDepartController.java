package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdCategory;
import org.jeecg.modules.pd.entity.PdDepartConfig;
import org.jeecg.modules.pd.service.IPdDepartConfigService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Description: 部门管理
* @Author: zxh
* @Date:   2020-01-19
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdDepart")
@Slf4j
public class PdDepartController extends JeecgController<PdDepartConfig, IPdDepartConfigService> {

    @Autowired
    private IPdDepartService pdDepartService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            SysDepart sysDepart = new SysDepart();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            sysDepart.setDepartParentId(sysUser.getDepartParentId());
            List<SysDepartTreeModel> list = pdDepartService.queryTreeList(sysDepart);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }


    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryPdDepartTreeList", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryPdDepartTreeList() {
        Result<Map<String, Object>> result = new Result<>();
        try {
            SysDepart sysDepart = new SysDepart();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            sysDepart.setDepartParentId(sysUser.getDepartParentId());
            List<SysDepartTreeModel> treeList = pdDepartService.queryTreeList(sysDepart);
            List<String> ids =  pdDepartService.findSysDepart(sysDepart);
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); // 全部树节点数据
            resMap.put("ids", ids);// 全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    /**
     * 用户角色授权功能，查询菜单权限树
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryPermissionTreeList", method = RequestMethod.GET)
    public Result<Map<String,Object>> queryTreeList(HttpServletRequest request) {
        Result<Map<String,Object>> result = new Result<>();
        //全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
            query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.eq(SysPermission::getBusinessType, PdConstant.PERMISSION_BUSINESS_TYPE_0);
            query.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> list = sysPermissionService.list(query);
            for(SysPermission sysPer : list) {
                ids.add(sysPer.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getPermissionTreeModelList(treeList, list, null);
            Map<String,Object> resMap = new HashMap<String,Object>();
            resMap.put("treeList", treeList); //全部树节点数据
            resMap.put("ids", ids);//全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getPermissionTreeModelList(List<TreeModel> treeList,List<SysPermission> metaList,TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(),permission.getRuleFlag(), permission.isLeaf());
            if(temp==null && oConvertUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                if(!tree.getIsLeaf()) {
                    getPermissionTreeModelList(treeList, metaList, tree);
                }
            }else if(temp!=null && tempPid!=null && tempPid.equals(temp.getKey())){
                temp.getChildren().add(tree);
                if(!tree.getIsLeaf()) {
                    getPermissionTreeModelList(treeList, metaList, tree);
                }
            }

        }
    }
}
