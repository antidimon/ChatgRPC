// Добавление чатов в список
const chatsList = document.getElementById('chats');

// Пример данных о чатах
const chatsData = [
    { id: 1, name: 'Чат 1' },
    { id: 2, name: 'Чат 2' },
    { id: 3, name: 'Чат 3' },
];

chatsData.forEach((chat) => {
    const chatItem = document.createElement('LI');
    chatItem.textContent = chat.name;
    chatItem.addEventListener('click', () => {
        // Обновление окна чата при клике на чат
        updateChatWindow(chat.id);
    });
    chatsList.appendChild(chatItem);
});

// Обновление окна чата
function updateChatWindow(chatId) {
    const chatWindow = document.querySelector('.chat-window');
    chatWindow.innerHTML = '';

    // Пример сообщений в чате
    const messages = [
        { text: 'Привет!', type: 'received' },
        { text: 'Привет! Как дела?', type: 'sent' },
        { text: 'Хорошо, спасибо!', type: 'received' },
    ];

    messages.forEach((message) => {
        const messageElement = document.createElement('DIV');
        messageElement.textContent = message.text;
        messageElement.classList.add('chat-message', message.type);
        chatWindow.appendChild(messageElement);
    });
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