<template>
  <view style="text-align: center;">
    <!-- <view style="padding-top: 180rpx;">
      <image src="@/static/logo.png" style="width: 240rpx; height: 240rpx;"></image>
    </view> -->

    <!--   <view style="color: rgb(24, 144, 255); padding-top: 280rpx;">
      <view style="font-size: 52rpx; font-weight: 800;">资产管理系统</view>
      <view style="font-size: 30rpx;">AMS标准版</view>
    </view>
 -->

    <view style="padding-top: 180rpx;">
      <image src="../../static/logo.png" style="width: 160rpx; height: 160rpx;"></image>
    </view>

    <view style="color: rgb(24, 144, 255); margin-top: 24rpx;">
      <view style="font-size: 52rpx; font-weight: 800;">Himool AMS</view>
      <view style="font-size: 30rpx; margin-top: 8rpx;">盒木资产管理系统</view>
    </view>

    <view style="width: 70%; margin: auto; padding-top: 10rpx;">
      <!-- <view class="form-input">
        <u--input v-model="form.number" placeholder="公司编号" border="none" prefixIcon="tags"
          prefixIconStyle="font-size: 24px; color: #3c9cffbb" fontSize="18px" clearable></u--input>
      </view> -->
      <view class="form-input">
        <u--input v-model="form.username" placeholder="用户名" border="none" prefixIcon="account"
          prefixIconStyle="font-size: 24px; color: #3c9cffbb" fontSize="18px" clearable></u--input>
      </view>
      <view class="form-input">
        <u--input v-model="form.password" placeholder="密码" border="none" prefixIcon="lock"
          prefixIconStyle="font-size: 24px; color: #3c9cffbb" fontSize="18px" clearable type="password"></u--input>
      </view>

      <view style="padding-top: 80rpx;">
        <u-button type="primary" text="登录" size="large" :loading="loginLoading" :disabled="loginLoading"
          @click="handleLogin"></u-button>
      </view>
    </view>

    <u-toast ref="uToast"></u-toast>
  </view>
</template>

<script>
  import { getToken } from '@/api/system.js';

  export default {
    data() {
      return {
        loginLoading: false,
        form: {
          number: 'admin',
          username: '',
          password: '',
        },
      };
    },
    methods: {
      handleLogin() {
        if (!this.validateForm()) {
          return;
        }

        this.loginLoading = true;
        getToken(this.form).then((data) => {
          uni.setStorageSync('access', data.access);
          uni.setStorageSync('refresh', data.refresh);
          uni.redirectTo({ url: '../stockCheckForm/index' });
        }).finally(() => {
          this.loginLoading = false;
        })
      },
      validateForm() {
        if (!this.form.number) {
          this.$refs.uToast.show({ type: 'warning', duration: 1000, message: '请输入公司编号' })
          return false;
        }

        if (!this.form.username) {
          this.$refs.uToast.show({ type: 'warning', duration: 1000, message: '请输入用户名' })
          return false;
        }

        if (!this.form.password) {
          this.$refs.uToast.show({ type: 'warning', duration: 1000, message: '请输入密码' })
          return false;
        }

        return true;
      },
    },
  }
</script>

<style scoped>
  .form-input {
    border-bottom: 1px solid #3c9cffbb;
    padding: 16rpx 8rpx;
    margin-top: 16rpx;
  }
</style>
