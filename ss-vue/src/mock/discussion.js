// src/mock/discussion.js

// 用函数生成 200 条帖子
export function generateMockPosts() {
  const arr = [];
  for (let i = 1; i <= 200; i++) {
    arr.push({
      id: i,
      title: `第 ${i} 条帖子标题`,
      content: `这是第 ${i} 条帖子内容，模拟一些讨论文本。可以在这里放问题、回答或者随便写点东西用于演示效果。`,
      author: `用户${(i % 20) + 1}`,
      time: `2025-08-${(i % 30) + 1} 12:${(i % 60)
        .toString()
        .padStart(2, "0")}`,
    });
  }
  return arr;
}

// 默认导出一份静态数据
const posts = generateMockPosts();
export default posts;
