<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row :gutter="[12, 8]">
          <a-col :span="4">
            <a-range-picker @change="onChangePicker" />
          </a-col>

          <a-col :span="4">
            <a-select v-model="searchForm.type" style="width: 100%;" placeholder="类型" allowClear @change="search">
              <a-select-option key="create" value="create">创建</a-select-option>
              <a-select-option key="collect" value="collect">领用</a-select-option>
              <a-select-option key="return_stock" value="return_stock">退库</a-select-option>
              <a-select-option key="borrow" value="borrow">借用</a-select-option>
              <a-select-option key="give_back" value="give_back">归还</a-select-option>
              <a-select-option key="transfer_out" value="transfer_out">调拨出</a-select-option>
              <a-select-option key="transfer_in" value="transfer_in">调拨入</a-select-option>
              <a-select-option key="need_repair" value="need_repair">需要维修</a-select-option>
              <a-select-option key="under_repair" value="under_repair">维修中</a-select-option>
              <a-select-option key="fixed" value="fixed">已修复</a-select-option>
              <a-select-option key="scrapped" value="scrapped">已报废</a-select-option>
              <a-select-option key="lose" value="lose">丢失</a-select-option>
              <a-select-option key="surplus" value="surplus">盘盈</a-select-option>
            </a-select>
          </a-col>
          <a-col :span="4">
            <location-tree-select v-model="searchForm.fuzzy_location" placeholder="地点" @change="search" />
          </a-col>
          <a-col :span="4">
            <department-select
              v-model="searchForm.department"
              placeholder="部门"
              @change="
                () => {
                  searchForm.person = undefined;
                  search();
                }
              "
            />
          </a-col>
          <a-col :span="4">
            <person-select
              v-model="searchForm.person"
              placeholder="人员"
              :department="searchForm.department"
              @change="search"
            />
          </a-col>
          <a-col :span="4">
            <a-input-search v-model="searchForm.search" placeholder="资产编号, 名称" @search="search"></a-input-search>
          </a-col>

          <a-col :span="24" :md="6" :xl="8" style="margin-bottom: 12px">
            <a-button icon="download" @click="exportExcel">导出</a-button>
          </a-col>
        </a-row>
      </div>

      <div>
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
import { assetFlowList, assetFlowExport } from "@/api/asset";
import { exportExcel } from "@/utils/excel";

export default {
  components: {
    LocationTreeSelect: () => import("@/components/LocationTreeSelect/"),
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    PersonSelect: () => import("@/components/PersonSelect/"),
  },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
        person: undefined,
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
          customRender: (value, item, index) => item.asset_item.number,
        },
        {
          title: "名称",
          dataIndex: "name",
          customRender: (value, item, index) => item.asset_item.name,
        },
        {
          title: "条码",
          dataIndex: "barcode",
          customRender: (value, item, index) => item.asset_item.barcode,
        },
        {
          title: "规格",
          dataIndex: "spec",
          customRender: (value, item, index) => item.asset_item.spec,
        },
        {
          title: "型号",
          dataIndex: "model",
          customRender: (value, item, index) => item.asset_item.model,
        },
        {
          title: "品牌",
          dataIndex: "brand",
          customRender: (value, item, index) => item.asset_item.brand,
        },
        {
          title: "地点",
          dataIndex: "name",
          customRender: (value, item, index) => item.location_item?.name,
        },
        {
          title: "部门",
          dataIndex: "department_name",
          customRender: (value, item, index) => item.department_item?.name,
        },
        {
          title: "人员",
          dataIndex: "name",
          customRender: (value, item, index) => item.person_item?.name,
        },
        {
          title: "类型",
          dataIndex: "type_display",
        },
        {
          title: "记录时间",
          dataIndex: "create_time",
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
      assetFlowList(this.searchForm)
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
    onChangePicker(date, dateString) {
      let startDate = date[0],
        endDate = date[1];
      this.searchForm.start_date = startDate ? startDate.format("YYYY-MM-DD") : undefined;
      this.searchForm.end_date = endDate ? endDate.format("YYYY-MM-DD") : undefined;
      this.search();
    },
    exportExcel() {
      assetFlowExport(this.searchForm)
        .then((resp) => {
          exportExcel(resp, "资产流水");
        })
        .catch((err) => {
          this.$message.error(err.response.data.error);
        });
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
