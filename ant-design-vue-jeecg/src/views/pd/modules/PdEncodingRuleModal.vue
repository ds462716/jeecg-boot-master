<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="编码名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input @change="pinyinTran"  v-decorator="[ 'name', validatorRules.name]" placeholder="请输入编码名称"></a-input>
        </a-form-item>
        <a-form-item label="编码名称拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入编码名称拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="编码名称五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入编码名称五笔简码"></a-input>
        </a-form-item>
        <a-form-item label="自定义名称查询码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义名称查询码"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <!--<div>-->
          <div style="float: left;">
            <a-button @click="choice" style="margin-left: 0px;margin-bottom: 20px"  type="primary">选择标识符</a-button>
          </div>
          <div style="float: left;width:100%;margin-bottom: 70px">
            <table id="contentTable" class="tableStyle">
              <tr>
                <th>应用标识符</th>
                <th>应用标识符含义 </th>
                <th>内容位数 </th>
                <th>类型 </th>
                <th>顺序</th>
              </tr>
              <tr v-for="(item, index) in dataSource">
                <td>{{item.value}}
                  <a-form-item>
                    <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].value',{'initialValue':item.value}]"/>
                  </a-form-item>
                </td>
                <td>{{item.meaning}}</td>
                <td>
                  <a-form-item>
                    <a-input :style="{width: 'calc(100% - 120px)'}" v-if="item.type=='1'" disabled="disabled" v-decorator="['pdEncodingRuleDetails['+index+'].length',{'initialValue':item.length}]"/>
                    <a-input :style="{width: 'calc(100% - 120px)'}" v-else="item.type!='1'" v-decorator="['pdEncodingRuleDetails['+index+'].length', {'initialValue':item.length,rules:validatorRules.length}]"/>
                  </a-form-item>
                </td>
                <td>{{item.typeText}}
                  <a-form-item>
                    <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].type',{'initialValue':item.type}]"/>
                  </a-form-item>
                </td>
                <td>
                  <a-form-item>
                    <a-input :style="{width: 'calc(100% - 120px)'}"v-decorator="['pdEncodingRuleDetails['+index+'].codeOrder',{'initialValue':item.codeOrder,rules:validatorRules.order}]"/>
                  </a-form-item>
                </td>
                <a-form-item>
                  <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].identifier',{'initialValue':item.id}]"/>
                </a-form-item>
              </tr>
            </table>
          </div>
        <!--</div>-->
        <!-- 表单区域 -->
        <pdEncodingIdentifierAdd-modal ref="pdEncodingIdentifierAddModal" @ok="modalFormOk"></pdEncodingIdentifierAdd-modal>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk" type="primary" :loading="confirmLoading">提交</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
    </div>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { makeWb } from '@/utils/wubi'
  import PdEncodingIdentifierAddModal from './PdEncodingIdentifierAddModal'
  import {typeText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdEncodingRuleModal",
    components: {PdEncodingIdentifierAddModal },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disableSubmit:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        validatorRules:{
          name:{rules: [{ required: true, message: '请输入编码名称!' }]},
          length:[
            {
              required: true, // 必填
              message: '请输入内容位数' // 显示的文本
            },
            {
              pattern: '^[1-9]$|^[1][0-9]$|^[2][0]$',
              message: '内容位数的格式不正确'
            }
          ],
          order:[
            {
              required: true, // 必填
              message: '请输入顺序' // 显示的文本
            },
            {
              pattern: '^([1-9]\\d?|20)$',
              message: '顺序的格式不正确'
            }
          ],
        },
        dataSource: [],
        confirmLoading: false,
        url: {
          add: "/pd/pdEncodingRule/savePdEncodingRule",
          edit: "/pd/pdEncodingRule/updatePdEncodingRule",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.dataSource = [];
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        if(record.hasOwnProperty("pdEncodingRuleDetails")){
          let pdEncodingRuleDetails = record.pdEncodingRuleDetails;
          this.modalFormUpdate(pdEncodingRuleDetails);
        }
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','remarks'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.disableSubmit = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            //选择标识符的校验通过后
            if(values.hasOwnProperty("pdEncodingRuleDetails")){
              //that.confirmLoading = true;
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
              that.$message.error("请添加标识符");
            }
          }
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','codeDetail','codeDesc','codeQuery','totalDigit','remarks'))
      },
      pinyinTran(e){
        var val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        //var py = pinyin.getFullChars(val);//获取全拼
        var py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        var wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      //选择标识符
      choice() {
        this.$refs.pdEncodingIdentifierAddModal.show();
        this.$refs.pdEncodingIdentifierAddModal.title = "添加标识符";
      },
      // 获取应用标识符选择的数据,flag为true时代表修改时回显数据
      modalFormOk (formData) {
        let values = [];
        for(let i=0;i<formData.length;i++){
          values.push({
            id: formData[i].id,
            value: formData[i].value,
            meaning: formData[i].meaning,
            length: formData[i].length,
            typeText: typeText(formData[i].type),
            type: formData[i].type,
            codeOrder: ''
          })
        }
        this.dataSource = values;
      },
      modalFormUpdate(formData){
        let values = [];
        for(let i=0;i<formData.length;i++){
          let data =formData[i];
          values.push({
            id: data.identifier,
            value: data.pdEncodingIdentifier.value,
            meaning: data.pdEncodingIdentifier.meaning,
            length: data.pdEncodingIdentifier.type=="1"?""+data.pdEncodingIdentifier.length:""+data.length,
            typeText: typeText(data.pdEncodingIdentifier.type),
            type: data.pdEncodingIdentifier.type,
            codeOrder: data.codeOrder
          })
        }
        this.dataSource = values;
      }
    }
  }
</script>

<style lang="less" scoped>
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
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
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
  padding: 16px 16px;
  font-weight: 500;
  box-sizing: border-box;
}

.tableStyle> tr > td >input{
  width:45px;
  text-align: center;
}
</style>