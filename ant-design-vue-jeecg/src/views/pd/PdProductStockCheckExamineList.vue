<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="盘点科室">
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
            <a-form-item label="审核状态">
              <j-dict-select-tag-expand v-model="queryParam.auditStatus" dictCode="audit_status"/>
            </a-form-item>
          </a-col>
         <!-- <template :md="6" v-if="toggleSearchStatus">
              &lt;!&ndash;待扩展查询条件&ndash;&gt;
          </template>-->
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
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a v-if="record.auditStatus=='1'" @click="handleExamine(record)">审核</a> &nbsp;&nbsp;&nbsp;
          <a @click="handleDetail(record)">详情</a>
        </span>
      </a-table>
    </div>

    <pdProductStockCheck-Examine-modal ref="modalForm" @ok="modalFormOk"></pdProductStockCheck-Examine-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin ,handleEdit,batchDel} from '@/mixins/JeecgListMixin'
  import { deleteAction,getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdProductStockCheckExamineModal from './modules/PdProductStockCheckExamineModal'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

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
    name: "PdProductStockCheckList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockCheckExamineModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '盘点记录表管理页面',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
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
            title:'盘点编号',
            align:"center",
            dataIndex: 'checkNo'
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'盘点日期',
            align:"center",
            dataIndex: 'checkDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'盘点人',
            align:"center",
            dataIndex: 'checkName'
          },
          {
            title:'盘点总数量',
            align:"center",
            dataIndex: 'shouldCount'
          },
          {
            title:'已盘总数量',
            align:"center",
            dataIndex: 'checkCount'
          },
          {
            title:'盘盈盘亏数量',
            align:"center",
            dataIndex: 'profitLossCount'
          },
          {
            title:'提交状态',
            align:"center",
            dataIndex: 'checkStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['checkStatus'], text+"")
              }
            }
          },
          {
            title:'审批状态',
            align:"center",
            dataIndex: 'auditStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['auditStatus'], text+"")
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
          list: "/pd/pdProductStockCheck/examineList",
          delete: "/pd/pdProductStockCheck/delete",
          deleteBatch: "/pd/pdProductStockCheck/deleteBatch",
          exportXlsUrl: "/pd/pdProductStockCheck/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{
          auditStatus:[],

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
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      departHandleChange(value) {
        this.departValue = value;
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      //科室查询end
      batchDel: function () { //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          var checkNos="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let checkStatus= this.selectionRows[a].checkStatus;
            if(checkStatus=='1'){
              checkNos+=this.selectionRows[a].checkNo + ",";
            }else{
              ids += this.selectionRows[a].id + ",";
            }
          }
          if(checkNos != ""){
            this.$message.warning("盘点编号["+checkNos.substring(0,checkNos.length-1)+"]已盘点完成，不允许删除！")
            return
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      initDictConfig(){
        initDictOptions('submit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'checkStatus', res.result)
          }
        }),
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })

      },
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
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                  }
                }
              }
            }
          }
        }
      },
      /**
       * 审核
       * @param record
       */
      handleExamine: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "审核";
        this.$refs.modalForm.disableSubmit = false;
      },
       
    }
  }
</script>
<style scoped>

</style>