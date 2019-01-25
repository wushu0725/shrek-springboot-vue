import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/user',
    component: Layout,
    redirect: '/user/role',
    name: '用户权限',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '', name: '用户列表', component: _import('user/user'), meta: {title: '用户列表', icon: 'user'}, menu: 'user'
      },
      {
        path: 'webchattxll', name: '微信通讯录', component: _import('user/webchattxll'), meta: {title: '微信通讯录', icon: 'user'}, menu: 'webchattxll'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
      {
        path: 'org',
        name: '组织管理',
        component: _import('user/org'),
        meta: {title: '组织管理', icon: 'peoples'},
        menu: 'org'
      },
      {
        path: 'webchatconfig', name: '微信参数', component: _import('user/webchatConfig'), meta: {title: '微信参数', icon: 'user'}, menu: 'org'
      },
      {
        path: 'student', name: '学生信息', component: _import('student/index'), meta: {title: '学生信息', icon: 'user'}, menu: 'org'
      },
      {
        path: 'codegen', name: '代码生成', component: _import('user/codegen'), meta: {title: '代码生成', icon: 'user'}, menu: 'org'
      }
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]
