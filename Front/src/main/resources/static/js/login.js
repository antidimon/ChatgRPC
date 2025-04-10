document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.auth-form');
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.href = '/';
                } else {
                    // Отображение ошибки
                    alert('Неправильный логин или пароль');
                }
            })
            .catch(error => console.error('Ошибка:', error));
    });
});
