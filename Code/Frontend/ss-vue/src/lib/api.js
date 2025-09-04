// 后端接口基础地址（必须写后端真实地址，如 http://localhost:3000，不能是相对路径）
export const BASE =  'http://10.10.24.152:8080'; 

// 通用请求函数（封装fetch，添加必要的请求头和错误处理）
const request = async (url, options = {}) => {
  try {
    // 拼接完整URL（确保请求发往后端，而非前端Vite服务器）
    const fullUrl = `${BASE}${url}`;

    // 设置默认请求头（关键：告诉后端数据格式是JSON）
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers, // 允许覆盖默认头
    };

    // 发送请求（自动处理POST参数的JSON序列化）
    const response = await fetch(fullUrl, {
      ...options,
      headers,
      credentials: 'include', // 允许跨域携带cookie（如果后端需要）
      // 核心修改：在request函数内统一处理body序列化，无需在调用时手动写JSON.stringify
      body: options.body ? JSON.stringify(options.body) : null,
    });

    // 处理HTTP错误状态（4xx/5xx）
    if (!response.ok) {
      const errorText= await response.text(); // 尝试获取后端返回的错误信息
      throw new Error(`后端接口错误：${response.status}-${errorText}（可能地址错或后端没启动）`);
    }

    // 解析响应数据（假设后端返回JSON）
    const data = await response.json();
    return data;
  } catch (err) {
    // 统一捕获错误（网络错误、地址错误等）
    throw new Error(`请求失败：${err.message}（检查后端是否启动、地址是否正确）`);
  }
};

// 登录接口（POST请求，传递账号密码）
export const loginApi = async (username, password) => {
  // 调用通用请求函数，明确用POST方法
  return request('/api/login', { // 后端登录接口路径（根据后端实际接口调整）
    method: 'POST',
    // 核心修改：直接传对象，由request函数自动转为JSON字符串（去掉手动JSON.stringify）
    body: { 
      id: username, // 确保参数名与后端一致
      password: password 
    },
  });
};
// 上传头像接口
export const uploadAvatarApi = async (userId, file) => {
  const formData = new FormData();
  formData.append('avatar', file);

  const fullUrl = `${BASE}/api/update/avatar/${userId}`;

  try {
    const response = await fetch(fullUrl, {
      method: 'PUT',
      body: formData,
      // 注意：上传文件时不要设置 Content-Type，让浏览器自动设置 multipart/form-data
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`头像上传失败：${response.status}-${errorText}`);
    }

    const data = await response.json();
    return data;
  } catch (err) {
    throw new Error(`请求失败：${err.message}`);
  }
};