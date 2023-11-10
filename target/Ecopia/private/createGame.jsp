<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
    <nav class="navbar navbar-expand-lg navbar-light bg-info fixed-top">
        <div class="container">
            <h3 class="navbar-brand" href="#" style="color:white">Invite Players</h3>
            <!-- <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span> -->
            </button>
            <a type="button" href="LogoutServlet" class="btn btn-danger"><i class="fa fa-sign-out" aria-hidden="true"></i>
                Logout</a>
            <!-- <div class="collapse navbar-collapse" id="navbarResponsive">
              <ul class="navbar-nav ms-auto">
                <li class="nav-item active">
                  <a class="nav-link" href="#">Home</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="#">About</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="#">Services</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="#">Contact</a>
                </li>
              </ul>
            </div> -->
        </div>
    </nav>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <link rel="stylesheet" href="../css/bootstrap.css"></link>
        <script type="text/javascript" src="../js/bootstrap.js"></script>
        <title>Invite Players | Ecopia</title>

        <!-- #datatables files -->
        <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
        <!-- #end -->
        <script>
            $(document).ready(function () {
                $('#example').DataTable(
                        {
                            "aLengthMenu": [[5, 10, 25, -1], [5, 10, 25, "All"]],
                            "iDisplayLength": 5,
                            "processing": true
                        });
            });
            var set = new Set();
            ;
            function invitePlayers(flag) {
                document.getElementById("command").value = "";
                var checked_rows = $('input.dt-checkboxes:checkbox:checked').parents("tr");
                $.each(checked_rows, function (key, val) {
                    set.add($(this).attr('id'));
                });
                if (set.size < 4) {
                    var plyrs = Array.from(set).join(',');
                    document.getElementById("plyrs").value = plyrs;
                    if (flag && set.size > 0) {
                        console.log("waawwa");
                        document.getElementById("command").value = "make_game";
                        document.getElementById("createGame").submit();
                    }
                } else {
                    alert("Maximum four players allowed to play the game");
                    $('#example').DataTable().$('.dt-checkboxes').prop('checked', false);
                    set.clear();
                    return false;
                }
            }

            function startGame(gameId, isGameStarted) {
                document.getElementById("command").value = "startGame";
                document.getElementById("gameId").value = gameId;
                document.getElementById("isGameStarted").value = isGameStarted;
                document.getElementById("createGame").submit();
            }
        </script>

        <style>


            .required:after {
                content:" *";
                color: red;
            }
        </style>

    </head>

    <body>

        <form class="flex-c" action="createGame" id="createGame" method="post">
            <input type="hidden" name="command" id="command">
            <input type="hidden" name="plyrs" id="plyrs" value="">
            <input type="hidden" name="gameId" id="gameId" value="">
            <input type="hidden" name="isGameStarted" id="isGameStarted" value="">
            <div class="container" style="margin-top:80px" >
                <h5 style="text-align-last: end;"><b><a href="${profileAtr}">${userAtr.username}</a></b> Welcome to Ecopia!</h5>

                <c:if test="${ not empty successMessage}">
                    <div class="row alert alert-success" style="margin-top:20px"><h5 role="alert" style="color: green"><c:out value = "${successMessage}"/></h5></div>
                    </c:if>
                <div class="row" >
                    <div class="col-md-6">
                        <table id="example" class="table table-striped table-bordered" style="width:100%">
                            <thead>
                                <tr>
                                    <!-- <th><input type="checkbox" onclick="checkAll(this)"></th> -->
                                    <th>Select</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Notes</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var = "userId" scope = "session" value = "${userAtr.id}"/>
                                <c:forEach items="${users}" var="user">
                                    <c:if test="${user.id != userId}">
                                        <tr id = "${user.id}">
                                            <td><input type="checkbox" class="dt-checkboxes" name="player" id="player" onclick="invitePlayers(false)"></td>
                                            <td>${user.username}</td>
                                            <td>${user.email}</td>
                                            <td>${user.notes}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                            <!-- <tfoot>
                                <tr>
                                    <th></th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Notes</th>
                                </tr>
                            </tfoot> -->
                        </table>
                    </div>
                    <div class="col-md-6">
                        <div class="row">
                            <c:if test="${fn:length(invitations) > 0}">
                                <div class="card" style="width:100%">
                                    <div class="card-header">
                                        Invitations
                                    </div>
                                    <c:forEach items="${invitations}" var="invitation">
                                        <div class="card-body">
                                            <blockquote class="blockquote mb-0">
                                                <c:if test="${invitation.invitedUserId != userId}">
                                                    <p><b>${invitation.invitedUserName}</b> Invited you to play the game</p>
                                                </c:if>
                                                <c:if test="${invitation.invitedUserId == userId}">
                                                    <p>Start playing the game (You have created this game)</p>
                                                    <button type="button" onclick="startGame(${invitation.gameId}, ${invitation.isGameStarted})" class="btn"><i class="fa fa-play" style="color:green" ></i></button>
                                                    </c:if>
                                                    <c:if test="${invitation.invitedUserId != userId}">
                                                        <c:if test="${invitation.isGameStarted}">
                                                        <button type="button" onclick="startGame(${invitation.gameId}, ${invitation.isGameStarted})" class="btn"><i class="fa fa-play" style="color:green" ></i></button>
                                                        </c:if>
                                                        <c:if test="${!invitation.isGameStarted}">
                                                        <button type="button"  class="btn"><i class="fa fa-play" style="color:green" ></i></button>
                                                        </c:if>
                                                    </c:if>
                                                <button class="btn"><i class="fa fa-close" style="color:red" ></i></button>
                                            </blockquote>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <c:if test="${fn:length(invitations) == 0}">
                                <div class="card" style="width:100%">
                                    <div class="card-header">
                                        <b>No!</b> New game Invitations found
                                    </div>
                                    <div class="card-body">
                                        <blockquote class="blockquote mb-0">
                                            <p>You can start playing the game by inviting players!</p>
                                        </blockquote>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                    </div>

                </div>
                <div class="row mt-2">
                    <button type="button" class="btn btn-success" onclick="invitePlayers(true)">Create Game and Invite Players</button>
                    <!-- <button type="button" class="btn btn-secondary ml-2">Cancel</button> -->
                </div>
                <hr>
                <div class="row mt-2">
                    <div class="card" style="width:100%">
                        <div class="card-header">
                            How to play Ecopia!
                        </div>
                        <div class="card-body">
                            <blockquote class="blockquote mb-0">
                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
                                <!-- <footer class="blockquote-footer">Someone famous in <cite title="Source Title">Source Title</cite></footer> -->
                            </blockquote>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>

</html>