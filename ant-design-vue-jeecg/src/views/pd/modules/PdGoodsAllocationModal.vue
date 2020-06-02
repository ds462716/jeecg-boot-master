<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :maskClosable=disableSubmit
    :visible="visible">

    <a-spin :spinning="confirmLoading" style="margin-bottom: 70px">
      <a-form :form="form">
        <a-form-item label="部门名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input disabled v-decorator="['departName', {} ]" autocomplete="off" placeholder="请输入机构/部门名称"/>
        </a-form-item>
        <a-form-item label="部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
          <a-input v-decorator="['departId', {} ]" v-show="false"/>
        </a-form-item>
        <a-form-item label="父ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
          <a-input v-decorator="['parentId', {} ]" v-show="false"/>
        </a-form-item>
        <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <template>
            <a-radio-group disabled v-decorator="['areaType',{}]" placeholder="类型">
              <a-radio value="1">
                货区
              </a-radio>
              <a-radio value="2" >
                货位
              </a-radio>
            </a-radio-group>
          </template>
        </a-form-item>
        <a-form-item :label="title2+'名称'" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="['name', validatorRules.name ]" @change="pinyinTran" placeholder="请输入名称"/>
        </a-form-item>
        <a-form-item label="编号标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input disabled v-decorator="['subCode', {} ]"  placeholder="编号标识" />
        </a-form-item>
        <a-form-item label="编号后缀" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="['codeSuffix', validatorRules.codeSuffix ]" @change="getCode" placeholder="请输入编号后缀" />
        </a-form-item>
        <a-form-item :label="title2+'编号'" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input disabled v-decorator="[ 'code', validatorRules.code]" placeholder="请输入编号"></a-input>
          <span style="color: red" v-show="showTips">注：修改货位编号会同时修改库存货位编号</span>
        </a-form-item>
        <a-form-item label="存放数量" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="showSubNum">
          <a-input-number :disabled="disableSubmit" v-decorator="[ 'subNum', validatorRules.subNum]" placeholder="存放数量" :min="0" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址"></a-input>
        </a-form-item>

        <a-form-item label="面积（㎡）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'area', validatorRules.area]" placeholder="请输入面积" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="容积（m³）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'volume', validatorRules.volume]" placeholder="请输入容积" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'contacts', validatorRules.contacts]" placeholder="请输入联系人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'contactsPhone', validatorRules.contactsPhone]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <template>
            <a-radio-group :disabled="disableSubmit" v-decorator="['state',validatorRules.state]" placeholder="状态">
              <a-radio value="0">
                未启用
              </a-radio>
              <a-radio value="1">
                启用
              </a-radio>
            </a-radio-group>
          </template>
        </a-form-item>
        <a-form-item label="拼音码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音码"></a-input>
        </a-form-item>
        <a-form-item label="五笔码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔码"></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" >
      <a-button type="default" @click="handleCancel" v-show="disableSubmit">关闭</a-button>
      <a-button type="primary" @click="handleOk" :loading="confirmLoading" v-show="!disableSubmit">确定</a-button>
      <!--<a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消" v-show="!disableSubmit">-->
        <a-button type="default" @click="handleCancel" v-show="!disableSubmit">取消</a-button>
      <!--</a-popconfirm>-->
    </div>
  </a-drawer>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { makeWb } from '@/utils/wubi'
  import { duplicateCheckHasDelFlag } from '@/api/api'

  export default {
    name: "PdGoodsAllocationModal",
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,

        title2:"",
        showSubNum:false,
        goodsCode:"", // 货区货位编码
        disableSubmit:false,
        showTips:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          departName: {rules: [{required: true, message: '请输入机构/部门名称!'}]},
          subCode: {rules: [{required: true, message: '下级存放单位编号标识!'}]},
          // orgCategory: {rules: [{required: true, message: '请输入机构类型!'}]},
          mobile: {rules: [{validator: this.validateMobile}]},
          name: {rules: [{required: true, message: '请输入名称!'}]},
          code: {rules: [{required: true, message: '请输入编号!'}]},
          codeSuffix: {rules: [{required: true, message: '请输入编号后缀!'},{validator: this.validateCode}]},
          areaType: {rules: []},
          address: {rules: []},
          area: {rules: [{pattern:/^-?\d+\.?\d*$/, message: '请输入数字!'},]},
          volume: {rules: [{pattern:/^-?\d+\.?\d*$/, message: '请输入数字!'},]},
          contacts: {rules: []},
          contactsPhone: {rules: [{pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},]},
          subNum: {rules: [{required: true, message: '请输入整数!'},{pattern:/^-?\d+$/, message: '请输入整数!'},]},
          state: {rules: [{required: true, message: '请输选择状态!'}]},
          py: {rules: []},
          wb: {rules: []},
          zdy: {rules: []},
          remarks: {rules: []},
        },
        url: {
          add: "/pd/pdGoodsAllocation/add",
          edit: "/pd/pdGoodsAllocation/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add (param) {
        this.edit(param);
      },
      edit (record) {
        this.form.resetFields();
        if(!record.state){
          record.state = "1";
        }
        if(record.areaType == "1"){
          this.showSubNum = false;
        }else if(record.areaType == "2"){
          this.showSubNum = true;
        }
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'departId', 'parentId', 'departName','name','code','areaType','address','area','volume','contacts','contactsPhone','subCode','codeSuffix','subNum','state','py','wb','zdy','remarks'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      pinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      getCode(e){
        this.showTips = false;
        if(this.model.id){
          this.showTips = true;
        }
        let codeSuffix = e.target.value;
        this.goodsCode = this.form.getFieldValue("subCode")+codeSuffix;
        this.form.setFieldsValue({code:this.goodsCode});
      },
      validateCode(rule, value, callback){
        let dataId = ""
        if(this.model.id){
          dataId = this.model.id;
        }
        let params = {
          tableName: 'pd_goods_allocation',
          fieldName: 'code',
          fieldVal: this.goodsCode,
          dataId: dataId
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback();
          } else {
            callback("货位编号已存在，请重新输入编号后缀!");
          }
        })
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              this.$emit('ok', this.model.departId);
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'departId', 'parentId', 'departName','name','code','areaType','address','area','volume','contacts','contactsPhone','subCode','codeSuffix','subNum','state','py','wb','zdy','remarks'))
      }

    }
  }
</script>

<style lang="less" scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    /*margin-bottom: 30px;*/
    float: right;
  }
  .drawer-bootom-button {
     position: absolute;
     bottom: 0px;
     width: 100%;
     border-top: 1px solid #e8e8e8;
     padding: 10px 16px;
     text-align: right;
     left: 0;
     background: #fff;
     border-radius: 0 0 2px 2px;
     z-index:199;
   }
</style>