function createTicketItem(item) {
    const container = document.createElement('div');
    container.className = "row d-flex justify-content-center border-top";
    container.innerHTML = `
                <div class="col-5">
                    <div class="row d-flex">
                        <div class="book">
                            <img src="https://file.48.cn/Shop/Product/202310/202310241134153501.png" class="book-img">
                        </div>
                        <div class="my-auto flex-column d-flex pad-left">
                            <h6 class="mob-text" id="eventName${item.ticketID}">Loading...</h6>
                            <p class="mob-text" id="eventDate${item.ticketID}">Loading...</p>
                        </div>
                    </div>
                </div>
                <div class="my-auto col-7">
                    <div class="row text-right">
                        <div class="col-4">
                            <p class="mob-text">${item.ticketType}</p>
                        </div>
                        <div class="col-4">
                            <div class="row d-flex justify-content-end px-3">
                                <p class="mb-0" id="cnt${item.ticketID}">1</p>
                                <div class="d-flex flex-column plus-minus">
                                <span class="vsm-text plus">+</span>
                                <span class="vsm-text minus">-</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <h6 class="mob-text">$${item.price}</h6>
                        </div>
                    </div>
                </div>
            `;
    const plusButton = container.querySelector('.plus');
    const minusButton = container.querySelector('.minus');

    plusButton.addEventListener('click', function() {
        const countElem = plusButton.parentElement.previousElementSibling;
        countElem.textContent = Number(countElem.textContent) + 1;
        updatePrices();
    });

    minusButton.addEventListener('click', function() {
        const countElem = minusButton.parentElement.previousElementSibling;
        if (Number(countElem.textContent) > 0) {
            countElem.textContent = Number(countElem.textContent) - 1;
        }
        updatePrices();
    });

    return container;
}

document.addEventListener('DOMContentLoaded', function() {
    const ticketsArray = JSON.parse(localStorage.getItem('selectedTickets') || "[]");
    ticketsArray.forEach(ticketID => {
        fetch(`http://localhost:8080/ticket/${ticketID}`, {
            method: 'GET',
            credentials: 'include'
        })
            .then(response => response.json())
            .then(ticketData => {
                const ticketItemsContainer = document.getElementById('ticket-items-container');
                ticketItemsContainer.appendChild(createTicketItem(ticketData));
                return getEventfromTicketID(ticketID);
            })
            .then(eventID => {
                return getEventfromID(eventID);
            })
            .then(eventData => {
                document.getElementById(`eventName${ticketID}`).innerText = eventData.eventName;
                document.getElementById(`eventDate${ticketID}`).innerText = eventData.eventDate;
            });
    });
    updatePrices();
});

function getEventfromTicketID(ticketID) {
    return fetch(`http://localhost:8080/ticket/${ticketID}`, {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json())
        .then(data => data.eventID);
}

function getEventfromID(eventID) {
    return fetch(`http://localhost:8080/event/${eventID}`, {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json());
}

document.addEventListener("DOMContentLoaded", function() {

    // Radio group functionality
    let radios = document.querySelectorAll('.radio-group .radio');
    radios.forEach(function(radio) {
        radio.addEventListener('click', function() {
            radios.forEach(function(innerRadio) {
                innerRadio.classList.add('gray');
            });
            this.classList.remove('gray');
        });
    });

    /*// Plus button functionality
    let plusButtons = document.querySelectorAll('.plus-minus .plus');
    plusButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            let countElem = this.parentElement.previousElementSibling;
            countElem.textContent = Number(countElem.textContent) + 1;
        });
    });

    // Minus button functionality
    let minusButtons = document.querySelectorAll('.plus-minus .minus');
    minusButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            let countElem = this.parentElement.previousElementSibling;
            if(Number(countElem.textContent) > 0) {
                countElem.textContent = Number(countElem.textContent) - 1;
            }
        });
    });*/

});

function fetchTicketInfo(ticketID) {
    return fetch(`http://localhost:8080/ticket/${ticketID}`, {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json());
}


// Example prices and tax rate (adjust as needed)
const TAX_RATE = 0.0723;  // 7.23% tax rate
const SHIPPING_COST = 0;  // digital goods don't have shipping costs

async function updatePrices() {
    let cartItems = localStorage.getItem('selectedTickets');
    if (cartItems) {
        cartItems = JSON.parse(cartItems);
    } else {
        cartItems = [];
    }

    let subtotal = 0;

    for (let ticketID of cartItems) {
        let ticketInfo = await fetchTicketInfo(ticketID);

        // 获取票商品的数量
        const countElem = document.getElementById(`cnt${ticketID}`);
        let quantity = 1;  // 默认数量为1
        if (countElem) {
            quantity = parseInt(countElem.textContent, 10);
        }

        subtotal += ticketInfo.price * quantity;  // 考虑数量
    }

    let totalPrice = subtotal + SHIPPING_COST;
    totalPrice += totalPrice * TAX_RATE;

    document.getElementById('subtotal-price').textContent = `$${subtotal.toFixed(2)}`;
    document.getElementById('shipping-price').textContent = `$${SHIPPING_COST.toFixed(2)}`;
    document.getElementById('total-price').textContent = `$${totalPrice.toFixed(2)}`;
    document.getElementById('check-amt').textContent = `$${totalPrice.toFixed(2)}`;
}

