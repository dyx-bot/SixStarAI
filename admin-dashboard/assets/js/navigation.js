// 侧边栏切换、页面切换逻辑
document.addEventListener('DOMContentLoaded', function() {
  // 1. 侧边栏导航切换
  const sidebarItems = document.querySelectorAll('.sidebar-item');
  const pages = document.querySelectorAll('.page');

  sidebarItems.forEach(item => {
    item.addEventListener('click', function(e) {
      e.preventDefault();
      // 移除所有活跃状态
      sidebarItems.forEach(i => i.classList.remove('active'));
      pages.forEach(page => page.classList.add('hidden'));
      // 添加当前活跃状态
      this.classList.add('active');
      const target = this.getAttribute('data-target');
      document.getElementById(`${target}-page`).classList.remove('hidden');
      // 移动设备点击后关闭侧边栏
      if (window.innerWidth < 1024) {
        document.getElementById('sidebar').classList.add('-translate-x-full');
      }
    });
  });

  // 2. 侧边栏展开/关闭按钮
  document.getElementById('toggle-sidebar').addEventListener('click', function() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('-translate-x-full');
  });

  document.getElementById('close-sidebar').addEventListener('click', function() {
    document.getElementById('sidebar').classList.add('-translate-x-full');
  });
});