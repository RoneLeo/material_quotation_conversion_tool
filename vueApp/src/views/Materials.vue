<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium">
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleImport">导入可用材料</el-button>
                    <el-button type="primary">
                        <a :href="$url+'/file/getModelFile?lx=2'" style="color: #fff;text-decoration: none">材料EXCEL模板</a>
                    </el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <!--列表-->
        <el-table size="medium" :data="tableData" v-loading="listLoading"
                  style="width: 100%;">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="clmc" label="材料名称" width="280"></el-table-column>
            <el-table-column prop="clgg" label="材料规格"></el-table-column>
            <el-table-column prop="cldw" label="材料单位"></el-table-column>
            <el-table-column prop="cbj" label="成本价"></el-table-column>
            <el-table-column prop="jj" label="基价"></el-table-column>
            <el-table-column label="操作" width="220">
                <template slot-scope="scope">
                    <el-button size="mini" @click="updatePrice(scope.row)">修改价目</el-button>
                    <el-button size="mini" type="danger" @click="deleteMaterial(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--新增界面-->
        <el-dialog title="修改材料价目" :visible.sync="addFormVisible" :close-on-click-modal="false" width="40%">
            <el-form :model="addForm" ref="addForm" label-width="120px">
                <el-form-item label="材料名称">
                    <span style="font-size: 16px;">{{addForm.clmc}}</span>
                </el-form-item>
                <el-form-item label="材料规格">
                    <span style="font-size: 16px;">{{addForm.clgg}}</span>
                </el-form-item>
                <el-form-item label="材料单位">
                    <span style="font-size: 16px;">{{addForm.cldw}}</span>
                </el-form-item>
                <el-form-item label="成本价">
                    <el-input-number v-model="addForm.cbj" :min="0.00" :step="0.01" :precision="2"  label="成本价" size="small" style="width: 180px"></el-input-number>
                </el-form-item>
                <el-form-item label="基价">
                    <el-input-number v-model="addForm.jj" :min="0.00" :step="0.01" :precision="2"  label="基价" size="small" style="width: 180px"></el-input-number>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="导入可用材料" :visible.sync="importFormShow" :close-on-click-modal="false" width="40%">
            <el-form label-width="120px" ref="importForm">
                <el-form-item label="文件">
                    <input type="file" id="file" @change="handlerUpload">
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="importFormShow = false">取消</el-button>
                <el-button type="primary" @click.native="importSave" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    export default {
        data() {
            return {
                //公司代码修改后同步修改此处,此代码作为管理员权限代码
                tableData: [],
                listLoading: false,
                addFormVisible: false,//
                resetPassword: false,//
                addLoading: false,
                //新增界面数据
                addForm: {},
                importFormShow: false,
            }
        },
        created(){
            this.getData();
        },
        methods: {
            updatePrice(row) {
                this.addFormVisible = true;
                this.addForm = Object.assign({}, row);
            },
            deleteMaterial(row) {
                this.$confirm('此操作将为本项目删除该材料, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post('/material/deleteOneById', {clid: row.clid}).then((res) => {
                        this.$message({type: 'success', message: res.resMsg});
                        this.getData();
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleImport() {
                this.importFormShow = true;
            },
            importSave() {
                let param = new FormData();
                param.append('file', this.file)
                let config = {
                    headers:{'Content-Type':'multipart/form-data'}
                }
                this.$axios.post('/material/fileImport', param, config ).then(res => {
                    this.$message(res.data.data);
                    this.getData();
                    this.importFormShow = false;
                })
            },
            handlerUpload(e) {
                this.file = e.target.files[0];
            },
            formatterJS(row){
                return row.js == 0 ? '普通用户' : '管理员';
            },
            //获取数据
            getData() {
                this.listLoading = true;
                this.$axios.get('/material/findAll').then((res) => {
                    this.listLoading = false;
                    this.tableData = res.data;
                    this.tableData.forEach(item => {
                        item.cbj = item.cbj.toFixed(2);
                        item.jj = item.jj.toFixed(2);
                    })
                });
            },
            //显示新增界面
            handleAdd: function () {
                this.passwordShow = this.addFormVisible = true;
                this.addForm = {};
            },
            //提交
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$axios.post('/material/updateOne', {clid: this.addForm.clid, jj: this.addForm.jj, cbj: this.addForm.cbj}).then((res) => {
                            this.$message.success(res.resMsg);
                            this.getData();
                            this.addFormVisible = false;
                        });
                    }
                });
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