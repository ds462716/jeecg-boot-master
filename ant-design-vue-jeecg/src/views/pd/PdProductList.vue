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
            <a-form-item label="注册证">
              <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
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
              <a-form-item label="供应商">
                <a-select
                  showSearch
                  :supplierId="supplierValue"
                  placeholder="请选择供应商"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
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
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="型号">
                <a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="状态">
                <a-select placeholder="状态" :allowClear="true" v-model="queryParam.validityFlag" >
                  <a-select-option value="0">正常</a-select-option>
                  <a-select-option value="1">已过期</a-select-option>
                  <a-select-option value="2">近效期</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="器械分类">
                <j-dict-select-tag-expand v-model="queryParam.deviceClassification" dictCode="device_classification" placeholder="请选择器械分类"/>
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
            <a-col :md="6" :sm="8">
              <a-form-item label="产品组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select
                  showSearch
                  :groupId="groupValue"
                  placeholder="请选择产品组别"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="groupHandleSearch"
                  @change="groupHandleChange"
                  @focus="groupHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.groupId"
                >
                  <a-select-option v-for="d in groupData" :key="d.value">{{d.text}}</a-select-option>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('产品')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" icon="copy" @click="copy()">复制</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
          <a-menu-item key="2" @click="handleChargeCode"><a-icon type="edit"/>批量修改产品收费代码</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        :rowClassName="setdataCss"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pdProduct-modal ref="modalForm" @ok="modalFormOk"></pdProduct-modal>
    <a-modal :visible="chargeCodeVisible"  :maskClosable="false"  :confirmLoading="confirmLoading"
             @ok="handleOk" :width="900" @cancel="handleCancel">
      <a-form :form="form">
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="24" >
            <a-form-item label="产品收费代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input autocomplete="off" placeholder="请输入产品收费代码" v-decorator="[ 'chargeCode', validatorRules.chargeCodeT]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdProductModal from './modules/PdProductModal'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from  '@/api/manage'
  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import pick from 'lodash.pick'
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
    name: "PdProductList",
    mixins:[JeecgListMixin],
    components: {
      PdProductModal,
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
        groupData: [],
        groupValue: undefined,
        categoryOneData: [],
        categoryOneValue: undefined,
        categoryTwoData: [],
        categoryTwoValue: undefined,
        chargeCodeVisible:false,
        confirmLoading: false,
        copyRecord:"",
        form: this.$form.createForm(this),
        model: {},
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
            title:'二级分类',
            align:"center",
            dataIndex: 'categoryTwoName'
          },
          {
            title:'产品组别',
            align:"center",
            dataIndex: 'groupName'
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
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'产品收费代码',
            align:"center",
            dataIndex: 'chargeCode'
          },
          {
            title:'注册证',
            align:"center",
            dataIndex: 'registration'
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
          list: "/pd/pdProduct/list",
          delete: "/pd/pdProduct/delete",
          deleteBatch: "/pd/pdProduct/deleteBatch",
          exportXlsUrl: "/pd/pdProduct/exportXls",
          importExcelUrl: "pd/pdProduct/importExcel",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryGroup:"/pd/pdGroup/getGroupList",
          queryCategoryOne:"/pd/pdCategory/getCategoryOneList?type=0",
          queryCategoryTwo:"/pd/pdCategory/getCategoryOneList?type=1",
          editChargeCodeBatch:"/pd/pdProduct/editChargeCodeBatch",
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
      //组别查询start
      groupHandleSearch(value) {
        fetch(value, data => (this.groupData = data),this.url.queryGroup);
      },
      groupHandleChange(value) {
        this.groupValue = value;
        fetch(value, data => (this.groupData = data),this.url.queryGroup);
      },
      //组别查询end
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
      //一级分类查询end
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
      setdataCss(record,index) {
        let validityFlag = record.validityFlag;
        return "validityFlag"+validityFlag;
      },
      //产品收费代码提交
      handleOk(){
        const that = this;
        // 触发表单验证
        that.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = that.url.editChargeCodeBatch;
            let method = 'post';
            let formData = Object.assign(that.model, values);
            if (that.selectedRowKeys.length <= 0) {
              that.$message.warning('请选择一条记录！');
              return;
            } else {
              let ids = "";
              for (let a = 0; a < that.selectedRowKeys.length; a++) {
                ids += that.selectedRowKeys[a] + ",";
              }
              let formDataAll = new FormData();
              formDataAll.append("ids",ids);
              for (let obj in formData) {
                formDataAll.append(obj, formData[obj]?formData[obj]:"");
              }
              //console.log("表单提交数据",formData)
              httpAction(httpurl,formDataAll,method).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.$emit('ok');
                  this.loadData();
                }else{
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }
          }
        })
      },
      copy(){
        let selectionRows = this.selectionRows;
        if(selectionRows.length>0){
          if(selectionRows.length==1){
            this.copyRecord = Object.assign({}, selectionRows[0]);
            this.$message.warning("复制成功");
          }else{
            this.$message.warning("不能选择多行复制");
          }
        }else{
          this.$message.warning("请选择一行进行复制");
        }
      },
      //新增方法重写  加入复制
      handleAdd: function () {
        if(this.copyRecord){
          let record = this.copyRecord;
          record.number = "";
          record.id="";
          this.copyRecord = "";
          this.selectedRowKeys=[];
          this.selectionRows=[];
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title = "新增";
          this.$refs.modalForm.disableSubmit = false;
        }else{
          this.$refs.modalForm.add();
          this.$refs.modalForm.title = "新增";
          this.$refs.modalForm.disableSubmit = false;
        }

      },
      //供应商查询end
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
      }
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