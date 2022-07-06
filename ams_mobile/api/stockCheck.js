import request from "@/utils/request.js";

// 盘点
export function stockCheckValidate(data) {
  return request({ url: `/locations/${data.id}/validate/`, method: 'post', data });
}

export function stockCheckConfirm(data) {
  return request({ url: `/locations/${data.id}/confirm/`, method: 'post', data });
}
