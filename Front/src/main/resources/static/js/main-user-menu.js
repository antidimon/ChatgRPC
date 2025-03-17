export function updateUsersList(users, usersList, chatsList, fetchChatsCallback) {
    usersList.innerHTML = '';

    users.forEach((user) => {
        const userItem = document.createElement('LI');
        userItem.textContent = user.username;
        userItem.classList.add('user-item');

        userItem.addEventListener('click', async () => {
            console.log(`Выбран пользователь: ${user.username}`);
            try {
                const response = await fetch(`/api/users/${user.id}`);
                if (!response.ok) {
                    throw new Error(`Ошибка ${response.status}`);
                }
                const userProfile = await response.json();

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

let currentProfileUsername = null;

export function getCurrentProfileUsername() {
    return currentProfileUsername;
}

export function setCurrentProfileUsername(username) {
    currentProfileUsername = username;
}

function displayUserProfile(userProfile) {
    setCurrentProfileUsername(userProfile.username);

    const chatHeader = document.querySelector('.chat-header');
    const chatMessagesContainer = document.querySelector('.chat-messages-container');
    const chatInputContainer = document.querySelector('.chat-input');
    const chatNameSpan = document.querySelector('#chat-name');
    const userProfileContainer = document.querySelector('.user-profile-container');

    chatMessagesContainer.style.display = 'none';
    userProfileContainer.style.display = 'block';

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

    // Создаём форму для отправки сообщений, если её еще нет
    if (!chatInputContainer.querySelector('#send-message-form')) {
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

        chatInputContainer.appendChild(sendMessageForm);
    }

    chatInputContainer.style.display = 'flex';
}


function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
    });
}
