<template>
  <a-card :bordered="true">
  <div>
    <a-button @click="choice" type="primary">选择标识符</a-button>
    <span style="padding-left:8px;"></span>
    <table id="contentTable" class="tableStyle">
      <tr>
        <th><input type="checkbox" id="checkAll"></th>
        <th class="">应用标识符</th>
        <th>应用标识符含义 </th>
        <th>内容位数 </th>
        <th>类型 </th>
        <th>顺序</th>
      </tr>
      <tr v-for="(item, index) in dataSource">
        <td><input type="checkbox"  name="chkList"/>
          <a-form-item>
            <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].identifier',{'initialValue':item.id}]"/>
          </a-form-item>
        </td>
        <td>{{item.value}}
          <a-form-item>
            <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].value',{'initialValue':item.value}]"/>
          </a-form-item>
        </td>
        <td>{{item.meaning}}</td>
        <td>
          <a-form-item>
            <a-input :style="{width: 'calc(100% - 90px)'}" v-if="item.type=='1'" disabled="disabled" v-decorator="['pdEncodingRuleDetails['+index+'].length',{'initialValue':item.length}]"/>
            <a-input :style="{width: 'calc(100% - 90px)'}" v-else="item.type!='1'" v-decorator="['pdEncodingRuleDetails['+index+'].length', {'initialValue':item.length,rules:validatorRules.length}]"/>
          </a-form-item>
        </td>
        <td>{{item.typeText}}
          <a-form-item>
            <a-input type="hidden" v-decorator="['pdEncodingRuleDetails['+index+'].type',{'initialValue':item.type}]"/>
          </a-form-item>
        </td>
        <td>
          <a-form-item>
            <a-input :style="{width: 'calc(100% - 90px)'}"v-decorator="['pdEncodingRuleDetails['+index+'].codeOrder',validatorRules.order]"/>
          </a-form-item>
        </td>
      </tr>
    </table>
  </div>
  <!-- 表单区域 -->
  <pdEncodingIdentifierAdd-modal ref="pdEncodingIdentifierAddModal" @ok="modalFormOk"></pdEncodingIdentifierAdd-modal>
  </a-card>
</template>

<script>
  import { randomUUID, randomNumber } from '@/utils/util'
  import PdEncodingIdentifierAddModal from './PdEncodingIdentifierAddModal'
  import {typeText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: 'DefaultTable',
    components: {PdEncodingIdentifierAddModal},
    data() {
      return {
        loading: false,
        validatorRules:{
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
          order:{rules: [
              {
                required: true, // 必填
                message: '请输入顺序' // 显示的文本
              },
              {
                pattern: '^([1-9]\\d?|100)$',
                message: '顺序的格式不正确'
              }
            ]},
        },
        dataSource: [],
      }
    },
    methods: {
     //选择标识符
      choice() {
        this.$refs.pdEncodingIdentifierAddModal.show();
        this.$refs.pdEncodingIdentifierAddModal.title = "添加标识符";
      },
      // 获取应用标识符选择的数据
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
      }
    }
  }
</script>

<style scoped>
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