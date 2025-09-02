// 图表初始化（用户分布改为大一大二大三大四教师）
document.addEventListener('DOMContentLoaded', function() {
  initCharts();
});

function initCharts() {
  // 1. 软件使用趋势图表
  const usageTrendCtx = document.getElementById('usage-trend-chart');
  if (usageTrendCtx) {
    new Chart(usageTrendCtx, {
      type: 'line',
      data: {
        labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        datasets: [
          {
            label: '活跃用户',
            data: [5400, 5800, 6200, 5900, 6500, 4200, 3800],
            borderColor: '#165DFF',
            backgroundColor: 'rgba(22, 93, 255, 0.1)',
            tension: 0.4,
            fill: true
          },
          {
            label: '提问数量',
            data: [1250, 1320, 1480, 1390, 1520, 980, 870],
            borderColor: '#36CFC9',
            backgroundColor: 'rgba(54, 207, 201, 0.1)',
            tension: 0.4,
            fill: true
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'top' } },
        scales: { y: { beginAtZero: true } }
      }
    });
  }

  // 2. 用户分布图表（改为：大一、大二、大三、大四、教师）
  const userDistributionCtx = document.getElementById('user-distribution-chart');
  if (userDistributionCtx) {
    new Chart(userDistributionCtx, {
      type: 'doughnut',
      data: {
        labels: ['大一', '大二', '大三', '大四', '教师'],
        datasets: [{
          data: [30, 25, 20, 15, 10], // 各角色占比
          backgroundColor: ['#165DFF', '#36CFC9', '#FAAD14', '#722ED1', '#F5222D'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'bottom' } },
        cutout: '70%'
      }
    });
  }
}