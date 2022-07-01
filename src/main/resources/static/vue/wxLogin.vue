<template>
  <div>
    <div id="login_container"></div>
  </div>
</template>

<script>
module.exports={
  name: "wxLogin",
  data() {
    return {

    }
  },
  mounted() {
    this.WxQRCode()
  },
  methods: {
    WxQRCode() {
      let uri=encodeURIComponent("http://ngrok.tianyihui.cc:8088/wx/Wx")
      let url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8a0a151278949bbd&redirect_uri="+uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
      window.location.href = url;
    },
  }
}
</script>

<style scoped>

</style>