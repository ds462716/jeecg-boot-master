<template>
  <div :style="{ padding: '0 0 32px 32px' }"  >
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :options="polar"  :style="{width:'1600px'}"  />
  </div>
</template>

<script>

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
  import "echarts/lib/component/graphic";
  export default {
    name: "HbBarMultid",
    components: {
      'v-chart': ECharts
    },
    data() {
      return {
        title: "采购环比——同比图",
        polar: {
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
            //data: ['6月份', '7月份', '涨幅']
            data: []
          },
          xAxis: [
            {
              type: 'category',
              //data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
              data:[],
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '金额',
              min: 0,
              max: 2000000,
              interval: 1000000,
              axisLabel: {
                formatter: '{value}'
              }
            },
            {
              type: 'value',
              name: '涨幅',
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
              //data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
              data:[]
            },
            {
              name: '7月份',
              type: 'bar',
              //data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
              data:[]
            },
            {
              name: '涨幅',
              type: 'line',
              yAxisIndex: 1,
              //data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
              data:[]
            }
          ],
          label:{show:true},
        },
        url: {
          queryPurchaseAmountMomTableView: "/pd/pdStatisticalReport/queryPurchaseAmountMomTableView",
        },
      }
    },

    created () {
       this.useLoadData();
    },
    methods: {
      useLoadData() {
        //获取采购收费对照表数据
        var params = {};
        this.loading = true;
        getAction(this.url.queryPurchaseAmountMomTableView, params).then((res) => {
          if (res.success) {
            let resultMap = res.result;
            this.polar.xAxis[0].data = resultMap.xAxis;
            console.log(this.polar.xAxis);
            this.polar.series[0].name = resultMap.legends[0];
            console.log( this.polar.series[0].name);
            this.polar.series[1].name = resultMap.legends[1];
            console.log(this.polar.series[1].name);
            this.polar.series[2].name = resultMap.legends[2];
            console.log(this.polar.series[2].name);
            this.polar.series[0].data = resultMap.seriesData1;
            console.log(this.polar.series[0].data);
            this.polar.series[1].data = resultMap.seriesData2;
            console.log(this.polar.series[1].data);
            this.polar.series[2].data = resultMap.seriesData3;
            console.log(this.polar.series[2].data);
            this.polar.legend.data = resultMap.legends;
            console.log(this.polar.legend.data);
          }
          this.loading = false;
        })

      },
    }
  }
</script>