<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申购编号">
              <a-input placeholder="请输入申购编号" v-model="queryParam.orderNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="审核状态">
              <a-select v-model="queryParam.auditStatus" :allowClear="true" placeholder="请选择审核状态">
                <a-select-option value="1">待审核</a-select-option>
                <a-select-option value="2">审核通过</a-select-option>
                <a-select-option value="3">已驳回</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <a-form-item label="申购日期">
              <a-range-picker @change="rejectedDateChange" v-model="queryParam.queryDate"/>
            </a-form-item>
          </a-col>
          <template :md="6" v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="申购类型">
                <j-dict-select-tag-expand v-model="queryParam.purchaseType" dictCode="purchase_type"/>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-divider type="vertical" />
      <a-button @click="automaticRep()" type="primary" v-show="isDisabledAuth('purchase:form:autoAdd')" icon="plus">自动生成采购计划</a-button>
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">
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
          <a  @click="handleEdit(record)"  v-bind:disabled="record.submitStatus=='2'">修改</a>
          <a-divider type="vertical" />
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link"  v-bind:disabled="record.submitStatus=='2'" >更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.submitStatus=='1'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pdPurchaseOrder-modal ref="modalForm" @ok="modalFormOk"></pdPurchaseOrder-modal>
    <pd-purchase-rep-modal ref="modalForm1" @ok="modalFormOk"></pd-purchase-rep-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,batchDel} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import PdPurchaseOrderModal from './modules/PdPurchaseOrderModal'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import PdPurchaseRepModal from './modules/PdPurchaseRepModal'
  import { disabledAuthFilter } from "@/utils/authFilter"
  export default {
    name: "PdPurchaseOrderList",
    mixins:[JeecgListMixin],
    components: {
      PdPurchaseOrderModal,
      JDictSelectTagExpand,
      PdPurchaseRepModal
    },
    data () {
      return {
        description: '申购订单主表管理页面',
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
            title:'申购编号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'申购人',
            align:"center",
            dataIndex: 'purchaseName'
          },
          {
            title:'申购类型',
            align:"center",
            dataIndex: 'purchaseType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['purchaseType'], text+"")
              }
            }
          },
          {
            title:'申购日期',
            align:"center",
            dataIndex: 'orderDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申购科室',
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
            title:'申购总数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'申购总金额',
            align:"center",
            dataIndex: 'totalPrice'
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdPurchaseOrder/list",
          delete: "/pd/pdPurchaseOrder/delete",
          deleteBatch: "/pd/pdPurchaseOrder/deleteBatch"
        },
        dictOptions:{
          purchaseType:[],
          auditStatus:[],
          submitStatus:[],
        },

      }
    },
    computed: {

    },
    methods: {
      /*batchDel: function () { //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          var orderNos="";
          for (let a = 0; a < this.selectionRows.length; a++) {
                let submitStatus= this.selectionRows[a].submitStatus;
            if(submitStatus=='2'){
              orderNos+=this.selectionRows[a].orderNo + ",";
            }else{
              ids += this.selectionRows[a].id + ",";
            }
          }
          if(orderNos != ""){
            this.$message.warning("采购编号["+orderNos.substring(0,orderNos.length-1)+"]已提交审核，不允许删除！")
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


      automaticRep() { //自动补货
        this.$refs.modalForm1.add();
        this.$refs.modalForm1.title = "自动生成采购计划";
        this.$refs.modalForm1.disableSubmit = false;
      },

      handleEdit: function (record) { //编辑
        if(record.repType){
          this.$refs.modalForm1.edit(record);
          this.$refs.modalForm1.title = "编辑";
          this.$refs.modalForm1.disableSubmit = false;
        }else{
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title = "编辑";
          this.$refs.modalForm.disableSubmit = false;
        }
      },

      handleDetail:function(record){  //详情
        if(record.repType){
          this.$refs.modalForm1.edit(record);
          this.$refs.modalForm1.title="详情";
          this.$refs.modalForm1.disableSubmit = true;
        }else{
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title="详情";
          this.$refs.modalForm.disableSubmit = true;
        }
      },


      initDictConfig(){ //静态字典值加载
        initDictOptions('purchase_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'purchaseType', res.result)
          }
        })
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
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
      },
      /**
       * 校验权限
       * @param code
       * @returns {boolean|*}
       */
      isDisabledAuth(code){
        return !disabledAuthFilter(code);
      },
       
    }
  }
</script>
<style scoped>
</style>