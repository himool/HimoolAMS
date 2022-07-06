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
            <a-popconfirm title="确定退库吗" :disabled="item.status != 'in_use'" @confirm="stockReturn(item)">
              <a-button :disabled="item.status != 'in_use'" size="small">退库</a-button>
            </a-popconfirm>
          </div>
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { assetList, categoryList } from "@/api/data";
import { assetReturnStock } from "@/api/asset";

export default {
  components: {
    CategoryTreeSelect: () => import("@/components/CategoryTreeSelect/"),
  },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
        status: ["in_use"],
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
          title: "使用部门",
          dataIndex: "department_name",
          customRender: (value, item, index) => {
            return item.department_item?.name;
          },
        },
        {
          title: "使用人",
          dataIndex: "person_name",
          customRender: (value, item, index) => {
            return item.person_item?.name;
          },
        },
        {
          title: "存放地点",
          dataIndex: "location_name",
          customRender: (value, item, index) => {
            return item.location_item?.name;
          },
        },
        {
          title: "领用日期",
          dataIndex: "collect_date",
        },
        {
          title: "领用描述",
          dataIndex: "collect_description",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],
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
    stockReturn(item) {
      assetReturnStock(item).then((data) => {
        this.$message.success("退库成功");
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
