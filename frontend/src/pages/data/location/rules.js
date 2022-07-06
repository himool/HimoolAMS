export default {
  name: [
    { required: true, message: "请输入资产名称", trigger: "change" },
    { max: 64, message: "超出最大长度 (64)", trigger: "change" },
  ],
  manager: [{ required: true, message: "请选择负责人", trigger: "change" }],
};
