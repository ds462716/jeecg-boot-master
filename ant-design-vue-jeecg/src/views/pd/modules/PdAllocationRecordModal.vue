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
      <div style="background:#ECECEC; padding:20px">
      <a-card style="margin-bottom: 10px;">
      <!-- 主表单区域 -->
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
          <a-col :span="12">
            <a-form-item label="调拨总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%;height: 60px"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
        </a-card>
      <!-- 子表单区域 -->
      <a-card style="margin-bottom: 10px;">
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="调拨明细表" :key="refKeys[0]" :forceRender="true">
          <div style="margin-bottom: 8px;">
            <a-button v-show="!disableSubmit" type="primary" icon="plus" @click="choice">选择产品</a-button>
            <span style="padding-left: 8px;"></span>
            <!--<a-button v-show="!disableSubmit" type="primary" icon="plus" @click="choicePackage">选择定数包</a-button>
            <span style="padding-left: 8px;"></span>-->
            <a-popconfirm
              :title="`确定要删除吗?`"
              @confirm="handleConfirmDelete">
              <a-button v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
              <span class="gap"></span>
            </a-popconfirm>
            <span style="padding-left: 8px;"></span>
            <!--<a-button type="primary" icon="download" @click="exportXls('调拨产品列表')">导出</a-button>-->
          </div>
          <j-editable-table
            bordered
            :ref="refKeys[0]"
            :loading="pdAllocationDetailTable.loading"
            :columns="pdAllocationDetailTable.columns"
            :dataSource="pdAllocationDetailTable.dataSource"
            :maxHeight="500"
            :rowNumber="true"
            :rowSelection="true"
            :disabled="disableSubmit"
            :actionButton="false"
            @valueChange="valueChange"
            style="text-overflow: ellipsis;"
          />
        </a-tab-pane>
      </a-tabs>
        </a-card>
        <a-card style="margin-bottom: 10px;" v-show="disableSubmit">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left">
                  <a-textarea :disabled="true" v-decorator="[ 'rejectReason', validatorRules.rejectReason]" placeholder="请输入审核意见"></a-textarea>
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
    <pd-allocation-detail-add-modal ref="PdAllocationDetailAddModal"   @ok="modalFormOk"></pd-allocation-detail-add-modal>
    <pd-allocation-package-add-modal ref="PdAllocationPackageAddModal"  @ok="modalFormInfoOk"></pd-allocation-package-add-modal>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import { httpAction,getAction,downFile,inArray} from '@/api/manage'
  import PdAllocationDetailAddModal from './PdProductStockListModel'
  import PdAllocationPackageAddModal from './PdChoosePackageListModel'

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: 'PdAllocationRecordModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,PdAllocationDetailAddModal,PdAllocationPackageAddModal
    },
    data() {
      return {
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        model:{},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        //出库科室下拉列表 start
        outDeptValue: undefined,
        notFoundContent:"未找到内容",
        outDeptData: [],
        //出库科室下拉列表 end
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
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
        // 调拨明细表
        pdAllocationDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '定数包编号', width:"130px",   key: 'packageCode' },
            { title: '定数包名称', width:"130px",   key: 'packageName' },
            { title: '定数包产品数量',  width:"130px", key: 'packageNum' },
            { title: '定数包Id', key: 'packageId', type: FormTypes.hidden },
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
        url: {
          add: "/pd/pdAllocationRecord/add",
          edit: "/pd/pdAllocationRecord/edit",
          exportXlsUrl: "/pd/pdAllocationRecord/exportXls",
          querySysDepartList:"/pd/pdDepart/getSysTwoDepartList",
          pdAllocationDetail: {
            list: '/pd/pdAllocationRecord/queryPdAllocationDetailList'
          },
        },
      }
    },
    methods: {
      add () { //初始化新增
        this.pdAllocationDetailTable.dataSource = [];
        this.edit({});
        this.allocationInfo();
      },


      allocationInfo() { //新增页面初始化
        getAction("/pd/pdAllocationRecord/allocationInfo",{}).then((res)=>{
          if (res.success) {
            let model={};
            this.model=res.result;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'allocationNo','allocationDate','totalNum','inDeptName','outDeptName','realName','remarks','rejectReason'))
            })
          }
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
        this.$refs.PdAllocationDetailAddModal.show({departId:outDeptId,code:"1"});
      },

      //选择定数包产品
      choicePackage() {
        this.$refs.PdAllocationPackageAddModal.show();
        this.$refs.PdAllocationPackageAddModal.title = "选择定数包";
      },

      handleConfirmDelete() {
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
// 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if(type === FormTypes.input){
            if(column.key === "allocationNum"){
              // 申领数量变更
              let rows = target.getValuesSync({ validate: false });
              target.setValues([{rowKey: row.id, values: {allocationNum :row.allocationNum }}]);
              // 计算总数量
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      // 计算总数量
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdAllocationDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalNum = 0;
          rows.forEach((item, idx) => {
            if (item.packageCode != '' && item.packageCode != null) {
              totalNum += parseFloat(item.allocationNum) * parseFloat(item.packageNum);
            } else {
              totalNum += parseFloat(item.allocationNum);
            }
          })
          this.model.totalNum = totalNum;
          this.form.setFieldsValue(pick(this.model, 'totalNum'))
          })

      },

      /* 导出 */
      exportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        if(this.pdAllocationDetailTable.dataSource.length>0){
          let param = {"allocationNo":this.model.allocationNo};
          console.log("导出参数",param)
          downFile(this.url.exportXlsUrl,param).then((data)=>{
            if (!data) {
              this.$message.warning("文件下载失败")
              return
            }
            if (typeof window.navigator.msSaveBlob !== 'undefined') {
              window.navigator.msSaveBlob(new Blob([data]), fileName+'.xls')
            }else{
              let url = window.URL.createObjectURL(new Blob([data]))
              let link = document.createElement('a')
              link.style.display = 'none'
              link.href = url
              link.setAttribute('download', fileName+'.xls')
              document.body.appendChild(link)
              link.click()
              document.body.removeChild(link); //下载完成移除元素
              window.URL.revokeObjectURL(url); //释放掉blob对象
            }
          })
        }else{
          this.$message.warning("产品为空")
          return
        }
      },
      modalFormOk (formData) { //选择产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdAllocationDetail.getValues((error, values) => {
          formData.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              let packageCode = value.packageCode;
              if (packageCode == "" ||  packageCode == null  ) {
                if(item.productId==value.productId){
                  bool=false;
                }
              }
            })
            if(bool){
              data.push(item);
            }
          })
          data.forEach((item, idx) => {
            this.pdAllocationDetailTable.dataSource = values;
            this.addrows(item);
          })
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice(values);
          })
        })
      },



      modalFormInfoOk (formData) { //选择定数包产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdAllocationDetail.getValues((error, values) => {
          formData.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              let packageCode = value.packageCode;
              if (packageCode != "" &&  packageCode != null  ) {
                if(item.productId==value.productId){
                  bool=false;
                }
              }
            })
            if(bool){
              data.push(item);
            }
          })
          data.forEach((item, idx) => {
            this.pdAllocationDetailTable.dataSource = values;
            this.addrows(item);
          })
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice(values);
          })
        })
      },

      addrows(row) {
        let data = {
          packageId:row.packageId,
          packageCode:row.code,
          packageName:row.name,
          packageNum:row.count,
          productId: row.productId,
          number: row.number,
          productName:row.productName,
          spec: row.spec,
          version: row.version,
          unitName: row.unitName,
          allocationNum: "1",//默认 1
          stockNum: row.stockNum,
          currentStockNum:row.currentStockNum
        }
        this.pdAllocationDetailTable.dataSource.push(data)
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
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },

      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'allocationNo','allocationDate','totalNum','inDeptName','outDeptId','outDeptName','realName','remarks','rejectReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
          //初始化出库科室
          this.sysDeptHandleSearch();
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { allocationNo: this.model.allocationNo }
          this.requestSubTableData(this.url.pdAllocationDetail.list, params, this.pdAllocationDetailTable)
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
       this.form.setFieldsValue(pick(row,'allocationNo','allocationDate','totalNum','inDeptName','outDeptName','realName','remarks','rejectReason'))
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
      },
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