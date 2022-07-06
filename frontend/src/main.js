import Vue from 'vue'
import App from './App.vue'
import {initRouter} from './router'
import './theme/index.less'
import Antd from 'ant-design-vue'
import Viser from 'viser-vue'
// import '@/mock'
import store from './store'
import 'animate.css/source/animate.css'
import Plugins from '@/plugins'
import {initI18n} from '@/utils/i18n'
import bootstrap from '@/bootstrap'
// import axios from 'axios'
import 'moment/locale/zh-cn'
// Vue.use(axios)
const router = initRouter(store.state.setting.asyncRoutes)
const i18n = initI18n('CN', 'US')

Vue.use(Antd)
Vue.config.productionTip = false
Vue.use(Viser)
Vue.use(Plugins)
window.XMLHttpRequest.withCredentials = true // 允许跨域
// axios.defaults.baseURL = 'http://114.218.158.78:14445';
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// Vue.prototype.$axios = axios // 全局使用接口axios

bootstrap({router, store, i18n, message: Vue.prototype.$message})

new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
}).$mount('#app')
