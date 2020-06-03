<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="disableSubmit"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->

      <div style="background:#ECECEC; padding:20px" class="table-page-search-wrapper">
        <a-card title="" style="">
          <a-form :form="form" layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="5" :sm="8">
                <a-form-item label="巡查单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'patrolNo', {}]" placeholder="请输入巡查单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="巡查日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选巡查日期" v-decorator="[ 'patrolDate', {}]" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="巡查库房" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="!showPatrolDepartName">
                  <a-select
                    showSearch
                    placeholder="请选巡查库房"
                    :disabled="disableSubmit"
                    :defaultActiveFirstOption="false"
                    :allowClear="true"
                    :showArrow="true"
                    :filterOption="false"
                    @search="departHandleSearch"
                    @focus="departHandleSearch"
                    @change="departHandleChange"
                    :notFoundContent="notFoundContent"
                    v-model="queryParam.departId"
                  >
                    <a-select-option v-for="d in departList" :key="d.id" :text="d.departName" >{{d.departName}}</a-select-option>
                  </a-select>
                </a-form-item>
                <a-form-item label="巡查部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
                  <a-input disabled v-decorator="[ 'patrolDepartId', {}]" ></a-input>
                </a-form-item>
                <a-form-item label="巡查库房" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="showPatrolDepartName">
                  <a-input disabled v-decorator="[ 'patrolDepartName', {}]" ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24"  v-show="!disableSubmit">
              <a-col :md="5" :sm="8">
                <a-form-item label="产品名称">
                  <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="产品编号">
                  <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="批号">
                  <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="有效期">
                  <a-range-picker @change="expDateChange" v-model="queryParam.queryDate"/>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="8">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                  <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
        <a-card style="margin-top: 10px;">
          <!-- 子表单区域 -->
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="库存明细" :key="refKeys[0]" :forceRender="true">
              <j-editable-table
                :ref="refKeys[0]"
                :loading="pdStoreroomPatrolDetailTable.loading"
                :columns="pdStoreroomPatrolDetailTable.columns"
                :dataSource="pdStoreroomPatrolDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :disabled="disableSubmit"/>
            </a-tab-pane>
          </a-tabs>
        </a-card>
        <a-card style="margin-top: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="温度（℃）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input :disabled="disableSubmit" v-decorator="[ 'temperature', validatorRules.temperature]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="湿度（%）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input :disabled="disableSubmit" v-decorator="[ 'wetness', validatorRules.wetness]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>

  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { filterObj } from '@/utils/util';
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  export default {
    name: 'PdStoreroomPatrolModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
    },
    data() {
      return {
        queryParam:{},
        showPatrolDepartName:false,

        /* 排序参数 */
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        /* 筛选参数 */
        filters: {},
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },

        initData:{},
        disableSubmit:false,

        //部门下拉列表 start
        departValue: undefined,
        notFoundContent:"未找到内容",
        departList:[],
        inDepartName:"",
        //部门下拉列表 end
        labelCol: {span: 6},
        wrapperCol: {span: 16},

        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          temperature:{rules: [{required: true, message: '请输入温度!'},{pattern: '^-?\\d+\\.?\\d*$$',message: '只能输入数字' }]},
          wetness:{rules: [{required: true, message: '请输入湿度!'},{pattern: '^-?\\d+\\.?\\d*$',message: '只能输入数字' }]},
        },
        refKeys: ['pdStoreroomPatrolDetail', ],
        tableKeys:['pdStoreroomPatrolDetail', ],
        activeKey: 'pdStoreroomPatrolDetail',
        // pd_storeroom_patrol_detail
        pdStoreroomPatrolDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品主键', key: 'productId', type: FormTypes.hidden },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.hidden },
            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            // { title: '库房名称', key: 'departName', width:"200px" },
            { title: '产品名称', key: 'productName', width:"200px" },
            { title: '产品编号', key: 'number', width:"150px" },
            { title: '规格', key: 'spec', width:"150px" },
            // { title: '型号', key: 'version', width:"200px" },
            { title: '批号', key: 'batchNo', width:"120px" },
            { title: '有效期', key: 'expDate', width:"90px" },
            { title: '数量', key: 'stockNum', width:"90px" },
            { title: '单位', key: 'unitName', width:"90px" },
            { title: '注册证号', key: 'registration', width:"200px" },
            { title: '生产厂家', key: 'venderName', width:"200px" },
            { title: '是否过期', key: 'isExp', width:"90px" },
            {
              title: '巡查结果',
              key: 'patrolResult',
              type: FormTypes.select,
              width:"100px",
              options: [
                { title: '合格', value: '合格' },
                { title: '不合格', value: '不合格' },
                { title: '其他', value: '其他' }
              ],
              allowSearch:true,
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '备注',
              key: 'remarks',
              type: FormTypes.input,
              placeholder: '${title}',
              defaultValue: '',
            },
          ]
        },
        popModal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
        url: {
          add: "/pd/pdStoreroomPatrol/add",
          edit: "/pd/pdStoreroomPatrol/edit",
          init:"/pd/pdStoreroomPatrol/initModal",
          stockList:"/pd/pdStoreroomPatrol/stockList",
          departList:"/pd/pdDepart/getSysDepartList",
          pdStoreroomPatrolDetail: {
            list: '/pd/pdStoreroomPatrol/queryPdStoreroomPatrolDetailByMainId'
          },
        }
      }
    },
    methods: {
      close() {
        this.visible = false;
        this.pdStoreroomPatrolDetailTable.dataSource = [];
        this.queryParam = {};
        this.form.setFieldsValue({patrolNo:""});
        this.form.setFieldsValue({patrolDate:""});
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData(){

        let params = {};
        if(this.model.id){
          this.popModal.title="巡查明细";
          this.showPatrolDepartName = true;
          let fieldval = pick(this.model,'patrolNo','patrolDate','temperature','wetness','patrolDepartName')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = { id: this.model.id }
        }else{
          this.popModal.title="新增巡查";
          params = { id: "" }
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){
                this.pdStoreroomPatrolDetailTable.dataSource = res.result.pdStoreroomPatrolDetailList || [];
                // this.requestSubTableData(this.url.pdStoreroomPatrolDetail.list, params, this.pdStoreroomPatrolDetailTable)
              }else{

                this.initData = res.result;
                let fieldval = pick(this.initData,'patrolNo','patrolDate','temperature','wetness','patrolDepartName');

                this.$nextTick(() => {
                  this.form.setFieldsValue(fieldval);
                });

                // 加载子表数据
                let params = { id: this.model.id }
              }
            })
          }
        })
      },
      submitBtn(){
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }

          let formData = this.classifyIntoFormData(allValues);

          if(formData.pdStoreroomPatrolDetailList.length <= 0){
            this.$message.warning("巡查明细为空，不能提交！");
            return;
          }

          this.confirmLoading = true;
          httpAction(this.url.add, formData, "post").then((res) => {
            if (res.success) {
              this.$message.success(res.message);
              this.$emit('ok');
              this.close();
            } else {
              this.$message.warning(res.message);
            }
          }).finally(() => {
            this.confirmLoading = false;
          })
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })

      },
      searchQuery(){
        if(!this.queryParam.departId){
          this.$message.error("请先选择巡查库房！");
          return;
        }
        let params = this.getQueryParams();
        this.pdStoreroomPatrolDetailTable.loading = true;
        getAction(this.url.stockList, params).then((res) => {
          if (res.success) {
            this.pdStoreroomPatrolDetailTable.dataSource = res.result.records;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.pdStoreroomPatrolDetailTable.loading = false;
        })
      },
      searchReset() {
        // this.queryParam = {};
        this.queryParam.productName = "";
        this.queryParam.number = "";
        this.queryParam.batchNo = "";
        this.queryParam.queryDateStart = "";
        this.queryParam.queryDateEnd = "";
        this.searchQuery();
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
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
      departHandleChange(value,option){
        this.form.setFieldsValue({patrolDepartId:value});
      },
      expDateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      getQueryParams() {
        var param = this.queryParam;
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },


      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdStoreroomPatrolDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'patrolNo','patrolDate','temperature','wetness','patrolDepartName'))
     },

    }
  }
</script>

<style scoped>
</style>