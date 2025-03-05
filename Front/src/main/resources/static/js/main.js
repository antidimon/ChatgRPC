// Добавление чатов в список
const chatsList = document.getElementById('chats');

// Функция для обновления списка чатов на странице
function updateChatsList(chats) {
    chatsList.innerHTML = ''; // Очистка списка

    chats.forEach((chat) => {
        const chatItem = document.createElement('LI');
        chatItem.textContent = chat.name;
        chatItem.addEventListener('click', () => {
            // Обновление окна чата при клике на чат
            updateChatWindow(chat.chatId);
        });
        chatsList.appendChild(chatItem);
    });
}

// Обновление окна чата
async function updateChatWindow(chatId) {
    const chatWindow = document.querySelector('.chat-window');
    chatWindow.innerHTML = ''; // Очистка окна чата

    // Добавляем заголовок чата
    const chatHeader = document.createElement('div');
    chatHeader.classList.add('chat-header');
    const chatTypeIcon = document.createElement('img');
    chatTypeIcon.src = '/images/private-chat-icon.png';
    chatTypeIcon.classList.add('chat-type-icon');
    const chatNameSpan = document.createElement('span');
    chatNameSpan.id = 'chat-name';
    chatNameSpan.textContent = 'Название чата'; // Укажите название чата
    const chatMenu = document.createElement('div');
    chatMenu.classList.add('chat-menu');
    chatMenu.style.display = 'none';
    chatHeader.appendChild(chatTypeIcon);
    chatHeader.appendChild(chatNameSpan);
    chatHeader.appendChild(chatMenu);
    chatWindow.appendChild(chatHeader);

    // Добавляем контейнер для сообщений
    const chatMessagesContainer = document.createElement('div');
    chatMessagesContainer.classList.add('chat-messages-container');
    chatWindow.appendChild(chatMessagesContainer);

    // Добавляем поле для ввода нового сообщения
    const chatInput = document.createElement('div');
    chatInput.classList.add('chat-input');
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
    chatInput.appendChild(sendMessageForm);
    chatWindow.appendChild(chatInput);

    chatHeader.style.display = 'flex';
    chatMessagesContainer.style.display = 'block';
    chatInput.style.display = 'flex';

    try {
        // URL API для получения сообщений
        const messagesApiUrl = `/api/chats/${chatId}/messages`;

        // Отправка GET-запроса на сервер
        const response = await fetch(messagesApiUrl, { credentials: 'include' });
        if (!response.ok) {
            throw new Error(`Ошибка HTTP! status: ${response.status}`);
        }
        const messages = await response.json();
        console.log(messages); // Вывод полученных сообщений в консоль

        // Отображение сообщений в окне чата
        messages.forEach((message) => {
            const messageElement = document.createElement('DIV');
            messageElement.classList.add('chat-message', message.messageType.toLowerCase());

            // Создаем элемент для имени отправителя
            const senderElement = document.createElement('DIV');
            senderElement.classList.add('sender-username');
            senderElement.textContent = message.senderUsername;
            messageElement.appendChild(senderElement);

            // Создаем элемент для текста сообщения
            const textElement = document.createElement('DIV');
            textElement.classList.add('message-text');
            textElement.textContent = message.message;
            messageElement.appendChild(textElement);

            chatMessagesContainer.appendChild(messageElement);
        });

        // Показ всплывающего меню при клике на иконку чата
        chatTypeIcon.addEventListener('click', () => {
            chatMenu.style.display = 'block';
        });

        // Скрытие меню при клике вне его
        document.addEventListener('click', (e) => {
            if (!e.target.closest('.chat-menu') && !e.target.classList.contains('chat-type-icon')) {
                chatMenu.style.display = 'none';
            }
        });

        // Отправка сообщения
        const sendMessageFormElement = document.getElementById('send-message-form');
        sendMessageFormElement.addEventListener('submit', async (e) => {
            e.preventDefault();
            const messageText = newMessageInput.value.trim();

            if (messageText) {
                try {
                    // AJAX-запрос на сервер для отправки сообщения
                    const response = await fetch(`/api/chats/${chatId}/messages`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ message: messageText })
                    });

                    if (!response.ok) {
                        throw new Error(`Ошибка HTTP! status: ${response.status}`);
                    }

                    // Очистка поля ввода и обновление окна чата
                    newMessageInput.value = '';
                    updateChatWindow(chatId); // Вызовите функцию обновления чата
                } catch (error) {
                    console.error('Ошибка:', error);
                }
            }
        });

        // Прокрутка вниз
        chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight;
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

// Поиск пользователей
document.getElementById('search-button').addEventListener('click', () => {
    const searchInput = document.getElementById('search-input');
    const username = searchInput.value.trim();

    if (username) {
        // AJAX-запрос на сервер для поиска пользователя
        console.log(`Поиск пользователя: ${username}`);
        // Обработка ответа от сервера
    }
});

// Функция для получения чатов
async function fetchChats() {
    try {
        // URL API для получения чатов
        const chatsApiUrl = '/api/chats';

        // Отправка GET-запроса на сервер
        const response = await fetch(chatsApiUrl);
        if (!response.ok) {
            throw new Error(`Ошибка HTTP! status: ${response.status}`);
        }
        const chats = await response.json();
        console.log(chats); // Вывод полученных чатов в консоль

        // Обновление списка чатов на странице
        updateChatsList(chats);
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

// Вызов функции для получения чатов при загрузке страницы
fetchChats();

// Дополнительный код для работы с меню профиля
const profileIcon = document.querySelector('.profile-icon');
const profileMenu = document.querySelector('.profile-menu');

let timer;

profileIcon.addEventListener('mouseover', function() {
    clearTimeout(timer);
    profileMenu.style.display = 'block';
});

profileMenu.addEventListener('mouseover', function() {
    clearTimeout(timer);
    profileMenu.style.display = 'block';
});

profileIcon.addEventListener('mouseout', function() {
    timer = setTimeout(function() {
        if (!profileMenu.matches(':hover')) {
            profileMenu.style.display = 'none';
        }
    }, 500); // Задержка в 500 мс
});

profileMenu.addEventListener('mouseout', function() {
    timer = setTimeout(function() {
        if (!profileIcon.matches(':hover') && !profileMenu.matches(':hover')) {
            profileMenu.style.display = 'none';
        }
    }, 500); // Задержка в 500 мс
});
