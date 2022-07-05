<template>
  <div>
    <div>
      <van-nav-bar
          style="margin-top: 20px;height: 50px;font-size: 20px"
          title="文件上传"
          left-text="返回"
          left-arrow
          @click-left="onClickLeft"
      ></van-nav-bar>
    </div>
    <div class="con" >
      <van-uploader v-model="fileList"
                    :max-count="1"
                    multiple
                    accept="*"
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
          <van-radio name="1">单面</van-radio>
          <van-radio name="2">双面</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">色彩模式</div>
      <div style="float: right;">
        <van-radio-group v-model="printColor" direction="horizontal">
          <van-radio name="1">黑白</van-radio>
          <van-radio name="2">彩色</van-radio>
        </van-radio-group>
      </div>
      <br style="clear: both;"/>
      <hr />
      <div style="float: left;margin-left: 5px;color:#646566;font-size: 18px;">打印份数</div>
      <div style="float: right;">
        <van-stepper v-model="printNum" integer />
      </div>
      <br style="clear: both;"/>
      <hr />
    </div>

    <van-dialog v-model="showPdf" title="预览" >
      <div style="width: 830px;height: 500px;overflow: auto;background-color: #ccc;">
        <div v-for="data in PdfPath" >
          <img :src="'http://mytest.vaiwan.com/upload/'+data" style="width:98%;margin-left: 10px;border: 1px solid #000;" alt="">
        </div>
      </div>    </van-dialog>

    <van-button style="width: 96%;height: 50px;font-size: 20px;margin: 30px 10px 10px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                border: 1px solid #ccc;background-color: #eee"
                :loading="ylLoading" loading-text="上传中，请等待..."
                type="default" @click="yl()">预览</van-button>
    <van-button style="width: 96%;height: 50px;font-size: 20px;
                border-radius: 10px; box-shadow:  9px 9px 100px #a1a1a1,-9px -9px 100px #ffffff;
                margin: 10px;" type="primary"
                :loading="commitLoading" loading-text="提交中，请等待..."
                @click="commit()">提交</van-button>
  </div>
</template>
<script>
module.exports = {
  name: "imageUpload",
  data(){
    return{
      userid:this.$route.query.userid,
      ylLoading:false,
      commitLoading:false,
      fileList: [],
      PdfPath:[],
      showPdf:false,
      paperSize:"2",
      DorS:"1",
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
      // if(this.fileList.length<=0){
      //   this.$toast.fail('至少选择一个文件进行上传');
      //   return false;
      // }
      this.ylLoading=true
      // if(this.isUpload==false){
      //   this.upload();
      // }
      this.showPdf=true;
    },
    commit(){
      if(this.fileList.length<=0){
        this.$toast.fail('至少选择一个文件进行上传');
        this.ylLoading=false
        this.commitLoading=false
        return false;
      }
      this.commitLoading=true
      if(this.isUpload==false){
        this.upload();
      }else{
        this.commitLoading=false
        this.$toast.success('提交完成');
      }
    },
    upload(){
      if(this.fileList[0].file.size>(20*1024*1024)){
        this.$notify('只能上传小于20MB的文件');
        this.ylLoading=false
        this.commitLoading=false
        return false;
      }
      let formData=new FormData();
      formData.append('userid',this.userid)
      formData.append("paperSize",this.paperSize);
      formData.append("dorS",this.DorS);
      formData.append("printColor",this.printColor);
      formData.append("printNum",this.printNum);
      this.fileList.forEach(item => {
        formData.append('files',item.file)
      });
      axios({
        url:"http://mytest.vaiwan.com/upload/uploadImage",
        method: "post",
        data:formData,
        headers : {"Content'-Type" : "multipart/form-data"}
      }).then(({data})=>{
        if(data.status==200){
          this.$toast.success('上传成功');
          this.isUpload=true;
          this.PdfPath=data.data;
        }else{
          this.$notify({
            type:"danger",
            message:data.msg
          })
        }
      }).catch(e=>{
        this.$notify({
          type:"danger",
          message:e.message
        })
      }).finally(f=>{
        this.ylLoading=false
        this.commitLoading=false
      })
    },
  }
}
</script>

<style scoped>
body{
  margin: 10px!important;
}
.van-nav-bar__title {
  font-size: 20px;
}
.van-nav-bar .van-icon,.van-nav-bar__text {
  font-size: 20px;
}
.van-stepper__minus, .van-stepper__plus {
  margin-right: 10px;
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