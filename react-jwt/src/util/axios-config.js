import axios from 'axios';

const AxiosInstance = axios.create({
  responseType: 'json',
  xsrfHeaderName: 'csrf',
  headers: {
    'accept': 'application/json',
    'content-type': 'application/json'
  }
});

AxiosInstance.interceptors.response.use(response => {
  localStorage.setItem('csrf', response.headers['csrf']);
  return response;
});

AxiosInstance.interceptors.request.use(request => {
  const headers = request.headers;
  const csrfToken = localStorage.getItem('csrf');
  request.headers = Object.assign({},{...headers, "csrf": csrfToken});
  return request;
});

export { AxiosInstance as http };
