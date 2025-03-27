function validatePhoneNumber(input) {
    const regex = /^[0-9]{11}$/;
    return regex.test(input);
}

function validateEmail(email) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
}

document.getElementById('profileForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(msg => msg.remove());

    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => input.classList.remove('error'));

    let isValid = true;

    if (this.username.value.length < 3) {
        handleError(this.username, 'Имя пользователя должно быть не менее 3 символов');
        isValid = false;
    }

    if (this.name.value.trim() === '') {
        handleError(this.name, 'Введите полное имя');
        isValid = false;
    }

    if (this.age.value < 0 || this.age.value > 100) {
        handleError(this.age, 'Возраст должен быть от 0 до 100 лет');
        isValid = false;
    }

    if (!validateEmail(this.email.value)) {
        handleError(this.email, 'Некорректный email');
        isValid = false;
    }

    if (!validatePhoneNumber(this.phoneNumber.value)) {
        handleError(this.phoneNumber, 'Номер телефона должен содержать 11 цифр');
        isValid = false;
    }

    if (this.password.value.length < 5 && this.password.value.length > 0) {
        handleError(this.password, 'Пароль должен быть не менее 5 символов');
        isValid = false;
    }

    if (isValid) {
        try {
            const response = await fetch(`/api/users/${window.userId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: this.username.value,
                    name: this.name.value,
                    age: parseInt(this.age.value),
                    email: this.email.value,
                    phoneNumber: this.phoneNumber.value,
                    password: this.password.value
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка обновления');
            }

            location.reload();

        } catch (error) {
            showToast(error.message, 'error');
        }
    }
});

function handleError(input, message) {
    input.classList.add('error');
    input.insertAdjacentHTML('afterend', `<div class="error-message">${message}</div>`);
}

function showToast(text, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = text;
    document.body.appendChild(toast);

    setTimeout(() => toast.remove(), 3000);
}


document.querySelector('.show-password').addEventListener('click', function() {
    const passwordInput = document.getElementById('password');
    passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
    this.textContent = passwordInput.type === 'password' ? 'Показать' : 'Скрыть';
});