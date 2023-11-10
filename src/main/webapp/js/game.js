/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function load_polities(id) {

    update_polities_data(id);
    console.log(id);

    fetch(`../getPolities?gameId=${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Parse the response as JSON
            })
            .then(data => {
                // Handle the received JSON data here
                console.log(data);
                updatePolities(data);
            })
            .catch(error => {
                // Handle errors
                console.error('Fetch error:', error);
            });
}

// Function to update the polities
function updatePolities(data) {
    const cardContainer = document.querySelector('.card1');
    const existingPolities = Array.from(cardContainer.getElementsByClassName('polity'));

    for (const p of data) {
        const existingPolity = existingPolities.find(polity => polity.dataset.id === p.polityId);
        if (existingPolity) {
            // Update existing polity
            existingPolity.style.left = p.x + 'px';
            existingPolity.style.top = p.y + 'px';
            // Update any other properties as needed
        } else {
            // Create and append a new polity element
            const polityElement = createPolityElement(p);
            cardContainer.appendChild(polityElement);
        }
    }

    // Remove polities that are no longer present in the data
    for (const existingPolity of existingPolities) {
        const existingPolityId = existingPolity.dataset.id;
        if (!data.some(p => p.polityId === existingPolityId)) {
            cardContainer.removeChild(existingPolity);
        }
    }
}

// Function to create a polity element
function createPolityElement(polity) {
    const polityElement = document.createElement('div');
    polityElement.className = 'polity';
    polityElement.style.height = polity.height + 'px';
    polityElement.style.width = polity.width + 'px';
    polityElement.style.backgroundColor = '';
    polityElement.style.border = '2px solid black';
    polityElement.style.position = 'absolute';
    polityElement.style.left = polity.x + 'px';
    polityElement.style.top = polity.y + 'px';
    polityElement.dataset.id = polity.polityId; // Store an identifier to match with data
    polityElement.style.textAlign = 'center';

    // Create a text node and append it to the div element
    const textNode = document.createTextNode(polity.username+polity.polityId);
    polityElement.appendChild(textNode);

    return polityElement;
}

function clearCard() {
    const card1 = document.querySelector('.card1');
    card1.innerHTML = ''; // Clear the content of the card
}

let initPlayerDetailsExecuted = false;

function updatePolitiesPeriodically(id, playerId) {
    load_polities(id); // Initial load
    if (!initPlayerDetailsExecuted) {
        init_player_details(playerId, id);
        update_player_game_stats(id, playerId);
        initPlayerDetailsExecuted = true; // Set the flag to indicate it has been executed
    }
    const interval = 1000; // 5 seconds in milliseconds
    setInterval(() => {
        load_polities(id); // Reload the polities data
        update_player_game_stats(id, playerId);
        update_leaderboard(id);
    }, interval);
}

function update_polities_data(id) {
    $.post("../UpdateGamePolities", {action: "updatePolities", gameId: id}, function () {
        // Optional: You can update the year count after changing the speed
    });
}

function init_player_details(playerId, gameId) {
    $.post("../init_player_details", {action: "init_details", playerId: playerId, gameId: gameId}, function () {
        // Optional: You can update the year count after changing the speed
    });
}

function update_game_speed(gameId, player) {

    var speed = $("#game_speed").val();

    $.post("../UpdateGameControls", {action: "changeGameSpeed", speed: speed, gameId: gameId, playerId: player}, function () {
        // Optional: You can update the year count after changing the speed
    });

}

function update_pop_speed(gameId, player) {

    var speed = $("#pop_speed").val();
    $.post("../UpdateGameControls", {action: "changePopSpeed", speed: speed, gameId: gameId, playerId: player}, function () {
        // Optional: You can update the year count after changing the speed
    });
}

function update_player_game_stats(gameId, playerId) {
    $.post("../UpdatePlayerGameStats", {action: "update_stats", gameId: gameId, playerId: playerId}, function (data) {
        var set_year = document.getElementById("get_stat_year");
        var set_population = document.getElementById("get_stat_pop");
        var set_growth_rate = document.getElementById("get_stat_rate");
        try {
            var year = data[0].year;
            var population = data[0].population;
            var growth_rate = data[0].growth_rate;

            // Function to animate a number from startValue to endValue
            function animateNumber(element, startValue, endValue, duration) {
                if (!isNaN(startValue) && !isNaN(endValue)) {
                    var interval = 100; // Update every 100ms
                    var steps = duration / interval;
                    var stepCount = 0;
                    var stepValue = (endValue - startValue) / steps;
                    var currentValue = startValue;

                    var animationInterval = setInterval(function () {
                        currentValue += stepValue;
                        element.innerHTML = Math.round(currentValue); // Round for a whole number

                        stepCount++;

                        if (stepCount >= steps) {
                            clearInterval(animationInterval);
                        }
                    }, interval);
                } else {
                    // If startValue or endValue is NaN, just set the final value
                    element.innerHTML = endValue;
                }
            }

            animateNumber(set_year, parseFloat(set_year.innerHTML), year, 1000); // 1000ms (1 second) animation
            animateNumber(set_population, parseFloat(set_population.innerHTML), population, 1000);
            animateNumber(set_growth_rate, parseFloat(set_growth_rate.innerHTML), growth_rate, 1000);
        } catch (e) {
            console.error("JSON parsing error:", e);
        }
    })
            .done(function () {
                console.log("Request successfully completed");
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Request failed:", textStatus, errorThrown);
            });
}

function update_leaderboard(gameId) {

    $.post("../update_leaderboard", {action: "update_leaderboard", gameId: gameId}, function (data) {

        var player_1 = document.getElementById("player_1");
        var player_1_pop = document.getElementById("player_1_pop");
        var player_2 = document.getElementById("player_2");
        var player_2_pop = document.getElementById("player_2_pop");
        var player_3 = document.getElementById("player_3");
        var player_3_pop = document.getElementById("player_3_pop");
        var player_4 = document.getElementById("player_4");
        var player_4_pop = document.getElementById("player_4_pop");

        try {

            console.log(data);

            if (data[0].username) {
                player_1.innerHTML = data[0].username;
                player_1_pop.innerHTML = data[0].population;
            }
            if (data[1].username) {
                player_2.innerHTML = data[1].username;
                player_2_pop.innerHTML = data[1].population;
            }
            if (data[2].username) {
                player_3.innerHTML = data[2].username;
                player_3_pop.innerHTML = data[2].population;
            }
            if (data[3].username) {
                player_4.innerHTML = data[3].username;
                player_4_pop.innerHTML = data[3].population;
            }

        } catch (e) {
            console.error("JSON parsing error:", e);
        }
    })
            .done(function () {
                console.log("Request successfully completed");
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Request failed:", textStatus, errorThrown);
            });
}




