<template>
  <a-list
    size="large"
    rowKey="id"
    :loading="loading"
    itemLayout="vertical"
    :dataSource="data"
  >
    <a-list-item :key="item.id" slot="renderItem" slot-scope="item">
      <a-list-item-meta>
        <a slot="title" href="https://vue.ant.design/">{{ item.title }}</a>
        <template slot="description">
          <span>
            <a-tag>Ant Design</a-tag>
            <a-tag>设计语言99999</a-tag>
            <a-tag>蚂蚁金服</a-tag>
          </span>
        </template>
      </a-list-item-meta>
      <article-list-content :description="item.description" :owner="item.owner" :avatar="item.avatar" :href="item.href" :updateAt="item.updatedAt" />
    </a-list-item>
    <div slot="footer" v-if="data.length > 0" style="text-align: center; margin-top: 16px;">
      <a-button @click="loadMore" :loading="loadingMore">加载更多</a-button>
    </div>
  </a-list>
</template>

<script>
import { ArticleListContent } from '@/components'

export default {
  name: 'Article',
  components: {
    ArticleListContent
  },
  data () {
    return {
      loading: true,
      loadingMore: false,
      data: []
    }
  },
  mounted () {
    this.getList()
  },
  methods: {
    getList () {
      this.data = [
        {
          updatedAt: '2021-10-01 12:00:00',
          title: 'gallop',
          owner: 'gallop',
          description: 'gallop 开发团队',
          href: 'https://www.mygallop.cn',
          avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ZiESqWwCXBRQoaPONSJe.png',
          name: 'gallop',
          id: 'member1'
        },
        {
          updatedAt: '2021-07-01 12:00:00',
          title: 'gallop',
          owner: 'gallop',
          description: 'gallop 开发团队',
          href: 'https://www.mygallop.cn',
          avatar: 'https://gw.alipayobjects.com/zos/rmsportal/tBOxZPlITHqwlGjsJWaF.png',
          name: '小君君',
          id: 'member2'
        }
      ]
      this.loading = false
    },
    loadMore () {
      this.loadingMore = false
    }
  }
}
</script>

<style scoped>

</style>
