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
          <a-input   :disabled="!disableSubmit" autocomplete="off" v-decorator="[ 'instrCode', validatorRules.instrCode]" ></a-input>
        </a-form-item>
        <a-form-item label="检验仪器名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input  :disabled="!disableSubmit" autocomplete="off" v-decorator="[ 'instrName', validatorRules.instrName]" ></a-input>
        </a-form-item>
        <a-form-item label="所属科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--
          <a-input   :disabled="!disableSubmit" autocomplete="off" v-decorator="[ 'departName', validatorRules.departName]" ></a-input>
-->
              <a-select
                showSearch
                placeholder="请选择科室"
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :showArrow="true"
                :filterOption="false"
                :allowClear="true"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :disabled="!disableSubmit"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'departId', validatorRules.departId]"
              >
                <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
        </a-form-item>
          <a-form-item label="关联实验室名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select
              mode="multiple"
              showSearch
              :testDepartId="testDepartValue"
              :defaultActiveFirstOption="false"
              :allowClear="true"
              :showArrow="true"
              :filterOption="false"
              @search="departHandleSearch"
              @focus="departHandleSearch"
              :disabled="!disableSubmit"
              :notFoundContent="notFoundContent"
              v-decorator="[ 'departIdList', validatorRules.departIdList]"
              placeholder="请选择科室"
            >
              <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
            </a-select>
          </a-form-item>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" >
      <a-button v-show="disableSubmit" @click="close"  style="margin-right: 15px;" >关 闭</a-button>
      <a-button v-show="disableSubmit" type="primary" :loading="confirmLoading" @click="handleOk">确 定</a-button>
      <a-button v-show="disableSubmit" @click="handleCancel">取 消</a-button>
    </div>

  </a-drawer>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryPdDepartTreeList} from '@/api/api'

  export default {
    name: "ExLabInstrInfModal",
    components: {
      JDictSelectTag
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
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        testDepartValue: undefined,
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
          /*departName: {rules: [
              {required: true, message: '请选择关联实验室!'},
            ]},*/
          departId: { rules: [{ required: true, message: '请选择科室!' }] },
          departIdList: { rules: [{ required: true, message: '请选择科室!' }] },
          fsfKsbh:{},
          fsfKsmc:{},
          fsfKsjm:{},
        },
        url: {
          add: "/ex/exLabInstrInf/add",
          edit: "/ex/exLabInstrInf/edit",
          queryDepart: "/pd/pdDepart/queryListTree",
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
        this.departHandleSearch();
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'departName','departId','instrCode','instrName','testDepartName'));
          let testDepartId=this.model.testDepartId;
          if(testDepartId != null && testDepartId != ""){
            let departIds = this.model.testDepartId.split(",");
            this.form.setFieldsValue({departIdList:departIds});
          }

          //获取光标
          /*let input = this.$refs['inputFocus'];
          input.focus()*/
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
            formData.testDepartId=this.model.departIdList.join(",");
            if ((formData.testDepartId == '') ||  formData.testDepartId==null) {
               that.$message.error("请选择关联实验室！");
              return;
            }

            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
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
          }

        })
      },
      //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end
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