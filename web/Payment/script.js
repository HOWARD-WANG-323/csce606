const checkOutBtn = document.getElementById('checkoutBtn');

function createTicketItem(item) {
    const container = document.createElement('div');
    container.className = "row d-flex justify-content-center border-top";
    container.innerHTML = `
                <div class="col-5">
                    <div class="row d-flex">
                        <div class="book">
                            <img src="https://www.barclayscenter.com/assets/img/New-York_Website-Thumbnail-1-656X596safety-around-bottom-e2d40dd0c9.jpg" class="book-img">
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
                        <div class="d-flex flex-column align-items-end">
                            <h6 class="mob-text">$${item.price}</h6>
                            <button class="btn btn-danger delete-btn rounded-square">×</button>
                        </div>
                        </div>
                        
              
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

    const deleteButton = container.querySelector('.delete-btn');

    deleteButton.addEventListener('click', function() {



        // Remove the ticket item from localStorage
        const ticketsArray = JSON.parse(localStorage.getItem('selectedTickets') || "[]");
        console.log('Before delete:', ticketsArray);
        const index = ticketsArray.indexOf(String(item.ticketID)); //
        console.log('Item Ticket ID Type:', typeof item.ticketID);
        console.log('Array Types:', ticketsArray.map(ticket => typeof ticket));

        console.log('Index:', index);
        if (index > -1) {
            ticketsArray.splice(index, 1);
        }
        localStorage.setItem('selectedTickets', JSON.stringify(ticketsArray));
        console.log('After delete:', ticketsArray);

        // Remove the ticket item from the cart
        container.remove();
        // Update prices
        updatePrices();
        if(ticketsArray.length === 0) {
            document.getElementById('cart-placeholder').style.display = 'block';
        }
    });


    return container;
}

function isCreditCardValid(cardNumber,cardHolderName,dateStr,cvvStr) {
    try {
        if (!/^\d{16}$/.test(cardNumber) || !cardNumber) {
            console.log(cardNumber);
            throw new Error("Invalid card number");
        }

        if (!cardHolderName) {
            throw new Error("Invalid card holder name");
        }

        const dateParts = dateStr.split("/");
        if (dateParts.length !== 2) {
            throw new Error("Invalid date format");
        }

        const month = parseInt(dateParts[0], 10);
        const year = parseInt(dateParts[1], 10);
        const currentDate = new Date().toISOString().split("T")[0].replace(/-/g, "/");
        const currentDateParts = currentDate.split("/");
        const currentMonth = parseInt(currentDateParts[1], 10);
        const currentYear = parseInt(currentDateParts[0].slice(-2), 10);

        if (
            year < currentYear ||
            month < 1 ||
            month > 12 ||
            (year === currentYear && month < currentMonth) ||
            !month ||
            !year
        ) {
            throw new Error("Invalid expiration date");
        }
        if (!cvvStr) {
            throw new Error("Invalid CVV");
        }

        const cvv = parseInt(cvvStr, 10);
        if (isNaN(cvv)) {
            throw new Error("Invalid CVV");
        }

        if (cvv < 100 || cvv > 999) {
            throw new Error("Invalid CVV");
        }
        return true;
    } catch (e) {
        // Using 'alert' for simplicity. In a real application, consider more user-friendly error displays.
        alert(e.message);
        return false;
    }
}

function isAddressValid(street, city, state, zipCode) {
    try {
        // Verify that the street is not empty
        if (!street) {
            throw new Error("Invalid street");
        }

        // Verify that the city is not empty
        if (!city) {
            throw new Error("Invalid city");
        }

        // Verify that the state is not empty
        if (!state) {
            throw new Error("Invalid state");
        }

        // Verify that the zip code is 5 digits
        if (!/^\d{5}$/.test(zipCode) || !zipCode) {
            throw new Error("Invalid zip code");
        }

        return true;
    } catch (e) {
        alert(e.message);
        return false;
    }
}

checkOutBtn.addEventListener('click', (e) => {
    console.log(localStorage);
    console.log(document.getElementById('total-price').textContent.substring(1));
    let cardNumber = document.getElementById('cnum').value;
    let cardHolderName = document.getElementById('cname').value;
    let dateStr = document.getElementById('exp').value;
    let cvvStr = document.getElementById('cvv').value;
    let street = document.getElementById('street').value;
    let city = document.getElementById('city').value;
    let state = document.getElementById('state').value;
    let zipCode = document.getElementById('zip').value;
    let truncatedCard = cardNumber.slice(0, 6) + '*'.repeat(cardNumber.length - 10) + cardNumber.slice(-4);
    console.log("truncatedCard: ", truncatedCard);
    let receiept = {
        "paymentID": null,
        "customerName": cardHolderName,
        "paymentDateTime":null,
        "paymentAmount":null,
        "truncatedCardNumber" : null,
        "deliveryAddress": street +", " + city + ", " + state+ ", " + zipCode,
        "ticketDetails": "",
    }
    receiept.truncatedCardNumber = truncatedCard;
    if ( !isAddressValid(street, city, state, zipCode) || !isCreditCardValid(cardNumber,cardHolderName,dateStr,cvvStr)){
        return;
    }
        let totalPrice = document.getElementById('total-price').textContent.substring(1);
        let paymentDate = new Date();
        console.log(paymentDate.toString())

        const ticketsArray = JSON.parse(localStorage.getItem('selectedTickets') || "[]");

        if (ticketsArray.length === 0) {
            document.getElementById('cart-placeholder').style.display = 'block';
            return;
        }
        let promises = [];

        ticketsArray.forEach(ticketID => {
            let data = {
                "ticketID": ticketID,
                "eventID": null,
                "ticketStatus": "SOLD",
                "ticketType": "Regular",
                "price": null,
                "userID": null
            }
            let promise = fetch(`http://localhost:8080/ticket/`, {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(data),
            })
                .then(response => response.text())
                .then(data=>{
                    receiept.ticketDetails = receiept.ticketDetails + '\n' + data;
                })
            promises.push(promise)
        });

        Promise.all(promises).then(results => {
            // 所有请求都完成后的处理
            results.forEach(data => {
                console.log(data); // 输出每个请求的结果
            });
            receiept.paymentAmount = totalPrice;
            receiept.paymentDateTime = paymentDate;
            let string
            let payment = {
                "paymentID": null,
                "paymentDate": paymentDate,
                "paymentStatus": "PAID",
                "card": {
                    "cardID": null,
                    "userID": null,
                    "card": null,
                    "cardNumber": cardNumber,
                    "expiryDate": null,
                    "cvv": null, // Do not store CVV in a real-world application
                    "cardHolderName": null,
                },
                "userID": null,
                "paymentAmount": totalPrice,
                "receipt": receiept,
            }
            fetch(`http://localhost:8080/payment/`, {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(payment),
            })
                .then(response => response.text())
                .then(data => {
                    // console.log(data);
                    // receiept.paymentID = data;
                    // const blob = new Blob([JSON.stringify(receiept)], {type: 'text/plain;charset=utf-8'});
                    //
                    // // 创建一个链接并将其指向 Blob
                    // const a = document.createElement('a');
                    // a.href = URL.createObjectURL(blob);
                    // a.download = "reciept.txt";
                    //
                    // // 将链接添加到页面并触发点击，然后移除链接
                    // document.body.appendChild(a);
                    // a.click();
                    // document.body.removeChild(a);
                    //
                    //
                    // // 释放 Blob URL
                    // URL.revokeObjectURL(a.href);

                    localStorage.removeItem('selectedTickets');
                    alert('Successfully Paid!');
                    window.location.href = '../homepage/index.html';
                })
        });
});

document.addEventListener('DOMContentLoaded', function() {
    const ticketsArray = JSON.parse(localStorage.getItem('selectedTickets') || "[]");

    if (ticketsArray.length === 0) {
        document.getElementById('cart-placeholder').style.display = 'block';
        return;
    }

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
let SHIPPING_COST = 2.99;  // digital goods don't have shipping costs

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
    const ticketsArray = JSON.parse(localStorage.getItem('selectedTickets') || "[]");
    if(ticketsArray.length === 0) {
        SHIPPING_COST = 0;
    }
    let totalPrice = subtotal * (1 + TAX_RATE) + SHIPPING_COST;

    console.log('Subtotal:', subtotal);
    console.log('Total Price:', totalPrice);


    document.getElementById('subtotal-price').textContent = `$${subtotal.toFixed(2)}`;
    document.getElementById('shipping-price').textContent = `$${SHIPPING_COST.toFixed(2)}`;
    document.getElementById('total-price').textContent = `$${totalPrice.toFixed(2)}`;
    document.getElementById('check-amt').textContent = `$${totalPrice.toFixed(2)}`;
}

