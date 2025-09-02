<<<<<<< HEAD
// 主逻辑：初始化、全局事件、工具函数
document.addEventListener('DOMContentLoaded', function() {
  // 页面初始化提示
  console.log('管理员后台初始化完成 ✅');

=======
const BASE =`http://10.10.24.99:8080`

// 主逻辑：初始化、全局事件、工具函数
document.addEventListener('DOMContentLoaded', function() {
  // 页面初始化提示
  console.log('管理员后台初始化完成');

  const urlParams = new URLSearchParams(window.location.search);
  const targetId = urlParams.get('page') || 'content-management'; // 修改默认页面
  
  // 显示对应的页面
  showPage(targetId);
  
>>>>>>> origin/wmk
  // 全局事件：ESC键关闭侧边栏（移动设备）
  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' && window.innerWidth < 1024) {
      document.getElementById('sidebar').classList.add('-translate-x-full');
    }
    // Ctrl+F 聚焦搜索框
    if (e.ctrlKey && e.key === 'f') {
      e.preventDefault();
      document.querySelector('input[placeholder="搜索..."]').focus();
    }
  });

  // 暴露工具函数到全局（后续开发可用）
  window.utils = {
    // 日期格式化
    formatDate(dateString) {
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
      }).format(date);
    },
    // 显示通知
    showNotification(message, type = 'info') {
      alert(`[${type.toUpperCase()}] ${message}`); // 简单实现，后续可替换为弹窗组件
    }
  };
<<<<<<< HEAD
});
=======

  // 获取内容搜索框并绑定事件
  const searchInput = document.querySelector('#content-management-page input[placeholder="搜索提问..."]');
  if (searchInput) {
    searchInput.addEventListener('input', function() {
      const keyword = this.value.trim();
      searchPosts(keyword);
    });
  }

  // 获取用户管理页面的搜索框并绑定事件
  const userSearchInput = document.querySelector('#user-management-page input[placeholder="搜索用户..."]');
  if (userSearchInput) {
    userSearchInput.addEventListener('input', function() {
      const keyword = this.value.trim();
      searchUsers(keyword);
    });
}

  loadPosts(0);
  loadUsers(0);

    // 添加导航点击事件
  document.querySelectorAll('.sidebar-item').forEach(item => {
    item.addEventListener('click', function(e) {
      e.preventDefault();
      
      const targetPage = this.getAttribute('data-target');
      const url = new URL(window.location.href);
      url.searchParams.set('page', targetPage);
      window.history.pushState({}, '', url.toString());
      
      // 切换页面并更新高亮
      showPage(targetPage);
    });
  });

});

  function showPage(pageId) {
  // 隐藏所有页面
  document.querySelectorAll('.page').forEach(page => {
    page.classList.add('hidden');
  });
  
  // 移除所有导航项的高亮
  document.querySelectorAll('.sidebar-item').forEach(item => {
    item.classList.remove('bg-primary/20', 'text-primary');
  });

  // 显示指定页面
  const targetPage = document.getElementById(`${pageId}-page`);
  if (targetPage) {
    targetPage.classList.remove('hidden');
  }

  // 设置当前页面导航项高亮
  const activeItem = document.querySelector(`.sidebar-item[data-target="${pageId}"]`);
  if (activeItem) {
    activeItem.classList.add('bg-primary/20', 'text-primary');
  }

}

// 退出登录函数
function logout() {
  if (confirm('确定要退出登录吗？')) {
    // 清除可能存在的用户会话信息
    // localStorage.removeItem('userToken');
    // sessionStorage.clear();
    
    // 跳转到登录页面
    window.location.href = '/login.html'; 
    
  }
}

// 分页相关变量
let currentPage = 0;
const pageSize = 5;
let totalItems = 0;
let totalPages = 0;

