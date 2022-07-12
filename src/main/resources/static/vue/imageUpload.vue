<template>
  <div style="width: 100%;height: 96vh;"
        v-loading="loading" :element-loading-text="loadingText">
    <div class="con" >
      <van-uploader v-model="fileList.files"
                    :max-count="12"
                    :after-read="af"
                    :max-size="10*1024*1024"
                    @oversize="onOversize"
                    :before-delete="del"
      />
    </div>
    <hr/>
    <div class="con" style="margin-top: 20px;font-size: 16px;">
      <div style="font-weight: bold;font-size:20px;margin-left: 5px;">打印设置</div>
      <hr/>
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">打印比例</div>
      <div style="float: right;">
        <van-radio-group v-model="PlotScale" direction="horizontal" @change="change">
          <van-radio name="1">原图大小</van-radio>
          <van-radio name="2">自适应</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">纸张方向</div>
      <div style="float: right;">
        <van-radio-group v-model="paperFx" direction="horizontal" @change="change">
          <van-radio name="1">纵向</van-radio>
          <van-radio name="2">横向</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">纸张尺寸</div>
      <div style="float: right;">
        <van-radio-group v-model="paperSize" direction="horizontal">
          <van-radio name="1">A3</van-radio>
          <van-radio name="2">A4</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">单双面</div>
      <div style="float: right;">
        <van-radio-group v-model="DorS" direction="horizontal">
          <van-radio name="OFF">单面</van-radio>
          <van-radio name="ON">双面</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">色彩模式</div>
      <div style="float: right;">
        <van-radio-group v-model="printColor" direction="horizontal" @change="SzPrintColor">
          <van-radio name="1">黑白</van-radio>
          <van-radio name="3">彩色</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 16px;">打印份数</div>
      <div style="float: right;">
        <van-stepper v-model="printNum" integer max="100" @overlimit="overLimit" />
      </div>
      <br style="clear: both;"/>
      <hr />
    </div>
    <van-dialog v-model="showPdf" title="预览" >
      <div style="width: 100%;height: 500px;overflow: auto;background-color: #ccc;">
        <div :style="'height: 480px;overflow: auto;margin-top: 10px;filter: grayscale('+ImgColor+')'">
          <div :style="'height: 480px;overflow: auto;margin-top: 10px;filter: grayscale('+ImgColor+')'">
            <div v-for="(data,index) in PdfPath" >
              <img :src="'/upload/'+data" style="display:inline-block;width:90%;margin:5px 15px 0px" alt="">
              <div style="text-align: center;margin-top: 5px">{{index+1}}/{{ totalPage }}</div>
            </div>
            <div @click="lazy()" style="text-align: center;margin-top: 15px" v-if="!finish">点击加载更多</div>
            <div style="text-align: center;margin-top: 15px" v-else>已经加载完毕！</div>
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
        uuid:1
      },
      PdfPath:[],
      showPdf:false,
      paperSize:"2",
      DorS:"ON",
      printColor:"1",
      printNum:1,
      PlotScale:"2",
      printType:"1",
      paperFx:"2",
      isUpload:false,
      isShow:false,
      uuid:"2",
      loading:false,
      loadingText:"图片上传中...",
      ImgColor:1,
      pdfFilePath:"",
      fileName:"",
      totalPage:0,
      pageNo:0,
      finish:false
    }
  },
  mounted() {
  },
  methods: {
    onClickLeft() {
      this.$router.push({path: '/home', query: {userid: this.userid}})
    },
    af(file){
      if(!["image/png","image/jpeg"].includes(file.file.type)){
        this.$notify('只能上传png、jpg、jpeg、jfif的图片');
        this.fileList.files.pop();
        return false;
      }
      let uuid=Math.random();
      this.fileList.uuid=uuid;
    },
    onOversize(file) {
      this.$toast.fail('只能上传小于10MB的图片');
    },
    del(file,detail){
      this.$dialog.confirm({
        title: '删除',
        message: '确定要删除该图片吗？',
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
    change(){
      let uuid=Math.random();
      this.fileList.uuid=uuid;
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
        this.isShow=true;
        this.upload("show");
      }else{
        this.loading=false;
        this.showPdf=true;
      }
    },
    commit(){
      this.loading=true
      if(this.fileList.files.length<=0){
        this.$toast.fail('至少选择一张图片进行上传');
        this.loading=false;
        return false;
      }
      this.loading=true
      if(this.fileList.uuid!=this.uuid){
        this.isShow=false;
        this.upload("submit")
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
    upload(type){
      try{
        if(this.fileList.files.length<=0){
          this.$toast.fail('至少选择一张图片进行上传');
          return false;
        }
        this.loading=true;
        let formData=new FormData();
        formData.append('userid',this.userid)
        formData.append("paperFx",this.paperFx);
        formData.append("paperSize",this.paperSize);
        formData.append("dorS",this.DorS);
        formData.append("printColor",this.paperFx);
        formData.append("printNum",this.printNum);
        formData.append("plotScale",this.PlotScale);
        formData.append("printType",this.printType);
        this.fileList.files.forEach(item => {
          formData.append('files',item.file)
        });
        axios({
          url:"/uploadFile/uploadImage",
          method: "post",
          data:formData,
          headers : {"Content'-Type" : "multipart/form-data"}
        }).then(({data})=>{
          if(data.status==200){
            this.pdfFilePath=data.data;
            this.fileName=data.fileName;
            if (type === 'submit') {
              this.loadingText="提交中...";
              this.printf();
            }else if (type === "show") {
              this.loadingText="预览生成中...";
              this.toImage();
            }
          }else{
            this.$notify({
              type:"danger",
              message:"图片上传失败"+data.msg
            })
          }
        }).catch(e=>{
          this.$notify({
            type:"danger",
            message:"图片上传失败"+e.message
          })
          this.loadingText="图片上传中..."
          this.loading=false;
          this.isShow=false;
        })
      }catch (e) {
        this.$notify({
          type: "danger",
          message: e.message
        })
        this.loading=false;
      }
    },
    toImage(){
      this.pageNo=0;
      axios.post("/uploadFile/toImage",{
        "pdfFilePath":this.pdfFilePath,
        "num":this.pageNo
      }).then(({data})=>{
        if (data.status == 200) {
          console.log(data)
          // this.$toast.success('上传成功');
          this.uuid=this.fileList.uuid;
          this.isUpload = true;
          this.PdfPath = data.data;
          this.totalPage=data.total;
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
    lazy(){
      if(this.totalPage<=this.PdfPath.length){
        this.$toast.success("已经加载完了")
        this.finish=true;
        return false;
      }
      this.pageNo=this.pageNo+1;
      axios.post("/uploadFile/toImage",{
        "pdfFilePath":this.pdfFilePath,
        "num":this.pageNo
      }).then(({data})=>{
        if (data.status == 200) {
          for(let i=0;i<data.data.length;i++){
            this.PdfPath.push(data.data[i]);
          }
          console.log(this.PdfPath)
        }else{
          this.$notify({
            type: "danger",
            message: "加载失败"+data.msg
          })
        }
      }).catch(e=>{
        this.$notify({
          type: "danger",
          message:"加载失败"+ e.message
        })
      })
    },
    printf(){
      let fileName =this.pdfFilePath
      let filePath=fileName.substring(fileName.lastIndexOf("/")+1)
      axios.post("/uploadFile/printf",{
        "fileName":this.fileName,
        "fileUrl":filePath,
        "loginName":this.userid,
        "printColor":this.printColor,
        "dorS":this.DorS,
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