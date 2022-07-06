<template>
  <view style="background-color: #f8f8f8; height: 100vh;">
    <view>
      <u-row style="background-color: #3c9cff; color: #ffffff; height: 72rpx; padding-right: 16rpx;">
        <u-col offset="9" span="3" @click="showActionSheet = true">
          <u-icon name="arrow-down" :label="userItem.name" labelPos="left" color="#ffffff" labelColor="#ffffff"
            space="6px" style="font-weight: 600;"></u-icon>
        </u-col>
      </u-row>

      <u-action-sheet :actions="actionList" :show="showActionSheet" :closeOnClickOverlay="true"
        @close="showActionSheet = false" @select="selectAction"></u-action-sheet>
    </view>

    <u-grid :border="false" col="2" @click="onClickGrid">
      <u-grid-item v-for="(item, index) in funcList" :key="index"
        style="padding: 64rpx 0; border-right:1px solid #8888; border-bottom: 1px solid #8888;">
        <image :src="item.image" style="width: 120rpx; height: 120rpx" />
        <view style="margin-top: 16rpx;">
          <text style="font-size: 40rpx;">{{item.title}}</text>
        </view>
      </u-grid-item>
    </u-grid>
  </view>
</template>

<script>
  import { getInfo } from 'api/system.js';
  import { mapState, mapMutations } from 'vuex';

  export default {
    data() {
      return {
        funcList: [{
          title: '盘点',
          path: '/pages/stockCheckForm/index',
          image: '../../static/func9.png',
        }, ],
        showActionSheet: false,
        actionList: [
          { code: 'logout', name: '退出登录' }
        ],
        userItem: {},
      }
    },
    methods: {
      initData() {
        let access_token = uni.getStorageSync('access');
        if (access_token.length == 0) {
          uni.redirectTo({ url: '../login/index' });
        }

        getInfo().then((data) => {
          this.userItem = data;
        });
      },
      onClickGrid(index) {
        uni.navigateTo({ url: this.funcList[index].path });
      },
      selectAction(item) {
        this.showActionSheet = false;

        if (item.code == 'logout') {
          uni.removeStorageSync('access');
          uni.removeStorageSync('refresh');
          uni.redirectTo({ url: '../login/index' });
        }
      },
    },
    onShow() {
      this.initData();
    },
  }
</script>

<style>

</style>
