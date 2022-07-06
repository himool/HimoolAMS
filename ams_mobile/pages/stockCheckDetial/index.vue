<template>
  <view>
    <view style="margin-bottom: 100rpx;">
      <u-cell-group v-if="item.lose_assets.length > 0" title="丢失资产:">
        <u-cell v-for="item in item.lose_assets">
          <template slot="title">{{ item.name }}</template>
          <template slot="label">
            <u-row class="cell-label-row">
              <u-col span="6">资产编号: {{ item.number }}</u-col>
              <u-col span="6">存放地点: {{ item.location_name }}</u-col>
            </u-row>
          </template>
          <template slot="value">
            <view>
              <u-button type="error" text="移除" @click="removeLose(item)" />
            </view>
          </template>
        </u-cell>
      </u-cell-group>

      <u-cell-group v-if="item.surplus_assets.length > 0" title="盘盈资产:">
        <u-cell v-for="item in item.surplus_assets" :title="item.name">
          <template slot="title">{{ item.name }}</template>
          <template slot="label">
            <u-row class="cell-label-row">
              <u-col span="6">资产编号: {{ item.number }}</u-col>
              <u-col span="6">存放地点: {{ item.location_name }}</u-col>
            </u-row>
          </template>
          <template slot="value">
            <view>
              <u-button type="error" text="移除" @click="removeSurplus(item)" />
            </view>
          </template>
        </u-cell>
      </u-cell-group>
    </view>

    <view style="position: fixed; bottom: 0; width: 100%;">
      <u-button type="primary" size="large" text="确认盘点" :loading="loading" :disabled="loading" @click="confirm" />
    </view>

    <u-toast ref="uToast"></u-toast>
  </view>
</template>

<script>
  import { stockCheckConfirm } from '@/api/stockCheck.js';

  export default {
    data() {
      return {
        eventChannel: null,
        item: {},
        loading: false,
      }
    },
    methods: {
      removeSurplus(item) {
        let index = this.item.surplus_assets.findIndex((surplusItem) => surplusItem.id == item.id);
        if (index !== -1) {
          this.item.surplus_assets.splice(index, 1);
        }
      },
      removeLose(item) {
        let index = this.item.lose_assets.findIndex((loseItem) => loseItem.id == item.id);
        if (index !== -1) {
          this.item.lose_assets.splice(index, 1);
        }
      },
      confirm() {
        let surplus_assets = [];
        for (let item of this.item.surplus_assets) {
          surplus_assets.push(item.id);
        }

        let lose_assets = [];
        for (let item of this.item.lose_assets) {
          lose_assets.push(item.id);
        }

        this.loading = true;
        stockCheckConfirm({ id: this.item.location, surplus_assets, lose_assets }).then((data) => {
          this.eventChannel.emit('clearPage');
          uni.navigateBack();
        }).finally(() => {
          this.loading = false;
        });
      },
    },
    onLoad(option) {
      this.eventChannel = this.getOpenerEventChannel();
      this.item = JSON.parse(decodeURIComponent(option.item));
    },
  }
</script>

<style>
  .cell-title {
    font-size: 32rpx;
    font-weight: 500;
    margin-bottom: 12rpx;
  }

  .cell-label {
    font-size: 24rpx;
    color: #0005;
  }

  .cell-label-row {
    margin-bottom: 8rpx;
  }
</style>
