<template>
  <common-layout>
    <div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 14 }">
          <a-form-model-item prop="number" label="公司编号">
            <a-input size="large" v-model="form.number" @pressEnter="login" />
          </a-form-model-item>
          <a-form-model-item prop="username" label="用户名">
            <a-input size="large" v-model="form.username" @pressEnter="login" />
          </a-form-model-item>
          <a-form-model-item prop="password" label="密码">
            <a-input-password size="large" v-model="form.password" @pressEnter="login" />
          </a-form-model-item>
        </a-form-model>
      </div>
  
      <a-row>
        <a-col :span="14" offset="5">
          <a-button type="primary" size="large" :loading="isLoading" style="width: 100%;" @click="login">登录</a-button>
        </a-col>
      </a-row>
  
      <div style="text-align: center; width: 100%; margin-top: 24px;">
        <div>
          试用，购买或问题咨询请扫描下方客户经理二维码
        </div>
        <div>
          <img :src="wechatCustomerService" width="100" style="margin-top: 8px;" />
        </div>
      </div>
    </div>
  </common-layout>
</template>

<script>
  import CommonLayout from '@/layouts/CommonLayout';
  import { getToken } from '@/api/user';
  import Cookies from 'js-cookie';

  export default {
    components: { CommonLayout },
    data() {
      return {
        wechatCustomerService: require('@/assets/wechat_customer_service.png'),
        isLoading: false,
        form: {
          number: '',
          username: '',
          password: '',
        },
        rules: {
          number: [
            { required: true, message: '请输入公司编号', trigger: 'change' },
          ],
          username: [
            { required: true, message: '请输入用户名', trigger: 'change' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'change' },
          ],
        },
      }
    },
    methods: {
      login() {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.isLoading = true;
            getToken(this.form).then(data => {
              Cookies.set('access', data.access);
              Cookies.set('refresh', data.refresh);
              this.$router.push('/');
            }).finally(() => {
              this.isLoading = false;
            });
          }
        });
      },
      // login() {
      //   this.$refs.form.validate(valid => {
      //     if (valid) {
      //       this.loading = true;
      //       getToken(this.form).then(data => {
      //         Cookies.set('access', data.access);
      //         Cookies.set('refresh', data.refresh);
      //         this.$router.push('/');
      //       }).finally(() => {
      //         this.loading = false;
      //       });
      //     }
      //   });
      // },
    },
  }
</script>

<style lang="less" scoped>
  .common-layout {
    .top {
      text-align: center;

      .header {
        height: 44px;
        line-height: 44px;

        a {
          text-decoration: none;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
        }

        .title {
          font-size: 33px;
          color: @title-color;
          font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica, sans-serif;
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }

      .desc {
        font-size: 14px;
        color: @text-color-second;
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }

    .login {
      width: 368px;
      margin: 0 auto;

      @media screen and (max-width: 576px) {
        width: 95%;
      }

      @media screen and (max-width: 320px) {
        .captcha-button {
          font-size: 14px;
        }
      }

      .icon {
        font-size: 24px;
        color: @text-color-second;
        margin-left: 16px;
        vertical-align: middle;
        cursor: pointer;
        transition: color 0.3s;

        &:hover {
          color: @primary-color;
        }
      }
    }
  }
</style>