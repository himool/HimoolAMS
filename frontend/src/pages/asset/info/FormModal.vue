<template>
  <div>
    <a-modal v-model="visible" :confirmLoading="loading" :maskClosable="false" centered @cancel="cancel" @ok="confirm">
      <div slot="title">{{ form.id ? "编辑资产" : "新增资产" }}</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
          <a-form-model-item prop="number" label="RFID编号">
            <a-input v-model="form.number" />
          </a-form-model-item>
          <a-form-model-item prop="name" label="名称">
            <a-input v-model="form.name" />
          </a-form-model-item>
          <a-form-model-item prop="barcode" label="条码">
            <a-input v-model="form.barcode" />
          </a-form-model-item>
          <a-form-model-item prop="category" label="分类">
            <category-tree-select v-model="form.category" />
          </a-form-model-item>
          <a-form-model-item prop="spec" label="规格">
            <a-input v-model="form.spec" />
          </a-form-model-item>
          <a-form-model-item prop="model" label="型号">
            <a-input v-model="form.model" />
          </a-form-model-item>
          <a-form-model-item prop="brand" label="品牌">
            <a-input v-model="form.brand" />
          </a-form-model-item>
          <a-form-model-item prop="original_value" label="资产原值">
            <a-input-number v-model="form.original_value" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="enable_borrow" label="借用状态">
            <a-select v-model="form.enable_borrow" style="width: 100%;">
              <a-select-option key="0" :value="true">开启</a-select-option>
              <a-select-option key="1" :value="false">关闭</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { assetCreate, assetUpdate } from "@/api/data";
import rules from "./rules.js";

export default {
  components: {
    CategoryTreeSelect: () => import("@/components/CategoryTreeSelect/"),
  },
  props: ["visible", "form"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      rules,
      loading: false,
    };
  },
  methods: {
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          let func = this.form.id ? assetUpdate : assetCreate;
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
