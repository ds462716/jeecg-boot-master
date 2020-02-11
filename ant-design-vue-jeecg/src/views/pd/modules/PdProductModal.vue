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

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'number', validatorRules.number]" placeholder="请输入产品编号"></a-input>
            </a-form-item>
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
                :showArrow="false"
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
              <!--<a-input v-decorator="[ 'venderId', validatorRules.venderId]" placeholder="请选择生产厂家"></a-input>-->

              <a-select
                showSearch
                :venderId="venderValue"
                placeholder="请选择生产厂家"
                :defaultActiveFirstOption="false"
                :showArrow="false"
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
              <!--<a-input v-decorator="[ 'supplierId', validatorRules.supplierId]" placeholder="请输入供应商"></a-input>-->
              <a-select
                showSearch
                :venderId="supplierValue"
                placeholder="请选择供应商"
                :defaultActiveFirstOption="false"
                :showArrow="false"
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
            <a-form-item label="是否计费" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'isCharge', validatorRules.isCharge]" placeholder="请输入是否计费"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="产品组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'groupId', validatorRules.groupId]" placeholder="请输入产品组别"></a-input>
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
            <a-form-item label="产品收费代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'sellingPrice', validatorRules.sellingPrice]" placeholder="请输入产品收费代码" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item label="一级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'categoryOne', validatorRules.categoryOne]" placeholder="请输入一级分类"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="二级分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'categoryTwo', validatorRules.categoryTwo]" placeholder="请输入二级分类"></a-input>
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
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName0', validatorRules.licenceName0]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum0', validatorRules.licenceNum0]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照号码" v-decorator="[ 'licenceDate0', validatorRules.licenceDate0]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite0', validatorRules.licenceSite0]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity0', validatorRules.licenceValidity0]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="产品授权书" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName1', validatorRules.licenceName1]" placeholder="请输入产品授权书"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum1', validatorRules.licenceNum1]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效期" v-decorator="[ 'licenceDate1', validatorRules.licenceDate1]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite1', validatorRules.licenceSite1]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity1', validatorRules.licenceValidity1]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName2', validatorRules.licenceName2]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum2', validatorRules.licenceNum2]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效期" v-decorator="[ 'licenceDate2', validatorRules.licenceDate2]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite2', validatorRules.licenceSite2]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity2', validatorRules.licenceValidity2]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName3', validatorRules.licenceName3]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum3', validatorRules.licenceNum3]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate3', validatorRules.licenceDate3]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite3', validatorRules.licenceSite3]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity3', validatorRules.licenceValidity3]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName4', validatorRules.licenceName4]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum4', validatorRules.licenceNum4]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate4', validatorRules.licenceDate4]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite4', validatorRules.licenceSite4]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity4', validatorRules.licenceValidity4]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName5', validatorRules.licenceName5]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum5', validatorRules.licenceNum5]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate5', validatorRules.licenceDate5]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址5" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite5', validatorRules.licenceSite5]" placeholder="请输入证照地址5"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity5', validatorRules.licenceValidity5]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName6', validatorRules.licenceName6]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum6', validatorRules.licenceNum6]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate6', validatorRules.licenceDate6]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite6', validatorRules.licenceSite6]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity6', validatorRules.licenceValidity6]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName7', validatorRules.licenceName7]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum7', validatorRules.licenceNum7]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate7', validatorRules.licenceDate7]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite7', validatorRules.licenceSite7]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity7', validatorRules.licenceValidity7]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName8', validatorRules.licenceName8]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum8', validatorRules.licenceNum8]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate8', validatorRules.licenceDate8]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite8', validatorRules.licenceSite8]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label=" 是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity8', validatorRules.licenceValidity8]" placeholder="请输入 是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName9', validatorRules.licenceName9]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum9', validatorRules.licenceNum9]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate9', validatorRules.licenceDate9]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite9', validatorRules.licenceSite9]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity9', validatorRules.licenceValidity9]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="证照名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceName10', validatorRules.licenceName10]" placeholder="请输入证照名称"></a-input>
        </a-form-item>
        <a-form-item label="证照号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceNum10', validatorRules.licenceNum10]" placeholder="请输入证照号码"></a-input>
        </a-form-item>
        <a-form-item label="证照有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证照有效期" v-decorator="[ 'licenceDate10', validatorRules.licenceDate10]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="证照地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceSite10', validatorRules.licenceSite10]" placeholder="请输入证照地址"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'licenceValidity10', validatorRules.licenceValidity10]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新日期" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>
        </a-form-item>
        <a-form-item label="是否过期标识，0未过期，1已过期，2近效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'validityFlag', validatorRules.validityFlag]" placeholder="请输入是否过期标识，0未过期，1已过期，2近效期"></a-input>
        </a-form-item>
        <a-form-item label="delFlag" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入delFlag"></a-input>
        </a-form-item>
        
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
  import { makeWb } from '@/utils/wubi'
  import { getAction } from  '@/api/manage'

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
    },
    data () {
      return {
        unitData: [],
        unitValue: undefined,
        venderData: [],
        venderValue: undefined,
        supplierData: [],
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
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
          isCharge: {rules: [
              {required: true, message: '请选择是否计费!'},
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
          description: {rules: [
          ]},
          licenceName0: {rules: [
          ]},
          licenceNum0: {rules: [
          ]},
          licenceDate0: {rules: [
          ]},
          licenceSite0: {rules: [
          ]},
          licenceValidity0: {rules: [
          ]},
          licenceName1: {rules: [
          ]},
          licenceNum1: {rules: [
          ]},
          licenceDate1: {rules: [
          ]},
          licenceSite1: {rules: [
          ]},
          licenceValidity1: {rules: [
          ]},
          licenceName2: {rules: [
          ]},
          licenceNum2: {rules: [
          ]},
          licenceDate2: {rules: [
          ]},
          licenceSite2: {rules: [
          ]},
          licenceValidity2: {rules: [
          ]},
          licenceName3: {rules: [
          ]},
          licenceNum3: {rules: [
          ]},
          licenceDate3: {rules: [
          ]},
          licenceSite3: {rules: [
          ]},
          licenceValidity3: {rules: [
          ]},
          licenceName4: {rules: [
          ]},
          licenceNum4: {rules: [
          ]},
          licenceDate4: {rules: [
          ]},
          licenceSite4: {rules: [
          ]},
          licenceValidity4: {rules: [
          ]},
          licenceName5: {rules: [
          ]},
          licenceNum5: {rules: [
          ]},
          licenceDate5: {rules: [
          ]},
          licenceSite5: {rules: [
          ]},
          licenceValidity5: {rules: [
          ]},
          licenceName6: {rules: [
          ]},
          licenceNum6: {rules: [
          ]},
          licenceDate6: {rules: [
          ]},
          licenceSite6: {rules: [
          ]},
          licenceValidity6: {rules: [
          ]},
          licenceName7: {rules: [
          ]},
          licenceNum7: {rules: [
          ]},
          licenceDate7: {rules: [
          ]},
          licenceSite7: {rules: [
          ]},
          licenceValidity7: {rules: [
          ]},
          licenceName8: {rules: [
          ]},
          licenceNum8: {rules: [
          ]},
          licenceDate8: {rules: [
          ]},
          licenceSite8: {rules: [
          ]},
          licenceValidity8: {rules: [
          ]},
          licenceName9: {rules: [
          ]},
          licenceNum9: {rules: [
          ]},
          licenceDate9: {rules: [
          ]},
          licenceSite9: {rules: [
          ]},
          licenceValidity9: {rules: [
          ]},
          licenceName10: {rules: [
          ]},
          licenceNum10: {rules: [
          ]},
          licenceDate10: {rules: [
          ]},
          licenceSite10: {rules: [
          ]},
          licenceValidity10: {rules: [
          ]},
          createBy: {rules: [
          ]},
          createTime: {rules: [
          ]},
          updateBy: {rules: [
          ]},
          updateTime: {rules: [
          ]},
          sysOrgCode: {rules: [
          ]},
          validityFlag: {rules: [
          ]},
          delFlag: {rules: [
            {required: true, message: '请输入delFlag!'},
          ]},
        },
        url: {
          add: "/pd/pdProduct/add",
          edit: "/pd/pdProduct/edit",
          queryUnit:"/pd/pdEncodingRule/getEncodingRuleList",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
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
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'number','name','py','wb','bname','bpy','bwb','zdy','spec','version','unitId','categoryOne','categoryTwo','groupId','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','description','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceValidity0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceValidity1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceValidity2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceValidity3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceValidity4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceValidity5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceValidity6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceValidity7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceValidity8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceValidity9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceValidity10','createBy','createTime','updateBy','updateTime','sysOrgCode','validityFlag','delFlag'))
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
        this.form.setFieldsValue(pick(row,'number','name','py','wb','bname','bpy','bwb','zdy','spec','version','unitId','categoryOne','categoryTwo','groupId','venderId','isCharge','supplierId','purchasePrice','sellingPrice','registration','description','licenceName0','licenceNum0','licenceDate0','licenceSite0','licenceValidity0','licenceName1','licenceNum1','licenceDate1','licenceSite1','licenceValidity1','licenceName2','licenceNum2','licenceDate2','licenceSite2','licenceValidity2','licenceName3','licenceNum3','licenceDate3','licenceSite3','licenceValidity3','licenceName4','licenceNum4','licenceDate4','licenceSite4','licenceValidity4','licenceName5','licenceNum5','licenceDate5','licenceSite5','licenceValidity5','licenceName6','licenceNum6','licenceDate6','licenceSite6','licenceValidity6','licenceName7','licenceNum7','licenceDate7','licenceSite7','licenceValidity7','licenceName8','licenceNum8','licenceDate8','licenceSite8','licenceValidity8','licenceName9','licenceNum9','licenceDate9','licenceSite9','licenceValidity9','licenceName10','licenceNum10','licenceDate10','licenceSite10','licenceValidity10','createBy','createTime','updateBy','updateTime','sysOrgCode','validityFlag','delFlag'))
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
</style>