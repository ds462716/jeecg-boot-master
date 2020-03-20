<template>
  <a-modal
    :title="title"
    :width="1500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    cancelText="关闭">
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">返回</a-button>
    </template>

    <a-spin :spinning="confirmLoading">
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
            <a-form-item label="申购科室">
              <a-input placeholder="请输入申购科室名称" v-model="queryParam.deptName"></a-input>
            </a-form-item>
          </a-col>
          <template :md="6" v-if="toggleSearchStatus">
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
        :scroll="tableScroll"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </div>
     </a-spin>
  </a-modal>
</template>
<script>

  import {getAction } from '@/api/manage'
  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdPurchaseOrderMergeModal",
    mixins:[JeecgListMixin],
    components: {},
    data () {
      return {
        form: this.$form.createForm(this),
        visible: false,
        model:{},
        confirmLoading: false,
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
            dataIndex: 'orderNo',
            width:"80px"
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
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'订单数量',
            align:"center",
            dataIndex: 'orderNum'
          },
          {
            title:'到货数量',
            align:"center",
            dataIndex: 'arrivalNum'
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
          },
        ],
        url: {
          list: "/pd/pdPurchaseOrderMerge/queryPdPurchaseMergeDetail",
        },
         tableScroll:{x :13*147+50},
        dictOptions:{
          auditStatus:[],
        },

      }
    },
    computed: {
      /*importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }*/
    },
    methods: {
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.loadData(1);
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close();
      },
      loadData(arg){
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.mergeOrderNo=this.model.mergeOrderNo;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
          }
          this.loading = false;
        })
      },

      initDictConfig(){ //静态字典值加载

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