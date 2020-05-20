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
                <a-select v-model="queryParam.acceptStatus" placeholder="请选择扣减状态">
                  <a-select-option value="0">已扣减</a-select-option>
                  <a-select-option value="1">未配置检验用量</a-select-option>
                  <a-select-option value="2">未扣减</a-select-option>
                </a-select>
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
    <!--<div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>-->

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

        <span slot="action" slot-scope="text, record">
          <a @click="editUsePackageDetail(record)" v-bind:disabled="record.acceptStatus=='0'">重新扣减</a>&nbsp;&nbsp;&nbsp;
         <a  @click="queryUsePackageDetail(record)">查看检验包详情</a>
        </span>

      </a-table>
    </div>

    <exInspectionItems-modal ref="modalForm" @ok="modalFormOk"></exInspectionItems-modal>
    <pdUsePackage-modal ref="pdUsePackageModal" @ok="modalFormOk"></pdUsePackage-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ExInspectionItemsModal from './modules/ExInspectionItemsModal'
  import PdUsePackageModal from '../pd/modules/PdUsePackageModal'
  import {httpAction,getAction} from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'


  export default {
    name: "ExInspectionItemsList",
    mixins:[JeecgListMixin],
    components: {
      ExInspectionItemsModal,
      PdUsePackageModal
    },
    data () {
      return {
        description: '检查项目表管理页面',
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
            dataIndex: 'patientSex'
          },
          {
            title:'患者年龄',
            align:"center",
            dataIndex: 'patientAge'
          },
          {
            title:'就诊卡号',
            align:"center",
            dataIndex: 'cardId'
          },
         /* {
            title:'条形码',
            align:"center",
            dataIndex: 'barCode'
          },
          {
            title:'申请医生',
            align:"center",
            dataIndex: 'applyDoctorName'
          },*/
          {
            title:'申请科室',
            align:"center",
            dataIndex: 'applyDepartmentName'
          },
          {
            title:'检验科室',
            align:"center",
            dataIndex: 'testDepartment'
          },
          {
            title:'患者类型',
            align:"center",
            dataIndex: 'patientType'
          },
          /*{
            title:'接收日期',
            align:"center",
            dataIndex: 'receiveDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },*/
          {
            title:'检验日期',
            align:"center",
            dataIndex: 'testDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'项目组合名称',
            align:"center",
            dataIndex: 'combinationName'
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
           dataIndex: 'remarks'
         },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:140,
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

      initDictConfig(){ //静态字典值加载
        initDictOptions('accept_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'acceptStatus', res.result)
          }
        })
      },



      //重新扣减
      editUsePackageDetail: function (record) {
        httpAction(this.url.editUsePackage,record,"post").then((res)=>{
          if(res.success){
            this.$message.success(res.message)
             this.loadData();
          }else{
            this.$message.warning(res.message)
          }
        })
      },
      //查询检验包详情
      queryUsePackageDetail: function (record) {
        getAction(this.url.queryUsePackageDetail, {testItemName:record.testItemName,testItemCode:record.testItemCode}).then((res) => {
          if (res.success) {
            this.$refs.pdUsePackageModal.edit(res.result);
            this.$refs.pdUsePackageModal.title = "详情";
            this.$refs.pdUsePackageModal.disableSubmit = true;
          }else{
            this.$message.warning(res.message);
          }
        })

      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>