<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
               <a-select
                showSearch
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @change="departHandleChange"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departId"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>
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
      <a-button type="primary" icon="download" @click="handleExportXls('库存明细')">导出</a-button>
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
        :scroll="tableScroll"
        @change="handleTableChange"
        >
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{departName:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.departName,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0);
  }
  export default {
    name: "PdProductStockQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '库存明细查询',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          /*{
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'huoweiCode'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
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
            title:'数量',
            align:"center",
            dataIndex: 'stockNum'
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
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/queryList",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{
        },
        tableScroll:{x :13*147+50},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
     //科室查询start
      departHandleSearch(value) {
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      departHandleChange(value) {
        this.departValue = value;
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      //科室查询end
    }
  }
</script>
<style scoped>
</style>