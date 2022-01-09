<template>
  <a-modal
    title="用户增加"
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
              label="密码"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input
                placeholder="请输入密码"
                type="password"
                v-decorator="['password', {rules: [{required: true, message: '请输入密码！'},{
                  validator: validateToNextPassword,
                },]}]" />
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="12" :sm="24">
          <a-form :form="form">
            <a-form-item
              label="重复密码"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              has-feedback
            >
              <a-input
                placeholder="请再次输入密码"
                type="password"
                v-decorator="['confirm', {rules: [{required: true, message: '请再次输入密码！'},
                                                  {
                                                    validator: compareToFirstPassword,
                                                  }]}]" />
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
  import { sysUserAdd } from '@/api/modular/system/userManage'
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
        birthdayString: []
      }
    },
    methods: {
      // 初始化方法
      add () {
        this.visible = true
      },
      compareToFirstPassword (rule, value, callback) {
        const form = this.form
        if (value && value !== form.getFieldValue('password')) {
          // eslint-disable-next-line standard/no-callback-literal
          callback('请确认两次输入密码的一致性！')
        } else {
          callback()
        }
      },
      validateToNextPassword (rule, value, callback) {
        const form = this.form
        if (value && this.confirmDirty) {
          form.validateFields(['confirm'], { force: true })
        }
        callback()
      },
      /**
       * 日期需单独转换
       */
      onChange (date, dateString) {
        if (date == null) {
          this.birthdayString = []
        } else {
          this.birthdayString = moment(date).format('YYYY-MM-DD')
        }
      },
      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            if (this.birthdayString.length > 0) {
              values.birthday = this.birthdayString
            }
            sysUserAdd(values).then((res) => {
              if (res.success) {
                this.$message.success('新增成功')
                this.confirmLoading = false
                this.$emit('ok', values)
                this.handleCancel()
              } else {
                this.$message.error('新增失败：' + res.message)
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
        this.birthdayString = []
      }
    }
  }
</script>
