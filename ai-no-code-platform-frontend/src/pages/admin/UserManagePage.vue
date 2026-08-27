<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import type { TablePaginationConfig } from 'ant-design-vue'
import { DeleteOutlined, SearchOutlined } from '@ant-design/icons-vue'
import { deleteUser, listUserVoByPage } from '@/api/userController.ts'

const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '账号', dataIndex: 'userAccount', width: 150 },
  { title: '用户名', dataIndex: 'userName', width: 150 },
  { title: '头像', dataIndex: 'userAvatar', width: 90 },
  { title: '简介', dataIndex: 'userProfile', width: 220 },
  { title: '用户角色', dataIndex: 'userRole', width: 110 },
  { title: '创建时间', dataIndex: 'createTime', width: 180 },
  { title: '操作', key: 'action', width: 90, fixed: 'right' as const },
]

const data = ref<API.UserVO[]>([])
const total = ref(0)
const loading = ref(false)

const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  userAccount: '',
  userName: '',
})

const pagination = computed(() => ({
  current: searchParams.pageNum,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  showTotal: (count: number) => `共 ${count} 条`,
}))

/** 获取用户分页数据。 */
async function fetchData() {
  loading.value = true
  try {
    const res = await listUserVoByPage({ ...searchParams })
    if (res.data.code === 0 && res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('获取数据失败，' + res.data.message)
    }
  } catch {
    message.error('获取用户数据失败')
  } finally {
    loading.value = false
  }
}

/** 根据当前条件从第一页搜索。 */
function doSearch() {
  searchParams.pageNum = 1
  fetchData()
}

/** 切换分页后重新获取数据。 */
function doTableChange(page: TablePaginationConfig) {
  searchParams.pageNum = page.current ?? 1
  searchParams.pageSize = page.pageSize ?? 10
  fetchData()
}

/** 删除指定用户并刷新列表。 */
async function doDelete(id?: string) {
  if (!id) {
    return
  }

  try {
    const res = await deleteUser({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error('删除失败，' + res.data.message)
    }
  } catch {
    message.error('删除请求失败')
  }
}

/** 将后端时间转换为本地显示格式。 */
function formatTime(value?: string) {
  if (!value) {
    return '-'
  }
  return new Date(value).toLocaleString('zh-CN', { hour12: false })
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <section id="userManagePage" class="user-manage-page">
    <div class="page-heading">
      <div>
        <h1>用户管理</h1>
      </div>
      <span class="record-count">{{ total }} 位用户</span>
    </div>

    <a-form class="search-form" layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" allow-clear placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" allow-clear placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">
          <template #icon><SearchOutlined /></template>
          搜索
        </a-button>
      </a-form-item>
    </a-form>

    <div class="table-wrap">
      <a-table
        row-key="id"
        :columns="columns"
        :data-source="data"
        :loading="loading"
        :pagination="pagination"
        :scroll="{ x: 1120 }"
        @change="doTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userAvatar'">
            <a-avatar :src="record.userAvatar" :size="38">
              {{ record.userName?.slice(0, 1) }}
            </a-avatar>
          </template>
          <template v-else-if="column.dataIndex === 'userProfile'">
            <span class="profile-text">{{ record.userProfile || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'userRole'">
            <a-tag :color="record.userRole === 'admin' ? 'green' : 'blue'">
              {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'createTime'">
            {{ formatTime(record.createTime) }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-popconfirm
              title="确定删除这个用户吗？"
              ok-text="删除"
              cancel-text="取消"
              @confirm="doDelete(record.id)"
            >
              <a-button type="text" danger aria-label="删除用户">
                <template #icon><DeleteOutlined /></template>
              </a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </div>
  </section>
</template>

<style scoped>
.user-manage-page {
  width: min(100%, 1200px);
  margin: 0 auto;
  overflow: hidden;
  background: var(--ios-surface-solid);
  border: 1px solid var(--ios-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

.page-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28px 30px 22px;
  border-bottom: 1px solid var(--ios-line);
}

.page-heading h1 {
  margin: 0 0 4px;
  color: var(--ios-text);
  font-size: 24px;
  font-weight: 650;
}

.page-heading p {
  margin: 0;
  color: var(--ios-secondary);
  font-size: 13px;
}

.record-count {
  padding: 7px 11px;
  color: var(--ios-secondary);
  font-size: 13px;
  border: 1px solid var(--ios-line);
  border-radius: 999px;
  background: var(--bg-secondary);
}

.search-form {
  gap: 4px 0;
  padding: 20px 30px;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--ios-line);
}

.table-wrap {
  padding: 12px 18px 18px;
}

.profile-text {
  display: block;
  overflow: hidden;
  color: var(--ios-secondary);
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.ant-input),
:deep(.ant-btn),
:deep(.ant-select-selector) {
  border-radius: 10px;
}

:deep(.ant-btn-primary) {
  height: auto;
  padding: 14px 28px;
  border: 0;
  font-size: 17px;
  font-weight: 500;
  background: var(--ios-blue);
  border-radius: 980px;
  box-shadow: none;
}

:deep(.ant-btn-primary:hover) { background: var(--ios-blue-dark); }

:deep(.ant-form-item-label > label) {
  color: var(--ios-secondary);
  font-size: 13px;
}

:deep(.ant-input) {
  border-color: var(--ios-line);
  background: var(--bg-glass-strong);
}

:deep(.ant-input:focus),
:deep(.ant-input-focused) {
  border-color: var(--ios-blue);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.12);
}

:deep(.ant-table-wrapper .ant-table-thead > tr > th) {
  color: var(--ios-secondary);
  font-size: 12px;
  font-weight: 600;
  background: var(--bg-elevated);
  border-bottom-color: var(--ios-line);
}

:deep(.ant-table),
:deep(.ant-table-container) {
  background: var(--bg-elevated);
}

:deep(.ant-table-wrapper .ant-table-tbody > tr > td) {
  color: var(--ios-text);
  background: var(--bg-elevated);
  border-bottom-color: rgba(60, 60, 67, 0.1);
}

:deep(.ant-table-wrapper .ant-table-tbody > tr:hover > td) {
  background: rgba(0, 122, 255, 0.035);
}

:deep(.ant-pagination-item),
:deep(.ant-pagination-prev .ant-pagination-item-link),
:deep(.ant-pagination-next .ant-pagination-item-link) {
  border-radius: 8px;
}

@media (max-width: 768px) {
  .page-heading {
    align-items: flex-start;
    padding: 22px 18px 16px;
  }

  .search-form {
    display: grid;
    padding: 18px;
  }

  .search-form :deep(.ant-form-item) {
    margin-right: 0;
  }

  .search-form :deep(.ant-input) {
    width: 100%;
  }

  .table-wrap {
    padding: 4px 8px 12px;
  }

  .record-count { margin-top: 2px; }
}
</style>
