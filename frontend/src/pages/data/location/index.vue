<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="6" style="max-width: 200px;">
            <a-input-search v-model="searchForm.search" placeholder="存放地点编号" @search="search"></a-input-search>
          </a-col>

          <a-col style="float: right;">
            <a-button type="primary" icon="plus" @click="openFormModal(defaultForm)">新增地点</a-button>
          </a-col>
        </a-row>
      </div>

      <div style="margin-top: 16px;">
        <a-table
          rowKey="id"
          :columns="tableColumns"
          :data-source="tableItems"
          :pagination="pagination"
          @change="tableChange"
          :loading="tableLoading"
        >
          <div slot="expandedRowRender" slot-scope="item" style="background-color: #fff;">
            <a-table
              rowKey="id"
              size="small"
              :columns="tableColumns"
              :data-source="item.sub_location_items"
              :pagination="false"
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
import { locationList, locationDestroy } from "@/api/data";

export default {
  components: {
    FormModal: () => import("./FormModal.vue"),
  },
  data() {
    return {
      searchForm: { search: "", page: 1, ordering: undefined },
      pagination: { current: 1, total: 0, pageSize: 16 },
      tableLoading: false,
      defaultForm: { manager: undefined },

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
          title: "名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "负责人",
          dataIndex: "manager_name",
          key: "manager_name",
          customRender: (value, item, index) => {
            return item.manager_item?.name;
          },
        },
        {
          title: "地点描述",
          dataIndex: "description",
          key: "description",
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
      locationList(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    create() {
      this.list();
    },
    update() {
      this.list();
    },
    destroy(id) {
      locationDestroy({ id }).then(() => {
        this.list();
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
