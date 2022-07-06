<template>
  <div>
    <a-tree-select
      v-model="value"
      style="width: 100%"
      :placeholder="placeholder"
      :allow-clear="true"
      tree-default-expand-all
      @change="handleChange"
    >
      <a-tree-select-node v-for="item in items" :key="item.id" :value="item.id" :title="item.name" :item="item">
        <a-tree-select-node
          v-for="subItem in item.sub_category_items"
          :key="subItem.id"
          :value="subItem.id"
          :title="subItem.name"
          :item="subItem"
        />
      </a-tree-select-node>
    </a-tree-select>
  </div>
</template>

<script>
import { categoryOptionList } from "@/api/option";

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
      categoryOptionList().then((data) => {
        this.items = data.results;
      });
    },
    handleChange(value) {
      this.$emit("change", value);
    },
  },
  mounted() {
    this.initData();
  },
};
</script>

<style scoped></style>
