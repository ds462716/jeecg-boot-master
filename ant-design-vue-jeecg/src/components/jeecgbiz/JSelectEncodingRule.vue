<template>
  <!-- 定义在这里的参数都是不可在外部覆盖的，防止出现问题 -->
  <j-select-biz-component
    :value="value"
    :listUrl="url.list"
    :columns="columns"
    v-on="$listeners"
    v-bind="attrs"
  />
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectMultiUser',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        url: { list: '/pd/pdEncodingRule/list' },
        columns: [
          { title: '编码名称', align: 'center', width: '10%', widthRight: '70%', dataIndex: 'name' },
          { title: '规则详情', align: 'center', width: '30%', dataIndex: 'codeDetail' },
          { title: '规则描述', align: 'center', width: '50%', dataIndex: 'codeDesc' },
          { title: '总位数', align: 'center', width: '5%', dataIndex: 'totalDigit' }
        ],
        // 定义在这里的参数都是可以在外部传递覆盖的，可以更灵活的定制化使用的组件
        default: {
          name: '编码规则',
          width: 1200,
          displayKey: 'name',
          returnKeys: ['id', 'id'],
          queryParamText: '编码名称',
        }
      }
    },
    computed: {
      attrs() {
        return Object.assign(this.default, this.$attrs)
      }
    }
  }
</script>

<style lang="scss" scoped></style>