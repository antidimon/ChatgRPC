export async function getUserProfile(userId) {
    const response = await fetch(`/api/users/${userId}`);
    if (!response.ok) {
        throw new Error(`Ошибка ${response.status}`);
    }
    return await response.json();
}


export async function addUserToChat(chatId, userId) {
    try {
        await addMemberToChat(chatId, userId);
        window.location.reload();
    } catch (error) {
        console.error('Ошибка при добавлении пользователя в чат:', error);
    }
}

export async function removeUserFromChat(chatId, userId) {
    try {
        await kickMemberFromChat(chatId, userId);
        window.location.reload();
    } catch (error) {
        console.error('Ошибка при удалении пользователя из чата:', error);
    }
}
