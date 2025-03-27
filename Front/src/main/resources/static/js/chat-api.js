export async function fetchChats() {
    try {
        const response = await fetch('/api/chats');
        if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);

        return await response.json();
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

export async function getChatInfo(chatId, isPrivate) {
    const url = `/api/chats/${chatId}?isPrivate=${isPrivate}`;
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);
    return await response.json();
}


export async function getChatMessages(chatId) {
    const response = await fetch(`/api/chats/${chatId}/messages`, { credentials: 'include' });
    if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);
    return await response.json();
}


export async function sendMessageToChat(chatId, messageText) {
    const response = await fetch(`/api/chats/${chatId}/messages`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ message: messageText }),
    });
    if (!response.ok) throw new Error(`Ошибка HTTP! status: ${response.status}`);
    return response;
}



export async function createPrivateChat(username) {
    const chatResponse = await fetch(`/api/chats/private?user2=${username}`, {
        method: 'POST',
    });
    if (!chatResponse.ok) {
        throw new Error(`Ошибка создания чата: ${chatResponse.status}`);
    }
    return await chatResponse.json();
}



export async function createGroupChat(name, description){
    const chatResponse = await fetch('/api/chats/group', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, description }),
    });
    if (!chatResponse.ok){
        throw new Error(`Ошибка создания чата: ${chatResponse.status}`);
    }
    return await chatResponse.json();
}



export async function deleteChat(chatId, isPrivate) {
    const deleteUrl = `/api/chats/${chatId}?isPrivate=${isPrivate}`;
    const deleteResponse = await fetch(deleteUrl, {
        method: 'DELETE',
    });
    if (!deleteResponse.ok) {
        throw new Error(`Ошибка при удалении чата: ${deleteResponse.status}`);
    }
    return deleteResponse;
}
