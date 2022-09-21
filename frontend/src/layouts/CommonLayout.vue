<template>
  <div class="background" ref="card">
    <a-card class="card">
      <!-- <div>
        <div style="text-align: center; padding-top: 16px">
          <img :src="logo" width="140" style="margin-top: -6px; margin-left: 8px; cursor: pointer" @click="backIndex" />
        </div>
        <div class="title" style="margin: 8px">
          <span style="cursor: pointer; font-size: 24px" @click="backIndex">Himool AMS</span>
        </div>
        <div style="text-align: center; font-size: 16px; color: #1890ff; margin-bottom: 24px">
          资产管理系统
        </div>
      </div> -->

      <div>
        <div style="text-align: center; padding-top: 16px">
          <img :src="logo" width="100" style="cursor: pointer; margin-top: 16px" @click="backIndex" />
        </div>
        <div class="title" style="margin-top: 16px">
          <span style="cursor: pointer; font-size: 24px" @click="backIndex">Himool AMS</span>
        </div>
        <div style="text-align: center; font-size: 16px; color: #1890ff; margin-top: 8px; margin-bottom: 24px">
          <span>资产管理系统</span>
        </div>
      </div>
      <slot></slot>
    </a-card>
  </div>
</template>

<script>
export default {
  name: "UserLayout",
  data() {
    return {
      logo: require("@/assets/logo.png"),
    };
  },
  methods: {
    backIndex() {
      window.open("http://www.haioucloud.com", "_self");
    },
  },
};
</script>

<style scoped>
.card {
  width: 480px;
  border-radius: 8px;
  padding-bottom: 32px;
  /* margin: auto; */
  margin-left: 60%;
}

.background {
  height: 100vh;
  /* min-height: 100%; */
  padding: 40px 24px;
  /* padding: 5vh 0; */
  /* background: #f0f2f5 url("~@/assets/background.svg") no-repeat 50%; */
  background-image: url("~@/assets/background.jpeg");
  background-size: 100% 100%;
}

.title {
  text-align: center;
  font-size: 28px;
  color: #1890ff;
  margin-top: 8px;
  font-weight: bold;
}
</style>
