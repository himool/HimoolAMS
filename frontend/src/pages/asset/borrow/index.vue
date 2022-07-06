<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="4">
            <category-tree-select v-model="searchForm.fuzzy_category" placeholder="分类" @change="search" />
          </a-col>
          <a-col :span="4">
            <a-input-search v-model="searchForm.search" placeholder="资产编号, 名称" @search="search"></a-input-search>
          </a-col>

          <a-col :span="16" style="padding: 4px 24px">
            <a-checkbox-group v-model="searchForm.status" @change="search" style="width: 100%">
              <a-space>
                <a-checkbox value="idle">闲置</a-checkbox>
                <a-checkbox value="in_use">在用</a-checkbox>
                <a-checkbox value="borrowing">借用中</a-checkbox>
                <a-checkbox value="need_repair">需要维修</a-checkbox>
                <a-checkbox value="under_repair">维修中</a-checkbox>
                <a-checkbox value="scrapped">已报废</a-checkbox>
                <a-checkbox value="lose">丢失</a-checkbox>
              </a-space>
            </a-checkbox-group>
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
            <a-button :disabled="item.status != 'idle'" size="small" @click="borrow(item)">借用</a-button>
            <a-popconfirm title="确定归还吗" :disabled="item.status != 'borrowing'" @confirm="giveBack(item)">
              <a-button :disabled="item.status != 'borrowing'" size="small">归还</a-button>
            </a-popconfirm>
          </div>
        </a-table>
      </div>
    </div>

    <form-modal v-model="visible" :id="targetId" @update="update" />
  </div>
</template>

<script>
import { mapState } from "vuex";
import { assetList } from "@/api/data";
import { assetGiveBack } from "@/api/asset";

export default {
  components: {
    CategoryTreeSelect: () => import("@/components/CategoryTreeSelect/"),
    FormModal: () => import("./FormModal.vue"),
  },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
        enable_borrow: true,
        status: ["idle", "borrowing"],
      },
      pagination: { current: 1, total: 0, pageSize: 16 },
      tableLoading: false,

      tableColumns: [
        {
          title: "序号",
          dataIndex: "index",
          customRender: (value, item, index) => {
            return index + 1 + (this.pagination.current - 1) * this.pagination.pageSize;
          },
        },
        {
          title: "编号",
          dataIndex: "number",
        },
        {
          title: "名称",
          dataIndex: "name",
        },
        {
          title: "分类",
          dataIndex: "category_name",
          customRender: (value, item, index) => {
            return item.category_item?.name;
          },
        },
        {
          title: "状态",
          dataIndex: "status_display",
        },
        {
          title: "借用部门",
          dataIndex: "department_name",
          customRender: (value, item, index) => {
            return item.department_item?.name;
          },
        },
        {
          title: "借用人",
          dataIndex: "person_name",
          customRender: (value, item, index) => {
            return item.person_item?.name;
          },
        },
        {
          title: "借用日期",
          dataIndex: "borrow_date",
        },
        {
          title: "预计归还日期",
          dataIndex: "give_back_date",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],
      categoryItems: [],
      targetId: null,
      visible: false,
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
      assetList(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    update(item) {
      this.tableItems.splice(
        this.tableItems.findIndex((i) => i.id == item.id),
        1,
        item
      );
    },
    borrow(item) {
      this.targetId = item.id;
      this.visible = true;
    },
    giveBack(item) {
      assetGiveBack(item).then((data) => {
        this.$message.success("归还成功");
        this.tableItems.splice(
          this.tableItems.findIndex((i) => i.id == data.id),
          1,
          data
        );
      });
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
