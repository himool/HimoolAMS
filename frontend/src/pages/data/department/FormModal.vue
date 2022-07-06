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
      <div slot="title">{{ form.id ? "编辑部门" : "新增部门" }}</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 14 }">
          <a-form-model-item prop="number" label="编号">
            <a-input v-model="form.number" />
          </a-form-model-item>
          <a-form-model-item prop="name" label="名称">
            <a-input v-model="form.name" />
          </a-form-model-item>
          <a-form-model-item prop="remark" label="备注">
            <a-input v-model="form.remark" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { departmentCreate, departmentUpdate } from "@/api/data";
import rules from "./rules.js";

export default {
  name: "FormModal",
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
          let func = this.form.id ? departmentUpdate : departmentCreate;
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
