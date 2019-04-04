<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar1" id="toolbar">
            <el-form :inline="true" :model="filters">
                <el-form-item label="车辆自编号" size="medium" class="czbh">
                    <el-input v-model="filters.czbh" placeholder="车辆自编号" @keyup.enter.native="searchData"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="缴费月份" size="medium" class="date">
                    <el-date-picker clearable
                                    value-format="yyyy-MM"
                                    v-model="filters.date"
                                    type="month"
                                    placeholder="选择月">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="公司名称" v-if="userInfo.qx == 0" size="medium">
                    <el-select v-model="filters.gsdm" clearable>
                        <el-option v-for="(item,index) in company" :label="item" :key="index"
                                   :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item size="medium">
                    <el-button type="primary" @click="searchData">
                        <i class="el-icon-search"></i>
                        查询
                    </el-button>
                    <a :href="`${$url}/payrecords/exportBygsbm?rzdm=${$rzdm}&date=${this.filters.date}&gsdm=${filters.gsdm || userInfo.gsdm}`">
                        <el-button type="primary" >
                            <i class="el-icon-upload2"></i>
                            导出
                        </el-button>
                    </a>

                    <el-button type="warning" @click="handleAdd" v-show="userInfo.qx != 0">
                        <i class="el-icon-plus"></i>
                        添加
                    </el-button>
                    <el-button type="primary" @click="importEXCEL" v-show="userInfo.qx != 0">
                        <i class="el-icon-download"></i>
                        导入
                    </el-button>
                    <el-button type="primary" @click="submitSH" v-show="userInfo.qx != 0">
                        提交审核
                    </el-button>
                    <el-button type="warning" @click="deleteAll" v-show="userInfo.qx != 0">
                        一键删除
                    </el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="records" v-loading="listLoading" style="width: 100%;overflow: auto; font-size: 14px;">
            <el-table-column type="index" width="55" fixed></el-table-column>
            <el-table-column prop="czbh" label="出租车自编号" show-overflow-tooltip width="140" fixed></el-table-column>
            <el-table-column prop="czcsj" label="出租车司机" show-overflow-tooltip width="130"
                             align="center"></el-table-column>
            <el-table-column prop="jfje" label="应缴金额" width="80"></el-table-column>
            <el-table-column prop="yjje" label="已缴金额" width="80"></el-table-column>
            <el-table-column prop="qfje" label="欠费金额" width="90"></el-table-column>
            <el-table-column prop="jfzt" label="缴费状态" width="90" :formatter="formatterJFZT"></el-table-column>
            <el-table-column prop="jfyf" label="缴费月份" width="90"></el-table-column>
            <el-table-column prop="jfsj" label="缴费时间" width="100"></el-table-column>
            <el-table-column prop="jfr" label="缴费人" width="80" v-if="'1' == '0'"></el-table-column>
            <el-table-column prop="jfjl" label="缴费记录" width="80" v-if="'1' == '0'"></el-table-column>
            <el-table-column prop="gsdm" label="所属公司" show-overflow-tooltip
                             :formatter="formatterCompany"></el-table-column>
            <el-table-column label="状态" align="left" width="110">
                <template slot-scope="scope">
                    <span :style="{color: scope.row.shzt === 0 ? 'red' : scope.row.shzt === 1 ? '#FF8A00': scope.row.shzt === 2 ? '#FFCA00': scope.row.shzt === 3 ? 'green' : scope.row.shzt === 4 ? '#0095FF' : '#472DB3'}">
                        {{scope.row.shzt === 0 ? '新建' : scope.row.shzt === 1 ? '待审核' : scope.row.shzt === 2 ? '审核通过' : scope.row.shzt === 3 ? '审核不通过' : scope.row.shzt === 4 ? '可修改' : '修改审核中'}}
                    </span>
                    <!--<el-tag-->
                    <!--:type="scope.row.shzt === 0 ? '' : scope.row.shzt === 1 ? 'warning': scope.row.shzt === 2 ? 'success': 'danger'"-->
                    <!--disable-transitions>{{scope.row.shzt === 0 ? '新建' : scope.row.shzt === 1 ? '待审核': scope.row.shzt === 2 ? '审核通过': '审核不通过'}}</el-tag>-->
                </template>
            </el-table-column>
            <el-table-column label="操作" align="left" width="140">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">明细</el-button>
                    <el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="bottom-toolbar">
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="size"
                           :total="total" style="float:right;">
            </el-pagination>
        </el-col>
        <!--导入excel弹框-->
        <el-dialog title="导入excel" :visible.sync="filedialogFormVisible" width='35%' v-loading="importLoading"
                   element-loading-text="导入数据中"
                   element-loading-spinner="el-icon-loading"
                   element-loading-background="rgba(255, 255, 255, 0.8)">
            <el-form :model="fileForm" label-width="140px">
                <el-form-item label="选择Excel文件">
                    <input ref="fileInput" type="file" id="fileUpload" class="file" @change="getFile($event)">
                </el-form-item>
                <a href="/static/excelTemplet/出租车缴费明细模版.xlsx"
                   style="color: RGB(0, 166, 27); font-size: 16px;margin-left: 10px;">点击下载缴费记录的Excel模板 </a>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm($event)">导 入</el-button>
                <el-button @click="filedialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>


        <!--新增,新增界面-->
        <el-dialog title="车辆缴费信息" width="75%" :visible.sync="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="120px" ref="addForm" class="account-form clearfix">
                <el-form-item label="车辆自编号" prop="czbh"
                              :rules="[{ required: true, message: '出租车自编号不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.czbh" :disabled="inputDisabled"></el-input>
                </el-form-item>
                <el-form-item label="所属公司" prop="gsdm"
                              :rules="[{ required: true, message: '所属公司不能为空', trigger: 'blur' }]">
                    <el-select v-model="addForm.gsdm" :disabled="inputDisabled" style="width:100%;">
                        <el-option v-for="(item,index) in company" :label="item" :key="index"
                                   :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="出租车司机">
                    <el-input v-model="addForm.czcsj"></el-input>
                </el-form-item>
                <el-form-item label="应缴金额">
                    <el-input type="number" v-model="addForm.jfje" disabled></el-input>
                </el-form-item>
                <el-form-item label="代收费用种类" style="width:98%">
                    <ul>
                        <li v-for="(item, index) in fyzl" :key="item.name" class="fyzl"
                            style="display: inline-block;width: 49%;">
                            <span class="name" :style="{width: (index + 1)%2 ? '150px' : '205px'}">{{item.value}} <span style="font-size: 12px;padding-left: 3px;color: #009B1B">已收:{{item.ys}}</span></span>
                            <template v-if="item.name != 'qtfy'">
                                <el-input-number v-model="item.yjje" :min="addForm.id? item.ys : 0" size="mini"
                                                 controls-position="right"></el-input-number>
                            </template>
                            <template v-else>
                                <el-input-number v-if="item.ys === 0" v-model="item.yjje" size="mini"
                                                 controls-position="right"></el-input-number>
                                <el-input-number v-else-if="item.ys>0" v-model="item.yjje" :min="item.ys" size="mini"
                                                 controls-position="right"></el-input-number>
                                <el-input-number v-else v-model="item.yjje" :max="item.ys" size="mini"
                                                 controls-position="right"></el-input-number>
                            </template>
                        </li>
                    </ul>

                </el-form-item>
                <el-form-item label="缴费明细" style="width:100%">
                    <el-input type="textarea" v-model="addForm.jfmx"></el-input>
                </el-form-item>
                <el-form-item label="已缴金额">
                    <el-input v-model="addForm.yjje" disabled></el-input>
                </el-form-item>
                <!--<el-form-item label="现金金额">-->
                <!--<el-input v-model="addForm.xjje" disabled></el-input>-->
                <!--</el-form-item>-->
                <el-form-item label="欠费金额">
                    <el-input v-model="addForm.qfje" disabled></el-input>
                </el-form-item>
                <!--<el-form-item label="非现金金额">-->
                <!--<el-input v-model="addForm.fxjje" disabled></el-input>-->
                <!--</el-form-item>-->
                <el-form-item label="缴费月份">
                    <el-date-picker style="width:100%"
                                    value-format="yyyy-MM"
                                    v-model="addForm.jfyf"
                                    type="month"
                                    placeholder="选择月" :disabled="inputDisabled">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="缴费时间">
                    <el-input v-model="addForm.jfsj" disabled></el-input>
                </el-form-item>
                <el-form-item label="缴费状态">
                    <el-select v-model="addForm.jfzt" placeholder="" disabled style="width:100%;">
                        <el-option v-for="(item,index) in jfzt" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="记录人">
                    <el-input v-model="addForm.jlr" disabled></el-input>
                </el-form-item>
                <el-form-item label="本月缴费详情" style="width:100%">
                    <el-input type="textarea" rows="4" v-model="jfjl" disabled></el-input>
                </el-form-item>
                <el-form-item label="修改历史" style="width:100%">
                    <el-input type="textarea" rows="3" v-model="xgls" disabled></el-input>
                </el-form-item>
                <el-form-item v-if="addForm.shzt === 4" label="本次修改原因" style="width:100%">
                    <el-input type="textarea" rows="2" v-model="xgyy" placeholder="（必填项）"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button v-if="!addForm.id||addForm.shzt == 0 ||addForm.shzt == 3 || addForm.shzt == 4" type="primary"
                           @click.native="saveSubmit" :loading="addLoading">提交
                </el-button>
            </div>
        </el-dialog>

    </section>
