import { getCurrentChatId } from './chat-state.js';
import {getCurrentProfileUsername, setCurrentProfileUsername} from './user-context.js';
import {sendMessageToChat, createPrivateChat, fetchChats} from './chat-api.js';
import { updateChatWindow } from './chat-ui.js';
import {updateChatsList} from "./chat-list.js";


export async function sendMessage(messageText) {
    const currentChatId = getCurrentChatId();
    const currentProfileUsername = getCurrentProfileUsername();
    const searchInput = document.getElementById('search-input');
    const usersList = document.getElementById('users');
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

    setCurrentProfileUsername(null);
    searchInput.value = '';
    usersList.style.display = 'none';
    fetchChats().then(chats => updateChatsList(chats));

}