// 加载帖子数据函数
function loadPosts(page = 0) {
  const contentList = document.getElementById('content-list');
  const loadingRow = '<tr><td colspan="5" class="text-center py-4">加载中...</td></tr>';
  contentList.innerHTML = loadingRow;

  fetch(`${BASE}/api/admin/posts?page=${page}&size=${pageSize}`)
    .then(response => response.json())
    .then(data => {
      console.log('接口返回数据:', data);

      totalItems = data.totalItems || 0;
      totalPages = Math.ceil(totalItems / pageSize);
      currentPage = page;

    
      const totalElement = document.getElementById('total-questions-count');
      if (totalElement) {
        totalElement.textContent = totalItems.toLocaleString(); 
      }


       // 清空缓存并重新填充当前页数据
      postCache = {};
      data.data.forEach(item => {
        postCache[item.id] = item; // 缓存每个帖子
      });

      if (data.data.length === 0) {
        contentList.innerHTML = '<tr><td colspan="5" class="text-center text-gray-500 py-4">暂无数据</td></tr>';
        updatePaginationInfo();
        return;
      }

      const rows = data.data.map(item => `
        <tr class="border-b border-gray-100 table-row-hover">
           <td class="py-3 px-4">
            <div class="flex items-start gap-3">
              <div class="w-16 h-12 bg-primary/5 rounded flex items-center justify-center text-primary">
                <i class="fa fa-question text-xl"></i>
              </div>
              <div>
                <p class="font-medium">${item.title}</p>
                <p class="text-xs text-gray-dark mt-1">ID: #${item.id}</p>
              </div>
            </div>
          </td>
          <td class="py-3 px-4">
            <div class="flex items-center gap-2">
              <img src="${item.avatar || 'https://picsum.photos/id/1012/40/40'}" alt="头像" class="w-6 h-6 rounded-full">
              <span>${item.username} (${item.grade})</span>
            </div>
          </td>
          <td class="py-3 px-4 text-gray-dark">
            ${window.utils.formatDate(item.createdAt) || "未知时间"}
          </td>
          <td class="py-3 px-4">
            <button class="text-primary hover:text-primary/80 mr-3" onclick="viewPost(${item.id})">查看</button>
            <button class="text-gray-dark hover:text-danger" onclick="deletePost(${item.id})">删除</button>
          </td>
        </tr>
      `);

      contentList.innerHTML = rows.join('');
      updatePaginationInfo(); // 更新分页信息
    })
    .catch(err => {
      console.error('加载失败:', err);
      contentList.innerHTML = '<tr><td colspan="5" class="text-center text-red-500 py-4">加载失败，请稍后重试</td></tr>';
      updatePaginationInfo();
    });
}

// 更新分页控件信息
function updatePaginationInfo() {
  const paginationContainer = document.querySelector('#content-management-page .flex.items-center.justify-between.mt-6');
  if (!paginationContainer) return;

  const startItem = totalItems > 0 ? currentPage * pageSize + 1 : 0;
  const endItem = Math.min((currentPage + 1) * pageSize, totalItems);

  // 更新记录数显示
  const recordInfo = paginationContainer.querySelector('.text-sm.text-gray-dark');
  if (recordInfo) {
    recordInfo.textContent = `显示 ${startItem}-${endItem} 条，共 ${totalItems} 条记录`;
  }

  // 更新分页按钮状态
  const prevButton = paginationContainer.querySelector('button:first-child');
  const nextButton = paginationContainer.querySelector('button:last-child');

  if (prevButton) {
    prevButton.disabled = currentPage === 0;
    prevButton.onclick = () => {
      if (currentPage > 0) loadPosts(currentPage - 1);
    };
  }

  if (nextButton) {
    nextButton.disabled = currentPage >= totalPages - 1;
    nextButton.onclick = () => {
      if (currentPage < totalPages - 1) loadPosts(currentPage + 1);
    };
  }

  // 更新页码按钮（简化处理，只更新第一页按钮）
  const pageButtons = paginationContainer.querySelectorAll('button.w-9.h-9');
  if (pageButtons.length >= 3) {
    pageButtons[1].textContent = currentPage + 1; // 当前页
    pageButtons[1].className = 'w-9 h-9 flex items-center justify-center rounded bg-primary text-white';
    
    if (pageButtons[2]) {
      pageButtons[2].textContent = currentPage + 2 <= totalPages ? currentPage + 2 : '...';
    }
  }
}

