<template>
    <a-card :bordered="false">
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="综合交易数据报表" key="1">
          <!--查询按钮开始-->
          <div class="table-page-search-wrapper">

          </div>
        <!--查询按钮结束-->
          <!-- 操作按钮区域 -->
          <div class="table-operator">
            <a-button type="primary" icon="download" @click="handleExportXls('综合交易数据报表')">导出图表</a-button>
          </div>
          <a-card :bordered="false">
            <!--综合交易数据报表-->
            <div :style="{ padding: '2px 2px 2px 2px' }" >
               <h4 :style="{ marginBottom: '2px' }">综合交易数据报表</h4>
              <v-chart   :options="polar1"  :style="{width:'1700px'}"  />
            </div>
            <div v-show="disableSubmit" style="width: 1600px;overflow-x: scroll;white-space: nowrap;" >
              <table id="contentTable" class="tableStyle">
                <tr >
                  <th v-for="(item, index) in hTable.columns">{{item}}</th>
                </tr>
                <tr>
                  <td v-for="(item, index) in hTable.dataSource">{{item}}</td>
                </tr>
              </table>
            </div>
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
  import YearPicker from '../modules/YearPicker'

  export default {
    name: "RpPurchaseUseReport",
    mixins:[JeecgListMixin],
    components: {
      'v-chart': ECharts,
      Pie,
      JDictSelectTagExpand,
      YearPicker
    },
    data() {
      return {
        height: 320,
        width: 420,
        description: '报表页面',
        /* 查询折叠 */
        notFoundContent: "未找到内容",
        activeKey: "1",
        disableSubmit:false,
        dataSource: [],
        // 表头
        queryParam1: {},
        visible: false,
        popModal: {
          visible: false,
          width: '100%',
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
//------------------------------
        polar1: {
          backgroundColor: '#ffffff',
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          xAxis: {
            type: 'category',
            axisPointer: {type: 'shadow'},
            axisLabel: {
              fontFamily:'微软雅黑',
              interval: 0,
              fontSize:'16',
            },

            data: []
          },
          yAxis: {
            type: 'value',
            name: '金额(万元)',
            axisLabel: {
              fontSize:'16',
              fontFamily:'微软雅黑',
            }
          },
          series: [{
            data: [],
            type: 'line',
          }],
          dataZoom: [{
            type: 'slider',
            show: true, //flase直接隐藏图形
            xAxisIndex: [0],
            left: '9%', //滚动条靠左侧的百分比
            bottom: -5,
            start: 0,//滚动条的起始位置
            end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
          }],
          label:{show:true},
        },
//------------------------------
        //采购环比表头
        hTable: {
          loading:false,
          dataSource: [],
          columns: [],
        },
        url: {
          queryConsolidatedDataView: "/pd/pdStatisticalReport/queryConsolidatedDataView",
          queryMoOnMoView: "/pd/pdStatisticalReport/queryMoOnMoView",
          exportXlsUrl: "/pd/pdStatisticalReport/queryConsolidatedExportXls",
        },
        dictOptions: {},
      }
    },
    computed: {

    },
    methods: {
      /** 重写导出方法 **/
      handleExportXls(fileName){
         fileName =  fileName + "_" + new Date().toLocaleString();
        let params ={};
        downFile(this.url.exportXlsUrl,params).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        });

        var canvas=document.getElementsByTagName('canvas')[0];
        let image = canvas.toDataURL({
          type:"png",
          pixelRatio: 2,
        });
        let alink = document.createElement("a");
        alink.href = image;
        alink.download = "综合交易数据趋势图"; //导出的图片名
        alink.click();
      },


      loadData() {
        this.visible = true;
        this.activeKey = "1";
        this.handleChangeTabs(1);
      },

      //tabs 切换事件
      handleChangeTabs(key) {
        // 自动重置scrollTop状态，防止出现白屏
        getRefPromise(this, key).then(editableTable => {
          editableTable.resetScrollTop()
        })
        if (key == 1) {
          this.useLoadData(1);//科室采购趋势表
        }
      },

      //科室采购趋势表
      useLoadData(arg) {
        //获取采购收费对照表数据
        var params = {};
        this.loading = true;
        getAction(this.url.queryConsolidatedDataView, params).then((res) => {
          if (res.success) {
            this.polar1.xAxis.data = res.result.xAxis;//月份
            this.polar1.series[0].data = res.result.series;//金额
            if(res.result.series.length>0){
              let columns = new Array();
              let xAxis = res.result.xAxis;
              let series = res.result.series;
              columns.push("对象/月份");
              //表格头
              for(let item of xAxis){
                columns.push(item);
              }
              columns.push("各月累计（万元）");
              let  tableData = new Array();
              tableData.push("数值");
              let total =0;
              for(let item of series){
                tableData.push(item);
                total +=item;
              }
              tableData.push(total);
              this.hTable.columns = columns;
              this.hTable.dataSource = tableData;
              //如果没数据则不显示表格
              this.disableSubmit = true;
            }
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })

      },
    }
  }
</script>
<style scoped>
  .tableStyle> tr > th{
    border: 1px solid #e8e8e8;
    text-align: center;
    padding: 16px 16px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    background: #fafafa;
    transition: background 0.3s ease;
    display: table-cell;
    vertical-align: inherit;
    box-sizing: border-box;
  }
  .tableStyle> tr > td{
    border:1px solid #e8e8e8;
    text-align: center;
    padding: 16px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

</style>