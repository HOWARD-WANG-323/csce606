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
