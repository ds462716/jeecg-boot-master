<template>
  <a-spin :spinning="loading">
      <a-col :md="4" :sm="12">
        <a-card :bordered="false" style="height:1200px;overflow-x: scroll;white-space: nowrap;">
          <div>
            <a-tree
              v-if="treeData.length>0"
              :treeData="treeData"
              :show-line="showLine"
              :defaultExpandAll="defaultExpandAll"
              :selectedKeys="selectedKeys"
              @select="onTreeNodeSelect"
            >
            </a-tree>
          </div>
        </a-card>
      </a-col>
      <a-col :md="20" :sm="12">
        <a-card :bordered="true">
          <div>
            <a-table
              ref="table"
              size="small"
              bordered
              rowKey="id"
              :pagination="ipagination"
              :columns="columns"
              :dataSource="productTable.dataSource"
              :loading="productTable.loading"
              :scroll="productTable.tableScroll"
              :rowSelection="{fixed:true,type:'radio',selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              :rowClassName="setdataCss"
              :customRow="onClickRow"
              @change="handleTableChange">
            </a-table>
          </div>
        </a-card>
      </a-col>
      <a-col :lg="20">
        <a-card :bordered="true">
          <detail-list size="small">
            <detail-list-item term="产品编号">{{productObj.number}}</detail-list-item>
            <detail-list-item term="产品名称">{{productObj.name}}</detail-list-item>
            <detail-list-item term="单位">{{productObj.unitName}}</detail-list-item>
          </detail-list>
          <detail-list size="small">
            <detail-list-item term="规格">{{productObj.spec}}</detail-list-item>
            <detail-list-item term="型号">{{productObj.version}}</detail-list-item>
            <detail-list-item term="产品收费代码">{{productObj.chargeCode}}</detail-list-item>
          </detail-list>
          <detail-list size="small">
            <detail-list-item term="生产厂家">{{productObj.venderName}}</detail-list-item>
            <detail-list-item term="供应商">{{productObj.supplierName}}</detail-list-item>
            <detail-list-item term="注册证">{{productObj.registration}}</detail-list-item>
          </detail-list>
          <detail-list size="small">
            <detail-list-item term="产品组别">{{productObj.groupName}}</detail-list-item>
            <detail-list-item term="一级分类">{{productObj.categoryOneName}}</detail-list-item>
            <detail-list-item term="二级分类">{{productObj.categoryTwoName}}</detail-list-item>
          </detail-list>
          <detail-list size="small">
            <detail-list-item term="进价">{{productObj.purchasePrice}}</detail-list-item>
            <detail-list-item term="出价">{{productObj.sellingPrice}}</detail-list-item>
          </detail-list>

        </a-card>
      </a-col>

  </a-spin>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DetailList from '@/components/tools/DetailList'

  const DetailListItem = DetailList.Item;

  export default {
    name: "PdSupplierRelevanceProduct",
    mixins:[JeecgListMixin],
    components: {
      DetailList,
      DetailListItem
    },
    data () {
      return {
        form: this.$form.createForm(this),
        treeData: [],
        showLine: true,
        defaultExpandAll: true,
        disableMixinCreated:true,
        productObj:"",
        supplierId: "",
        selectedKeys:[],
        /* table加载状态 */
        loading:false,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        url: {
          list: "/pd/pdSupplier/queryTreeList",
          queryProductList: "/pd/pdProduct/list",
        },
        dictOptions:{
          isCharge:[],
          status:[],
        },
        productTable:{
          loading:false,
          dataSource:[],
          tableScroll:{x :1500},
        },
        // 表头
        columns: [
          {
            title:'产品编号',
            align:"center",
            width:120,
            fixed: 'left',
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            width:220,
            fixed: 'left',
            dataIndex: 'name'
          },
          {
            title:'规格',
            align:"center",
            width:170,
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            width:120,
            dataIndex: 'version'
          },
          {
            title:'单位',
            align:"center",
            width:50,
            dataIndex: 'unitName'
          },
          {
            title:'生产厂家',
            align:"center",
            width:220,
            dataIndex: 'venderName'
          },
          {
            title:'是否计费',
            align:"center",
            width:50,
            dataIndex: 'isCharge',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isCharge'], text+"")
              }
            }
          },
          {
            title:'进价',
            align:"center",
            width:90,
            dataIndex: 'purchasePrice'
          },
          {
            title:'出价',
            align:"center",
            width:90,
            dataIndex: 'sellingPrice'
          },
          {
            title:'产品收费代码',
            align:"center",
            width:110,
            dataIndex: 'chargeCode'
          },
          {
            title:'状态',
            align:"center",
            width:50,
            dataIndex: 'status',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['status'], text+"")
              }
            }
          },
          {
            title:'注册证',
            align:"center",
            dataIndex: 'registration'
          },
        ],

      }
    },
    computed: {
    },
    created() {
      this.loadTreeData();
      //初始化字典配置 在自己页面定义
      this.initDictConfig();

    },
    methods: {
      //树节点点击事件
      onTreeNodeSelect(id){
        this.loading = true;
        if(id && id.length>0){
          this.selectedKeys = id;
          this.supplierId = id[0];
          this.loadData(1);
        }
        this.loading = false;
      },
      //字典初始化
      initDictConfig(){
        initDictOptions('is_charge').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isCharge', res.result)
          }
        }),
        initDictOptions('disable_enable_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'status', res.result)
          }
        })
      },
      //加载树
      loadTreeData(){
        this.loading = true;
        getAction(this.url.list).then((res) => {
          this.treeData = res.result;
          let supplierData = res.result[0].children;
          if(supplierData.length>0){
            this.supplierId = supplierData[0].id;
            this.loadData(1);
            //选中树几点
            let array = [];
            array.push(supplierData[0].id);
            this.selectedKeys = array;
          }else{
            this.productTable.dataSource = [];
          }
          this.loading = false;
        })
      },
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        //查询产品
        params.supplierId = this.supplierId;
        this.productTable.loading = true;
        getAction(this.url.queryProductList, params).then((res) => {
          if (res.success) {
            this.productTable.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
            if(this.productTable.dataSource.length>0){
              this.productObj = this.productTable.dataSource[0];
            }
          }
          this.productTable.loading = false;
        })
      },
      setdataCss(record,index) {
        let validityFlag = record.validityFlag;
        return "validityFlag"+validityFlag;
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              console.log(record)
              this.productObj = record;
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
                if(parseInt(lie)-parseInt(cellIndex) > 0){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  this.selectedRowKeys = [];
                  this.selectionRows = [];
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                    this.selectionRows.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                    this.selectionRows.push(record);
                  }
                }
              }
            }
          }
        }
      },
      //选择radio事件
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.productObj = selectionRows[0];
      },
    }
  }
</script>
<style scoped>
  .validityFlag0{
  }
  .validityFlag1{
    background-color:#FF3333;
  }
  .validityFlag2{
    background-color:#FFFFCC;
  }
</style>