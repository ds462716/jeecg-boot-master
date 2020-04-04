<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }"  @click="deomtest('purchase')">
        <!--<chart-card :loading="loading" title="总采购量" :total="this.model.orderCount | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px;">
              <span slot="term">周同比</span>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">日同比</span>
              11%
            </trend>
          </div>
          <template slot="footer">日均采购量<span>23</span></template>
        </chart-card>-->
        <chart-card :loading="loading" title="总采购量" :total="this.model.orderCount | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area-order />
          </div>
          <template slot="footer">今日采购量<span> {{ this.model.dayOrderNum | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="deomtest('apply')">
        <chart-card :loading="loading" title="总申领量" :total="this.model.applyCount | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <!--<mini-area />-->
            <mini-area-apply :height="40" />
          </div>
          <template slot="footer">今日领用量<span> {{this.model.dayApplyNum | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }"  @click="deomtest('dosage')">
        <chart-card :loading="loading" title="总使用量" :total="this.model.dosageCount | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <!--<mini-bar :height="40" />-->
            <mini-bar-dosage :height="40" />
          </div>
          <template slot="footer">今日使用量 <span>{{this.model.dayDosageNum | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="deomtest('stock')">
        <chart-card :loading="loading" title="总库存量" :total="this.model.stockCount | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar-stock :height="40" />
          </div>
          <template slot="footer">今日入库量 <span>{{this.model.dayRecordNum | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <!-- <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
         <chart-card :loading="loading" title="运营活动效果" total="78%">
           <a-tooltip title="指标说明" slot="action">
             <a-icon type="info-circle-o" />
           </a-tooltip>
           <div>
             <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" :height="8" />
           </div>
           <template slot="footer">
             <trend flag="down" style="margin-right: 16px;">
               <span slot="term">同周比</span>
               12%
             </trend>
             <trend flag="up">
               <span slot="term">日环比</span>
               80%
             </trend>
           </template>
         </chart-card>
       </a-col>-->
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{width: '256px'}" />
          </div>
          <a-tab-pane loading="true" tab="金额" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-total :title="this.totalTitle" :dataSource="barData"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
<!--
                <rank-list title="耗材类别" :list="rankList"/>
-->
                <rank-total-list title="耗材类别(金额)" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="数量" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-count :title="this.countTitle" :dataSource="barData"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-count-list  title="耗材类别(数量)" :list="rankList"/>
<!--
                <rank-list title="耗材类别" :list="rankList"/>
-->
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="耗材分类金额统计(RMB)" :style="{ marginTop: '24px' }">
          <!--<a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="environment" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="team" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="rise" style="font-size: 24px"  />
              </a-spin>
            </a-col>
          </a-row>-->
          <new-line-chart-multid :fields="visitFields" :dataSource="visitInfo"></new-line-chart-multid>
<!--
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
-->
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import MiniAreaOrder from '@/components/chart/MiniAreaOrder'
  import MiniAreaApply from '@/components/chart/MiniAreaApply'
  import MiniBarDosage from '@/components/chart/MiniBarDosage'
  import MiniBarStock from '@/components/chart/MiniBarStock'
  import MiniProgress from '@/components/chart/MiniProgress'
  import RankCountList from '@/components/chart/RankCountList'
  import RankTotalList from '@/components/chart/RankTotalList'
  import BarCount from '@/components/chart/BarCount'
  import BarTotal from '@/components/chart/BarTotal'
  import newLineChartMultid from '@/components/chart/newLineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'

  import Trend from '@/components/Trend'
  import { getLoginfo,getVisitInfo } from '@/api/api'
  import { httpAction,getAction } from '@/api/manage'

  const rankList = []
 /* for (let i = 0; i < 7; i++) {
    rankList.push({
      name: '类别 ' + (i+1) + ' ',
      total: 1234.56 - i * 100
    })
  }*/
  const barData = []
 /* for (let i = 0; i < 12; i += 1) {
    barData.push({
      x: `${i + 1}月`,
      y: Math.floor(Math.random() * 1000) + 200
    })
  }*/
  export default {
    name: "IndexChart",
    components: {
      ATooltip,
      ACol,
      ChartCard,
      MiniAreaOrder,
      MiniAreaApply,
      MiniBarDosage,
      MiniBarStock,
      MiniProgress,
      RankCountList,
      RankTotalList,
      BarCount,
      BarTotal,
      Trend,
      newLineChartMultid,
      HeadInfo
    },
    data() {
      return {
        loading: true,
        center: null,
        rankList,
        barData,
        model:{},
        totalTitle:"采购金额统计",//默认
        countTitle:"采购数量统计",//默认
        loginfo:{},
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: <a-icon type="loading" style="font-size: 24px" spin />
    }
    },
    created() {
      setTimeout(() => {
        this.loading = !this.loading}, 1000)
      this.initLogInfo();
      this.initOrderTotalList();
    },
    methods: {
      deomtest(type){
        if(type=="apply"){
          this.totalTitle="申领金额统计";
          this.countTitle="申领数量统计";
        }else  if(type=="dosage"){
          this.totalTitle="使用金额统计";
          this.countTitle="使用数量统计";
        }else if(type=="stock") {
          this.totalTitle="库存金额统计";
          this.countTitle="库存数量统计";
        }else{
          this.totalTitle="采购金额统计";
          this.countTitle="采购数量统计";
        }

        this.initOrderTotalList(type);
      },

      initLogInfo () {
        //加载总数量和日统计量
        getAction("/pd/IndexChart/list",{}).then((res) => {
          if (res.success) {
            this.model = res.result;
          }else{
            this.$message.warning(res.message)
          }
        })
        /*getLoginfo(null).then((res)=>{
          if(res.success){
            Object.keys(res.result).forEach(key=>{
              res.result[key] =res.result[key]+""
            })
            this.loginfo = res.result;
          }
        })*/
        /* getVisitInfo().then(res=>{
          if(res.success){
            console.log("aaaaaa",res.result)
            this.visitInfo = res.result;
          }
        }) */
      },



      initOrderTotalList (type) {
        //加载条柱形数据  根据类别统计采购数量和金额
        getAction("/pd/IndexChart/orderTotalList",{type:type}).then((res) => {
          if (res.success) {
             this.barData = res.result.orderCountDate;
             this.rankList = res.result.orderCountDate;
             this.visitInfo = res.result.orderCountDate;
          }else{
            this.$message.warning(res.message)
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .circle-cust{
    position: relative;
    top: 28px;
    left: -100%;
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  /* 首页访问量统计 */
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: rgba(0, 0, 0, .45);
      display: inline-block;
      font-size: .95rem;
      line-height: 42px;
      margin-bottom: 4px;
    }
    p {
      line-height: 42px;
      margin: 0;
      a {
        font-weight: 600;
        font-size: 1rem;
      }
    }
  }
</style>