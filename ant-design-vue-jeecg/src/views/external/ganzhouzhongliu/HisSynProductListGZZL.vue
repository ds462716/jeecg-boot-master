<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="对照状态">
              <a-select placeholder="状态" :allowClear="true" v-model="queryParam.hisChargeCodeSynFlag" >
                <a-select-option value="0">全部</a-select-option>
                <a-select-option value="1">已对照</a-select-option>
                <a-select-option value="2">未对照</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <!--<a-col :md="6" :sm="8">-->
              <!--<a-form-item label="注册证">-->
                <!--<a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>-->
              <!--</a-form-item>-->
            <!--</a-col>-->
            <a-col :md="6" :sm="8">
              <a-form-item label="收费代码">
                <a-input placeholder="请输入收费代码" v-model="queryParam.chargeCode"></a-input>
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
            <a-col :md="6" :sm="8">
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="一级分类">
                <a-select
                  showSearch
                  :categoryOne="categoryOneValue"
                  placeholder="请选择一级分类"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="categoryOneHandleSearch"
                  @change="categoryOneHandleChange"
                  @focus="categoryOneHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.categoryOne"
                >
                  <a-select-option v-for="d in categoryOneData" :key="d.value">{{d.text}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="二级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select
                  showSearch
                  :categoryTwo="categoryTwoValue"
                  placeholder="请选择二级分类"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="categoryTwoHandleSearch"
                  @change="categoryTwoHandleChange"
                  @focus="categoryTwoHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.categoryTwo"
                >
                  <a-select-option v-for="d in categoryTwoData" :key="d.value">{{d.text}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8" >
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
      <!--<a-button @click="handleExportXls('产品价格对照表')" type="primary" icon="download" >导出</a-button>-->
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
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEditChargeCode(record)">对照</a>
        </span>

      </a-table>
    </div>

    <his-syn-charge-code-model-gZZL ref="modalForm" @ok="modalFormOk"></his-syn-charge-code-model-gZZL>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from  '@/api/manage'
  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import pick from 'lodash.pick'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import HisSynChargeCodeModelGZZL from "./modules/HisSynChargeCodeModelGZZL";


  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{name:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0);
  }


  export default {
    name: "HisSynChargeCodeFC",
    mixins:[JeecgListMixin],
    components: {
      HisSynChargeCodeModelGZZL,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '产品管理页面',
        notFoundContent:"未找到内容",
        venderData: [],
        venderValue: undefined,
        supplierData: [],
        supplierValue: undefined,
        categoryOneData: [],
        categoryOneValue: undefined,
        categoryTwoData: [],
        categoryTwoValue: undefined,
        chargeCodeVisible:false,
        confirmLoading: false,
        copyRecord:"",
        form: this.$form.createForm(this),
        model: {},
        productInfo:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        validatorRules: {
          chargeCodeT: {rules: [
              {required: true, message: '请输入产品收费代码!'},
            ]},
        },
        // 表头
        columns: [
          {
            title:'产品编号',
            align:"center",
            width:120,
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title:'是否计费',
            align:"center",
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
            dataIndex: 'purchasePrice'
          },
          {
            title:'出价',
            align:"center",
            dataIndex: 'sellingPrice'
          },
          {
            title:'HIS收费代码',
            align:"center",
            dataIndex: 'chargeCode'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width:120,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/external/exPdDosageGzzl/listForHisCharge",
          exportXlsUrl: "/pd/hisChargeFCRMYY/exportXls",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryCategoryOne:"/pd/pdCategory/getCategoryOneList?type=0",
          queryCategoryTwo:"/pd/pdCategory/getCategoryOneList?type=1",
        },
        dictOptions:{
          isCharge:[],
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
        //查询产品
        params.productFlag = 0;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },
      initDictConfig(){
        initDictOptions('is_charge').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isCharge', res.result)
          }
        }),
        initDictOptions('device_classification').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'deviceClassification', res.result)
          }
        })
      },
      //生产厂家查询start
      venderHandleSearch(value) {
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      venderHandleChange(value) {
        this.venderValue = value;
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      //生产厂家查询end
      //供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //供应商查询end
      //一级分类查询start
      categoryOneHandleSearch(value) {
        fetch(value, data => (this.categoryOneData = data),this.url.queryCategoryOne);
      },
      categoryOneHandleChange(value) {
        this.categoryOneValue = value;
        fetch(value, data => (this.categoryOneData = data),this.url.queryCategoryOne);
        this.queryParam.categoryTwo="";
      },
      //一级分类查询end
      //二级分类查询start
      categoryTwoHandleSearch(value) {
        let categoryOne = this.categoryOneValue;
        if(!categoryOne){
          categoryOne = "";
        }
        fetch(value, data => (this.categoryTwoData = data),this.url.queryCategoryTwo+"&parentId="+categoryOne);
      },
      categoryTwoHandleChange(value) {
        let categoryOne = this.categoryOneValue;
        if(!categoryOne){
          categoryOne = "";
        }
        this.categoryTwoValue = value;
        fetch(value, data => (this.categoryTwoData = data),this.url.queryCategoryTwo+"&parentId="+categoryOne);
      },
      //二级分类查询end
      //批量修改收费代码点击事件
      handleChargeCode(){
        this.form.resetFields();
        this.model = Object.assign({}, "");
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'chargeCode'))
        });
        this.chargeCodeVisible = true;//当前窗口打开
      },
      //产品收费代码窗口关闭事件
      handleCancel(){
        this.close()
      },
      close () {
        this.$emit('close');
        this.chargeCodeVisible = false;
      },
      handleEditChargeCode(record){
        this.$refs.modalForm.edit(record);
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
<style>
  .validityFlag0{
  }
  .validityFlag1{
    background-color:#FF3333;
  }
  .validityFlag2{
    background-color:#FFFFCC;
  }
</style>