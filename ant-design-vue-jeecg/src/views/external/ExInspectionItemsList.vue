<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="病人姓名">
              <a-input placeholder="请输入病人姓名" v-model="queryParam.patientName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="检验项目代码">
              <a-input placeholder="请输入检验项目代码" v-model="queryParam.testItemCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="扣减状态">
                <a-select v-model="queryParam.acceptStatus" :allowClear="true" placeholder="请选择扣减状态">
                  <a-select-option value="0">已扣减</a-select-option>
                  <a-select-option value="1">未配置检验項目</a-select-option>
                  <a-select-option value="2">未扣减</a-select-option>
                  <a-select-option value="3">未配置试剂用量</a-select-option>
                  <a-select-option value="4">部分扣减</a-select-option>
                </a-select>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="项目名称">
                <a-input placeholder="请输入项目名称" v-model="queryParam.testItemName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="检验日期">
                <a-range-picker @change="dateChange" v-model="queryParam.queryDate"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="条形码">
                <a-input placeholder="请输入条形码" v-model="queryParam.barCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="所属仪器">
                <a-select
                  showSearch
                  :departId="instrValue"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="instrHandleSearch"
                  @focus="instrHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.instrCode"
                  placeholder="请选择仪器"
                >
                  <a-select-option v-for="d in instrData" :key="d.instrCode">{{d.instrName}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>


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
       <a-button @click="batchUsePackageDetail" :loading="loading" type="primary" icon="edit">批量扣减</a-button>
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>-->
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
        :scroll="tableScroll"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="ellipsisText" slot-scope="text">
          <j-ellipsis :value="text" :length="textMaxLength"></j-ellipsis>
        </template>


        <span slot="action" slot-scope="text, record">
          <a @click="editUsePackageDetail(record)" :loading="loading" v-bind:disabled="record.acceptStatus=='0'">重新扣减</a>&nbsp;&nbsp;&nbsp;
         <a  @click="queryUsePackageDetail(record)">查看配置用量</a>&nbsp;&nbsp;&nbsp;
          <a  @click="queryExInspection(record)">查看扣减明细</a>
        </span>
      </a-table>
    </div>

    <exInspectionItems-modal ref="modalForm" @ok="modalFormOk"></exInspectionItems-modal>
    <pdUsePackage-modal ref="pdUsePackageModal" @ok="modalFormOk"></pdUsePackage-modal>
    <ex-inspection-inf-modal ref="exInspectionInfModal" @ok="modalFormOk" ></ex-inspection-inf-modal>
  </a-card>
</template>
<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ExInspectionItemsModal from './modules/ExInspectionItemsModal'
  import PdUsePackageModal from '../pd/modules/PdUsePackageModal'
  import ExInspectionInfModal from './modules/ExInspectionInfModal'
  import {httpAction,getAction} from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: "ExInspectionItemsList",
    mixins:[JeecgListMixin],
    components: {
      ExInspectionItemsModal,
      PdUsePackageModal,
      ExInspectionInfModal,
      JEllipsis
    },
    data () {
      return {
        description: '检查项目表管理页面',
        instrData: [],
        instrValue: undefined,
        notFoundContent:"未找到内容",
        textMaxLength:20,
        // 表头
        columns: [
          {
            title:'患者姓名',
            align:"center",
            dataIndex: 'patientName'
          },
          {
            title:'患者性别',
            align:"center",
            width:60,
            dataIndex: 'patientSex'
          },
          {
            title:'患者年龄',
            align:"center",
            width:60,
            dataIndex: 'patientAge'
          },
          {
            title:'就诊卡号',
            align:"center",
            dataIndex: 'cardId'
          },
         {
            title:'条形码',
            align:"center",
            dataIndex: 'barCode'
          },

          {
            title:'申请科室',
            align:"center",
            dataIndex: 'applyDepartment'
          },

          {
            title:'检验科室',
            align:"center",
            dataIndex: 'testDepartment'
          },
          {
            title:'患者类型',
            align:"center",
            width:60,
            dataIndex: 'patientType'
          },

          {
            title:'检验日期',
            align:"center",
            width:110,
            dataIndex: 'testDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'检验仪器',
            align:"center",
            dataIndex: 'instrName'
          },
          {
            title:'检查项目名称',
            align:"center",
            dataIndex: 'testItemName'
          },
          {
            title:'检查项目代码',
            align:"center",
            dataIndex: 'testItemCode'
          },
          {
            title:'检查费用',
            align:"center",
            dataIndex: 'testItemCost'
          },
          {
            title:'扣减状态',
            align:"center",
            dataIndex: 'acceptStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['acceptStatus'], text+"")
              }
            }
          },
            {
           title:'备注',
           align:"center",
           scopedSlots: {customRender: "ellipsisText"},
           dataIndex: 'remarks'
         },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:200,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/external/exInspectionItems/list",
          queryUsePackageDetail: "/pd/pdUsePackage/queryUsePackageDetail",
          delete: "/external/exInspectionItems/delete",
          deleteBatch: "/external/exInspectionItems/deleteBatch",
          exportXlsUrl: "/external/exInspectionItems/exportXls",
          importExcelUrl: "external/exInspectionItems/importExcel",
          editUsePackage:"/external/exInspectionItems/editUsePackage",
          batchUsePackageDetail:"/external/exInspectionItems/batchUsePackageDetail",
          queryExLabInstrInf:"/ex/exLabInstrInf/getExLabInstrInf",
        },
        dictOptions:{
          acceptStatus:[],
        },
        tableScroll:{x :10*200+30},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData(arg) {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.loading = true;
        let params = this.getQueryParams();//查询条件
        //查询
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },

      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('accept_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'acceptStatus', res.result)
          }
        })
      },

      //仪器查询start
      instrHandleSearch(value) {
        getAction(this.url.queryExLabInstrInf,{queryType:"0",instrName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.instrData = res.result;
        })
      },
      //仪器查询end
      //批量扣减
      batchUsePackageDetail:function() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请勾选需要扣减的记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
        }
        this.loading = true;
        let formDataAll = new FormData();
        formDataAll.append("ids",ids);
        httpAction(this.url.batchUsePackageDetail,formDataAll,"post").then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.loadData();
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false;
        })

      },


      //重新扣减
      editUsePackageDetail: function (record) {
        this.loading = true;
        httpAction(this.url.editUsePackage,record,"post").then((res)=>{
          if(res.success){
            this.$message.success(res.message)
             this.loadData();
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      //查询检验包详情
      queryUsePackageDetail: function (record) {
        var testItemCode=record.testItemCode;
        if(testItemCode=="" || testItemCode==null){
          this.$message.warning('检验项目代号为空，无法查看配置信息！');
          return;
        }
        getAction(this.url.queryUsePackageDetail, {testItemCode:testItemCode}).then((res) => {
          if (res.success) {
            this.$refs.pdUsePackageModal.edit(res.result);
            this.$refs.pdUsePackageModal.title = "详情";
            this.$refs.pdUsePackageModal.disableSubmit = true;
          }else{
            this.$message.warning(res.message);
          }
        })

      },

      //查询扣减详情
      queryExInspection: function (record) {
            this.$refs.exInspectionInfModal.edit(record);
            this.$refs.exInspectionInfModal.title = "扣减详情";
            this.$refs.exInspectionInfModal.disableSubmit = true;
      },
      dateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>