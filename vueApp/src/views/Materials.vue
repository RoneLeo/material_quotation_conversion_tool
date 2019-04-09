<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium">
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleImport">导入可用材料</el-button>
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
                <template slot-scope="scope">
                    <!--<el-button size="mini" @click="">编辑</el-button>-->
                    <el-button size="mini" @click="">修改价目</el-button>
                    <el-button size="mini" type="danger" @click="">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--新增界面-->
        <el-dialog title="用户信息" :visible.sync="addFormVisible" :close-on-click-modal="false" width="40%">
            <el-form :model="addForm" label-width="120px" ref="addForm">
                <el-form-item
                        label="账号"
                        prop="zh"
                        :rules="[{ required: true, message: '账号号不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.zh"></el-input>
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

        <el-dialog title="导入可用材料" :visible.sync="importFormShow" :close-on-click-modal="false">
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
                });
            },
            //显示新增界面
            handleAdd: function () {
                this.passwordShow = this.addFormVisible = true;
                this.addForm = {};
            },
            //提交
            addSubmit: function () {
                console.log(this.addForm)
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$axios.post('/user/add', this.addForm).then((res) => {
                            this.$message.success('添加成功!');
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