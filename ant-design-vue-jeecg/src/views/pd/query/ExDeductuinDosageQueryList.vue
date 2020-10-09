<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
             <!-- <a-col :md="6" :sm="8">
                <a-form-item label="科室">
                  <a-select
                    mode="multiple"
                    showSearch
                    :departId="departValue"
                    :defaultActiveFirstOption="false"
                    :allowClear="true"
                    :showArrow="true"
                    :filterOption="false"
                    @search="departHandleSearch"
                    @focus="departHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-model="queryParam.departIds"
                    placeholder="请选择科室"
                  >
                    <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
                  </a-select>

                </a-form-item>
              </a-col>-->
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
            <a-form-item label="扣减类型">
              <j-dict-select-tag-expand v-model="queryParam.deductuinType" dictCode="deductuin_type"/>
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
       <a-button type="primary" icon="download" @click="handleExportXls('试剂用量扣减记录表')">导出</a-button>
    </div>

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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

      </a-table>
    </div>
   </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "ExDeductuinDosageQueryList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTagExpand
     },
    data () {
      return {
        description: '试剂用量查询页面',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品有效期',
            align:"center",
            dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'产品批次号',
            align:"center",
            dataIndex: 'batchNo'
          },
        /*  {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },*/
          {
            title:'扣减类型',
            align:"center",
            dataIndex: 'deductuinType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['deductuinType'], text+"")
              }
            }

          },
          {
            title:'规格单位',
            align:"center",
            dataIndex: 'specUnitName'
          },
          {
            title:'规格数量',
            align:"center",
            dataIndex: 'specQuantity'
          },
          {
            title:'扣减规格数量',
            align:"center",
            dataIndex: 'specNum',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'扣减日期',
            align:"center",
            dataIndex: 'specDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'对应库存明细ID',
            align:"center",
            dataIndex: 'stockId'
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'personName'
          },
        ],
        url: {
          list: "/pd/exDeductuinDosage/list",
          delete: "/pd/exDeductuinDosage/delete",
          deleteBatch: "/pd/exDeductuinDosage/deleteBatch",
          exportXlsUrl: "/pd/exDeductuinDosage/exportXls",
          importExcelUrl: "pd/exDeductuinDosage/importExcel",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{
          deductuinType:[],
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
      //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;

        })
      },
      //科室查询end




      initDictConfig(){
        initDictOptions('deductuin_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'deductuinType', res.result)
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>