<template>
  <div id="app">
    <el-container>
      <el-header class="head1" v-if="!deak">
        <div>
          <input
            type="text"
            placeholder="请输入用户名"
            class="input1"
            v-model="username"
          />
        </div>
        <div><button class="btn1" @click="requstWs">点击启用</button></div>
      </el-header>
      <el-header class="head1" v-else>
        <div>
          <label
            style="
              box-sizing: border-box;
              margin-right: 5px;
              color: red;
              font-size: 20px;
              font-weight: bold;
            "
            >{{ username }}</label
          >
        </div>
        <div><button class="btn1" disabled>已启用</button></div>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <div>
            <div style="font-size: 25px; font-weight: 500; color: red">
              在线用户
            </div>
            <ul v-for="item in list1" :key="item.index">
              <li class="li1">{{ item }}</li>
            </ul>
          </div>
        </el-aside>

        <el-main>
          <div style="height: 600px">
            <div style="height: 450px; box-sizing: border-box">
              <div style="overflow: auto; min-height: 450px">
                <div class="c3" v-for="item1 in ls" :key="item1.index">
                  <div
                    v-if="username === item1.from"
                    style="display: flex; justify-content: end"
                  >
                    <div
                      style="
                        background-color: rgba(53, 233, 125, 0.542);
                        box-sizing: border-box;
                        padding: 10px;
                        border-radius: 4px;
                      "
                    >
                      {{ item1.msg }}
                    </div>
                    <div
                      style="
                        display: flex;
                        justify-content: center;
                        align-items: center;
                      "
                    >
                      :你
                    </div>
                  </div>
                  <div v-else style="display: flex">
                    <div
                      style="
                        color: blue;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                      "
                    >
                      {{ item1.from }} :
                    </div>
                    <div
                      style="
                        background-color: rgb(240, 201, 201);
                        box-sizing: border-box;
                        padding: 10px;
                        border-radius: 4px;
                      "
                    >
                      {{ item1.msg }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div style="height: 150px; display: flex">
              <textarea
                style="width: 90%; resize: none; font-size: 20px"
                v-model="gomsg"
              ></textarea>
              <button style="width: 10%" @click="sendmsgs()">发送</button>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";
import { sendWebsocket, closeWebsocket } from "@/utils/websocket.js";

export default {
  name: "App",
  components: {
    // HelloWorld,
  },
  data() {
    return {
      username: "",
      deak: false,
      list1: [],
      gomsg: "",
      ls: [],
    };
  },
  beforeDestroy() {
    // 页面销毁时关闭ws。因为有可能ws连接接收数据尚未完成，用户就跳转了页面
    // 在需要主动关闭ws的地方都可以调用该方法
    closeWebsocket();
  },
  methods: {
    // ws连接成功，后台返回的ws数据，组件要拿数据渲染页面等操作
    wsMessage(data) {
      const dataJson = data;
      console.log(dataJson);
      if (dataJson.userName) {
        this.list1 = dataJson.userName;
      } else {
        this.ls.push({ from: dataJson.from, msg: dataJson.msg });
      }
      // if (dataJson.from != "undefined") {
      //   this.ls.push(dataJson.from + " : " + dataJson.msg);
      //   console.log(233);
      // }
      // console.log(dataJson.from);
    },
    // ws连接失败，组件要执行的代码
    wsError() {
      // 比如取消页面的loading
    },
    requstWs() {
      this.deak = true;
      // 防止用户多次连续点击发起请求，所以要先关闭上次的ws请求。
      closeWebsocket();
      // 跟后端协商，需要什么参数数据给后台
      const obj = {
        time: "error",
        from: this.username,
        msg: this.username + "加入房间",
      };
      // 发起ws请求
      sendWebsocket(
        // "ws://202.182.125.24:61487/socket/" + this.username,
        "ws://localhost:8383/socket/" + this.username,
        obj,
        this.wsMessage,
        this.wsError
      );
    },
    sendmsgs() {
      // 防止用户多次连续点击发起请求，所以要先关闭上次的ws请求。
      closeWebsocket();
      // 跟后端协商，需要什么参数数据给后台
      const obj = {
        time: "error",
        from: this.username,
        msg: this.gomsg,
      };
      // 发起ws请求
      sendWebsocket(
        // "ws://202.182.125.24:61487/socket/" + this.username,
        "ws://localhost:8383/socket/" + this.username,
        obj,
        this.wsMessage,
        this.wsError
      );
      this.gomsg = "";
    },
  },
};
</script>

<style>
.c3 {
  box-sizing: border-box;
  height: 45px;
  min-width: 100%;
  margin: 0;
  padding: 0;
  box-sizing: content-box;
}
ul {
  margin: 0;
  padding: 0;
}
li {
  margin: 0;
  padding: 0;
  list-style: none;
}
.li1 {
  width: 200px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 25px;
  box-sizing: border-box;
  border: 1px solid black;
}
.btn1 {
  width: 70px;
  height: 30px;
}
.input1 {
  width: 130px;
  height: 30px;
  box-sizing: border-box;
  border-radius: 5px;
  font-size: 17px;
  margin-right: 5px;
}
.head1 {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.el-header,
.el-footer {
  background-color: #5e5a5a;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #c2d3e7;
  color: #333;
  text-align: center;
  min-height: 620px;
}

.el-main {
  background-color: #f7f7f7;
  color: #333;
  text-align: center;
  /* line-height: 160px; */
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
