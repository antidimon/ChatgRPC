import {setCurrentProfileUsername} from "./user-context.js";
import {formatDate} from "./main-utils.js";
import {sendMessage} from "./chat-message.js";

export function displayUserProfile(userProfile) {
    setCurrentProfileUsername(userProfile.username);
    const chatHeader = document.querySelector('.chat-header');
    const chatMessagesContainer = document.querySelector('.chat-messages-container');
    const chatInputContainer = document.querySelector('.chat-input');
    const chatNameSpan = document.querySelector('#chat-name');
    let userProfileContainer = document.querySelector('.user-profile-container');

    if (!userProfileContainer) {
        userProfileContainer = document.createElement('div');
        userProfileContainer.classList.add('user-profile-container');
        document.querySelector('.chat-window').appendChild(userProfileContainer);
    }

    chatMessagesContainer.style.display = 'none';
    userProfileContainer.style.display = 'block';

    if (chatInputContainer) {
        chatInputContainer.remove();
    }

    userProfileContainer.innerHTML = `
        <h3>Профиль пользователя</h3>
        <p>Имя пользователя: ${userProfile.username}</p>
        <p>Имя: ${userProfile.name}</p>
        <p>Возраст: ${userProfile.age}</p>
        <p>Email: ${userProfile.email}</p>
        <p>Номер телефона: ${userProfile.phoneNumber}</p>
        <p>Дата создания: ${formatDate(userProfile.createdAt)}</p>
    `;

    chatNameSpan.textContent = userProfile.username;
    chatHeader.style.display = 'flex';

    // Удаляем старую форму, если она существует
    let existingChatInput = userProfileContainer.querySelector('.chat-input');
    if (existingChatInput) {
        existingChatInput.remove();
    }

    // Создаем новую форму
    const newChatInputContainer = document.createElement('div');
    newChatInputContainer.classList.add('chat-input');

    const sendMessageForm = document.createElement('form');
    sendMessageForm.id = `send-message-form-${userProfile.username}`; // Уникальный ID

    const newMessageInput = document.createElement('input');
    newMessageInput.type = 'text';
    newMessageInput.id = `new-message-${userProfile.username}`;
    newMessageInput.placeholder = 'Введите сообщение...';

    const sendButton = document.createElement('button');
    sendButton.id = `send-button-${userProfile.username}`;
    sendButton.textContent = 'Отправить';

    sendMessageForm.appendChild(newMessageInput);
    sendMessageForm.appendChild(sendButton);
    newChatInputContainer.appendChild(sendMessageForm);

    // Добавляем новую форму в userProfileContainer
    userProfileContainer.appendChild(newChatInputContainer);

    // Добавляем обработчик события отправки формы
    sendMessageForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const messageText = newMessageInput.value.trim();

        if (messageText) {
            await sendMessage(messageText);
            newMessageInput.value = '';
        }
    });
}