// 等待 DOM 加载完成并初始化第一页数据
document.addEventListener('DOMContentLoaded', function () {
  loadPosts(currentPage);
});

function viewPost(postId) {
  const post = postCache[postId];
  if (!post) {
    window.utils.showNotification('未找到该帖子', 'error');
    return;
  }

  // 填充模态框
  document.getElementById('modal-title').textContent = post.title;
  document.getElementById('modal-tag').textContent = post.tag;
  document.getElementById('modal-content').textContent = post.content;

  // 显示弹窗
  document.getElementById('post-modal').classList.remove('hidden');
}

// 删除帖子
function deletePost(postId) {
  if (confirm(`确定要删除ID为 ${postId} 的帖子吗？`)) {
    fetch(`${BASE}/api/admin/posts/${postId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    .then(response => {
      if (response.ok) {
        window.utils.showNotification('帖子删除成功', 'success');
        // 重新加载当前页数据
        loadPosts(currentPage);
      } else {
        throw new Error('删除失败');
      }
    })
    .catch(error => {
      console.error('删除失败:', error);
      window.utils.showNotification('删除失败，请稍后重试', 'error');
    });
  }
}

// 另一种绑定事件的方式（可选）
function bindDeleteEvents() {
  const deleteButtons = document.querySelectorAll('#content-list .text-gray-dark.hover\\:text-danger');
  deleteButtons.forEach(button => {
    button.addEventListener('click', function() {
      const row = this.closest('tr');
      const postId = row.getAttribute('data-post-id');
      deletePost(postId);
    });
  });
}

// 全局变量：缓存帖子数据
let postCache = {};

// 搜索函数：根据关键词过滤帖子
function searchPosts(keyword) {
  const contentList = document.getElementById('content-list');
  const posts = postCache;

  if (!keyword || keyword.trim() === '') {
    // 清空搜索，重新加载原始数据
    loadPosts(currentPage);
    return;
  }

  // 过滤符合条件的帖子
  const filteredPosts = Object.values(posts).filter(post => {
    const lowerKeyword = keyword.toLowerCase();
    return (
      post.title?.toLowerCase().includes(lowerKeyword) ||
      post.content?.toLowerCase().includes(lowerKeyword) ||
      post.username?.toLowerCase().includes(lowerKeyword) ||
      post.college?.toLowerCase().includes(lowerKeyword) ||
      post.grade?.toLowerCase().includes(lowerKeyword)
    );
  });

  // 渲染结果
  if (filteredPosts.length === 0) {
    contentList.innerHTML = '<tr><td colspan="5" class="text-center text-gray-500 py-4">未找到相关提问</td></tr>';
    return;
  }

  const rows = filteredPosts.map(item => `
    <tr class="border-b border-gray-100 table-row-hover">
      <td class="py-3 px-4"><input type="checkbox"></td>
      <td class="py-3 px-4">
        <div class="flex items-start gap-3">
          <div class="w-16 h-12 bg-primary/5 rounded flex items-center justify-center text-primary">
            <i class="fa fa-question text-xl"></i>
          </div>
          <div>
            <p class="font-medium">${item.title}</p>
            <p class="text-xs text-gray-dark mt-1">ID: #${item.id}</p>
          </div>
        </div>
      </td>
      <td class="py-3 px-4">
        <div class="flex items-center gap-2">
          <img src="${item.avatar || 'https://picsum.photos/id/1012/40/40'}" alt="头像" class="w-6 h-6 rounded-full">
          <span>${item.username} (${item.grade})</span>
        </div>
      </td>
      <td class="py-3 px-4 text-gray-dark">
        ${window.utils.formatDate(item.createdAt)}
      </td>
      <td class="py-3 px-4">
        <button class="text-primary hover:text-primary/80 mr-3" onclick="viewPost(${item.id})">查看</button>
        <button class="text-gray-dark hover:text-danger" onclick="deletePost(${item.id})">删除</button>
      </td>
    </tr>
  `);

  contentList.innerHTML = rows.join('');
}


// 分页相关变量（用户）
let userCurrentPage = 0;
const userPageSize = 5;
let userItems = 0;
let userTotalPages = 0;

// 缓存用户数据
let userCache = {};

// 加载用户列表
function loadUsers(page = 0) {
  const userList = document.getElementById('user-list');
  const loadingRow = '<tr><td colspan="6" class="text-center py-4">加载中...</td></tr>';
  userList.innerHTML = loadingRow;

  fetch(`${BASE}/api/admin/users/page?page=${page}&size=${userPageSize}`)
    .then(response => response.json())
    .then(data => {
      console.log('用户接口返回:', data);

      userItems = data.userItems || 0; 
      userTotalPages = Math.ceil(userItems / userPageSize);
      userCurrentPage = page;

      // ✅ 正确更新总用户数
      const totalUsersElement = document.getElementById('total-users-count');
      if (totalUsersElement) {
        totalUsersElement.textContent = userItems.toLocaleString();
      }

      // 清空缓存并重新填充当前页数据
      userCache = {};
      data.data.forEach(item => {
        userCache[item.id] = item;
      });

      if (data.data.length === 0) {
        userList.innerHTML = '<tr><td colspan="6" class="text-center text-gray-500 py-4">暂无用户</td></tr>';
        updateUserPaginationInfo();
        return;
      }

      const rows = data.data.map(item => `
        <tr class="border-b border-gray-100 table-row-hover">
          <td class="py-3 px-4">
            <div class="flex items-center gap-2">
              <img src="${item.avatar || 'https://picsum.photos/id/1012/40/40'}" alt="头像" class="w-8 h-8 rounded-full">
              <div>
                <p class="font-medium">${item.username}</p>
                <p class="text-xs text-gray-dark">#${item.id}</p>
              </div>
            </div>
          </td>
          <td class="py-3 px-4">${item.grade}</td>
          <td class="py-3 px-4">${item.college}</td>
          <td class="py-3 px-4">
            <span class="px-2 py-1 bg-primary/10 text-primary rounded-full text-xs">${item.role}</span>
          </td>
          <td class="py-3 px-4">
            <button class="text-primary hover:text-primary/80 mr-3" onclick="viewUser(${item.id})">查看</button>
            <button class="text-gray-dark hover:text-danger" onclick="deleteUser(${item.id})">删除</button>
          </td>
        </tr>
      `);

      userList.innerHTML = rows.join('');
      updateUserPaginationInfo();
    })
    .catch(err => {
      console.error('加载用户失败:', err);
      userList.innerHTML = '<tr><td colspan="6" class="text-center text-red-500 py-4">加载失败，请稍后重试</td></tr>';
      updateUserPaginationInfo();
    });
}

// 更新用户分页信息
function updateUserPaginationInfo() {
  const paginationContainer = document.querySelector('#user-management-page .flex.items-center.justify-between.mt-6');
  if (!paginationContainer) return;

  const startItem = userItems > 0 ? userCurrentPage * userPageSize + 1 : 0;
  const endItem = Math.min((userCurrentPage + 1) * userPageSize,userItems);

  const recordInfo = paginationContainer.querySelector('.text-sm.text-gray-dark');
  if (recordInfo) {
    recordInfo.textContent = `显示 ${startItem}-${endItem} 条，共 ${userItems} 条记录`;
  }

  const prevButton = paginationContainer.querySelector('button:first-child');
  const nextButton = paginationContainer.querySelector('button:last-child');

  if (prevButton) {
    prevButton.disabled = userCurrentPage === 0;
    prevButton.onclick = () => {
      if (userCurrentPage > 0) loadUsers(userCurrentPage - 1);
    };
  }

  if (nextButton) {
    nextButton.disabled = userCurrentPage >= userTotalPages - 1;
    nextButton.onclick = () => {
      if (userCurrentPage < userTotalPages - 1) loadUsers(userCurrentPage + 1);
    };
  }

  // 更新页码按钮
  const pageButtons = paginationContainer.querySelectorAll('button.w-9.h-9');
  if (pageButtons.length >= 3) {
    pageButtons[1].textContent = userCurrentPage + 1;
    pageButtons[1].className = 'w-9 h-9 flex items-center justify-center rounded bg-primary text-white';
    
    if (pageButtons[2]) {
      pageButtons[2].textContent = userCurrentPage + 2 <= userTotalPages ? userCurrentPage + 2 : '...';
    }
  }
}

function viewUser(userId) {
  const user = userCache[userId];
  if (!user) {
    window.utils.showNotification('未找到该用户', 'error');
    return;
  }
  alert(`查看用户: ${user.username},${user.id},${user.role},${user.college},${user.grade}`);
  // 可扩展为弹窗
}

function deleteUser(userId) {
  if (confirm(`确定要删除用户 ID: ${userId} 吗？`)) {
    fetch(`${BASE}/api/admin/users/${userId}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        window.utils.showNotification('删除成功', 'success');
        loadUsers(userCurrentPage); // 刷新当前页
      } else {
        window.utils.showNotification('删除失败', 'error');
      }
    })
    .catch(err => {
      console.error(err);
      window.utils.showNotification('请求失败', 'error');
    });
}
}

