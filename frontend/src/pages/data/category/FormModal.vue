<template>
  <div>
    <a-modal
      v-model="visible"
      :confirmLoading="loading"
      :destroyOnClose="true"
      :maskClosable="false"
      centered
      @cancel="cancel"
      @ok="confirm"
    >
      <div slot="title">{{ form.id ? "编辑分类" : "新增分类" }}</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 14 }">
          <a-form-model-item prop="number" label="编号">
            <a-input v-model="form.number" />
          </a-form-model-item>
          <a-form-model-item prop="name" label="名称">
            <a-input v-model="form.name" />
          </a-form-model-item>
          <a-form-model-item v-if="!form.id" prop="parent" label="一级分类">
            <category-select v-model="form.parent" />
          </a-form-model-item>
          <a-form-model-item prop="origin" label="产地">
            <a-input v-model="form.origin" />
          </a-form-model-item>
          <a-form-model-item prop="property_unit" label="产权单位">
            <a-input v-model="form.property_unit" />
          </a-form-model-item>
          <a-form-model-item prop="unit" label="计量单位">
            <a-input v-model="form.unit" />
          </a-form-model-item>
          <a-form-model-item prop="depreciation_life" label="折旧年限">
            <a-input-number v-model="form.depreciation_life" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="useful_life" label="使用年限">
            <a-input-number v-model="form.useful_life" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="description" label="描述">
            <a-input v-model="form.description" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { categoryCreate, categoryUpdate } from "@/api/data";
import rules from "./rules.js";

export default {
  components: {
    CategorySelect: () => import("@/components/CategorySelect/"),
  },
  props: ["visible", "form"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      rules,
      loading: false,
    };
  },
  computed: {
    parents() {
      let items = [];
      for (let item of this.$parent.tableItems) {
        if (item.id != this.form.id) {
          items.push(item);
        }
      }
      return items;
    },
  },
  methods: {
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          let func = this.form.id ? categoryUpdate : categoryCreate;
          func(this.form)
            .then((data) => {
              this.$message.success(this.form.id ? "修改成功" : "新增成功");
              this.$emit(this.form.id ? "update" : "create", data);
              this.cancel();
            })
            .finally(() => {
              this.loading = false;
            });
        }
      });
    },
    cancel() {
      this.$emit("cancel", false);
      this.$refs.form.resetFields();
    },
  },
};
</script>

<style scoped></style>
