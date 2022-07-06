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
      <div slot="title">领用</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-form-model-item prop="department" label="使用部门">
            <department-select v-model="form.department" @change="form.person = undefined" />
          </a-form-model-item>
          <a-form-model-item prop="person" label="使用人">
            <person-select v-model="form.person" :department="form.department" />
          </a-form-model-item>
          <a-form-model-item prop="location" label="存放地点">
            <location-tree-select v-model="form.location" />
          </a-form-model-item>
          <a-form-model-item prop="collect_date" label="领用日期">
            <a-date-picker v-model="form.collect_date" valueFormat="YYYY-MM-DD" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="collect_description" label="领用说明">
            <a-input v-model="form.collect_description" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { assetCollect } from "@/api/asset";

export default {
  components: {
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    PersonSelect: () => import("@/components/PersonSelect/"),
    LocationTreeSelect: () => import("@/components/LocationTreeSelect/"),
  },
  props: ["visible", "id"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      loading: false,
      form: {
        department: undefined,
        person: undefined,
        location: undefined,
        collect_date: undefined,
        collect_description: undefined,
      },
      rules: {
        department: [{ required: true, message: "请选择部门", trigger: "change" }],
        location: [{ required: true, message: "请选择存放地点", trigger: "change" }],
        collect_date: [{ required: true, message: "请选择领用日期", trigger: "change" }],
      },
    };
  },
  methods: {
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          assetCollect({ id: this.id, ...this.form })
            .then((data) => {
              this.$message.success("领用成功");
              this.$emit("update", data);
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
