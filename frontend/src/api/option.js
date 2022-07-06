import request from "@/tools/request";

export function departmentOptionList(params) {
  return request({ url: `/departments/options/`, method: "get", params });
}

export function personOptionList(params) {
  return request({ url: `/persons/options/`, method: "get", params });
}

export function categoryOptionList(params) {
  return request({ url: `/categories/options/`, method: "get", params });
}

export function locationOptionList(params) {
  return request({ url: `/locations/options/`, method: "get", params });
}
