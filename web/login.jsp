<%-- 
    Document   : index
    Created on : 15-Mar-2010, 14:47:22
    Author     : bastinl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript">
        
        function checkname()
        {
            var name=document.getElementById( "newUsernameId" ).value;
            
            if(name){
                $.ajax({
                    url: 'Controller',
                    data: {
                      user_name:name,  
                    },
                    success: function (response) {
                        //var data = JSON.parse(response);
                        $('#name_status').text(response);
                    error:function() {
                        alert("error occured");
                    }
                });
            }
        }
    </script>
    <head>
       
        <title>Login / sign-up page</title>
    </head>
    <body>
        <h2>Please log in!</h2>
              <form method="POST" action="http://localhost:8084/coursework/do/login">
                Username:<input type="text" name="username" value="" />----
                Password:<input type="password" name="password" value="" />
               
        <input type="hidden" name="from" value="${pageContext.request.requestURI}"> 
        <input type="hidden" name="from" value="${param.from}">
        <input type="submit" value="Click to log in" />
        </form>
        
        <form method="POST" action="http://localhost:8084/coursework/do/addUser">
            <h2> Don't yet have an account? </h2>
            Username:<input id="newUsernameId" type="text" name="newUsername" value="" onkeyup="checkname();"/>----
            Password:<input type="password" name="newPassword" value="" />
            <div id="name_status"></div><br>
            <input type="submit" value="Sign up as a new user"/>
        </form>
        
    </body>
</html>
