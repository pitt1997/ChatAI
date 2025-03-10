<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ChatGPT 风格 - AI 聊天</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #343541;
            color: white;
            font-family: Arial, sans-serif;
        }
        .chat-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #40414f;
            border-radius: 10px;
        }
        .chat-box {
            height: 400px;
            overflow-y: auto;
            padding: 10px;
            border-radius: 10px;
            background: #343541;
        }
        .message {
            padding: 10px;
            margin: 5px 0;
            border-radius: 5px;
        }
        .user-message {
            background: #0d6efd;
            color: white;
            text-align: right;
        }
        .bot-message {
            background: #4c4f5a;
        }
        .input-group {
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="chat-container">
        <h3 class="text-center">ChatGPT 风格 - AI 聊天</h3>
        <div class="chat-box" id="chatBox">
            <div class="message bot-message">你好，我是 AI 助手，请问有什么可以帮助你的？</div>
        </div>
        <div class="input-group">
            <input type="text" id="userInput" class="form-control" placeholder="输入您的问题..." />
            <button class="btn btn-primary" onclick="sendMessage()">发送</button>
        </div>
    </div>
</div>

<script>
    function sendMessage() {
        let input = document.getElementById("userInput");
        let message = input.value.trim();
        if (message === "") return;

        // 显示用户消息
        let chatBox = document.getElementById("chatBox");
        let userMessage = document.createElement("div");
        userMessage.className = "message user-message";
        userMessage.innerText = message;
        chatBox.appendChild(userMessage);

        // 清空输入框
        input.value = "";

        // 调用后端 API
        fetch("/api/ai/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer your-token"
            },
            body: JSON.stringify({ prompt: message })
        })
            .then(response => response.json())
            .then(data => {
                let botMessage = document.createElement("div");
                botMessage.className = "message bot-message";
                botMessage.innerText = data.response; // 解析后端返回的内容
                chatBox.appendChild(botMessage);
                chatBox.scrollTop = chatBox.scrollHeight; // 滚动到底部
            })
            .catch(error => console.error("请求失败:", error));
    }
</script>
</body>
</html>
