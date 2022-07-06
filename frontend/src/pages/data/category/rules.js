export default {
  number: [
    { required: true, message: '请输入分类编号', trigger: 'change' },
    { max: 32, message: '超出最大长度 (32)', trigger: 'change' },
  ],
  name: [
    { required: true, message: '请输入分类名称', trigger: 'change' },
    { max: 64, message: '超出最大长度 (64)', trigger: 'change' },
  ],
}