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
      <div slot="title">{{ form.id ? "编辑地点" : "新增地点" }}</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
          <a-form-model-item prop="name" label="名称">
            <a-input v-model="form.name" />
          </a-form-model-item>
          <a-form-model-item v-if="!form.id" prop="parent" label="一级地点">
            <location-select v-model="form.parent" />
          </a-form-model-item>
          <a-form-model-item prop="department" label="部门">
            <department-select v-model="form.department" @change="form.manager = undefined" />
          </a-form-model-item>
          <a-form-model-item prop="manager" label="负责人">
            <person-select v-model="form.manager" :department="form.department" />
          </a-form-model-item>
          <a-form-model-item prop="description" label="地点描述">
            <a-input v-model="form.description" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { locationCreate, locationUpdate } from "@/api/data";
import rules from "./rules.js";

export default {
  components: {
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    LocationSelect: () => import("@/components/LocationSelect/"),
    PersonSelect: () => import("@/components/PersonSelect/"),
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
          let func = this.form.id ? locationUpdate : locationCreate;
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
