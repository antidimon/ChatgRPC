import { updateChatsList } from './chat-list.js';
import {createGroupChat, fetchChats} from './chat-api.js';
import { fetchUsers } from './user-search.js';
import { setCurrentChatId } from './chat-state.js';
import {updateChatWindow} from "./chat-ui.js";

document.addEventListener('DOMContentLoaded', () => {
    const chatsList = document.getElementById('chats');
    const usersList = document.getElementById('users');
    const searchInput = document.getElementById('search-input');
    const searchButton = document.getElementById('search-button');
    const groupAddButton = document.getElementById('group-add-button');
    const groupModal = document.getElementById('group-modal');
    const groupNameInput = document.getElementById('group-name');
    const groupDescriptionInput = document.getElementById('group-description');
    const groupCreateButton = document.getElementById('group-create-button');
    const groupCancelButton = document.getElementById('group-cancel-button');

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

    groupAddButton.addEventListener('click', () => {
        console.log("Открыть");
        groupModal.style.display = 'block';
    });

    groupCancelButton.addEventListener('click', () => {
        groupModal.style.display = 'none';
    });

    groupCreateButton.addEventListener('click', async () => {
        const name = groupNameInput.value.trim();
        const description = groupDescriptionInput.value.trim();

        if (name && description) {
            try {
                const data = await createGroupChat(name, description);
                groupModal.style.display = 'none';
                fetchChats().then(chats => updateChatsList(chats));
                await updateChatWindow(data);
            } catch (error) {
                console.error('Ошибка:', error);
            }
        } else {
            alert('Пожалуйста, заполните все поля');
        }
    });

    fetchChats().then(chats => updateChatsList(chats));
});
