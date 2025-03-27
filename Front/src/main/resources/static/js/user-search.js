import { updateUsersList } from './user-list.js';

export async function fetchUsers(username) {
    try {
        const response = await fetch(`/api/users?regex=${username}`, { credentials: 'include' });
        if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

        const users = await response.json();
        updateUsersList(users, document.getElementById('users'), document.getElementById('chats'));
    } catch (error) {
        console.error('Ошибка:', error);
    }
}
