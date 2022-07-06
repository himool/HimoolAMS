<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="6" style="max-width: 200px;">
            <a-select v-model="searchForm.status" style="width: 100%;" placeholder="状态" allowClear @change="search">
              <a-select-option key="idle" value="idle">闲置</a-select-option>
              <a-select-option key="in_use" value="in_use">在用</a-select-option>
              <a-select-option key="borrowing" value="borrowing">借用中</a-select-option>
              <a-select-option key="need_repair" value="need_repair">需要维修</a-select-option>
              <a-select-option key="under_repair" value="under_repair">维修中</a-select-option>
              <a-select-option key="scrapped" value="scrapped">已报废</a-select-option>
              <a-select-option key="lose" value="lose">丢失</a-select-option>
            </a-select>
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

    <detial-modal v-model="visible" :status="targetStatus" />
  </div>
</template>

<script>
import { mapState } from "vuex";
import { statusSummary, statusSummaryExport, assetSummaryExport } from "@/api/summary";
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
          title: "状态",
          dataIndex: "status",
          key: "status",
          customRender: (value) => {
            if (value == "idle") {
              return "闲置";
            } else if (value == "in_use") {
              return "在用";
            } else if (value == "borrowing") {
              return "借用中";
            } else if (value == "under_repair") {
              return "维修中";
            } else if (value == "need_repair") {
              return "需要维修";
            } else if (value == "scrapped") {
              return "已报废";
            } else if (value == "lose") {
              return "丢失";
            }
          },
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
      targetStatus: undefined,
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
      statusSummary(this.searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    exportData() {
      statusSummaryExport(this.searchForm).then((resp) => {
        exportExcel(resp, "状态汇总");
      });
    },
    tableChange(pagination, filters, sorter) {
      this.searchForm.page = pagination.current;
      this.pagination.current = pagination.current;
      this.searchForm.ordering = `${sorter.order == "descend" ? "-" : ""}${sorter.field}`;
      this.list();
    },
    exportDetial(item) {
      let statusDisplay;

      if (item.status == "idle") {
        statusDisplay = "闲置";
      } else if (item.status == "in_use") {
        statusDisplay = "在用";
      } else if (item.status == "borrowing") {
        statusDisplay = "借用中";
      } else if (item.status == "under_repair") {
        statusDisplay = "维修中";
      } else if (item.status == "need_repair") {
        statusDisplay = "需要维修";
      } else if (item.status == "scrapped") {
        statusDisplay = "已报废";
      } else if (item.status == "lose") {
        statusDisplay = "丢失";
      }
      assetSummaryExport({ status: item.status }).then((resp) => {
        exportExcel(resp, `状态汇总-${statusDisplay}`);
      });
    },
    openDetialModal(item) {
      this.targetStatus = item.status;
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
