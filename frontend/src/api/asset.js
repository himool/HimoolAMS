import request from "@/tools/request";

export function assetCollect(data) {
  return request({ url: `/assets/${data.id}/collect/`, method: "post", data }, false);
}

export function assetReturnStock(data) {
  return request({ url: `/assets/${data.id}/return_stock/`, method: "post", data }, false);
}

export function assetBorrow(data) {
  return request({ url: `/assets/${data.id}/borrow/`, method: "post", data }, false);
}

export function assetGiveBack(data) {
  return request({ url: `/assets/${data.id}/give_back/`, method: "post", data }, false);
}

export function assetTransfer(data) {
  return request({ url: `/assets/${data.id}/transfer/`, method: "post", data }, false);
}

export function assetNeedRepair(data) {
  return request({ url: `/assets/${data.id}/need_repair/`, method: "post", data }, false);
}

export function assetFixed(data) {
  return request({ url: `/assets/${data.id}/fixed/`, method: "post", data }, false);
}

export function assetUnderRepair(data) {
  return request({ url: `/assets/${data.id}/under_repair/`, method: "post", data }, false);
}

export function assetScrapped(data) {
  return request({ url: `/assets/${data.id}/scrapped/`, method: "post", data }, false);
}

export function assetTemplate(params) {
  return request({
    url: "/assets/import_template/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function assetImport(data) {
  return request({
    url: "/assets/import_data/",
    method: "post",
    data,
  });
}

export function assetExport(params) {
  return request({
    url: "/assets/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

// AssetTransfer
export function assetTransferImport(data) {
  return request({
    url: "/asset_transfers/import_data/",
    method: "post",
    data,
  });
}

export function assetTransferExport(params) {
  return request({
    url: "/asset_transfers/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

// AssetFlow
export function assetFlowList(params) {
  return request({ url: `/asset_flows/`, method: "get", params });
}

export function assetFlowExport(params) {
  return request({
    url: "/asset_flows/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}
