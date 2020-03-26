<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申领编号">
              <a-input placeholder="请输入申领编号" v-model="queryParam.applyNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="申领科室">
              <a-input placeholder="请输入申领科室" v-model="queryParam.deptName"></a-input>
            </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <a-form-item label="申领日期">
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
    <!-- 查询区域-END -->
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
    <pd-apply-examine-modal ref="modalForm" @ok="modalFormOk"></pd-apply-examine-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,handleEdit} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import PdApplyExamineModal from './modules/PdApplyExamineModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdApplyExamineList",
    mixins:[JeecgListMixin],
    components: {JDictSelectTag, PdApplyExamineModal},
    data () {
      return {
        description: '科室领用管理页面',
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
            title:'申领编号',
            align:"center",
            dataIndex: 'applyNo'
          },
          {
            title:'申领人',
            align:"center",
            dataIndex: 'realName'
          },
          {
            title:'申领日期',
            align:"center",
            dataIndex: 'applyDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申领科室',
            align:"center",
            dataIndex: 'deptName'

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
            title:'申领数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'实际领用数量',
            align:"center",
            dataIndex: 'arrivalCount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdApplyOrder/auditList",
          delete: "/pd/pdApplyOrder/delete",
          deleteBatch: "/pd/pdApplyOrder/deleteBatch"
        },
        dictOptions:{
          auditStatus:[],
        },

      }
    },
    computed: {

    },
    methods: {

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