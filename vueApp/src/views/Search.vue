<template>
    <section>
        <div class="search-box clearfix" v-show="searchShow">
            <!--<h2 class="title">成都市蓉城出租汽车有限公司缴费管理系统</h2>-->
            <div class="search">
                <span id="clbh"><el-input placeholder="请输入车辆自编号" v-model="serachCzbh" @keyup.enter.native="search"
                                          clearable></el-input></span>
                <span id="time"><el-date-picker v-model="serachTime" value-format="yyyy-MM" type="month"
                                                @change="timeChange" placeholder="选择月份"
                                                clearable></el-date-picker></span>
                <el-button type="primary" icon="el-icon-search" @click.native="search">查询</el-button>
            </div>
            <div class="tips">
                <i>{{tips}}</i>
            </div>
            <el-table
                    v-loading="listLoading" :data="tableData" border
                    style="width: 100%; margin-top: 30px">
                <el-table-column prop="czbh" align="center" label="出租车编号"></el-table-column>
                <el-table-column prop="gsdm" label="所属公司" align="center" show-overflow-tooltip
                                 :formatter="formatterCompany"></el-table-column>
                <el-table-column prop="shzt" label="审核状态" align="center" show-overflow-tooltip
                                 :formatter="formatterSHZT"></el-table-column>
                <el-table-column label="操作" align="center" width="130">
                    <template slot-scope="scope">
                        <el-button type="text" @click="gotoPay(scope.row)" size="small">缴费</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-col :span="24" class="bottom-toolbar">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="page"
                        :page-size="size"
                        layout="prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </el-col>
        </div>
        <div class="account-form" v-show="accountShow">
            <div class="account-title clearfix">
                <div class="taxi left">
                    出租车自编号: <span class="number">{{recordForm.czbh}}</span>
                </div>
                <div class="company left">
                    所属公司: <span class="number">{{recordForm.company}}</span>
                </div>
            </div>
            <el-form :model="recordForm" label-width="120px" ref="recordForm" class="clearfix">
                <el-form-item label="应缴金额">
                    <el-input v-model="recordForm.jfje" readonly></el-input>
                </el-form-item>
                <el-form-item label="缴费状态">
                    <el-input v-model="recordForm.jfztTxt" readonly></el-input>
                </el-form-item>
                <el-form-item label="已缴金额">
                    <el-input v-model="yjje" readonly></el-input>
                </el-form-item>

                <el-form-item label="欠费金额">
                    <el-input v-model="recordForm.qfje" readonly></el-input>
                </el-form-item>
                <el-form-item label="本次缴费金额">
                    <el-input type="number" v-model="recordForm.bcjfje" readonly></el-input>
                </el-form-item>
                <el-form-item label="记录人">
                    <el-input v-model="recordForm.jlr" readonly></el-input>
                </el-form-item>

                <el-form-item class="txt" label="代收费用种类"
                              style="width:98%">
                    <el-checkbox-group
                            v-model="selectedFyzl">
                        <span v-for="item in fyzl" :key="item" v-if="recordForm[item] > 0 && item != 'qtfy'"
                              class="fyzl-item">
                            <el-checkbox :label="item" disabled>
                                {{types[item]}}
                                <span class="fyzl-item-td">应缴：{{recordForm[item]}}</span>
                                <span class="fyzl-item-td">已缴：{{recordForm['ys' + item]}}</span>
                                <span class="fyzl-item-td">欠费：{{Math.round(recordForm[item] * 100 - recordForm['ys' + item] * 100)/100}}</span>
                                <el-input-number v-model="je[item]" :min="0"
                                                 :max="Math.round(recordForm[item] * 100 - recordForm['ys' + item] * 100)/100"
                                                 controls-position="right" size="small"
                                                 style="margin-left: 10px;"></el-input-number>
                            </el-checkbox>
                        </span>
                        <span v-if="recordForm.qtfy != 0" class="fyzl-item">
                            <el-checkbox label="qtfy" disabled>
                                其他费用
                                <span class="fyzl-item-td">应缴：{{recordForm.qtfy}}</span>
                                <span class="fyzl-item-td">已缴：{{recordForm.ysqtfy}}</span>
                                <span class="fyzl-item-td">欠费：{{Math.round(recordForm.qtfy * 100 - recordForm.ysqtfy * 100)/100}}</span>
                                <el-input-number v-if="Math.round(recordForm.qtfy * 100 - recordForm.ysqtfy * 100)/100 >= 0" v-model="je.qtfy"
                                                 :min="0" :max="Math.round(recordForm.qtfy * 100 - recordForm.ysqtfy * 100)/100"
                                                 controls-position="right" size="small"
                                                 style="margin-left: 10px;"></el-input-number>
                                <el-input-number v-if="Math.round(recordForm.qtfy * 100 - recordForm.ysqtfy * 100)/100 < 0" v-model="je.qtfy"
                                                 :min="Math.round(recordForm.qtfy * 100 - recordForm.ysqtfy * 100)/100" :max="0"
                                                 controls-position="right" size="small"
                                                 style="margin-left: 10px;"></el-input-number>
                            </el-checkbox>
                        </span>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item class="txt" label="缴费明细" style="width:98%">
                    <el-input type="textarea" style="height:80px; resize: none;" v-model="recordForm.jfmx"
                              readOnly></el-input>
                </el-form-item>
                <el-form-item label="缴费月份">
                    <el-input v-model="recordForm.jfyf" readonly></el-input>
                </el-form-item>
                <el-form-item label="缴费人">
                    <el-select v-model="recordForm.jfr" clearable placeholder="请选择">
                        <el-option
                                v-for="item in jfr"
                                :key="item"
                                :label="item"
                                :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="缴费时间" style="width:98%">
                    <el-date-picker style="width:100%;"
                                    v-model="recordForm.jfsj"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetime"
                                    placeholder="选择日期时间" readonly>
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="上月欠费情况" style="width: 98%">
                    <el-input v-model="bz" readOnly></el-input>
                </el-form-item>
                <el-form-item label="支付方式" style="width: 98%">
                    <el-radio-group v-model="zffs">
                        <el-radio label="1">
                            <i class="iconfont icon-zhifubao" style="font-size: 42px;color: #00A9F6;"></i>
                        </el-radio>
                        <el-radio label="2">
                            <i class="iconfont icon-weixin" style="font-size: 50px;color: #00C339;"></i>
                        </el-radio>
                        <el-radio label="3">
                            <i class="iconfont icon-qqqianbao" style="font-size: 56px;color: #1F2124;"></i>
                        </el-radio>
                        <el-radio label="55">
                            <img src="/static/images/yyb.png" alt="">
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="btn">
                <el-button @click.native="searchReturn">返回搜索</el-button>
                <el-button v-if="loginInfo.user.qx!=0" type="primary" @click.native="searchPrint('code')"
                           style="margin-left: 20px;">扫码收钱
                </el-button>
                <el-button v-if="loginInfo.user.qx!=0" type="danger" @click.native="searchPrint('money')"
                           style="margin-left: 20px;">现金缴费
                </el-button>
            </div>
        </div>

        <!-- 打印区域 -->
        <div id="print" v-show="printShow">
            <h3 style="font-size:18px;font-weight:bold;text-align:center;">成都市蓉城出租汽车有限公司缴费记录单</h3>
            <div v-if="gslxShow" class="content" style="z-index: 3">
                <div style="border-bottom:1px solid #ccc;margin-bottom:5px;padding-bottom: 5px">
                    <span>车辆自编号: <b>{{recordForm.czbh}}</b></span>
                    <span style="margin-left:30px">所属公司: <b>{{recordForm.company}}</b></span>
                </div>
                <div class="item" style="margin-top:25px;">
                    <span style="display: inline-block;min-width: 225px;">应缴金额: {{recordForm.jfje}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费状态: {{recordForm.jfztTxt}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费月份: {{recordForm.jfyf}}</span>
                </div>
                <div class="item" style="margin-top:5px;">
                    <span style="display: inline-block;min-width: 225px;">已缴金额: {{recordForm.yjje}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费人: {{recordForm.jfr}}</span>
                    <!--<span style="display: inline-block;min-width: 225px;">欠费金额: {{recordForm.qfje}}</span>-->
                    <span style="display: inline-block;min-width: 225px;">记录员: {{recordForm.jlr}}</span>
                </div>
                <div class="item" style="margin-top:5px;">
                    <span style="display: inline-block;min-width: 225px;">本次缴费: {{recordForm.bcjfje}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费方式: {{jffs == 'code' ? '移动支付' : '现金支付'}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费时间: {{recordForm.jfsj}}</span>
                </div>
                <div class="item" style="margin-top:5px;">
                    <div>缴费详情:</div>
                    <div>{{recordForm.jfxq}}</div>
                </div>
                <!--<div style="width: 85%;position: relative; margin-top: -35px; font-family: 'Songti SC';font-size: 24px; font-weight: bold; color: #F7C5BC;">-->
                <!--<span style="display: inline-block; padding:5px 10px; border: 3px solid #FB826C; border-radius: 4px;transform:rotate(-7deg);float: right;margin-right: 10%">{{loginInfo.user.gsmc}}</span>-->
                <!--</div>-->
            </div>

            <div class="content" v-if="!gslxShow" style="z-index: 3">
                <div style="border-bottom:1px solid #ccc;margin-bottom:5px;padding-bottom: 5px">
                    <span>车辆自编号: <b>{{recordForm.czbh}}</b></span>
                    <span style="margin-left:30px">所属公司: <b>{{recordForm.company}}</b></span>
                </div>
                <div class="item" style="margin-top:25px;">
                    <span style="display: inline-block;min-width: 225px;">缴费金额: {{recordForm.bcjfje}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费人: {{recordForm.jfr}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费月份: {{recordForm.jfyf}}</span>
                </div>
                <div class="item" style="margin-top:5px;">
                    <span style="display: inline-block;min-width: 225px;">缴费方式: {{jffs == 'code' ? '移动支付' : '现金支付'}}</span>
                    <span style="display: inline-block;min-width: 225px;">记录员: {{recordForm.jlr}}</span>
                    <span style="display: inline-block;min-width: 225px;">缴费时间: {{recordForm.jfsj}}</span>
                </div>
                <!--<div class="item" style="margin-top:5px;">-->
                <!--<span style="display: inline-block;min-width: 225px;">缴费方式: {{jffs == 'code' ? '移动支付' : '现金支付'}}</span>-->
                <!--</div>-->
                <div class="item" style="margin-top:5px;">
                    <div>缴费详情:</div>
                    <div>{{recordForm.jfxq}}</div>
                </div>
            </div>
        </div>
        <el-dialog
                title="提示"
                :center="true"
                :visible.sync="dialogVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :show-close="false"
                style="margin-top: 25vh"
                width="35%">
            <div v-if="!dialogVisible200 && !dialogVisible201" style="margin: 15px 10px;">
                <input id="input1"
                       v-model="sqm"
                       @keyup.enter="CheckInfo"
                       class="sqmInput" placeholder="请扫描获取支付码"></input>
                <div style="padding-top: 20px;font-size: 18px;"><strong style="color: #00A61B;">注<sup>*</sup>：</strong>扫码前请确认选中输入框！
                </div>
            </div>

            <span v-show="dialogVisible200 || dialogVisible201" style="font-size: 20px;margin: 15px 10px;">
                <i class="iconfont icon-dayin" style="font-size: 22px;color: #DE8C2F"></i>
                保存数据并打印缴费信息！
            </span>
            <div v-show="noticeShow" style="text-align: center;font-size: 18px;color: #00A61B;margin-top: 20px;">
                {{noticeMsg}} <i class="el-icon-loading"></i>
            </div>
            <span v-show="dialogVisible200" slot="footer" class="dialog-footer"
                  style="margin: 15px 10px;margin-top: 20px;">

                <el-button type="warning" @click="cancel" style="margin-right: 15px;"
                           :disabled="disabled200">撤单并返回</el-button>

                <!--<el-button @click="saveData('callback')" style="margin-right: 15px;" :disabled="disabled200">取 消</el-button>-->
                <el-button type="primary" @click="saveData('print')" :disabled="disabled200">保存并打印</el-button>
            </span>
            <span v-show="dialogVisible201" slot="footer" class="dialog-footer"
                  style="margin: 15px 10px;margin-top: 20px;">

                <el-button type="warning" @click="cancel" style="margin-right: 15px;"
                           :disabled="disabled201">撤单并返回</el-button>

                <!--<el-button @click="payQuery('callback')" style="margin-right: 15px;" :disabled="disabled201">取 消</el-button>-->
                <el-button type="primary" @click="payQuery('print')" :disabled="disabled201">保存并打印</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="提示"
                :center="true"
                :visible.sync="XJdialogVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :show-close="false"
                style="margin-top: 25vh"
                width="35%">
            <span style="font-size: 20px;margin: 15px 10px;">
                <i class="iconfont icon-dayin" style="font-size: 22px;color: #DE8C2F"></i>
                保存数据并打印缴费信息!
            </span>
            <span slot="footer" class="dialog-footer" style="margin: 15px 10px;margin-top: 20px;">
                <el-button @click="searchReturn" style="margin-right: 15px;" :disabled="disabledMoney">返 回</el-button>
                <el-button type="primary" @click="saveData('print', 'money')" :disabled="disabledMoney">保存并打印</el-button>
                <!--<el-button type="primary" @click="printOK">打 印</el-button>-->
            </span>
        </el-dialog>
    </section>
</template>


<script>
    export default {
        directives: {
            focus: {
                inserted: function (el) {
                    el.focus();
                }
            }
        },
        data() {
            return {
                autofocus: false,
                nowMonth: this.$util.formatDate.format(new Date(), 'yyyy-MM'),
                loginInfo: JSON.parse(sessionStorage.getItem('userInfo')),
                company: JSON.parse(sessionStorage.getItem('company')),
                jfzt: {
                    0: '欠费',
                    1: '已缴清'
                },
                serachCzbh: '',
                jffs: '',
                tips: '',
                recordForm: {bcjfje: 0},
                searchShow: true,
                accountShow: false,
                printShow: false,
                loading: true,
                record: {},
                serachTime: '',
                jfr: [],
                je: {
                    zzs: '', cjs: '', jyffj: '', grsds: '',
                    dfjyfj: '', ztxdf: '', gxhf: '', fwf: '',
                    gps: '', pjf: '', wyj: '', gf: '',
                    bzj: '', dep: '', gbf: '', ejbyf: '',
                    cbj: '', dmsbgjj: '', qtfy: ''
                },
                yjje: 0,
                qfje: 0,
                bz: '',
//                src:'/static/images/已缴费.png',
                zffs: '1',
                sqm: '',
                dialogVisible: false,
                dialogVisible200: false,
                dialogVisible201: false,
                noticeShow: false,
                noticeMsg: '',
                ddh: '',
                tableData: [],
                total: 0,
                page: 1, //当前页
                size: 10,
                listLoading: false,
                XJdialogVisible: false,
                gslxShow: true,
                selectedFyzl: [],
                fyzl: ['gf', 'zzs', 'gps', 'cjs', 'fwf', 'gxhf', 'pjf', 'ztxdf',
                    'wyj', 'grsds', 'dep', 'jyffj', 'gbf', 'ejbyf', 'bzj', 'dfjyfj', 'cbj', 'dmsbgjj', 'qtfy'],
                checkAll: false,
                isIndeterminate: false,
                types: {
                    'zzs': '增值税', 'cjs': '城建税', 'jyffj': '教育费附加',
                    'grsds': '个人所得税', 'dfjyfj': '地方教育附加', 'ztxdf': '座套洗涤费',
                    'gxhf': '个协会费', 'fwf': '服务费', 'gps': 'GPS',
                    'pjf': '聘驾费', 'wyj': '违约金', 'gf': '规费',
                    'bzj': '保证金', 'dep': '定额票', 'gbf': '工本费',
                    'ejbyf': '二级保养费', 'cbj': '承包金', 'dmsbgjj': '代买社保公积金',
                    'qtfy': '其他费用'
                },
                disabled201: false,
                disabled200: false,
                disabledMoney: false
            }
        },
        computed: {},
        mounted() {
        },
        created() {
            this.serachTime = this.nowMonth;
        },
        methods: {
            timeChange(val) {
                console.log(val);
                if (this.serachTime != '') {
                    this.getData();
                }
            },
            handleSizeChange(val) {
                this.page = val;
                this.getData();
            },
            gotoPay(data) {
                this.serachCzbh = data.czbh;
                this.search();
            },
            getData() {
                this.listLoading = true;
                let param = {
                    date: this.serachTime,
                    page: this.page,
                    size: this.size,
                    rzdm: this.$rzdm
                }
                this.$axios.post(this.$url + '/payrecords/searchorder', this.$qs.stringify(param)).then(res => {
                    let data = res.data;
                    this.listLoading = false;
                    if (data.resCode == 200) {
                        let data2 = data.data;
                        this.tableData = data2.content;
                        this.total = data2.totalElements;
                    } else {
                        this.$message({
                            message: res.data.resMsg,
                            type: 'error',
                            duration: 5000
                        });
                    }
                })
            },
            formatterCompany(row, index){
                return this.company[row.gsdm];
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getData();
            },
            CheckInfo() {
                if (event.keyCode == 13) {
                    this.searchEnterFun();
                }
            },
            //扫码枪扫码后
            searchEnterFun() {
                const loading = this.$loading({
                    lock: false,
                    text: '扫码收钱中......',
                    background: 'rgba(0, 0, 0, 0.7)',
                    customClass: 'loading'
                })
                let param = {
                    amt: this.recordForm.bcjfje,
                    barcode: this.sqm,
                    channel: this.zffs,
                    rzdm: this.$rzdm
                }
                this.$axios.post(this.$url + '/payrecords/payorder', this.$qs.stringify(param)).then(res => {
                    loading.close();
                    if (res.data.resCode == 200) {
                        this.jffs = 'code';
                        this.dialogVisible200 = true;
                        this.ddh = res.data.data;
                    } else if (res.data.resCode == 201) {
                        this.ddh = res.data.data;
                        this.dialogVisible201 = true;
                    } else {
                        this.dialogVisible = false;
                        this.$message({
                            message: res.data.resMsg + '，交易失败，试试其他支付方式或者刷新重试!',
                            type: 'error',
                            duration: 5000
                        });
                        this.sqm = '';
                    }
                }).catch(() => {
                        loading.close();
                        this.$router.push({path: '/error'});
                    }
                );
            },
            cancel() {
                this.noticeMsg = '正在撤单，请稍后';
                this.noticeShow = true;
                this.$axios.post(this.$url + '/payrecords/payclose', this.$qs.stringify({
                    orderno: this.ddh,
                    rzdm: this.$rzdm
                })).then(res => {
                    if (res.data.resCode == 200) {
                        this.$message.success("订单已撤销");
                        this.dialogVisible = false;
                        if (this.dialogVisible201) {
                            this.dialogVisible201 = false;
                        }
                        if (this.dialogVisible200) {
                            this.dialogVisible200 = false;
                        }
                        if (this.noticeShow) {
                            this.noticeShow = false;
                        }
                        this.sqm = '';
                        this.searchReturn();
                    } else {
                        this.$message({
                            message: res.data.resMsg,
                            type: 'error',
                            duration: 5000
                        });
                    }
                }).catch((err) => {
                        alert(err);
                        this.$router.push({path: '/error'});
                    }
                );
            },
            payQuery(type) {
                this.disabled201 = true;
                this.$axios.post(this.$url + '/payrecords/payquery', this.$qs.stringify({
                    orderno: this.ddh,
                    rzdm: this.$rzdm
                })).then(res => {
                    if (res.data.resCode == 200) {
                        this.saveData(type);
                        this.dialogVisible201 = false;
                    } else {
                        this.disabled201 = false;
                        this.$message.warning("正在等待支付");
                    }
                }).catch((err) => {
                    alert(err);
                });
            },
            isGslxShow() {
                return this.loginInfo.user.gslx === 1 ? true : false;
            },
            saveData(type, fkfs){   //fkfs不为空就是现金支付，
                if (this.dialogVisible200) {
                    this.disabled200 = true;
                }
                if(this.XJdialogVisible) {
                    this.disabledMoney = true;
                }
                if (type == 'print' || type == 'callback') {
                    this.noticeMsg = '正在保存缴费信息， 请稍后';
                    this.noticeShow = true;
                }

                this.recordForm.rzdm = this.$rzdm;
                this.recordForm.yjje = this.yjje;
                if (!this.recordForm.fxjje) {
                    this.recordForm.fxjje = 0;
                }
                if (!this.recordForm.xjje) {
                    this.recordForm.xjje = 0;
                }
                this.setYSMoney();
                this.recordForm.jfsj = this.$util.formatDate.format(new Date(), 'yyyy-MM-dd hh:mm:ss');

                if (fkfs) {
                    this.jffs = 'money';
                    this.recordForm.xjje = Math.round(parseFloat(this.recordForm.xjje) * 100 + parseFloat(this.recordForm.bcjfje) * 100) / 100;
                    this.recordForm.jfjl = this.recordForm.jfsj + '     ' + this.recordForm.jfr + '现金缴费' + this.recordForm.bcjfje + '元     记录员：' + this.loginInfo.user.gsmc + '-' + this.loginInfo.user.xm;
                } else {
                    this.jffs = 'code';
                    this.recordForm.fxjje = Math.round(parseFloat(this.recordForm.fxjje) * 100 + parseFloat(this.recordForm.bcjfje) * 100) / 100;
                    this.recordForm.jfjl = this.recordForm.jfsj + '     ' + this.recordForm.jfr + '移动支付' + this.recordForm.bcjfje + '元     记录员：' + this.loginInfo.user.gsmc + '-' + this.loginInfo.user.xm;
                }
                console.log(this.recordForm)
                let url = this.$url + '/payrecords/update';

                this.$axios.post(url, this.$qs.stringify(this.recordForm)).then((res) => {
                    this.disabled201 = false;
                    this.disabled200 = false;
                    this.disabledMoney = false;
                    if (res.data.resCode == 200) {
                        if (type == 'print') {
                            this.$message.success('已支付成功');
                            let jfxq = [];
                            this.selectedFyzl.forEach(item => {
                                jfxq.push(this.types[item] + '：' + this.je[item]);
                            })
                            this.recordForm.jfxq = jfxq.join('；') + '。';
                            this.searchReturn();
                            this.gslxShow = this.isGslxShow();
                            window.setTimeout(() => {
                                this.print();
                            }, 500)
                        } else if (type == 'callback') {
                            this.searchReturn();
                            this.$message.success('已支付成功');
                        }
                        this.$message.success("交易成功！");
                    } else {
                        if(fkfs) {
                            this.$message({
                                message: res.data.resMsg,
                                type: 'error',
                                duration: 5000
                            });
                        }else {
                            //系统保存数据失败，为客户撤单，重新缴费
                            this.$axios.post(this.$url + '/payrecords/payclose', this.$qs.stringify({
                                orderno: this.ddh,
                                rzdm: this.$rzdm
                            })).then(res => {
                                if (res.data.resCode == 200) {
                                    this.$notify.error({
                                        title: '错误',
                                        message: '系统保存数据失败,已为客户撤单,需重新缴费',
                                        duration: 5000
                                    });
                                    this.dialogVisible = false;
                                } else {
                                    this.$message({
                                        message: res.data.resMsg,
                                        type: 'error',
                                        duration: 5000
                                    });
                                }
                            }).catch((err) => {
                                alert(err);
                            });
                        }
                    }
                }).catch((err) => {   //在调用保存数据接口出错，撤单
                    if(!fkfs) {
                        const loading = this.$loading({
                            lock: false,
                            text: '保存数据出错，正在尝试撤单中...',
                            background: 'rgba(0, 0, 0, 0.7)',
                            customClass: 'loading'
                        })
                        //系统保存数据失败，为客户撤单，重新缴费
                        this.$axios.post(this.$url + '/payrecords/payclose', this.$qs.stringify({
                            orderno: this.ddh,
                            rzdm: this.$rzdm
                        })).then(res => {
                            loading.close();
                            if (res.data.resCode == 200) {
                                this.$notify.error({
                                    title: '错误',
                                    message: '系统保存数据失败,已为客户撤单,需重新缴费',
                                    duration: 5000
                                });
                                this.dialogVisible = false;
                            } else {
                                this.$message({
                                    message: res.data.resMsg,
                                    type: 'error',
                                    duration: 5000
                                });
                            }
                        }).catch((err) => {
                            alert(err)
                        });
                    }
                });
            },
            searchPrint (type) {
                if (!this.recordForm.jfr) {
                    this.$message({
                        message: '请选择缴费人',
                        type: 'warning'
                    });
                    return 0;
                }
                if (!this.zffs) {
                    this.$message({
                        message: '请选择支付方式',
                        type: 'warning'
                    });
                    return 0;
                }
                if (!this.recordForm.bcjfje) {
                    this.$message({
                        message: '缴费金额必须大于0',
                        type: 'warning'
                    });
                    return 0;
                }
                if (this.recordForm.bcjfje <= 0) {
                    this.$message({
                        message: '缴费金额不能小于或等于0',
                        type: 'warning'
                    });
                    return 0;
                } else {
                    if (this.recordForm.qfje == this.qfje) {
                        this.$message({
                            message: '缴费金额不能大于应缴金额',
                            type: 'warning'
                        });
                        return 0;
                    }
                }
                if (this.recordForm.qfje < 0) {
                    this.$alert('实收金额大于缴纳金额,请核对账目!', '温馨提示', {
                        confirmButtonText: '确定'
                    });
                    return false;
                }
                this.jffs = type;
                if (type == 'code') {
                    this.dialogVisible = true;
                    this.dialogVisible200 = false;
                    this.dialogVisible201 = false;
                    this.sqm = '';

                    setTimeout(() => {
                        document.getElementById('input1').focus();
                    }, 50)
                } else {
                    this.XJdialogVisible = true;
                }
            },
            setYSMoney() {
                this.fyzl.forEach(item => {
                    let ys = 'ys' + item;
                    this.recordForm[ys] = Math.round(parseFloat(this.recordForm[ys]) * 100 + parseFloat(this.je[item]) * 100) / 100;
                })
            },
            searchReturn() {
                this.dialogVisible = false;
                this.dialogVisible200 = false;
                this.dialogVisible201 = false;
                this.XJdialogVisible = false;
                this.noticeShow = false;
                this.searchShow = true;
                this.accountShow = false;
                this.printShow = false;
                this.serachCzbh = '';
                this.tips = '';
            },
            search(){
                if (!this.serachCzbh) {
                    this.$message({
                        message: '请选择查询的车辆编号',
                        type: 'warning'
                    });
                    return 0;
                }
                if (this.serachCzbh) {
                    const loading = this.$loading({
                        lock: true,
                        text: '正在查询车辆信息,请稍后......',
                        background: 'rgba(0, 0, 0, 0.7)',
                        customClass: 'loading'
                    });
                    let url = this.$url + '/payrecords/search';
                    let param = {
                        page: 1,
                        size: 10,
                        czbh: this.serachCzbh,
                        date: this.serachTime,
                        rzdm: this.$rzdm
                    };
                    this.$axios.post(url, this.$qs.stringify(param)).then((res) => {
                        let data = res.data;
                        if (data.resCode == 200) {
                            let record = data.data.content;
                            console.log(record);
                            if (record && record.length) {
                                if (record[0].shzt !== 2) {
                                    this.$message.error("该车辆未通过审核");
                                } else {
                                    this.recordForm = record[0];
                                    this.yjje = record[0].yjje;
                                    this.qfje = record[0].qfje;
                                    this.recordForm.jfztTxt = this.jfzt[this.recordForm.jfzt];
                                    this.recordForm.jlr = this.loginInfo.user.xm;
                                    this.recordForm.company = this.company[this.recordForm.gsdm];// || this.loginInfo.user.gsmc;
                                    this.recordForm.jfsj = this.$util.formatDate.format(new Date(), 'yyyy-MM-dd hh:mm:ss');
                                    this.recordForm.bcjfje = 0;
                                    this.zffs = '1';
                                    this.sqm = '';
                                    this.jfr = this.recordForm.czcsj == null ? '' : this.recordForm.czcsj.split(',');
//                                    this.recordForm.jfr = this.jfr=='' ? '' : (this.jfr.length > 1 ? this.jfr[0] : '');

                                    this.recordForm.jfr = '';


                                    if (record.length > 1) {
                                        if (record[1].jfzt == 1) {
                                            this.bz = record[1].jfyf.substring(0, 4) + '年' + record[1].jfyf.substring(5, 7) + '月已缴清全部费用';
                                        } else {
                                            this.bz = record[1].jfyf.substring(0, 4) + '年' + record[1].jfyf.substring(5, 7) + '月欠费' + record[1].qfje + '元';
                                        }
                                    } else {
                                        this.bz = '无';
                                    }
                                    //
                                    for (let key in this.je) {
                                        this.je[key] = 0;
                                    }
                                    for (let i = 0; i < this.fyzl.length; i++) {
                                        let ys = 'ys' + this.fyzl[i];
                                        this.je[this.fyzl[i]] = Math.round(this.recordForm[this.fyzl[i]] * 100 - this.recordForm[ys] * 100)/100;
                                    }
                                    this.selectedFyzl = [];

                                    if (!this.recordForm.company) {
                                        this.$message.error("公司名称为空，请尝试重新查询");
                                    }
                                    this.searchShow = false;
                                    this.accountShow = true;
                                    this.printShow = false;
                                }
                            } else {
                                this.tips = '未查询到车辆编号为: ' + this.serachCzbh + '，月份为：' + (this.serachTime == '' ? this.nowMonth : this.serachTime) + ' 的缴费情况！'
                            }
                        } else {
                            this.$message({
                                message: res.data.resMsg,
                                type: 'error',
                                duration: 5000
                            });
                        }
                        setTimeout(function () {
                            loading.close();
                        }, 600);
                    }).catch(() => {
                            loading.close();
                            this.$router.push({path: '/error'});
                        }
                    );
                }
            },
            print(){
                let newWindow = window.open('_blank');   //  打开新窗口
                let codestr = document.getElementById('print').innerHTML;   //  获取需要生成pdf页面的div代码
                newWindow.document.write(codestr);   //  向文档写入HTML表达式或者JavaScript代码
                newWindow.document.close();          //  关闭document的输出流, 显示选定的数据
                newWindow.print();   //  打印当前窗口
                newWindow.close(); //关闭窗口
                this.searchReturn();
                return true;
            },
            formatterSHZT(row) {
                let shzt = '';
                switch (row.shzt) {
                    case 0:
                        shzt = '新建';
                        break;
                    case 1:
                        shzt = '审核中';
                        break;
                    case 2:
                        shzt = '审核通过';
                        break;
                    case 3:
                        shzt = '审核不通过';
                        break;
                    case 4:
                        shzt = '修改中';
                        break;
                    case 5:
                        shzt = '已修改审核中';
                        break;
                    default:
                        shzt = '';
                        break;
                }
                return shzt;
            }
        },
    }
</script>
<style lang="scss">
    .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
        background-color: RGBA(0, 155, 27, .8);
        border-color: RGBA(0, 155, 27, .8);
    }

    .el-checkbox__input.is-disabled + span.el-checkbox__label {
        color: #777;
    }

    .fyzl-item {
        display: inline-block;
        width: 49%;
        font-size: 14px;
        .fyzl-item-td {
            display: inline-block;
            margin-left: 10px;
        }
    }

    #input1 {
        font-size: 18px;
        width: 90%;
        padding: 8px 18px;
    }

    #input1:focus {
        outline: #00A61B auto 2px;
        border: 1px solid #00A61B;
        border-radius: 3px;
    }

    .loading {
        .el-loading-spinner .el-loading-text {
            font-size: 25px;
            color: #fff;
        }
    }

    .search-box {
        width: 720px;
        margin: auto;
        .title {
            /*margin-top: 70px;*/
            font-size: 36px;
            color: #199025;
            text-align: center;
        }
        .search {
            margin-top: 30px;
            .el-input {
                font-size: 18px;
                padding: 5px 0;
            }
            #clbh .el-input {
                width: 350px;
                margin-left: 50px;
            }
            #time .el-input {
                width: 150px;
            }
            .el-button {
                font-size: 15px;
                /*float:left;*/
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
    }

    .btn {
        text-align: center;
        margin-top: 15px;
    }

    .account-form {
        .account-title {
            /*text-align: center;*/
            padding-bottom: 7px;
            font-size: 28px;
            margin-bottom: 40px;
            border-bottom: 1px solid #ccc;
            color: #696969;
            .company {
                margin-left: 60px;
            }
            .number {
                color: #0B911B;
                font-weight: bold;
            }
        }
        .el-form-item__label {
            font-size: 16px;
        }
        .el-form-item {
            width: 49%;
            float: left;
            margin-bottom: 18px;
        }
        .el-radio {
            margin: 10px 40px;
            .el-radio__inner {
                width: 20px;
                height: 20px;
            }
        }

        .el-input__inner {
            color: #707276;
            font-weight: normal;
        }
        .txt {
            width: 98%;
            .el-textarea__inner {
                font-size: 16px;
                height: 80px;
                resize: none !important;
            }
        }
    }

    .print {
        .print-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
        }
    }
</style>