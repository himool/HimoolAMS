import TabsView from "@/layouts/tabs/TabsView";
import BlankView from "@/layouts/BlankView";
// import PageView from '@/layouts/PageView'

// 路由配置
const options = {
  routes: [
    {
      path: "/login",
      name: "登录页",
      component: () => import("@/pages/login"),
    },
    {
      path: "*",
      name: "404",
      component: () => import("@/pages/exception/404"),
    },
    {
      path: "/403",
      name: "403",
      component: () => import("@/pages/exception/403"),
    },
    {
      path: "/",
      name: "首页",
      component: TabsView,
      redirect: "/home",
      children: [
        {
          path: "/home",
          name: "首页",
          meta: {
            icon: "home",
          },
          component: () => import("@/pages/home"),
        },
        {
          path: "data",
          name: "基础数据",
          meta: {
            icon: "table",
          },
          component: BlankView,
          children: [
            {
              path: "department",
              name: "部门管理",
              component: () => import("@/pages/data/department"),
            },
            {
              path: "person",
              name: "人员管理",
              component: () => import("@/pages/data/person"),
            },
            {
              path: "location",
              name: "存放地点",
              component: () => import("@/pages/data/location"),
            },
            {
              path: "category",
              name: "资产分类",
              component: () => import("@/pages/data/category"),
            },
            {
              path: "asset",
              name: "资产清单",
              component: () => import("@/pages/asset/info"),
            },
          ],
        },
        {
          path: "asset",
          name: "资产管理",
          meta: {
            icon: "database",
          },
          component: BlankView,
          children: [
            {
              path: "collect",
              name: "资产领用",
              component: () => import("@/pages/asset/collect"),
            },
            {
              path: "return_stock",
              name: "资产退库",
              component: () => import("@/pages/asset/returnStock"),
            },
            {
              path: "borrow",
              name: "借用&归还",
              component: () => import("@/pages/asset/borrow"),
            },
            {
              path: "transfer",
              name: "资产调拨",
              component: () => import("@/pages/asset/transfer"),
            },
            {
              path: "repair",
              name: "资产维修",
              component: () => import("@/pages/asset/repair"),
            },
          ],
        },
        {
          path: "summary",
          name: "统计报表",
          meta: {
            icon: "bar-chart",
          },
          component: BlankView,
          children: [
            {
              path: "category_summary",
              name: "分类汇总",
              component: () => import("@/pages/summary/categorySummary"),
            },
            {
              path: "location_summary",
              name: "地点汇总",
              component: () => import("@/pages/summary/locationSummary"),
            },
            {
              path: "department_summary",
              name: "部门汇总",
              component: () => import("@/pages/summary/departmentSummary"),
            },
            {
              path: "person_summary",
              name: "人员汇总",
              component: () => import("@/pages/summary/personSummary"),
            },
            {
              path: "status_summary",
              name: "状态汇总",
              component: () => import("@/pages/summary/statusSummary"),
            },
          ],
        },
        {
          path: "record",
          name: "记录查询",
          meta: {
            icon: "profile",
          },
          component: BlankView,
          children: [
            {
              path: "stock_check_record",
              name: "盘点记录",
              component: () => import("@/pages/record/stockCheckRecord"),
            },
            {
              path: "asset_flow",
              name: "资产流水",
              component: () => import("@/pages/record/assetFlow"),
            },
          ],
        },
      ],
    },
  ],
};

export default options;
