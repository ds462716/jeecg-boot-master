<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="出库科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择出库科室"
                    :disabled="disableSubmit"
                    :outDeptId="outDeptValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    @search="sysDeptHandleSearch"
                    @change="sysDeptHandleChange"
                    @focus="sysDeptHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'outDeptId', validatorRules.outDeptId]"
                  >
                    <a-select-option v-for="d in outDeptData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="调拨单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'allocationNo', validatorRules.allocationNo]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="调拨日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled="disabled"  v-decorator="[ 'allocationDate', validatorRules.allocationDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'realName', validatorRules.realName]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="入库科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'inDeptName', validatorRules.inDeptName]"></a-input>
                </a-form-item>
              </a-col>
            <!--  <a-col :span="12">
                <a-form-item label="调拨总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]" style="width: 100%"/>
                </a-form-item>
              </a-col>-->
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%;height: 60px"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="调拨明细表" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;">
                <a-button v-show="!disableSubmit" type="primary" icon="plus" @click="choice">选择产品</a-button>
                <span style="padding-left: 8px;"></span>
                <a-popconfirm
                  :title="`确定要删除吗?`"
                  @confirm="handleProdDelete">
                  <a-button v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
                <span style="padding-left: 8px;"></span>
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="table1.loading"
                :columns="table1.columns"
                :dataSource="table1.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :disabled="disableSubmit"
                :actionButton="false"
                @valueChange="valueChange"
                style="text-overflow: ellipsis;"
              >
              </j-editable-table>

              <span style="padding-left: 8px;"></span>
              <div style="margin-bottom: 8px;">
                <a-button v-show="!disableSubmit" type="primary" icon="plus" @click="choicePackage">选择套包</a-button>
                <span style="padding-left: 8px;"></span>
                <a-popconfirm
                  :title="`确定要删除吗?`"
                  @confirm="handlePackDelete">
                  <a-button v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
                <span style="padding-left: 8px;"></span>
              </div>


              <a-table
                ref="table"
                bordered
                rowKey="packageRecordId"
                :columns="table2.columns"
                :dataSource="table2.dataSource"
                :loading="table1.loading"
                :pagination="ipagination"
                :expandedRowKeys= "expandedRowKeys"
                :rowSelection="{type:'radio',selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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
                  :loading="subloading">
                </a-table>


                <span slot="action" slot-scope="text, record">
             <a-input
              autocomplete="off"
              ref="inputFocus"
              aria-required="false"
              :disabled="disableSubmit"
              @change="(e)=>valuePackChange(e,record)"
              v-model="record.allocationNum"
              type="number"
                />
        </span>
              </a-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{this.model.totalNum }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>
        <a-card style="margin-bottom: 10px;" v-show="disableSubmit">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left">
                  <a-textarea :disabled="true" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审核意见"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk('save')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
      <a-button @click="handleOk('submit')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>
    <pd-allocation-detail-add-modal  ref="PdAllocationDetailAddModal" @ok="modalFormOk"></pd-allocation-detail-add-modal>
    <pd-allocation-package-add-modal  ref="PdAllocationPackageAddModal" @ok="modalFormInfoOk"></pd-allocation-package-add-modal>
  </j-modal>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile,inArray} from '@/api/manage'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import {JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import PdAllocationDetailAddModal from './PdProductStockListModel'
  import PdAllocationPackageAddModal from './PdChoosePackageRecordListModel'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { filterObj } from '@/utils/util';

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: 'NewPdAllocationRecordModal',
    mixins: [JEditableTableMixin,JeecgListMixin],
    components: {
      JDate,
      JDictSelectTag,
      PdAllocationDetailAddModal,
      PdAllocationPackageAddModal},
    data() {
      return {
        model:{},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        confirmLoading: false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        //出库科室下拉列表 start
        outDeptValue: undefined,
        notFoundContent:"未找到内容",
        outDeptData: [],
        //出库科室下拉列表 end
        validatorRules: {
            allocationNo:{},
            allocationDate:{},
            allocationBy:{},
            realName:{},
            outDeptId:{ rules: [{ required: true, message: '请选择出库科室!' }] },
            outDeptName:{},
            inDeptId:{},
            inDeptName:{},
            totalNum: {},
            remarks:{},
            rejectReason:{}
        },
        refKeys: ['pdAllocationDetail', ],
        tableKeys:['pdAllocationDetail', ],
        activeKey: 'pdAllocationDetail',
        // 调拨单详细表
        table1: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', width:"250px",  key: 'productName' },
            { title: '产品编号',width:"200px",  key: 'number' },
            { title: '规格',width:"240px", key: 'spec' },
            { title: '型号', width:"240px", key: 'version' },
            { title: '单位',width:"50px",  key: 'unitName' },
            { title: '发货数量', width:"100px", key: 'arrivalNum' },
            {title: '调拨数量', key: 'allocationNum', type: FormTypes.input, width:"100px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },
                { pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',message: '${title}的格式不正确' }]
            },
            { title: '库存数量', key: 'currentStockNum'},
            { title: '出库科室库存数量',  key: 'stockNum',type: FormTypes.hidden},
          ]
        },


        // 表头
        table2:{
          dataSource:[],
          columns: [
            {title: '#', dataIndex: 'packageRecordId', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            {title:'套包编号', align:"center",key:"packageCode", dataIndex: 'packageCode'},
            {title:'套包名称', align:"center", key:"packageName",dataIndex: 'packageName'},
            { title:'套包条码', align:"center", dataIndex: 'packageBarCode' },
            {title:'产品总数', align:"center", key:"packageSum",dataIndex: 'packageSum'},
            {title:'调拨数量', align:"center",key:"allocationNum",  dataIndex: 'allocationNum',
            },
            { title:'打包人', align:"center", dataIndex: 'createBy' },
            {title: '打包时间', align: "center", dataIndex: 'createTime'},
            {title:'备注', align:"center", key:"remarks",dataIndex: 'remarks'}
          ]
        },
        innerColumns:[
          { title:'产品名称', align:"center", dataIndex: 'productName' },
          { title:'产品编号', align:"center", dataIndex: 'productNumber' },
          { title:'产品条码', align:"center",dataIndex: 'productBarCode' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'批号', align:"center", dataIndex: 'batchNo' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title:'有效期', align:"center", dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'入库单价', align:"center", dataIndex: 'purchasePrice' },
          { title:'出库单价', align:"center", dataIndex: 'sellingPrice' },
          { title:'套包产品数量', align:"center", dataIndex: 'productNum' },
          { title:'出库金额', align:"center", dataIndex: 'outTotalPrice' },
          { title:'库存数量', align:"center", dataIndex: 'stockNum' },
          { title: '出库货位', align:"center", dataIndex: 'outHuoweiName' },
          { title: '生产日期', align:"center", dataIndex: 'produceDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }}
        ],
        url: {
          add: "/pd/pdAllocationRecord/add",
          edit: "/pd/pdAllocationRecord/edit",
          exportXlsUrl: "/pd/pdAllocationRecord/exportXls",
          querySysDepartList:"/pd/pdDepart/getSysTwoDepartList",
          chooseDetailList:"/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId",
          pdAllocationDetail: {
            list: '/pd/pdAllocationRecord/queryPdAllocationDetailList',
            packList: '/pd/pdAllocationRecord/queryAllocationDetailPack'

          },
        },

      }
    },
    methods: {
      add () { //初始化新增
        this.table1.dataSource = [];
        this.table2.dataSource = [];
        this.innerData = [];
        this.edit({});
        this.allocationInfo();
      },
      loadData(arg) {
      },
        allocationInfo() { //新增页面初始化
          getAction("/pd/pdAllocationRecord/allocationInfo",{}).then((res)=>{
            if (res.success) {
              let model={};
              this.model=res.result;
              this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model,'allocationNo','allocationDate','inDeptName','outDeptName','outDeptId','realName','remarks','rejectReason'))
              })
            }
          })
      },






      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.subloading = true;
          this.expandedRowKeys.push(record.packageRecordId);
          getAction(this.url.chooseDetailList, {id: record.packageRecordId}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },


      // 产品表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if(type === FormTypes.input){
            if(column.key === "allocationNum"){
              // 申领数量变更
              target.setValues([{rowKey: row.id, values: {allocationNum :row.allocationNum }}]);
              this.$nextTick(() => {
                // 计算总数量
                this.getTotalNumAndPrice([]);
              })
            }
          }
        }
      },
      valuePackChange(e,row){
        let val = e.target.value;
        row.allocationNum=val;
        this.$nextTick(() => {
          // 计算总数量
          this.getTotalNumAndPrice([]);
        })
      },
      //选择产品
      choice() {
          //判断是否选择了出库科室
          let outDeptId = this.form.getFieldValue("outDeptId");
          if(outDeptId==null){
            this.$message.warning("请先选择出库科室")
            return
          }
          this.$refs.PdAllocationDetailAddModal.show({departId:outDeptId});
      },

      //选择套包产品
      choicePackage() {
        //判断是否选择了出库科室
        let outDeptId = this.form.getFieldValue("outDeptId");
        if(outDeptId==null){
          this.$message.warning("请先选择出库科室")
          return
        }
        this.$refs.PdAllocationPackageAddModal.show();
        this.$refs.PdAllocationPackageAddModal.title = "选择套包";
      },

      handleProdDelete() {  //删除产品
        if(this.$refs.pdAllocationDetail.selectedRowIds.length > 0){
          this.$refs.pdAllocationDetail.removeSelectedRows();
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice([]);
          })
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },

      handlePackDelete() {  //删除套包
        let dataId = [];
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          //删除套包
          let packageId= this.selectionRows[0].packageId;
          let data=this.table2.dataSource;
          data.forEach((item,index)=>{
            if(packageId==item.packageId){
              data.splice(index,1)
            }
          })
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice([]);
          })
        }
      },

      //-------------------------
      // 计算总数量
      getTotalNumAndPrice(rows){
        let totalNum=0;//总申领数量
        let data=this.table2.dataSource;//套包的
        const that = this;
        data.forEach((item,ids) => {
          totalNum+=parseFloat(item.packageSum) * parseFloat(item.allocationNum);
        });
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdAllocationDetail.getValuesSync({validate: false});//产品的
            rows =values;
          }
          rows.forEach((item, idx) => {
            totalNum += parseFloat(item.allocationNum);
          })
          this.model.totalNum = totalNum;
          //this.form.setFieldsValue(pick(this.model, 'totalNum'))
        })
      },
      modalFormOk (formData) { //选择产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdAllocationDetail.getValues((error, values) => {
          formData.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              if(item.productId==value.productId){
                bool=false;
              }
            })
            if(bool){
              data.push(item);
            }
          })
          data.forEach((item, idx) => {
            this.table1.dataSource = values;
            this.addProdRows(item);
          })

          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice(values);
          })
        })
      },
      addProdRows(row) {  //新增产品
        let data = {
          productId: row.productId,
          number: row.number,
          productName:row.productName,
          spec: row.spec,
          version:row.version,
          unitName:row.unitName,
          allocationNum: "1",//默认 1
          stockNum:row.stockNum,
          currentStockNum:row.currentStockNum
        }
        this.table1.dataSource.push(data)
      },

      modalFormInfoOk (rows) { //选择套包产品确定后返回所选择的数据
        var  formData= rows.pdPackageRecordList;
        let data = [];
        let source=this.table2.dataSource;
        formData.forEach((item, idx) => {
          let bool = true;
          source.forEach((value, idx) => {
            if(item.packageId==value.packageId){
              bool=false;
            }
          })
          if(bool){
            data.push(item);
          }
        })
        data.forEach((item, idx) => {
          this.table2.dataSource = source;
          this.addPackRows(item);
        })
        this.$nextTick(() => {
          // 计算总数量
          this.getTotalNumAndPrice([]);
        })

      },

      addPackRows(row) {  //新增套包
        let data = {
          packageRecordId:row.id,
          packageId:row.packageId,
          packageCode:row.packageCode,
          packageName:row.packageName,
          packageSum:row.packageSum,
          packageBarCode:row.packageBarCode,
          allocationNum:"1",
          createBy:row.createBy,
          /* createTime:row.createTime,*/
          remarks:row.remarks
        }
        this.table2.dataSource.push(data)
      },


      handleOk (submitType) { //提交
        this.model.submitStatus = '1';
        if(submitType=="submit"){
          this.model.submitStatus='2';
          this.model.auditStatus='1';
        }
        const that = this;
        // 触发表单验证
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          let data= this.table2.dataSource;
          let packageName=null;
          data.forEach((item,ids)=>{ //合并套包
            if(item.allocationNum <=0 || item.allocationNum=='' ||item.allocationNum==null){
              packageName+=item.packageName+",";
            }
            formData.pdAllocationDetailList.push(item);
          });

          if(packageName!=null){
            that.$message.error("套包调拨数量不能为空或不能为0");
            return;
          }

          let pdAllocationDetailList=formData.pdAllocationDetailList;
          if(pdAllocationDetailList.length>0){
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            that.confirmLoading = true;
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
            that.$message.error("请选择产品");
          }
        })




      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval =  pick(this.model,'allocationNo','allocationDate','inDeptName','outDeptName','outDeptId','realName','remarks','rejectReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        //初始化出库科室
        this.sysDeptHandleSearch();
        //加载子表数据
        if (this.model.id) {
          let params = {allocationNo: this.model.allocationNo,productAttr:"1"}
          this.requestSubTableData(this.url.pdAllocationDetail.list, params, this.table1)//加载产品
          params =  {allocationNo: this.model.allocationNo,productAttr:"2" }
          this.requestSubTableData(this.url.pdAllocationDetail.packList, params, this.table2)//加载套包
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdAllocationDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'allocationNo','allocationDate','inDeptName','outDeptName','outDeptId','realName','remarks','rejectReason'))
      },


      //-----------------出库科室查询start
      sysDeptHandleSearch(value) {
        fetch(value, data => (this.outDeptData = data),this.url.querySysDepartList);
      },

      sysDeptHandleChange(value) {
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.outDeptValue = value;
        fetch(value, data => (this.outDeptData = data),this.url.querySysDepartList);
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
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
      getAction(url,{departName:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.departName,
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

