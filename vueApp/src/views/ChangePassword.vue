<template>
    <section>
        <el-form :model="userForm" label-width="80px" class="user-form" ref="userForm">
            <el-form-item label="账号">
                <el-input v-model="userForm.zh" disabled></el-input>
            </el-form-item>
            <el-form-item label="原密码"
                          prop="mm"
                          :rules="[{ required: true, message: '密码不能为空'}]">
                <el-input type="password" v-model="userForm.ymm"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="xmm"
                          :rules="[{ required: true, message: '新密码不能为空'}]">
                <el-input type="password" v-model="userForm.xmm"></el-input>
            </el-form-item>
            <div class="form-footer">
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">修改密码</el-button>
            </div>
        </el-form>

    </section>
</template>

<script>
    export default {
        data() {
            return {
                addLoading:false,
                userForm:JSON.parse(sessionStorage.getItem('userInfo')),
            }
        },
        created(){
        },
        methods: {
            //修改
            addSubmit: function () {
                this.$refs.userForm.validate((valid) => {
                        this.$axios.post('/user/changePassword',{ymm: this.userForm.ymm, xmm: this.userForm.xmm}).then((res) => {
                                this.$message.success(res.resMsg);
                                this.userForm = JSON.parse(sessionStorage.getItem('userInfo'));
                                this.$router.push('/login')
                        });
                });
            }
        },
    }
</script>

<style scoped lang="scss">
    .user-form{
        /*margin-top: 30px;*/
        padding: 50px 90px 60px 90px;
        /*padding-top: 90px;*/
        border: 1px solid rgba(230, 230, 230, 0.4);
        box-shadow: 1px 2px 4px rgba(230, 230, 230, 0.4);
        border-radius: 5px;
        width: 600px;
        margin: 30px auto;
        /*padding-top:25px;*/
        .form-footer{
            text-align: center;
        }
    }
    .user-form:hover {
        border: 1px solid rgba(0, 0, 0, 0.2);
        box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.4);
    }
</style>