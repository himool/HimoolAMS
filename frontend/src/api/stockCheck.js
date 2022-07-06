import request from "@/tools/request";

export function stockCheckRecordList(params) {
  return request({ url: `/stock_check_records/`, method: "get", params });
}
