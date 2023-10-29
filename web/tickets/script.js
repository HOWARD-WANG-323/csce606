
function createTicketItem(ticket) {
    return `
         <input class="checkbox-ticket" type="radio" name="ticket" id="ticket-${ticket.ticketID}">
			<label for="ticket-${ticket.ticketID}">
				<span class="top-dots">
					<span class="section dots">
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
								</span>
							</span>
							<span class="TicketType">
								${ticket.ticketType}
							</span>
							<span class="price mt-2 pb-4 mb-3">
								<sup>$</sup>${ticket.price}
							</span>
							<span class="section dots">
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
								<span></span>
							</span>
							<span class="section pt-4">
								<i class='uil uil-ticket mt-3'></i>
							</span>
							<span class="time mt-2">
								2:00 pm - 3:30 pm
							</span>
							<span class="bottom-dots">
								<span class="section dots">
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
									<span></span>
								</span>
							</span>
						</label>
						</input>
    `;}

document.addEventListener('DOMContentLoaded', function() {
    // 获取URL参数
    const urlParams = new URLSearchParams(window.location.search);
    const eventId = urlParams.get('eventID');

    // 使用eventID构造API请求
    fetch(`http://localhost:8080/ticketByEvent/${eventId}`, {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json())
        .then(data => {
            const ticket_list = document.querySelector('.section-1');
            data.forEach(ticket => {
                ticket_list.innerHTML += createTicketItem(ticket);
            });
        });
});

// 获取所有的 radio 按钮和按钮元素
const radios = document.querySelectorAll('.checkbox-ticket');
const button = document.querySelector('.btn');

// 定义一个函数来检查 radio 按钮的状态
function checkRadioStatus() {
    let isChecked = false;
    radios.forEach(radio => {
        if (radio.checked) {
            isChecked = true;
        }
    });

    // 根据 radio 按钮的状态禁用或启用按钮
    if (isChecked) {
        button.disabled = false;
        button.classList.add('active');
    } else {
        button.disabled = true;
        button.classList.remove('active');
    }
}

// 添加事件监听器来检测 radio 按钮的变化
radios.forEach(radio => {
    radio.addEventListener('change', checkRadioStatus);
});

// 初始检查
checkRadioStatus();

