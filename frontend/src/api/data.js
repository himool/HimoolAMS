import request from "@/tools/request";

// Asset
export function assetList(params) {
  return request({ url: `/assets/`, method: "get", params }, false);
}

export function assetNumber(params) {
  return request({ url: `/assets/number/`, method: "get", params }, false);
}

export function assetCreate(data) {
  return request({ url: `/assets/`, method: "post", data });
}

export function assetUpdate(data) {
  return request({ url: `/assets/${data.id}/`, method: "put", data });
}

export function assetDestroy(data) {
  return request({ url: `/assets/${data.id}/`, method: "delete", data });
}

// Category
export function categoryList(params) {
  return request({ url: `/categories/`, method: "get", params }, false);
}

export function categoryCreate(data) {
  return request({ url: `/categories/`, method: "post", data });
}

export function categoryUpdate(data) {
  return request({ url: `/categories/${data.id}/`, method: "put", data });
}

export function categoryDestroy(data) {
  return request({ url: `/categories/${data.id}/`, method: "delete", data });
}

// Location
export function locationList(params) {
  return request({ url: `/locations/`, method: "get", params }, false);
}

export function locationCreate(data) {
  return request({ url: `/locations/`, method: "post", data });
}

export function locationUpdate(data) {
  return request({ url: `/locations/${data.id}/`, method: "put", data });
}

export function locationDestroy(data) {
  return request({ url: `/locations/${data.id}/`, method: "delete", data });
}

// Department
export function departmentList(params) {
  return request({ url: `/departments/`, method: "get", params }, false);
}

export function departmentCreate(data) {
  return request({ url: `/departments/`, method: "post", data });
}

export function departmentUpdate(data) {
  return request({ url: `/departments/${data.id}/`, method: "put", data });
}

export function departmentDestroy(data) {
  return request({ url: `/departments/${data.id}/`, method: "delete", data });
}

// Person
export function personList(params) {
  return request({ url: `/persons/`, method: "get", params });
}

export function personCreate(data) {
  return request({ url: `/persons/`, method: "post", data });
}

export function personUpdate(data) {
  return request({ url: `/persons/${data.id}/`, method: "put", data });
}

export function personDestroy(data) {
  return request({ url: `/persons/${data.id}/`, method: "delete", data });
}
