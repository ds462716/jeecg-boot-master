<template>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="useSearchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="年月">
                <a-month-picker placeholder="选择年月" @change="monthChange"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="科室">
                <a-select
                  mode="multiple"
                  showSearch
                  :departId="departValue"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="departHandleSearch"
                  @focus="departHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="useQueryParam.departIds"
                  placeholder="请选择科室"
                >
                  <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <a-tabs v-model="activeKey" @change="handleChangeTabs">

        <a-tab-pane tab="科室采购趋势表" key="1">
          <a-card :bordered="false">
            <bar-multid title="采购收费对照表" :height="height" :width="width"/>
            <line-chart-multid title="采购收费趋势表" :height="height" :width="width"/>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>

        <a-tab-pane tab="科室采购消耗表" key="2">
          <a-card :bordered="false">
            <bar-multid title="采购收费柱状图" :height="height" :width="width"/>
            <line-chart-multid title="采购收费趋势图" :height="height" :width="width"/>
               <div style="width:800px;height:400px;float:left">
            <pie title="采购科室金额占比"  :height="height"/>
               </div>
            <div style="width:800px;height:400px;float:right">
            <pie title="收费金额占比" :height="height"/>
             </div>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>



        <a-tab-pane tab="科室采购环比——同比图" key="3">
          <a-card :bordered="false">
            <bar-multid title="采购环比——同比图" :height="height" :width="width"/>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>



        <a-tab-pane tab="可收费——不可收费比例图" key="4">
          <a-card :bordered="false">
            <pie title="全院耗材占比" :height="height"/>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>
      </a-tabs>

    </a-card>
</template>

<script>
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import BarMultid from '@/components/chart/gzslyyChart/GzslyyBarMultid'
  import LineChartMultid from '@/components/chart/gzslyyChart/GzslyyLineChartMultid'
  import Pie from '@/components/chart/gzslyyChart/GzslPircePie'
  export default {
    name: "RpDepartUseReportMain",
    components: {
      BarMultid,LineChartMultid,Pie
    },
    data() {
      return {
        height: 320,
        width: 420,
        description: '报表页面',
        /* 查询折叠 */
        notFoundContent:"未找到内容",
        activeKey:"1",
        // 表头
        useQueryParam: {},
        departData: [],
        departValue: undefined,
        visible: false,
        initParams:{
          supplierId :"",
          yearMonth :""
        },
        popModal: {
          visible: false,
          width: '100%',
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
        url: {
          useList: "/pd/pdStatisticalReport/rpDepartUseDetailReport",
          chargeUseList:  "/pd/pdStatisticalReport/rpDepartUseDetailReport",
          noChargeUseList: "/pd/pdStatisticalReport/rpDepartUseDetailReport",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{

        },
      }
    },
    methods: {
      //tabs 切换事件
      handleChangeTabs(key){
        // 自动重置scrollTop状态，防止出现白屏
        getRefPromise(this, key).then(editableTable => {
          editableTable.resetScrollTop()
        })
        if(key==1){
          this.useLoadData(1);
        }else if(key==2){
          this.chargeUseLoadData(1);
        }else if(key==3){
          this.noChargeUseLoadData(1);
        }
      },
      /** 关闭按钮 **/
      handleCancel(){
        this.$emit('ok');
        this.close();
      },
      //关闭方法
      close() {
        this.useQueryParam = {};
        this.chargeUseQueryParam = {};
        this.noChargeUseQueryParam = {};
        this.visible = false;
        this.$emit('close');
      },
      //初始化
      show(initParams){
        this.visible = true;
        this.initParams = initParams;
        this.activeKey = "1";
        this.handleChangeTabs(1)
      },

      monthChange(date, dateString){
        this.useQueryParam.month=dateString;
      },

//科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value,departType:"1,2"}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end

      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        param.departIds = this.queryParam.departIds+"";
        return filterObj(param);
      },



      //供应商入库明细start
      useLoadData(arg) {

      },

      //入库明细搜索
      useSearchQuery() {
        this.useLoadData();
      },
      //入库明细重置
      useSearchReset() {
        this.useQueryParam = {}
        this.useLoadData();
      },

      //供应商入库明细end



      //供应商使用明细start
      chargeUseLoadData(arg) {
      },
      //获取使用条件
      getChargeUseQueryParams() {

      },


      //入库明细搜索
      chargeUseSearchQuery() {
        this.chargeUseLoadData();
      },
      //入库明细重置
      chargeUseSearchReset() {
        this.chargeUseQueryParam = {}
        this.chargeUseLoadData();
      },
      chargeUseHandleToggleSearch(){
      },

      //供应商退货明细start
      noChargeUseLoadData(arg) {

      },
      //获取使用条件
      getNoChargeUseQueryParams() {

      },
    }
  }
</script>
<style scoped>
</style>