<template>
<div style="background: #eee;height: 100vh">
<!--  轮播-->
  <div style="width: 98%;margin: 0 auto;">
    <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white">
      <van-swipe-item>1</van-swipe-item>
      <van-swipe-item>2</van-swipe-item>
      <van-swipe-item>3</van-swipe-item>
      <van-swipe-item>4</van-swipe-item>
    </van-swipe>
  </div>
<!--  内容-->
  <div class="content" :v-loading="loading">
    <van-uploader
        style="height: 260px"
        :max-size="20*1024*1024"
        :after-read="afterRead"
        accept="*"

    >
      <div style="margin-top: 0px;">
        <span><i class="el-icon-s-home" style="font-size: 100px;"></i></span>
        <span>本地文件</span>
      </div>
    </van-uploader>

    <van-uploader
        style="height: 260px"
        :max-size="20*1024*1024"
        :after-read="afterRead"
        accept="image/*"
    >
      <div style="margin-top: 0px;">
        <span><i class="el-icon-picture-outline" style="font-size: 100px;"></i></span>
        <span>图片打印</span>
      </div>
    </van-uploader>

    <div style="height: 260px" @click="card()">
      <span><i class="el-icon-s-custom" style="font-size: 100px;margin-top: 50px;margin-bottom: 20px"></i></span>
      <span style="margin-top: 60px">证件打印</span>
    </div>
  </div>
</div>
</template>
<script>
let ElMessage = window.ELEMENT.Message;
module.exports = {
  name: "index",
  data(){
    return{
      loginInfo:{
        userid:this.$route.params.userid,
        name:this.$route.params.name
      },
      loading:false,
    }
  },
  mounted() {
  },
  methods: {
    sx(){
      this.$router.go(0);
    },
    beforeUpload(file){
      if(file.size>(20*1024*1024)){
        ElMessage({
          message:"文件大小不可以超过20MB",
          grouping:true,
          type:"error"
        })
        return false;
      }
    },
    afterRead(file) {
      console.log(file)
      // 此时可以自行将文件上传至服务器
      if(file.file.size>(20*1024*1024)){
        ElMessage({
          message:"文件大小不可以超过20MB",
          grouping:true,
          type:"error"
        })
        return false;
      }
      this.loading=true;
      let formData=new FormData();
      formData.append('userid',"123")
      formData.append('files',file.file)
      console.log(file.file)
      axios.post("/file/upload",formData).then(r=>{
        if(r.data.status==200){
          ElMessage({
            message:"上传成功",
            grouping:true,
            type:"success"
          })
          this.fileList=[];
          this.loading=false;
        }else{
          ElMessage({
            message: "上传失败"+r.data.message,
            grouping: true,
            type: 'error',
          })
          // }
          this.loading=false;
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
    card(){
      this.$router.push("/js")
    }
  }
}
</script>

<style scoped>
.my-swipe .van-swipe-item {
  height: 500px;
  color: #fff;
  font-size: 40px;
  line-height: 150px;
  text-align: center;
  background-color: #39a9ed;
}
.content{
  width: 98%;
  margin: 100px auto;
}
.content div{
  display: inline-block;
  width: 75%;
  height: 100px;
  margin: 33px 13%;
  text-align: center;
  font-size: 20px;
  background: #fff;
  border-radius: 20px;
}

.content div span{
  display: block;
  height: 100px!important;
}
/*滚动条*/
.con .el-scrollbar__wrap {
  overflow-x: hidden;
}
/*table行选中颜色*/
.el-table--striped .el-table__body tr.el-table__row--striped.current-row td, .el-table__body tr.current-row>td {
  background-color: #fff9c7 !important;
}
</style>