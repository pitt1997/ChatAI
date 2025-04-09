<#assign content>
    <div class="mb-8 text-center">
        <!-- 更换为简洁用户头像图标 -->
        <svg class="w-16 h-16 mx-auto mb-4 text-purple-600 dark:text-purple-400" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M4 20C4 16.6863 7.58172 14 12 14C16.4183 14 20 16.6863 20 20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <!-- 修改文案 -->
        <p class="mt-2 text-sm text-gray-600 dark:text-gray-400">ChatAI 统一身份认证</p>
    </div>

    <form class="form-signin" action="${request.contextPath}/login" method="post">
        <input type="hidden" name="client_id" value="chatai">
        <input type="hidden" name="grant_type" value="password">
        <div class="space-y-6">
            <div class="">
                <input class="w-full text-sm px-4 py-3 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:outline-none focus:border-purple-400 dark:focus:border-purple-500 dark:text-gray-300 transition-colors"
                       type="text" placeholder="账号" name="username" required>
            </div>

            <div class="relative">
                <input placeholder="密码" type="password" name="password" required
                       class="w-full text-sm px-4 py-3 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:outline-none focus:border-purple-400 dark:focus:border-purple-500 dark:text-gray-300 transition-colors">
            </div>

            <#if error??>
                <div class="relative text-center">
                    <span class="text-red-600 dark:text-red-400 text-sm font-medium">${error}</span>
                </div>
            </#if>

            <button type="submit"
                    class="w-full flex justify-center bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 dark:bg-blue-700 dark:hover:bg-blue-600 text-white p-3 rounded-lg font-semibold cursor-pointer transition-all duration-300 transform hover:scale-105">
                立即登录
            </button>
        </div>

    </form>
</#assign>

<#include "layout/base.ftl">
