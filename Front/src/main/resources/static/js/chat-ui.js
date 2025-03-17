import { sendMessage } from './chat-message.js';
import { createMessageElement } from './main-utils.js';
import { getChatInfo, getChatMessages, deleteChat } from './chat-api.js';
import { setCurrentChatId } from "./chat-state.js";

/**
 * Обновляет окно чата.
 */
export async function updateChatWindow(chat) {
    setCurrentChatId(chat.chatId);
    const chatWindow = document.querySelector('.chat-window');
    chatWindow.innerHTML = '';

    const chatHeader = createChatHeader(chat);
    const chatMessagesContainer = createChatMessagesContainer();
    const chatInputContainer = createChatInputContainer(chat.chatId); // Передаем chatId

    chatWindow.appendChild(chatHeader);
    chatWindow.appendChild(chatMessagesContainer);
    chatWindow.appendChild(chatInputContainer);

    // Загрузка и отображение сообщений
    try {
        const messages = await getChatMessages(chat.chatId);
        messages.forEach(message => {
            const messageElement = createMessageElement(message);
            chatMessagesContainer.appendChild(messageElement);
        });

        // Скролл к последнему сообщению
        chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight;

    } catch (error) {
        console.error(error);
    }
}

/**
 * Создает шапку чата.
 */
function createChatHeader(chat) {
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

    return chatHeader;
}

/**
 * Создает контейнер для сообщений.
 */
function createChatMessagesContainer() {
    const chatMessagesContainer = document.createElement('div');
    chatMessagesContainer.classList.add('chat-messages-container');
    return chatMessagesContainer;
}

/**
 * Создает форму отправки сообщений для чата.
 */
function createChatInputContainer(chatId) {
    const chatInputContainer = document.createElement('div');
    chatInputContainer.classList.add('chat-input');

    const sendMessageForm = document.createElement('form');
    sendMessageForm.id = `send-message-form-${chatId}`; // Уникальный ID для формы

    const newMessageInput = document.createElement('input');
    newMessageInput.type = 'text';
    newMessageInput.id = `new-message-${chatId}`;
    newMessageInput.placeholder = 'Введите сообщение...';

    const sendButton = document.createElement('button');
    sendButton.id = `send-button-${chatId}`;
    sendButton.textContent = 'Отправить';

    sendMessageForm.appendChild(newMessageInput);
    sendMessageForm.appendChild(sendButton);
    chatInputContainer.appendChild(sendMessageForm);

    // Добавляем обработчик события отправки формы
    sendMessageForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const messageText = newMessageInput.value.trim();

        if (messageText) {
            await sendMessage(messageText);
            newMessageInput.value = '';
        }
    });

    return chatInputContainer;
}

/**
 * Создает меню чата.
 */
async function createChatMenu(chat, chatMenuElement) {
    chatMenuElement.innerHTML = ''; // Очищаем меню

    try {
        const chatData = await getChatInfo(chat.chatId, chat.chatType === 'PRIVATE');

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

        // Добавляем кнопку удаления чата
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Удалить чат';
        deleteButton.addEventListener('click', async () => {
            if (confirm('Вы уверены, что хотите удалить этот чат?')) {
                try {
                    await deleteChat(chat.chatId, chat.chatType === 'PRIVATE');
                    alert('Чат успешно удален.');
                    window.location.reload(); //  перезагрузка страницы
                } catch (deleteError) {
                    console.error('Ошибка при удалении чата:', deleteError);
                    alert('Ошибка при удалении чата.');
                }
            }
        });

        chatMenuElement.innerHTML = chatInfo;
        chatMenuElement.appendChild(deleteButton);

    } catch (error) {
        console.error('Ошибка при получении информации о чате:', error);
        chatMenuElement.innerHTML = `<p>Ошибка загрузки информации о чате</p>`;
    }
}
