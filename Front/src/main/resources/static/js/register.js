// Получаем все поля формы
const usernameInput = document.getElementById('username');
const nameInput = document.getElementById('name');
const ageInput = document.getElementById('age');
const emailInput = document.getElementById('email');
const phoneNumberInput = document.getElementById('phoneNumber');
const passwordInput = document.getElementById('password');

// Функция для проверки логина
function checkUsername() {
    const username = usernameInput.value.trim();
    if (username.length < 3 || username.length > 20) {
        usernameInput.style.border = '1px solid red';
        document.getElementById('usernameError').innerText = 'Логин должен быть от 3 до 20 символов';
    } else {
        usernameInput.style.border = '1px solid green';
        document.getElementById('usernameError').innerText = '';
    }
}

// Функция для проверки имени
function checkName() {
    const name = nameInput.value.trim();
    if (name.length < 2 || name.length > 50) {
        nameInput.style.border = '1px solid red';
        document.getElementById('nameError').innerText = 'Имя должно быть от 2 до 50 символов';
    } else {
        nameInput.style.border = '1px solid green';
        document.getElementById('nameError').innerText = '';
    }
}

// Функция для проверки возраста
function checkAge() {
    const age = parseInt(ageInput.value);
    if (isNaN(age) || age < 0 || age > 100) {
        ageInput.style.border = '1px solid red';
        document.getElementById('ageError').innerText = 'Возраст должен быть от 0 до 100 лет';
    } else {
        ageInput.style.border = '1px solid green';
        document.getElementById('ageError').innerText = '';
    }
}

// Функция для проверки электронной почты
function checkEmail() {
    const email = emailInput.value.trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        emailInput.style.border = '1px solid red';
        document.getElementById('emailError').innerText = 'Неправильный формат электронной почты';
    } else {
        emailInput.style.border = '1px solid green';
        document.getElementById('emailError').innerText = '';
    }
}

// Функция для проверки номера телефона
function checkPhoneNumber() {
    const phoneNumber = phoneNumberInput.value.trim();
    const phoneRegex = /^8\d{10}$/;
    if (!phoneRegex.test(phoneNumber)) {
        phoneNumberInput.style.border = '1px solid red';
        document.getElementById('phoneNumberError').innerText = 'Номер телефона должен быть в формате 8ХХХХХХХХХХ';
    } else {
        phoneNumberInput.style.border = '1px solid green';
        document.getElementById('phoneNumberError').innerText = '';
    }
}

// Функция для проверки пароля
function checkPassword() {
    const password = passwordInput.value.trim();
    if (password.length < 5) {
        passwordInput.style.border = '1px solid red';
        document.getElementById('passwordError').innerText = 'Пароль должен быть не менее 5 символов';
    } else {
        passwordInput.style.border = '1px solid green';
        document.getElementById('passwordError').innerText = '';
    }
}

// Добавляем обработчики событий для каждого поля
usernameInput.addEventListener('input', checkUsername);
nameInput.addEventListener('input', checkName);
ageInput.addEventListener('input', checkAge);
emailInput.addEventListener('input', checkEmail);
phoneNumberInput.addEventListener('input', checkPhoneNumber);
passwordInput.addEventListener('input', checkPassword);
