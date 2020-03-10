<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="调拨单号">
                <a-input placeholder="请输入调拨单号" v-model="queryParam.allocationNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="调拨日期">
                <a-range-picker @change="orderDateChange" v-model="queryParam.queryDate"/>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="产品编号">
                  <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="产品名称">
                  <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="6" :sm="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
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
      <a-table
        ref="table"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys= "expandedRowKeys"
        :rowSelection="{type:'radio',selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @expand="handleExpand"
        @change="handleTableChange">

        <a-table
          slot="expandedRowRender"
          slot-scope="text"
          :columns="innerColumns"
          :dataSource="innerData"
          size="middle"
          bordered
          rowKey="purchaseDetailId"
          :pagination="false"
          :loading="subloading"
        >
        </a-table>
      </a-table>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';

  export default {
    name: "PdChooseAllocationListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择订单",
        width:1200,
        visible: false,
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        queryDate:[],
        // model: {},
        confirmLoading: false,
        inDepartId:"",
        outDepartId:"",
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'调拨单号',
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
            title:'入库科室',
            align:"center",
            dataIndex: 'inDeptName'
          },
          {
            title:'出库科室',
            align:"center",
            dataIndex: 'outDeptName'
          },
          {
            title:'调拨数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'实际发货数量',
            align:"center",
            dataIndex: 'arrivalCount'
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
            title:'审核人',
            align:"center",
            dataIndex: 'auditByName'
          }
        ],
        innerColumns:[
          {title:'定数包编号', align:"center", width: 100, dataIndex: 'packageCode'},
          {title:'定数包名称', align:"center", width: 100, dataIndex: 'packageName'},
          {title:'定数包产品数量', align:"center", width: 100, dataIndex: 'packageNum'},
          {title:'产品编号', align:"center", width: 100, dataIndex: 'number'},
          {title:'产品名称', align:"center", dataIndex: 'productName'},
          {title:'规格', align:"center", dataIndex: 'spec'},
          {title:'型号', align:"center", dataIndex: 'version'},
          {title:'单位', align:"center", dataIndex: 'unitName'},
          {title:'调拨数量', align:"center", dataIndex: 'allocationNum'},
          {title:'实际发货数量', align:"center", dataIndex: 'arrivalNum' },
          {title:'库存数量', align:"center", dataIndex: 'stockNum'},
        ],
        url: {
          list: "/pd/pdAllocationRecord/chooseAllocationList",
          chooseDetailList:"/pd/pdAllocationRecord/queryPdAllocationDetailList",
        },
        dictOptions:{
          auditStatus:[],
        },
      }
    },
    methods: {
      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.subloading = true;
          this.expandedRowKeys.push(record.id);
          getAction(this.url.chooseDetailList, {allocationNo: record.allocationNo}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.visible = false;
        this.departId = "";
        this.$emit('close');
      },
      show(param) {
        this.visible = true;
        this.inDepartId = param.inDepartId;
        this.outDepartId = param.outDepartId;
        this.loadData();
      },
      handleOk () {
        if(this.selectionRows.length > 0){
          let params = { allocationNo: this.selectionRows[0].allocationNo }
          getAction(this.url.chooseDetailList, params).then((res) => {
            if (res.success) {
              let data = res.result;
              this.$emit('ok', data);
              this.close();
            }
          });
        }else{
          this.$message.error("请选择一行数据!")
        }
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      initDictConfig(){ //静态字典值加载
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
      },

      orderDateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
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
        param.inDeptId = this.inDepartId;
        param.outDeptId = this.outDepartId;
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
    }
  }
</script>