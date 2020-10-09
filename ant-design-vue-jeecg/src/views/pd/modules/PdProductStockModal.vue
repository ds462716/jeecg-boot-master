<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    :maskClosable=disableSubmit
    @cancel="handleCancel"

  >
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">返回</a-button>
    </template>
    <div class="numberWARAP">
      <div class="total">总数量：<span id="totalNum">{{this.validatorRules.pCount}}</span></div>
      <div class="nearTime">近效期数量：<span id="nearNum">{{this.validatorRules.jCount}}</span></div>
      <div class="overTime">过期数量：<span id="overNum">{{this.validatorRules.gCount}}</span></div>
    </div>
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
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
            <a-col :md="6" :sm="8">
              <a-form-item label="使用状态">
                <j-dict-select-tag-expand v-model="queryParam.nestatStatus" dictCode="nestat_status"/>
              </a-form-item>
            </a-col>
            <template :md="6" v-if="toggleSearchStatus">
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
                <a-form-item label="是否久存">
                  <j-dict-select-tag-expand v-model="queryParam.isLong" dictCode="pd_isLong"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
              <a-form-item label="是否过期">
                <j-dict-select-tag-expand v-model="queryParam.expStatus" dictCode="exp_status"/>
              </a-form-item>
            </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="批次号">
                  <a-input placeholder="请输入批次号" v-model="queryParam.batchNo"></a-input>
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
        <a-button v-show="this.model.productFlag=='1'  &&  isDisabledAuth('stock:form:specRemove')" @click="handleUpdate()" type="primary"  >规格数量清零</a-button>
       </div>
      <hr/>
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
        :rowClassName="setdataCss"
        :loading="loading"
        :scroll="tableScroll"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
         <span slot="action" slot-scope="text, record">
          <a  @click="yiHuoWei(record)">移库</a>
         <a-divider type="vertical"/>
           <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.produceDate==null">
               <a @click="handleEdit(record)">修改</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="findBarCodeDetail(record)">查看赋码</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
         </span>
      </a-table>
      </div>
    </a-spin>
    <!--修改-->
     <pd-update-stock-modal ref="PdUpdateStockModal" @ok="modalFormOk"></pd-update-stock-modal>
    <!--移庫-->
     <pd-stock-huo-wei-modal ref="PdStockHuoWeiModal" @ok="modalFormOk"></pd-stock-huo-wei-modal>
     <!--規格數量清零-->
     <pd-product-stock-spec-modal ref="modalForm1" @ok="modalFormOk"></pd-product-stock-spec-modal>
    <!--查看唯一码-->
     <pd-product-stock-unique-code-modal ref="modalUniqueForm1" @ok="modalFormOk"></pd-product-stock-unique-code-modal>


  </j-modal>
</template>

<script>

  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdUpdateStockModal from './PdUpdateStockModal'
  import PdStockHuoWeiModal from './PdStockHuoWeiModal'
  import PdProductStockSpecModal from './PdProductStockSpecModal'
  import JDate from "../../../components/jeecg/JDate";
  import { disabledAuthFilter } from "@/utils/authFilter"
  import PdProductStockUniqueCodeModal from "./PdProductStockUniqueCodeModal";
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: "PdProductStockModel",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockUniqueCodeModal,
      JDate,
      PdUpdateStockModal,
      PdStockHuoWeiModal,
      PdProductStockSpecModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"库存产品",
        width:1200,
        visible: false,
        model:{},
        validatorRules: {
          pCount:{},//总数量
          gCount:{},//过期数量
          jCount:{},//近效期数量
        },
        confirmLoading: false,
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
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
            title:'生产日期',
            align:"center",
            dataIndex: 'produceDate'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'huoweiName'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title: '生产厂家',
            align: "center",
            dataIndex: 'venderName'
          },
          {
            title:'注册证号',
            align:"center",
            dataIndex: 'registration'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'规格单位',
            align:"center",
            dataIndex: 'specUnitName'
          },
          {
            title:'库存规格数量',
            align:"center",
            dataIndex: 'specNum'
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
            title:'是否过期',
            align:"center",
            dataIndex: 'expStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['expStatus'], text+"")
              }
            }
          },
          {
            title:'是否久存',
            align:"center",
            dataIndex: 'isLong',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isLong'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            width:100,
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/selectProductStockList",
        },
        dictOptions:{
          expStatus:[],
          nestatStatus:[],
          isLong:[],
        },
        tableScroll:{ x: 1300 },
      }
    },
    created () {

    },
    methods: {
      add () {
        this.edit({});
      },
      loadData(arg){
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.productId=this.model.productId;
        params.departId=this.model.departId;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.pCount=res.result.pCount;
            this.validatorRules.gCount=res.result.gCount;
            this.validatorRules.jCount=res.result.jCount;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },

      modalFormOk(record){
        this.loadData(1);
      },


      handleUpdate() { //规格库存数量清零
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请勾选需要清零的数据！');
          return;
        }
        let  ids="";
        let batchNos="";
        //只能是使用中的试剂才能清零
        this.selectionRows.forEach(function(item,index){
          if(item.nestatStatus != '0') {
            batchNos+=item.batchNo+",";
          }else{
            ids +=item.id+",";
          }
        });

         if(batchNos !=""){
           this.$message.warning("只能清零使用中产品的规格库存数量！");
           return;
         }
        this.$refs.modalForm1.edit({"ids":ids});
        this.$refs.modalForm1.title ="清零原因"
        this.$refs.modalForm1.disableSubmit = false;
      },

      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.loadData(1);
       this.initDictConfig();
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        let rows = this.selectionRows;
        this.$emit('ok', rows);
        this.close();
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      handleEdit: function (record) { //修改
        this.$refs.PdUpdateStockModal.edit(record);
        this.$refs.PdUpdateStockModal.title = "编辑";
        this.$refs.PdUpdateStockModal.disableSubmit = false;
      },

      yiHuoWei: function (record) { //移库
        this.$refs.PdStockHuoWeiModal.edit(record);
        this.$refs.PdStockHuoWeiModal.title = "移库";
        this.$refs.PdStockHuoWeiModal.disableSubmit = false;
      },


      initDictConfig(){ //静态字典值加载
        initDictOptions('exp_status').then((res) => { //是否过期字典转换
          if (res.success) {
            this.$set(this.dictOptions, 'expStatus', res.result)
          }
        })
        initDictOptions('pd_isLong').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isLong', res.result)
          }
        })
        initDictOptions('nestat_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'nestatStatus', res.result)
          }
        })
      },
      setdataCss(record,index) {
        let expire = record.expStatus;
        return "expire"+expire;
      },
      /**
       * 校验权限
       * @param code
       * @returns {boolean|*}
       */
      isDisabledAuth(code){
        return !disabledAuthFilter(code);
      },

      //如果是唯一码查询唯一码的详细内容
      findBarCodeDetail(record){
        this.$refs.modalUniqueForm1.show(record);
        this.$refs.modalUniqueForm1.title = "查看赋码";
        this.$refs.modalUniqueForm1.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:20px;line-height:20px;margin:1px 0;}
  .numberWARAP>div{float:left;width:33%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
</style>
<style>
  .expire0{
  }
  .expire1{
    background-color:#FFFFCC;
  }
  .expire2{
    background-color:#FF3333;
  }
</style>


