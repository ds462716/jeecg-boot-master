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
            <a-form-item label="病人姓名">
              <a-input placeholder="请输入病人姓名" v-model="queryParam.productNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="住院号">
              <a-input placeholder="请输入住院号" v-model="queryParam.productNo"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('用量明细')">导出</a-button>
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
    name: "PdDosageQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '用量明细查询',
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
            title:'用量单号',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'用量库房',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'用量日期',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品权限',
            align:"center",
            dataIndex: 'productNo'
          },
          {
            title:'产品条码',
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
            title:'批号',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'金额',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'执行科室',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'手术科室',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'住院号',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'病人姓名',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'手术医生',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'HIS收费单价',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'HIS收费数量',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'HIS收费金额',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'收费标志',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'limitDown'
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