export default {
  number: [
    { required: true, message: "请输入资产编号", trigger: "change" },
    { max: 32, message: "超出最大长度 (32)", trigger: "change" },
  ],
  name: [
    { required: true, message: "请输入资产名称", trigger: "change" },
    { max: 64, message: "超出最大长度 (64)", trigger: "change" },
  ],
  category: [{ required: true, message: "请选择分类", trigger: "change" }],
  original_value: [{ required: true, message: "请输入资产原值", trigger: "change" }],
};
