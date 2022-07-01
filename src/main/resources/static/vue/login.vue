<template>
<div>
<!--  <el-button size="medium" type="info" plain style="width: 920px;height: 300px;margin: 100px 20px;font-size: 50px" @click="ToWx">微信登录</el-button>-->
<!--  <el-button size="medium" type="info" plain style="width: 920px;height: 300px;margin: 30px 20px;font-size: 50px" @click="WxQRCode">企业微信登录</el-button>-->
</div>
</template>

<script>
module.exports= {
  name: "login",
  data() {
    return {

    }
  },
  mounted() {
    this.WxQRCode();
  },
  methods: {
    WxQRCode() {
      axios.get("/getQrCode").then(r => {
        let redirect_uri = encodeURIComponent(r.data.redirect_uri);
        let url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + r.data.appid + '&redirect_uri=' + redirect_uri + '&response_type=code&scope=snsapi_privateinfo&state=STATE&agentid=' + r.data.agentid + '#wechat_redirect'
        window.location.href = url;
      })
    },
    ToWx(){
      this.$router.push("/wx")
    }
  }
}
</script>

<style scoped>

</style>