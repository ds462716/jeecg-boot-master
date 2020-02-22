<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="true"
    @close="close"
    :maskClosable="false"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input ref="inputFocus" @keyup.enter.native="getPrdNumber" v-decorator="[ 'number', validatorRules.number]" placeholder="请输入产品编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="1">
            <a-button @click="getNumber" type="primary">生成</a-button>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="产品名称" :labelCol="labelCol"  :wrapperCol="wrapperCol">
              <a-input autocomplete="off" @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" placeholder="请输入产品名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :unitId="unitValue"
                placeholder="请选择单位"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="unitHandleSearch"
                @change="unitHandleChange"
                @focus="unitHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'unitId', validatorRules.unitId]"
              >
                <a-select-option v-for="d in unitData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔简码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="产品别名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input @change="bmPinyinTran" v-decorator="[ 'bname', validatorRules.bname]" placeholder="请输入产品别名"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="别名拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'bpy', validatorRules.bpy]" placeholder="请输入别名拼音简码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="别名五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'bwb', validatorRules.bwb]" placeholder="请输入别名五笔简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="自定义简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义简码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'spec', validatorRules.spec]" placeholder="请输入规格"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'version', validatorRules.version]" placeholder="请输入型号"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :venderId="venderValue"
                placeholder="请选择生产厂家"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="venderHandleSearch"
                @change="venderHandleChange"
                @focus="venderHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'venderId', validatorRules.venderId]"
              >
                <a-select-option v-for="d in venderData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :supplierId="supplierValue"
                placeholder="请选择供应商"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="supplierHandleSearch"
                @change="supplierHandleChange"
                @focus="supplierHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'supplierId', validatorRules.supplierId]"
              >
                <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="注册证" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'registration', validatorRules.registration]" placeholder="请输入注册证，多个注册证以“；”分开"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="产品组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :groupId="groupValue"
                placeholder="请选择产品组别"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="groupHandleSearch"
                @change="groupHandleChange"
                @focus="groupHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'groupId', validatorRules.groupId]"
              >
                <a-select-option v-for="d in groupData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="是否计费" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="[ 'isCharge',{'initialValue':'1',rules:isChargeRules}]" placeholder="请选择是否计费">
                <a-select-option value="0">是</a-select-option>
                <a-select-option value="1">否</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="产品收费代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'chargeCode', validatorRules.chargeCode]" placeholder="请输入产品收费代码" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="一级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :categoryOne="categoryOneValue"
                placeholder="请选择一级分类"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="categoryOneHandleSearch"
                @change="categoryOneHandleChange"
                @focus="categoryOneHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'categoryOne', validatorRules.categoryOne]"
              >
                <a-select-option v-for="d in categoryOneData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="二级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <!-- <a-input v-decorator="[ 'categoryTwo', validatorRules.categoryTwo]" placeholder="请输入二级分类"></a-input>-->
              <a-select
                showSearch
                :categoryTwo="categoryTwoValue"
                placeholder="请选择二级分类"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="categoryTwoHandleSearch"
                @change="categoryTwoHandleChange"
                @focus="categoryTwoHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'categoryTwo', validatorRules.categoryTwo]"
              >
                <a-select-option v-for="d in categoryTwoData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="编码规则" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-encodingRule placeholder="请选择编码规则" :multiple="true" v-decorator="['pdProductRules']"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="产品权限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="[ 'power',{'initialValue':'0'}]" placeholder="请选择产品权限">
                <a-select-option value="0">公有</a-select-option>
                <a-select-option value="1">自有</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="进价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'purchasePrice', validatorRules.purchasePrice]" placeholder="请输入进价" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="出价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'sellingPrice', validatorRules.sellingPrice]" placeholder="请输入出价" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'description', validatorRules.description]" placeholder="请输入描述"></a-textarea>
        </a-form-item>

        <label style="float:left;padding-top:15px;">证照扫描件</label>
        <div class="all-card-box" style="padding-left:105px;margin-bottom: 70px">
          <template v-for="(item, index) in 12" >
            <div class="card-box" :class="imgIsValidity[index]">
              <div class="card-box-code">
                <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'licenceName'+index, validatorRules['licenceNum'+index]]"  placeholder="请输入证照名称"></a-input>
                </a-form-item>
                <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'licenceNum'+index, validatorRules['licenceNum'+index]]"  placeholder="请输入证照号码"></a-input>
                </a-form-item>
                <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate'+index, validatorRules['licenceDate'+index]]" :trigger-change="true"  />
                </a-form-item>
              </div>
              <div class="showpic" @click="handlePreview(index)">查看大图</div>
              <div class="controls">
                <div class='pictureUploadDiv'>
                  <div class='tR_upPic_icon'>
                    <input type="file" ref="file" class="upPic" style="width: 100%; height: 100%;" v-on:change="handleFileUpload($event,index)">
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
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk" type="primary" :loading="confirmLoading">提交</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
    </div>
    <a-modal :visible="previewVisible" :footer="null" :width="900" @cancel="handleImgCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </a-modal>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import { makeWb } from '@/utils/wubi'
  import { getAction } from  '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import { photoCheck } from '@/utils/fileUpload'
  import JSelectEncodingRule from '@/components/jeecgbiz/JSelectEncodingRule'
  import { generateNumber,getPrdNumber,scanCode } from '@/utils/barcode'

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{name:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 300);
  }

  export default {
    name: "PdProductModal",
    components: { 
      JDate,
      JDictSelectTagExpand,
      JSelectEncodingRule
    },
    data () {
      return {
        unitData: [],
        unitValue: undefined,
        venderData: [],
        venderValue: undefined,
        supplierData: [],
        supplierValue: undefined,
        groupData: [],
        groupValue: undefined,
        categoryOneData: [],
        categoryOneValue: undefined,
        categoryTwoData: [],
        categoryTwoValue: undefined,
        notFoundContent:"未找到内容",
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        disableSubmit:false,
        previewVisible: false,
        previewImage: '',
        model: {},
        imgIsShow:[{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false},{show:false}],
        imgIsValidity:['validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0','validity0'],//0无过期，1已过期，2近效期
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
          number: {rules: [
            {required: true, message: '请输入产品编号!'},
          ]},
          name: {rules: [
            {required: true, message: '请输入产品名称!'},
          ]},
          py: {rules: [
          ]},
          wb: {rules: [
          ]},
          bname: {rules: [
          ]},
          bpy: {rules: [
          ]},
          bwb: {rules: [
          ]},
          zdy: {rules: [
          ]},
          spec: {rules: [
              {required: true, message: '请输入规格!'},
          ]},
          version: {rules: [
          ]},
          unitId: {rules: [
              {required: true, message: '请选择单位!'},
          ]},
          categoryOne: {rules: [
          ]},
          categoryTwo: {rules: [
          ]},
          groupId: {rules: [
          ]},
          venderId: {rules: [
              {required: true, message: '请选择生产厂家!'},
          ]},
          supplierId: {rules: [
          ]},
          purchasePrice: {rules: [
          ]},
          sellingPrice: {rules: [
          ]},
          registration: {rules: [
              {required: true, message: '请输入注册证!'},
          ]},
          chargeCode: {rules: [
          ]},
          description: {rules: [
          ]}
        },
        isChargeRules:[
          {
            required: true, // 必填
            message: '请选择是否计费' // 显示的文本
          }
        ],
        url: {
          add: "/pd/pdProduct/save",
          edit: "/pd/pdProduct/update",
          queryUnit:"/pd/pdUnit/getUnitList",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryGroup:"/pd/pdGroup/getGroupList",
          queryCategoryOne:"/pd/pdCategory/getCategoryOneList?type=0",
          queryCategoryTwo:"/pd/pdCategory/getCategoryOneList?type=1",
          imgerver: window._CONFIG['domianURL']+"/sys/common/view",
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
        //编辑时显示图片
        if(record.hasOwnProperty("id")){
          for(let index = 0;index<12;index++){
            if(record["licenceSite"+index]){
              this.imgIsShow[index].show=true;
            }
            if(record["licenceValidity"+index]){
              this.imgIsValidity[index]="validity"+record["licenceValidity"+index];
            }
          }
          //单位
          const unitData = [];
          unitData.push({
            value: record.unitId,
            text: record.unitName,
          });
          this.unitData = unitData;
          //生产厂家
          const venderData = [];
          venderData.push({
            value: record.venderId,
            text: record.venderName,
          });
          this.venderData = venderData;
          //供应商
          const supplierData = [];
          supplierData.push({
            value: record.supplierId,
            text: record.supplierName,
          });
          this.supplierData = supplierData;
          //组别
          const groupData = [];
          groupData.push({
            value: record.groupId,
            text: record.groupName,
          });
          this.groupData = groupData;
          //一级分类
          const categoryOneData = [];
          categoryOneData.push({
            value: record.categoryOne,
            text: record.categoryOneName,
          });
          this.categoryOneData = categoryOneData;
          //二级分类
          const categoryTwoData = [];
          categoryTwoData.push({
            value: record.categoryTwo,
            text: record.categoryTwoName,
          });
          this.categoryTwoData = categoryTwoData;
          //编码规则
          var pdProductRules = record.pdProductRules;
          if(pdProductRules  instanceof Array){
            let pdProductRuleStr = "";
            if(pdProductRules.length>0){
              for(let i = 0;i<pdProductRules.length;i++){
                pdProductRuleStr+=pdProductRules[i]+",";
              }
              if(pdProductRuleStr.charAt(pdProductRuleStr.length - 1) == ","){
                pdProductRuleStr = pdProductRuleStr.substr(0, pdProductRuleStr.length - 1);
              }
            }
            record.pdProductRules = pdProductRuleStr;
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
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'number','name','py','wb','bname','bpy','bwb','zdy','spec','version','unitId','power','pdProductRules','categoryOne','categoryTwo','groupId','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','chargeCode','description','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11'))
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
           //console.log("表单提交数据",formDataAll)
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'number','name','py','wb','bname','bpy','bwb','zdy','spec','version','unitId','power','categoryOne','categoryTwo','groupId','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','chargeCode','description','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11'))
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
      bmPinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        //let py = pinyin.getFullChars(val);//获取全拼
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({bpy:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({bwb:wb});//获取五笔简码
      },
     //单位查询start
      unitHandleSearch(value) {
        fetch(value, data => (this.unitData = data),this.url.queryUnit);
      },
      unitHandleChange(value) {
        this.unitValue = value;
        fetch(value, data => (this.unitData = data),this.url.queryUnit);
      },
      //单位查询end
      //生产厂家查询start
      venderHandleSearch(value) {
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      venderHandleChange(value) {
        this.venderValue = value;
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      //生产厂家查询end
      //供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //供应商查询end
      //组别查询start
      groupHandleSearch(value) {
        fetch(value, data => (this.groupData = data),this.url.queryGroup);
      },
      groupHandleChange(value) {
        this.groupValue = value;
        fetch(value, data => (this.groupData = data),this.url.queryGroup);
      },
      //组别查询end
      //一级分类查询start
      categoryOneHandleSearch(value) {
        fetch(value, data => (this.categoryOneData = data),this.url.queryCategoryOne);
      },
      categoryOneHandleChange(value) {
        this.categoryOneValue = value;
        fetch(value, data => (this.categoryOneData = data),this.url.queryCategoryOne);
        this.form.setFieldsValue({categoryTwo:""});
      },
      //一级分类查询end
      //二级分类查询start
      categoryTwoHandleSearch(value) {
        let categoryOne = this.categoryOneValue;
        if(!categoryOne){
          categoryOne = "";
        }
        fetch(value, data => (this.categoryTwoData = data),this.url.queryCategoryTwo+"&parentId="+categoryOne);
      },
      categoryTwoHandleChange(value) {
        let categoryOne = this.categoryOneValue;
        if(!categoryOne){
          categoryOne = "";
        }
        this.categoryTwoValue = value;
        fetch(value, data => (this.categoryTwoData = data),this.url.queryCategoryTwo+"&parentId="+categoryOne);
      },
      //一级分类查询end
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
        let that = this;
        //that.$refs.upImg[index].src = "";
        this.form.setFieldsValue({["licenceSite"+index]:""});
        this.imgIsShow[index].show = false;
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
      //生成产品编号
      getNumber(){
        let code = "93";
        let number = generateNumber(code);
        this.form.setFieldsValue({number:number});
      },
      //扫码自动截取产品编号
      getPrdNumber(e){
        const that = this;
        let val = e.target.value;
        let number = getPrdNumber(val,that);
        this.form.setFieldsValue({number:number});
      }
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
.drawer-bootom-button {
  position: absolute;
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
  background:url(../../../assets/close.png) no-repeat center center;
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