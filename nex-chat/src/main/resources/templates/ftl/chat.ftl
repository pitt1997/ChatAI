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
        .header {
            width: 100%;
            padding: 10px;
            display: flex;
            justify-content: flex-end;
            background: #007bff;
        }
        .header button {
            background: #000;  /* 退出按钮黑色 */
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            transition: background 0.3s ease;
        }
        .header button:hover {
            background: #333; /* 悬停时变成深灰色 */
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

<div class="header">
    <button onclick="logout()">退出登录</button>
</div>

<div class="chat-container">
    <h1>AI 双向流式聊天</h1>
    <div class="chat-box" id="chatBox"></div>
    <div class="input-area">
        <input type="text" id="userInput" placeholder="请输入问题...">
        <select id="modelType">
            <option value="deepseek">DeepSeek</option>
            <option value="chatgpt">ChatGPT</option>
            <option value="local">本地模型</option>
        </select>
        <button onclick="sendMessage()">发送</button>
        <button id="stopButton" onclick="stopStreaming()" disabled>停止回答</button>
    </div>
</div>

<script>
    let ws;
    let isStreaming = false;

    function initWebSocket() {
        ws = new WebSocket("ws://localhost:8080/web/api/ai/chat/websocket");

        ws.onopen = function() {
            console.log("WebSocket 连接成功！");
            document.getElementById("stopButton").disabled = false;
        };

        ws.onmessage = function(event) {
            if (!isStreaming) return;
            appendBotMessage(event.data);
        };

        ws.onerror = function(error) {
            console.error("WebSocket 错误: ", error);
        };

        ws.onclose = function() {
            console.log("WebSocket 连接关闭");
            document.getElementById("stopButton").disabled = true;
        };
    }

    function sendMessage() {
        let input = document.getElementById("userInput");
        let message = input.value.trim();
        if (message === "") return;

        let modelType = document.getElementById("modelType").value;
        let token = localStorage.getItem("token");

        appendUserMessage(message);

        let payload = {
            modelType: modelType,
            message: message,
            token: token
        };

        if (!ws || ws.readyState === WebSocket.CLOSED) {
            initWebSocket();
            setTimeout(() => ws.send(JSON.stringify(payload)), 500);
        } else {
            ws.send(JSON.stringify(payload));
        }

        isStreaming = true;
        input.value = "";
        document.getElementById("stopButton").disabled = false;
    }

    function stopStreaming() {
        isStreaming = false;
        if (ws) {
            ws.close();
        }
        document.getElementById("stopButton").disabled = true;
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

    function logout() {
        fetch("http://localhost:8080/web/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    // alert("登出成功！");
                    localStorage.removeItem("token");
                    window.location.href = "/web/auth/login"; // 重定向
                } else {
                    alert("登出失败: " + data.message);
                }
            })
            .catch(error => console.error("登出错误:", error));
    }

    initWebSocket();
</script>

</body>
</html>
