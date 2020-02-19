<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
              <a-input placeholder="请选择科室" v-model="queryParam.deptName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请选择产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.productNo"></a-input>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('进销存报表')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
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
        @change="handleTableChange">
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { JeecgListMixin} from '@/mixins/JeecgListMixin'

  export default {
    name: "EntersSellsDetailQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '进销存报表',
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
            title:'供应商',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'期初库存',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'本期入库',
            align:"center",
            dataIndex: 'productNo'
          },
          {
            title:'本期出库',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'调出数量',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'调入数量',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'退货入库',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'用量数量',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'用量退回',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'院外退货',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'用量金额',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'期末库存',
            align:"center",
            dataIndex: 'stockNum'
          }
        ],
        url: {
          list: "",
          exportXlsUrl: "",
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {

    }
  }
</script>
<style scoped>
</style>