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
                    placeholder="请选择盘点科室"
                    :disabled="disableSubmit"
                    :targetDepartId="deptValue"
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
                  <a-textarea :disabled="disableSubmit" autocomplete="off"
                              v-decorator="[ 'remarks', validatorRules.remarks]"></a-textarea>
                </a-form-item>
              </a-col>
              <a-form-item v-show="false" label="盘点人">
                <a-input v-show="false" v-decorator="[ 'checkBy', {}]"></a-input>
              </a-form-item>
            </a-row>
          </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;" v-show="showRefuseReason">
          <a-form :form="form">
            <a-col :span="24">
              <a-form-item label="审批意见" :labelCol="labelCol3" :wrapperCol="wrapperCol3" style="text-align: left">
                <a-textarea disabled v-decorator="[ 'refuseReason', validatorRules.refuseReason]"
                            placeholder="请输入审批意见"></a-textarea>
              </a-form-item>
            </a-col>
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
                bordered
                :ref="refKeys[0]"
                :loading="pdProductStockCheckTable.loading"
                :columns="pdProductStockCheckTable.columns"
                :dataSource="pdProductStockCheckTable.dataSource"
                :maxHeight="300"
                :rowNumber="true"
                :disabled="disableSubmit"
                :rowSelection="true"
                :actionButton="false"
                @valueChange="valueChange"
                style="text-overflow: ellipsis;">
              </j-editable-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关 闭</a-button>
      <a-popconfirm title="确定放弃盘点吗？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取 消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定撤回？" @confirm="cancelBtn" v-show="showCancelBtn" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;" :loading="confirmLoading" type="danger">撤 回</a-button>
      </a-popconfirm>
      <a-button @click="printBtn()" style="margin-right: 15px;" type="primary" v-show="disableSubmit">打 印</a-button>
      <a-button @click="submitBtn('submit')" v-show="!disableSubmit" type="primary" :loading="confirmLoading"
                style="margin-right: 15px;">盘点完成
      </a-button>
      <a-button @click="saveBtn('save')" v-show="!disableSubmit" type="primary" :loading="confirmLoading"
                style="margin-right: 15px;">保存草稿
      </a-button>
      <a-button @click="saveBtn('saveAndPrint')" v-show="!disableSubmit" type="primary" :loading="confirmLoading"
                style="margin-right: 15px;">保存草稿并打印
      </a-button>
    </template>


    <pd-product-stock-check-add-modal ref="PdProductStockCheckAddModal"
                                      @ok="modalFormOk"></pd-product-stock-check-add-modal>
    <pd-product-stock-check-print-modal ref="PdProductStockCheckPrintModal"></pd-product-stock-check-print-modal>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import {FormTypes, getRefPromise, validateFormAndTables} from '@/utils/JEditableTableUtil'
  import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
  import {validateDuplicateValue, isRealNum} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction, downFile, inArray} from '@/api/manage'
  import PdProductStockCheckAddModal from './PdCheckProductListModel'
  import PdProductStockCheckPrintModal from '../print/PdProductStockCheckPrintModal'

  const VALIDATE_NO_PASSED = Symbol()
  export {FormTypes, VALIDATE_NO_PASSED}
  export default {
    name: 'PdProductStockCheckModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate, PdProductStockCheckAddModal,
      PdProductStockCheckPrintModal
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
          targetDepartId: {
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
            {title: '产品名称', key: 'productName', width: "400px"},
            {title: '产品编号', key: 'number', width: "200px"},
            {title: '产品规格', key: 'spec', width: "200px"},
            {title: '产品批号', key: 'batchNo', width: "200px"},
            {title: '产品有效期', key: 'expDate', width: "100px"},
            {title: '产品单位', key: 'unitName', width: "100px"},
            {title: '理论数量', key: 'stockNum', width: "120px"},
            {
              title: '盘点数量', key: 'checkNum', type: FormTypes.input, width: "120px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [/*{ required: true, message: '${title}不能为空' },*/
                {pattern: '^(?:[0-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$', message: '${title}的格式不正确'}]
            },
            {title: '盘盈盘亏', key: 'profitLossCount', width: "100px"}
          ]
        },
        url: {
          init: "/pd/pdProductStockCheck/initModal",
          submit: "/pd/pdProductStockCheck/submit",
          add: "/pd/pdProductStockCheck/add",
          edit: "/pd/pdProductStockCheck/edit",
          querySysDepartList: "/pd/pdDepart/queryListTree",
          pdProductStockCheckChild: {
            list: '/pd/pdProductStockCheck/queryPdProductStockCheckChild'
          },
          queryById: "/pd/pdProductStockCheck/queryById",
          cancel: "/pd/pdProductStockCheck/cancel",
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
        getAction("/pd/pdProductStockCheck/queryCheckTotalNum", {targetDepartId: this.deptValue}).then((res) => {
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
      //选择产品
      choice() {
        //判断是否选择了盘点科室
        let targetDepartId = this.form.getFieldValue("targetDepartId");
        if (targetDepartId == null || targetDepartId == "") {
          this.$message.warning("请先选择盘点科室")
          return
        }
        this.$refs.PdProductStockCheckAddModal.show({departId: targetDepartId});
      },

      handleConfirmDelete() { //删除产品
        if (this.$refs.pdProductStockCheck.selectedRowIds.length > 0) {
          this.$refs.pdProductStockCheck.removeSelectedRows();
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNumAndPrice();
          })
        } else {
          this.$message.error("请选择需要删除的数据！")
        }
      },

      modalFormOk(formData) { //选择产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdProductStockCheck.getValues((error, values) => {
          formData.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              if (item.id == value.stockId) {
                bool = false;
              }
            })
            if (bool) {
              data.push(item);
            } else {
              this.$message.warn("不可重复选择！")
              return;
            }
          })
          data.forEach((item, idx) => {
            this.pdProductStockCheckTable.dataSource = values;
            this.addRows(item);
          })
        })
      },

      addRows(row) {
        let data = {
          stockId: row.id,
          productId: row.productId,
          number: row.number,
          productName: row.productName,
          spec: row.spec,
          ///checkNum:null,//默认null
          version: row.version,
          unitName: row.unitName,
          batchNo: row.batchNo,
          expDate: row.expDate,
          stockNum: row.stockNum,
          profitLossCount: null
        }
        this.pdProductStockCheckTable.dataSource.push(data)
        this.$refs.pdProductStockCheck.add();
      },


      // 表格数据变更
      valueChange(event) {
        if (event) {
          const {type, row, column, value, target} = event;
          if (type === FormTypes.input) {
            if (column.key === "checkNum") {
              // 盘点数量变更
              let profitCount = parseFloat(row.checkNum) - parseFloat(row.stockNum);
              if (isRealNum(profitCount)) {
                target.setValues([{
                  rowKey: row.id,
                  values: {checkNum: row.checkNum, profitLossCount: profitCount.toFixed(2)}
                }]);
              } else {
                target.setValues([{rowKey: row.id, values: {checkNum: row.checkNum, profitLossCount: ""}}]);
              }
              // 计算盘盈盘亏数量
              this.getTotalNumAndPrice();
            }
          }
        }
      },
      // 计算盘盈盘亏数量等
      getTotalNumAndPrice() {
        this.$nextTick(() => {
          let {values} = this.$refs.pdProductStockCheck.getValuesSync({validate: false});
          let profitLossCount = 0;//盘盈盘亏
          let checkCount = 0;//已盘产品数量
          values.forEach((item, idx) => {
            if (item.checkNum != null && item.checkNum != '') {
              checkCount += parseFloat(item.stockNum);
              if (isRealNum(item.profitLossCount)) {
                profitLossCount += parseFloat(item.profitLossCount);
              } else {
                profitLossCount = 0;
              }
            }
          })
          this.model.profitLossCount = profitLossCount.toFixed(2);
          this.model.checkCount = checkCount.toFixed(2);
          this.form.setFieldsValue(pick(this.model, 'profitLossCount', 'checkCount'))
        })
      },

      /** 保存草稿 **/
      saveBtn(flag) {
        this.request(this.url.add, "post", flag);
      },
      /** 提交 **/
      submitBtn(flag) {
        this.request(this.url.submit, "post", flag);
      },
      request(url, method, flag) {
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
          let pdProductStockCheckChildList = formData.pdProductStockCheckChildList;
          if (pdProductStockCheckChildList.length > 0) {
            //临时保存不校验，盘点完成才校验
            if (flag == "submit") {
              let bo = true;
              let index = 1;
              for (let item of pdProductStockCheckChildList) {
                if (!item.checkNum) {
                  bo = false;
                  break;
                }
                index++
              }
              if (!bo) {
                this.$message.error("第" + index + "行数量填写错误请检查！");
                return bo;
              }
            }
            this.confirmLoading = true;
            httpAction(url, formData, method).then((res) => {
              if (res.success) {
                this.model.id = res.result.recordId;
                that.$message.success(res.message);
                that.$emit('ok');
                that.close();
                //临时保存并打印
                if (flag == "saveAndPrint") {
                  this.printBtn(); //通过并打印
                }
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          } else {
            that.$message.error("请选择需要盘点的产品");
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

      add() {//初始化新增
        this.pdProductStockCheckTable.dataSource = [];
        this.edit({});
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
          if (this.model.auditStatus == "1" && this.model.checkStatus == "2") {
            this.showCancelBtn = true;
          }
          if (this.model.checkStatus == "2") { // 提交完可打印
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
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdProductStockCheckChildList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg) {
        this.$message.error(msg)
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
      /**撤回**/
      cancelBtn() {
        if (this.model.auditStatus == "1" && this.model.checkStatus == "2") {
          this.confirmLoading = true
          httpAction(this.url.cancel, {id: this.model.id}, 'put').then((res) => {
            if (res.success) {
              this.$message.success(res.message)
              this.$emit('ok')
              this.close()
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.confirmLoading = false
          })
        } else {
          this.$message.warning("当前盘点单状态已被审批或已撤回，不能撤回！"); //当前盘点单状态非已提交、待审核状态，不能撤回！
        }
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