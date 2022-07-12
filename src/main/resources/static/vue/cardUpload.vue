<template>
<div style="width: 100%;height: 100%;"
     v-loading="loading" :element-loading-text="loadingText">
  <div>
    <div>
      <van-uploader :after-read="ZmUp">
        <div style="width: 300px;text-align: center;margin: 10px 25px 30px;">
          <img id="imgZm" :src="imgZm" width="300px" height="190px">
          <div style="font-size: 16px;text-align: center;margin-top: 10px">正面</div>
        </div>
      </van-uploader>
    </div>
    <div>
      <van-uploader :after-read="BmUp" >
        <div style="width: 300px;text-align: center;margin: 20px 25px;">
          <img id="imgBm" :src="imgBm" width="300px" height="190px">
          <div style="font-size: 16px;text-align: center;margin-top: 10px">反面</div>
        </div>
      </van-uploader>
    </div>
  </div>
  <hr/>
  <div class="con" style="margin-top: 20px;font-size: 18px;">
    <div style="font-weight: bold;font-size:20px;margin-left: 5px;">打印设置</div>
    <hr/>
    <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">纸张尺寸</div>
    <div style="float: right;">
      <van-radio-group v-model="paperSize" direction="horizontal">
        <van-radio name="1">A3</van-radio>
        <van-radio name="2">A4</van-radio>
      </van-radio-group>
    </div>
    <br style="clear: both;"/>
    <hr />
    <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">色彩模式</div>
    <div style="float: right;">
      <van-radio-group v-model="printColor" direction="horizontal" @change="SzPrintColor">
        <van-radio name="1">黑白</van-radio>
        <van-radio name="3">彩色</van-radio>
      </van-radio-group>
    </div>
    <br style="clear: both;"/>
    <hr />
    <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">打印份数</div>
    <div style="float: right;">
      <van-stepper v-model="printNum" integer max="100" @overlimit="overLimit" />
    </div>
    <br style="clear: both;"/>
    <hr />
  </div>
  <van-dialog v-model="showPdf" title="预览">
    <div style="width: 100%;height: 500px;overflow: auto;background-color: #ccc;">
      <div :style="'height: 480px;overflow: auto;margin-top: 10px;filter: grayscale('+ImgColor+')'">
        <div v-for="(data,index) in PdfPath" >
          <img :src="'/upload/'+data" style="display:inline-block;width:90%;margin:5px 15px 0px" alt="">
          <div style="text-align: center;margin-top: 5px">{{index+1}}/{{ PdfPath.length }}</div>
        </div>
      </div>
    </div>
  </van-dialog>
  <div>
    <van-button style="width: 96%;height: 50px;font-size: 20px;margin: 30px 10px 10px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                border: 1px solid #ccc;background-color: #eee"
                type="default" @click="yl()">预览</van-button>
    <van-button style="width: 96%;height: 50px;font-size: 20px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                margin: 10px;" type="primary"
                @click="commit()">提交</van-button>
  </div>
</div>
</template>