// 搜索用户函数
function searchUsers(keyword) {
  const userList = document.getElementById('user-list');
  
  // 如果搜索关键词为空，重新加载所有用户
  if (!keyword || keyword.trim() === '') {
    loadUsers(userCurrentPage);
    return;
  }
  
  // 从缓存中过滤用户数据
  const filteredUsers = Object.values(userCache).filter(user => {
    const lowerKeyword = keyword.toLowerCase();
    return (
      user.username?.toLowerCase().includes(lowerKeyword) ||
      user.college?.toLowerCase().includes(lowerKeyword) ||
      user.grade?.toLowerCase().includes(lowerKeyword) ||
      user.role?.toLowerCase().includes(lowerKeyword) ||
      String(user.id).includes(keyword)
    );
  });
  
  // 渲染搜索结果
  if (filteredUsers.length === 0) {
    userList.innerHTML = '<tr><td colspan="5" class="text-center text-gray-500 py-4">未找到相关用户</td></tr>';
    return;
  }
  
  const rows = filteredUsers.map(item => `
    <tr class="border-b border-gray-100 table-row-hover">
      <td class="py-3 px-4">
        <div class="flex items-center gap-2">
          <img src="${item.avatar || 'https://picsum.photos/id/1012/40/40'}" alt="头像" class="w-8 h-8 rounded-full">
          <div>
            <p class="font-medium">${item.username}</p>
            <p class="text-xs text-gray-dark">#${item.id}</p>
          </div>
        </div>
      </td>
      <td class="py-3 px-4">${item.grade}</td>
      <td class="py-3 px-4">${item.college}</td>
      <td class="py-3 px-4">
        <span class="px-2 py-1 bg-primary/10 text-primary rounded-full text-xs">${item.role}</span>
      </td>
      <td class="py-3 px-4">
        <button class="text-primary hover:text-primary/80 mr-3" onclick="viewUser(${item.id})">查看</button>
        <button class="text-gray-dark hover:text-danger" onclick="deleteUser(${item.id})">删除</button>
      </td>
    </tr>
  `);
  
  userList.innerHTML = rows.join('');
}
>>>>>>> origin/wmk
