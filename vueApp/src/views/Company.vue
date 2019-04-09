<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium" >
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleAdd">添加分公司</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <!--列表-->
        <el-table size="medium"  :data="tableData" v-loading="listLoading"  style="width: 100%;font-size: 14px;margin-top: 20px;">
            <el-table-column prop="gsmc" label="公司名称"></el-table-column>
            <el-table-column prop="gslx" label="公司类型" :formatter="formatterLx"></el-table-column>
            <el-table-column prop="gsxz" label="公司性质" :formatter="formatterXz"></el-table-column>

            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button v-if="scope.row.gslx == 1" size="mini" type="danger" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--新增界面-->
        <el-dialog title="公司信息" :visible.sync="addFormVisible"
                   @close="addFormCloseClear"
                   :close-on-click-modal="false" class="company-form" width="45%">
            <el-form :model="addForm" label-width="120px" ref="addForm">
                <el-form-item label="公司名称" prop="gsmc"
                              :rules="[{ required: true, message: '公司名称不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.gsmc"></el-input>
                </el-form-item>
                <el-form-item label="公司商户号"
                              prop="shh">
                    <el-input v-model="addForm.shh"></el-input>
                </el-form-item>
                <el-form-item label="商户签名"
                              prop="qm">
                    <el-input v-model="addForm.qm"></el-input>
                </el-form-item>
                <el-form-item label="公司性质"
                              prop="gsxz"
                              :rules="[{ required: true, message: '公司性质不能为空', trigger: 'blur' }]">
                    <el-select v-model="addForm.gsxz" style="width:100%;">
                        <el-option v-for="(item,index) in gsxz" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="公司类型"
                              prop="gslx"
                              :rules="[{ required: true, message: '公司类型不能为空', trigger: 'blur' }]">
                    <el-select v-model="addForm.gslx" style="width:100%;" disabled>
                        <el-option v-for="(item,index) in gslx" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span style="font-size: 16px;margin-left: 20px;"> <strong style="color: #00A61B">注 <sup>*</sup>：</strong>公司商户号、商户签名为空时，默认为总公司的商户号和商户签名。</span>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取 消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">保 存</el-button>
            </div>
        </el-dialog>
        <el-dialog title="公司信息" :visible.sync="editFormVisible"
                   @close="editFormCloseClear"
                   :close-on-click-modal="false" class="company-form" width="45%">
            <el-form :model="editForm" label-width="120px" ref="editForm">
                <el-form-item label="公司名称" prop="gsmc"
                              :rules="[{ required: true, message: '公司名称不能为空', trigger: 'blur' }]">
                    <el-input v-model="editForm.gsmc"></el-input>
                </el-form-item>
                <el-form-item label="公司性质"
                              prop="gsxz"
                              :rules="[{ required: true, message: '公司性质不能为空', trigger: 'blur' }]">
                    <el-select v-model="editForm.gsxz" style="width:100%;">
                        <el-option v-for="(item,index) in gsxz" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="公司类型"
                              prop="gslx"
                              :rules="[{ required: true, message: '公司类型不能为空', trigger: 'blur' }]">
                    <el-select v-model="editForm.gslx" style="width:100%;" disabled>
                        <el-option v-for="(item,index) in gslx" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-show="updateShow" label="公司商户号">
                    <el-input v-model="shh" placeholder="填写新的商户号"></el-input>
                </el-form-item>
                <el-form-item v-show="updateShow" label="商户签名">
                    <el-input v-model="qm" placeholder="填写新的商户签名"></el-input>
                </el-form-item>
            </el-form>
            <el-button v-show="!updateShow" type="text" @click="updateShow = true" style="margin-left: 20px;">修改商户信息</el-button>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">保 存</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    export default {
        data() {
            return {
                newPassword: '',
                gslx:{
                  '0': '总公司',
                  '1': '分公司'
                },
                gsxz:{
                    '1': '蓉城公司',
                    '2': '服务公司'
                },
                tableData:[],
                listLoading: false,
                addFormVisible: false,//
                editFormVisible: false,
                resetPassword: false,//
                addLoading: false,
                editLoading: false,
                updateShow: false,
                //新增界面数据
                addForm: {},
                editForm: {},
                shh: '',
                qm:''
            }
        },
        created(){
           this.getData();
        },
        methods: {
            editFormCloseClear() {
                this.$refs.editForm.resetFields()
            },
            addFormCloseClear() {
                this.$refs.addForm.resetFields()
            },
            formatterXz(row) {
                return this.gsxz[row.gsxz];
            },
            formatterLx(row){
                return this.gslx[row.gslx];
            },
            //获取数据
            getData() {
                let param = {
                    rzdm: this.$rzdm,
                };
                let url = this.$url + '/company/findAll';
                this.listLoading = true;
                this.$axios.post(url,this.$qs.stringify(param)).then((res) => {
                    this.listLoading = false;
                    let data = res.data;
                    //console.log(data);
                    if(data.resCode == 200){
                        let companyData = this.tableData = data.data;
                        let company = {};
                        for(let i=0;i<companyData.length;i++){
                            let item = companyData[i];
                            company[item.gsbm] = item.gsmc;
                        }
                        sessionStorage.setItem('company', JSON.stringify(company));
                    }else{
                        this.$message.error(res.data.resMsg);
                    }
                }).catch(() => {
                        this.$router.push({path: '/error'});
                    }
                );
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    let url = this.$url + '/company/delete';
                    this.$axios.post(url,this.$qs.stringify({gsbm:row.gsbm,rzdm:this.$rzdm})).then(res=>{
                        if(res.data.resCode == 200){
                            this.getData()
                            this.listLoading = false;
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        }else{
                            this.$message.error(res.data.resMsg);
                        }
                    })
                }).catch(() => {
                        this.$router.push({path: '/error'});
                    }
                )
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.updateShow = false;
//                this.$refs.addForm.resetFields()
                row.gslx = row.gslx + '';
                row.gsxz = row.gsxz + '';
                this.editForm = Object.assign({}, row); //合并对象防止改动页面内容
