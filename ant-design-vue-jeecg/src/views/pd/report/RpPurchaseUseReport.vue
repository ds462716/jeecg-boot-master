<template>
    <a-card :bordered="false">
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="科室采购趋势表" key="1">
          <!--查询按钮开始-->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery(1)">
              <a-row :gutter="24">
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
                      v-model="queryParam1.departIds"
                      placeholder="请选择科室"
                    >
                      <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
               <a-button type="primary" @click="searchQuery(1)" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset(1)" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
        <!--查询按钮结束-->
          <a-card :bordered="false">
            <!--采购收费对照图-->
            <div :style="{ padding: '0 0 32px 32px' }"  >
              <h4 :style="{ marginBottom: '20px' }">采购收费对照图</h4>
              <v-chart :options="polar1"  :style="{width:'1600px'}"  />
            </div>
            <!--采购收费趋势图-->
            <div :style="{ padding: '0 0 32px 32px' }"  >
              <h4 :style="{ marginBottom: '20px' }">采购收费趋势图</h4>
              <v-chart :options="polar2"  :style="{ width:'1600px'}"  />
            </div>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>

        <a-tab-pane tab="科室采购消耗表" key="2">
          <!--查询按钮开始-->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery(2)">
              <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="年月">
                    <a-month-picker placeholder="选择年月" @change="monthChange" v-model="queryParam2.ym"/>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
               <a-button type="primary" @click="searchQuery(2)" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset(2)" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <!--查询按钮结束-->
          <a-card :bordered="false">
            <!--采购收费柱状图-->
            <div :style="{ padding: '0 0 32px 32px' }"  >
              <h4 :style="{ marginBottom: '20px' }">采购收费柱状图</h4>
              <v-chart :options="polar3"  :style="{ width:'1600px'}"  />
            </div>
            <!--采购收费趋势图-->
            <div :style="{ padding: '0 0 32px 32px' }"  >
              <h4 :style="{ marginBottom: '20px' }">采购收费趋势图</h4>
              <v-chart :options="polar4"  :style="{ width:'1600px'}"  />
            </div>
             <div style="width:800px;height:400px;float:left">
            <pie title="采购科室金额占比"  :height="height"  :dataSource="dataSource5"/>
               </div>
            <div style="width:800px;height:400px;float:right">
            <pie title="收费金额占比" :height="height"  :dataSource="dataSource6"/>
             </div>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>



        <a-tab-pane tab="科室采购环比——同比图" key="3">
          <!--查询按钮开始-->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery(3)">
              <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="年月">
                    <a-month-picker placeholder="选择年月" @change="monthChangeUse" v-model="queryParam3.ym"/>
                  </a-form-item>
                </a-col>
                <!--<a-col :md="6" :sm="8">
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
                      v-model="queryParam3.departIds"
                      placeholder="请选择科室"
                    >
                      <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>-->
                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <a-button type="primary" @click="searchQuery(3)" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset(3)" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <!--查询按钮结束-->
          <a-card :bordered="false">
            <!--采购环比——同比图-->
            <div :style="{ padding: '0 0 32px 32px' }"  >
              <h4 :style="{ marginBottom: '20px' }">采购环比——同比图</h4>
              <v-chart :options="polar5"  :style="{ width:'1600px'}"  />
            </div>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>



        <a-tab-pane tab="可收费——不可收费比例图" key="4">
          <!--查询按钮开始-->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery(4)">
                <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="年月">
                    <a-month-picker placeholder="选择年月" @change="monthChangeAllConion" v-model="queryParam4.ym"/>
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
                      v-model="queryParam4.departIds"
                      placeholder="请选择科室"
                    >
                      <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery(4)" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset(4)" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <!--查询按钮结束-->
          <a-card :bordered="false">
            <pie title="全院耗材占比"  :height="height"  :dataSource="dataSource8"/>
            <div class="table-operator">
            </div>
          </a-card>
        </a-tab-pane>
      </a-tabs>

    </a-card>
</template>

