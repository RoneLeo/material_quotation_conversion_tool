<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium">
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleAdd">添加材料</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <!--列表-->
        <el-table size="medium" :data="tableData" v-loading="listLoading"
                  style="width: 100%;">
            <el-table-column prop="clmc" label="材料名称" width="280"></el-table-column>
            <el-table-column prop="clgg" label="材料规格"></el-table-column>
            <el-table-column prop="cldw" label="材料单位"></el-table-column>
            <el-table-column prop="clsl" label="材料数量"></el-table-column>
            <el-table-column prop="cbj" label="成本价"></el-table-column>
            <el-table-column prop="jj" label="基价"></el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope" v-if="scope.row.zh !== 'admin'">
                    <!--<el-button size="mini" @click="">编辑</el-button>-->
                    <el-button size="mini" @click="">修改</el-button>
                    <el-button size="mini" type="danger" @click="">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="添加材料" :visible.sync="addFormVisible" :close-on-click-modal="false" width="40%">
            <el-form label-width="120px" ref="addForm">
                <el-form-item label="快速选择">
                    <el-select v-model="selectedMaterial" placeholder="请选择" style="width: 100%" clearable>
                        <el-option
                                v-for="item in materials"
                                :key="item.id"
                                :label="item.clmc"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="密码"
                              prop="mm"
                              :rules="[{ required: true, message: '密码不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.mm"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="js"
                              :rules="[{ required: true, message: '密码不能为空', trigger: 'blur' }]">
                    <el-select v-model="addForm.js" style="width:100%;">
                        <el-option label="普通用户" value="0"></el-option>
                        <el-option label="系统用户" value="1"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--<el-dialog title="重置密码" :visible.sync="resetPassword" :close-on-click-modal="false">-->
            <!--<el-form label-width="120px" ref="addForm">-->
                <!--<el-form-item-->
                        <!--label="账号">-->
                    <!--<el-input v-model="addForm.zh" readonly></el-input>-->
                <!--</el-form-item>-->
                <!--<el-form-item label="密码">-->
                    <!--<el-input v-model="newPassword"></el-input>-->
                <!--</el-form-item>-->
            <!--</el-form>-->
            <!--<div slot="footer" class="dialog-footer">-->
                <!--<el-button @click.native="resetPassword = false">取消</el-button>-->
                <!--<el-button type="primary" @click.native="resetPwdSubmit" :loading="addLoading">提交</el-button>-->
            <!--</div>-->
        <!--</el-dialog>-->
    </section>
</template>

<script>
    export default {
        data() {
            return {
                xmbh: 0,
                tableData: [],
                listLoading: false,
                addFormVisible: false,//
                resetPassword: false,//
                addLoading: false,
                //新增界面数据
                addForm: {},
                materials: [],
                selectedMaterial:''
            }
        },
        created(){
            this.xmbh = this.$route.query.xmbh;
            this.getData();
            this.$axios.get('/material/findAll').then(res => {
                this.materials = res.data;
            })
        },
        methods: {
            formatterJS(row){
                return row.js == 0 ? '普通用户' : '管理员';
            },
            //获取数据
            getData() {
                this.listLoading = true;
                this.$axios.post('/projectdata/findAllByXmbh', {xmbh: this.xmbh}).then((res) => {
                    this.listLoading = false;
                    this.tableData = res.data;
                });
            },
            //显示新增界面
            handleAdd: function () {
                this.passwordShow = this.addFormVisible = true;
                this.addForm = {};
            },
            //提交
            addSubmit: function () {
                console.log(this.selectedMaterial)
//                console.log(this.addForm)
//                this.$refs.addForm.validate((valid) => {
//                    if (valid) {
//                        this.$axios.post('/user/add', this.addForm).then((res) => {
//                            this.$message.success('添加成功!');
//                            this.getData();
//                            this.addFormVisible = false;
//                            this.$refs.addForm.resetFields()
//                        });
//                    }
//                });
            },
            resetPwd(index, row){
                this.addForm = Object.assign({}, row); //合并对象防止改动页面内容
                this.addForm.mm = '';
                this.resetPassword = true;
            },
            resetPwdSubmit(){
                let param = {
                    id: this.addForm.id,
                    newword: this.newPassword,
                    rzdm: this.$rzdm
                };
                let url = this.$url + '/user/changepassword';
                this.$axios.post(url, this.$qs.stringify(param)).then(res => {
                    if (res.data.resCode == 200) {
                        this.getData()
                        this.resetPassword = false;
                        this.$message({
                            message: '密码修改成功,请牢记!',
                            type: 'success'
                        });
                    } else {
                        this.$message.error(res.data.resMsg);
                    }
                })
            }
        },
    }
</script>

<style>
    /*.el-input__inner, .el-textarea__inner {*/
    /*color: #707276;*/
    /*font-weight: normal;*/
    /*}*/
    /*.el-textarea__inner {*/
    /*font-size: 16px;*/
    /*}*/
    /*.el-input.is-disabled .el-input__inner, .el-textarea.is-disabled .el-textarea__inner {*/
    /*background-color: #FAFAFA;*/
    /*border-color: #DCDFE6;*/
    /*color: #707276;*/
    /*}*/

    .file-dialog .content {
        width: 100%;
        height: 800px;
        background: #cccccc;
    }

    .el-dialog--small {
        width: 80%;
    }
</style>