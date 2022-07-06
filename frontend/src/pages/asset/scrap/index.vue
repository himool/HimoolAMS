<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <a-row gutter="12">
        <a-col :span="6" style="max-width: 200px;">
          <category-tree-select v-model="searchForm.fuzzy_category" placeholder="分类" @change="search" />
        </a-col>
        <a-col :span="6" style="max-width: 200px;">
          <a-input-search v-model="searchForm.search" placeholder="资产编号, 名称" @search="search"></a-input-search>
        </a-col>
      </a-row>
    </div>

    <div>
      <div style="margin-top: 16px;">
        <a-table
          :columns="tableColumns"
          :data-source="tableItems"
          :pagination="pagination"
          :loading="tableLoading"
          @change="tableChange"
        >
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { assetList } from "@/api/data";

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
        status: ["scrapped"],
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
          title: "条码",
          dataIndex: "barcode",
        },
        {
          title: "分类",
          dataIndex: "category_name",
          customRender: (value, item, index) => {
            return item.category_item?.name;
          },
        },
        {
          title: "规格",
          dataIndex: "spec",
        },
        {
          title: "型号",
          dataIndex: "model",
        },
        {
          title: "品牌",
          dataIndex: "brand",
        },
        {
          title: "资产原值",
          dataIndex: "original_value",
        },
        {
          title: "状态",
          dataIndex: "status_display",
        },
      ],
      tableItems: [],
      categoryItems: [],
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
