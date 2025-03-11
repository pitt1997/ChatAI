<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>AI 流式聊天（WebSocket 双向通信）</title>
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
    <h1>AI 双向流式聊天</h1>
    <div class="chat-box" id="chatBox"></div>
    <div class="input-area">
        <input type="text" id="userInput" placeholder="请输入问题...">
        <select id="modelType">
            <option value="chatgpt">ChatGPT</option>
            <option value="deepseek">DeepSeek</option>
            <option value="local">本地模型</option>
        </select>
        <button onclick="sendMessage()">发送</button>
        <button id="stopButton" onclick="stopStreaming()" disabled>停止回答</button>
    </div>
</div>

<script>
    let ws;
    let isStreaming = false; // 是否正在接收 AI 回复

    function initWebSocket() {
        ws = new WebSocket("ws://localhost:8080/web/api/ai/chat/websocket");

        ws.onopen = function() {
            console.log("WebSocket 连接成功！");
            document.getElementById("stopButton").disabled = false; // 允许停止
        };

        ws.onmessage = function(event) {
            if (!isStreaming) return; // 如果停止了，就不处理新消息
            appendBotMessage(event.data);
        };

        ws.onerror = function(error) {
            console.error("WebSocket 错误: ", error);
        };

        ws.onclose = function() {
            console.log("WebSocket 连接关闭");
            document.getElementById("stopButton").disabled = true; // 禁用停止按钮
        };
    }

    function sendMessage() {
        let input = document.getElementById("userInput");
        let message = input.value.trim();
        if (message === "") return;

        let modelType = document.getElementById("modelType").value;

        appendUserMessage(message);

        // 如果 WebSocket 关闭了，重新连接
        if (!ws || ws.readyState === WebSocket.CLOSED) {
            initWebSocket();
            setTimeout(() => ws.send(JSON.stringify({ modelType: modelType, message: message })), 500);
        } else {
            ws.send(JSON.stringify({ modelType: modelType, message: message }));
        }

        isStreaming = true; // 允许接收 AI 回复
        input.value = "";
        document.getElementById("stopButton").disabled = false; // 启用“停止”按钮
    }

    function stopStreaming() {
        isStreaming = false; // 停止接收数据
        if (ws) {
            ws.close(); // 关闭 WebSocket 连接
        }
        document.getElementById("stopButton").disabled = true; // 禁用“停止”按钮
    }

    function appendUserMessage(text) {
        let chatBox = document.getElementById("chatBox");
        let userMessage = document.createElement("div");
        userMessage.className = "message user-message";
        userMessage.innerText = text;
        chatBox.appendChild(userMessage);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function appendBotMessage(text) {
        let chatBox = document.getElementById("chatBox");

        let lastMessage = chatBox.lastElementChild;
        if (lastMessage && lastMessage.classList.contains("bot-message")) {
            lastMessage.innerText += text;
        } else {
            let botMessage = document.createElement("div");
            botMessage.className = "message bot-message";
            botMessage.innerText = text;
            chatBox.appendChild(botMessage);
        }

        chatBox.scrollTop = chatBox.scrollHeight;
    }

    // 初始化 WebSocket 连接
    initWebSocket();
</script>

</body>
</html>
