<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="注册证">
              <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="住院号">
              <a-input placeholder="请输入住院号" v-model="queryParam.inHospitalNo"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="型号">
                <a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,type:'radio',selectedRowKeys:selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
      <br>
      <br>
      <br>
      <div class="logisticsBox">
        <h3>院内物流追溯</h3>
        <div class="timeLIistBox" style="white-space: pre-wrap;" >
          <ul v-html="trackListHtml" class="trackList">
          </ul>
        </div>
      </div>
    </div>


  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getByOriginalProduct } from '@/api/api'
  import { httpAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from  '@/api/manage'

  export default {
    name: "PdStockLogList",
    mixins:[JeecgListMixin],
    components: {

    },
    data () {
      return {
        description: '院内物流追溯',
        loading:false,
        trackListHtml :"",
        // 表头
        columns: [
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            width:120,
            dataIndex: 'number'
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },
          {
            title:'批号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'注册证',
            align:"center",
            dataIndex: 'registration'
          },
        ],
        // 表头
        url: {
          list: "/pd/pdStockLog/getByOriginalProduct",
          getProdFlowInfo: "/pd/pdStockLog/getProdFlowInfo",
        },
        dictOptions:{
          logType:[],
        },
      }
    },
    computed: {

    },
    methods: {
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              //获取当前点击的是第几列
              let td = pathArray[0];
              let cellIndex = td.cellIndex;
              //获取tr
              let tr = pathArray[1];
              //获取一共多少列
              let lie = tr.childElementCount;
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex)!=1){
                  let recordId = record.id;
                  this.selectedRowKeys = [];
                  this.selectedRowKeys.push(recordId);
                  this.findStockLog(record);
                }
              }
            }
          }
        }
      },
      //按钮点击事件
      onSelectChange(selectedRowKeys, selectionRows){
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.findStockLog(selectionRows[0]);
      },
      findStockLog(stockLog){
        let formData = new URLSearchParams();
        formData.append("productId",stockLog.productId);
        formData.append("batchNo",stockLog.batchNo);
        formData.append("productBarCode",stockLog.productBarCode);
        formData.append("expDate",stockLog.expDate);
        let httpurl = this.url.getProdFlowInfo;
        httpAction(httpurl,formData, 'post').then((res)=>{
          if(res.success){
            let data = res.result;
            let len = data.length;
            let  html = "";
            for(let i = 0; i < len; i++){
              html += '<li>'
                +		'<i class="fa fa-dot-circle-o"></i>'
                +		'<span class="date">'+data[i].timeStr[0]+'</span>'
                +		'<span class="week">'+data[i].timeStr[1]+'</span>'
                +		'<span class="time">'+data[i].timeStr[2]+'</span>\t\t'
                +		'<span class="txt">'
                +			'<span class="txtType">'+filterMultiDictText(this.dictOptions['stockLogType'], data[i].logType+"")+'</span>\t\t'
                +			'<span class="txtNum">数量：'+data[i].productNum+'</span>\t\t'
                +			'<span class="txtVenderName">'+data[i].inFrom+'</span>'
                +          '<span class="txtHomeLevel">';
              if(data[i].outTo){
                html +=	'→'+data[i].outTo;

              }
              html +=   '</span><span class="txtHosCode">';
              if(data[i].patientInfo){
                html +=	data[i].patientInfo;

              }
              html +=	'</span>'
                +     '</span>'
                +  '</li>';
            }
            this.trackListHtml = html;
          }else{
            this.$message.warning(res.message);
          }
        })
      },
      initDictConfig(){
        initDictOptions('stock_log_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockLogType', res.result)
          }
        })
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
  .logisticsBox{margin-top:30px;}
  .logisticsBox>h3,.littleTip{float: left;font-weight: 400;color: #666;font-size: 14px;padding-right: 10px;width: 100px;line-height:30px;}
  .timeLIistBox{height: 180px;overflow: auto;padding:0 15px;border:1px solid #ccc;}
  .timeLIistBox .trackList{margin:0;}
  .timeLIistBox .trackList>li{ position: relative;padding: 9px 0 0 15px;line-height: 22px;border-left: 1px solid #ccc;color: #666;}
  .timeLIistBox .trackList>li.afterdate{padding-top: 0; }
  .timeLIistBox .trackList>li.afterdate>i.fa{top: -4px;padding-top: 10px;color:#62BC62;}
  .timeLIistBox .trackList>li.afterdate>.time{margin-left: 2px;}
  .timeLIistBox .trackList>li>i.fa{position: absolute;left: -6px;top: 15px;width: 11px;height: 11px;color: #ccc;background: #fff;}
  .timeLIistBox .trackList>li>.time{display: inline-block;width: 50px;margin-right: 20px;vertical-align: top;font-size: 12px;}
  .timeLIistBox .trackList>li>.txt{display: inline-block;max-width: 520px;color: #666;font-size: 12px;vertical-align: top;}
  .timeLIistBox .trackList>li>.date{width: 70px;color: #666;border-radius: 14px;font-size: 12px;text-align: left;display: inline-block;vertical-align: top;}
  .timeLIistBox .trackList>li>.week{padding: 0 10px;}
</style>