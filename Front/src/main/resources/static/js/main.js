import { updateChatWindow } from './main-chat-window.js';
import { updateUsersList } from './main-user-menu.js';

document.addEventListener('DOMContentLoaded', () => {
    const chatsList = document.getElementById('chats');
    const usersList = document.getElementById('users');
    const searchInput = document.getElementById('search-input');
    const searchButton = document.getElementById('search-button');

    if (!chatsList || !usersList || !searchInput || !searchButton) {
        console.error('Не найдены необходимые элементы');
        return;
    }

    usersList.style.display = 'none';

    function updateChatsList(chats) {
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

    async function fetchChats() {
        try {
            const response = await fetch('/api/chats');
            if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

            const chats = await response.json();
            updateChatsList(chats);
        } catch (error) {
            console.error('Ошибка:', error);
        }
    }

    async function fetchUsers(username) {
        try {
            const response = await fetch(`/api/users?regex=${username}`, { credentials: 'include' });
            if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

            const users = await response.json();
            updateUsersList(users, usersList, chatsList, fetchChats);
        } catch (error) {
            console.error('Ошибка:', error);
        }
    }

    searchButton.addEventListener('click', () => {
        const username = searchInput.value.trim();

        if (username) {
            fetchUsers(username);
        } else {
            usersList.style.display = 'none';
            chatsList.style.display = 'block';
            fetchChats();
        }
    });

    searchInput.addEventListener('input', () => {
        if (searchInput.value.trim() === '') {
            usersList.style.display = 'none';
            chatsList.style.display = 'block';
            fetchChats();
        }
    });

    fetchChats();
});