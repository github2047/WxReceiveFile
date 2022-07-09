<template>
  <div id="loading" style="width: 100%;height: 100vh" v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)">
  </div>
</template>

<script>
module.exports = {
  name: "ding",
  data() {
    return {
      loading:true,
      code: window.localStorage.getItem("code"),
      userid: '',
      name:'',
      status:0,
      fileList: [],
    }
  },
  mounted() {
      axios.get('/ding/ding', {params: {code: this.code}}).then(({data})=> {
        if(data.status==200){
          this.userid = data.userid;
          window.location.href="/home?userid="+this.userid;
        }
        else if(data.status==500){
          const loading=document.getElementById("loading")
          const app=document.getElementById("app")
          app.removeChild(loading);
          if(data.msg=="授权码错误")
          {
            this.$router.go(0);
          }else if(data.msg=="用户不存在"){
            this.$toast.fail("你不在该应用中")
          }else{
            this.$toast.fail("钉钉登录失败！")
          }
        }
      })
  },
  methods: {
  }
}

</script>

<style scoped>
*{
  margin: 0px;
  padding: 0px;
}
.upload-demo{
  position: absolute;
  left: 10%;
  top: 40%;

}
.dingContainer {

  padding-left: 2%;
  padding-top: 2%;
}
</style>