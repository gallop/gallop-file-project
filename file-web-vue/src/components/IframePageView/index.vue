
<template>

    <iframe
      :id='id'
      :src='url'
      frameborder='0'
      width='100%'
      height='100%'
      scrolling='auto'></iframe>

</template>

<script>
// import Vue from 'vue'
// import { ACCESS_TOKEN } from '@/store/mutation-types'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
// import PageLayout from '../page/PageLayout'
// import RouteView from './RouteView'

export default {
  name: 'IframePageContent',
  // inject: ['closeCurrent'],
  props: {
    src: {
      required: true,
      type: String
    },
    id: {
      required: true,
      type: String
    }
  },
  data () {
    return {
      url: ''
    }
  },
  created () {
    this.goUrl()
  },
  updated () {
    this.goUrl()
  },
  watch: {
    $route(to, from) {
      this.goUrl()
    }
  },
  methods: {
    goUrl () {
      const url = this.src // this.$route.meta.url
      const id = this.id // this.$route.path
      // this.id = id
      // url = 'http://www.baidu.com'
      console.log('------url------' + url)
      console.log('------id------' + id)
      if (url !== null && url !== undefined) {
        // -----------------------------------------------------------------------------------------
        // url支持通过 ${token}方式传递当前登录TOKEN
        // eslint-disable-next-line no-template-curly-in-string
        const tokenStr = '${token}'
        if (url.indexOf(tokenStr) !== -1) {
          const token = Vue.ls.get(ACCESS_TOKEN)
          console.log(' token=' + token)
          this.url = url.replace(tokenStr, token)
        } else {
          this.url = url
        }
        console.log('after url=' + this.url)
        // -----------------------------------------------------------------------------------------

        // *update_begin author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
        if (this.$route.meta.internalOrExternal !== undefined && this.$route.meta.internalOrExternal === true) {
          // this.closeCurrent()
          window.open(this.url)
        }
        // *update_end author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
      }
    }
  }
}
</script>

<style>
</style>
