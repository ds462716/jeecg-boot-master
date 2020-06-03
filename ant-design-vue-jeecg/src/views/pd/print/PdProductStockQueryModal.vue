<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
       <!-- /* 供应商，生产厂家*/-->

        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
               <a-select
                 mode="multiple"
                showSearch
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departIds"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="入库单号">
              <a-input placeholder="请输入入库单号" v-model="queryParam.recordNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="使用状态">
                <j-dict-select-tag-expand v-model="queryParam.nestatStatus" dictCode="nestat_status"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="注册证">
                <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="批号">
                <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="供应商">
                <a-select
                  ref="supplierSelect"
                  showSearch
                  :supplierId="supplierValue"
                  placeholder="请选择供应商"
                  :defaultActiveFirstOption="false"
                  :showArrow="true"
                  :allowClear="true"
                  :filterOption="false"
                  @search="supplierHandleSearch"
                  @change="supplierHandleChange"
                  @focus="supplierHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.supplierId"
                >
                  <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="生产厂家">
                <a-select
                  showSearch
                  :venderId="venderValue"
                  placeholder="请选择生产厂家"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="venderHandleSearch"
                  @change="venderHandleChange"
                  @focus="venderHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.venderId"
                >
                  <a-select-option v-for="d in venderData" :key="d.value">{{d.text}}</a-select-option>
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
      <a-button type="primary" icon="plus" @click="batchPrint()">打印批次码</a-button>
      <a-button type="primary" icon="plus" :loading="confirmLoading" @click="onlyPrint()" style="margin-left: 8px">打印唯一码</a-button>
      <a-button type="primary" icon="delete" @click="handleDelete" style="margin-left: 8px">清除条码</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        @change="handleTableChange"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        >
      </a-table>
    </div>
    <pdProduct-stock-query-print ref="printModalForm" ></pdProduct-stock-query-print>
    <a-modal :visible="orderVisible"  :maskClosable="false"  :confirmLoading="confirmLoading"
             @ok="handleOk" :width="900" @cancel="handleCancel">
      <a-form :form="form">
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="24" >
            <a-form-item label="输入开始序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number autocomplete="off" v-model="startOrder" placeholder="请输入开始序号"  style="width: 100%"/>
            </a-form-item>
            <a-form-item label="输入结束序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number autocomplete="off" v-model="endOrder" placeholder="请输入结束序号" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </a-card>

</template>


<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import { getAction ,deleteAction } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import PdProductStockQueryPrint from './PdProductStockQueryPrint'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdProductStockQueryList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockQueryPrint,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '库存明细查询',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        supplierValue: undefined,
        orderVisible:false,
        supplierData: [],
        previewVisible:false,
        confirmLoading: false,
        venderValue: undefined,
        startOrder: "",
        endOrder: "",
        form: this.$form.createForm(this),
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        venderData: [],
        // 表头
        columns: [
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'huoweiName'
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
            title:'产品类型',
            align:"center",
            dataIndex: 'productFlagName'
          },
          {
            title:'使用状态',
            align:"center",
            dataIndex: 'nestatStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['nestatStatus'], text+"")
              }
            }
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'批号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'生产日期',
            align:"center",
            dataIndex: 'produceDate'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'进价',
            align:"center",
            dataIndex: 'inPurchasePrice'
          },
          {
            title:'出价',
            align:"center",
            dataIndex: 'sellingPrice'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'注册证号',
            align:"center",
            dataIndex: 'registration'
          },
          {
            title:'JDE编号',
            align:"center",
            dataIndex: 'jdeCode'
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
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/queryList",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          deleteCode:"/pd/pdProductStockUniqueCode/deleteCode",
        },
        dictOptions:{
          nestatStatus:[],
        },
        tableScroll:{x :13*157+50},
      }
    },

    methods: {
     //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end

      //供应商查询start
      supplierHandleSearch(value) {
        this.getList(value,this.url.querySupplier,"1");
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        this.getList(value,this.url.querySupplier,"1");
      },
      //供应商查询end
      //生产厂家查询start
      venderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"2");
      },
      venderHandleChange(value) {
        this.venderValue = value;
        this.getList(value,this.url.queryVender,"2");
      },
      //生产厂家查询end
      getList(value,url,flag){
        getAction(url,{name:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          if(flag == "1"){
            this.supplierData = data;
          }else if(flag == "2"){
            this.venderData = data;
          }
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
        param.departIds = this.queryParam.departIds+"";
        return filterObj(param);
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
      batchPrint(){
        if(this.selectionRows.length>0){
          let stockObjs = this.selectionRows;
          let flag = true;
          for(let item of stockObjs){
            if(item.productFlag=="1"){
              //如果是试剂不让批量打印
              flag = false;
              break;
            }
          }
          if(!flag){
            this.$message.error("勾选的内容包含试剂，试剂只能打印唯一码!");
            return ;
          }
          this.$refs.printModalForm.init(this.selectionRows);
        }else{
          this.$message.error("请选择需要打印的内容!")
        }
      },
      onlyPrint(){
        if(this.selectionRows.length>0){
          if(this.selectionRows.length==1){
            let stockObj = this.selectionRows[0];
            if(stockObj.stockNum>200){
              this.orderVisible = true;
            }else{
              this.$refs.printModalForm.onlyInit(stockObj,"","");
            }
          }else{
            this.$message.error("只能勾选一条进行打印!")
          }
        }else{
          this.$message.error("请选择需要打印的内容!")
        }
      },
      handleImgCancel () {
        this.previewVisible = false;
      },
      handleOk(){
        let startOrder = this.startOrder;
        let endOrder = this.endOrder;
        if(this.isRealNum(startOrder) && this.isRealNum(endOrder)){
          let stockObj = this.selectionRows[0];
          let stockNum = parseInt(stockObj.stockNum);
          if(startOrder>0 && endOrder>0){
            if(stockNum>=endOrder){
              if(endOrder-startOrder<200){
                this.orderVisible = false;
                this.$refs.printModalForm.onlyInit(stockObj,startOrder,endOrder);
                this.startOrder = "";
                this.endOrder = "";
              }else{
                this.$message.error("系统只允许每次最多打印两百个条码!")
              }
            }else{
              this.$message.error("结束序号超出库存总数!")
            }
          }else{
            this.$message.error("请输入正确的序号!")
          }
        }else{
          this.$message.error("请输入开始序号和结束序号!")
        }
      },
      handleCancel () {
        this.close()
      },
      close () {
        //解决滚动条缓存bug
        this.$emit('close');
        this.orderVisible = false;
      },
      isRealNum(val){
        // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
        if(val === "" || val ==null){
          return false;
        }
        if(!isNaN(val)){
          return true;
       }else{
        return false;
       }
     },
      handleDelete() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        }else{
          if(this.selectedRowKeys.length!=1){
            this.$message.warning('请勿选择多条记录！');
            return;
          }else{
            let id = this.selectedRowKeys[0];
            let that = this;
            this.$confirm({
              title: "确认删除",
              content: "是否删除选中库存的条码?",
              onOk: function () {
                that.loading = true;
                deleteAction(that.url.deleteCode, {id: id}).then((res) => {
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
        }
      },
      initDictConfig() { //静态字典值加载
        initDictOptions('nestat_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'nestatStatus', res.result)
          }
        })
      },
    },
  }
</script>
<style scoped>
</style>