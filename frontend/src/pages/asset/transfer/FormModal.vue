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
      <div slot="title">调拨</div>
      <div>
        <a-form-model
          ref="form"
          :model="dataForm"
          :rules="form.status == 'in_use' ? inUseRules : borrowRules"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 18 }"
        >
          <a-form-model-item prop="department" label="部门">
            <department-select v-model="dataForm.department" @change="dataForm.person = null" />
          </a-form-model-item>
          <a-form-model-item prop="person" label="人员">
            <person-select v-model="dataForm.person" :department="dataForm.department" @change="changePerson" />
          </a-form-model-item>
          <a-form-model-item v-if="form.status == 'in_use'" prop="location" label="存放地点">
            <location-tree-select v-model="dataForm.location" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { assetTransfer } from "@/api/asset";

export default {
  components: {
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    PersonSelect: () => import("@/components/PersonSelect/"),
    LocationTreeSelect: () => import("@/components/LocationTreeSelect/"),
  },
  props: ["visible", "form"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      dataForm: { person: undefined },
      loading: false,
      inUseRules: {
        department: [{ required: true, message: "请输选择部门", trigger: "change" }],
        location: [{ required: true, message: "请选择存放地点", trigger: "change" }],
      },
      borrowRules: {
        department: [{ required: true, message: "请输选择部门", trigger: "change" }],
        person: [{ required: true, message: "请输选择使用人", trigger: "change" }],
      },
    };
  },
  methods: {
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          console.log({ ...this.dataForm });
          assetTransfer({ ...this.dataForm })
            .then((data) => {
              this.$message.success("调拨成功");
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
    changePerson(value) {
      console.log(value);
      if (value === undefined) {
        this.dataForm.person = null;
      }
    },
  },
  watch: {
    visible(status) {
      if (status) {
        this.dataForm = {
          id: this.form.id,
          department: this.form.department,
          person: this.form.person,
          location: this.form.location,
        };
      }
    },
  },
};
</script>

<style scoped></style>
