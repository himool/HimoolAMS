<template>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <!-- <a-avatar class="avatar" size="small" shape="circle"/> -->
      <span class="name">用户名: {{ userName }}</span>
    </div>
    <a-menu :class="['avatar-menu']" slot="overlay">
      <!-- <a-menu-item>
        <a-icon type="user" />
        <span>个人中心</span>
      </a-menu-item>
      <a-menu-item>
        <a-icon type="setting" />
        <span>设置</span>
      </a-menu-item> -->
      <!-- <a-menu-divider /> -->
      <a-menu-item @click="logout">
        <a-icon style="margin-right: 8px;" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
import { mapGetters } from "vuex";
// import {logout} from '@/services/user'
import { getInfo } from '@/api/user'
import Cookies from 'js-cookie';

export default {
  name: "HeaderAvatar",
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data() {
    return {
      userName: "",
    };
  },
  methods: {
    logout() {
      this.$router.push("/login");
    },
    initialize() {
      if (!Cookies.get("access") || !Cookies.get("refresh")) {
        return this.$router.push("/login");
      }

      getInfo().then((data) => {
        this.userName = data.name
        console.log(data)
      });
    },
  },
  mounted() {
    this.initialize()
  },
};
</script>

<style lang="less">
.header-avatar {
  display: inline-flex;
  .avatar,
  .name {
    align-self: center;
  }
  .avatar {
    margin-right: 8px;
  }
  .name {
    font-weight: 500;
  }
}
.avatar-menu {
  width: 150px;
}
</style>
