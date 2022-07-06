import request from "@/utils/request.js";

// Location
export function locationOption(data) {
  return request({ url: `/locations/options/`, method: 'get', data })
}
