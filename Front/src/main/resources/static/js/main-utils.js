export function createMessageElement(message) {
    const messageElement = document.createElement('DIV');
    messageElement.classList.add('chat-message', message.messageType.toLowerCase());

    const senderElement = document.createElement('DIV');
    senderElement.classList.add('sender-username');
    senderElement.textContent = message.senderUsername || 'Система';
    messageElement.appendChild(senderElement);

    const textElement = document.createElement('DIV');
    textElement.classList.add('message-text');
    textElement.textContent = message.message;
    messageElement.appendChild(textElement);

    return messageElement;
}

export function positionElement(element, target) {
    const rect = target.getBoundingClientRect();
    element.style.left = `${rect.right + window.scrollX}px`;
    element.style.top = `${rect.top + window.scrollY}px`;
}

export function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
    });
}