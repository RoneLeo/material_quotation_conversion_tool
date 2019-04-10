<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium">
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleImport">导入项目</el-button>
                    <el-button type="primary">
                        <a :href="$url+'/file/getModelFile?lx=1'" style="color: #fff;text-decoration: none">项目EXCEL模板</a>
                    </el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <!--列表-->
        <el-table size="medium" :data="tableData" v-loading="listLoading"
                  style="width: 100%;">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="wzxmmc" label="项目名称"></el-table-column>
            <el-table-column prop="bjdw" label="报价单位"></el-table-column>
            <el-table-column prop="ysf" label="运输费"></el-table-column>
            <el-table-column prop="jcf" label="检测费"></el-table-column>
            <el-table-column label="操作" width="300">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)">修改</el-button>
                    <el-button size="mini" @click="handleLook(scope.row)">材料详情</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--项目信息界面-->
        <el-dialog title="项目信息" :visible.sync="addFormVisible" :close-on-click-modal="false" width="40%" @closed="closeClear">
            <el-form :model="addForm" label-width="130px" ref="addForm">
                <el-form-item label="项目名称" prop="wzxmmc" :rules="[{ required: true, message: '项目名称不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.wzxmmc"></el-input>
                </el-form-item>
                <el-form-item label="excel项目名称" prop="xmmc">
                    <el-input v-model="addForm.xmmc"></el-input>
                </el-form-item>
                <el-form-item label="报价单位" prop="bjdw">
                    <el-input v-model="addForm.bjdw"></el-input>
                </el-form-item>
                <el-form-item label="运输费" prop="ysf">
                    <el-input v-model="addForm.ysf"></el-input>
                </el-form-item>
                <el-form-item label="第三方检测费" prop="jcf">
                    <el-input v-model="addForm.jcf"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="bz">
                    <el-input
                            type="textarea"
                            :autosize="{ minRows: 2, maxRows: 4}"
                            placeholder="请输入内容"
                            v-model="addForm.bz">
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="导入项目" :visible.sync="importFormShow" :close-on-click-modal="false" @closed="closeClear" width="40%">
            <el-form label-width="120px" ref="importForm">
                <el-form-item label="项目名称">
                    <el-input v-model="importForm.xmmc"></el-input>
                </el-form-item>
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
                importForm: {},
                tableData: [],
                importFormShow: false,
                listLoading: false,
                addFormVisible: false,
                addLoading: false,
                //新增界面数据
                addForm: {},
                file: {},
            }
        },
        created(){
            this.getData();
        },
        methods: {
            handleLook(row) {
                this.$router.push({path: '/UpdateMaterials', query: {xmbh: row.id}});
            },
            handleEdit(row) {
                this.addFormVisible = true;
                this.addForm = Object.assign({}, row)
            },
            handleDelete(row) {
                this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post('/project/del', {id: row.id}).then(res => {
                        this.$message({type: 'success', message: '删除成功!'});
                        this.getData();
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            closeClear() {
                this.addLoading = false;
            },
            handlerUpload(e) {
                this.file = e.target.files[0];
            },
            importSave() {
//                console.log(this.importForm, this.file)
                let param = new FormData();
                param.append('xmmc', this.importForm.xmmc)
                param.append('file', this.file)
                let config = {
                    headers:{'Content-Type':'multipart/form-data'}
                }
                this.$axios.post('/projectdata/importExcel', param, config ).then(res => {
                    this.$message(res.data);
                    this.getData();
                    this.importFormShow = false;
                })
            },
            //获取数据
            getData() {
                this.listLoading = true;
                this.$axios.get('/project/findAll').then((res) => {
                    this.listLoading = false;
                    this.tableData = res.data;
                });
            },
            //显示新增界面
            handleImport: function () {
                this.importFormShow = true;
                this.importForm = {};
            },
            //提交
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$axios.post('/project/update', Object.assign({}, this.addForm)).then((res) => {
                            this.$message.success('修改成功!');
                            this.getData();
                            this.addFormVisible = false;
                            this.$refs.addForm.resetFields()
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