</template>

<script>

    export default {
        data() {
            return {
                nowMonth: this.$util.formatDate.format(new Date(), 'yyyy-MM'),
                company: JSON.parse(sessionStorage.getItem('company')),
                userInfo: JSON.parse(sessionStorage.getItem('userInfo')).user,
                jfzt: {
                    0: '欠费',
                    1: '已缴清'
                },
                records: [],//数据总数
                filters: {
                    date: ''
                },
                total: 0,
                page: 1, //当前页
                size: 10,
                listLoading: false,
                fileDalogVisible: false,//模板界面
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {},
                filedialogFormVisible: false,
                fileForm: {},
                file: {
                    size: 0,
                    name: ''
                },
                jfjl: '',
                xgls: '',
                xgyy: '',
                yjje: 0,
                inputDisabled: false,
                status: 1,
                importLoading: false,
                selectedFyzl: [],
                fyzl: [
                    {name: 'zzs', value: '增值税', yjje: 0, ys: 0},
                    {name: 'cjs', value: '城建税', yjje: 0, ys: 0},


                    {name: 'gps', value: 'GPS', yjje: 0, ys: 0},
                    {name: 'qtfy', value: '其他费用', yjje: 0, ys: 0},

                    {name: 'pjf', value: '聘驾费', yjje: 0, ys: 0},
                    {name: 'gxhf', value: '个协会费', yjje: 0, ys: 0},

                    {name: 'wyj', value: '违约金', yjje: 0, ys: 0},
                    {name: 'dfjyfj', value: '地方教育附加', yjje: 0, ys: 0},
                    {name: 'gf', value: '规费', yjje: 0, ys: 0},

                    {name: 'ztxdf', value: '座套洗涤费', yjje: 0, ys: 0},
                    {name: 'bzj', value: '保证金', yjje: 0, ys: 0},
                    {name: 'grsds', value: '个人所得税', yjje: 0, ys: 0},
                    {name: 'dep', value: '定额票', yjje: 0, ys: 0},
                    {name: 'ejbyf', value: '二级保养费', yjje: 0, ys: 0},
                    {name: 'gbf', value: '工本费', yjje: 0, ys: 0},
                    {name: 'jyffj', value: '教育费附加', yjje: 0, ys: 0},
                    {name: 'cbj', value: '承包金', yjje: 0, ys: 0},
                    {name: 'dmsbgjj', value: '代买社保公积金', yjje: 0, ys: 0},
                    {name: 'fwf', value: '服务费', yjje: 0, ys: 0},

                ]
            }
        },
        created(){
            this.filters.date = this.nowMonth;
            this.getData();
            //添加公司信息
            let thisCompany = {};
            thisCompany[this.userInfo.gsdm] = this.userInfo.gsmc;
            this.company = Object.assign(this.company, thisCompany);
        },
        watch: {
            fyzl: {
                handler(val, oldVal){
                    this.addForm.jfje = 0;
                    this.addForm.jfmx = '';
                    for (let i = 0; i < val.length; i++) {
                        val[i].yjje = Math.round(val[i].yjje*100)/100;
                        this.addForm.jfje = Math.round(parseFloat(this.addForm.jfje) * 100 + parseFloat(val[i].yjje) * 100) / 100;
                    }
                    this.addForm.jfje = Math.round(this.addForm.jfje * 100) / 100;
                    if (this.addForm.jfje === '') {  //应缴金额为空
                        this.status = 0;
                    } else if (Number(this.addForm.jfje) <= 0) {  //应缴金额小于等于0
                        this.status = 0;
                    } else if (Number(this.addForm.jfje) < this.yjje) {  //应缴金额 小于 已缴金额
                        this.status = 0;
                    } else {
                        this.status = 1;
                        let jfje = this.addForm.jfje;
                        this.addForm.yjje = this.yjje;
                        this.addForm.qfje = Math.round(parseFloat(jfje) * 100 - parseFloat(this.addForm.yjje) * 100) / 100;
                        if (this.addForm.qfje <= 0) {
                            this.addForm.jfzt = "1";
                        } else {
                            this.addForm.jfzt = '0';
                        }
                    }
                },
                deep: true
            },
        },
        methods: {
            submitSH() {
                this.$confirm('此操作将提交' + this.filters.date + '月的所有缴费信息给总公司审核, 是否继续?', '提示', {
                    confirmButtonText: '提交审核',
                    cancelButtonText: '取消提交',
                    type: 'success',
                    center: true,
                    showClose: false
                }).then(() => {
                    this.$axios.post(this.$url + '/payrecords/examineup', this.$qs.stringify({
                        date: this.filters.date,
                        rzdm: this.$rzdm,
                    })).then(res => {
                        if (res.data.resCode == 200) {
                            this.$message({
                                message: '提交成功！',
                                type: 'success'
                            });
                            this.getData();
                        } else {
                            this.$message({
                                message: res.data.resMsg,
                                type: 'error'
                            });
                        }
                    }).catch(function (error) {
                        this.$router.push({path: '/error'});
                    });
                }).catch(() => {
                });
            },
            deleteAll() {
                this.$confirm('此操作将永久删除' + this.filters.date + '月所有审核状态为新建的缴费信息, 是否继续?', '提示', {
                    confirmButtonText: '确定删除',
                    cancelButtonText: '取消删除',
                    type: 'warning',
                    center: true,
                    showClose: false
                }).then(() => {
                    this.$axios.post(this.$url + '/payrecords/deleteall', this.$qs.stringify({
                        date: this.filters.date,
                        rzdm: this.$rzdm,
                    })).then(res => {
                        if (res.data.resCode == 200) {
                            this.$message({
                                message: '已删除成功！',
                                type: 'success'
                            });
                            this.getData();
                        } else {
                            this.$message({
                                message: res.data.resMsg,
                                type: 'error'
                            });
                        }
                    }).catch(function (error) {
                        this.$router.push({path: '/error'});
                    });
                }).catch(() => {
                })
            },
            fyzlChange(val) {
                console.log(val);
            },
            submitForm(event) {
                if (this.file.size == 0) {
                    this.$message.error("请选择需要导入的文件");
                } else {
//                    event.preventDefault();
                    let formData = new FormData();
                    formData.append('file', this.file);
                    formData.append('rzdm', this.$rzdm);
                    let config = {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    };
                    this.importLoading = true;
                    this.$axios.post(this.$url + '/payrecords/batchImport', formData, config).then((res) => {
                        if (res.data.resCode == 200) {
                            setTimeout(() => {
                                this.filedialogFormVisible = false;
                                this.$notify.info({
                                    title: '提示',
                                    message: res.data.data,
                                    duration: 6000,
                                    dangerouslyUseHTMLString: true,
                                });
                                this.importLoading = false;
                                this.getData();
                            }, 600);
                        } else {
                            this.$notify.error({
                                title: '提示',
                                message: res.data.resMsg,
                                duration: 4000,
                                dangerouslyUseHTMLString: true,
                            });
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            },
            getFile(event) {
                this.file = event.target.files[0];
                if (!this.file) {
                    let filename = this.file.name;
                    let index = filename.indexOf('.');
                }
            },
            //显示打开/编辑界面
            handleEdit: function (index, row) {
                this.addFormVisible = true;
                this.inputDisabled = true;
                row.jfzt = row.jfzt + '';
                console.log(row)
                this.addForm = Object.assign({}, row); //合并对象防止改动页面内容
                this.fyzl.forEach(item => {
                    item.yjje = this.addForm[item.name];
                    let ys = 'ys' + item.name;
                    item.ys = this.addForm[ys];
                })
                if (this.addForm.jfjl) {
                    this.jfjl = this.addForm.jfjl.replace(/\|\|/g, '\n')
                } else {
                    this.jfjl = '';
                }
                if (this.addForm.xgls) {
                    this.xgls = this.addForm.xgls.replace(/\|\|/g, '\n')
                } else {
                    this.xgls = '';
                }
                this.yjje = row.yjje;
                this.xgyy = '';
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.inputDisabled = false;
                this.fyzl.forEach(item => {
                    item.yjje = 0;
                    item.ys = 0;
                })
                this.addForm = {
                    jfyf: this.$util.formatDate.format(new Date(), 'yyyy-MM'),
                    jfzt: "0",
                    yjje: '0',
                    qfje: '0',
                    jfje: '0',
                };
                this.jfjl = '';
                this.yjje = 0;
            },
            searchData(){
                this.page = 1;
                this.getData(this.filters)
            },
            importEXCEL() {
                this.filedialogFormVisible = true;
                this.file = {size: 0, name: ''};
                if (this.$refs.fileInput) {
                    this.$refs.fileInput.value = '';
                }
            },
            //导出数据
            exportEXCEL(){
                let url = this.$url + '/payrecords/exportBygsbm';
                let paramExcel = {
                    date: this.filters.date,
                    rzdm: this.$rzdm
                };
                paramExcel.gsdm = this.filters.gsdm || this.userInfo.gsdm;
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
                let url = this.$url + '/payrecords/search';
                let param = {
                    page: this.page,
                    size: this.size,
                    rzdm: this.$rzdm
                };
                //if(filters){
                param = Object.assign(param, this.filters);
                //}
                this.$axios.post(url, this.$qs.stringify(param)).then((res) => {
                    let data = res.data;
                    if (data.resCode == 200) {
                        let data2 = data.data;
                        this.records = data2.content;
                        this.total = data2.totalElements;
                        this.listLoading = false;
                    } else {
                        this.$message.error(res.data.resMsg);
                    }
                });
            },
            formatterJF(row, index){
                return row.xjje + '/' + row.fxjje
            },
            formatterJFZT(row, index){
                return this.jfzt[row.jfzt];
            },
            formatterCompany(row, index){
                return this.company[row.gsdm];
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getData();
            },

            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该缴费记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    let url = this.$url + '/payrecords/delete';
                    this.$axios.post(url, this.$qs.stringify({id: row.id, rzdm: this.$rzdm})).then(res => {
                        if (res.data.resCode == 200) {
                            this.getData();
                            this.listLoading = false;
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        } else {
                            this.$message.error(res.data.resMsg);
                        }
                    })
                })
            },

            //新增
            saveSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        if (this.status === 0) {
                            this.$message.error('缴费金额输入有误');
                        } else {
                            this.addForm.rzdm = this.$rzdm;
                            let url = this.$url + '/payrecords/add';
                            if (this.addForm.id) {
                                url = this.$url + '/payrecords/update';
                                this.addForm.shzt = this.addForm.shzt === 3 ? 0 : this.addForm.shzt === 4 ? 4 : this.addForm.shzt;
                                if (this.addForm.shzt === 4) {
                                    if (this.xgyy == '') {
                                        this.$message.error('本次修改原因为必填项!');
                                        return;
                                    }
                                    this.addForm.xgls = this.xgyy + '     ' + this.userInfo.gsmc + ' - ' + this.userInfo.xm + '（ ' + this.$util.formatDate.format(new Date(), 'yyyy-MM-dd hh:mm:ss') + ' ）';
                                } else {
                                    this.addForm.xgls = '';
                                }
                            }
                            let param = this.addForm;
                            param.jfjl = '';
                            this.fyzl.forEach(item => {
                                param[item.name] = item.yjje;
                            })
                            console.log(param);
                            this.$axios.post(url, this.$qs.stringify(param)).then((res) => {
                                let data = res.data;
                                if (res.data.resCode == 200) {
                                    this.$message.success('保存成功!');
                                    this.getData();
                                    this.addFormVisible = false;
                                    this.$refs.addForm.resetFields()
                                } else {
                                    this.$message.error(res.data.resMsg);
                                }
                            });
                        }
                    }
                });
            }
        }
    }
</script>
<style lang="scss">
    .el-message-box__btns {
        padding: 10px 15px 0;
    }

    .el-message-box__btns .el-button--small {
        font-size: 16px;
    }
    .name + .el-input-number--mini {
        width: 130px;
    }
</style>
<style scoped lang="scss">
    .fyzl > .name {
        display: inline-block;
        font-size: 16px;
    }

    .account-form {
        .el-form-item {
            float: left;
            width: 50%;
        }
    }

    .file {
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