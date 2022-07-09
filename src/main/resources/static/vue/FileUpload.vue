<template>
  <div style="width: 100%;height: 96vh;"
      v-loading="loading" :element-loading-text="loadingText">
    <div>
      <van-nav-bar
          style="margin: 20px 0px;height: 50px;font-size: 18px"
          title="文件上传"
          left-text="返回"
          left-arrow
          @click-left="onClickLeft"
      ></van-nav-bar>
    </div>
    <div class="con" >
      <van-uploader v-model="fileList.files"
                    :max-count="1"
                    multiple
                    accept="*"
                    :after-read="af"
                    :before-delete="del"

      />
    </div>
    <hr/>
    <div class="con" style="margin-top: 20px;font-size: 18px;">
      <div style="font-weight: bold;font-size:20px;margin-left: 5px; ">打印设置</div>
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
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">单双面</div>
      <div style="float: right;">
        <van-radio-group v-model="DorS" direction="horizontal">
          <van-radio name="OFF">单面</van-radio>
          <van-radio name="ON">双面</van-radio>
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
        <van-stepper v-model="printNum" integer max="100" @overlimit="overLimit"/>
      </div>
      <br style="clear: both;"/>
      <hr />
    </div>
    <van-dialog v-model="showPdf" title="预览" >
      <div style="width: 100%;height: 500px;overflow: auto;background-color: #ccc;">
        <div :style="'height: 480px;overflow: auto;margin-top: 10px;filter: grayscale('+ImgColor+')'">
          <div v-for="(data,index) in PdfPath" >
            <img :src="'/upload/'+data" style="display:inline-block;width:90%;margin:5px 15px 0px" alt="">
            <div style="text-align: center;margin-top: 5px">{{index+1}}/{{ PdfPath.length }}</div>
          </div>
        </div>
      </div>
    </van-dialog>

    <van-button style="width: 96%;height: 50px;font-size: 20px;margin: 30px 10px 10px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                border: 1px solid #ccc;background-color: #eee"
                type="default" @click="yl()">预览</van-button>
    <van-button style="width: 96%;height: 50px;font-size: 20px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                margin: 10px;" type="primary"
                @click="commit()">提交</van-button>
  </div>
