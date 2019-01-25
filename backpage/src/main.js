import Vue from 'vue'
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'
import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import {default as api} from './utils/api'
import {hasPermission} from "./utils/hasPermission"
import 'babel-polyfill'
import VueDirectiveImagePreviewer from 'vue-directive-image-previewer'
import 'vue-directive-image-previewer/dist/assets/style.css'
import VueAMap from 'vue-amap';

import VueQuillEditor from 'vue-quill-editor'

// require styles

Vue.use(VueQuillEditor)

Vue.use(VueAMap);
Vue.use(VueDirectiveImagePreviewer)

import VCharts from 'v-charts'
Vue.use(VCharts)

VueAMap.initAMapApiLoader({
  // 高德的key
  key: '0751c3fc674a7bf5bbebb73d3f19e43b',
  // 插件集合
  plugin: ['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PolyEditor', 'AMap.CircleEditor'],
  // 高德 sdk 版本，默认为 1.4.4
  v: '1.4.4'
});


require('es6-promise').polyfill()
Vue.use(ElementUI, {locale})
Vue.prototype.api = api
//全局的常量
Vue.prototype.hasPerm = hasPermission
//生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = (process.env.NODE_ENV != 'production')


new Vue({
  el: '#app',
  router,
  store,
  VueAMap,
  template: '<App/>',
  components: {App}
})
