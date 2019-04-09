<template>
    <div class="login">

        <div class="login-area">
            <div class="company-title clearfix">

                <div class="text">防护/防化/通风设备厂商报价工具软件</div>
                <div class="img">
                    <img src="../assets/images/yz.png" alt="">
                    <img src="../assets/images/taxi.png" alt="">
                </div>

            </div>
            <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px"
                     class="login-container">
                <h3 class="title">系统登录</h3>
                <el-form-item prop="account" style="margin-bottom:20px;">
                    <el-input class="input-item" type="text" v-model="ruleForm2.account" auto-complete="off"
                              placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item prop="password" style="margin-bottom:20px;">
                    <el-input @keyup.enter.native="loginSubmit" class="input-item" type="password"
                              v-model="ruleForm2.password" auto-complete="off" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item style="width:100%;">
                    <el-button type="success" style="width:100%;font-size:20px;font-weight: bold;"
                               @click.native.prevent="loginSubmit" :loading="logining">
                        登 录
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                logining: false,
                ruleForm2: {
                    account: 'user',
                    password: '666666'
                },
                rules2: {
                    account: [
                        {required: true, message: '请输入账号', trigger: 'blur'},
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                    ]
                },
                userInfo: {},
            };
        },
        methods: {
            loginSubmit(ev) {
                this.$refs.ruleForm2.validate((valid) => {
                    if (valid) {
                        this.logining = true;
                        var loginParams = {zh: this.ruleForm2.account, mm: this.ruleForm2.password};
                        this.$axios.post('/user/login', loginParams).then((res) => {
                            this.logining = false;
                            sessionStorage.setItem('userInfo', JSON.stringify(res.data));
                            this.$router.push({path: '/Discount'});
                        });
                    }
                });
            }
        }
    }

</script>

<style lang="scss" scoped>
    .login {
        position: relative;
        width: 100%;
        height: 100%;
        background: url("../assets/images/login-bg.jpg");
        overflow: auto;
        .login-area {
            position: relative;
            width: 1100px;
            margin: auto;
            .company-title {
                position: absolute;
                top: 280px;
                color: #fff;
                font-size: 48px;
                font-weight: bold;
                font-family: "Songti SC";
                /*width: 560px;*/
                .text {
                    text-shadow: 5px 5px 5px green;
                    margin-left: 25px;
                    text-align: center;
                    vertical-align: middle;
                }
                .img {
                    margin-top: 35px;
                    width: 100%;
                    text-align: center;
                    padding-left: 70px;
                    img {
                        margin: 25px;
                        float: left;
                        width: 150px;
                    }
                }

            }
            .login-container {
                position: absolute;
                right: 0px;
                top: 230px;
                width: 320px;
                padding: 35px 40px;
                background: #0bb81f;
                box-shadow: 0 0 25px #0b7f17;
                border-radius: 5px;
                .title {
                    font-size: 30px;
                    margin: 0px auto 20px auto;
                    text-align: center;
                    color: #fff;
                }

                .remember {
                    margin: 0px 0px 35px 0px;
                }
                .input-item {
                    input {
                        background: transparent;
                    }
                    font-size: 20px !important;
                    font-weight: bold;
                    color: #000;
                }
            }
        }

    }

</style>