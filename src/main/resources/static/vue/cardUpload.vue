<template>
<div>
  <div>
    <div>
      <van-nav-bar
          style="margin-top: 40px;height: 100px;"
          title="证件上传"
          left-text="返回"
          left-arrow
          @click-left="onClickLeft"
      ></van-nav-bar>
    </div>
    <div>
      <van-uploader :after-read="ZmUp">
        <div style="width: 600px;text-align: center;margin: 50px 30%;">
          <img :src="imgZm" width="600px" height="400px" id="pdfDom">
          <div style="font-size: 40px;text-align: center;margin-top: 30px">正面</div>
        </div>
      </van-uploader>
    </div>
    <div>
      <van-uploader :after-read="BmUp" >
        <div style="width: 600px;text-align: center;margin: 50px 30%;">
          <img :src="imgBm" width="600px" height="400px">
          <div style="font-size: 40px;text-align: center;margin-top: 30px">反面</div>
        </div>
      </van-uploader>
    </div>
  </div>
  <div>
   <iframe :src="pdf" width="90%" height="800px" style="margin-left: 30px;"></iframe>
    <el-button style="width: 98%;height: 200px;font-size: 50px;margin:100px 15px;" type="success" :loading="loading" @click="sc()">上传</el-button>
    <el-button style="width: 98%;height: 200px;font-size: 50px;margin:100px 15px;" type="success" :loading="loading" @click="">pdf</el-button>
  </div>
</div>
</template>

<script>
let ElMessage = window.ELEMENT.Message;
module.exports= {
  name: "cardUpload",
  data(){
    return{
      imgZm:"static/image/正面.png",
      imgBm:"static/image/背面.png",
      ZmFile:null,
      BmFile:null,
      loading:false,
      pdf:""
    }
  },
  mounted() {
  },
  methods: {
    onClickLeft() {
      this.$router.push("/home")
    },
    ZmUp(file) {
      console.log(file)
      this.pdf=file.content;
      if(file.file.size>(20*1024*1024)){
        ElMessage({
          message:"文件大小不可以超过20MB",
          grouping:true,
          type:"error"
        })
        return false;
      }
      if(file.file.type!="image/png" && file.file.type!="image/jpeg"){
        ElMessage({
          message:"只能选择jpg、png、jpeg格式的图片",
          grouping:true,
          type:"error"
        })
        return false;
      }
      console.log("44444")
      this.ZmFile=file.file;
      this.imgZm=file.content;
    },
    BmUp(file) {
      if(file.file.size>(20*1024*1024)){
        ElMessage({
          message:"文件大小不可以超过20MB",
          grouping:true,
          type:"error"
        })
        return false;
      }
      if(file.file.type!="image/png" && file.file.type!="image/jpeg"){
        ElMessage({
          message:"只能选择jpg、png、jpeg格式的图片",
          grouping:true,
          type:"error"
        })
        return false;
      }
      this.BmFile=file.file
      this.imgBm=file.content;
    },
    sc(){
      if(this.ZmFile==null){
        ElMessage({
          dangerouslyUseHTMLString:true,
          message: "<div style='font-size: 50px'>请上传正面照</div>",
          grouping: true,
          type: 'error',
        })
        return false;
      }
      if(this.BmFile==null){
        ElMessage({
          dangerouslyUseHTMLString:true,
          message: "<h1 style='font-size: 50px'>请上传反面照</h1>",
          grouping: true,
          type: 'error',
        })
        return false;
      }
      this.upload(this.ZmFile)
      this.upload(this.BmFile)
      this.imgZm="static/image/正面.png"
      this.imgBm="static/image/背面.png"
      this.ZmFile=null;
      this.BmFile=null;
    },
    upload(file){
      this.loading=true;
      let formData=new FormData();
      formData.append('userid',"456")
      formData.append('file',file)
      axios.post("http://mytest.vaiwan.com/feiShu/upload",file).then(r=>{
        if(r.data.status==200){
          this.loading=false;
        }else{
          ElMessage({
            message: "上传失败"+r.data.message,
            grouping: true,
            type: 'error',
          })
          this.loading=false;
          return false;
        }
      }).catch(e=>{
        ElMessage({
          message: e.message,
          grouping: true,
          type: 'error',
        })
      }).finally(f=>{
        this.loading=false;
      })
    },
  }
}
</script>

<style scoped>
.van-nav-bar__title {
  padding: 30px;
  font-size: 50px;
}
.van-nav-bar .van-icon,.van-nav-bar__text {
  padding: 30px 5px;
  font-size: 50px;
}
</style>