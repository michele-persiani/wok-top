<%-- 
    Document   : patient-rehabilitation
    Created on : Oct 22, 2016, 9:49:34 PM
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
        <script src="resources/js/d50be4394a.js"></script>
        <link rel="icon" href="resources/favicon.ico">

        <title><!--paziente-->partecipante</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript"
        src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet"
              href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron.css">
        <!--link rel="stylesheet" href="resources/signin.css"-->
        <link rel="stylesheet" href="resources/css/custom.css">
        
        

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
                  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
                  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
                <![endif]-->

    </head>

    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-left">
                        <li role="presentation">
                            <a data-toggle="modal" href="#infoModal">
                                <span class="glyphicon glyphicon-info-sign fa-2x"
                                      data-toggle="tooltip"
                                      data-placement="bottom"
                                      title="Info">
                                </span>
                            </a>
                        </li>
                    </ul>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="patienthome">
                                <span
                                    class="glyphicon glyphicon-chevron-left fa-2x"
                                    data-toggle="tooltip" data-placement="bottom"
                                    title="Indietro">
                                </span>
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="patienthome">
                                <span
                                    class="glyphicon glyphicon-home fa-2x"
                                    data-toggle="tooltip"
                                    data-placement="bottom" title="Home">
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="logout">
                                <span
                                    class="glyphicon glyphicon-log-out fa-2x"
                                    data-toggle="tooltip"
                                    data-placement="bottom" title="Logout">
                                </span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <div class="jumbotron">
                <!--h2>Ecco i suoi esercizi di riabilitazione</h2-->
                <h2>Ecco i suoi esercizi di training</h2>
                <p></p>

                <c:forEach var="group" items="${groupMap}">
                <c:if test="${not empty group.value.exerciseCategory}">
                    <hr>
                    <h3>${group.value.groupName}</h3>
                    <div class="row">
                        <c:forEach var="category" items="${group.value.exerciseCategory}">
                            <div class="col-sm-5">
                                <ul class="list-group" style="position:relative;">
                                    <c:if test="${!category.id.equals(lastInterruptedExerciseCategory)}">
                                        <a href="#${category.id}" class="list-group-item list-group-item-action active" data-toggle="collapse">
                                            ${category.categoryNameForPatient}<i class="fa fa-caret-down pull-right"></i>
                                        </a>
                                    </c:if>
                                    <c:if test="${category.id.equals(lastInterruptedExerciseCategory)}">
                                        <button href="#${category.id}" type="button" class="list-group-item list-group-item-action active" data-toggle="collapse">
                                            <i class="fa fa-pause-circle-o fa-fw" aria-hidden="true"></i>${category.categoryNameForPatient}<i class="fa fa-caret-down pull-right"></i>
                                        </button>
                                    </c:if>
                                    <div class="collapse" id="${category.id}" style="position: absolute; z-index: 3; width:100%">
                                        <c:forEach var="exercise" items="${category.exercises}">

                                            <!-- Costruisco URL -->
                                            <c:choose>
                                                <c:when test="${category.id == 'ATT_RFLXS'}">
                                                    <c:url var="exerciseURL" value="reflexes1">
                                                        <c:param name="difficulty" value="${difficulty}" />
                                                        <c:param name="patientid" value="${patientid}" />
                                                        <c:param name="exerciseid" value="${exercise.id}" />
                                                        <c:param name="category" value="${exercise.exelementcat}" />
                                                        <c:param name="sessid" value="${sessid}" />
                                                        <c:param name="type" value="${exercise.category}" />
                                                        <c:param name="exname" value="${exercise.name}" />
                                                    </c:url>
                                                </c:when>
                                                <c:when test = "${category.id == 'ATT_SEL_STD' || category.id == 'ATT_SEL_STD_FAC' || category.id == 'ATT_SEL_STD_ORI'}">
                                                    <c:url var="exerciseURL" value="attention1">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>
                                                </c:when>
                                                <c:when test = "${category.id == 'ATT_SEL_FLW' || category.id == 'ATT_SEL_FLW_FAC' || category.id == 'ATT_SEL_FLW_ORI'}">
                                                    <c:url var="exerciseURL" value="attention2">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'ATT_ALT' || category.id == 'ATT_ALT_FAC' || category.id == 'ATT_ALT_ORI'}">
                                                    <c:url var="exerciseURL" value="attention3">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'ATT_DIV' || category.id == 'ATT_DIV_FAC' || category.id == 'ATT_DIV_ORI'}">
                                                    <c:url var="exerciseURL" value="attention4">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'MEM_VIS_1' || category.id == 'MEM_VIS_1_FAC' || category.id == 'MEM_VIS_1_ORI'}">
                                                    <c:url var="exerciseURL" value="memory1">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'MEM_VIS_2' || category.id == 'MEM_VIS_2_FAC'}">
                                                    <c:url var="exerciseURL" value="memory2">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                 <c:when test = "${category.id == 'MEM_VIS_5'}">
                                                    <c:url var="exerciseURL" value="memory5">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'NBACK' || category.id == 'NBACK_FAC' || category.id == 'NBACK_ORI'}">
                                                    <c:url var="exerciseURL" value="nback">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="type" value="${exercise.category}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'MEM_LONG_1'}">
                                                    <c:url var="exerciseURL" value="memory4">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>                                                        
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'RES_INH'}">
                                                    <c:url var="exerciseURL" value="execfunct1">
                                                        <c:param name="difficulty" value="${difficulty}"/>
                                                        <c:param name="patientid" value="${patientid}"/>
                                                        <c:param name="exerciseid" value="${exercise.id}"/>
                                                        <c:param name="category" value="${exercise.exelementcat}"/>
                                                        <c:param name="sessid" value="${sessid}"/>
                                                        <c:param name="exname" value="${exercise.name}"/>
                                                        <c:param name="exdescr" value="${exercise.description}"/>                                                        
                                                    </c:url>  
                                                </c:when>
                                                <c:when test = "${category.id == 'PLAN_1'}">
                                                    <c:if test = "${exercise.name == 'PLAN_1'}">
                                                        <c:url var="exerciseURL" value="pianificazione1">
                                                            <c:param name="difficulty" value="${difficulty}"/>
                                                            <c:param name="patientid" value="${patientid}"/>
                                                            <c:param name="exerciseid" value="${exercise.id}"/>
                                                            <c:param name="sessid" value="${sessid}"/>
                                                            <c:param name="exname" value="${exercise.name}"/>
                                                            <c:param name="exdescr" value="${exercise.description}"/>                                                            
                                                        </c:url>  
                                                    </c:if>
                                                     </c:when>
                                                    <c:when test = "${category.id == 'PLAN_2'}">
                                                    <c:if test = "${exercise.name == 'PLAN_2'}">
                                                        <c:url var="exerciseURL" value="pianificazione2">
                                                            <c:param name="difficulty" value="${difficulty}"/>
                                                            <c:param name="patientid" value="${patientid}"/>
                                                            <c:param name="exerciseid" value="${exercise.id}"/>
                                                            <c:param name="sessid" value="${sessid}"/>
                                                            <c:param name="exname" value="${exercise.name}"/>
                                                            <c:param name="exdescr" value="${exercise.description}"/>                                                            
                                                        </c:url>  
                                                    </c:if>
                                                </c:when>
                                                <c:when test = "${category.id == 'PLAN_3'}">
                                                    <c:if test = "${exercise.name == 'PLAN_3'}">
                                                        <c:url var="exerciseURL" value="pianificazione3">
                                                            <c:param name="difficulty" value="${difficulty}"/>
                                                            <c:param name="patientid" value="${patientid}"/>
                                                            <c:param name="exerciseid" value="${exercise.id}"/>
                                                            <c:param name="sessid" value="${sessid}"/>
                                                            <c:param name="exname" value="${exercise.name}"/>
                                                            <c:param name="exdescr" value="${exercise.description}"/>                                                            
                                                        </c:url>  
                                                    </c:if>
                                                </c:when>
                                                
                                                
                                            </c:choose>

                                            <c:if test="${exercise.status == 'done'}">
                                                <a href="#" class="list-group-item list-group-item-action list-group-item-success">
                                                    <i class="fa fa-check-circle-o fa-fw" aria-hidden="true" style="color:green"></i>
                                                    ${exercise.description}
                                                    <small class="text-muted">(Completato)</small>
                                                </a>
                                            </c:if>
                                            <c:if test="${exercise.status == 'interrupted' && exercise.id != lastInterruptedExercise}">
                                                <a href="<c:out value="${exerciseURL}"/>" class="list-group-item list-group-item-action list-group-item-warning">
                                                    <i class="fa fa-pause-circle-o fa-fw" aria-hidden="true" style="color:orange"></i>
                                                    ${exercise.description}
                                                    <small class="text-muted">(Interrotto)</small>
                                                </a>
                                            </c:if>
                                            <c:if test="${exercise.id == lastInterruptedExercise}">
                                                <a href="<c:out value="${exerciseURL}"/>" class="list-group-item list-group-item-action list-group-item-danger">
                                                    <i class="fa fa-pause-circle-o fa-fw" aria-hidden="true" style="color:red"></i>
                                                    ${exercise.description}
                                                    <small class="text-muted">(Interrotto)</small>
                                                </a>
                                            </c:if>
                                            <c:if test="${exercise.status == 'todo'}">
                                                <a href="<c:out value="${exerciseURL}"/>" class="list-group-item list-group-item-action">
                                                    <i class="fa fa-play-circle-o fa-fw" aria-hidden="true" style="color:grey"></i>
                                                    ${exercise.description}
                                                    <small class="text-muted">(Da iniziare)</small>
                                                </a>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </ul>
                                <c:if test="${! inHospital}">
                                    <div class="progress">
                                        <c:choose>
                                            <c:when test="${category.progress==100}">
                                                <div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="${category.progress}" aria-valuemin="0" aria-valuemax="100"  style="width:${category.progress}%">${String.valueOf(category.progress)} %</div>
                                            </c:when>    
                                            <c:otherwise>
                                                <div class="progress-bar progress-bar-warning progress-bar-striped" role="progressbar" aria-valuenow="${category.progress}" aria-valuemin="0" aria-valuemax="100"  style="width:${category.progress}%"></div>
                                            </c:otherwise>
                                        </c:choose>
                                        ${String.valueOf(category.progress)} %
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                    </c:if>
                </c:forEach>
            </div>
            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>


        </div>
        <!-- /container -->


        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-pat.jsp" />

        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>

        <script>
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
    </body>
</html>