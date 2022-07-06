<template>
  <view>
    <view>
      <u-cell title="一级地点" :value="locationItem.name" isLink @click="showLocationPicker = true" />
      <u-cell title="二级地点" :value="subLocationItem.name" isLink @click="showSubLocationPicker = true" />
    </view>

    <view style="margin-top: 12rpx;">
      <u--textarea v-model="numberText" border="bottom" placeholder="请扫描资产" height="200" :maxlength="-1"
        @input="changeNumber" />
    </view>

    <view style="position: fixed; bottom: 0; width: 100%;">
      <u-button type="primary" size="large" text="上传数据" :loading="loading" :disabled="loading" @click="validate" />
    </view>

    <u-picker :show="showLocationPicker" :columns="[locationItems]" keyName="name" :closeOnClickOverlay="true"
      @close="showLocationPicker = false" @cancel="showLocationPicker = false" @confirm="selectLocation" />
    <u-picker :show="showSubLocationPicker" :columns="[subLocationItems]" keyName="name" :closeOnClickOverlay="true"
      @close="showSubLocationPicker = false" @cancel="showSubLocationPicker = false" @confirm="selectSubLocation" />
    <u-toast ref="uToast"></u-toast>
  </view>
</template>

<script>
  import { locationOption } from '@/api/option.js';
  import { stockCheckValidate } from '@/api/stockCheck.js';

  export default {
    data() {
      return {
        numberText: '',
        loading: false,

        locationItem: {},
        subLocationItem: {},

        locationItems: [],
        subLocationItems: [],
        showLocationPicker: false,
        showSubLocationPicker: false,
      }
    },
    methods: {
      initData() {
        locationOption().then((data) => {
          this.locationItems = data.results;
          let subLocationItems = [];
          for (let locationItem of this.locationItems) {
            subLocationItems = subLocationItems.concat(locationItem.sub_location_items);
          }
          this.subLocationItems = subLocationItems;
          console.log(subLocationItems)
        });
      },
      selectLocation(item) {
        this.locationItem = this.locationItems[item.indexs[0]];
        this.subLocationItems = this.locationItem.sub_location_items;
        this.subLocationItem = {};
        this.showLocationPicker = false;
      },
      selectSubLocation(item) {
        this.subLocationItem = this.subLocationItems[item.indexs[0]];
        this.showSubLocationPicker = false;
      },
      validate() {
        const that = this;

        let location = this.subLocationItem.id || this.locationItem.id;
        if (!location) {
          this.$refs.uToast.show({ type: 'error', message: "请选择位置", });
          return;
        }

        let numberList = [];
        for (let number of this.numberText.split('\n')) {
          if (number.length > 0) numberList.push(number);
        }

        this.loading = true;
        stockCheckValidate({ id: location, number_list: numberList }).then((data) => {
          this.$refs.uToast.show({ type: 'success', message: "上传成功", });
          data.location = location;
          uni.navigateTo({
            url: '../stockCheckDetial/index?item=' + encodeURIComponent(JSON.stringify(data)),
            events: {
              clearPage() {
                that.clearPage();
              },
            },
          });
        }).finally(() => {
          this.loading = false;
        });
      },
      changeNumber(value) {
        let numberList = new Set(value.split('\n'));
        numberList.delete('');
        this.numberText = Array.from(numberList).join('\n') + '\n';
      },
      clearPage() {
        this.$refs.uToast.show({ type: 'success', message: "盘点成功" });
        this.locationItem = {};
        this.subLocationItem = {};
        this.numberText = '';
      },
    },
    onLoad() {
      this.initData();
    },
  }
</script>

<style>
</style>
