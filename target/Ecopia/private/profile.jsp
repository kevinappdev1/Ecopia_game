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
    <h3 class="navbar-brand" href="#" style="color:white">Update Profile</h3>
    <a type="button" href="LogoutServlet" class="btn btn-danger"><i class="fa fa-sign-out" aria-hidden="true"></i>
    Logout</a>
  </div>
</nav>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="../css/bootstrap.css"></link>
    <script type="text/javascript" src="../js/bootstrap.js"></script>
    <title>Profile | Ecopia</title>

    <!-- #datatables files -->
    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
    <!-- #end -->
</head>
<body>
    <form class="flex-c" action="uploadServlet" id="loginRegister" method="post" enctype="multipart/form-data">
    <input type="hidden" name="command" id="command" value="UPDATE">
    <input type="hidden" name="userId" value="${userAtr.id}" />
    <h4 style="color: red"><c:out value = "${errorMessage}"/></h4>

    <div class="container" style="margin-top:80px" >
    	<div class="row mt-2">
            <div class="card" style="width:100%">
              <div class="card-header">
                Update Profile
              </div>
              <div class="card-body form-group">
                    <h4 style="color: red"><c:out value = "${errorMessage}"/></h4>
                    <div class="mt-2">
                        <b><span class="label required">User Name</span></b>
                        <div class=" flex-r input">
                            <input class="form-control" type="text" name="username" placeholder="Enter Username" value="${userAtr.username}" required>
                        </div>
                    </div>

                    <div class="mt-2">
                        <b><span class="label required">Email</span></b>
                        <div class=" flex-r input">
                            <input class="form-control" type="text" name="email"  placeholder="Enter Email" required value="${userAtr.email}">
                        </div>
                    </div>

                    <div class="mt-2">
                        <b><span class="label">Notes</span></b>
                        <div class=" flex-r input">
                            <input class="form-control" type="text" name="notes" placeholder="Enter Notes" value="${userAtr.notes}">
                        </div>
                    </div>
                  <div class="mt-2">
                      <b><span class="label">Existing Logo</span></b>
                      <div class=" flex-r input">
                          <img style="width: 100px" src="data:image/jpeg;base64,${userAtr.photo}" alt="Image">
                      </div>
                  </div>

                    <div class="mt-2">
                        <b><span class="label">Select Icon</span></b>
                       <input type="file" class="form-control-file" id="photo" name="photo" required>
                    </div>

                    <div class="mt-4">
                        <input class="btn btn-success"  type="submit" name="submit" value="Update">
                    </div>
              </div>
            </div>
        </div>
    </div>
    </form>
</body>

</html>