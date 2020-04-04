<template>
  <div :style="{'width':width==null?'auto':width+'px'}">
    <v-chart :forceFit="width==null" :height="height" :data="data" padding="0">
      <v-tooltip/>
      <v-bar position="x*y"/>
    </v-chart>
  </div>
</template>

<script>
  import moment from 'dayjs'
  import {getAction } from '@/api/manage'
  const sourceData = []
  const beginDay = new Date().getTime()

  for (let i = 0; i < 10; i++) {
    sourceData.push({
      x: moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'),
      y: Math.round(Math.random() * 10)
    })
  }

  const tooltip = [
    'x*y',
    (x, y) => ({
      name: x,
      value: y
    })
  ]

  const scale = [{
    dataKey: 'x',
    min: 2
  }, {
    dataKey: 'y',
    title: '时间',
    min: 1,
    max: 30
  }]

  export default {
    name: 'MiniBarDosage',
    props: {
      dataSource: {
        type: Array,
        default: () => []
      },
      width: {
        type: Number,
        default: null
      },
      height: {
        type: Number,
        default: 200
      }
    },
    created() {
      this.initLogInfo();
    },
    methods: {
      initLogInfo (){
        getAction("/pd/IndexChart/dosageDateList", {}).then((res) => {
          if (res.success) {
            this.data= res.result.dosageDate;
          } else {
            this.$message.warning(res.message)
          }
        })
      }
    },
    data() {
      return {
        tooltip,
        data: [],
        scale
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "chart";
</style>