</template>
<script>
module.exports = {
  name: "imageUpload",
  data(){
    return{
      userid:this.$route.query.userid,
      fileList: {
        files:[],
        uuid:"1"
      },
      PdfPath:[],
      showPdf:false,
      paperSize:"2",
      DorS:"ON",
      printColor:"1",
      printNum:1,
      isUpload:false,
      isShow:false,
      uuid:"2",
      loading:false,
      loadingText:"文件上传中...",
      ImgColor:1,
      pdfFilePath:"",
      fileName:""
    }
  },
  mounted() {

  },
  methods: {
    onClickLeft() {
      this.$router.push({path: '/home', query: {userid: this.userid}})
    },
    af(file){
      if(file.file.size>(20*1024*1024)){
        this.$notify('只能上传小于20MB的文件');
        this.fileList=[];
        return false;
      }
      if(!["application/pdf","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/msword","text/plain","application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/vnd.ms-powerpoint",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation"].includes(file.file.type)){
        this.$notify('只能上传word、xls、ppt、txt、pdf文件');
        this.fileList.files=[];
        return false;
      }
      let uuid=Math.random();
      this.fileList.uuid=uuid;
    },
    del(file,detail){
      this.$dialog.confirm({
        title: '删除',
        message: '确定要删除该文件吗？',
      }).then(() => {
        this.fileList.files.splice(detail.index,1)
        this.$toast.success("已删除")
        let uuid=Math.random();
        this.fileList.uuid=uuid;
      }).catch(() => {
        this.$toast.success("已取消")
      });
      return false
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
      if(this.fileList.files.length<=0){
        this.$toast.fail('至少选择一个文件进行上传');
        return false;
      }
      this.loading=true;
      if(this.fileList.uuid!=this.uuid){
        this.upload("show");
      }else{
        this.loading=false;
        this.showPdf=true;
      }
    },
    commit(){
      this.loading=true
      if(this.fileList.files.length<=0){
        this.$toast.fail('至少选择一个文件进行上传');
        this.loading=false;
        return false;
      }
      if(this.fileList.uuid!=this.uuid){
        this.isShow=false;
        this.upload('submit')
      }else{
        if(this.isUpload){
          this.loading=false;
          this.printf();
        }else{
          this.isShow=false;
          this.upload('submit')
        }
      }
    },
    upload(type) {
      try {
        if (this.fileList.files[0].file.size > (20 * 1024 * 1024)) {
          this.$notify('只能上传小于20MB的文件');
          this.ylLoading = false
          this.commitLoading = false
          return false;
        }
        let formData = new FormData();
        formData.append('userid', this.userid)
        formData.append("paperSize", this.paperSize);
        formData.append("dorS", this.DorS);
        formData.append("printColor", this.printColor);
        formData.append("printNum", this.printNum);
        this.fileList.files.forEach(item => {
          formData.append('files', item.file)
        });
        let time1=new Date().getTime()
        axios({
          url: "/upload/uploadImage",
          method: "post",
          data: formData,
          headers: {"Content'-Type": "multipart/form-data"}
        }).then(({data}) => {
          let time2=new Date().getTime()
          this.$toast("上传耗时"+((time2 - time1) / 1000.0)+"秒");
          if (data.status == 200) {
            this.pdfFilePath=data.data;
            this.fileName=data.fileName;
            if (type === 'submit') {
              this.loadingText="提交中...";
              this.printf();
            }else if (type === "show") {
              this.loadingText="预览生成中...";
              this.toImage();
            }
          } else {
            this.$notify({
              type: "danger",
              message: data.msg
            })
          }
        }).catch(e => {
          this.$notify({
            type: "danger",
            message: e.message
          })
          this.loadingText="文件上传中..."
          this.loading = false;
        })
      }catch (e) {
        this.$notify({
          type: "danger",
          message: e.message
        })
        this.loading=false;
        this.isShow=false;
      }
    },
    toImage(){
      let time1=new Date().getTime();
      axios.post("/upload/toImage",{
        "pdfFilePath":this.pdfFilePath
      }).then(({data})=>{
        let time2=new Date().getTime()
        this.$toast("预览耗时"+((time2 - time1) / 1000.0)+"秒");
        if (data.status == 200) {
          console.log(data)
          // this.$toast.success('上传成功');
          this.uuid=this.fileList.uuid;
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
      let fileName =this.pdfFilePath
      let filePath=fileName.substring(fileName.lastIndexOf("/")+1)
      let time1=new Date().getTime()
      axios.post("/upload/printf",{
        "fileName":this.fileName,
        "fileUrl":filePath,
        "loginName":this.userid,
        "printColor":this.printColor,
        "dorS":this.DorS,
        "paperSize":this.paperSize,
        "printNum":this.printNum
      }).then(r=>{
        let time2=new Date().getTime()
        this.$toast("提交耗时"+((time2 - time1) / 1000.0)+"秒");
        if(r.data=="ok"){
          this.$toast.success("提交成功")
          // this.$router.go(0)
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
/*.con{*/
/*  width: 925px;*/
/*  margin: 50px auto;*/
/*}*/
/*.van-uploader__preview-image {*/
/*  display: block;*/
/*  width: 300px;*/
/*  height: 300px;*/
/*  overflow: hidden;*/
/*}*/
/*.van-uploader__upload,.van-uploader__file{*/
/*  width: 300px;*/
/*  height: 300px;*/
/*}*/
/*.van-uploader__file-name{*/
/*  font-size:40px;*/
/*}*/
/*.van-uploader__preview-delete {*/
/*  position: absolute;*/
/*  top: 0;*/
/*  right: 0;*/
/*  width: 50px;*/
/*  height: 55px;*/
/*  background-color: rgba(0,0,0,.7);*/
/*  border-radius: 0 0 0 50px;*/
/*}*/
/*.van-uploader__preview-delete-icon {*/
/*  position: absolute;*/
/*  top: -2px;*/
/*  right: -4px;*/
/*  color: #fff;*/
/*  font-size: 50px;*/
/*  transform: scale(.5);*/
/*}*/
/*.van-uploader__file-icon {*/
/*  color: #646566;*/
/*  font-size: 40px;*/
/*}*/
/*.van-notify {*/
/*  display: -webkit-box;*/
/*  display: -webkit-flex;*/
/*  display: flex;*/
/*  -webkit-box-align: center;*/
/*  -webkit-align-items: center;*/
/*  align-items: center;*/
/*  -webkit-box-pack: center;*/
/*  -webkit-justify-content: center;*/
/*  justify-content: center;*/
/*  box-sizing: border-box;*/
/*  padding: 8px 16px;*/
/*  color: #fff;*/
/*  font-size: 60px;*/
/*  line-height: 100px;*/
/*  white-space: pre-wrap;*/
/*  text-align: center;*/
/*  word-wrap: break-word;*/
/*}*/
/*.van-dialog{*/
/*  width: 860px;*/
/*  height: 1400px;*/
/*}*/
/*.van-dialog__header {*/
/*  padding-top: 26px;*/
/*  font-weight: 500;*/
/*  line-height: 50px;*/
/*  text-align: center;*/
/*  font-size: 40px;*/
/*}*/
/*.van-dialog__content{*/
/*  height: 1250px;*/
/*}*/
/*.van-dialog__confirm, .van-dialog__confirm:active {*/
/*  color: #ee0a24;*/
/*  font-size: 40px;*/
/*}*/
/*.van-cell__left-icon, .van-cell__right-icon {*/
/*  height: 24px;*/
/*  font-size: 30px;*/
/*  line-height: 24px;*/
/*}*/
/*.van-field__label {*/
/*  margin-top: 10px;*/
/*}*/
/*.van-field__label {*/
/*  font-size: 35px;*/
/*}*/
/*.van-radio__icon {*/
/*  font-size: 35px;*/
/*  height: 45px;*/
/*}*/
/*.van-stepper__input {*/
/*  box-sizing: border-box;*/
/*  width: 80px;*/
/*  height: 50px;*/
/*  margin: 0 2px;*/
/*  padding: 0;*/
/*  color: #323233;*/
/*  font-size: 35px;*/
/*  line-height: normal;*/
/*  text-align: center;*/
/*  vertical-align: middle;*/
/*  background-color: #f2f3f5;*/
/*  border: 0;*/
/*  border-width: 1px 0;*/
/*  border-radius: 0;*/
/*  -webkit-appearance: none;*/
/*}*/
/*.van-stepper__minus {*/
/*  border-radius: 4px 0 0 4px;*/
/*  width: 50px;*/
/*  height: 50px;*/
/*}*/
/*.van-stepper__plus {*/
/*  border-radius: 0 4px 4px 0;*/
/*  width: 50px;*/
/*  height: 50px;*/
/*}*/
/*.van-pull-refresh__head,.van-loading__text {*/
/*  font-size: 35px;*/
/*}*/
</style>