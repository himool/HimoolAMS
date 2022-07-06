<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="4">
            <department-select v-model="searchForm.department" placeholder="部门" @change="search" />
          </a-col>
          <a-col :span="6" style="max-width: 200px;">
            <a-input-search v-model="searchForm.search" placeholder="人员编号, 名称" @search="search"></a-input-search>
          </a-col>

          <a-col style="float: right;">
            <a-button type="primary" icon="plus" @click="openFormModal({})">新增人员</a-button>
          </a-col>
        </a-row>
      </div>

      <div style="margin-top: 16px;">
        <a-table
          :columns="tableColumns"
          :data-source="tableItems"
          :pagination="pagination"
          :loading="tableLoading"
          @change="tableChange"
        >
          <div slot="action" slot-scope="value, item">
            <a-button-group>
              <a-button icon="edit" size="small" @click="openFormModal(item)">编辑</a-button>
              <a-popconfirm title="确定删除吗" @confirm="destroy(item.id)">
                <a-button type="danger" icon="delete" size="small">删除</a-button>
              </a-popconfirm>
            </a-button-group>
          </div>
        </a-table>
      </div>
    </div>

    <form-modal v-model="visible" :form="targetItem" @create="create" @update="update" />
  </div>
</template>

<script>
import { mapState } from "vuex";
import { personList, personDestroy } from "@/api/data";

export default {
  components: {
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    FormModal: () => import("./FormModal.vue"),
  },
  data() {
    return {
      searchForm: { search: "", page: 1, ordering: undefined },
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
          title: "编号",
          dataIndex: "number",
          key: "number",
        },
        {
          title: "名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "部门",
          dataIndex: "department_item",
          key: "department_name",
          customRender: (value, item, index) => {
            return item.department_item?.name;
          },
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],

      visible: false,
      targetItem: {},
    };
  },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
  },
  methods: {
    initData() {
      this.list();
    },
    search() {
      this.searchForm.page = 1;
      this.pagination.current = 1;
      this.list();
    },
    list() {
      this.tableLoading = true;
      personList(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    create(item) {
      this.tableItems.splice(0, 0, item);
    },
    update(item) {
      this.tableItems.splice(
        this.tableItems.findIndex((i) => i.id == item.id),
        1,
        item
      );
    },
    destroy(id) {
      personDestroy({ id }).then(() => {
        this.tableItems.splice(
          this.tableItems.findIndex((item) => item.id == id),
          1
        );
        this.$message.success("删除成功");
      });
    },
    openFormModal(item) {
      this.targetItem = { ...item };
      this.visible = true;
    },
    tableChange(pagination, filters, sorter) {
      this.searchForm.page = pagination.current;
      this.pagination.current = pagination.current;
      this.searchForm.ordering = `${sorter.order == "descend" ? "-" : ""}${sorter.field}`;
      this.list();
    },
  },
  mounted() {
    this.initData();
  },
};
</script>

<style scoped lang="less">
@import "index";
</style>
