<template>
  <a-drawer
    :title="title"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    @close="close"
    :visible="visible"
    :maskClosable=disableSubmit
    :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          v-if="localDepartType!=0"
          v-show="false"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="类型">
          <a-input  placeholder="请输入类型"  v-decorator="['orgType', {'initialValue':2}]"/>
        </a-form-item>

        <a-form-item
          v-else
          v-show="false"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="类型">
          <a-input  placeholder="请输入类型" v-decorator="['orgType',  {'initialValue':1}]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          :label="menuLabel"
           >
          <a-input placeholder="请输入名称" :disabled="disableSubmit" autocomplete="off" ref="inputFocus" @change="pinyinTran"  v-decorator="[ 'departName', validatorRules.departName]"/>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔简码"></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="机构编码">
          <a-input disabled placeholder="请输入机构编码" v-decorator="['orgCode']"/>
        </a-form-item>

        <a-form-item
          v-show="localDepartType!=0"
          label="上级部门"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          :validate-status="validateStatus"
          :disabled="disableSubmit"
          :hasFeedback="true"
          :required="true">
          <span slot="help">{{ validateStatus=='error'?'请选择上级部门':'&nbsp;&nbsp;' }}</span>
          <a-tree-select
            style="width:100%"
            :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
            :treeData="treeData"
            v-model="model.parentId"
            placeholder="请选择上级部门"
            :disabled="disableSubmit"
            @change="handleParentIdChange">
          </a-tree-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="电话">
          <a-input :disabled="disableSubmit" autocomplete="off" placeholder="请输入电话" v-decorator="['mobile',validatorRules.mobile]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="传真">
          <a-input :disabled="disableSubmit" autocomplete="off" placeholder="请输入传真" v-decorator="['fax', {}]"  />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="地址">
          <a-input :disabled="disableSubmit" autocomplete="off" placeholder="请输入地址" v-decorator="['address', {}]"  />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序">
          <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'departOrder',{'initialValue':0}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-textarea :disabled="disableSubmit" autocomplete="off" placeholder="请输入备注" v-decorator="['memo', {}]"  />
        </a-form-item>

        <a-form-item>

        </a-form-item>

      </a-form>
    </a-spin>

    <div class="drawer-bootom-button">
      <a-button @click="close" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button @click="handleOk"  v-show="!disableSubmit" type="primary" style="margin-right: 15px;" :loading="confirmLoading">提  交</a-button>
      <a-popconfirm title="确定放弃编辑？" v-show="!disableSubmit" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
    </div>

  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryPdDepartTreeList} from '@/api/api'
  import { makeWb } from '@/utils/wubi'
  
  export default {
    name: "PdDepartModal",
    components: { 
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        drawerWidth:800,
        visible: false,
        model: {},
        localDepartType:0,
        show:true,//根据菜单类型，动态显示隐藏表单元素
        treeData:[],
        validateStatus:"",
        disableSubmit:false,
        menuLabel:'名称',
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
          orgCode: {rules: [{required: true, message: '请输入机构编码!'}]},
          orgType: {rules: [{required: true, message: '请输入机构类型!'}]},
          mobile: {rules: [{validator: this.validateMobile}]}
        },
        url: {
          add: "/sys/sysDepart/add",
          edit: '/sys/sysDepart/edit',
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        this.form.resetFields();
        this.model = Object.assign({}, record);
        if(this.model.parentId){
          this.localDepartType = 1;
        }else{
          this.localDepartType = 0;
        }
        this.visible = true;
        this.loadTree();
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'departName','py','wb','zdy','departOrder','orgType','orgCode','mobile','fax','address','memo'));
          //获取光标
          let input = this.$refs['inputFocus'];
          input.focus()
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            if ((formData.orgType == 2) && !formData.parentId) {
              that.validateStatus = 'error';
              that.$message.error("请检查你填的部门是否正确！");
              return;
            } else {
              that.validateStatus = 'success';
            }
            that.confirmLoading = true;
            //console.log("表单提交数据",formData)
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
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'departName','py','wb','zdy','departOrder','orgType','orgCode','mobile','fax','address','memo'))
      },
      onChangeMenuType(e) {
        this.localDepartType=e.target.value
        this.show = true;
      },
      handleParentIdChange(value){
        if(!value){
          this.validateStatus="error"
        }else{
          this.validateStatus="success"
        }
      },
      loadTree(){
        let that = this;
        queryPdDepartTreeList().then((res)=>{
          if(res.success){
            that.treeData = [];
            let treeList = res.result.treeList
            for(let a=0;a<treeList.length;a++){
              let temp = treeList[a];
              temp.isLeaf = temp.leaf;
              that.treeData.push(temp);
            }
          }
        });
      },

      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize(){
        let screenWidth = document.body.clientWidth;
        if(screenWidth < 500){
          this.drawerWidth = screenWidth;
        }else{
          this.drawerWidth = 700;
        }
      },
      pinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        //let py = pinyin.getFullChars(val);//获取全拼
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      validateMobile(rule,value,callback){
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)){
          callback();
        }else{
          callback("您的手机号码格式不正确!");
        }

      }
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
.drawer-bootom-button {
  position: absolute;
  /*top:95%;*/
  bottom: -30px;
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