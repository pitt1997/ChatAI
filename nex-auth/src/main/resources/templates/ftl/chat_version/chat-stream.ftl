<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>AI 流式聊天</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            height: 100vh;
        }
        .chat-container {
            width: 600px;
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            height: 80vh;
        }
        .chat-box {
            flex-grow: 1;
            overflow-y: auto;
            border: 1px solid #ccc;
            padding: 10px;
            border-radius: 5px;
            background: #fafafa;
        }
        .input-area {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        input, select, button {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input {
            flex-grow: 1;
        }
        button {
            background: #007bff;
            color: white;
            cursor: pointer;
        }
        button:hover {
            background: #0056b3;
        }
        .message {
            margin: 5px 0;
            padding: 10px;
            border-radius: 5px;
            max-width: 80%;
        }
        .user-message {
            background: #007bff;
            color: white;
            align-self: flex-end;
        }
        .bot-message {
            background: #eef;
            color: black;
            align-self: flex-start;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <h1>AI 大模型聊天</h1>
    <div class="chat-box" id="chatBox"></div>
    <div class="input-area">
        <input type="text" id="userInput" placeholder="请输入问题...">
        <select id="modelType">
            <option value="chatgpt">ChatGPT</option>
            <option value="deepseek">DeepSeek</option>
            <option value="local">本地模型</option>
        </select>
        <button onclick="sendMessage()">发送</button>
    </div>
</div>

<script>
    function sendMessage() {
        let input = document.getElementById("userInput");
        let message = input.value.trim();
        if (message === "") return;

        let modelType = document.getElementById("modelType").value;
        let chatBox = document.getElementById("chatBox");

        // 显示用户消息
        let userMessage = document.createElement("div");
        userMessage.className = "message user-message";
        userMessage.innerText = message;
        chatBox.appendChild(userMessage);

        // 显示 AI "正在输入..."
        let botMessage = document.createElement("div");
        botMessage.className = "message bot-message";
        botMessage.innerText = "AI 正在输入...";
        chatBox.appendChild(botMessage);
        input.value = "";

        var encodedMessage = encodeURIComponent(message);
        console.log("URL Encoded message:", encodedMessage);
        // 通过 SSE 方式获取流式返回的 AI 结果
        let eventSource = new EventSource("/web/api/ai/chat/stream?modelType=${modelType}&prompt=" + encodedMessage);
        eventSource.onmessage = function(event) {
            botMessage.innerText += event.data; // 逐步添加 AI 返回的文本
        };
        eventSource.onerror = function() {
            eventSource.close();
        };
    }


</script>
</body>
</html>
