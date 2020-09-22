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
        <!-- 主表单区域 -->
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="8">
                <a-form-item label="盘点科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    disabled
                    placeholder="请选择盘点科室"
                    :departId="deptValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    @search="sysDeptHandleSearch"
                    @change="sysDeptHandleChange"
                    @focus="sysDeptHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'targetDepartId', validatorRules.targetDepartId]"
                  >
                    <a-select-option v-for="d in deptData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="8">
                <a-form-item label="盘点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'checkNo', validatorRules.checkNo]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="盘点日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled="disabled" v-decorator="[ 'checkDate', validatorRules.checkDate]"
                          :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="盘点人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'checkName', validatorRules.checkName]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="盘点产品量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'shouldCount', validatorRules.shouldCount]"
                                  style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="已盘产品量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'checkCount', validatorRules.checkCount]"
                                  style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="盘盈盘亏数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'profitLossCount', validatorRules.profitLossCount]"
                                  style="width: 100%"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="24">
                <a-form-item label="备注" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                  <a-textarea disabled="disabled" autocomplete="off"
                              v-decorator="[ 'remarks', validatorRules.remarks]"></a-textarea>
                </a-form-item>
              </a-col>
              <a-form-item v-show="false" label="盘点人">
                <a-input v-show="false" v-decorator="[ 'checkBy', {}]"></a-input>
              </a-form-item>
            </a-row>
            <!--<a-col :span="24">-->
              <!--<a-form-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                <!--<a-textarea :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]"-->
                            <!--placeholder="请输入审批意见"></a-textarea>-->
              <!--</a-form-item>-->
            <!--</a-col>-->
          </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-col :span="24">
              <a-form-item label="审批意见" :labelCol="labelCol3" :wrapperCol="wrapperCol3" >
                <a-textarea :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]"
                            placeholder="请输入审批意见"></a-textarea>
              </a-form-item>
            </a-col>
          </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <!-- 子表单区域 -->
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="盘点明细表" :key="refKeys[0]" :forceRender="true">
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdProductStockCheckTable.loading"
                :columns="pdProductStockCheckTable.columns"
                :dataSource="pdProductStockCheckTable.dataSource"
                :maxHeight="300"
                :rowNumber="true"
                disabled
                :rowSelection="true"
                :actionButton="false"
                style="text-overflow: ellipsis;">
              </j-editable-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关 闭</a-button>
      <a-button @click="printBtn()" style="margin-right: 15px;" type="primary" v-show="disableSubmit">打 印</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取 消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定驳回？" @confirm="refuseBtn" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳回</a-button>
      </a-popconfirm>
      <a-button @click="submitBtn('1')" v-show="!disableSubmit" type="primary" :loading="confirmLoading"
                style="margin-right: 15px;">审核通过
      </a-button>
      <a-button @click="submitBtn('2')" v-show="!disableSubmit" type="primary" :loading="confirmLoading"
                style="margin-right: 15px;">审核通过并打印
      </a-button>
    </template>

    <pd-product-stock-check-print-modal ref="PdProductStockCheckPrintModal"></pd-product-stock-check-print-modal>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import {FormTypes, getRefPromise, validateFormAndTables} from '@/utils/JEditableTableUtil'
  import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
  import {validateDuplicateValue, isRealNum} from '@/utils/util'
  import {httpAction, getAction, downFile, inArray} from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import PdProductStockCheckPrintModal from '../print/PdProductStockCheckPrintModal'

  const VALIDATE_NO_PASSED = Symbol()
  export {FormTypes, VALIDATE_NO_PASSED}
  export default {
    name: 'PdProductStockCheckExamineModal',
    mixins: [JEditableTableMixin],
    components: {
      PdProductStockCheckPrintModal,
      JDate
    },
    data() {
      return {
        model: {},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit: false,
        showCancelBtn: false,
        showPrintBtn: false,
        showRefuseReason: false,
        labelCol: {xs: {span: 24}, sm: {span: 6},},
        wrapperCol: {xs: {span: 24}, sm: {span: 16},},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        labelCol3: {span: 2},
        wrapperCol3: {span: 20},
        //盘点科室下拉列表 start
        deptValue: undefined,
        notFoundContent: "未找到内容",
        deptData: [],
        //盘点科室下拉列表 end
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          checkNo: {rules: []},
          departId: {
            rules: [
              {required: true, message: '请选择盘点科室!'},
            ]
          },
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
        refKeys: ['pdProductStockCheck',],
        tableKeys: ['pdProductStockCheck',],
        activeKey: 'pdProductStockCheck',
        // 盘点明细表
        pdProductStockCheckTable: {
          loading: false,
          dataSource: [],
          columns: [
            {title: '库存明细ID', key: 'stockId', type: FormTypes.hidden},
            {title: '产品id', key: 'productId', type: FormTypes.hidden},
            {title: '产品名称', key: 'productName', width: "200px"},
            {title: '产品编号', key: 'number', width: "200px"},
            {title: '产品规格', key: 'spec', width: "200px"},
            {title: '产品批号', key: 'batchNo', width: "200px"},
            {title: '产品有效期', key: 'expDate', width: "100px"},
            {title: '产品单位', key: 'unitName', width: "100px"},
            {title: '理论数量', key: 'stockNum', width: "120px"},
            {title: '盘点数量', key: 'checkNum', width: "120px"},
            {title: '盘盈盘亏', key: 'profitLossCount', width: "100px"}
          ]
        },
        url: {
          init: "/pd/pdProductStockCheck/initModal",
          add: "/pd/pdProductStockCheck/add",
          edit: "/pd/pdProductStockCheck/edit",
          querySysDepartList: "/pd/pdDepart/getSysDepartList",
          pdProductStockCheckChild: {
            list: '/pd/pdProductStockCheck/queryPdProductStockCheckChild'
          },
          queryById: "/pd/pdProductStockCheck/queryById",
          audit: "/pd/pdProductStockCheck/audit",
        },
      }
    },
    methods: {
      //-----------------盘点科室查询start
      sysDeptHandleSearch(value) {
        fetch(value, data => (this.deptData = data), this.url.querySysDepartList);
      },

      sysDeptHandleChange(value) {
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.deptValue = value;
        fetch(value, data => (this.deptData = data), this.url.querySysDepartList);
        //根据选择的盘点科室查询到该科室下需盘点产品总数
        getAction("/pd/pdProductStockCheck/queryCheckTotalNum", {departId: this.deptValue}).then((res) => {
          if (res.success) {
            this.model.shouldCount = res.result;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'shouldCount'))
              this.pdProductStockCheckTable.dataSource = [];
            })
          }
        })
      },
      //-----------------盘点科室查询end

      /** 拒绝 **/
      refuseBtn() {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if (!refuseReason) {
          this.$message.warning("请填写审批意见！");
          return;
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "3"  // 拒绝
          }
        this.request(params);
      },
      /** 确定按钮点击事件 */
      submitBtn(flag) {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if (!refuseReason) {
          refuseReason = "同意";
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "2" // 通过
          }
        this.request(params, flag);
      },
      // 保存 提交 修改 请求函数
      request(params, flag) {
        this.confirmLoading = true
        httpAction(this.url.audit, params, "post").then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok');
            this.close();
            if (flag == "2") {
              this.printBtn(); //通过并打印
            }
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      },

      checkInfo() { //新增页面初始化
        getAction("/pd/pdProductStockCheck/checkInfo", {}).then((res) => {
          if (res.success) {
            this.model = res.result;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'checkNo', 'deptName', 'checkDate', 'checkName', 'shouldCount', 'checkCount', 'profitLossCount', 'remarks'))
            })
          }
        })
      },

      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData() {
        this.showCancelBtn = false;
        this.showPrintBtn = false;
        this.showRefuseReason = false;
        this.$nextTick(() => {
          //初始化盘点科室
          this.sysDeptHandleSearch();
        })
        let params = {};
        // 加载子表数据
        if (this.model.id) {
          if (this.model.auditStatus == "1" && this.model.submitStatus == "2") {
            this.showCancelBtn = true;
          }
          if (this.model.submitStatus == "2") { // 提交完可打印
            this.showPrintBtn = true;
          }
          if (this.model.auditStatus == "2" || this.model.auditStatus == "3") {
            this.showRefuseReason = true;
          }
          let fieldval = pick(this.model, 'checkNo', 'targetDepartId', 'checkDate', 'checkName', 'checkBy', 'refuseReason', 'shouldCount', 'checkCount', 'profitLossCount', 'remarks');
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = {id: this.model.id}
        } else {
          params = {id: ""}
        }
        this.pdProductStockCheckTable.loading = true;
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if (this.model.id) { //详情页
                this.pdProductStockCheckTable.dataSource = res.result.pdProductStockCheckChildList || [];
              } else {
                this.initData = res.result;
                let fieldval = pick(this.initData, 'checkNo', 'targetDepartId', 'checkDate', 'checkName', 'checkBy', 'refuseReason', 'shouldCount', 'checkCount', 'profitLossCount', 'remarks');
                this.form.setFieldsValue(fieldval);
              }
            })
          }
          this.pdProductStockCheckTable.loading = false;
        })
      },

      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'checkNo', 'deptName', 'checkDate', 'checkName', 'shouldCount', 'checkCount', 'profitLossCount', 'remarks'))
      },
      /** 关闭按钮 **/
      closeBtn() {
        this.visible = false;
        this.$emit('close');
        this.disableSubmit = false;
      },
      /**打印按钮**/
      printBtn() {
        let recordId = this.model.id;
        if (!recordId) {
          this.$message.warning("参数不正确，请重新打印！");
          return;
        }
        getAction(this.url.queryById, {id: this.model.id}).then((res) => {
          this.$refs.PdProductStockCheckPrintModal.show(res.result);
          this.$refs.PdProductStockCheckPrintModal.title = "盘点单";
        })
      },

    }
  }


  let timeout;
  let currentValue;

  function fetch(value, callback, url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url, {departName: value, parentFlag: "0"}).then((res) => {
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({value: r.id, text: r.departName});
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
    margin-top: 10px;
  }

  .tableStyle > tr > th {
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

  .tableStyle > tr > td {
    border: 1px solid #e8e8e8;
    text-align: center;
    padding: 1px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

  .tableStyle > tr > td > input {
    width: 45px;
    text-align: center;
  }
</style>