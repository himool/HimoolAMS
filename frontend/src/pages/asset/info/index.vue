<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row gutter="12">
          <a-col :span="4">
            <category-tree-select v-model="searchForm.fuzzy_category" placeholder="分类" @change="search" />
          </a-col>
          <a-col :span="4">
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
          <a-col :span="4">
            <a-input-search v-model="searchForm.search" placeholder="资产编号, 名称" @search="search"></a-input-search>
          </a-col>
          <a-col :span="24" :md="6" :xl="8" style="margin-bottom: 12px">
            <a-button-group>
              <a-button icon="file-excel" @click="downloadTemplate">模板下载</a-button>
              <a-upload name="file" :showUploadList="false" :customRequest="importExcel">
                <a-button icon="upload">导入</a-button>
              </a-upload>
              <a-button icon="download" @click="exportExcel">导出</a-button>
            </a-button-group>
          </a-col>

          <a-col style="float: right;">
            <a-button type="primary" icon="plus" @click="openFormModal({})">新增资产</a-button>
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
    <a-modal v-model="importLoading" :footer="null" :maskClosable="false" :closable="false">
      <div><a-spin style="margin-right: 12px" />正在导入中, 请等待...</div>
    </a-modal>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { assetList, assetDestroy } from "@/api/data";
import { assetTemplate, assetImport, assetExport } from "@/api/asset";
import { exportExcel } from "@/utils/excel";

export default {
  components: {
    CategoryTreeSelect: () => import("@/components/CategoryTreeSelect/"),
    FormModal: () => import("./FormModal.vue"),
  },
  data() {
    return {
      searchForm: { search: "", page: 1, ordering: undefined },
      pagination: { current: 1, total: 0, pageSize: 16 },
      tableLoading: false,
      importLoading: false,

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
          title: "分类",
          dataIndex: "category_item",
          key: "category_item",
          customRender: (item) => item?.name,
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
          title: "品牌",
          dataIndex: "brand",
          key: "brand",
        },
        {
          title: "资产原值",
          dataIndex: "original_value",
          key: "original_value",
        },
        {
          title: "状态",
          key: "status_display",
          dataIndex: "status_display",
        },
        {
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],
      categoryItems: [],

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
      assetList(this.searchForm)
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
      assetDestroy({ id }).then(() => {
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
    exportExcel() {
      assetExport(this.searchForm)
        .then((resp) => {
          exportExcel(resp, "资产列表");
        })
        .catch((err) => {
          this.$message.error(err.response.data.error);
        });
    },
    downloadTemplate() {
      assetTemplate()
        .then((resp) => {
          exportExcel(resp, "资产导入模板");
        })
        .catch((err) => {
          this.$message.error(err.response.data.error);
        });
    },
    importExcel(item) {
      let data = new FormData();
      data.append("file", item.file);
      this.importLoading = true;
      setTimeout(() => {
        assetImport(data)
          .then(() => {
            this.$message.success("导入成功");
            this.list();
          })
          .catch((err) => {
            this.$message.error(err.response.data.detail);
          })
          .finally(() => {
            this.importLoading = false;
          });
      }, 1000);
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
