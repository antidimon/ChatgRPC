// chat-message.js
import { getCurrentChatId } from './chat-state.js';
import { getCurrentProfileUsername } from './user-context.js';
import { sendMessageToChat, createPrivateChat } from './chat-api.js';
import { updateChatWindow } from './chat-ui.js';

/**
 * Отправляет сообщение.
 */
export async function sendMessage(messageText) {
    const currentChatId = getCurrentChatId();
    const currentProfileUsername = getCurrentProfileUsername();
    if (currentChatId) {
        try {
            console.log("Sending message to chat")
            await sendMessageToChat(currentChatId, messageText);
            await updateChatWindow({ chatId: currentChatId, name: document.querySelector('#chat-name').textContent });
        } catch (error) {
            console.error(error);
        }
    } else if (currentProfileUsername) {
        try {
            console.log("Trying to create chat")
            const chatData = await createPrivateChat(currentProfileUsername);

            await sendMessageToChat(chatData.chatId, messageText);
            await updateChatWindow(chatData);
        } catch (error) {
            console.error('Произошла ошибка:', error);
        }
    }
}
