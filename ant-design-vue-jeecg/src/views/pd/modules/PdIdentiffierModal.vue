<template>
  <a-card :bordered="true">
  <div>
    <a-button @click="choice" type="primary">选择标识符</a-button>
    <span style="padding-left:8px;"></span>
    <j-editable-table
      ref="editableTable"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      style="margin-top: 8px;text-align:center;"
      :rowSelection="true"
      >
    </j-editable-table>
  </div>
  <!-- 表单区域 -->
  <pdEncodingIdentifierAdd-modal ref="pdEncodingIdentifierAddModal" @ok="modalFormOk"></pdEncodingIdentifierAdd-modal>
  </a-card>
</template>

<script>
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { randomUUID, randomNumber } from '@/utils/util'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import PdEncodingIdentifierAddModal from './PdEncodingIdentifierAddModal'
  import {typeText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: 'DefaultTable',
    components: { JEditableTable ,PdEncodingIdentifierAddModal},
    data() {
      return {
        loading: false,
        columns: [
          {
            key:'value',
            title:'应用标识符',
            align:"center",
            dataIndex: 'value'
          },
          {
            key:'meaning',
            title:'标识符含义',
            align:"center",
            dataIndex: 'meaning'
          },
          {
            key:'length',
            width: '60px',
            title:'内容位数',
            align:"center",
            type: FormTypes.input,
            dataIndex: 'length',
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
              {
                pattern: '^[1-9]$|^[1][0-9]$|^[2][0]$',
                message: '${title}的格式不正确'
              }
            ]
          },
          {
            key:'typeText',
            title:'类型',
            align:"center",
            dataIndex: 'type'
          },{
            key:'type',
            title:'类型',
            align:"center",
            type: FormTypes.hidden,
            dataIndex: 'type'
          },
          {
            title: '顺序',
            key: 'codeOrder',
            width: '60px',
            type: FormTypes.input,
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
              {
                pattern: '^([1-9]\\d?|100)$',
                message: '${title}的格式不正确'
              }
            ]
          },

        ],
        disabledRows:[],
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
        let disabledRowsValue =[];
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
          //固定长度时需要禁用字段
          if(formData[i].type=="1"){
            disabledRowsValue.push(formData[i].id);
          }
        }
        this.dataSource = values;
        this.disabledRows = disabledRowsValue;
      }
    }
  }
</script>

<style scoped>

</style>