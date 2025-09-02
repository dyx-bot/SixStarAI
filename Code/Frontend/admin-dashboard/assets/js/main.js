// 主逻辑：初始化、全局事件、工具函数
document.addEventListener('DOMContentLoaded', function() {
  // 页面初始化提示
  console.log('管理员后台初始化完成 ✅');

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
});