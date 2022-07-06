<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <a-row gutter="16">
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="资产总数" :value="item.total_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="资产总值" :value="item.total_value" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="空闲数量" :value="item.idle_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="在用数量" :value="item.in_use_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="借出数量" :value="item.borrow_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="维修数量" :value="item.under_repair_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="报废数量" :value="item.scrapped_quantity" />
        </a-card>
      </a-col>
      <a-col :span="3">
        <a-card :loading="loading">
          <a-statistic title="报废总值" :value="item.scrapped_amount" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { homeSummary } from "@/api/summary";

export default {
  data() {
    return {
      loading: false,
      item: {},
    };
  },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
  },
  methods: {
    initData() {
      this.list();
    },
    list() {
      this.loading = true;
      homeSummary()
        .then((data) => {
          this.item = data;
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  mounted() {
    this.initData();
  },
};
</script>

<style scoped lang="less">
@import "index";
</style>
