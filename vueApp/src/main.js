import babelpolyfill from 'babel-polyfill'
import ElementUI from 'element-ui'
import { Message } from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import Vue from 'vue'
import App from './App'

//import 'element-ui/lib/theme-default/index.css'
import './assets/theme/element-#444547/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
import axios from 'axios'

import qs from 'qs'
import 'nprogress/nprogress.css'
import routes from './routes'
import 'font-awesome/css/font-awesome.min.css'
import util from './common/js/util'
import VueNumberInput from '@chenfengyuan/vue-number-input';

Vue.use(VueNumberInput);

Vue.prototype.$util = util;
Vue.prototype.$qs = qs;
Vue.prototype.$axios = axios;
Vue.prototype.$url = process.env.NODE_ENV == 'development' ? 'http://182.151.22.247:9007' : location.origin;
// Vue.prototype.$url = 'http://36.111.191.34:8088';
Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.use(Vuex);

const router = new VueRouter({
    routes
})

axios.defaults.baseURL = process.env.NODE_ENV == 'development' ? 'http://182.151.22.247:9007' : location.origin;
axios.defaults.withCredentials = true;

// http request 拦截器（所有发送的请求都要从这儿过一次）
axios.interceptors.request.use(
    config => {
        if(!config.headers.hasOwnProperty('Content-Type')) {
            if(config.data){
                config.data = qs.stringify(config.data); //处理参数格式
            }
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
// http response 拦截器（所有接收到的请求都要从这儿过一次）
axios.interceptors.response.use(
    response => {
        let status = response.status;
        let statusText = response.statusText;
        if(status == 200){
            let data = response.data;
            if(data.resCode == -1){
                Message.error(data.resMsg);
            }
            else if(data.resCode == 100){
                Message.error('未登录');
                router.push('./login')
            }
            else{
                return response.data;
            }
        }else{
            Message.error('服务器错误:' + status + ':' + statusText);
        }
    },
    error => {
        let errData = error.response.data;
        let status = errData.status;
        let path = errData.path;
        console.log('code: '+ status + ', path: ' + path);
        return Promise.reject(error.response.data)
    }
);




router.beforeEach((to, from, next) => {
  if (to.path == '/login') {
    sessionStorage.removeItem('userInfo');
  }
  let user = JSON.parse(sessionStorage.getItem('userInfo'));
  if (!user && to.path != '/login') {
      next({ path: '/login' })
  }else {
      next();
      if(user){
          Vue.prototype.$rzdm = user.code; //登录成功后添加全局认证码
      }
  }
});



//router.afterEach(transition => {
//NProgress.done();
//});

new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  //components: { App }
  render: h => h(App)
}).$mount('#app')