<script>
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj ,validateDuplicateValue} from '@/utils/util';
  import { VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import Pie from '@/components/chart/gzslyyChart/GzslPircePie'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ECharts from 'vue-echarts'
  import { makeWb } from '@/utils/wubi'
  import {duplicateCheckHasDelFlag } from '@/api/api'
  import "echarts/lib/chart/bar";
  import "echarts/lib/chart/line";
  import "echarts/lib/chart/pie";
  import "echarts/lib/component/tooltip";
  import "echarts/lib/component/legend";
  import "echarts/lib/component/markPoint";
  import "echarts/lib/component/markLine";
  import "echarts/lib/component/graphic"
  import { DataSet } from '@antv/data-set'

  export default {
    name: "RpPurchaseUseReport",
    mixins:[JeecgListMixin],
    components: {
      'v-chart': ECharts,
      Pie,
      JDictSelectTagExpand,
    },
    data() {
      return {
        height: 320,
        width: 420,
        description: '报表页面',
        /* 查询折叠 */
        notFoundContent: "未找到内容",
        activeKey: "1",
        dataSource: [],
        visitFields1: [],
        visitFields2: [],
        visitFields3: [],
        dataSource2: [],
        dataSource3: [],
        dataSource4: [],
        dataSource5: [],
        dataSource6: [],
        dataSource7: [],
        dataSource8: [],
        // 表头
        queryParam1: {},
        queryParam2: {},
        queryParam3: {},
        queryParam4: {},
        departData: [],
        departValue: undefined,
        visible: false,
        popModal: {
          visible: false,
          width: '100%',
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
//--------------------
        polar1: {
          //title:"采购收费对照图",
          tooltip: {trigger: 'axis'},
          legend: {data: ['采购金额', '收费金额']},
          toolbox: {
            show: true,
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          xAxis: [{type: 'category',data: [],axisPointer: {type: 'shadow'}}],
          yAxis: [{type: 'value'}],
          series: [
            {name: '采购金额', type: 'bar', data: []},
            {name: '收费金额', type: 'bar', data: []}
                  ],
          label:{show:true},
        },
 //----------------------
        polar2: {
          height: 300,
          tooltip: {trigger: 'axis'},
          legend: {data: ['采购金额', '收费金额']},
          toolbox: {show: true,},
          xAxis: {
            type: 'category',
            data:[]
          },
          yAxis: {},
          series: [
            {
              name: '采购金额',
              type: 'line',
              data:[]
            },
            {
              name: '收费金额',
              type: 'line',
              data:[]
            }
          ],
          label: {show: true}
        },
//-----------------
        polar3: {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['采购金额', '收费金额']
          },
          toolbox: {
            show: true,
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          xAxis: [
            {
              type: 'category',
              data: [],
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [{
            type: 'value'
          }],
          series: [
            {
              name: '采购金额',
              type: 'bar',
              data: []
            },
            {
              name: '收费金额',
              type: 'bar',
              data: []
            }
          ],
          label:{show:true},

        },
//------------------
        polar4: {
          height: 300,
          tooltip: {trigger: 'axis'},
          legend: {data: ['采购金额', '收费金额']},
          toolbox: {show: true},
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {},
          series: [
            {
              name: '采购金额',
              type: 'line',
              data: []
            },
            {
              name: '收费金额',
              type: 'line',
              data: []
            }
          ],
          label: {show: true}
        },
//-------------------
        polar5: {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999'
              }
            }
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          legend: {
            data: []
          },
          xAxis: [
            {
              type: 'category',
              data:[],
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '金额(RMB)',
              min: 0,
              max: 2000000,
              interval: 500000,
              axisLabel: {
                formatter: '{value}'
              }
            },
            {
              type: 'value',
              name: '涨幅(%)',
              min: -100,
              max: 100,
              interval: 20,
              axisLabel: {
                formatter: '{value} %'
              }
            }
          ],
          series: [
            {
              name: '6月份',
              type: 'bar',
              data:[]
            },
            {
              name: '7月份',
              type: 'bar',
              data:[]
            },
            {
              name: '涨幅',
              type: 'line',
              yAxisIndex: 1,
              data:[]
            }
          ],
          label:{show:true},
        },
//-------------------
        url: {
          queryDepart: "/pd/pdDepart/queryListTree",
          queryPurchaseCountView: "/pd/pdStatisticalReport/queryPurchaseCountView",
          queryConsumptionView: "/pd/pdStatisticalReport/queryConsumptionView",
          queryDepartContionView: "/pd/pdStatisticalReport/queryDepartContionView",
          queryPurchaseAmountMomTableView: "/pd/pdStatisticalReport/queryPurchaseAmountMomTableView",
        },
        dictOptions: {},
      }
    },
    computed: {

    },
    methods: {

      loadData() {
        this.visible = true;
        this.activeKey = "1";
        this.handleChangeTabs(1)
      },

      //tabs 切换事件
      handleChangeTabs(key) {
        // 自动重置scrollTop状态，防止出现白屏
        getRefPromise(this, key).then(editableTable => {
          editableTable.resetScrollTop()
        })
        if (key == 1) {
          this.useLoadData(1);//科室采购趋势表
        } else if (key == 2) {
          this.chargeUseLoadData(1);//科室采购消耗表
        } else if (key == 3) {
          this.noChargeUseLoadData(1);//科室采购环比——同比图
        } else if (key == 4) {
          this.allConsumptionData(1); //全院耗材占比
        }
      },

      monthChange(date, dateString) {
        this.queryParam2.yearMonth = dateString;
      },
      monthChangeUse(date, dateString) {
        this.queryParam3.yearMonth = dateString;
      },
      monthChangeAllConion(date, dateString) {
        this.queryParam4.yearMonth = dateString;
      },

//科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart, {departName: value, departType: "1,2"}).then((res) => {
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end
      //获取使用条件
      getQueryParams(num) {
        //获取查询条件
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        let param ={};
        if(num==1){
           param = Object.assign(sqp, this.queryParam1, this.isorter ,this.filters);
          param.departIds = this.queryParam1.departIds+"";
        }else if(num==2){
          param = Object.assign(sqp, this.queryParam2, this.isorter ,this.filters);
          param.departIds = this.queryParam2.departIds+"";
        }else if(num==3){
          param = Object.assign(sqp, this.queryParam3, this.isorter ,this.filters);
          param.departIds = this.queryParam3.departIds+"";
        }else if(num==4){
          param = Object.assign(sqp, this.queryParam4, this.isorter ,this.filters);
          param.departIds = this.queryParam4.departIds+"";
        }
        return filterObj(param);
      },





      //科室采购趋势表
      useLoadData(arg) {
       //获取采购收费对照表数据
         var params = {};
         params = this.getQueryParams(1);//查询条件
        this.loading = true;
        getAction(this.url.queryPurchaseCountView, params).then((res) => {
          if (res.success) {
            this.polar1.xAxis[0].data  = res.result.visitFields1;//采购收费趋势表
            this.polar1.series  = res.result.dataSource1;//采购收费趋势表
            this.polar2.xAxis.data  = res.result.visitFields1;//采购收费趋势表
            this.polar2.series  = res.result.dataSource1;//采购收费趋势表
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })

      },


      searchQuery(num) {
        if(num==1){
          this.useLoadData();
        }else if(num==2){
          this.chargeUseLoadData();//科室采购消耗表
        }else if(num==3){
          this.noChargeUseLoadData();
        }else if(num==4){
          this.allConsumptionData();
        }
      },


      //科室采购消耗表
      chargeUseLoadData(arg) {
         var params = {};
        params = this.getQueryParams(2);//查询条件
        this.loading = true;
        getAction(this.url.queryDepartContionView, params).then((res) => {
          if (res.success) {
            this.polar3.xAxis[0].data  = res.result.visitFields2;//采购收费趋势表
            this.polar3.series  = res.result.dataSource3;//采购收费趋势表
            this.polar4.xAxis.data  = res.result.visitFields2;//采购收费趋势表
            this.polar4.series  = res.result.dataSource3;//采购收费趋势表
            this.dataSource5 = res.result.dataSource5;//采购科室金额占比
            this.dataSource6 = res.result.dataSource6;//收费金额占比
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },


      //科室采购环比——同比图
      noChargeUseLoadData() {
        //获取采购收费对照表数据
        var params = {};
        params = this.getQueryParams(3);//查询条件
        this.loading = true;
        getAction(this.url.queryPurchaseAmountMomTableView, params).then((res) => {
          if (res.success) {
            let resultMap = res.result;
            this.polar5.xAxis[0].data = resultMap.xAxis;
            this.polar5.series[0].name = resultMap.legends[0];
            this.polar5.series[1].name = resultMap.legends[1];
            this.polar5.series[2].name = resultMap.legends[2];
            this.polar5.series[0].data = resultMap.seriesData1;
            this.polar5.series[1].data = resultMap.seriesData2;
            this.polar5.series[2].data = resultMap.seriesData3;
            this.polar5.legend.data = resultMap.legends;
          }
          this.loading = false;
        })
      },

      //全院耗材占比
      allConsumptionData(arg) {
         var params = {};
        params = this.getQueryParams(4);//查询条件
        this.loading = true;
        getAction(this.url.queryConsumptionView, params).then((res) => {
          if (res.success) {
            this.dataSource8 = res.result;
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },



      /** 关闭按钮 **/
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      //关闭方法
      close() {
        this.queryParam1 = {};
        this.queryParam2 = {};
        this.queryParam3 = {};
        this.queryParam4 = {};
        this.visible = false;
        this.$emit('close');
      },

      searchReset(num) {
        if(num==1){
          this.queryParam1 = {};
        }else if(num==2){
          this.queryParam2 = {};
        }else if(num==3){
          this.queryParam3 = {};
        }else if(num==4){
          this.queryParam4 = {};
        }
      },
    },
  }
</script>
<style scoped>
</style>