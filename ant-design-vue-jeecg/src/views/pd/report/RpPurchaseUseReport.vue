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
                  v-model="queryParam.departIds"
                  placeholder="请选择科室"
                >
                  <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <!-- <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <a-tabs v-model="activeKey" @change="handleChangeTabs">

        <a-tab-pane tab="科室采购趋势表" key="1">
          <a-card :bordered="false">
            <bar-multid title="采购收费对照表" :height="height" :width="width" :dataSource="dataSource1"/>
            <line-chart-multid title="采购收费趋势表" :height="height" :width="width"/>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>

        <a-tab-pane tab="科室采购消耗表" key="2">
          <a-card :bordered="false">
            <bar-multid title="采购收费柱状图" :height="height" :width="width"  />
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
            <pie title="全院耗材占比" :height="height"  :dataSource="dataSource4"/>
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
    name: "RpPurchaseUseReport",
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
        dataSource1:[],
        dataSource2:[],
        dataSource3:[],
        dataSource4:[],
        // 表头
        queryParam: {},
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
          queryDepart: "/pd/pdDepart/queryListTree",
          queryPurchaseCountView: "/pd/pdStatisticalReport/queryPurchaseCountView",
          queryConsumptionView: "/pd/pdStatisticalReport/queryConsumptionView",
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
          this.useLoadData(1);//科室采购趋势表
        }else if(key==2){
          this.chargeUseLoadData(1);//科室采购消耗表
        }else if(key==3){
          this.noChargeUseLoadData(1);//科室采购环比——同比图
        }else if(key==4){
          this.allConsumptionData(1); //全院耗材占比
        }
      },
      /** 关闭按钮 **/
      handleCancel(){
        this.$emit('ok');
        this.close();
      },
      //关闭方法
      close() {
        this.queryParam = {};
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
      searchReset() {
        this.queryParam = {}
      },
      monthChange(date, dateString){
        this.queryParam.month=dateString;
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

      useLoadData(arg) {

        //获取采购收费对照表数据
          var params={};
          this.loading = true;
          getAction(this.url.queryPurchaseCountView, params).then((res) => {
            if (res.success) {
              this.dataSource1 = res.result;
            }else{
              this.$message.warning(res.message)
            }
            this.loading = false;
          })

      },


      useSearchQuery() {
        this.useLoadData();
      },

      useSearchReset() {
        this.queryParam = {}
        this.useLoadData();
      },






      chargeUseLoadData(arg) {
      },




      chargeUseSearchQuery() {
        this.chargeUseLoadData();
      },

      chargeUseSearchReset() {
        this.chargeUseQueryParam = {}
        this.chargeUseLoadData();
      },

      noChargeUseLoadData(arg){

      },


      allConsumptionData(arg) {//全院耗材占比
        var params={};
        this.loading = true;
        getAction(this.url.queryConsumptionView, params).then((res) => {
          if (res.success) {
            this.dataSource4 = res.result;
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },

    }
  }
</script>
<style scoped>
</style>