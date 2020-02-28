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
        <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-radio-group @change="onChangeMenuType" v-decorator="['type',{'initialValue':localCategoryType}]">
            <a-radio :value="0">一级分类</a-radio>
            <a-radio :value="1">二级分类</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          :label="menuLabel"
           >
          <a-input placeholder="请输入名称" ref="inputFocus" @change="pinyinTran"  v-decorator="[ 'name', validatorRules.name]"/>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔简码"></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
        </a-form-item>


        <a-form-item
          v-show="localCategoryType!=0"
          label="一级分类"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          :validate-status="validateStatus"
          :hasFeedback="true"
          :required="true">
          <span slot="help">{{ validateStatus=='error'?'请选择一级分类':'&nbsp;&nbsp;' }}</span>
          <a-tree-select
            style="width:100%"
            :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
            :treeData="treeData"
            v-model="model.parentId"
            placeholder="请选择一级分类"
            :disabled="disableSubmit"
            @change="handleParentIdChange">
          </a-tree-select>
        </a-form-item>

        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button type="primary" :loading="confirmLoading" @click="handleOk">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>

  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryCategoryTreeList} from '@/api/api'
  import { makeWb } from '@/utils/wubi'
  
  export default {
    name: "PdCategoryModal",
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
        localCategoryType:0,
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
          name: {rules: [
            {required: true, message: '请输入名称!'},
            /*{ validator: (rule, value, callback) => validateDuplicateValue('pd_category', 'name', value, this.model.id, callback)},*/
          ]},
          type: {rules: [
            {required: true, message: '请输入类型!'},
          ]},
          remarks: {rules: [
          ]},
        },
        url: {
          add: "/pd/pdCategory/add",
          edit: "/pd/pdCategory/edit",
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
          this.localCategoryType = 1;
        }else{
          this.localCategoryType = 0;
        }
        this.visible = true;
        this.loadTree();
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','type','remarks'));
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
            if ((formData.type == 1) && !formData.parentId) {
              that.validateStatus = 'error';
              that.$message.error("请检查你填的分类是否正确！");
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
        this.form.setFieldsValue(pick(row,'name','type','createTime','updateTime','remarks'))
      },
      onChangeMenuType(e) {
        this.localCategoryType=e.target.value
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
        queryCategoryTreeList().then((res)=>{
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