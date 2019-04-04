<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar1" id="toolbar">
            <el-form :inline="true" :model="filters" class="filter">
                <el-form-item label="缴费月份" size="medium" class="date">
                    <el-date-picker clearable
                                    value-format="yyyy-MM"
                                    v-model="filters.date"
                                    type="month"
                                    @change="searchData"
                                    placeholder="选择月份">
                    </el-date-picker>
                </el-form-item>
                <el-form-item size="medium">
                    <!--<el-button type="primary" @click="searchData">-->
                        <!--<i class="el-icon-search"></i>-->
                        <!--查询-->
                    <!--</el-button>-->
                    <a :href="`${$url}/payrecords/paycountsexport?date=${filters.date}&rzdm=${$rzdm}`">
                        <el-button type="primary" >
                            <i class="el-icon-upload2"></i>
                            导出
                        </el-button>
                    </a>

                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="records" v-loading="listLoading" style="width: 100%;overflow: auto; font-size: 14px;">
            <el-table-column type="index" width="55" fixed></el-table-column>
            <el-table-column prop="name" label="公司名称" show-overflow-tooltip width="250" fixed></el-table-column>
            <el-table-column prop="cls" label="车辆数" ></el-table-column>
            <el-table-column prop="ysje" label="应缴费金额"></el-table-column>
            <el-table-column prop="yjje" label="已缴金额"></el-table-column>
            <el-table-column prop="xjje" label="现金金额"></el-table-column>
            <el-table-column prop="fxjje" label="非现金金额"></el-table-column>
            <el-table-column prop="qfje" label="欠费金额"></el-table-column>
            <el-table-column prop="pay_date" label="缴费月份" ></el-table-column>
        </el-table>

    </section>
</template>

<script>

    export default {
        data() {
            return {
                nowMonth: this.$util.formatDate.format(new Date(),'yyyy-MM'),
                records:[],//数据总数
                filters: {
                    date: ''
                },
                listLoading: false,
            }
        },
        created(){
            this.filters.date = this.nowMonth;
            this.getData();
        },
        methods: {
            searchData(){
                this.getData()
            },
            //导出数据
            exportEXCEL(){
                let url = this.$url + '/payrecords/paycountsexport';
                let paramExcel = {
                    date: this.filters.date,
                    rzdm: this.$rzdm
                };
                this.$axios.post(url, this.$qs.stringify(paramExcel)).then((res) => {
                    let data = res.data;
                    if (data.resCode == 200) {
                        location.href = this.$url + '/' + data.data;
                    } else {
                        this.$message.error(res.data.resMsg);
                    }
                });
            },
            //获取数据
            getData(filters) {
                this.listLoading = true;
                let url = this.$url + '/payrecords/getpaycounts';
                let param = {
                    rzdm: this.$rzdm
                };
                param = Object.assign(param, this.filters);
                this.$axios.post(url, this.$qs.stringify(param)).then((res) => {
                    let data = res.data;
                    if (data.resCode == 200) {
                        let data2 = data.data;
                        this.records = data2;
                        this.total = data2.totalElements;
                        this.listLoading = false;
                    } else {
                        this.$message.error(res.data.resMsg);
                    }
                });
            },
        }
    }
</script>

<style scoped lang="scss">
    #toolbar .filter .date .el-date-editor.el-input, #toolbar .filter .date .el-date-editor.el-input__inner{
        width: 200px;
    }
    /*.account-form{*/
        /*.el-form-item{*/
            /*float: left;*/
            /*width:50%;*/
        /*}*/
    /*}*/

    .file{
        -webkit-appearance: none;
        background-color: #fff;
        background-image: none;
        border-radius: 4px;
        border: 1px solid #dcdfe6;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        color: #606266;
        display: inline-block;
        font-size: inherit;
        width: 100%;
        padding: 6px;
    }
</style>