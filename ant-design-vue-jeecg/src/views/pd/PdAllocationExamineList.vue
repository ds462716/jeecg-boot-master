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
            <a-form-item label="入库科室名称">
              <a-select
                showSearch
                :inDeptId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @change="departHandleChange"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.inDeptId"
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
          <a-col :md="6" :sm="8">
            <a-form-item label="审核状态">
              <j-dict-select-tag-expand v-model="queryParam.auditStatus" dictCode="audit_status"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <!--<a-col :md="6" :sm="8">
              <a-form-item label="提交状态">
                <j-dict-select-tag-expand v-model="queryParam.submitStatus" dictCode="submit_status"/>
              </a-form-item>
            </a-col>-->
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
          <a v-if="record.auditStatus=='1'" @click="handleEdit(record)">审核</a>&nbsp;&nbsp;&nbsp;
          <a href="javascript:;" @click="handleDetail(record)">详情</a>
        </span>
      </a-table>
    </div>
    <pd-allocation-examine-modal ref="modalForm" @ok="modalFormOk"></pd-allocation-examine-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,handleEdit } from '@/mixins/JeecgListMixin'
  import { filterObj } from '@/utils/util';
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdAllocationExamineModal from './modules/NewPdAllocationExamineModal'
  import {getAction } from '@/api/manage'
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
    name: "PdAllocationExamineList",
    mixins:[JeecgListMixin],
    components: {
      PdAllocationExamineModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '调拨记录审核页面',
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
          list: "/pd/pdAllocationRecord/auditList",
          exportXlsUrl: "/pd/pdAllocationRecord/exportXls",
          queryDepart: "/pd/pdDepart/getSysTwoDepartList",
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
      initDictConfig(){ //静态字典值加载
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
      }
    }
  }
</script>
<style scoped>
</style>