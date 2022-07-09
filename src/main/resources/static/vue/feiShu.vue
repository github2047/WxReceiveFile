<template>
  <div id="loading" style="width: 100%;height: 100vh" v-loading="true"
       element-loading-text="拼命加载中"
       element-loading-spinner="el-icon-loading"
       element-loading-background="rgba(0, 0, 0, 0.8)">
  </div>
<!--  <div id="ts"></div>-->
</template>
<script>
module.exports = {
  name: "feiShu",
  data() {
    return {
      code: window.localStorage.getItem("code"),
      userid: '',
      name:'',
      status:0,
      fileList: []
    }
  },
  mounted() {
    try{
      axios.get('/feiShu/code', {params: {code: this.code}}).then(({data})=> {
        if(data.status==200){
          this.userid = data.userid;
          window.location.href="/home?userid="+this.userid;
        }else if(data.status==500) {
          const loading=document.getElementById("loading")
          const app=document.getElementById("app")
          app.removeChild(loading);
          if (data.msg === "授权码错误") {
            this.$router.go(0);
          }else if(data.msg=="用户不存在"){
            // document.getElementById("ts").innerHTML="你不在该应用中"
            this.$toast.fail("你不在该应用中")
          }else{
            // document.getElementById("ts").innerHTML="飞书登录失败,请退出重新登录"
            this.$toast.fail("飞书登录失败！")
          }
        }else{
          const loading=document.getElementById("loading")
          const app=document.getElementById("app")
          app.removeChild(loading);
          this.$toast.fail("飞书登录失败,请退出重新登录！")
        }
      })
    }catch (e) {
      this.$notify(e.message)
    }
  },
  methods: {
    submitUpload() {
      alert(this.fileList)
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    }
  }
}

</script>

<style scoped>
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