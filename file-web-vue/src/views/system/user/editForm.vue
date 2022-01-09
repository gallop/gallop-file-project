/* eslint-disable vue/no-template-shadow */
<template>
  <a-modal
    title="编辑用户"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-divider orientation="left">基本信息</a-divider>
      <a-row :gutter="24">
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              style="display: none;"
            >
              <a-input v-decorator="['id']" />
            </a-form-item>
            <a-form-item
              label="账号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input placeholder="请输入账号" v-decorator="['username', {rules: [{required: true, min: 5, message: '请输入至少五个字符的账号！'}]}]" />
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="昵称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input placeholder="请输入昵称" v-decorator="['nickName']" />
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="性别"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-radio-group v-decorator="['sex',{rules: [{ required: true, message: '请选择性别！' }]}]" >
                <a-radio :value="1">男</a-radio>
                <a-radio :value="2">女</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="生日"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-date-picker placeholder="请选择生日" @change="onChange" style="width: 100%" v-decorator="['birthday']" />
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="手机号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input placeholder="请输入手机号" v-decorator="['phone',{rules: [{ required: true, message: '请输入手机号！' }]}]" />
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="邮箱"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input placeholder="请输入邮箱" v-decorator="['email']" />
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-spin>
  </a-modal>
</template>
<script>
  import { sysUserEdit } from '@/api/modular/system/userManage'
  import moment from 'moment'
  export default {
    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        count: 1,
        visible: false,
        confirmLoading: false,
        memberLoading: false,
        form: this.$form.createForm(this),
        data: [],
        birthdayString: ''
      }
    },
    methods: {
      // 初始化方法
      edit (record) {
        this.confirmLoading = true
        this.visible = true
        // 基本信息加人表单
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              id: record.id,
              username: record.username,
              nickName: record.nickName,
              sex: record.sex,
              email: record.email,
              phone: record.phone
            }
          )
        }, 100)
        // 时间单独处理
        if (record.birthday != null) {
          this.form.getFieldDecorator('birthday', { initialValue: moment(record.birthday, 'YYYY-MM-DD') })
        }
        this.birthdayString = moment(record.birthday).format('YYYY-MM-DD')
        this.confirmLoading = false
      },
      /**
       * 选择子表单单项触发
       */
      handleChange (value, key, column) {
        const newData = [...this.data]
        const target = newData.find(item => key === item.key)
        if (target) {
          target[column] = value
          this.data = newData
        }
      },
      /**
       * 日期需单独转换
       */
      onChange (date, dateString) {
        this.birthdayString = moment(date).format('YYYY-MM-DD')
      },
      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            // eslint-disable-next-line eqeqeq
            if (this.birthdayString == 'Invalid date') {
              this.birthdayString = null
            }
            values.birthday = this.birthdayString
            sysUserEdit(values).then((res) => {
              if (res.success) {
                this.$message.success('编辑成功')
                this.confirmLoading = false
                this.$emit('ok', values)
                this.handleCancel()
              } else {
                this.$message.error('编辑失败：' + res.message)
              }
            }).finally((res) => {
              this.confirmLoading = false
            })
          } else {
            this.confirmLoading = false
          }
        })
      },
      handleCancel () {
        this.form.resetFields()
        this.visible = false
        // 清理子表单中数据
        this.data = []
        // 清理时间
        this.birthdayString = ''
        this.form.getFieldDecorator('birthday', { initialValue: null })
      }
    }
  }
</script>
