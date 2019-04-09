<template>
    <section>
        <el-form :model="userForm" label-width="100px" class="user-form" ref="userForm">
            <el-form-item label="账号">
                <el-input v-model="userForm.zh" disabled></el-input>
            </el-form-item>
            <el-form-item label="修改密码"
                          prop="mm"
                          :rules="[{ required: true, message: '密码不能为空'}]"
            >
                <el-input type="password" v-model="userForm.mm"></el-input>
            </el-form-item>
            <el-form-item label="确认密码">
                <el-input type="password" v-model="userForm.qrmm"></el-input>
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
                userForm:JSON.parse(sessionStorage.getItem('userInfo'))
            }
        },
        created(){
        },
        methods: {
            //修改
            addSubmit: function () {
                this.$refs.userForm.validate((valid) => {
                    if(this.userForm.mm == this.userForm.qrmm){
                        let url = this.$url + '/user/changepassword';
                        let param ={
                            id: this.userForm.id,
                            rzdm: this.$rzdm,
                            newword: this.userForm.mm
                        };
                        this.$axios.post(url,this.$qs.stringify(param)).then((res) => {
                            let data = res.data;
                            if(res.data.resCode == 200){
                                this.$message.success('密码修改成功,请牢记!');
                                this.addFormVisible = false;
                                this.$refs.userForm.resetFields()
                            }else{
                                this.$message.error(res.data.resMsg);
                            }
                        });
                    }else{
                        this.$message.error('两次密码输入不一致!');
                    }

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
        width: 650px;
        margin: 30px auto;
        /*padding-top:25px;*/
        .form-footer{
            text-align: center;
        }
    }
    .user-form:hover {
        border: 1px solid rgba(0, 165, 38, 0.2);
        box-shadow: 1px 2px 4px rgba(0, 165, 38, 0.4);
    }
</style>