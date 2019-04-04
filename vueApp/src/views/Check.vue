<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar1" id="toolbar">
            <el-form :inline="true" :model="filters" >
                <el-form-item label="车辆自编号" size="medium" class="czbh">
                    <el-input v-model="filters.czbh" placeholder="车辆自编号"  @keyup.enter.native="searchData" clearable @change="searchData"></el-input>
                </el-form-item>
                <el-form-item label="公司名称" v-if="userInfo.qx == 0" size="medium">
                    <el-select v-model="filters.gsdm" clearable @change="searchData">
                        <el-option v-for="(item,index) in company" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费月份" size="medium" class="date">
                    <el-date-picker clearable
                                    value-format="yyyy-MM"
                                    v-model="filters.date"
                                    type="month"
                                    @change="searchData"
                                    placeholder="选择月">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="审核状态" v-if="userInfo.qx == 0" size="medium">
                    <el-select v-model="filters.shzt" clearable
                               @change="searchData">
                        <el-option v-for="(item,index) in shzts" :label="item" :key="index" :value="index" ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item size="medium">
                    <el-button type="primary" @click="searchData">
                        <i class="el-icon-search"></i>
                        查询
                    </el-button>
                    <el-button v-if="filters.shzt=='1'" type="warning" @click="passAll">
                        <!--<i class="el-icon-search"></i>-->
                        一键通过
                    </el-button>
                </el-form-item>

            </el-form>
        </el-col>

        <el-table :data="records" v-loading="listLoading"
                  @row-click="clickRow"
                  ref="multipleTable"  @selection-change="handleSelectionChange"
                  style="width: 100%;overflow: auto; font-size: 14px;">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="czbh" label="出租车自编号" show-overflow-tooltip fixed></el-table-column>
            <el-table-column prop="czcsj" label="出租车司机" width="130" align="center" fixed></el-table-column>
            <el-table-column prop="jfje" label="应缴金额" width="80"></el-table-column>
            <el-table-column prop="yjje" label="已缴金额" width="80"></el-table-column>
            <!--<el-table-column prop="xjje" label="现金/非现金" width="100" :formatter="formatterJF"></el-table-column>-->
            <el-table-column prop="qfje" label="欠费金额" width="90"></el-table-column>
            <el-table-column prop="jfzt" label="缴费状态" width="90" :formatter="formatterJFZT"></el-table-column>
            <el-table-column prop="jfyf" label="缴费月份" width="90" ></el-table-column>
            <el-table-column prop="jfsj" label="缴费时间" width="165"></el-table-column>
            <el-table-column prop="jfr" label="缴费人" width="80" v-if="'1' == '0'"></el-table-column>
            <!--<el-table-column prop="jlr" label="记录人" width="70"></el-table-column>-->
            <el-table-column prop="jfjl" label="缴费记录" width="80" v-if="'1' == '0'" ></el-table-column>
            <el-table-column prop="gsdm" label="所属公司" show-overflow-tooltip :formatter="formatterCompany"></el-table-column>
            <el-table-column label="状态" align="left" width="105">
                <template slot-scope="scope">
                    <span :style="{color: scope.row.shzt === 0 ? 'red' : scope.row.shzt === 1 ? '#FF8A00': scope.row.shzt === 2 ? '#FFCA00': scope.row.shzt === 3 ? 'green' : scope.row.shzt === 4 ? '#0095FF' : '#472DB3'}">
                        {{scope.row.shzt === 0 ? '新建' : scope.row.shzt === 1 ? '待审核': scope.row.shzt === 2 ? '审核通过': scope.row.shzt === 3 ? '审核不通过' : scope.row.shzt === 4 ? '修改中': '已修改待审核'}}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="left" width="130">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">明细</el-button>
                    <el-button v-show="scope.row.shzt === 1" type="danger" size="mini" icon="el-icon-error" @click="updateSHZT(3, scope.row.id)"></el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="bottom-toolbar">
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="size"
                           :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <el-dialog title="车辆缴费信息" width="75%" :visible.sync="addFormVisible" :close-on-click-modal="false" center>
            <el-form :model="addForm" label-width="120px" ref="addForm" class="account-form clearfix">
                <el-form-item label="车辆自编号" prop="czbh"
                              :rules="[{ required: true, message: '出租车自编号不能为空', trigger: 'blur' }]">
                    <el-input v-model="addForm.czbh" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="所属公司" prop="gsdm"
                              :rules="[{ required: true, message: '所属公司不能为空', trigger: 'blur' }]">
                    <el-select v-model="addForm.gsdm" :disabled="true" style="width:100%;">
                        <el-option v-for="(item,index) in company" :label="item" :key="index" :value="index"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="出租车司机">
                    <el-input v-model="addForm.czcsj" disabled></el-input>
                </el-form-item>
                <el-form-item label="应缴金额">
                    <el-input type="number" v-model="addForm.jfje" disabled></el-input>
                </el-form-item>
                <el-form-item label="代收费用种类" style="width:98%">
                    <ul>
                        <li v-for="(item, index) in fyzl" :key="item.name" class="fyzl"
                            style="display: inline-block;width: 33%;">
                            <span class="name" :style="{width: (index + 1)%3 ? '70px' : '120px'}">{{item.value}}</span>
                            <el-input-number v-if="item.name != 'qtfy'" v-model="item.yjje" :min="addForm.id? item.ys : 0" size="mini" controls-position="right" :disabled="true"></el-input-number>
                            <el-input-number v-else  v-model="item.yjje" :min="item.ys" size="mini" controls-position="right" :disabled="true"></el-input-number>
                        </li>
                    </ul>

                </el-form-item>
                <el-form-item label="缴费明细" style="width:100%">
                    <el-input type="textarea" v-model="addForm.jfmx" disabled></el-input>
                </el-form-item>
                <el-form-item label="已缴金额">
                    <el-input v-model="addForm.yjje" disabled></el-input>
                </el-form-item>
                <el-form-item label="欠费金额">
                    <el-input v-model="addForm.qfje" disabled></el-input>
                </el-form-item>
                <el-form-item label="缴费月份">
                    <el-date-picker style="width:100%"
                                    value-format="yyyy-MM"
                                    v-model="addForm.jfyf"
                                    type="month"
                                    placeholder="选择月" :disabled="true">
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
                    <el-input type="textarea" rows="5" v-model="jfjl" disabled></el-input>
                </el-form-item>
                <el-form-item label="修改历史" style="width:100%">
                    <el-input type="textarea" rows="3" v-model="xgls" disabled></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false" :disabled="ztdisabled">取消</el-button>
                <el-button type="primary" v-if="addForm.shzt === 1 || addForm.shzt === 5" @click.native="updateSHZT(2, addForm.id)" :disabled="ztdisabled" :loading="addLoading" style="margin-left: 10px;">通过</el-button>
                <el-button type="danger" v-if="addForm.shzt === 1" @click.native="updateSHZT(3, addForm.id)" :disabled="ztdisabled" :loading="addLoading" style="margin-left: 10px;">不通过</el-button>
                <el-button type="warning" v-if="addForm.shzt === 2 || addForm.shzt === 5" @click.native="updateSHZT(4, addForm.id)" :disabled="ztdisabled" :loading="addLoading" style="margin-left: 10px;">允许修改</el-button>
            </div>
        </el-dialog>


    </section>
