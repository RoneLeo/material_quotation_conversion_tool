<template>
    <el-row class="container">
        <el-col :span="24" class="header">
            <el-col :span="12" class="logo">
                {{collapsed?'':sysName}}
            </el-col>
            <el-col :span="12" class="userinfo">
                <el-dropdown trigger="click" size="medium">
					<span class="el-dropdown-link userinfo-inner">
                        <i class="fa fa-user"></i>
						{{sysUserName}},
                        {{sysgGsmc}}
                    </span>
                    <el-dropdown-menu slot="dropdown" >
                        <el-dropdown-item @click.native="changePassWord">修改密码</el-dropdown-item>
                        <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-col>
        </el-col>
        <el-col :span="24" class="main">
            <aside :class="collapsed?'menu-collapsed':'menu-expanded'">
                <!--导航菜单-->
                <el-menu :default-active="$route.path" class="el-menu-vertical-demo"
                         unique-opened router v-show="!collapsed">
                    <template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
                        <el-submenu :index="index+''" v-if="!item.leaf">
                            <template slot="title"><i :class="item.iconCls"></i>{{item.name}}</template>
                            <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path"
                                          v-if="!child.hidden">{{child.name}}
                            </el-menu-item>
                        </el-submenu>
                        <!--单个节点菜单-->
                        <el-menu-item style="font-size: 18px;" v-if="limit <= item.limit && item.leaf && item.children.length>0" :index="item.children[0].path" >
                            <i :class="item.iconCls"></i> {{item.children[0].name}}
                        </el-menu-item>

                    </template>
                </el-menu>

                <!--导航菜单-折叠后-->
                <ul class="el-menu el-menu-vertical-demo collapsed" v-show="collapsed" ref="menuCollapsed">
                    <template v-for="(item,index) in $router.options.routes" >
                        <template v-if="!item.leaf">
                            <li v-if="!item.hidden" class="el-submenu item" style="font-size: 18px;">
                                <div class="el-submenu__title" style="padding-left: 20px;font-size: 18px;" @mouseover="showMenu(index,true)"
                                     @mouseout="showMenu(index,false)"><i :class="item.iconCls"></i></div>
                                <ul class="el-menu submenu" style="font-size: 18px;" :class="'submenu-hook-'+index" @mouseover="showMenu(index,true)"
                                    @mouseout="showMenu(index,false)">
                                    <li v-for="child in item.children" v-if="!child.hidden" :key="child.path"
                                        class="el-menu-item" style="padding-left: 40px;font-size: 18px;"
                                        :class="$route.path==child.path?'is-active':''" @click="$router.push(child.path)">
                                        {{child.name}}
                                    </li>
                                </ul>
                            </li>
                        </template>
                        <template v-else>
                            <li class="el-submenu">
                                <div class="el-submenu__title el-menu-item"
                                     style="padding-left: 20px;font-size: 18px !important;height: 56px;line-height: 56px;padding: 0 20px;"
                                     :class="$route.path==item.children[0].path?'is-active':''"
                                     @click="$router.push(item.children[0].path)">
                                    <i :class="item.iconCls"></i>
                                </div>
                            </li>
                        </template>
                    </template>

                </ul>
            </aside>
            <section class="content-container">
                <div class="grid-content bg-purple-light">
                    <template v-if="$route.path !== '/Search'">
                        <el-col :span="24" class="breadcrumb-container">
                            <strong class="title">{{$route.name}}</strong>
                            <el-breadcrumb separator="/" class="breadcrumb-inner">
                                <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                                    {{ item.name }}
                                </el-breadcrumb-item>
                            </el-breadcrumb>
                        </el-col>
                    </template>

                    <el-col :span="24" class="content-wrapper">
                        <transition name="fade" mode="out-in">
                            <router-view></router-view>
                        </transition>
                    </el-col>
                </div>
            </section>
        </el-col>
    </el-row>
</template>

