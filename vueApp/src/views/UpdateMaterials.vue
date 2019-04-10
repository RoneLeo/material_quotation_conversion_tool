<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar toolbar2">
            <el-form :inline="true" size="medium">
                <el-form-item style="margin: auto;">
                    <el-button type="primary" @click="handleAdd">添加材料</el-button>
                    <el-button type="primary" @click="handleAddMaterial">从材料库中添加材料</el-button>
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
            <el-table-column prop="clsl" label="材料数量"></el-table-column>
            <el-table-column prop="cbj" label="成本价"></el-table-column>
            <el-table-column prop="jj" label="基价"></el-table-column>
            <el-table-column label="操作" width="250">
                <template slot-scope="scope">
                    <el-button size="mini" @click="updateMaterial(scope.row)">修改材料数量</el-button>
                    <el-button size="mini" type="danger" @click="deleteMaterial(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="从材料库中添加材料" :visible.sync="materialFormVisible" :close-on-click-modal="false" width="70%"
                   top="5vh">
            <el-row :gutter="30">
                <el-col :span="14">
                    <div>
                        <el-input
                                placeholder="请输入材料名称进行查询"
                                prefix-icon="el-icon-search"
                                size="small"
                                style="width: 40%;display: inline-block"
                                clearable
                                v-model="searchMaterialName">
                        </el-input>
                        <el-input
                                placeholder="请输入材料规格进行查询"
                                prefix-icon="el-icon-search"
                                size="small"
                                style="width: 40%;display: inline-block"
                                clearable
                                v-model="searchMaterialType">
                        </el-input>
                        <el-button size="small" @click.native="searchMaterial">查询</el-button>
                    </div>
                    <el-table size="mini" :data="materials" ref="multipleTable"
                              @row-click="clickRow"
                              @selection-change="handleSelectionChange" height="400"
                              style="width: 100%;margin-top: 10px">
                        <el-table-column type="selection" width="40"></el-table-column>
                        <el-table-column prop="clmc" label="材料名称" width="250"></el-table-column>
                        <el-table-column prop="clgg" label="材料规格"></el-table-column>
                        <el-table-column prop="cldw" label="材料单位"></el-table-column>
                    </el-table>
                </el-col>
                <el-col :span="10">
                    <div style="height: 30px;line-height: 30px;font-weight: 500;font-size: 16px;color: #313131">已选择材料列表：</div>
                    <div style="max-height: 410px;overflow-y: auto;">
                        <div v-for="(item, index) in multipleSelection" :key="index" style="margin-top: 10px; ">
                            <div style="font-weight: 500;line-height: 26px;">{{index + 1}}.  {{item.clmc}}</div>
                            {{item.clgg}}
                            <el-input-number size="mini" v-model="item.clsl" style="margin-left: 10px"></el-input-number>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <div slot="footer" class="dialog-footer">
                <el-button @click.native="materialFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addMaterialSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog :title="title" :visible.sync="addFormVisible" :close-on-click-modal="false" width="40%">
            <el-form :model="addForm" label-width="120px" ref="addForm">
                <el-form-item label="材料名称"
                              prop="clmc"
                              :rules="[{ required: true, message: '材料名称不能为空', trigger: 'blur' }]">
                    <el-input v-show="!addForm.id" v-model="addForm.clmc"></el-input>
                    <span style="font-size: 16px;" v-show="addForm.id">{{addForm.clmc}}</span>
                </el-form-item>
                <el-form-item label="材料规格"
                              prop="clgg"
                              :rules="[{ required: true, message: '材料规格不能为空', trigger: 'blur' }]">
                    <el-input v-show="!addForm.id" v-model="addForm.clgg"></el-input>
                    <span v-show="addForm.id" style="font-size: 16px;">{{addForm.clgg}}</span>
                </el-form-item>
                <el-form-item label="材料单位"
                              prop="cldw"
                              :rules="[{ required: true, message: '材料单位不能为空', trigger: 'blur' }]">
                    <el-input v-show="!addForm.id" v-model="addForm.cldw"></el-input>
                    <span v-show="addForm.id" style="font-size: 16px;">{{addForm.cldw}}</span>
                </el-form-item>
                <el-form-item label="材料数量"
                              prop="clsl"
                              :rules="[{ required: true, message: '材料数量不能为空', trigger: 'blur' }]">
                    <el-input-number v-model="addForm.clsl" :min="1" label="材料数量"></el-input-number>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
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
                title: '',
                materialFormVisible: false,
                multipleSelection: [],
                searchMaterialName: '',
                searchMaterialType: '',
                tableLoading: false
            }
        },
        created(){
            this.xmbh = this.$route.query.xmbh;
            this.getData();
            this.searchMaterial();
        },
        methods: {
            searchMaterial() {
                this.$axios.post('/material/searchByXmbh', {xmbh: this.xmbh, clgg: this.searchMaterialType, clmc: this.searchMaterialName}).then(res => {
                    this.materials = res.data;
                })
            },
            clickRow(row){
                this.$refs.multipleTable.toggleRowSelection(row)
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
//                console.log(this.multipleSelection)
            },
            addMaterialSubmit() {
                console.log('共添加', this.multipleSelection)
                let param = this.multipleSelection.map(item => {
                    return {cldw: item.cldw, clgg: item.clgg, clmc: item.clmc, clsl: item.clsl, xmbh: this.xmbh}
                })
                let config = {
                    headers: {
                        'Content-Type': 'application/json;charset=utf-8'
                    }
                }
                this.$axios.post('/projectdata/addAll', JSON.stringify(param), config).then(res => {
                    this.getData();
                    this.$message({type: 'success', message: res.resMsg});
                    this.materialFormVisible = false;
                })
            },
            handleAddMaterial() {
                this.materialFormVisible = true;
            },
            deleteMaterial(row) {
                this.$confirm('此操作将为本项目删除该材料, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post('/projectdata/deleteOne', {id: row.id}).then((res) => {
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
            updateMaterial(row) {
                this.title = '修改材料数量';
                this.addFormVisible = true;
                console.log(row)
                this.addForm = Object.assign({}, row);
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
                this.title = '添加材料';
                this.addForm = {};
                this.addFormVisible = true;
            },
            //提交
            addSubmit: function () {
                this.addLoading = true;
                if (this.addForm.id) {
                    this.$axios.post('/projectdata/updateOne', {
                        id: this.addForm.id,
                        clsl: this.addForm.clsl
                    }).then(res => {
                        this.addLoading = false;
                        this.addFormVisible = false;
                        this.getData();
                        this.$message({type: 'success', message: res.resMsg});
                    })
                } else {
                    this.$axios.post('/projectdata/addMaterial', Object.assign({}, {xmbh: this.xmbh}, this.addForm)).then(res => {
                        this.addLoading = false;
                        this.addFormVisible = false;
                        this.getData();
                        this.$message({type: 'success', message: res.resMsg});
                    })
                }

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