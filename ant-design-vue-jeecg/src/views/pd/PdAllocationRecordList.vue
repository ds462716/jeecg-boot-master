<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="调拨单号">
              <a-input placeholder="请输入调拨单号" v-model="queryParam.allocationNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="出库科室">
              <a-select
                showSearch
                :outDepartId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @change="departHandleChange"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.outDepartId"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <a-form-item label="调拨日期">
              <a-range-picker @change="rejectedDateChange" v-model="queryParam.queryDate"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="审核状态">
                <a-select v-model="queryParam.auditStatus" placeholder="请选择审核状态">
                  <a-select-option value="1">待审核</a-select-option>
                  <a-select-option value="2">审核通过</a-select-option>
                  <a-select-option value="3">已驳回</a-select-option>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('调拨记录表')">导出</a-button>
     <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
     <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
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
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
         <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" v-bind:disabled="record.submitStatus=='2'">修改</a>
          <a-divider type="vertical" />
          <a href="javascript:;" @click="handleDetail(record)">详情</a>
           <a-divider type="vertical" />
           <a-dropdown>
            <a class="ant-dropdown-link"  v-bind:disabled="record.submitStatus=='2'" >更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.submitStatus=='1'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <pdAllocationRecord-modal ref="modalForm" @ok="modalFormOk"></pdAllocationRecord-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,batchDel } from '@/mixins/JeecgListMixin'
  import { filterObj } from '@/utils/util';
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdAllocationRecordModal from './modules/PdAllocationRecordModal'
  import { deleteAction,getAction } from '@/api/manage'

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
    name: "PdAllocationRecordList",
    mixins:[JeecgListMixin],
    components: {
      PdAllocationRecordModal
    },
    data () {
      return {
        description: '调拨记录表管理页面',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'调拨单编号',
            align:"center",
            dataIndex: 'allocationNo'
          },
          {
            title:'调拨日期',
            align:"center",
            dataIndex: 'allocationDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'realName'
          },
          {
            title:'出库科室',
            align:"center",
            dataIndex: 'outDeptName'
          },
          {
            title:'入库科室',
            align:"center",
            dataIndex: 'inDeptName'
          },
          {
            title:'调拨总数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'实际发货总数量',
            align:"center",
            dataIndex: 'arrivalCount '
          },
          {
            title:'审核状态',
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
            title:'提交状态',
            align:"center",
            dataIndex: 'submitStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['submitStatus'], text+"")
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
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdAllocationRecord/list",
          delete: "/pd/pdAllocationRecord/delete",
          deleteBatch: "/pd/pdAllocationRecord/deleteBatch",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{
          auditStatus:[],
          submitStatus:[],
        },

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


      rejectedDateChange (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
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
      /*batchDel: function () { //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          var allocationNos="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let submitStatus= this.selectionRows[a].submitStatus;
            if(submitStatus=='2'){
              allocationNos+=this.selectionRows[a].allocationNo + ",";
            }else{
              ids += this.selectionRows[a].id + ",";
            }
          }
          if(allocationNos != ""){
            this.$message.warning("调拨编号["+allocationNos.substring(0,allocationNos.length-1)+"]已提交审核，不允许删除！")
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
      },*/
      initDictConfig(){ //静态字典值加载
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        }),
          initDictOptions('submit_status').then((res) => {
            if (res.success) {
              this.$set(this.dictOptions, 'submitStatus', res.result)
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
      }
    }
  }
</script>
<style scoped>
</style>