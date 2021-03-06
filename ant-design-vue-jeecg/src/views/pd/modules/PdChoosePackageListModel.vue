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
              <a-form-item label="套包编号">
                <a-input placeholder="请输入套包编号" v-model="queryParam.packageCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="套包名称">
                <a-input placeholder="请输入套包名称" v-model="queryParam.packageName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="所属科室">
                <a-select
                  showSearch
                  placeholder="请选择科室"
                  :supplierId="departValue"
                  :defaultActiveFirstOption="false"
                  :showArrow="true"
                  :filterOption="false"
                  :allowClear="true"
                  @search="departHandleSearch"
                  @focus="departHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.departId"
                >
                  <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
                </a-select>
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
        :customRow="onClickRow"
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
    name: "PdChoosePackageListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择套包",
        width:1200,
        visible: false,
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        confirmLoading: false,

        notFoundContent:"未找到内容",
        departValue: undefined,
        departList:[],

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
            title:'套包编号',
            align:"center",
            dataIndex: 'packageCode'
          },
          {
            title:'套包名称',
            align:"center",
            dataIndex: 'packageName',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'产品总数',
            align:"center",
            dataIndex: 'packageSum'
          },
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          }
        ],
        innerColumns:[
          {
            title:'产品编号',
            align:"center",
            width: 100,
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
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
            title:'套包产品数量',
            align:"center",
            width: 100,
            dataIndex: 'count'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'产品ID',
            align:"center",
            dataIndex: 'productId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
        ],
        url: {
          list: "/pd/pdPackage/queryPackgeList",
          chooseDetailList:"/pd/pdPackage/queryPdPackageDetailList",
          departList:"/pd/pdDepart/getSysDepartList",
        },
        dictOptions:{
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
          getAction(this.url.chooseDetailList, {packageId: record.id}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },
      close () {
        this.expandedRowKeys=[];
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        if(this.selectionRows.length == 1){
          let params = { packageId: this.selectionRows[0].id }
          getAction(this.url.chooseDetailList, params).then((res) => {
            if (res.success) {
              let data = res.result;
              let rows = this.selectionRows;
              rows[0].pdPackageDetailList = data;
              this.$emit('ok', rows);
              this.close();
            }
          });
          // let rows = this.selectionRows;
          // this.$emit('ok', rows);
          // this.close();
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

      },
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              let td = pathArray[0];//获取当前点击的是第几列
              let cellIndex = td.cellIndex;
              let tr = pathArray[1];//获取tr
              let lie = tr.childElementCount;//获取一共多少列
              this.selectedRowKeys = [];
              this.selectionRows = [];
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex) > 0){
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
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value,parentFlag:"0"}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
        })
      },
    }
  }
</script>