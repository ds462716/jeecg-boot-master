<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    :maskClosable=disableSubmit
    @cancel="handleCancel"

  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="试剂编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input ref="inputFocus" :disabled="focusDisable" autocomplete="off" @keyup.enter.native="getPrdNumber" v-decorator="[ 'number', validatorRules.number]" placeholder="请输入试剂编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="1" >
            <a-button @click="getNumber"  v-show="!focusDisable" type="primary">生成</a-button>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="试剂名称" :labelCol="labelCol"  :wrapperCol="wrapperCol">
              <a-input  :disabled="disableSubmit" autocomplete="off" @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" placeholder="请输入试剂名称"></a-input>
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
                :disabled="disableSubmit"
                @search="bzUnitHandleSearch"
                @change="bzUnitHandleChange"
                @focus="bzUnitHandleSearch"
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
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'py', validatorRules.py]" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'wb', validatorRules.wb]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="试剂别名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" @change="bmPinyinTran" v-decorator="[ 'bname', validatorRules.bname]" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="别名拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'bpy', validatorRules.bpy]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="别名五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'bwb', validatorRules.bwb]" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="自定义简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'zdy', validatorRules.zdy]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="使用规格单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :specUnitId="specUnitValue"
                placeholder="请选择使用规格单位"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
                @search="ggUnitHandleSearch"
                @change="ggUnitHandleChange"
                @focus="ggUnitHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'specUnitId', validatorRules.specUnitId]"
              >
                <a-select-option v-for="d in specUnitData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="使用规格数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'specQuantity', validatorRules.specQuantity]" placeholder="请输入规格单位" style="width: 100%" ></a-input-number>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'spec', validatorRules.spec]" placeholder="请输入规格"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'version', validatorRules.version]" ></a-input>
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
                :disabled="disableSubmit"
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
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
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
              <a-input :disabled="disableSubmit" @change="registrationChange" autocomplete="off" v-decorator="[ 'registration', validatorRules.registration]" placeholder="请输入注册证，多个注册证以“；”分开"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="产品组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :groupId="groupValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
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
              <a-select :disabled="disableSubmit" @change="isChargeChange" v-decorator="[ 'isCharge',{'initialValue':'1',rules:isChargeRules}]" placeholder="请选择是否计费">
                <a-select-option value="0">是</a-select-option>
                <a-select-option value="1">否</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12" v-if="isChargeBl">
            <a-form-item label="产品收费代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off"  v-decorator="[ 'chargeCode', validatorRules.chargeCodeF]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12" v-else="!isChargeBl">
            <a-form-item label="产品收费代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off"  v-decorator="[ 'chargeCode', validatorRules.chargeCodeT]" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="是否紧急产品" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select :disabled="disableSubmit" @change="isUrgentChange" v-decorator="[ 'isUrgent',{'initialValue':'1',rules:isUrgentRules}]" placeholder="请选择是否是紧急产品">
                <a-select-option value="0">是</a-select-option>
                <a-select-option value="1">否</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12" v-if="isUrgentBl">
            <a-form-item label="紧急采购数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled v-decorator="[ 'upQuantity', validatorRules.upQuantityF]" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12" v-if="!isUrgentBl">
            <a-form-item label="紧急产品数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" v-decorator="[ 'upQuantity',validatorRules.upQuantityT]" placeholder="请输入紧急产品采购数量" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="已采购数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled v-decorator="[ 'purchasedQuantity']" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="编码规则" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-encodingRule :disabled="disableSubmit" placeholder=""  :multiple="true" v-decorator="['pdProductRules']"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="一级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                :categoryOne="categoryOneValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
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
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
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
            <a-form-item label="进价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'purchasePrice', validatorRules.purchasePrice]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="出价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'sellingPrice', validatorRules.sellingPrice]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="产品权限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select :disabled="disableSubmit"  v-decorator="[ 'power',{'initialValue':'0',rules:powerRules}]" placeholder="请选择产品权限">
                <a-select-option value="0">公有</a-select-option>
                <a-select-option value="1">自有</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="JDE编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'jdeCode', validatorRules.jdeCode]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="器械分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-expand  :disabled="disableSubmit" :trigger-change="true" dictCode="device_classification" v-decorator="['deviceClassification',validatorRules.deviceClassification]"  placeholder="请选择器械分类"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="中标号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'bidingNumber', validatorRules.bidingNumber]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="中标类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-expand  :disabled="disableSubmit" :trigger-change="true" dictCode="biding_type" v-decorator="['bidingType',validatorRules.bidingType]"  placeholder="请选择中标类型"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="中标价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'bidingPrice', validatorRules.bidingPrice]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="财务分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-expand  :disabled="disableSubmit" :trigger-change="true" dictCode="finance_classification" v-decorator="['financeClassification',validatorRules.financeClassification]"  placeholder="财务分类"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'description', validatorRules.description]"></a-textarea>
        </a-form-item>

        <a-form-item v-show="false">
          <a-input  autocomplete="off" v-decorator="[ 'productFlag',{'initialValue':'1'}]" ></a-input>
        </a-form-item>

        <label style="float:left;padding-top:15px;">证照扫描件</label>
        <div class="all-card-box" style="padding-left:105px;margin-bottom: 70px">
          <template v-for="(item, index) in 12" >
            <div class="card-box" :class="imgIsValidity[index]">
              <div class="card-box-code" style="margin-top:10px;margin-left:10px;">
                <a-form-item label="证照名称" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'licenceName'+index, validatorRules['licenceNum'+index]]"></a-input>
                </a-form-item>
                <a-form-item label="证照号码" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'licenceNum'+index, validatorRules['licenceNum'+index]]"></a-input>
                </a-form-item>
                <a-form-item label="有效期" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <j-date :disabled="disableSubmit"  style="width: 100%" v-decorator="[ 'licenceDate'+index, validatorRules['licenceDate'+index]]" :trigger-change="true"  />
                </a-form-item>
              </div>
              <div class="showpic" @click="handlePreview(index)">查看大图</div>
              <div class="controls">
                <div class='pictureUploadDiv'>
                  <div class='tR_upPic_icon'>
                    <input type="file" :disabled="disableSubmit" ref="file" class="upPic" style="width: 100%; height: 100%;" v-on:change="handleFileUpload($event,index)">
                    <div class="smallImg"  style='display:block;width:256px;height:160px;' >
                      <img :src="getAvatarView(index)" v-show="imgIsShow[index].show" ref="upImg" :key="index" class="card-box_img" />
                      <div   v-show="imgIsShow[index].show" class="smallImg_cloBtn" @click="closeImg(index)" ref="close"></div>
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
  import { makeWb } from '@/utils/wubi'
  import { getAction } from  '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import { photoCheck } from '@/utils/fileUpload'
  import JSelectEncodingRule from '@/components/jeecgbiz/JSelectEncodingRule'
  import { generateNumber,getPrdNumber,scanCode } from '@/utils/barcode'
  import {duplicateCheckHasDelFlag } from '@/api/api'
  import {isDisabledNumber } from '@/api/api'
  import {initDictOptions} from '@/components/dict/JDictSelectUtil'

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
    timeout = setTimeout(fake, 0);
  }

  export default {
    name: "PdProductReagentsModal",
    components: { 
      JDate,
      JDictSelectTagExpand,
      JSelectEncodingRule
    },
    data () {
      return {
        unitData: [],
        unitValue: undefined,
        specUnitData: [],
        specUnitValue: undefined,
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
        isChargeBl:true,
        isUrgentBl:true,
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        validateProductId:"",
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        disableSubmit:false,
        focusDisable:false,//用于产品编号是否禁用
        previewVisible: false,
        previewImage: '',
        model: {},
        dictOptions:{
        },
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
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          number: {rules: [
            {required: true, message: '请输入产品编号!'},
            {
              validator: this.validateNumber,
            }
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
          specUnitId: {rules: [
              {required: true, message: '请输入使用规格单位!'},
            ]},
          specQuantity: {rules: [
              {required: true, message: '请输入使用规格数量!'},
              { pattern: /^[1-9]\d*$/, message: '请输入零以上的正整数' }
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
          chargeCodeF: {rules: [
            ]},
          chargeCodeT: {rules: [
              {required: true, message: '请输入产品收费代码!'},
            ]},
          upQuantityF: {rules: [
            ]},
          upQuantityT: {rules: [
              {required: true, message: '请输入紧急产品数量!'},
            ]},
          deviceClassification: {rules: [
              {required: true, message: '请选择器械分类!'},
            ]},
          description: {rules: [
          ]},
          jdeCode: {rules: [
          ]},
          bidingNumber: {rules: [
            ]},
          bidingPrice: {rules: [
            ]},
          bidingType: {rules: [
            ]},
          financeClassification: {rules: [

            ]},
        },
        isChargeRules:[
          {
            required: true, // 必填
            message: '请选择是否计费' // 显示的文本
          }
        ],
        isUrgentRules:[
          {
            required: true, // 必填
            message: '请选择是否是紧急产品' // 显示的文本
          }
        ],
        powerRules:[
          {
            required: true, // 必填
            message: '请选择产品权限' // 显示的文本
          }
        ],
        url: {
          add: "/pd/pdProduct/save",
          edit: "/pd/pdProduct/update",
          queryBzUnit:"/pd/pdUnit/getUnitList?unitType=0",
          queryGgUnit:"/pd/pdUnit/getUnitList?unitType=1",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryGroup:"/pd/pdGroup/getGroupList",
          queryCategoryOne:"/pd/pdCategory/getCategoryOneList?type=0",
          queryCategoryTwo:"/pd/pdCategory/getCategoryOneList?type=1",
          imgerver: window._CONFIG['staticDomainURL'],
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
          this.validateProductId = record.id;
          for(let index = 0;index<12;index++){
            if(record["licenceSite"+index]){
              this.imgIsShow[index].show=true;
            }else{
              this.imgIsShow[index].show=false;
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
          //规格单位
          const specUnitData = [];
          specUnitData.push({
            value: record.specUnitId,
            text: record.specUnitName,
          });
          this.specUnitData = specUnitData;
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
          this.categoryOneValue = record.categoryOne;
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
          //紧急产品状态还原
          let isUrgent = record.isUrgent;
          this.isUrgentChange(isUrgent);
          //收费代码还原
          let isCharge = record.isCharge;
          this.isChargeChange(isCharge);
        }else{
          for(let index = 0;index<12;index++){
            if(!record["licenceSite"+index]){
              this.imgIsShow[index].show=false;
            }
            if(!record["licenceValidity"+index]){
              this.imgIsValidity[index]="validity0";
            }
          };
          //是否计费状态还原；
          this.isChargeBl = true;
          this.isUrgentBl = true;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        //解决滚动条缓存bug
        this.focusDisable = false;
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'number','name','py','wb','bname','bpy','bwb','zdy','specUnitId','specQuantity','spec','version','unitId','power','pdProductRules','categoryOne','categoryTwo','groupId','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','chargeCode','description','productFlag','isUrgent','upQuantity','purchasedQuantity','bidingNumber','bidingType','bidingPrice','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11','jdeCode','deviceClassification','financeClassification'))
          //获取光标
          let input = this.$refs['inputFocus'];
          input.focus();
          //解决滚动条缓存bug
          if(this.disableSubmit){
            this.focusDisable = true;
          }else{
            //校验产品编号是否禁用
            if(record.id){
              this.isDisabledNumber(record.id);
            }else{
              this.focusDisable = false;
            }
          }
        })
      },
      close () {
        //解决滚动条缓存bug
        this.focusDisable = false;
        this.$emit('close');
        this.visible = false;
        this.categoryTwoData = [];
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
        this.form.setFieldsValue(pick(row,'number','name','py','wb','bname','bpy','bwb','zdy','specUnitId','specQuantity','spec','version','unitId','power','categoryOne','categoryTwo','groupId','bidingNumber','bidingType','bidingPrice','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','chargeCode','description','productFlag','isUrgent','upQuantity','purchasedQuantity','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceName11','licenceNum11','licenceDate11','licenceSite11','jdeCode','deviceClassification','financeClassification'))
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
      bzUnitHandleSearch(value) {
        fetch(value, data => (this.unitData = data),this.url.queryBzUnit);
      },
      bzUnitHandleChange(value) {
        this.unitValue = value;
        fetch(value, data => (this.unitData = data),this.url.queryBzUnit);
      },
      ggUnitHandleSearch(value) {
        fetch(value, data => (this.specUnitData = data),this.url.queryGgUnit);
      },
      ggUnitHandleChange(value) {
        this.specUnitValue = value;
        fetch(value, data => (this.specUnitData = data),this.url.queryGgUnit);
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
        //查询时不做修改
        if(!this.disableSubmit){
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
      //生成产品编号
      getNumber(){
        let code = "93";
        let number = generateNumber(code);
        this.form.setFieldsValue({number:number});
      },
      //扫码自动截取试剂编号
      getPrdNumber(e){
        const that = this;
        let val = e.target.value;
        let number = getPrdNumber(val,that);
        this.form.setFieldsValue({number:number});
      },
      //是否需要校验产品收费代码
      isChargeChange(value){
        if(value=="0"){
          //需要校验产品收费代码
          this.isChargeBl = false;
        }else{
          this.isChargeBl = true;
        }
      },
      //是否是紧急产品
      isUrgentChange(value){
        if(value=="0"){
          //需要校验产品收费代码
          this.isUrgentBl = false;
        }else{
          this.isUrgentBl = true;
        }
      },
      validateNumber(rule, value, callback){
        value = value.replace(/\s/g, "");
        var params = {
          tableName: 'pd_product',
          fieldName: 'number',
          fieldVal: value,
          dataId: this.validateProductId
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback("试剂编号已存在!")
          }
        })
      },
      //是否禁用试剂编号
      isDisabledNumber(productId){
        if(productId == ""){
          return;
        }
        isDisabledNumber({id:productId}).then((res)=>{
          if(res.success){
            this.focusDisable = false;
          }else{
            this.focusDisable = true;
          }
        });
      },
      //注册证替换;
      registrationChange(e){

      },
      initDictConfig(){
        initDictOptions('device_classification').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'deviceClassification', res.result)
          }
        })
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