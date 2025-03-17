// user-api.js

/**
 * Получает профиль пользователя по его ID.
 */
export async function getUserProfile(userId) {
    const response = await fetch(`/api/users/${userId}`);
    if (!response.ok) {
        throw new Error(`Ошибка ${response.status}`);
    }
    return await response.json();
}
