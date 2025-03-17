// user-context.js

let currentProfileUsername = null;

/**
 * Получает имя пользователя текущего профиля.
 */
export function getCurrentProfileUsername() {
    return currentProfileUsername;
}

/**
 * Устанавливает имя пользователя текущего профиля.
 */
export function setCurrentProfileUsername(username) {
    currentProfileUsername = username;
}
