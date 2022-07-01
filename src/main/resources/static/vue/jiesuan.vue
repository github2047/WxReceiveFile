<template>
  <div>
    <div>
      <van-nav-bar
          style="margin-top: 40px;height: 100px;"
          title="结算"
          left-text="返回"
          left-arrow
          @click-left="onClickLeft"
      ></van-nav-bar>
    </div>
    <div class="con" >
      <van-uploader v-model="fileList"
                    :max-count="9"
                    multiple
      />
    </div>
    <van-dialog v-model="showPdf" title="预览" >
      <iframe :src="'static/js/pdfjs/web/viewer.html?file='+PdfPath" style="width: 830px;height:1000px;"></iframe>
    </van-dialog>
    <el-button style="width: 98%;height: 200px;font-size: 40px" type="success" @click="sc()">上传</el-button>
    <el-button style="width: 98%;height: 200px;font-size: 40px" plain type="success" @click="yl()">预览</el-button>
  </div>
</template>
<script>
module.exports = {
  name: "jiesuan",
  data(){
    return{
      fileList: [],
      PdfPath:"",
      showPdf:false,
    }
  },
  mounted() {
  },
  methods: {
    onClickLeft() {
      this.$router.push("/home")
    },
    yl(){
      this.showPdf=true;
    },
    sc(){
      let formData=new FormData();
      formData.append('userid',"2047")
      this.fileList.forEach(item => {
        formData.append('files',item.file)
      });
      axios({
        url:"/file/upload",
        method: "post",
        data:formData,
        headers : {"Content'-Type" : "multipart/form-data"}
      }).then(({data})=>{
        if(data.code==200){
          this.$toast.success('上传成功');
          this.PdfPath="/upload"+data.data;
        //   axios.post("/file/getPdf",{
        //     "path":path
        //   }).then(({data})=>{
        //     if(data.code==200){
        //       this.$toast.success('上传成功');
        //
        //     }else{
        //       this.$notify({
        //         type:"danger",
        //         message:data.msg
        //       })
        //     }
        //   })
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
      })
    },
    base64ToBlob(urlData, type) {
      let arr = urlData.split(',');
      let mime = arr[0].match(/:(.*?);/)[1] || type;
      // 去掉url的头，并转化为byte
      let bytes = window.atob(arr[1]);
      // 处理异常,将ascii码小于0的转换为大于0
      let ab = new ArrayBuffer(bytes.length);
      // 生成视图（直接针对内存）：8位无符号整数，长度1个字节
      let ia = new Uint8Array(ab);
      for (let i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i);
      }
      return new Blob([ab], {
        type: mime
      });
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
.con{
  width: 925px;
  margin: 50px auto;
  border: 1px solid #eee;
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
</style>