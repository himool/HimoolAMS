<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <a-row gutter="12">
        <a-col :span="24" :md="8" :xl="5" style="max-width: 256px; margin-bottom: 12px">
          <a-range-picker @change="onChangePicker" />
        </a-col>
        <a-col :span="6" style="max-width: 200px;">
          <location-tree-select v-model="searchForm.fuzzy_location" placeholder="地点" @change="search" />
        </a-col>
      </a-row>
    </div>

    <div>
      <div style="margin-top: 16px;">
        <a-table :columns="tableColumns" :data-source="tableItems" :pagination="pagination" :loading="tableLoading">
          <template slot="action" slot-scope="value, item">
            <a-button size="small" @click="openDetialModal(item)">
              详情
            </a-button>
          </template>
        </a-table>
      </div>
    </div>

    <a-modal
      v-model="visible"
      :width="960"
      title="详情"
      :destroyOnClose="true"
      :maskClosable="true"
      centered
      :footer="null"
    >
      <a-table
        :columns="detialColumns"
        :data-source="targetItem.stock_check_asset_items"
        :pagination="false"
        @change="tableChange"
      />
    </a-modal>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { stockCheckRecordList } from "@/api/stockCheck";
import moment from "moment";

export default {
  components: {
    LocationTreeSelect: () => import("@/components/LocationTreeSelect/"),
  },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
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
          title: "存放地点",
          dataIndex: "location_name",
          key: "location_name",
          customRender: (value, item, index) => {
            return item.location_item?.name;
          },
        },
        {
          title: "创建时间",
          dataIndex: "create_time",
          key: "create_time",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      detialColumns: [
        {
          title: "序号",
          dataIndex: "index",
          key: "index",
          customRender: (value, item, index) => {
            return index + 1 + (this.pagination.current - 1) * this.pagination.pageSize;
          },
        },
        {
          title: "资产RFID编号",
          dataIndex: "asset_number",
          key: "asset_number",
          customRender: (value, item, index) => item.asset_item?.number,
        },
        {
          title: "资产名称",
          dataIndex: "asset_name",
          key: "asset_name",
          customRender: (value, item, index) => item.asset_item?.name,
        },
        {
          title: "资产条码",
          dataIndex: "asset_barcode",
          key: "asset_barcode",
          customRender: (value, item, index) => item.asset_item?.barcode,
        },
        {
          title: "规格",
          dataIndex: "asset_spec",
          key: "asset_spec",
          customRender: (value, item, index) => item.asset_item?.spec,
        },
        {
          title: "型号",
          dataIndex: "asset_model",
          key: "asset_model",
          customRender: (value, item, index) => item.asset_item?.model,
        },
        {
          title: "品牌",
          dataIndex: "asset_brand",
          key: "asset_brand",
          customRender: (value, item, index) => item.asset_item?.brand,
        },
        {
          title: "存放地点",
          dataIndex: "location_name",
          key: "location_name",
          customRender: (value, item, index) => this.targetItem.location_item.name,
        },
        {
          title: "状态",
          dataIndex: "status_display",
          key: "status_display",
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
      let searchForm = { ...this.searchForm };
      if (searchForm.end_date) {
        searchForm.end_date = moment(searchForm.end_date)
          .add(1, "days")
          .format("YYYY-MM-DD");
      }

      stockCheckRecordList(searchForm)
        .then((data) => {
          this.pagination.total = data.count;
          this.tableItems = data.results;
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    onChangePicker(date, dateString) {
      let startDate = date[0],
        endDate = date[1];
      this.searchForm.start_date = startDate ? startDate.format("YYYY-MM-DD") : undefined;
      this.searchForm.end_date = endDate ? endDate.format("YYYY-MM-DD") : undefined;
      this.search();
    },
    openDetialModal(item) {
      this.targetItem = item;
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
