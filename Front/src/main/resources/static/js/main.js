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

    try {
        // URL API для получения сообщений
        const messagesApiUrl = `/api/chats/${chatId}/messages`;

        // Отправка GET-запроса на сервер
        const response = await fetch(messagesApiUrl);
        if (!response.ok) {
            throw new Error(`Ошибка HTTP! status: ${response.status}`);
        }
        const messages = await response.json();
        console.log(messages); // Вывод полученных сообщений в консоль

        // Отображение сообщений в окне чата
        messages.forEach((message) => {
            const messageElement = document.createElement('DIV');
            messageElement.textContent = message.message;
            messageElement.title = message.senderId;
            chatWindow.appendChild(messageElement);
            
        });
        // Прокрутка вниз
        chatWindow.scrollTop = chatWindow.scrollHeight;
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
