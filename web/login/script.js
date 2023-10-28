console.clear();

const loginBtn = document.getElementById('login');
const signupBtn = document.getElementById('signup');

loginBtn.addEventListener('click', (e) => {
	let parent = e.target.parentNode.parentNode;
	Array.from(e.target.parentNode.parentNode.classList).find((element) => {
		if(element !== "slide-up") {
			parent.classList.add('slide-up')
		}else{
			signupBtn.parentNode.classList.add('slide-up')
			parent.classList.remove('slide-up')
		}
	});
});

signupBtn.addEventListener('click', (e) => {
	let parent = e.target.parentNode;
	Array.from(e.target.parentNode.classList).find((element) => {
		if(element !== "slide-up") {
			parent.classList.add('slide-up')
		}else{
			loginBtn.parentNode.parentNode.classList.add('slide-up')
			parent.classList.remove('slide-up')
		}
	});
});

document.getElementById('login-button').addEventListener('click', function(event) {
	event.preventDefault();  // 防止表单的默认提交行为

	var email = document.querySelector('.login .input[type="email"]').value;
	var password = document.querySelector('.login .input[type="password"]').value;

	if (!email.trim() || !password.trim()) {
		alert('Both email and password are required.');
		return;
	}

	var url = 'http://localhost:8080/user?username=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(password);

	fetch(url, {
		method: 'GET',
		credentials: 'include'  // Ensure cookies are sent with the request
	})
		.then(response => response.json())  // 假设服务器返回JSON响应
		.then(data => {
			if (data && data.userID) {  // 假设如果用户存在，服务器会返回一个包含 id 字段的对象
				// 如果登录成功，重定向到 ../homepage/index.html
				window.location.href = '../homepage/index.html';
			} else {
				// 如果登录失败，显示错误消息
				alert('Login failed: Invalid username or password.');
			}
		})
		.catch(error => console.error('Error:', error));
});


