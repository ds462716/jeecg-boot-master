<template>
  <a-modal
    :visible="visible"
    :width="popModal.width"
    :style="popModal.style"
    :maskClosable="disableSubmit"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :footer="null">
    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button icon="fullscreen" style="width:56px;height:100%;border:0" @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">
      <div style="background:#ECECEC; padding:20px">
      <!-- 主表单区域 -->
      <a-card title="" style="margin-bottom: 10px;">
      <a-form :form="form">
        <a-row>

          <a-col :span="12">
            <a-form-item label="盘点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input  disabled="disabled" v-decorator="[ 'checkNo', validatorRules.checkNo]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'deptName', validatorRules.deptName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date disabled="disabled" v-decorator="[ 'checkDate', validatorRules.checkDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'checkName', validatorRules.checkName]" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点产品量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'shouldCount', validatorRules.shouldCount]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="已盘产品量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number  disabled="disabled" v-decorator="[ 'checkCount', validatorRules.checkCount]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘盈盘亏数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'profitLossCount', validatorRules.profitLossCount]"  style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      </a-card>
        <a-card style="margin-bottom: 10px;">
      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="盘点明细表" :key="refKeys[0]" :forceRender="true">
          <div style="margin-bottom: 8px;">
            <a-button v-show="!disableSubmit" type="primary" icon="plus" @click="choice">选择产品</a-button>
            <span style="padding-left: 8px;"></span>
            <a-popconfirm
              :title="`确定要删除吗?`"
              @confirm="handleConfirmDelete">
              <a-button v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
              <span class="gap"></span>
            </a-popconfirm>
          </div>
          <j-editable-table
            :ref="refKeys[0]"
            :loading="pdProductStockCheckChildTable.loading"
            :columns="pdProductStockCheckChildTable.columns"
            :dataSource="pdProductStockCheckChildTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="false"/>
        </a-tab-pane>
        
      </a-tabs>
        </a-card>
      </div>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk('submit')" type="primary" :loading="confirmLoading" style="margin-right: 15px;">盘点完成</a-button>
      <a-button @click="handleOk('save')" type="primary" :loading="confirmLoading" style="margin-right: 15px;">临时保存</a-button>
      <a-popconfirm title="确定放弃盘点吗？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取消</a-button>
      </a-popconfirm>
    </div>
    <pd-product-stock-check-add-modal  ref="PdProductStockCheckAddModal" @ok="modalFormOk"></pd-product-stock-check-add-modal>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import { httpAction,getAction,downFile,inArray} from '@/api/manage'
  import PdProductStockCheckAddModal from './PdChooseProductListModel'
  export default {
    name: 'PdProductStockCheckModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,PdProductStockCheckAddModal
    },
    data() {
      return {
        model:{},
        disableSubmit:false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          checkNo: {rules: []},
          departId: {rules: []},
          deptName: {rules: []},
          checkDate: {rules: []},
          checkBy: {rules: []},
          checkName: {rules: []},
          shouldCount: {rules: []},
          checkCount: {rules: []},
          profitLossCount: {rules: []},
          checkStatus: {rules: []},
          remarks: {rules: []},
        },
        refKeys: ['pdProductStockCheckChild', ],
        tableKeys:['pdProductStockCheckChild', ],
        activeKey: 'pdProductStockCheckChild',
        // 盘点明细表
        pdProductStockCheckChildTable: {
          loading: false,
          dataSource: [],
          columns: [
            {title: '产品名称', key: 'productName',width:"200px"},
            {title: '产品id', key: 'productId',width:"200px"},
            {title: '产品编号', key: 'number',width:"200px"},
            {title: '产品规格', key: 'spec',width:"200px"},
            {title: '产品批号', key: 'batchNo',width:"200px"},
            {title: '产品有效期', key: 'expDate',width:"200px"},
            {title: '产品单位', key: 'unitName',width:"200px"},
            {title: '产品理论数量', key: 'stockNum',width:"200px"},
            {title: '盘点数量', key: 'checkNum',width:"200px"},
            {title: '盘盈盘亏', key: 'profitLossCount',width:"200px"},
            {title: '备注', key: 'checkNo',width:"200px"}
          ]
        },
        url: {
          add: "/pd/pdProductStockCheck/add",
          edit: "/pd/pdProductStockCheck/edit",
          pdProductStockCheckChild: {
            list: '/pd/pdProductStockCheck/queryPdProductStockCheckChildByMainId'
          },
        },
      popModal: {
        title: '这里是标题',
          visible: false,
          width: '100%',
          style: { top: '10px' },
        fullScreen: true
      },
      }
    },
    methods: {
      add () {//初始化新增
        this.pdProductStockCheckChildTable.dataSource = [];
        this.edit({});
        this.checkInfo();
      },

      checkInfo() { //新增页面初始化
        getAction("/pd/pdProductStockCheck/checkInfo",{}).then((res)=>{
          if (res.success) {
            this.model = res.result;
            this.$nextTick(() => {
             this.form.setFieldsValue(pick(this.model,'checkNo','deptName','checkDate','checkName','shouldCount','checkCount','profitLossCount','remarks'))
            })
          }
        })
      },

      //选择产品
      choice() {
        this.$refs.PdProductStockCheckAddModal.show();
        this.$refs.PdProductStockCheckAddModal.title = "选择产品";
      },

      handleConfirmDelete() { //删除产品
        if(this.$refs.PdProductStockCheckModal.selectedRowIds.length > 0){
          this.$refs.PdProductStockCheckModal.removeSelectedRows();
          this.$nextTick(() => {
            // 计算总数量
           // this.getTotalNumAndPrice();
          })
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },

      modalFormOk (formData) { //选择产品确定后返回所选择的数据
        let idList = [];
        let values = [];
        let checkCount=0;
        let data= this.pdProductStockCheckChildTable.dataSource;
        for(let j=0;j<data.length;j++) {
          checkCount+=data[j].checkNum;
          let prodId=data[j].productId;
          idList.push(prodId);
        }
        values=data;

        for(let i=0;i<formData.length;i++){
          let prodId=formData[i].productId;
          if(inArray(prodId, idList) ==-1) {
            values.push({
              productId: formData[i].productId,
              number: formData[i].number,
              productName: formData[i].productName,
              spec: formData[i].spec,
              version: formData[i].version,
              unitName: formData[i].unitName,
              checkNum: "1",//默认 1
              stockNum: formData[i].stockNum
            })
            checkCount+=1;//计算总数量
          }
        }

        let model={};
        this.model.checkCount=checkCount;//盘点总数量
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'checkCount'))
        })
        this.pdProductStockCheckChildTable.dataSource = values;
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'checkNo','departId','checkDate','checkBy','shouldCount','checkCount','profitLossCount','checkStatus','createTime','createBy','updateTime','updateBy','remarks','delFlag')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdProductStockCheckChild.list, params, this.pdProductStockCheckChildTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdProductStockCheckChildList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'checkNo','departId','checkDate','checkBy','shouldCount','checkCount','profitLossCount','checkStatus','createTime','createBy','updateTime','updateBy','remarks','delFlag'))
     },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.popModal.fullScreen
        if (mode) {
          this.popModal.width = '100%'
          this.popModal.style.top = '20px'
        } else {
          this.popModal.width = '1200px'
          this.popModal.style.top = '50px'
        }
        this.popModal.fullScreen = mode
      },
    }
  }
</script>

<style scoped>
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
  .tableStyle> tr > th{
    border: 1px solid #e8e8e8;
    text-align: center;
    padding: 16px 16px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    background: #fafafa;
    transition: background 0.3s ease;
    display: table-cell;
    vertical-align: inherit;
    box-sizing: border-box;
  }
  .tableStyle> tr > td{
    border:1px solid #e8e8e8;
    text-align: center;
    padding: 1px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

  .tableStyle> tr > td >input{
    width:45px;
    text-align: center;
  }
</style>