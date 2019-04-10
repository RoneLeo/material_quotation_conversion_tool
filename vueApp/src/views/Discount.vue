<template>
    <section>
        <div class="search-block">
            <h2 class="title">防护/防化/通风设备厂商报价工具软件</h2>
            <div class="search">
                <span id="clbh">
                    <el-select v-model="searchData.xmbh" placeholder="请选择项目" clearable>
                        <el-option v-for="item in projects"
                            :key="item.id"
                            :label="item.wzxmmc"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </span>
                <span id="time">
                    <el-input placeholder="请输入折扣数，如0.3" v-model="searchData.discount" @keyup.enter.native="search" clearable></el-input>
                </span>
                <el-button type="primary" icon="el-icon-search" @click.native="search">查询</el-button>
            </div>
        </div>
        <div class="result-block" v-show="resShow">
            <div>
                <span class="res-name">基价：</span>
                <span class="res-num">
                    <a :href="$url+'/projectdata/getexcelnew?xmbh='+ searchData.xmbh +'&discount='+ searchData.discount +'&lx=1'">¥{{resData.jj}}</a>
                </span>
            </div>
            <div>
                <span class="res-name">折算价：</span>
                <span class="res-num">
                    <a :href="$url+'/projectdata/getexcelnew?xmbh='+ searchData.xmbh +'&discount='+ searchData.discount +'&lx=0'">¥{{resData.zsj}}</a>
                </span>
            </div>
            <div>
                <span class="res-name">成本价：</span>
                <span class="res-num">
                    <a :href="$url+'/projectdata/getexcelnew?xmbh='+ searchData.xmbh +'&discount='+ searchData.discount +'&lx=2'">¥{{resData.cbj}}</a>
                </span>
            </div>
        </div>


    </section>
</template>
<script>
    export default {
        data() {
            return {
                searchData: {
                    xmbh: '',
                    discount: ''
                },
                projects: [],
                resData: {},
                resShow: false
            }
        },
        mounted() {
            this.$axios.get('/project/findAll').then(res => {
                this.projects = res.data;
            })
        },
        methods: {
            search() {
                this.$axios.post('/projectdata/discount', Object.assign({}, this.searchData)).then(res => {
                    this.resData = res.data;
                    this.resShow = true;
                })
            },
            gotoHome() {
                this.$router.push({path: '/Search'});
            },
            fresh() {
                window.location.reload();
                this.$router.push({path: '/Search'});
            },
        }
    }
</script>
<style lang="scss" scoped>
    .result-block {
        font-size: 18px;
        color: #333;
        display: flex;
        flex-direction: row;
        justify-content: center;
        /*align-items: center;*/
        margin-top: 50px;
        div {
            margin-left: 30px;
            .res-num a{
                font-size: 32px;
                color: #232323;
                font-weight: 500;
            }
        }
        div:first-child {
            margin-left: 0;
        }
    }
    .search-block {
        margin-top: 120px;
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .title {
            /*margin-top: 70px;*/
            font-size: 34px;
            font-weight: 500;
            letter-spacing: 3px;
            text-shadow: 0 0 3px #666;
            color: #F8D200;
            text-align: center;
        }

        .search {
            margin-top: 30px;
            .el-input {
                font-size: 18px;
                padding: 5px 0;
            }
            #clbh .el-input {
                width: 250px;
                margin-left: 50px;
            }
            #time .el-input {
                width: 350px;
            }
            .el-button {
                font-size: 15px;
                /*float:left;*/
            }
        }
    }


    .el-table td, .el-table th {
        padding: 9px 0;
    }

    .tips {
        margin-top: 5px;
        color: #ff544d;
        font-size: 18px;
        padding-left: 25px;
    }

</style>