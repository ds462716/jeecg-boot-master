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
        <a-form-item label="检验仪器代号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="true" autocomplete="off" v-decorator="[ 'instrCode', validatorRules.instrCode]" ></a-input>
        </a-form-item>
        <a-form-item label="检验仪器名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="true" autocomplete="off" v-decorator="[ 'instrName', validatorRules.instrName]" ></a-input>
        </a-form-item>
        <a-form-item label="所属科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="true" autocomplete="off" v-decorator="[ 'departName', validatorRules.departName]" ></a-input>
        </a-form-item>
        <!--<a-form-item label="关联科室名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'departName', validatorRules.departName]" ></a-input>
        </a-form-item>-->
        <a-form-item
          label="关联实验室名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          :validate-status="validateStatus"
          :hasFeedback="true"
          :required="true">
           <a-tree-select
            style="width:100%"
            :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
            :treeData="treeData"
            v-model="model.testDepartId"
            placeholder="请选择关联实验室"
            :disabled="disableSubmit"
           >
          </a-tree-select>
        </a-form-item>



      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" >
      <a-button @click="close"  style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button v-show="!disableSubmit" type="primary" :loading="confirmLoading" @click="handleOk">确定</a-button>
      <a-button v-show="!disableSubmit" @click="handleCancel">取消</a-button>
    </div>

  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryPdDepartTreeList} from '@/api/api'

  export default {
    name: "ExLabInstrInfModal",
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
          departName: {rules: [
              {required: true, message: '请选择关联实验室!'},
            ]},
          fsfKsbh:{},
          fsfKsmc:{},
          fsfKsjm:{},
        },
        url: {
          edit: "/ex/exLabInstrInf/edit",
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
        this.visible = true;
        this.loadTree();
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'departName','instrCode','instrName','testDepartName'));
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
            let formData = Object.assign(this.model, values);
            that.confirmLoading = true;
            if ((formData.testDepartId == '') ||  formData.testDepartId==null) {
               that.$message.error("请选择关联实验室！");
              return;
            }
            httpAction(this.url.edit,formData,"put").then((res)=>{
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
        this.form.setFieldsValue(pick(row,'departName','instrCode','instrName','testDepartName'))
      },
      onChangeMenuType(e) {
        this.localCategoryType=e.target.value
        this.show = true;
      },
    /*  handleParentIdChange(value){
        if(!value){
          this.validateStatus="error"
        }else{
          this.validateStatus="success"
        }
      },*/
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