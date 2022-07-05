<template>
<div>
  <div>
    <div>
      <van-nav-bar
          style="margin-top: 100px;height: 100px;"
          title="证件上传"
          left-text="返回"
          left-arrow
          @click-left="onClickLeft"
      ></van-nav-bar>
    </div>
    <div>
      <van-uploader :after-read="ZmUp">
        <div style="width: 600px;text-align: center;margin: 50px 30%;">
          <img id="imgZm" :src="imgZm" width="600px" height="400px" id="pdfDom">
          <div style="font-size: 40px;text-align: center;margin-top: 30px">正面</div>
        </div>
      </van-uploader>
    </div>
    <div>
      <van-uploader :after-read="BmUp" >
        <div style="width: 600px;text-align: center;margin: 50px 30%;">
          <img id="imgBm" :src="imgBm" width="600px" height="400px">
          <div style="font-size: 40px;text-align: center;margin-top: 30px">反面</div>
        </div>
      </van-uploader>
    </div>
  </div>
  <hr/>
  <div class="con" style="margin-top: 30px;font-size: 45px;">
    <div style="font-weight: bold">打印设置</div>
    <hr/>
    <div style="float: left;margin-top: 20px;margin-left: 15px;color:#646566;font-size: 35px;">纸张尺寸</div>
    <div style="float: right;margin-top: 20px;margin-right: 15px;">
      <van-radio-group v-model="paperSize" direction="horizontal">
        <van-radio name="1">A3</van-radio>
        <van-radio name="2">A4</van-radio>
      </van-radio-group>
    </div>
    <br style="clear: both;"/>
    <hr />
    <div style="float: left;margin-top: 20px;margin-left: 15px;color:#646566;font-size: 35px;">色彩模式</div>
    <div style="float: right;margin-top: 20px;margin-right: 15px;">
      <van-radio-group v-model="printColor" direction="horizontal">
        <van-radio name="1">黑白</van-radio>
        <van-radio name="2">彩色</van-radio>
      </van-radio-group>
    </div>
    <br style="clear: both;"/>
    <hr />
    <div style="float: left;margin-top: 20px;margin-left: 15px;color:#646566;font-size: 35px;">打印份数</div>
    <div style="float: right;margin-top: 20px;margin-right: 15px;">
      <van-stepper v-model="printNum" integer />
    </div>
    <br style="clear: both;"/>
    <hr />
  </div>
  <van-dialog v-model="showPdf" title="预览">
    <div style="width: 100%;background-color: #ccc;">
      <img :src="'http://mytest.vaiwan.com'+PdfPath" style="display:inline-block;width:90%;margin:30px 40px" alt="">
    </div>
  </van-dialog>
  <div>
    <van-button style="width: 98%;height: 200px;font-size: 50px;margin:100px 15px 20px;" type="primary" :loading="loading" loading-text="上传中，请等待..." @click="sc()">上传</van-button>
    <van-button style="width: 98%;height: 150px;font-size: 40px;margin: 20px 10px;"  plain type="primary" @click="yl()">预览</van-button>
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
      show: false,
      paperSize:"2",
      printColor:"2",
      printNum:1,
      isUpload:false,
    }
  },
  mounted() {
  },
  methods: {
    onClickLeft() {
      this.$router.push({path: '/home', query: {userid: this.userid}})
    },
    yl(){
      if(!this.isUpload){
        this.$toast.fail('请先上传在进行预览');
        return false;
      }
      this.showPdf=true;
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
    drawProdPicture() {
      let imgZm = new Image()
      imgZm.src = this.imgZm
      imgZm.width = 1101
      imgZm.height = 638
      imgZm.setAttribute('crossOrigin', 'anonymous')
      let canvas = document.createElement("canvas")
      let context = canvas.getContext("2d")
      canvas.width = 2480
      canvas.height = 3508
      let imgBm = new Image()
      let flag = true
      // 将 img1 加入画布
      imgZm.onload = () => {
        context.drawImage(imgZm, 690, 858, 1101, 638)
        imgBm.src = this.imgBm
        imgBm.setAttribute('crossOrigin', 'anonymous')
        imgBm.width = 1101
        imgBm.height = 638
        if (flag) {
          flag = false
        } else {
          let src = canvas.toDataURL()
          this.makePic = src
        }
      }
      // 将 img2 加入画布
      imgBm.onload = () => {
        context.drawImage(imgBm, 690, 2012, 1101, 638)
        if (flag) {
          flag = false
        } else {
          let src = canvas.toDataURL('image/png')
          this.makePic = src
        }
        this.upload();
      }
    },
    sc(){
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
      this.drawProdPicture();
    },
    upload(){
      this.loading=true;
      axios.post("http://mytest.vaiwan.com/upload/uploadCard", {
        "card":this.makePic,
        "userid":this.userid,
        "paperSize":this.paperSize,
        "printColor":this.printColor,
        "printNum":this.printNum
      }).then(r=>{
        if(r.data.status==200){
          console.log(r)
          this.loading=false;
          this.$toast.success('上传成功')
          this.isUpload=true;
          this.PdfPath="/upload/"+r.data.data;
          this.imgZm="../static/image/正面.png"
          this.imgBm="../static/image/背面.png"
          this.ZmFile=null;
          this.BmFile=null;
        }else{
          this.$notify("上传失败:"+r.data.msg)
          this.loading=false;
          return false;
        }
      }).catch(e=>{
        this.$notify("上传失败"+e.message)
        return false;
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
.van-nav-bar__title {
  padding: 30px;
  font-size: 50px;
}
.van-nav-bar .van-icon,.van-nav-bar__text {
  padding: 30px 5px;
  font-size: 50px;
}
.con{
  width: 925px;
  margin: 50px auto;
}
.van-uploader__preview-image {
  display: block;
  width: 300px;
  height: 300px;
  overflow: hidden;
}
.van-uploader__upload,.van-uploader__file{
  width: 300px;
  height: 300px;
}
.van-uploader__file-name{
  font-size:40px;
}
.van-uploader__preview-delete {
  position: absolute;
  top: 0;
  right: 0;
  width: 50px;
  height: 55px;
  background-color: rgba(0,0,0,.7);
  border-radius: 0 0 0 50px;
}
.van-uploader__preview-delete-icon {
  position: absolute;
  top: -2px;
  right: -4px;
  color: #fff;
  font-size: 50px;
  transform: scale(.5);
}
.van-uploader__file-icon {
  color: #646566;
  font-size: 40px;
}
.van-notify {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  justify-content: center;
  box-sizing: border-box;
  padding: 8px 16px;
  color: #fff;
  font-size: 60px;
  line-height: 100px;
  white-space: pre-wrap;
  text-align: center;
  word-wrap: break-word;
}
.van-dialog{
  width: 860px;
  height: 1400px;
}
.van-dialog__header {
  padding-top: 26px;
  font-weight: 500;
  line-height: 50px;
  text-align: center;
  font-size: 40px;
}
.van-dialog__content{
  height: 1250px;
}
.van-dialog__confirm, .van-dialog__confirm:active {
  color: #ee0a24;
  font-size: 40px;
}
.van-cell__left-icon, .van-cell__right-icon {
  height: 24px;
  font-size: 30px;
  line-height: 24px;
}
.van-field__label {
  margin-top: 10px;
}
.van-field__label {
  font-size: 35px;
}
.van-radio__icon {
  font-size: 35px;
  height: 45px;
}
.van-stepper__input {
  box-sizing: border-box;
  width: 80px;
  height: 50px;
  margin: 0 2px;
  padding: 0;
  color: #323233;
  font-size: 35px;
  line-height: normal;
  text-align: center;
  vertical-align: middle;
  background-color: #f2f3f5;
  border: 0;
  border-width: 1px 0;
  border-radius: 0;
  -webkit-appearance: none;
}
.van-stepper__minus {
  border-radius: 4px 0 0 4px;
  width: 50px;
  height: 50px;
}
.van-stepper__plus {
  border-radius: 0 4px 4px 0;
  width: 50px;
  height: 50px;
}
.van-pull-refresh__head,.van-loading__text {
  font-size: 35px;
}
</style>