// user-list.js
import { getUserProfile } from './user-api.js';
import { displayUserProfile } from './user-ui.js';


export function updateUsersList(users, usersList, chatsList) {
    usersList.innerHTML = '';

    users.forEach((user) => {
        const userItem = document.createElement('LI');
        userItem.textContent = user.username;
        userItem.classList.add('user-item');

        userItem.addEventListener('click', async () => {
            console.log(`Выбран пользователь: ${user.username}`);
            try {
                const userProfile = await getUserProfile(user.id);

                displayUserProfile(userProfile);
            } catch (error) {
                console.error('Ошибка при получении профиля пользователя:', error);
            }
        });

        usersList.appendChild(userItem);
    });

    usersList.style.display = 'block';
    chatsList.style.display = 'none';
}
