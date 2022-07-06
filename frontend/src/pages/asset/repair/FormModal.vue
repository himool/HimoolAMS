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
      <div slot="title">维修</div>
      <div>
        <a-form-model ref="form" :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-form-model-item prop="repair_date" label="维修日期">
            <a-date-picker v-model="form.repair_date" valueFormat="YYYY-MM-DD" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="repair_description" label="维修说明">
            <a-input v-model="form.repair_description" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { assetNeedRepair } from "@/api/asset";

export default {
  props: ["visible", "id"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      loading: false,
      form: {
        repair_date: undefined,
        repair_description: undefined,
      },
    };
  },
  methods: {
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          assetNeedRepair({ id: this.id, ...this.form })
            .then((data) => {
              this.$message.success("维修中");
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
