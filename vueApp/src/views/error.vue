<template>
    <div style="display: flex;justify-content: center;align-items: center">
        <span style="margin-top: 30px;">
            <p class="page-container" style="font-size: 22px;"><strong>服务器出错了</strong><br/>数据库可能断开连接了<br/><br/></p>
            <div style="margin-top:20px;display: flex;justify-content: space-around;">
                <el-button type="text" @click="gotoHome" style="color:#00A61B">去首页</el-button>
                <el-button type="text" @click="fresh" style="color:#00A61B;margin-left:30px;">刷新试试</el-button>
            </div>
        </span>
        <img src="/static/images/cry.gif" alt="">
    </div>
</template>
<script>
    export default {
        methods: {
            gotoHome() {
                this.$router.push({ path: '/Search' });
            },
            fresh() {
                window.location.reload();
                this.$router.push({ path: '/Search' });
            },
        }
    }
</script>
<style lang="scss" scoped>
    .page-container {
        font-size: 20px;
        text-align: center;
        color: rgb(192, 204, 218);
    }
</style>