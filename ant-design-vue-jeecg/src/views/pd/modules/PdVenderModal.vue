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

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" :style="{width:'100%',margin:'0'}" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'py', validatorRules.py]" :style="{width:'100%',margin:'0'}" placeholder="请输入拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wb', validatorRules.wb]" :style="{width:'100%',margin:'0'}" placeholder="请输入五笔简码"></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zdy', validatorRules.zdy]"  :style="{width:'100%',margin:'0'}" placeholder="请输入自定义码"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" :style="{width:'100%',margin:'0'}" placeholder="请输入备注"></a-input>
        </a-form-item>
        <label for="" style="float:left;padding-top:15px;">证照扫描件</label>
        <div class="all-card-box" style="padding-left:105px;">
          <div class="card-box">
            <div class="card-box-code">
              <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'licenceName1', validatorRules.licenceName1]"  placeholder="请输入证照名称"></a-input>
              </a-form-item>
              <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'licenceNum1', validatorRules.licenceNum1]"  placeholder="请输入证照号码"></a-input>
              </a-form-item>
              <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate1', validatorRules.licenceDate1]" :trigger-change="true"  />
              </a-form-item>
            </div>
            <div class="showpic">查看大图</div>
            <div class="controls">
              <div class='pictureUploadDiv'>
                <div class='tR_upPic_icon'>
                  <!--<a-form-item  :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input type="file" class="upPic" :style="{width: '100%',height: '100%'}"></a-input>
                  </a-form-item>-->
                  <input type="file" ref="file1" class="upPic" style="width: 100%; height: 100%;" v-on:change="handleFileUpload($event,1)">
                  <!--<input type="file" data-code='0' class="upPic" @change="aaa($event)" name="licenceSite1Up" style="width: 100%; height: 100%;"/>-->
                  <div class="smallImg"  style='display:block;width:256px;height:160px;' >
                    <img ref="upImg1" :key="1" class="card-box_img" />
                    <div></div>
                </div>
                <input type="hidden" name="licenceSite1" value="${pdVender.licenceSite1 }" />
                <span class="addIcon">+</span>
              </div>
            </div>
            </div>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName2', validatorRules.licenceName2]"  placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum2', validatorRules.licenceNum2]"  placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate2', validatorRules.licenceDate2]" :trigger-change="true" />
            </a-form-item>
            <!--<a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite2', validatorRules.licenceSite2]" :trigger-change="true"></j-upload>
            </a-form-item>-->
              <div class="showpic">查看大图</div>
              <div class="controls">
                <div class='pictureUploadDiv'>
                  <div class='tR_upPic_icon'>
                    <!--<a-form-item  :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input type="file" class="upPic" :style="{width: '100%',height: '100%'}"></a-input>
                    </a-form-item>-->
                    <input type="file"  ref="file2" class="upPic" style="width: 100%; height: 100%;" v-on:change="handleFileUpload($event,2)">
                    <!--<input type="file" data-code='0' class="upPic" @change="aaa($event)" name="licenceSite1Up" style="width: 100%; height: 100%;"/>-->
                    <div class="smallImg"  style='display:block;width:256px;height:160px;' >
                      <img ref="upImg2" class="card-box_img" />
                      <div></div>
                    </div>
                    <input type="hidden" name="licenceSite1" value="${pdVender.licenceSite1 }" />
                    <span class="addIcon">+</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName3', validatorRules.licenceName3]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum3', validatorRules.licenceNum3]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate3', validatorRules.licenceDate3]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite3', validatorRules.licenceSite3]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName4', validatorRules.licenceName4]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum4', validatorRules.licenceNum4]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate4', validatorRules.licenceDate4]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite4', validatorRules.licenceSite4]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName5', validatorRules.licenceName5]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum5', validatorRules.licenceNum5]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate5', validatorRules.licenceDate5]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite5', validatorRules.licenceSite5]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName6', validatorRules.licenceName6]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum6', validatorRules.licenceNum6]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate6', validatorRules.licenceDate6]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite6', validatorRules.licenceSite6]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName7', validatorRules.licenceName7]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum7', validatorRules.licenceNum7]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate7', validatorRules.licenceDate7]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite7', validatorRules.licenceSite7]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName8', validatorRules.licenceName8]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum8', validatorRules.licenceNum8]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate8', validatorRules.licenceDate8]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite8', validatorRules.licenceSite8]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName9', validatorRules.licenceName9]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum9', validatorRules.licenceNum9]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate9', validatorRules.licenceDate9]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite9', validatorRules.licenceSite9]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName10', validatorRules.licenceName10]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum10', validatorRules.licenceNum10]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate10', validatorRules.licenceDate10]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite10', validatorRules.licenceSite10]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName11', validatorRules.licenceName11]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum11', validatorRules.licenceNum11]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate11', validatorRules.licenceDate11]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite11', validatorRules.licenceSite11]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
          <div class="card-box">
            <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceName12', validatorRules.licenceName12]" placeholder="请输入证照名称"></a-input>
            </a-form-item>
            <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'licenceNum12', validatorRules.licenceNum12]" placeholder="请输入证照号码"></a-input>
            </a-form-item>
            <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate12', validatorRules.licenceDate12]" :trigger-change="true" />
            </a-form-item>
            <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['licenceSite12', validatorRules.licenceSite12]" :trigger-change="true"></j-upload>
            </a-form-item>
          </div>
        </div>
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import Vue from 'vue'
  import { makeWb } from '@/utils/wubi'
  var  imgUploadSucCallBack,imgUploadClearCallBack;
  export default {
    name: "PdVenderModal",
    components: { 
      JDate,
      JUpload,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        model: {},
        src1:"",
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        uploadLoading:false,
        headers:{},
        picUrl: "",
        validatorRules: {
          name: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          py: {rules: [
          ]},
          wb: {rules: [
          ]},
          zdy: {rules: [
          ]},
          licenceName1: {rules: [
          ]},
          licenceNum1: {rules: [
          ]},
          licenceDate1: {rules: [
          ]},
          licenceSite1: {rules: [
          ]},
          licenceName2: {rules: [
          ]},
          licenceNum2: {rules: [
          ]},
          licenceDate2: {rules: [
          ]},
          licenceSite2: {rules: [
          ]},
          licenceName3: {rules: [
          ]},
          licenceNum3: {rules: [
          ]},
          licenceDate3: {rules: [
          ]},
          licenceSite3: {rules: [
          ]},
          licenceName4: {rules: [
          ]},
          licenceNum4: {rules: [
          ]},
          licenceDate4: {rules: [
          ]},
          licenceSite4: {rules: [
          ]},
          licenceName5: {rules: [
          ]},
          licenceNum5: {rules: [
          ]},
          licenceDate5: {rules: [
          ]},
          licenceSite5: {rules: [
          ]},
          licenceName6: {rules: [
          ]},
          licenceNum6: {rules: [
          ]},
          licenceDate6: {rules: [
          ]},
          licenceSite6: {rules: [
          ]},
          licenceName7: {rules: [
          ]},
          licenceNum7: {rules: [
          ]},
          licenceDate7: {rules: [
          ]},
          licenceSite7: {rules: [
          ]},
          licenceName8: {rules: [
          ]},
          licenceNum8: {rules: [
          ]},
          licenceDate8: {rules: [
          ]},
          licenceSite8: {rules: [
          ]},
          licenceName9: {rules: [
          ]},
          licenceNum9: {rules: [
          ]},
          licenceDate9: {rules: [
          ]},
          licenceSite9: {rules: [
          ]},
          licenceName10: {rules: [
          ]},
          licenceNum10: {rules: [
          ]},
          licenceDate10: {rules: [
          ]},
          licenceSite10: {rules: [
          ]},
          licenceName11: {rules: [
          ]},
          licenceNum11: {rules: [
          ]},
          licenceDate11: {rules: [
          ]},
          licenceSite11: {rules: [
          ]},
          licenceName12: {rules: [
          ]},
          licenceNum12: {rules: [
          ]},
          licenceDate12: {rules: [
          ]},
          licenceSite12: {rules: [
          ]},
        },
        url: {
          fileUpload: window._CONFIG['domianURL']+"/sys/common/upload",
          add: "/pd/pdVender/add",
          edit: "/pd/pdVender/edit",
        }
      }
    },
    created () {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token}
    },
    computed:{
      uploadAction:function () {
        return this.url.fileUpload;
      }
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11','licenceName12','licenceNum12','licenceDate12','licenceSite12','createTime','updateTime'))
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
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11','licenceName12','licenceNum12','licenceDate12','licenceSite12','createTime','updateTime'))
      },
      getAvatarView(){
        return this.url.imgerver +"/"+ this.model.avatar;
      },
      beforeUpload: function(file){
        var fileType = file.type;
        if(fileType.indexOf('image')<0){
          this.$message.warning('请上传图片');
          return false;
        }
        //TODO 验证文件大小
      },
      handleChange (info) {
        this.picUrl = "";
        if (info.file.status === 'uploading') {
          this.uploadLoading = true;
          return
        }
        if (info.file.status === 'done') {
          var response = info.file.response;
          this.uploadLoading = false;
          console.log(response);
          if(response.success){
            this.model.avatar = response.message;
            this.picUrl = "Has no pic url yet";
          }else{
            this.$message.warning(response.message);
          }
        }
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
      handleFileUpload(event,key){
        let that = this;
        event.preventDefault();
        var oFile = that.$refs[`file${key}`].files[0];
        var rFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
        if (oFile && !rFilter.test(oFile.type)) {
          alert('上传文件非图片格式');
          return;
        }
        if (oFile && oFile.size > 5242880) {
          alert('上传图片不能超过5M');
          return;
        }
        var oReader = new FileReader();
        oReader.onload = function(e){
          that.$refs[`upImg${key}`].src = e.target.result;
        };
        oReader.readAsDataURL(oFile);
        //图片上传完成回调
        if(imgUploadSucCallBack &&  typeof(imgUploadSucCallBack)== "function"){
          imgUploadSucCallBack(this);
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
  .ant-input{
    width:180px;
  }
  .card-box{
    width: 300px;
    height: 410px;
    border: 1px solid #ddd;
    padding: 0px;
    display:inline-block;
    margin:15px 40px 0 0;
    vertical-align: top;
  }
  .pictureUploadDiv{
    width:100%;
    height:auto;
    overflow:hidden
  }
  .showpic{
    padding-left: 12px;
    cursor:pointer
  }
.controls{
  margin-left:0;
}
.tR_upPic_icon{
  width:auto;
  height: auto;
  background:none;
  border:1px solid #ccc;
  margin:10px 0 0 10px;
  display:block;
  margin:10px 10px 10px 10px;
  border:1px solid #eee;
  float:left;
  border-radius:2px;
  //background:url(../spd/img/icon_add.png) no-repeat center center;
  background-size:cover;
  position:relative;
  overflow:hidden;
}
.upPic{
  display:block;
  width:100%;
  height:100%;
  position:absolute;
  left:0;
  top:0;
  opacity:0;
  filter:alpha(opacity=0);
  border:1px solid #333;
  z-index:99;
  font-size:100px;
}
.addIcon{font-size: 40px;color: #666;font-weight: 600;position: absolute;left: 110px;top: 45px;}
.smallImg img {width: 100%;height: 100%;position: relative; z-index: 6;}
</style>