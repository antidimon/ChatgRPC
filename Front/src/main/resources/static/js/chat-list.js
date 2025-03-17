// chat-list.js
import { updateChatWindow } from './chat-ui.js';

/**
 * Обновляет список чатов.
 */
export function updateChatsList(chats) {
    const chatsList = document.getElementById('chats');
    if (!chatsList) {
        console.error('Не найден список чатов');
        return;
    }

    chatsList.innerHTML = '';
    chats.forEach((chat) => {
        const chatItem = document.createElement('LI');
        chatItem.textContent = chat.name;
        chatItem.classList.add('chat-item');
        chatItem.addEventListener('click', () => {
            updateChatWindow(chat);
        });
        chatsList.appendChild(chatItem);
    });
}