</template>

<script>
    export default {
        data() {
            return {
                nowMonth: this.$util.formatDate.format(new Date(),'yyyy-MM'),
                company:JSON.parse(sessionStorage.getItem('company')),
                userInfo:JSON.parse(sessionStorage.getItem('userInfo')).user,
                filters: {
                    date: '',
                    shzt: '1'
                },
                shzts: {
                    "0" : '新建',
                    "4" : '修改中',
                    "1" : '待审核',
                    "5" : '已修改待审核',
                    "2" : '审核通过',
                    "3" : '审核不通过',
                },
                jfzt: {
                    0: '欠费',
                    1: '已缴清'
                },
                total: 0,
                page: 1, //当前页
                size: 10,
                records:[],
                selected: [],
                listLoading: false,
                addFormVisible: false,
                addLoading: false,
                addForm: {},
                fyzl: [
                    {name: 'zzs', value: '增值税',yjje: 0, ys: 0},
                    {name: 'cjs', value: '城建税',yjje: 0, ys: 0},
                    {name: 'jyffj', value: '教育费附加',yjje: 0, ys: 0},
                    {name: 'fwf', value: '服务费',yjje: 0, ys: 0},
                    {name: 'gps', value:'GPS',yjje: 0, ys: 0},
                    {name: 'grsds', value: '个人所得税',yjje: 0, ys: 0},
                    {name: 'pjf', value: '聘驾费',yjje: 0, ys: 0},
                    {name: 'wyj', value: '违约金',yjje: 0, ys: 0},
                    {name: 'dfjyfj', value:'地方教育附加',yjje: 0, ys: 0},
                    {name: 'gf', value: '规费',yjje: 0, ys: 0},
                    {name: 'bzj', value: '保证金',yjje: 0, ys: 0},
                    {name: 'ztxdf', value: '座套洗涤费',yjje: 0, ys: 0},
                    {name: 'gxhf', value: '个协会费',yjje: 0, ys: 0},
                    {name: 'dep', value:'定额票',yjje: 0, ys: 0},
                    {name: 'ejbyf', value: '二级保养费',yjje: 0, ys: 0},
                    {name: 'gbf', value: '工本费',yjje: 0, ys: 0},
                    {name: 'cbj', value: '承包金',yjje: 0, ys: 0},
                    {name: 'dmsbgjj', value: '代买社保公积金',yjje: 0, ys: 0},
                    {name: 'qtfy', value: '其他费用',yjje: 0, ys: 0}
                ],
                jfjl: '',
                xgls: '',
                ztdisabled: false
            }
        },
        created(){
            this.filters.date = this.nowMonth;
            this.getData();
        },
        methods: {
            passAll() {
                let ids = [];
                if(this.selected.length) {
                    this.selected.forEach(item => {
                        ids.push(item.id);
                    })
                    this.$axios.post(this.$url + '/payrecords/doexamine', this.$qs.stringify({
                        jlids: ids.join(','),
                        shzt: 2,
                        rzdm: this.$rzdm
                    })).then(res => {
                        if(res.data.resCode == 200) {
                            this.getData();
                            this.$message.success("操作成功！");
                        }else {
                            this.$message.error(res.data.resMsg);
                        }
                    }).catch(() => {
                        this.$router.push({path: '/error'});
                    })
                }else {
                    this.$notify({
                        title: '警告',
                        message: '没有勾选需要通过的缴费记录项。',
                        type: 'warning',
                        offset: 100
                    });
                }

            },
            updateSHZT(shzt, id) {
                this.ztdisabled = true;
                this.$axios.post(this.$url + '/payrecords/updateshzt', this.$qs.stringify({
                    id: id,
                    shzt: shzt,
                    rzdm: this.$rzdm
                })).then(res => {
                    this.ztdisabled = false;
                    if(res.data.resCode == 200) {
                        if(this.addFormVisible) {
                            this.addFormVisible = false;
                        }
                        this.getData();
                        this.$message.success("操作成功！");
                    }else {
                        this.$message.error(res.data.resMsg);
                    }
                }).catch(() => {
                    this.$router.push({path: '/error'});
                })
            },
            handleEdit: function (index,row) {
                this.addFormVisible = true;
                row.jfzt = row.jfzt+ '';
                console.log(row)
                this.addForm = Object.assign({}, row); //合并对象防止改动页面内容
                this.fyzl.forEach(item => {
                    item.yjje = this.addForm[item.name];
                    let ys = 'ys' + item.name;
                    item.ys = this.addForm[ys];
                })
                if(this.addForm.jfjl) {
                    this.jfjl = this.addForm.jfjl.replace(/\|\|/g, '\n')
                }else {
                    this.jfjl = '';
                }
                if(this.addForm.xgls) {
                    this.xgls = this.addForm.xgls.replace(/\|\|/g, '\n')
                }else {
                    this.xgls = '';
                }
                this.yjje = row.yjje;
            },
            clickRow(row){
                this.$refs.multipleTable.toggleRowSelection(row)
            },
            handleSelectionChange(val) {
                this.selected = val;
            },
            searchData() {
                this.page = 1;
                this.getData();
            },
            getData() {
                this.listLoading = true;
                let url = this.$url + '/payrecords/getpayrecords';
                let param = {
                    page: this.page,
                    size: this.size,
                    rzdm: this.$rzdm
                };
                param = Object.assign(param, this.filters);
                this.$axios.post(url,this.$qs.stringify(param)).then((res) => {
                    let data = res.data;
                    if(data.resCode == 200){
                        let data2 = data.data;
                        this.records = data2.content;
                        this.total = data2.totalElements;
                        this.listLoading = false;
                    }else{
                        this.$message.error(res.data.resMsg);
                    }
                });
            },
            formatterJFZT(row,index){
                return this.jfzt[row.jfzt];
            },
            formatterCompany(row,index){
                return this.company[row.gsdm];
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getData();
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