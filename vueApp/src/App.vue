<template>
	<div id="app">
		<transition name="fade"
		            mode="out-in">
			<router-view></router-view>
		</transition>
	</div>
</template>

<script>
export default {
	name: 'app',
	components: {
	}
}

</script>

<style lang="scss">
    @import "../static/css/iconfont/iconfont.css";

    body {
	margin: 0px;
	padding: 0px;
	font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
	font-size: 14px;
	-webkit-font-smoothing: antialiased;
}

#app {
	position: absolute;
	top: 0px;
	bottom: 0px;
	width: 100%;
}

.el-submenu [class^=fa] {
	vertical-align: baseline;
	margin-right: 10px;
}

.el-menu-item [class^=fa] {
	vertical-align: baseline;
	margin-right: 10px;
}
.toolbar1 {
    margin-top:8px;
    padding: 8px;
    box-shadow: 0 2px 4px rgba(0, 166, 27, 0.2);
    margin-bottom: 8px;
}


#toolbar {
    .el-input--small .el-input__inner {
        width: 200px;
    }
    .el-form--inline .el-form-item {
        vertical-align: middle;
        margin: 3px 10px;
        margin-right: 15px;
    }
    .el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label {
        font-size: 16px;
        color: #6A6C70;
    }
    .el-form-item__label {
        padding: 0px 5px 0px 0px;
    }
    /*.el-notification {*/
        /*overflow: auto;*/
        /*overflow-x: hidden;*/
        /*height: 600px;*/
    /*}*/

    .czbh {
        .el-input--small .el-input__inner {
            width: 180px;
        }
    }
    .date {
        .el-input--small .el-input__inner {
            width: 130px;
        }
        .el-date-editor.el-input, .el-date-editor.el-input__inner {
            width: 130px;
        }
    }
}
.account-form .el-input__inner, .account-form .el-textarea__inner, .user-form .el-input__inner, .user-form .el-textarea__inner, .company-form .el-input__inner, .company-form .el-textarea__inner  {
    color: #707276;
    font-weight: normal;
}
.account-form  .el-textarea__inner, .user-form .el-textarea__inner , .company-form .el-textarea__inner, .company-form .el-textarea__inner{
    font-size: 16px;
}
.account-form  .el-input.is-disabled .el-input__inner, .account-form .el-textarea.is-disabled .el-textarea__inner, .user-form .el-input.is-disabled .el-input__inner , .user-form .el-textarea.is-disabled .el-textarea__inner, .company-form .el-input.is-disabled .el-input__inner, .company-form .el-textarea.is-disabled .el-textarea__inner{
    background-color: #FAFAFA;
    border-color: #DCDFE6;
    color: #707276;
}
.toolbar2 {
	background: #f2f2f2;
	padding: 7px;
	//border:1px solid #dfe6ec;
	/*margin: 10px 0px;*/
}
.bottom-toolbar {
    margin-top:8px;
    padding: 8px 15px;
    box-shadow: 0 2px 5px #eee;
}



.fade-enter-active,
.fade-leave-active {
	transition: all .2s ease;
}

.fade-enter,
.fade-leave-active {
	opacity: 0;
}
</style>