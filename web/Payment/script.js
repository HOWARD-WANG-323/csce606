$(document).ready(function(){

    $('.radio-group .radio').click(function(){
        $('.radio').addClass('gray');
        $(this).removeClass('gray');
    });

    $('.plus-minus .plus').click(function(){
        var count = $(this).parent().prev().text();
        $(this).parent().prev().html(Number(count) + 1);
    });

    $('.plus-minus .minus').click(function(){
        var count = $(this).parent().prev().text();
        $(this).parent().prev().html(Number(count) - 1);
    });

});
// script.js
document.addEventListener('DOMContentLoaded', function() {
    fetch('/get-ticket-data')
        .then(response => response.json())
        .then(data => {
            const ticketItemsContainer = document.getElementById('ticket-items-container');
            data.forEach(item => {
                const itemHtml = `
                    <div class="row d-flex justify-content-center border-top">
                        <div class="col-5">
                            <div class="row d-flex">
                                <div class="book">
                                    <img src="${item.image_url}" class="book-img">
                                </div>
                                <div class="my-auto flex-column d-flex pad-left">
                                    <h6 class="mob-text">${item.title}</h6>
                                    <p class="mob-text">${item.author}</p>
                                </div>
                            </div>
                        </div>
                        <div class="my-auto col-7">
                            <div class="row text-right">
                                <div class="col-4">
                                    <p class="mob-text">${item.format}</p>
                                </div>
                                <div class="col-4">
                                    <div class="row d-flex justify-content-end px-3">
                                        <p class="mb-0" id="cnt${item.id}">1</p>
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
                    </div>`;
                ticketItemsContainer.insertAdjacentHTML('beforeend', itemHtml);
            });
        })
        .catch(error => console.error('Error fetching ticket data:', error));
});