<script>
let ElMessage = window.ELEMENT.Message;
module.exports= {
  name: "cardUpload",
  data(){
    return{
      userid:this.$route.query.userid,
      imgZm:"../static/image/正面.png",
      imgBm:"../static/image/背面.png",
      makePic: '',
      ZmFile:null,
      BmFile:null,
      loading:false,
      PdfPath:"",
      showPdf:false,
      isShow: false,
      paperSize:"2",
      printColor:"1",
      printNum:1,
      isUpload:false,
      fileListUuid:1,
      uuid:2,
      loadingText:"证件上传中...",
      ImgColor:1,
      pdfFilePath:"",
      fileName:"",
    }
  },
  mounted() {
  },
  methods: {
    onClickLeft() {
      this.$router.push({path: '/home', query: {userid: this.userid}})
    },
    SzPrintColor(){
      if(this.printColor=="1"){
        this.ImgColor=1;
      }
      if(this.printColor=="3"){
        this.ImgColor=0;
      }
    },
    overLimit(){
      this.$toast.fail("打印份数只能在1-100之间")
    },
    yl(){
      if(this.fileListUuid!=this.uuid){
        this.isShow=true;
        this.sc("show");
      }else{
        this.loading=false;
        this.showPdf=true;
      }
    },
    commit(){
      this.loading=true
      if(this.fileListUuid!=this.uuid){
        this.sc("submit");
      }else{
        if(this.isUpload){
          this.loading=false;
          this.printf();
        }else{
          this.sc('submit')
        }
      }
    },
    ZmUp(file) {
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
      this.ZmFile=file.file;
      this.imgZm=file.content;
      let uuid=Math.random();
      this.fileListUuid=uuid;
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
      let uuid=Math.random();
      this.fileListUuid=uuid;
    },
    drawProdPicture(type) {
      let time1=new Date().getTime();
      let imgZm = new Image()
      imgZm.src = this.imgZm
      imgZm.width = 1101/2
      imgZm.height = 638/2
      imgZm.setAttribute('crossOrigin', 'anonymous')
      let canvas = document.createElement("canvas")
      let context = canvas.getContext("2d")
      canvas.width = 2480/2
      canvas.height = 3508/2
      let imgBm = new Image()
      let flag = true
      // 将 img1 加入画布
      imgZm.onload = () => {
        context.drawImage(imgZm, 690/2, 858/2, 1101/2, 638/2)
        imgBm.src = this.imgBm
        imgBm.setAttribute('crossOrigin', 'anonymous')
        imgBm.width = 1101/2
        imgBm.height = 638/2
        // 将 img2 加入画布
        imgBm.onload = () => {
          context.drawImage(imgBm, 690/2, 2012/2, 1101/2, 638/2)
            let src = canvas.toDataURL('image/png')
            this.makePic = src
          let time2=new Date().getTime()
          this.$toast("提交耗时"+((time2 - time1) / 1000.0)+"秒");
          this.upload(type);
        }
      }
    },
    sc(type){
      this.loading=true;
      if(this.ZmFile==null){
        this.$notify("请上传正面照")
        this.loading=false;
        return false;
      }
      if(this.BmFile==null){
        this.$notify("请上传背面照")
        this.loading=false;
        return false;
      }
      this.drawProdPicture(type);
    },
    upload(type){
      try{
        let time1=new Date().getTime();
        this.loading=true;
        axios.post("/uploadFile/uploadCard", {
          "card":this.makePic,
          "userid":this.userid,
          "paperSize":this.paperSize,
          "printColor":this.printColor,
          "printNum":this.printNum
        }).then(({data})=>{
          let time2=new Date().getTime()
          this.$toast("上传耗时"+((time2 - time1) / 1000.0)+"秒");
          if(data.status==200){
            console.log(data)
            this.pdfFilePath=data.data;
            this.fileName="证件.pdf"
            if (type === 'submit') {
              this.loadingText="提交中...";
              this.printf();
            }else if (type === "show") {
              this.loadingText="预览生成中...";
              this.toImage();
            }
          }else{
            this.$notify("上传失败:"+data.msg)
            return false;
          }
        }).catch(e=>{
          this.$notify("上传失败"+e.message)
          this.loading=false;
          this.isShow=false;
          this.loadingText="证件上传中..."
        })
      }catch (e){
        this.$notify({
          type: "danger",
          message: e.message
        })
        this.loading=false;
      }

    },
    toImage(){
      let time1=new Date().getTime();
      axios.post("/uploadFile/toImage",{
        "pdfFilePath":this.pdfFilePath
      }).then(({data})=>{
        if (data.status == 200) {
          let time2=new Date().getTime()
          this.$toast("预览耗时"+((time2 - time1) / 1000.0)+"秒");
          console.log(data)
          // this.$toast.success('上传成功');
          this.uuid=this.fileListUuid;
          this.isUpload = true;
          this.PdfPath = data.data;
          this.showPdf = true;
        }else{
          this.$notify({
            type: "danger",
            message: "预览生成失败"+data.msg
          })
        }
      }).catch(e=>{
        this.$notify({
          type: "danger",
          message:"预览生成失败"+ e.message
        })
      }).finally(f => {
        this.loadingText="文件上传中..."
        this.loading = false;
        this.isShow=false;
      });
    },
    printf(){
      // console.log(this.fileName);
      let fileName =this.pdfFilePath
      let filePath=fileName.substring(fileName.lastIndexOf("/")+1)
      axios.post("/uploadFile/printf",{
        "fileName":this.fileName,
        "fileUrl":filePath,
        "loginName":this.userid,
        "printColor":this.printColor,
        "dorS":"ON",
        "paperSize":this.paperSize,
        "printNum":this.printNum
      }).then(r=>{
        if(r.data=="ok"){
          this.$toast.success("提交成功")
          this.$router.go(0)
        }else{
          this.$notify("提交失败："+r.data)
        }
      }).catch(e=>{
        this.$notify({
          type: "danger",
          message: e.message
        })
      }).finally(f => {
        this.loadingText="文件上传中..."
        this.loading = false;
      });
    }
  }
}
</script>

<style scoped>
.van-nav-bar__title {
  font-size: 20px;
}
.van-nav-bar .van-icon,.van-nav-bar__text {
  font-size: 20px;
}
.van-stepper__minus, .van-stepper__plus {
  margin-right: 10px;
}
.van-nav-bar__left, .van-nav-bar__right {
  padding: 0px!important;
}
.van-uploader__preview-image {
  display: block;
  width: 75px;
  height: 75px;
  overflow: hidden;
}
</style>