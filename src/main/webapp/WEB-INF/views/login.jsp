<%-- 
    Document   : index
    Created on : Feb 16, 2016, 9:49:34 AM
    Author     : floriano
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html lang="it">
    <head>    
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Floriano Zini">
        <link rel="icon" href="resources/favicon.ico">

        <!--title>Riabilitazione cognitiva</title-->
              <title>Training cognitivo</title>
        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        <!--link rel="stylesheet" href="resources/signin.css"-->

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>
        <div class="container">
            <div class="jumbotron">
                <div class="row">
                    <div class="col-lg-4">
                        <img class="img-circle" src="resources/images/logo.jpg" width="120" height="120" alt="logo">
                    </div>
                    <div class="col-lg-4">
                        <!--h1>Riabilitazione cognitiva</h1-->
                             <h1>Training cognitivo</h1>
                    </div>
                </div>
                <p>
                <p class="lead"><h3>Per cortesia, inserisci le tue credenziali di accesso</h3></p>
                <form role="form" method="post" action="login">
                    <!--label for="id" class="sr-only">Id</label>
                    <input type="text" id="id" class="form-control" placeholder="Identificativo" name="id" required autofocus-->
                    
                    <label for="username" class="sr-only">username</label>
                    <input type="text" username="username" class="form-control" placeholder="Identificativo" name="username" required autofocus>
               
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" class="form-control" placeholder="Password" name="password" required>
                    <p></p>
                    <input type="submit" class="btn btn-primary" value="Accedi">
                </form>
                <p class="text-danger"><b>${message}</b></p>
                <div><h4><a href="credential-forgotten.jsp">Ho perso le credenziali</a></h4></div>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script type="text/javascript" src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
    </body>
</html>