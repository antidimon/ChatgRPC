export function updateUsersList(users, usersList, chatsList, fetchChatsCallback) {
    usersList.innerHTML = '';

    users.forEach((user) => {
        const userItem = document.createElement('LI');
        userItem.textContent = user.username;
        userItem.classList.add('user-item'); // Добавляем класс user-item

        userItem.addEventListener('click', () => {
            console.log(`Выбран пользователь: ${user.username}`);
            // Логика взаимодействия с пользователем
        });

        usersList.appendChild(userItem);
    });

    // Показываем список пользователей и скрываем список чатов
    usersList.style.display = 'block';
    chatsList.style.display = 'none';
}
