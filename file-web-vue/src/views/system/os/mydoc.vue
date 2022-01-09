<template>
  <iframe-page-view :id="mydoc" :src='url'></iframe-page-view>
</template>

<script type="text/ecmascript-6">
  import IframePageView from '@/components/IframePageView'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  export default {
    name: '',
    components: {
      IframePageView
    },
    data () {
      return {
        mydoc: 'doc1',
        url: process.env.VUE_APP_API_BASE_URL + '/ui/index?token=' + Vue.ls.get(ACCESS_TOKEN)
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
