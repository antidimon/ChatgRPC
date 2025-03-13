const chatsList = document.getElementById('chats');

function updateChatsList(chats) {

    chats.forEach((chat) => {
        const chatItem = document.createElement('LI');
        chatItem.textContent = chat.name;
        chatItem.addEventListener('click', () => {
            updateChatWindow(chat);
        });
        chatsList.appendChild(chatItem);
    });
}

async function updateChatWindow(chat) {
    const chatWindow = document.querySelector('.chat-window');
    chatWindow.innerHTML = '';

    const chatHeader = document.createElement('div');
    chatHeader.classList.add('chat-header');
    const chatTypeIcon = document.createElement('img');
    chatTypeIcon.src = '/images/chat.png';
    chatTypeIcon.classList.add('chat-type-icon');
    const chatNameSpan = document.createElement('span');
    chatNameSpan.id = 'chat-name';
    chatNameSpan.textContent = chat.name;
    const chatMenu = document.createElement('div');
    chatMenu.classList.add('chat-menu');
    chatMenu.style.display = 'none';
    chatHeader.appendChild(chatTypeIcon);
    chatHeader.appendChild(chatNameSpan);
    chatHeader.appendChild(chatMenu);
    chatWindow.appendChild(chatHeader);

    const chatMessagesContainer = document.createElement('div');
    chatMessagesContainer.classList.add('chat-messages-container');
    chatWindow.appendChild(chatMessagesContainer);

    const chatInput = document.createElement('div');
    chatInput.classList.add('chat-input');
    const sendMessageForm = document.createElement('form');
    sendMessageForm.id = 'send-message-form';
    const newMessageInput = document.createElement('input');
    newMessageInput.type = 'text';
    newMessageInput.id = 'new-message';
    newMessageInput.placeholder = 'Введите сообщение...';
    const sendButton = document.createElement('button');
    sendButton.id = 'send-button';
    sendButton.textContent = 'Отправить';
    sendMessageForm.appendChild(newMessageInput);
    sendMessageForm.appendChild(sendButton);
    chatInput.appendChild(sendMessageForm);
    chatWindow.appendChild(chatInput);

    chatHeader.style.display = 'flex';
    chatMessagesContainer.style.display = 'block';
    chatInput.style.display = 'flex';

    try {
        const id = chat.chatId;
        const messagesApiUrl = `/api/chats/${id}/messages`;

        const response = await fetch(messagesApiUrl, { credentials: 'include' });
        if (!response.ok) {
            throw new Error(`Ошибка HTTP! status: ${response.status}`);
        }
        const messages = await response.json();

        messages.forEach((message) => {
            const messageElement = document.createElement('DIV');
            messageElement.classList.add('chat-message', message.messageType.toLowerCase());

            const senderElement = document.createElement('DIV');
            senderElement.classList.add('sender-username');
            senderElement.textContent = message.senderUsername;
            messageElement.appendChild(senderElement);

            const textElement = document.createElement('DIV');
            textElement.classList.add('message-text');
            textElement.textContent = message.message;
            messageElement.appendChild(textElement);

            chatMessagesContainer.appendChild(messageElement);
        });

        chatTypeIcon.addEventListener('click', () => {
            chatMenu.style.display = 'block';
        });

        document.addEventListener('click', (e) => {
            if (!e.target.closest('.chat-menu') && !e.target.classList.contains('chat-type-icon')) {
                chatMenu.style.display = 'none';
            }
        });

        const sendMessageFormElement = document.getElementById('send-message-form');
        sendMessageFormElement.addEventListener('submit', async (e) => {
            e.preventDefault();
            const messageText = newMessageInput.value.trim();

            if (messageText) {
                try {
                    const response = await fetch(`/api/chats/${id}/messages`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ message: messageText })
                    });

                    if (!response.ok) {
                        throw new Error(`Ошибка HTTP! status: ${response.status}`);
                    }

                    newMessageInput.value = '';
                    await updateChatWindow(chat);

                } catch (error) {
                    console.error('Ошибка:', error);
                }
            }
        });

        chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight;

    } catch (error) {
        console.error('Ошибка:', error);
    }
}

document.getElementById('search-button').addEventListener('click', async () => {
    const searchInput = document.getElementById('search-input');
    const username = searchInput.value.trim();

    if (username) {
        try {
            const response = await fetch(`/api/users?regex=${username}`, {credentials: 'include'});
            if (!response.ok){
                throw new Error(`Ошибка HTTP! status: ${response.status}`);
            }
            const users = response.json();

            // вывести список юзеров
        }catch (error){
            console.error(error);
        }
        console.log(`Поиск пользователя: ${username}`);
    }
});

async function fetchChats() {
    try {
        const chatsApiUrl = '/api/chats';

        const response = await fetch(chatsApiUrl);
        if (!response.ok) {
            throw new Error(`Ошибка HTTP! status: ${response.status}`);
        }
        const chats = await response.json();
        updateChatsList(chats);

    } catch (error) {
        console.error('Ошибка:', error);
    }
}

fetchChats();

const profileIcon = document.querySelector('.profile-icon');
const profileMenu = document.querySelector('.profile-menu');

let timer;

profileIcon.addEventListener('mouseover', function() {
    clearTimeout(timer);
    profileMenu.style.display = 'block';
});

profileMenu.addEventListener('mouseover', function() {
    clearTimeout(timer);
    profileMenu.style.display = 'block';
});

profileIcon.addEventListener('mouseout', function() {
    timer = setTimeout(function() {
        if (!profileMenu.matches(':hover')) {
            profileMenu.style.display = 'none';
        }
    }, 500);
});

profileMenu.addEventListener('mouseout', function() {
    timer = setTimeout(function() {
        if (!profileIcon.matches(':hover') && !profileMenu.matches(':hover')) {
            profileMenu.style.display = 'none';
        }
    }, 500);
});
