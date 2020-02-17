<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
              <a-input placeholder="请选择科室" v-model="queryParam.storeroomId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请选择产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
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
    <div class="numberWARAP">
      <div class="total">总数量：<span id="pCount">{{this.validatorRules.pCount}}</span></div>
      <div class="nearTime">近效期数量：<span id="jCount">{{this.validatorRules.jCount}}</span></div>
      <div class="overTime">过期数量：<span id="gCount">{{this.validatorRules.gCount}}</span></div>
    <!--  <div class="overTime">久存产品数量：<span id="isLcount">{{this.validatorRules.isLcount}}</span></div>
      <div class="overTime">超出库房上下限产品数量：<span id="limtCount">{{this.validatorRules.limtCount}}</span></div>-->
    </div>
    <!-- 操作按钮区域 -->
     <div class="table-operator">
      <a-button @click="handleUpdate('Up')" type="primary" icon="plus">批量设置库存上限</a-button>
      <a-button @click="handleUpdate('Down')" type="primary" icon="plus">批量设置库存下限</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('库存明细')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">库存明细</a>&nbsp;&nbsp;&nbsp;
         <a  @click="handleRecordEdit(record)">出入库明细</a>
        </span>
      </a-table>
    </div>
<!--设置库存上下限弹出框-->
    <pdProductStockTotal-modal ref="modalForm1" @ok="modalFormOk"></pdProductStockTotal-modal>
    <!--库存明细查看页面-->
    <pdProductStock-modal ref="stockForm" @ok="modalFormOk"></pdProductStock-modal>
    <!--出入库明细查看页面-->
    <pd-stock-record-detail-info-modal ref="stockForm2" @ok="modalFormOk"></pd-stock-record-detail-info-modal>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import PdProductStockTotalModal from './modules/PdProductStockTotalModal'
  import PdProductStockModal from './modules/PdProductStockModal'
  import PdStockRecordDetailInfoModal from './modules/PdStockRecordDetailInfoModal'

  import { getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdProductStockTotalList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockTotalModal,
      PdProductStockModal,
      PdStockRecordDetailInfoModal
    },
    data () {
      return {
        description: '库存管理页面',
        validatorRules: {
          pCount:{},//总数量
          gCount:{},//过期数量
          jCount:{},//近效期数量
          isLcount:{},//久存产品数量
          limtCount:{},////超出库房上下限产品数量
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'storeroomName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'productNo'
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
            title:'库存上限',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'库存下限',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'是否过期',
            align:"center",
            dataIndex: 'expire',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['expire'], text+"")
              }
            }
          },
          {
            title:'是否永存',
            align:"center",
            dataIndex: 'isLong',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isLong'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/list",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
        },
        dictOptions:{
          expire:[],
          isLong:[]
        },

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.pCount=res.result.pCount;
            this.validatorRules.gCount=res.result.gCount;
            this.validatorRules.jCount=res.result.jCount;
            this.validatorRules.isLcount=res.result.isLcount;
            this.validatorRules.limtCount=res.result.limtCount;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      handleEdit: function (record) { //编译
        this.$refs.stockForm.edit(record);
        this.$refs.stockForm.title = "库存明细";
        this.$refs.stockForm.disableSubmit = false;
      },
      handleRecordEdit: function (record) { //编译
        this.$refs.stockForm2.edit(record);
        this.$refs.stockForm2.title = "出入库明细";
        this.$refs.stockForm2.disableSubmit = false;
      },
      handleUpdate(type) { //设置库存上限
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请勾选需要设置的数据！');
          return;
        }
        let  ids="";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var name="下限";
        if(type=='Up'){
            name="上限"
        }
        this.$refs.modalForm1.edit({"ids":ids,"type":type});
        this.$refs.modalForm1.title = "设置"+name;
        this.$refs.modalForm1.disableSubmit = false;
      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('pd_state').then((res) => { //是否过期字典转换
          if (res.success) {
            this.$set(this.dictOptions, 'expire', res.result)
          }
        })
        initDictOptions('pd_isLong').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isLong', res.result)
          }
        })
      }
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:30px;line-height:30px;margin:20px 0;}
  .numberWARAP>div{float:left;width:33%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
  /*.alert_close_btn:hover{color:#fff;border-bottom:1px solid #dedede}*/
  @import '~@assets/less/common.less'
</style>