import Login from './views/Login.vue'
import Error from './views/error.vue'
import Home from './views/Home.vue'
import Discount from './views/Discount.vue'
import Projects from './views/Projects.vue'
import Materials from './views/Materials.vue'
import UpdateMaterials from './views/UpdateMaterials.vue'




import User from './views/User.vue'
import Summary from './views/Summary.vue'

import Company from './views/Company.vue'
import Check from './views/Check.vue'

import Search from './views/Search.vue'
import Account from './views/Account.vue'
import ChangePassword from './views/ChangePassword.vue'

//limit 1-分公司,0-总公司

let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        iconCls: 'fa fa-search',
        name: '',
        hidden: true,
        leaf: true,
        children: [
            { path: '/error', component: Error, name: 'Error' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        iconCls: 'fa fa-search',
        limit: 1,
        leaf: true,
        children: [
            { path: '/Discount', component: Discount, name: '查询/计算' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        limit: 0,
        iconCls: 'fa fa-address-card',
        leaf: true,
        children: [
            { path: '/Projects', component: Projects, name: '项目管理' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        limit: 0,
        iconCls: 'fa fa-th-list',
        leaf: true,
        children: [
            { path: '/Materials', component: Materials, name: '材料价目管理' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        limit: 0,
        hidden: true,
        iconCls: 'fa fa-th-list',
        leaf: true,
        children: [
            { path: '/UpdateMaterials', component: UpdateMaterials, name: '项目材料' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        limit: 1,
        iconCls: 'fa fa-key',
        leaf: true,
        children: [
            { path: '/ChangePassword', component: ChangePassword, name: '修改密码' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        limit: 0,
        iconCls: 'fa fa-user',
        leaf: true,
        children: [
            { path: '/User', component: User, name: '用户管理' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        iconCls: 'fa fa-search',
        limit: 1,
        leaf: true,
        children: [
            { path: '/Search', component: Search, name: '查询缴费' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        limit: 1,
        iconCls: 'fa fa-address-card',
        leaf: true,//只有一个节点
        children: [
            { path: '/Account', component: Account, name: '缴费信息' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        iconCls: 'fa fa-check',
        limit: 0,
        hidden: true,
        leaf: true,
        children: [
            { path: '/Check', component: Check, name: '审核记录' }
        ]
    },

    {
        path: '/',
        component: Home,
        name: '',
        iconCls: 'fa fa-home',
        limit: 0,
        hidden: true,
        leaf: true,
        children: [
            { path: '/Company', component: Company, name: '公司管理' }
        ]
    },

    {
        path: '/',
        component: Home,
        name: '',
        limit: 1,
        hidden: true,
        iconCls: 'fa fa-th-list',
        leaf: true,
        children: [
            { path: '/summary', component: Summary, name: '月度报表' }
        ]
    },


];

export default routes;