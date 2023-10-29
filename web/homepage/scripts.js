// Mocked database result, this would be your actual data from the database.
const eventsFromDatabase = [
    {
        num: '23',
        day: 'Feb',
        eventType: 'Music Event',
        title: 'Concert 1',
        dateDetails: 'Monday 15th 2016 <br/> 15:20Pm & 11:00Am',
        locationDetails: 'Shanghai',
        buttonLabel: 'View Tickets' //
    },
    // ... other events
];

// Function to create the event item HTML
function createEventItem(event) {
    return `
    <div class="item">
        <div class="item-right">
            <h2 class="num">${event.num}</h2>
            <p class="day">${event.day}</p>
            <span class="up-border"></span>
            <span class="down-border"></span>
        </div> 
        <div class="item-left">
            <p class="event">${event.eventType}</p>
            <h2 class="title">${event.title}</h2>
            <div class="sce">
                <div class="icon">
                    <i class="fa fa-table"></i>
                </div>
                <p>${event.dateDetails}</p>
            </div>
            <div class="fix"></div>
            <div class="loc">
                <div class="icon">
                    <i class="fa fa-map-marker"></i>
                </div>
                <p>${event.locationDetails}</p>
            </div>
            <div class="fix"></div>
            <button class="${event.buttonLabel.toLowerCase()}">${event.buttonLabel}</button>
        </div>
    </div>`;
}

// Append events to the DOM
document.addEventListener('DOMContentLoaded', function() {
    const eventList = document.querySelector('.event-list');

    eventsFromDatabase.forEach(event => {
        eventList.innerHTML += createEventItem(event);
    });
});

document.addEventListener('DOMContentLoaded', function() {

    var currentDate = new Date();
    var hour = currentDate.getHours();
    var timePeriod;

    if (hour >= 0 && hour < 6) {
        timePeriod = "night";
    } else if (hour >= 6 && hour < 12) {
        timePeriod = "morning";
    } else if (hour >= 12 && hour < 18) {
        timePeriod = "afternoon";
    } else {
        timePeriod = "evening";
    }

    document.getElementById('time-period').textContent = timePeriod;

    // Fetch user fullName from the server
    fetchUserFullName();

});

function fetchUserFullName() {
    fetch('http://localhost:8080/getUserName', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            // 检查响应的内容类型
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json();
            } else {
                throw new TypeError("Oops, we didn't get JSON!");
            }
        })
        .then(data => {
            if (data && data.fullName) {
                document.getElementById('user-fullname').textContent = data.fullName;
            } else if (data && data.error) {
                console.error(data.error);
            }
        })
        .catch(error => console.error('Error:', error));
}
