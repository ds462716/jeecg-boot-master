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
      <div class="total">总数量：<span id="totalNum">22</span></div>
      <div class="nearTime">近效期数量：<span id="nearNum">33</span></div>
      <div class="overTime">过期数量：<span id="overNum">44</span></div>
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">批量设置库存下限</a-button>
      <a-button @click="handleAdd" type="primary" icon="plus">批量设置库存上限</a-button>
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
         <a  @click="handleDetail(record)">出入库明细</a>
        </span>

      </a-table>
    </div>

    <pdProductStockTotal-modal ref="modalForm" @ok="modalFormOk"></pdProductStockTotal-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdProductStockTotalModal from './modules/PdProductStockTotalModal'

  export default {
    name: "PdProductStockTotalList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockTotalModal
    },
    data () {
      return {
        description: '库存管理页面',
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
            dataIndex: 'storeroomId'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productId'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'productId'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'productId'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'productId'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'productId'
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
            dataIndex: 'expire'
          },
          {
            title:'是否永存',
            align:"center",
            dataIndex: 'isLong'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/stock/pdProductStockTotal/list",
          deleteBatch: "/stock/pdProductStockTotal/deleteBatch",
          exportXlsUrl: "/stock/pdProductStockTotal/exportXls",
        },
        dictOptions:{
        },

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      }
       
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:60px;line-height:60px;margin:20px 0;}
  .numberWARAP>div{float:left;width:33%;height:60px;line-height:60px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
  /*.alert_close_btn:hover{color:#fff;border-bottom:1px solid #dedede}*/
  @import '~@assets/less/common.less'
</style>