import request from "@/tools/request";

export function assetSummaryExport(params) {
  return request({
    url: "/assets/summary_export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function categorySummary(params) {
  return request({ url: `/category_summaries/`, method: "get", params }, false);
}

export function categorySummaryExport(params) {
  return request({
    url: "/category_summaries/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function locationSummary(params) {
  return request({ url: `/location_summaries/`, method: "get", params }, false);
}

export function locationSummaryExport(params) {
  return request({
    url: "/location_summaries/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function departmentSummary(params) {
  return request({ url: `/department_summaries/`, method: "get", params }, false);
}

export function departmentSummaryExport(params) {
  return request({
    url: "/department_summaries/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function homeSummary(params) {
  return request({ url: `/home_summaries/`, method: "get", params }, false);
}

export function personSummary(params) {
  return request({ url: `/person_summaries/`, method: "get", params }, false);
}

export function personSummaryExport(params) {
  return request({
    url: "/person_summaries/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}

export function statusSummary(params) {
  return request({ url: `/status_summaries/`, method: "get", params }, false);
}

export function statusSummaryExport(params) {
  return request({
    url: "/status_summaries/export/",
    responseType: "blob",
    method: "get",
    params,
  });
}
