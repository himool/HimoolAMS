<template>
  <div>
    <a-modal v-model="visible" title="详情" :width="1200" :destroyOnClose="true" :footer="null" @cancel="cancel">
      <a-table
        :columns="tableColumns"
        :data-source="tableItems"
        :pagination="pagination"
        :loading="tableLoading"
        @change="tableChange"
      />
    </a-modal>
  </div>
</template>

<script>
import { assetList } from "@/api/data";

export default {
  props: ["visible", "person"],
  model: { prop: "visible", event: "cancel" },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
        status: ["in_use", "borrowing"],
      },
      pagination: { current: 1, total: 0, pageSize: 16 },
      tableLoading: false,
      tableColumns: [
        {
          title: "序号",
          dataIndex: "index",
          key: "index",
          customRender: (value, item, index) => {
            return index + 1 + (this.pagination.current - 1) * this.pagination.pageSize;
          },
        },
        {
          title: "RFID编号",
          dataIndex: "number",
          key: "number",
        },
        {
          title: "名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "条码",
          dataIndex: "barcode",
          key: "barcode",
        },
        {
          title: "规格",
          dataIndex: "spec",
          key: "spec",
        },
        {
          title: "型号",
          dataIndex: "model",
          key: "model",
        },
        {
          title: "部门",
          dataIndex: "department_name",
          customRender: (value, item, index) => {
            return item.department_item?.name;
          },
        },
        {
          title: "人员",
          dataIndex: "person_name",
          customRender: (value, item, index) => {
            return item.person_item?.name;
          },
        },
        {
          title: "地点",
          dataIndex: "location_name",
          customRender: (value, item, index) => {
            return item.location_item?.name;
          },
        },
        {
          title: "状态",
          key: "status_display",
          dataIndex: "status_display",
        },
      ],
      tableItems: [],
    };
  },
  methods: {
    list() {
      this.tableLoading = true;
      assetList(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    tableChange(pagination, filters, sorter) {
      this.searchForm.page = pagination.current;
      this.pagination.current = pagination.current;
      this.searchForm.ordering = `${sorter.order == "descend" ? "-" : ""}${sorter.field}`;
      this.list();
    },
    cancel() {
      this.$emit("cancel", false);
    },
  },
  watch: {
    visible(status) {
      if (status) {
        this.tableItems = [];
        this.searchForm.person = this.person;
        this.searchForm.page = 1;
        this.pagination.current = 1;
        this.list();
      }
    },
  },
};
</script>

<style scoped></style>
