// app.js
import { updateChatsList } from './chat-list.js';
import { fetchChats } from './chat-api.js';
import { fetchUsers } from './user-search.js';
import { setCurrentChatId } from './chat-state.js';

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

    searchButton.addEventListener('click', () => {
        const username = searchInput.value.trim();
        setCurrentChatId(null);
        if (username) {
            fetchUsers(username);
        } else {
            usersList.style.display = 'none';
            chatsList.style.display = 'block';
            fetchChats().then(chats => updateChatsList(chats));
        }
    });

    searchInput.addEventListener('input', () => {
        setCurrentChatId(null);
        if (searchInput.value.trim() === '') {
            usersList.style.display = 'none';
            chatsList.style.display = 'block';
            fetchChats().then(chats => updateChatsList(chats));
        }
    });

    fetchChats().then(chats => updateChatsList(chats));
});
