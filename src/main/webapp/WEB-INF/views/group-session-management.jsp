<%-- 
    Document   : group-session-management
    Created on : Jan 26, 2016, 15:09:34 AM
    Author     : floriano
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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

        <title>Sessioni esercizi gruppi</title>

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
        <link rel="stylesheet" href="resources/jumbotron.css">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <script type="text/javascript" src="resources/js/hello.js"></script>
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="adminhome">                                
                                <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="adminhome">                                
                                <span class="glyphicon glyphicon-home fa-2x" data-toggle="tooltip" data-placement="bottom" title="Home"></span>
                            </a>
                        </li>
                        <li>
                            <a href="logout">
                                <span class="glyphicon glyphicon-log-out fa-2x" data-toggle="tooltip" data-placement="bottom" title="Logout"></span>
                            </a>
                        </li>
                        
                    </ul>
                    <ul class="nav nav-pills pull-left">
                        <li role="presentation">
                            <a data-toggle="modal" href="#infoModal">
                                <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            <div class="jumbotron">

                <p class="lead"><strong>Esercizi per gruppi</strong></p>
                
                <c:if test="${!empty message}">
                    <div class="well">
                        <p class="lead"><strong>Attenzione!</strong></p>
                        <p class="lead alert alert-danger"><strong>${message}</strong></p>
                    </div>
                </c:if>
                
                <p class="lead">
                    <a class="btn btn-lg btn-success
                       <c:if test="${newsession=='false'}">
                             disabled
                       </c:if>"
                       href="groupsession" role="button">Configura sessione</a>
                </p>
                       
                <c:if test="${!empty hospitalsessions}">
                    <p></p><hr>
                    <span class="fa fa-medkit fa-2x"></span>
                    <span class="lead"><strong>Sessioni attive in ospedale</strong></span>

                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th>Id</th>
                                    <th>Gruppo</th>
                                    <th>Difficolt&agrave;</th>
                                    <th>Esercizi</th>
                                </tr>
                            </thead>
                            <c:forEach var="session" items="${hospitalsessions}" varStatus="status">
                                <tr>
                                    <td>
                                        <button
                                            class="btn btn-success"
                                            onclick="
                                                    bootbox.confirm({
                                                        size:'small',
                                                        buttons: {
                                                            confirm: {
                                                                label: 'Si',
                                                                className: 'btn-success'
                                                            },
                                                            cancel: {
                                                                label: 'No',
                                                                className: 'btn-danger'
                                                            }
                                                        },
                                                        message:'<h4>Assegnare gli esercizi per casa?</h4>',
                                                        callback: function(result) {
                                                            if(result===true) {
                                                                this.disabled = true;
                                                                post('buildhomesession', {
                                                                    sessionid: '${session.id}',
                                                                    forpatient: false
                                                                }
                                                                );
                                                            }
                                                        }
                                                    }
                                                );"
                                            style="pointer-events:auto"
                                            data-toggle="tooltip" data-placement="bottom" title="Assegna esercizi per casa">                                          
                                            <span class="fa fa-home fa-2x"></span>
                                        </button>
                                    </td>
                                    <td>                                    
                                        <button class="btn btn-danger"
                                                onclick="
                                                    bootbox.confirm({
                                                        size:'small',
                                                        buttons: {
                                                            confirm: {
                                                                label: 'Si',
                                                                className: 'btn-danger'
                                                            },
                                                            cancel: {
                                                                label: 'No',
                                                                className: 'btn-success'
                                                            }
                                                        },
                                                        message:'<h4>Vuoi veramente cancellare la sessione?</h4>',
                                                        callback:function(result) {
                                                            if(result===true) {
                                                                this.disabled = true;
                                                                post('deletesession',
                                                                {
                                                                    sessionid: '${session.id}',
                                                                    forpatient: false
                                                                })
                                                            }
                                                        }
                                                    });">

                                            <span class="fa fa-times fa-2x"></span>
                                        </button>

                                    </td>

                                    <td>${session.id}</td>

                                    <td>${hospitalgroupnames[status.index]}</td>

                                    <c:if test="${session.difficulty=='easy'}">
                                        <td><span class="glyphicon glyphicon-star"></span></td>
                                        </c:if>
                                        <c:if test="${session.difficulty=='medium'}">
                                        <td><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></td>
                                            </c:if>
                                            <c:if test="${session.difficulty=='difficult'}">
                                        <td><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></td>
                                            </c:if>
                                    <td>
                                        <button
                                            class="btn btn-primary"
                                            onclick="showexercises('${hospitalexercisenames[status.index]}')">
                                            <span class="fa fa-eye fa-2x"></span>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </c:if>
                <c:if test="${!empty homesessions}">
                    <p></p><hr>
                    <span class="fa fa-home fa-2x"></span>
                    <span class="lead"><strong>Sessioni da completare a casa</strong></span>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Id</th>
                                    <th>Gruppo</th>
                                    <th>Difficolt&agrave;</th>
                                    <th>Esercizi</th>
                                </tr>
                            </thead>
                            <c:forEach var="session" items="${homesessions}" varStatus="status">
                                <tr>
                                    <td>                                    
                                        <button class="btn btn-danger"
                                                onclick="
                                                    bootbox.confirm({
                                                        size:'small',
                                                        buttons: {
                                                            confirm: {
                                                                label: 'Si',
                                                                className: 'btn-danger'
                                                            },
                                                            cancel: {
                                                                label: 'No',
                                                                className: 'btn-success'
                                                            }
                                                        },
                                                        message:'<h4>Vuoi veramente cancellare la sessione?</h4>',
                                                        callback:function(result) {
                                                            if(result===true) {
                                                                this.disabled = true;
                                                                post('deletesession',
                                                                {
                                                                    sessionid: '${session.id}',
                                                                    forpatient: false
                                                                })
                                                            }
                                                        }
                                                    });">

                                            <span class="glyphicon glyphicon-remove" style="font-size: 20px"></span>
                                        </button>

                                    </td>

                                    <td>${session.id}</td>

                                    <td>${homegroupnames[status.index]}</td>

                                    <c:if test="${session.difficulty=='easy'}">
                                        <td><span class="glyphicon glyphicon-star"></span></td>
                                        </c:if>
                                        <c:if test="${session.difficulty=='medium'}">
                                        <td><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></td>
                                            </c:if>
                                            <c:if test="${session.difficulty=='difficult'}">
                                        <td><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></td>
                                            </c:if>
                                    <td>
                                        <button
                                            class="btn btn-primary"
                                            onclick="showexercises('${homeexercisenames[status.index]}')">
                                            <span class="glyphicon glyphicon-eye-open"style="font-size: 20px"></span>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                </c:if>

                
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div>


        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-grp-sess-man.jsp"/>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
            
        function showexercises(exerciseNames) {
                    
            var aa =
                    exerciseNames.substring(1, exerciseNames.length-1);
            var array = aa.split(",")

            var arrayLength = array.length;
            var message="<h4><b>";
            for (var i = 0; i < arrayLength; i++) {
                message += array[i];
                message += "<br>"
            }
            message += "</b></h4>"
            
            bootbox.alert({
                title: "Esercizi assegnati",
                message: message
            });
        }     
            
        </script>

        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();   
            });
        </script>
        
    </body>
</html>