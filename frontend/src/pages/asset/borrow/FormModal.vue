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
      <div slot="title">借用</div>
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-form-model-item prop="department" label="使用部门">
            <department-select v-model="form.department" @change="form.person = undefined" />
          </a-form-model-item>
          <a-form-model-item prop="person" label="使用人">
            <person-select v-model="form.person" :department="form.department" />
          </a-form-model-item>
          <a-form-model-item prop="borrow_date" label="借用日期">
            <a-date-picker v-model="form.borrow_date" valueFormat="YYYY-MM-DD" style="width: 100%;" />
          </a-form-model-item>
          <a-form-model-item prop="give_back_date" label="预计归还日期">
            <a-date-picker v-model="form.give_back_date" valueFormat="YYYY-MM-DD" style="width: 100%;" />
          </a-form-model-item>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { assetBorrow } from "@/api/asset";
import moment from "moment";

export default {
  components: {
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    PersonSelect: () => import("@/components/PersonSelect/"),
  },
  props: ["visible", "id"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      loading: false,
      form: {
        department: undefined,
        person: undefined,
        borrow_date: undefined,
        give_back_date: undefined,
      },
      rules: {
        department: [{ required: true, message: "请输选择部门", trigger: "change" }],
        person: [{ required: true, message: "请输选择使用人", trigger: "change" }],
        borrow_date: [{ required: true, message: "请输选择借用日期", trigger: "change" }],
      },
    };
  },
  methods: {
    confirm() {
      if (this.form.borrow_date && this.form.give_back_date) {
        let borrowingDate = moment(this.form.borrow_date);
        let expectedReturnDate = moment(this.form.give_back_date);

        if (expectedReturnDate.diff(borrowingDate) < 0) {
          this.$message.warning("预计归还日期不能小于借用日期");
          return;
        }
      }

      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          assetBorrow({ id: this.id, ...this.form })
            .then((data) => {
              this.$message.success("借用成功");
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
