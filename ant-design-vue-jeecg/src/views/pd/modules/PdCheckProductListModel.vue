<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null"
     >
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
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="8" :sm="8">
                <a-form-item label="供应商">
                  <a-select
                    ref="supplierSelect"
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
                    :disabled="supplierSelecDisabled"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
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
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        >
          <span slot="action" slot-scope="text, record">
          <a @click="handleOk(record)">选中</a>
        </span>
      </a-table>
    </a-spin>
    <div class="drawer-bootom-button">
        <a-button style="margin-right: 15px;" @click="handleCancel">返回</a-button>
    </div>
  </a-modal>
</template>

<script>

  import Vue from 'vue'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { FormTypes } from '@/utils/JEditableTableUtil'

  export default {
    name: "PdCheckProductListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择产品",
        width:1200,
        visible: false,
        supplierId:"", //供应商ID 用于入库时查询产品
        // model: {},
        venderData: [],
        venderValue: undefined,
        confirmLoading: false,
        supplierSelecDisabled:false,
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

        // 表头
        columns: [
          /*{
            title: '#',
            dataIndex: 'productId',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'理论库存数量',
            align:"center",
            dataIndex: 'stockNum'
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
            title: '有效期',
            align:"center",
            dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: "center",
            width: 100
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/queryCheckStockList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
        }
      }
    },
    methods: {
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.queryParam = {};
        this.dataSource=[];
        this.loadData(1);
        this.$emit('close');
        this.visible = false;
      },
      show(params) {
          this.departId = params.departId;
          this.$nextTick(() => {
            this.supplierHandleSearch(); //初始化供应商
            this.venderHandleSearch(); //初始化生产厂家
          })

        this.loadData(1);
        this.visible = true;
      },
      handleOk (record) {
        this.$emit('ok', record);
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

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
        if(this.departId){
          params.departId = this.departId;
        }
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
  .drawer-bootom-button {
    width: 100%;
    text-align: center;
    background: #fff;
    margin-top:10px;
  }
</style>