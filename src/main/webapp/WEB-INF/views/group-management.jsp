<%-- 
    Document   : group-management
    Created on : Dec 20, 2016, 9:49:34 AM
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

        <title>Gestione gruppi</title>

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

                <p class="lead"><strong>Gestione gruppi</strong></p>

                <c:if test="${!empty message}">
                    <div class="well">
                        <p class="lead"><strong>Attenzione!</strong></p>
                        <p class="lead alert alert-danger"><strong>${message}</strong></p>
                    </div>
                </c:if>

                <div>
                    <p><a class="btn btn-lg btn-success" onclick="post('groupform')" role="button">Nuovo gruppo</a></p>
                </div>

                <c:if test="${empty message}">

                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th>Id</th>
                                    <th>Nome</th>
                                    <th><!--pazienti-->partecipanti</th>
                                    <th>Esercizi</th>
                                </tr>
                            </thead>
                            <c:forEach var="group" items="${activegroups}">
                                <tr>
                                    <td>
                                        <button disabled class="btn btn-primary"
                                                onclick="post('groupform',
                                                                {groupid: '${group.id}'})">
                                            <span class="glyphicon glyphicon-edit"
                                                  style="font-size: 20px"></span>
                                        </button>
                                    </td>
                                    <td>                                    
                                        <button disabled class="btn btn-danger"
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
                                                            message: '<h4>Vuoi veramente cancellare il gruppo?</h4>',
                                                            callback: function (result) {
                                                                if (result === true) {
                                                                    this.disabled = true;
                                                                    post('deletegroup',
                                                                            {groupid: '${group.id}'})
                                                                }
                                                            }
                                                        });">

                                            <span class="glyphicon glyphicon-remove" style="font-size: 20px"></span></button>

                                    </td>

                                    <td>${group.id}</td>

                                    <td>${group.name}</td>
                                    <td>
                                        <button
                                            class="btn btn-info"
                                            onclick="
                                                post('patientgroup', {
                                                    groupid: '${group.id}',
                                                    back: '${back}',
                                                    home: '${home}'
                                                }, 'get');"
                                            >
                                            <span class="glyphicon glyphicon-eye-open" style="font-size: 20px"></span>
                                        </button>
                                    </td>
                                    <td>
                                        <span class="glyphicon glyphicon-ok-circle" style="font-size: 20px"></span>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:forEach var="group" items="${notactivegroups}">
                                <tr>
                                    <td>
                                        <button class="btn btn-primary"
                                                onclick=
                                                    "post('groupform', {groupid: '${group.id}'})"
                                                    >
                                            <span class="glyphicon glyphicon-edit"
                                                  style="font-size: 20px"></span>
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
                                                            message: '<h4>Vuoi veramente cancellare il gruppo?</h4>',
                                                            callback: function (result) {
                                                                if (result === true) {
                                                                    this.disabled = true;
                                                                    post('deletegroup',
                                                                            {groupid: '${group.id}'})
                                                                }
                                                            }
                                                        });">

                                            <span class="glyphicon glyphicon-remove" style="font-size: 20px"></span></button>

                                    </td>

                                    <td>${group.id}</td>

                                    <td>${group.name}</td>
                                    <td>
                                        <button
                                            class="btn btn-info"
                                            onclick="
                                                post('patientgroup', {
                                                    groupid: '${group.id}',
                                                    cid: '${cid}',
                                                    back: '${back}',
                                                    home: '${home}'
                                                }, 'get');"
                                            >
                                            <span class="glyphicon glyphicon-eye-open" style="font-size: 20px"></span>
                                        </button>
                                    </td>
                                    <td>
                                        <!--span class="glyphicon glyphicon-ban-circle" style="font-size: 20px"></span-->
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

        <jsp:include page="modal-grp-man.jsp"/>

        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>

    </body>
</html>