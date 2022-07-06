<template>
  <div>
    <a-select
      :value="value"
      :show-search="true"
      :placeholder="placeholder"
      :filter-option="filterOption"
      style="width: 100%"
      :allow-clear="true"
      @change="handleChange"
    >
      <a-select-option v-for="item in items" :key="item.id" :value="item.id" :item="item">
        {{ item.name }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
import { locationOptionList } from "@/api/option";

export default {
  props: ["value", "disabled", "placeholder"],
  model: { prop: "value", event: "change" },
  data() {
    return {
      items: [],
    };
  },
  methods: {
    initData() {
      this.list();
    },
    list() {
      locationOptionList().then((data) => {
        this.items = data.results;
      });
    },
    handleChange(value) {
      this.$emit("change", value);
    },
    filterOption(input, option) {
      let item = option.data.attrs.item;
      if (item.name.toLowerCase().indexOf(input.toLowerCase()) !== -1) {
        return true;
      }
      return false;
    },
  },
  mounted() {
    this.initData();
  },
};
</script>

<style scoped></style>
