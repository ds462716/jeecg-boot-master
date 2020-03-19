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
      <a-form :form="form">
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input ref="inputFocus" :disabled="focusDisable"  autocomplete="off" @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" :style="{width:'100%',margin:'0'}" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'py', validatorRules.py]" :style="{width:'100%',margin:'0'}" ></a-input>
        </a-form-item>
        <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'wb', validatorRules.wb]" :style="{width:'100%',margin:'0'}" ></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'zdy', validatorRules.zdy]"  :style="{width:'100%',margin:'0'}" ></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'remarks', validatorRules.remarks]" :style="{width:'100%',margin:'0'}" ></a-input>
        </a-form-item>
        <label style="float:left;padding-top:15px;">证照扫描件</label>
        <div class="all-card-box" style="padding-left:105px;margin-bottom: 70px">
          <template v-for="(item, index) in 12" >
            <div class="card-box" :class="imgIsValidity[index]">
              <div class="card-box-code" style="margin-top:10px;margin-left:10px;">
                <a-form-item label="证照名称" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'licenceName'+index, validatorRules['licenceNum'+index]]" ></a-input>
                </a-form-item>
                <a-form-item label="证照号码" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'licenceNum'+index, validatorRules['licenceNum'+index]]" ></a-input>
                </a-form-item>
                <a-form-item label="有效期" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <j-date :disabled="disableSubmit" style="width: 100%" v-decorator="[ 'licenceDate'+index, validatorRules['licenceDate'+index]]" :trigger-change="true"  />
                </a-form-item>
              </div>
              <div class="showpic" @click="handlePreview(index)">查看大图</div>
              <div class="controls">
                <div class='pictureUploadDiv'>
                  <div class='tR_upPic_icon'>
                    <input type="file" :disabled="disableSubmit" ref="file" class="upPic" style="width: 100%; height: 100%;" v-on:change="handleFileUpload($event,index)">
                    <div class="smallImg"  style='display:block;width:256px;height:160px;' >
                      <img :src="getAvatarView(index)" v-show="imgIsShow[index].show" ref="upImg" :key="index" class="card-box_img" />
                      <div  v-show="imgIsShow[index].show" class="smallImg_cloBtn" @click="closeImg(index)" ref="close"></div>
                    </div>
                    <a-form-item>
                      <a-input type="hidden" v-decorator="[ 'licenceSite'+index]"/>
                    </a-form-item>
                    <span class="addIcon">+</span>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </a-form>
    </a-spin>

    <template slot="footer">
      <a-button @click="close" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button @click="handleOk" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
    </template>

    <j-modal :visible="previewVisible" :footer="null" @cancel="handleImgCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </j-modal>

  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import { makeWb } from '@/utils/wubi'
  import { photoCheck } from '@/utils/fileUpload'
  import {duplicateCheck } from '@/api/api'

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
        disableSubmit:false,
        focusDisable:false,
        previewVisible: false,
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        validateVenderId:"",
        previewImage: '',
        model: {},
        imgIsShow:[{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false}],
        imgIsValidity:['validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0'],//0无过期，1已过期，2近效期
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol2: {
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
              {
                validator: this.validateName,
              }
          ]},
          py: {rules: [
          ]},
          wb: {rules: [
          ]},
          zdy: {rules: [
          ]},
        },
        url: {
          add: "/pd/pdVender/save",
          edit: "/pd/pdVender/update",
          imgerver: window._CONFIG['staticDomainURL'],
        }
      }
    },
    computed:{
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        //编辑时显示图片
        if(record.hasOwnProperty("id")){
          this.validateVenderId = record.id;
          for(let index = 0;index<12;index++){
            if(record["licenceSite"+index]){
              this.imgIsShow[index].show=true;
            }
            if(record["licenceValidity"+index]){
              this.imgIsValidity[index]="validity"+record["licenceValidity"+index];
            }
          }
        }else{
          for(let index = 0;index<12;index++){
            if(!record["licenceSite"+index]){
              this.imgIsShow[index].show=false;
            }
            if(!record["licenceValidity"+index]){
              this.imgIsValidity[index]="validity0";
            }
          }
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        //解决滚动条缓存bug
        this.focusDisable = false;
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11','remarks'))
          //获取光标
          let input = this.$refs['inputFocus'];
          input.focus();
          //解决滚动条缓存bug
          if(this.disableSubmit){
            this.focusDisable = true;
          }else{
            this.focusDisable = false;
          }
        })
      },
      close () {
        //解决滚动条缓存bug
        this.focusDisable = false;
        this.$emit('close');
        this.visible = false;
        this.disableSubmit = false;
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
               method = 'post';
            }
            let formDataAll = new FormData();
            let imgIsShow = this.imgIsShow;
            for(let m in imgIsShow){
              if(imgIsShow[m].show){
                let oFile = that.$refs.file[m].files[0];
                formDataAll.append("licenceSiteUp"+m, oFile);
              }
            }
            let formData = Object.assign(this.model, values);
            for (let obj in formData) {
              formDataAll.append(obj, formData[obj]?formData[obj]:"");
            }
            //console.log("表单提交数据",formData)
            httpAction(httpurl,formDataAll,method).then((res)=>{
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
      handleFileUpload(event,index){
        let that = this;
        this.imgIsShow[index].show=true;
        event.preventDefault();
        let oFile = that.$refs.file[index].files[0];
        let bo = photoCheck(oFile,that);
        if(bo){
          let oReader = new FileReader();
          oReader.onload = function(e){
            that.$refs.upImg[index].src = e.target.result;
          };
          oReader.readAsDataURL(oFile);
          this.imgIsShow[index].show=true;
        }
      },
      closeImg(index) {
        //查询时不做修改
        if(!this.disableSubmit) {
          let that = this;
          //that.$refs.upImg[index].src = "";
          that.form.setFieldsValue({["licenceSite"+index]:""});
          that.imgIsShow[index].show = false;
        }
      },
      getAvatarView(index){
        return this.url.imgerver +"/"+this.model["licenceSite"+index];
      },
      handleImgCancel () {
        this.previewVisible = false;
      },
      handlePreview (index) {
       if(this.model["licenceSite"+index]){
         this.previewImage = this.url.imgerver +"/"+this.model["licenceSite"+index];
         //window.open(this.previewImage) //新建窗口打开图片
         this.previewVisible = true;//当前窗口打开图片
       }else{
         this.$message.error("请先上传图片!")
       }
      },
      validateName(rule, value, callback){
        var params = {
          tableName: 'pd_vender',
          fieldName: 'name',
          fieldVal: value,
          dataId: this.validateVenderId
        };
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback("生产厂家已存在!")
          }
        })
      },
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
  .card-box{
    width: 300px;
    height: 430px;
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
    left: 10px;
  }
  .upPic{
    display:block;
    width:100%;
    height:100%;
    position:absolute;
    left:0px;
    top:0;
    opacity:0;
    filter:alpha(opacity=0);
    border:1px solid #333;
    z-index:99;
    font-size:100px;
  }
  .addIcon{font-size: 40px;color: #666;font-weight: 600;position: absolute;left: 110px;top: 45px;}
  .smallImg img {width: 100%;height: 100%;position: relative; z-index: 6}
  .smallImg_cloBtn{
    width:20px;
    height:20px;
    position:absolute;
    right:0;
    top:0;
    background:url(../../../assets/close_icon.png) no-repeat center center;
    cursor:pointer;
    background-size:cover;
    z-index:150;
  }
  .validity0{
    border:1px solid #ccc;
  }
  .validity1{
    border:2px solid #FF3333;
  }

  .validity2{
    border:2px solid #FFFFCC;
  }

</style>