<script>
    export default {
        data() {
            return {
                limit: 0,   //权限
                sysName: '防护/防化/通风设备厂商报价工具软件',
                collapsed: false,
                sysUserName: '',
                sysgGsmc: '',
                company:{},
                userInfo:{}
            }
        },
        mounted() {
            let user = JSON.parse(sessionStorage.getItem('userInfo'));
            if(user) {
                this.sysUserName = user.zh;
            }else {
                this.$router.push('/login');
            }
        },
        methods: {
            //退出登录
            logout: function () {
                var _this = this;
                this.$confirm('确认退出吗?', '提示', {
                    //type: 'warning'
                }).then(() => {
                    sessionStorage.removeItem('user');
                    _this.$router.push('/login');
                }).catch(() => {

                });
            },
            //修改密码
            changePassWord: function () {
                this.$router.push('/ChangePassword');
            },
            //折叠导航栏
            collapse: function () {
                //this.collapsed=!this.collapsed;
            },
            showMenu(i, status) {
                this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
            },
        },

    }

</script>

<style scoped lang="scss">
    @import '~scss_vars';

    .container {
        position: absolute;
        top: 0px;
        bottom: 0px;
        width: 100%;
        .header {
            height: 60px;
            line-height: 60px;
            background: $color-primary;
            color: #fff;
            .userinfo {
                text-align: right;
                padding-right: 35px;
                float: right;
                .userinfo-inner {
                    font-size:20px;
                    cursor: pointer;
                    color: #fff;
                    img {
                        width: 40px;
                        height: 40px;
                        border-radius: 20px;
                        margin: 10px 0px 10px 10px;
                        float: right;
                    }
                }
            }
            .logo {
                //width:230px;
                height: 60px;
                font-size: 24px;
                padding-left: 20px;
                padding-right: 20px;
                /*border-color: rgba(238,241,146,0.3);*/
                /*border-right-width: 1px;*/
                /*border-right-style: solid;*/
                img {
                    width: 40px;
                    float: left;
                    margin: 10px 10px 10px 18px;
                }
                .txt {
                    color: #fff;
                }
            }
            .logo-width {
                width: 230px;
            }
            .logo-collapse-width {
                width: 60px
            }
            .tools {
                padding: 0px 23px;
                width: 14px;
                height: 60px;
                line-height: 60px;
                cursor: pointer;
            }
        }
        .main {
            display: flex;
            // background: #324057;
            position: absolute;
            top: 60px;
            bottom: 0px;
            overflow: hidden;
            aside {
                font-size: 18px;
                flex: 0 0 180px;
                width: 180px;
                // position: absolute;
                // top: 0px;
                // bottom: 0px;
                .el-menu {
                    height: 100%;
                }
                .collapsed {
                    width: 60px;
                    .item {
                        position: relative;
                    }
                    .submenu {
                        position: absolute;
                        top: 0px;
                        left: 60px;
                        z-index: 99999;
                        height: auto;
                        display: none;
                    }

                }
            }
            .menu-collapsed {
                font-size: 18px;
                flex: 0 0 60px;
                width: 60px;
            }
            .menu-expanded {
                font-size: 18px;
                flex: 0 0 180px;
                width: 180px;
            }
            .content-container {
                // background: #f1f2f7;
                flex: 1;
                // position: absolute;
                // right: 0px;
                // top: 0px;
                // bottom: 0px;
                // left: 230px;
                overflow-y: scroll;
                padding: 20px;
                .breadcrumb-container {
                    margin-bottom: 15px;
                    color: $color-primary;
                    font-size:16px;
                    .title {
                        width: 200px;
                        float: left;

                    }
                    .breadcrumb-inner {
                        float: right;
                        color: $color-primary;
                        font-size:16px;
                    }
                }
                .content-wrapper {
                    background-color: #fff;
                    box-sizing: border-box;
                }
            }
        }
    }
</style>