//                this.addForm.mm = '';
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
//                this.$refs.addForm.resetFields()
                this.addForm = {
                    gslx: '1'
                };
            },
            editSubmit() {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.editLoading = true;
                        let param = Object.assign({}, this.editForm);
                        param.rzdm = this.$rzdm;
                        if(this.shh && this.qm) {
                            param.shh = this.shh;
                            param.qm = this.qm;
                        }
                        let url = this.$url + '/company/add';

                        this.$axios.post(url,this.$qs.stringify(param)).then((res) => {
                            let data = res.data;
                            if(res.data.resCode == 200){
                                this.editLoading = false;
                                this.$message.success('更新成功!');
                                this.getData();
                                this.editFormVisible = false;

                            }else{
                                this.$message.error(res.data.resMsg);
                            }
                        }).catch(() => {
                                this.$router.push({path: '/error'});
                            }
                        );
                    }
                });
            },
            //提交
            addSubmit: function () {
//                console.log(this.addForm)
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.addLoading = true;
                        let param = Object.assign({}, this.addForm);
                        param.rzdm = this.$rzdm;
                        let url = this.$url + '/company/add';
                        console.log(param)
                        this.$axios.post(url,this.$qs.stringify(param)).then((res) => {
                            let data = res.data;
                            if(res.data.resCode == 200){
                                this.addLoading = false;
                                this.$message.success('保存成功!');
                                this.getData();
                                this.addFormVisible = false;
                            }else{
                                this.$message.error(res.data.resMsg);
                            }
                        }).catch(() => {
                                this.$router.push({path: '/error'});
                            }
                        );
                    }
                });
            },
        },
    }
</script>

<style scoped>
    .file-dialog .content{
        width: 100%;
        height: 800px;
        background: #cccccc;
    }

    .el-dialog--small {
        width: 80%;
    }
</style>