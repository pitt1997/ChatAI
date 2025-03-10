<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>AI 聊天</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .chat-container {
            width: 500px;
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .input-group {
            margin: 15px 0;
        }
        input, select, button {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        button {
            background: #007bff;
            color: white;
            cursor: pointer;
        }
        button:hover {
            background: #0056b3;
        }
        .response {
            margin-top: 20px;
            background: #eef;
            padding: 10px;
            border-radius: 5px;
            min-height: 50px;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <h1>大模型 AI 聊天</h1>
    <div class="input-group">
        <label>输入你的问题：</label>
        <input type="text" id="message" placeholder="请输入问题">
    </div>
    <div class="input-group">
        <label>选择模型：</label>
        <select id="modelType">
            <option value="chatgpt">ChatGPT</option>
            <option value="deepseek">DeepSeek</option>
            <option value="local">本地模型</option>
        </select>
    </div>
    <button onclick="sendMessage()">发送</button>
    <div class="response" id="response">AI 回复将在这里显示...</div>
</div>

<script>
    async function sendMessage() {
        const message = document.getElementById('message').value;
        const modelType = document.getElementById('modelType').value;
        // 你的 JWT 令牌
        const token = getCookie('JSESSIONID'); // 这里应从登录系统获取
        console.log('JWT Token:', token); // 打印 token 值
        const response = await fetch('/web/api/ai/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer B6FC7391D7856A16391F9860DA5DA3B8}`
            },
            body: JSON.stringify({ message, modelType })
        });
        const text = await response.text();
        document.getElementById('response').innerText = text;
    }

    function getCookie(name) {
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [cookieName, cookieValue] = cookie.trim().split('=');
            if (cookieName === name) {
                return decodeURIComponent(cookieValue);
            }
        }
        return null;
    }
</script>
</body>
</html>
