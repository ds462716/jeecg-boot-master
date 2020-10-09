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
    <div class="numberWARAP">
      <div class="total">入库总数量：<span id="totalNum">{{this.validatorRules.productTotNum}}</span></div>
      <div class="nearTime">入库总金额：<span id="nearNum">{{this.validatorRules.inPrice}}</span></div>
      <div class="overTime">出库总数量：<span id="overNum">{{this.validatorRules.productOutTotNum}}</span></div>
      <div class="overMomy">出库总金额：<span id="overMomy">{{this.validatorRules.outPrice}}</span></div>
    </div>
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <!--<a-col :md="6" :sm="8">
              <a-form-item label="生产厂家">
                <a-input placeholder="请输入生产厂家" v-model="queryParam.venderName"></a-input>
              </a-form-item>
            </a-col>-->
            <a-col :md="8" :sm="8">
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
                  :disabled="supplierSelecDisabled"
                >
                  <a-select-option v-for="d in venderData" :key="d.value">{{d.text}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>


            <a-col :md="6" :sm="8">
              <a-form-item label="产品批号">
                <a-input placeholder="请输入产品批号" v-model="queryParam.batchNo"></a-input>
              </a-form-item>
            </a-col>
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
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        @change="handleTableChange">
      </a-table>
    </a-spin>
  </a-modal>
</template>
<script>
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: "PdStockRecordDetailInfoModal",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"出入库明细",
        venderData: [],
        venderValue: undefined,
        notFoundContent:"未找到内容",
        supplierSelecDisabled:false,
        width:1200,
        visible: false,
        model:{},
        validatorRules: {
          productTotNum:{},//入库总数量
          productOutTotNum:{},//出库总数量
          inPrice:{},//入库总金额
          outPrice:{},//出库总金额
        },
        confirmLoading: false,
        // 表头
        columns: [
          {
            title:' 单号',
            align:"center",
            dataIndex: 'recordNo'
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
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'出入库类型',
            align:"center",
            dataIndex: 'recordType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['recordType'], text+"")
              }
            }
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'productNum'
          },
          {
            title: '入库单价',
            align: "center",
            dataIndex: 'purchasePrice'
          },
          {
            title:'入库金额',
            align:"center",
            dataIndex: 'inTotalPrice'
          },
          {
            title: '出库单价',
            align: "center",
            dataIndex: 'sellingPrice'
          },
          {
            title:'出库金额',
            align:"center",
            dataIndex: 'outTotalPrice'
          },
          {
            title:'入库科室',
            align:"center",
            dataIndex: 'inDepartName'
          },
          {
            title:'出库科室',
            align:"center",
            dataIndex: 'outDepartName'
          },
          {
            title:'出入库时间',
            align:"center",
            dataIndex: 'submitDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title:'注册证号',
            align:"center",
            dataIndex: 'productRegistration'
          },

        ],
        url: {
          list: "/pd/pdProductStockTotal/stockInAndOutRecordDetailQuery",
          queryVender:"/pd/pdVender/getVenderList",
        },
        dictOptions:{
          recordType:[],
        },
        tableScroll:{x :13*147+50},
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
            this.validatorRules.productTotNum=res.result.productTotNum;
            this.validatorRules.productOutTotNum=res.result.productOutTotNum;
            this.validatorRules.inPrice=res.result.inPrice;
            this.validatorRules.outPrice=res.result.outPrice;
            this.dataSource = res.result.page.records;
            this.ipagination.total = res.result.page.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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

      //生产厂家查询start
      venderHandleSearch(value) {
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      venderHandleChange(value) {
        this.venderValue = value;
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      //生产厂家查询end

      initDictConfig(){ //静态字典值加载
        initDictOptions('stock_record_type').then((res) => { //出入库类型
          if (res.success) {
            this.$set(this.dictOptions, 'recordType', res.result)
          }
        })

      }
    }
  }



  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url, {name: value}).then((res) => {
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
    timeout = setTimeout(fake, 0); //这边不延迟
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:20px;line-height:20px;margin:1px 0;}
  .numberWARAP>div{float:left;width:25%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(4){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
  @import '~@assets/less/common.less'
</style>