<template>
  <div class="new-page" :style="`min-height: ${pageMinHeight}px`">
    <div>
      <div>
        <a-row :gutter="[12, 8]">
          <a-col :span="4">
            <category-tree-select v-model="searchForm.fuzzy_category" placeholder="分类" @change="search" />
          </a-col>
          <a-col :span="4">
            <location-tree-select v-model="searchForm.fuzzy_location" placeholder="地点" @change="search" />
          </a-col>
          <a-col :span="4">
            <department-select v-model="searchForm.department" placeholder="部门" @change="search" />
          </a-col>
          <a-col :span="4">
            <a-input-search v-model="searchForm.search" placeholder="资产编号, 名称" @search="search"></a-input-search>
          </a-col>

          <a-col :span="8">
            <a-button-group>
              <a-upload name="file" :showUploadList="false" :customRequest="importExcel">
                <a-button icon="upload">导入</a-button>
              </a-upload>
              <a-button icon="download" @click="exportExcel">导出</a-button>
            </a-button-group>
          </a-col>

          <a-col :span="16">
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
            <a-button
              :disabled="item.status != 'in_use' && item.status != 'borrowing'"
              size="small"
              @click="transfer(item)"
              >调拨</a-button
            >
          </div>
        </a-table>
      </div>
    </div>

    <form-modal v-model="visible" :form="targetItem" @update="update" />
    <a-modal v-model="importLoading" :footer="null" :maskClosable="false" :closable="false">
      <div><a-spin style="margin-right: 12px" />正在导入中, 请等待...</div>
    </a-modal>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { assetList } from "@/api/data";
import { assetTransferImport, assetTransferExport } from "@/api/asset";
import { exportExcel } from "@/utils/excel";

export default {
  components: {
    CategoryTreeSelect: () => import("@/components/CategoryTreeSelect/"),
    LocationTreeSelect: () => import("@/components/LocationTreeSelect/"),
    DepartmentSelect: () => import("@/components/DepartmentSelect/"),
    FormModal: () => import("./FormModal.vue"),
  },
  data() {
    return {
      searchForm: {
        search: "",
        page: 1,
        ordering: undefined,
        status: ["in_use", "borrowing"],
      },
      pagination: { current: 1, total: 0, pageSize: 16 },
      tableLoading: false,
      importLoading: false,

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
          title: "部门",
          dataIndex: "department_name",
          customRender: (value, item, index) => {
            return item.department_item?.name;
          },
        },
        {
          title: "人员",
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
          title: "操作",
          dataIndex: "action",
          scopedSlots: { customRender: "action" },
        },
      ],
      tableItems: [],
      targetItem: {},
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
    transfer(item) {
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
      assetTransferExport({...this.searchForm})
        .then((resp) => {
          exportExcel(resp, "资产调拨记录");
        })
        .catch((err) => {
          this.$message.error(err.response.data.error);
        });
    },
    // downloadTemplate() {
    //   assetTransferTemplate()
    //     .then((resp) => {
    //       exportExcel(resp, "资产调拨导入模板");
    //     })
    //     .catch((err) => {
    //       this.$message.error(err.response.data.error);
    //     });
    // },
    importExcel(item) {
      let data = new FormData();
      data.append("file", item.file);
      this.importLoading = true;
      setTimeout(() => {
        assetTransferImport(data)
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
