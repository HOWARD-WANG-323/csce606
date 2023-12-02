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
	event.preventDefault();  // Prevent the default form submission behavior

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
		.then(response => {
			if (!response.ok) {
				throw new Error('Login failed');
			}
			return response.json();
		}) // Assuming the server returns a JSON response
		.then(data => {
			if (data && data.userID) {  // Assuming if the user exists, the server would return an object with a userID field
				// Display the user's full name (you can adjust this as needed)
				alert('Logged in successfully as ' + data.fullName);
				console.log(data);
				// If logged in successfully, redirect to ../homepage/index.html
				window.location.href = '../homepage/index.html';
			} else {
				// If login failed, show an error message
				alert('Login failed: Invalid username or password.');
			}
		})
		.catch(error => {console.error('Error:', error);
		alert('Login failed: Invalid username or password.');
		});
});


document.getElementById('sign-up-btn').addEventListener('click', function(event) {
	event.preventDefault(); // 防止表单默认提交行为

	var name = document.querySelector('.signup .input[type="text"]').value;
	var email = document.querySelector('.signup .input[type="email"]').value;
	var password = document.querySelector('.signup .input[type="password"]').value;

	if (!name.trim() || !email.trim() || !password.trim()) {
		alert('All fields (name, email, and password) are required.');
		return;
	}

	var url = 'http://localhost:8080/signup'; // 修改为正确的注册路径
	var data = {
		name: name,
		email: email,
		password: password
	};

	fetch(url, {
		method: 'POST',
		credentials: 'include',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Registration failed');
			}
			return response.json();
		})
		.then(data => {
			if (data && data.userID) {
				alert('Registered successfully. Welcome ' + data.fullName + '!');
				//window.location.href = '../homepage/index.html';
			} else {
				alert('Registration failed: ' + (data.message || 'Unknown error'));
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert('Registration failed: ' + (data.message || 'Unknown error'));
		});
});

