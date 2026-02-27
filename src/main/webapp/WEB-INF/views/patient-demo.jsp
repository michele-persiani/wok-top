<%-- 
    Document   : patient-demo
    Created on : Mar 6, 2018, 6:19:15 PM
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
<!--link rel="stylesheet" href="resources/signin.css"-->

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
				<ul class="nav nav-pills pull-right">
					<li role="presentation"><a href="patienthome"> <span
							class="glyphicon glyphicon-chevron-left fa-2x"
							data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
					</a></li>
					<li role="presentation"><a href="patienthome"> <span
							class="glyphicon glyphicon-home fa-2x" data-toggle="tooltip"
							data-placement="bottom" title="Home"></span>
					</a></li>
					<li><a href="logout"> <span
							class="glyphicon glyphicon-log-out fa-2x" data-toggle="tooltip"
							data-placement="bottom" title="Logout"></span>
					</a></li>

				</ul>
				<ul class="nav nav-pills pull-left">
					<li role="presentation"><a data-toggle="modal"
						href="#infoModal"> <span
							class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip"
							data-placement="bottom" title="Info"></span>
					</a></li>
				</ul>
			</nav>
		</div>

		<div class="jumbotron">
			<h2>Ecco i suoi esercizi demo</h2>
			<p></p>

			<c:forEach var="group" items="${groupMap}">
				<hr>
				<h3>${group.value.groupName}</h3>
				<div class="row">
					<c:forEach var="category" items="${group.value.exerciseCategory}">
						<div class="col-sm-5">
							<ul class="list-group" style="position: relative;">
								<a href="#${category.id}"
									class="list-group-item list-group-item-action active"
									data-toggle="collapse"> ${category.categoryNameForPatient}<i
									class="fa fa-caret-down pull-right"></i>
								</a>
								<div class="collapse" id="${category.id}"
									style="position: absolute; z-index: 3; width: 100%">
									<c:forEach var="exercise" items="${category.exercises}">
										<!-- Costruisco URL -->
										<c:choose>
											<c:when
												test="${category.id == 'ATT_SEL_STD' || category.id == 'ATT_SEL_STD_FAC' || category.id == 'ATT_SEL_STD_ORI'}">
												<c:url var="exerciseURL" value="createattention1">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
													<c:param name="exname" value="${exercise.name}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'ATT_SEL_FLW' || category.id == 'ATT_SEL_FLW_FAC' || category.id == 'ATT_SEL_FLW_ORI'}">
												<c:url var="exerciseURL" value="createattention2">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
													<c:param name="exname" value="${exercise.name}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'ATT_ALT' || category.id == 'ATT_ALT_FAC' || category.id == 'ATT_ALT_ORI'}">
												<c:url var="exerciseURL" value="createattention3">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
													<c:param name="exname" value="${exercise.name}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'ATT_DIV' || category.id == 'ATT_DIV_FAC' || category.id == 'ATT_DIV_ORI'}">
												<c:url var="exerciseURL" value="createattention4">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
													<c:param name="exname" value="${exercise.name}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'MEM_VIS_1' || category.id == 'MEM_VIS_1_FAC' || category.id == 'MEM_VIS_1_ORI'}">
												<c:url var="exerciseURL" value="creatememory1">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
													<c:param name="exname" value="${exercise.name}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'MEM_VIS_2' || category.id == 'MEM_VIS_2_FAC'}">
												<c:url var="exerciseURL" value="creatememory2">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
												</c:url>
											</c:when>
                                                                                        <c:when
												test="${category.id == 'MEM_VIS_5' || category.id == 'MEM_VIS_5_FAC'}">
												<c:url var="exerciseURL" value="creatememory5">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
												</c:url>
											</c:when>
											<c:when
												test="${category.id == 'NBACK' || category.id == 'NBACK_FAC' || category.id == 'NBACK_ORI'}">
												<c:url var="exerciseURL" value="createnback">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
													<c:param name="type" value="${exercise.category}" />
												</c:url>
											</c:when>
											<c:when test="${category.id == 'MEM_LONG_1'}">
												<c:url var="exerciseURL" value="creatememory4">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
												</c:url>
											</c:when>
											<c:when test="${category.id == 'RES_INH'}">
												<c:url var="exerciseURL" value="createexecfunct1">
													<c:param name="difficulty" value="${difficulty}" />
													<c:param name="patientid" value="${patientid}" />
													<c:param name="exerciseid" value="${exercise.id}" />
													<c:param name="category" value="${exercise.exelementcat}" />
													<c:param name="sessid" value="${sessid}" />
												</c:url>
											</c:when>
											<c:when test="${category.id == 'PLAN_1'}">
												<c:if test="${exercise.name == 'PLAN_1'}">
													<c:url var="exerciseURL" value="pianificazione1phase1">
														<c:param name="difficulty" value="${difficulty}" />
														<c:param name="patientid" value="${patientid}" />
														<c:param name="exerciseid" value="${exercise.id}" />
														<c:param name="sessid" value="${sessid}" />
													</c:url>
												</c:if>
                                                                                           </c:when>
                                                                                        <c:when test="${category.id == 'PLAN_2'}">
												<c:if test="${exercise.name == 'PLAN_2'}">
													<c:url var="exerciseURL" value="pianificazione2phase1">
														<c:param name="difficulty" value="${difficulty}" />
														<c:param name="patientid" value="${patientid}" />
														<c:param name="exerciseid" value="${exercise.id}" />
														<c:param name="sessid" value="${sessid}" />
													</c:url>
												</c:if>
											</c:when>
                                                                                           <c:when test="${category.id == 'PLAN_3'}">
												<c:if test="${exercise.name == 'PLAN_3'}">
													<c:url var="exerciseURL" value="pianificazione3phase1">
														<c:param name="difficulty" value="${difficulty}" />
														<c:param name="patientid" value="${patientid}" />
														<c:param name="exerciseid" value="${exercise.id}" />
														<c:param name="sessid" value="${sessid}" />
													</c:url>
												</c:if>
											</c:when>
										</c:choose>

										<a href="<c:out value="${exerciseURL}"/>"
											class="list-group-item list-group-item-action">${exercise.description}
										</a>

									</c:forEach>
								</div>
							</ul>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>

		<footer class="footer">
			<p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
		</footer>

	</div>
	<!-- /container -->


	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

	<jsp:include page="modal-pat-train.jsp" />

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