<template>
   <!-- <v-chart :options="polar"/>-->
    <div :style="{ padding: '0 0 32px 32px' }"  >
      <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
      <v-chart :options="polar"  :style="{ width:'1600px'}"  />
    </div>
</template>

<script>
  import { DataSet } from '@antv/data-set'
  import { ChartEventMixins } from '@/components/chart/mixins/ChartMixins'
  import { httpAction ,getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { makeWb } from '@/utils/wubi'
  import {duplicateCheckHasDelFlag } from '@/api/api'
  import ECharts from 'vue-echarts'
  import "echarts/lib/chart/bar";
  import "echarts/lib/chart/line";
  import "echarts/lib/chart/pie";
  import "echarts/lib/component/tooltip";
  import "echarts/lib/component/legend";
  import "echarts/lib/component/markPoint";
  import "echarts/lib/component/markLine";
  import "echarts/lib/component/graphic";
   export default {
    name: "PdGroupModal",
     mixins: [ChartEventMixins],
    components: {
      'v-chart': ECharts
    },
     dataSource: [],
    data() {
      return {
        title: "采购收费趋势图",
        polar: {
          height: 300,
          title: {text: '采购收费趋势图',},
          tooltip: {trigger: 'axis'},
          legend: {data: ['采购金额', '收费金额']},
          toolbox: {show: true,},
          xAxis: {
            type: 'category',
            //data: ['一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份', '一月份']
            data:[]
          },
          yAxis: {},
          series: [
            {
              name: '采购金额',
              type: 'line',
              //data: [1100, -11, 150, 13, 12, 13, 10],
              data:[]
            },
            {
              name: '收费金额',
              type: 'line',
              //data: [100, -2, 20, 50, 3, 2, 0],
              data:[]
            }
          ],
          label: {show: true}
        },
        url: {
          queryPurchaseCountView: "/pd/pdStatisticalReport/queryPurchaseCountView",
        },
      }
    },

    created () {
      this.useLoadData();
    },
     computed: {
     },
    methods: {
//科室采购趋势表
      useLoadData() {
        //获取采购收费对照表数据
         var params = {};
        this.loading = true;
      getAction(this.url.queryPurchaseCountView, params).then((res) => {
          if (res.success) {
             this.polar.xAxis.data  = res.result.visitFields1;//采购收费趋势表
             this.polar.series  = res.result.dataSource1;//采购收费趋势表
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })

      },
    }
    }
</script>