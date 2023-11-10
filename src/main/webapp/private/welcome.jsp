<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String gameId = (String) request.getAttribute("gameId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Welcome</title>
        <link rel="stylesheet" href="../css/bootstrap.css"/>
        <link rel="stylesheet" href="../css/style.css"/>
        <script type="text/javascript" src="../js/bootstrap.js"></script>
        <link 
            href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.css" 
            rel="stylesheet"  type='text/css'/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <style>
            .card1 {
                background: url("../assets/ecopiaGamma2.jpg");
                background-size: auto;
            }
            canvas {
                background: url("../assets/ecopiaGamma2.jpg");
                background-size: auto;
            }

            .playerStats {
                margin: auto;
                padding: 0;
                list-style: none;
                margin: 6px;
            }

            .playerStats li {
                display: block;
                height: 30px;
                line-height: 30px;
                color: #990e6e;
            }

            .playerStats li span {
                display: inline-block;
                width: 60%;
                color: #999;
            }

            .chat-window {
                position: fixed;
                bottom: 0;
                right: 26px;
                width: 29%;
                height: 52%; /* Adjusted height */
                max-height: 600px; /* Added maximum height */
                background: #f2f2f2;
                border-left: 1px solid #ccc;
                box-shadow: -2px 0 5px rgba(0,0,0,0.1);
                padding: 10px;
                box-sizing: border-box;
                display: flex;
                flex-direction: column;
                overflow-y: auto; /* Added scroll if content exceeds height */
            }

            .chat-history {
                flex-grow: 1;
                overflow-y: auto;
                border: 1px solid #ccc;
                padding: 10px;
                background: #fff;
            }

            input[type="text"] {
                width: 100%;
                padding: 5px;
                margin-top: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
                box-sizing: border-box;
            }

            button {
                width: 60px;
                padding: 5px 10px;
                border: none;
                border-radius: 5px;
                background: #007bff;
                color: #fff;
                margin-top: 5px;
                cursor: pointer;
            }

            label {
                font-weight: bold;
            }

            .sender-message {
                text-align: right; /* Align sender messages to the right */
                color: #007bff; /* Set color for sender messages */
            }
            .status_bg{
                background-image: url("../assets/img/status_bg.jpg");
                background-size: cover;
                background-repeat: no-repeat;
            }
        </style>

        <script type="application/javascript">
            $(document).ready(function () {
            setInterval(updateChatHistoryOnLoad, 1000);

            var messageInput = document.getElementById("chat-message");

            messageInput.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
            event.preventDefault(); // Prevents the default "Enter" behavior (submitting the form)
            sendMessage(); // Simulate a click on the send button
            }
            });
            });
        </script>
    </head>
    <body onload="updatePolitiesPeriodically(<%=gameId%>,${userAtr.id});">

        <!-- Your existing code -->

        <div class="row" style="margin:10px">
            <div class="col-md-8">
                <!--status bar-->
                <div class="row" style="margin-left: 0px">
                    <div class="col-6">
                        <div class="row status_bg" style="padding-top: 20px">
                            <div class="col-4 ">
                                <label class="form-label text-white"><i class="fa fa-calendar"></i> <span id="get_stat_year">  </span></label>
                            </div>
                            <div class="col-4 ">
                                <label class="form-label text-white"><i class="fa fa-users"></i> <span id="get_stat_pop">  </span></label>
                            </div>
                            <div class="col-4 ">
                                <label class="form-label text-white"><i class="fa fa-bar-chart"></i> <span id="get_stat_rate">  </span></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card " style="height:600px;width:900px">
                    <div class="card-body card1"></div>
                </div>

                <script>
                    var c = document.getElementById('newCanvas');
                    var ctx = c.getContext('2d');
                    ctx.fillStyle = '#7cce2b';
                    ctx.fillRect(0, 0, 450, 300);

                    ctx.fillStyle = '#7cce2c';
                    ctx.fillRect(300, 600, 450, 300);

                    function sendMessage() {
                        var messageWindow = document.getElementById("chat-message");
                        var message = messageWindow.value;
                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", "chat", true); // Adjust the URL as needed

                        // Set the request header
                        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                        // Define the callback function
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                if (xhr.status === 200) {
                                    // Message sent successfully, update chat history if needed

                                    // Assuming the server responds with updated messages
                                    var receivedMessages = JSON.parse(xhr.responseText);
                                    updateChatHistory(receivedMessages); // Call the function here
                                } else {
                                    // Handle error if needed
                                }
                            }
                        };

                        // Prepare the request body
                        var params = "gameId=${gameId}&senderId=${userId}&senderName=${userName}&receiverId=" + document.getElementById("sendTo").value + "&message=" + encodeURIComponent(message);
                        // Send the request
                        xhr.send(params);
                        messageWindow.value = '';
                    }

                    function updateChatHistoryOnLoad() {
                        var xhr = new XMLHttpRequest();
                        xhr.open("GET", "chat?gameId=${gameId}&senderId=${userId}", true);
                        // Set the request header
                        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                        // Define the callback function
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                if (xhr.status === 200) {
                                    // Message sent successfully, update chat history if needed

                                    // Assuming the server responds with updated messages
                                    var receivedMessages = JSON.parse(xhr.responseText);
                                    updateChatHistory(receivedMessages); // Call the function here
                                } else {
                                    // Handle error if needed
                                }
                            }
                        };

                        xhr.send();

                    }

                    function updateChatHistory(messages) {

                        var chatHistory = document.getElementById("chat-history");
                        chatHistory.innerHTML = "";
                        messages.forEach(function (message) {
                            var messageElement = document.createElement("div");

                            var senderLabel = document.createElement("strong");
                            if (message.senderId !== ${userId}) {
                                senderLabel.textContent = message.senderName + ": ";
                            }

                            var messageText = document.createTextNode(message.message);

                            messageElement.appendChild(senderLabel);
                            messageElement.appendChild(messageText);
                            if (message.senderId === ${userId}) {
                                messageElement.classList.add("sender-message"); // Add CSS class for sender message
                            }
                            chatHistory.appendChild(messageElement);
                        });
                    }
                </script>

                <div class="row m-2">
                    <c:forEach items="${players}" var="player">
                        <div class="col-md-2">
                            <div alt="Avatar" style="text-align: center">
                                <img class="rounded-circle shadow-1-strong "
                                     src="data:image/jpeg;base64,${player.photo}" alt="avatar"
                                     style="width: 50px;"/>
                                <p class="text-muted">${player.username}</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="col-md-4" style="padding-left: 40px" >
                <div class="card">
                    <div class="card-body">
                        <div class="col">
                            <div class="row">
                                <div class="col-10">
                                    <h4>Welcome Chief ${userAtr.username}</h4>
                                </div>
                                <div class="col-2">
                                    <img  src="../assets/img/swords.png" style="height: 50px">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <span>Game Speed:</span>
                                        </div>
                                        <div class="col-6">
                                            <div class="form-group">
                                                <input type="range" class="form-control-range" id="game_speed" min="1" max="10"  value="${speed_session}" onchange="update_game_speed(<%=gameId%>,${userAtr.id})">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-6">
                                            <span>Population Growth</span>
                                        </div>
                                        <div class="col-6">
                                            <div class="form-group">
                                                <input type="range" class="form-control-range" id="pop_speed" min="-3" max="3"  value="${pop_speed_session}" onchange="update_pop_speed(<%=gameId%>,${userAtr.id})">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-6">
                                            <span>Tax Rate:</span>
                                        </div>
                                        <div class="col-6">
                                            <div class="form-group">
                                                <input type="range" class="form-control-range" id="formControlRange">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>   

                        </div>
                        <p>This is to show game statistics.</p>
                        <div class="row" style="margin:10px; justify-content: right">
                            <a type="button" class="btn btn-info" style="margin-right: 10px" data-toggle="modal" data-target="#exampleModal">Leaderboard</a>
                            <a type="button" class="btn btn-primary" href="${profileAtr}">Profile</a>
                            <div style="padding-left:10px">
                                <a type="button" href="LogoutServlet" class="btn btn-danger"><i class="fa fa-sign-out" aria-hidden="true"></i>
                                    Logout</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">LeaderBoard</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="container">
                            <div class="alert alert-primary" role="alert">
                                <div class="alert-dot"></div> Real-time data updating. Stay connected for the latest information about players
                            </div>
                        </div>

                        <div class="leaderboard">
                            <!--                            <div class="leaderboard-heading">Leaderboard</div>-->
                            <div class="leaderboard-item">
                                <div class="leaderboard-rank">1</div>
                                <div class="leaderboard-username" id="player_1">User 1</div>
                                <div class="leaderboard-score" id="player_1_pop">5000 points</div>
                            </div>
                            <div class="leaderboard-item">
                                <div class="leaderboard-rank">2</div>
                                <div class="leaderboard-username" id="player_2">User 2</div>
                                <div class="leaderboard-score" id="player_2_pop">4500 points</div>
                            </div>
                            <div class="leaderboard-item">
                                <div class="leaderboard-rank">3</div>
                                <div class="leaderboard-username" id="player_3">-</div>
                                <div class="leaderboard-score" id="player_3_pop">-</div>

                            </div>
                            <div class="leaderboard-item">
                                <div class="leaderboard-rank">4</div>
                                <div class="leaderboard-username" id="player_4">-</div>
                                <div class="leaderboard-score" id="player_4_pop">-</div>
                            </div>
                            <!-- Add more leaderboard items as needed -->
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="chat-window">
            <div class="chat-history" id="chat-history"></div>
            <input type="text" id="chat-message" placeholder="Type a message...">
            <label>Sent To:</label>
            <select id="sendTo" style="width: 30%;">
                <option value="-1">All Players</option>
                <c:forEach items="${players}" var="player">
                    <c:if test="${player.id != userId}">
                        <option value="${player.id}">${player.username}</option>
                    </c:if>
                </c:forEach>
            </select>
            <button onclick="sendMessage()">Send</button>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <script src="../js/game.js"></script>
    </body>
</html>
