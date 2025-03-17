import { createMessageElement } from './main-utils.js';
import { getCurrentProfileUsername } from './main-user-menu.js';

let currentChatId = null;

export function getCurrentChatId() {
    return currentChatId;
}

export function setCurrentChatId(chatId) {
    currentChatId = chatId;
}

async function createChatMenu(chat, chatMenuElement) {
    chatMenuElement.innerHTML = ''; // Очищаем меню

    try {
        const url = `/api/chats/${chat.chatId}?isPrivate=${chat.chatType === 'PRIVATE'}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

        const chatData = await response.json();

        let chatInfo = '';
        if (chat.chatType === 'GROUP') {
            chatInfo = `
                <p>Тип: Групповой</p>
                <p>Название: ${chatData.name}</p>
                <p>Описание: ${chatData.description}</p>
                <p>Владелец: ${chatData.users.find(u => u.id === chatData.ownerId)?.username || 'Неизвестен'}</p>
                <p>Участники: ${chatData.users.map(u => u.username).join(', ') || 'Нет'}</p>
            `;
        } else if (chat.chatType === 'PRIVATE') {
            chatInfo = `
                <p>Тип: Личный</p>
                <p>Пользователь 1: ${chatData.user1?.username || 'Неизвестен'}</p>
                <p>Пользователь 2: ${chatData.user2?.username || 'Неизвестен'}</p>
            `;
        } else {
            chatInfo = `<p>Неизвестный тип чата</p>`;
        }


        chatMenuElement.innerHTML = chatInfo;
    } catch (error) {
        console.error('Ошибка при получении информации о чате:', error);
        chatMenuElement.innerHTML = `<p>Ошибка загрузки информации о чате</p>`;
    }
}

export async function updateChatWindow(chat) {
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

    const chatInputContainer = document.createElement('div');
    chatInputContainer.classList.add('chat-input');

    chatInputContainer.appendChild(sendMessageForm);

    chatWindow.appendChild(chatInputContainer);

    currentChatId = chat.chatId;

    try {
        const response = await fetch(`/api/chats/${chat.chatId}/messages`, { credentials: 'include' });
        if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

        const messages = await response.json();
        messages.forEach((message) => {
            const messageElement = createMessageElement(message);
            chatMessagesContainer.appendChild(messageElement);
        });
        console.log('Форма существует:', document.querySelector('#send-message-form'));
        sendMessageForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const messageText = newMessageInput.value.trim();

            if (messageText) {
                // Отправка сообщения
                await sendMessage(messageText);
            }
        });

        // Скролл к последнему сообщению
        chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight;

        // Стилизация
        chatHeader.style.display = 'flex';
        chatMessagesContainer.style.display = 'block';
        chatInputContainer.style.display = 'flex';

        // Добавляем обработчик клика на иконку чата для отображения меню
        chatTypeIcon.addEventListener('click', async () => {
            await createChatMenu(chat, chatMenu); // Заполняем меню информацией о чате
            chatMenu.style.display = 'block'; // Отображаем меню
        });

        document.addEventListener('click', (e) => {
            if (!e.target.closest('.chat-menu') && !e.target.classList.contains('chat-type-icon')) {
                chatMenu.style.display = 'none';
            }
        });

    } catch (error) {
        console.error(error);
    }
}

async function sendMessage(messageText) {
    const newMessageInput = document.querySelector('#new-message');
    const currentProfileUsername = getCurrentProfileUsername();

    console.log(getCurrentChatId())
    console.log(getCurrentProfileUsername())

    if (currentChatId) {
        try {
            const response = await fetch(`/api/chats/${currentChatId}/messages`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: messageText }),
            });

            if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

            newMessageInput.value = '';
            await updateChatWindow({ chatId: currentChatId, name: document.querySelector('#chat-name').textContent });
        } catch (error) {
            console.error(error);
        }
    } else if (currentProfileUsername) {
        try {
            const chatResponse = await fetch(`/api/chats/private?user2=${currentProfileUsername}`, {
                method: 'POST',
            });

            if (!chatResponse.ok) {
                throw new Error(`Ошибка создания чата: ${chatResponse.status}`);
            }

            const chatData = await chatResponse.json();
            currentChatId = chatData.id;

            const messageResponse = await fetch(`/api/chats/${currentChatId}/messages`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ message: messageText }),
            });

            if (!messageResponse.ok) {
                throw new Error(`Ошибка отправки сообщения: ${messageResponse.status}`);
            }

            newMessageInput.value = '';
            await updateChatWindow({ chatId: currentChatId, name: currentProfileUsername });
        } catch (error) {
            console.error('Произошла ошибка:', error);
        }
    }
}
