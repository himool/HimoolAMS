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
      <a-select-option v-for="item in dataSource" :key="item.id" :value="item.id" :item="item">
        {{ item.name }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
import { personOptionList } from "@/api/option";

export default {
  props: ["value", "disabled", "placeholder", "department"],
  model: { prop: "value", event: "change" },
  data() {
    return {
      items: [],
    };
  },
  computed: {
    dataSource() {
      let items = [];
      for (let item of this.items) {
        if (!this.department || this.department == item.department) {
          items.push(item);
        }
      }
      return items;
    },
  },
  methods: {
    initData() {
      this.list();
    },
    list() {
      personOptionList().then((data) => {
        this.items = data.results;
      });
    },
    handleChange(value) {
      this.$emit("change", value);
    },
    filterOption(input, option) {
      let item = option.data.attrs.item;
      if (
        item.number.toLowerCase().indexOf(input.toLowerCase()) !== -1 ||
        item.name.toLowerCase().indexOf(input.toLowerCase()) !== -1
      ) {
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
