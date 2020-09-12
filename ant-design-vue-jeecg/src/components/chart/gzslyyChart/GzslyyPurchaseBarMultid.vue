<template>
  <!-- <v-chart :options="polar"/>-->
  <div :style="{ padding: '0 0 32px 32px' }"  >
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :options="polar"  :style="{width:'1600px'}"  />
  </div>
</template>
<script>
  import { DataSet } from '@antv/data-set'
  import { httpAction ,getAction} from '@/api/manage'
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
  import "echarts/lib/component/graphic"

  export default {
    name: "GzslyyPurchaseBarMultid",
    components: {
      'v-chart': ECharts
    },
    data() {
      return {
       title: "采购收费对照图",
        dataSource: [],
        polar: {
          dataSource: [],
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
              //data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
              data: [],
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [{type: 'value'}],
          series: [
            {
              name: '采购金额',
              type: 'bar',
              //data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
              data: []
            },
            {
              name: '收费金额',
              type: 'bar',
             // data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
              data: []
            }
          ],
          label:{show:true},

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
      useLoadData() {
        alert("e:");
        var params = {};
        this.loading = true;
        getAction(this.url.queryPurchaseCountView, params).then((res) => {
          if (res.success) {
            this.polar.xAxis[0].data  = res.result.visitFields1;//采购收费趋势表
            this.polar.series  = res.result.dataSource1;//采购收费趋势表
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      }
    }
  }
</script>

