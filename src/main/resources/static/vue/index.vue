<template>
<div>
<!--  轮播-->
  <div style="width: 98%;margin: 0 auto;">
    <van-swipe :autoplay="3000" indicator-color="white" style="height: 200px">
      <van-swipe-item><img src="../static/image/01.jfif" width="100%" height="100%"></van-swipe-item>
      <van-swipe-item><img src="../static/image/02.jpg" width="100%" height="100%"></van-swipe-item>
      <van-swipe-item><img src="../static/image/03.jpg" width="100%" height="100%"></van-swipe-item>
      <van-swipe-item><img src="../static/image/04.jpg" width="100%" height="100%"></van-swipe-item>
    </van-swipe>
  </div>
<!--  内容-->
  <div class="content" :v-loading="loading">
    <div style="height: 100px" @click="file()">
        <span><i class="el-icon-s-home" style="font-size: 40px;margin-top: 15px;"></i></span>
        <span>本地文件</span>
      </div>
    <div style="height: 100px" @click="image()">
        <span><i class="el-icon-picture-outline" style="font-size: 40px;margin-top: 15px;"></i></span>
        <span>图片打印</span>
      </div>
    <div style="height: 100px" @click="card()">
      <span><i class="el-icon-s-custom" style="font-size: 40px;margin-top: 15px;"></i></span>
      <span>证件打印</span>
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
      userid:this.$route.query.userid,
      loading:false,
    }
  },
  mounted() {


  },
  methods: {
    file(){
      this.$router.push({path: '/file', query: {userid: this.userid}})
    },
    image(){
      this.$router.push({path: '/image', query: {userid: this.userid}})
    },
    card(){
      this.$router.push({path: '/card', query: {userid: this.userid}})
    }
  }
}
// $(function(){
//   pushHistory();
//   window.addEventListener("popstate", function(e) {
//     location.reload();
//   }, false);
//   function pushHistory() {
//     var state = {
//       title: "title",
//       url: "#"
//     };
//     window.history.pushState(state, "title", "#");
//   }

// });
</script>

<style scoped>
html,body{
  width: 100%;
  height: 100%;
}
.content{
  width: 98%;
  margin: 40px auto;
}
.content div{
  display: inline-block;
  width: 75%;
  margin: 15px 12%;
  text-align: center;
  font-size: 18px;
  border-radius: 20px;
  background: linear-gradient(40deg, #eeeeee, #e6e6e6);
  box-shadow:  9px 9px 100px #a1a1a1,
  -9px -9px 100px #ffffff;
}

.content div span{
  display: block;
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