<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Register | Ecopia</title>

	<style>

.required:after {
  content:" *";
  color: red;
}


*{
    margin: 0px;
    padding: 0px;
    box-sizing: border-box;
    font-family: 'Poppins', sans-serif;
}

.flex-r ,.flex-c {
    justify-content: center;
    align-items: center;
    display: flex;
}
.flex-c{
    flex-direction: column;
}
.flex-r{
    flex-direction: row;
}

.container{
    width: 100%;
    min-height: 100vh;
    padding: 20px 10px;
    background: #E5E5E5;
}



.login-text{
    background-color: #F6F6F6;
    width: 500px;
    min-height: 500px;
    border-radius: 10px;
    padding: 10px 20px;
}

.logo{
    margin-bottom: 20px;
}
.logo span , .logo span i{
    font-size: 25px;
    color:#0d8aa7 ;
}

.login-text h1{
    font-size: 25px;
}
.login-text p{
    font-size: 15px;
    color: #000000B2;
}

form{
    align-items: flex-start !important;
    width: 100%;
    margin-top: 15px;
}

.input-box{
    margin: 10px 0px;
    width: 100%;
}

.label{
    font-size: 15px;
    color: black;
    margin-bottom: 3px;
}

.input{
    background-color: #F6F6F6;
    padding: 0px 5px;
    border: 2px solid rgba(216, 216, 216, 1);
    border-radius: 10px;
    overflow: hidden;
    justify-content: flex-start;
}

input{
    border: none;
    outline: none;
    padding: 10px 5px;
    background-color: #F6F6F6;
    flex: 1;
}
.input i{
    color:rgba(0, 0, 0, 0.4);
}

.check span{
    color:#000000B2;
    font-size: 15px;
    font-weight: bold;
    margin-left: 5px;
}

.btn{
    color: #ffffff;
    border-radius: 30px;
    padding: 10px 15px;
    background: linear-gradient(122.33deg, #68bed1 30.62%, #1E94E9 100%);
    margin-top: 10px;
    margin-bottom: 10px;
    font-size: 16px;
    transition: all 0.3s linear;
}

.btn:hover{
    transform: translateY(-2px);
}
.extra-line{
    font-size: 15px;
    font-weight: 600;
}
.extra-line a{
    color: #0095B6;
}
	</style>

</head>

<body>
    <div class=" flex-r container">
        <div class="flex-r login-wrapper">
            <div class="login-text">
                <div class="logo">
                    <span>Ecopia</span>
                </div>
                <h1>Register</h1>
                <p>Please Register to play the game!</p>

                <form class="flex-c" action="private/loginRegister" method="post">

                    <h4 style="color: red"><c:out value = "${errorMessage}"/></h4>
                    <div class="input-box">
                        <b><span class="label required">User Name</span></b>
                        <div class=" flex-r input">
                            <input type="text" name="username" placeholder="Enter Username" required>
                        </div>
                    </div>

                    <div class="input-box">
                        <b><span class="label required">Password</span></b>
                        <div class="flex-r input">
                            <input type="password" name="password1" placeholder="Enter Password" required>
                        </div>
                    </div>

                    <div class="input-box">
                        <b><span class="label required">Email</span></b>
                        <div class=" flex-r input">
                            <input type="text" name="email"  placeholder="Enter Email" required>
                        </div>
                    </div>

                    <div class="input-box">
                        <b><span class="label">Notes</span></b>
                        <div class=" flex-r input">
                            <input type="text" name="notes" placeholder="Enter Notes">
                        </div>
                    </div>

                    <input class="btn" type="submit" name="submit" value="Register">
                </form>

            </div>
        </div>
    </div>
</body>

</html>