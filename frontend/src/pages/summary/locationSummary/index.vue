<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="6" style="max-width: 200px;">
            <a-input-search
              v-model="searchForm.search"
              placeholder="存货地点编号, 名称"
              @search="search"
            ></a-input-search>
          </a-col>

          <a-col :span="6" style="max-width: 200px;">
            <a-button icon="download" @click="exportData">导出</a-button>
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
              <a-button size="small" @click="openDetialModal(item)">详情</a-button>
              <a-button size="small" @click="exportDetial(item)">导出</a-button>
            </a-button-group>
          </div>
        </a-table>
      </div>
    </div>

    <detial-modal v-model="visible" :location="targetLocation" />
  </div>
</template>

<script>
import { mapState } from "vuex";
import { locationSummary, locationSummaryExport, assetSummaryExport } from "@/api/summary";
import { exportExcel } from "@/utils/excel";

export default {
  components: {
    DetialModal: () => import("./DetialModal.vue"),
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
          title: "存放地点名称",
          dataIndex: "location_name",
          key: "location_name",
        },
        {
          title: "资产数量",
          dataIndex: "total_quantity",
          key: "total_quantity",
        },
        {
          title: "资产原值合计",
          dataIndex: "total_amount",
          key: "total_amount",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],
      visible: false,
      targetLocation: undefined,
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
      locationSummary(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    exportData() {
      locationSummaryExport(this.searchForm).then((resp) => {
        exportExcel(resp, "地点汇总");
      });
    },
    tableChange(pagination, filters, sorter) {
      this.searchForm.page = pagination.current;
      this.pagination.current = pagination.current;
      this.searchForm.ordering = `${sorter.order == "descend" ? "-" : ""}${sorter.field}`;
      this.list();
    },
    exportDetial(item) {
      let status = ["in_use"];
      assetSummaryExport({ location: item.location, status }).then((resp) => {
        exportExcel(resp, `地点汇总-${item.location_name}`);
      });
    },
    openDetialModal(item) {
      this.targetLocation = item.location;
      this.visible = true;
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
