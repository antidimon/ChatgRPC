// chat-state.js

let currentChatId = null;

/**
 * Получает ID текущего чата.
 */
export function getCurrentChatId() {
    return currentChatId;
}

/**
 * Устанавливает ID текущего чата.
 */
export function setCurrentChatId(chatId) {
    currentChatId = chatId;
}
