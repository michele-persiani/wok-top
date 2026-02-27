<%-- 
    Document   : patient-session
    Created on : Oct 14, 2016, 9:49:34 AM
    Author     : floriano
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

        <title>Configura esercizi</title>

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
        
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">
		
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>
		        <link rel="stylesheet" href="resources/css/custom.css">
		
		
        <!-- Custom styles and js for this template -->
        <link rel="stylesheet" href="resources/jumbotron.css">
		<script type="text/javascript" src="resources/js/custom.js"></script>
        <!--link rel="stylesheet" href="resources/signin.css"-->

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>        

    </head>

    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="patientsessionmanagement">                                
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
                <div><h2>Configura sessione esercizi individuale</h2></div>

			<form:form id="sessForm" class="form-inline" method="post" modelAttribute="sessionForm"
                           action="buildpatientsession">
                    
                    <div class="row">
                        <div class="col-sm-8">
                            <spring:bind path="usrgrpid">
                                <div class="form-group">
                                    <label><!--paziente-->Partecipante</label>
                                    <form:select path="usrgrpid" class="form-control selectpicker" data-style="btn-primary">
                                        <c:forEach var="patient" items="${patients}">
                                            <form:option value="${patient.id}">${patient.surname} ${patient.name}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>                        
                            </spring:bind>                      
                        </div>
                        <div class="col-sm-8">
                            <spring:bind path="difficulty">
                                <div class="form-group">
                                    <label>Difficolt&agrave</label>
                                    <form:select path="difficulty" class="form-control selectpicker" data-style="btn-primary">
                                        <form:option value="easy">Facile</form:option>
                                        <form:option value='medium'>Media</form:option>
                                        <form:option value="difficult">Difficile</form:option>
                                    </form:select>
                                </div>
                            </spring:bind>                      
                        </div>
                    </div>
                    <hr>


                <c:if test="${not empty ATT_SEL_STD or not empty ATT_SEL_FLW or not empty ATT_ALT or not empty ATT_DIV}">
                    <h3> Attenzione: figure </h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_STD">
                                <div class="form-group">
                                    <label>Cogli gli oggetti (Selett. 1)</label>
                                    <form:select id="a0" path = "ATT_SEL_STD" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_STD}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_FLW">
                                <div class="form-group">
                                    <label>Beccali al volo (Selett. 2)</label>
                                    <form:select id="a1" path="ATT_SEL_FLW" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_FLW}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_ALT">
                                <div class="form-group">
                                    <label>Prima uno e poi l’altro (Alternata)</label>
                                    <form:select id="a2" path="ATT_ALT" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_ALT}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_DIV">
                                <div class="form-group">
                                    <label>Su due fronti (Divisa)</label>
                                    <form:select id="a3" path="ATT_DIV" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_DIV}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                    </div>               
                    <hr>
                </c:if>



                <c:if test="${not empty ATT_SEL_STD_FAC or not empty ATT_SEL_FLW_FAC or not empty ATT_ALT_FAC or not empty ATT_DIV_FAC}">
                    <h3>Attenzione: volti</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_STD_FAC">
                                <div class="form-group">
                                    <label>Cogli gli oggetti (Selett. 1)</label>
                                    <form:select id="a4" path = "ATT_SEL_STD_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_STD_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_FLW_FAC">
                                <div class="form-group">
                                    <label>Beccali al volo (Selett. 2)</label>
                                    <form:select id="a5" path="ATT_SEL_FLW_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_FLW_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_ALT_FAC">
                                <div class="form-group">
                                    <label>Prima uno e poi l’altro  (Alternata)</label>
                                    <form:select id="a6" path="ATT_ALT_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_ALT_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_DIV_FAC">
                                <div class="form-group">
                                    <label>Su due fronti (Divisa)</label>
                                    <form:select id="a7" path="ATT_DIV_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_DIV_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                    </div>
                    <hr>
                </c:if>




                <c:if test="${not empty ATT_SEL_STD_ORI or not empty ATT_SEL_FLW_ORI or not empty ATT_ALT_ORI or not empty ATT_DIV_ORI}">

                    <h3>Attenzione: orientamento</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_STD_ORI">
                                <div class="form-group">
                                    <label>Cogli gli oggetti (Selett. 1)</label>
                                    <form:select id="a8" path = "ATT_SEL_STD_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_STD_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_SEL_FLW_ORI">
                                <div class="form-group">
                                    <label>Beccali al volo (Selett. 2)</label>
                                    <form:select id="a9" path="ATT_SEL_FLW_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_SEL_FLW_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_ALT_ORI">
                                <div class="form-group">
                                    <label> Prima uno e poi l’altro (Alternata)</label>
                                    <form:select id="a10" path="ATT_ALT_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_ALT_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="ATT_DIV_ORI">
                                <div class="form-group">
                                    <label>Su due fronti (Divisa)</label>
                                    <form:select id="a11" path="ATT_DIV_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_DIV_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                    </div>
                </c:if>
                    <hr>

                <c:if test="${not empty ATT_RFLXS}">
                    <h3>Attenzione: riflessi</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="ATT_RFLXS">
                                <div class="form-group">
                                    <label>Attenzione (Riflessi 1)</label>
                                    <form:select id="a12" path = "ATT_RFLXS" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${ATT_RFLXS}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                    </div>
                    <hr>
                </c:if>


                <c:if test="${not empty MEM_VIS_1 or not empty MEM_VIS_2 or not empty MEM_VIS_5 or not empty NBACK}">
                    <h3>Memoria: figure</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="MEM_VIS_1">
                                <div class="form-group">
                                    <label>Quali erano? Riconoscimento</label>
                                    <form:select id="a12" path="MEM_VIS_1" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_VIS_1}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="MEM_VIS_2">
                                <div class="form-group">
                                    <label>Nel posto giusto (Memoria visuo-spaziale)</label>
                                    <form:select id="a13" path="MEM_VIS_2" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_VIS_2}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                         <div class="col-sm-6">
                            <spring:bind path="MEM_VIS_5">
                                <div class="form-group">
                                    <label>Nel posto giusto griglia(Memoria visuo-spaziale)</label>
                                    <form:select id="a23" path="MEM_VIS_5" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_VIS_5}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="NBACK">
                                <div class="form-group">
                                    <label>Occhio alle spalle (Memoria di lavoro)</label>
                                    <form:select id="a14" path="NBACK" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${NBACK}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>                            
                        </div>
                    </div>
                    <hr>
                </c:if>


                <c:if test="${not empty MEM_VIS_1_FAC or not empty NBACK_FAC or not empty MEM_LONG_1}">
                    <h3>Memoria: volti</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="MEM_VIS_1_FAC">
                                <div class="form-group">
                                    <label>Quali erano? (Riconoscimento)</label>
                                    <form:select id="a15" path="MEM_VIS_1_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_VIS_1_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="NBACK_FAC">
                                <div class="form-group">
                                    <label>Occhio alle spalle (Memoria di lavoro)</label>
                                    <form:select id="a16" path="NBACK_FAC" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${NBACK_FAC}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>                            
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="MEM_LONG_1">
                                <div class="form-group">
                                    <label>Chi sarà? (Associazione volti-nomi)</label>
                                    <form:select id="a17" path="MEM_LONG_1" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_LONG_1}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                    </div>
                    <hr>
                </c:if>

                <c:if test="${not empty MEM_VIS_1_ORI or not empty NBACK_ORI}">
                    <h3>Memoria: orientamento</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="MEM_VIS_1_ORI">
                                <div class="form-group">
                                    <label>Riconoscimento</label>
                                    <form:select id="a18" path="MEM_VIS_1_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${MEM_VIS_1_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="NBACK_ORI">
                                <div class="form-group">
                                    <label>Occhio alle spalle (Memoria di lavoro)</label>
                                    <form:select id="a19" path="NBACK_ORI" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${NBACK_ORI}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>                            
                        </div>
                    </div>
                    <hr>
                </c:if>

                <c:if test="${not empty RES_INH or not empty PLAN_1 or not empty PLAN_2 or not empty PLAN_3}">
                    <h3>Funzioni esecutive</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <spring:bind path="RES_INH">
                                <div class="form-group">
                                    <label>Controllo - inibizione</label>
                                    <form:select id="a20" path="RES_INH" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${RES_INH}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col-sm-6">
                            <spring:bind path="PLAN_1">
                                <div class="form-group">
                                    <label>Pianificazione - Giornata</label>
                                    <form:select id="a21" path="PLAN_1" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${PLAN_1}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind>                            
                        </div>                  
                       
                        <div class="col-sm-6">
                            <spring:bind path="PLAN_2">
                                <div class="form-group">
                                    <label>Pianificazione - Zoo</label>
                                    <form:select id="a22" path="PLAN_2" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${PLAN_2}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                        </spring:bind> 
                            </div>
                        
                                <div class="col-sm-6"> 
                            <spring:bind path="PLAN_3">
                                <div class="form-group">
                                    <label>Pianificazione - Roma</label>
                                    <form:select id="a24" path="PLAN_3" class="form-control selectpicker" data-style="btn-warning">
                                        <form:option value="">Nessuno</form:option>
                                        <c:forEach var="ex" items="${PLAN_3}">
                                            <form:option value="${ex.id}">${ex.description}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </spring:bind> 
                                    </div>
                        
                        </div>                  
                    </div>
                    <hr>
            </c:if>
                    <div class="form-group">
                        <button id="submit-data" disabled="" type="submit" class="btn btn-success">Configura</button>
                        <!--button id="clear-data" type="reset" class="btn btn-warning">Annulla</button-->
                    </div>                    
                    
                </form:form>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->


        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-pat-sess.jsp" />
        
        <script type="text/javascript">
            $('#sessForm').submit(function(e) {
                var sessForm = this;
                e.preventDefault();
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
                    message:'<h4>Confermi la configurazione?</h4>',
                    callback:function(result) {
                        if (result) {
                            sessForm.submit();
                        }
                    }
                });
            });
        </script>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script>
            var disabled = [
                true,true,true,true,true,true,true,true,true,true,
                true,true,true,true,true,true,true,true,true,true,
                true,true,true,true];
            var allDisabled = true;
        
            //$("#clear-data").click()(function() {
            //    $("#submit-data").prop("disabled", true);
            //});    

            $("#a0").change(function() {
                if (this.value === "") {
                    disabled[0] = true;
                } else {
                    disabled[0] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
            $("#a1").change(function() {
                if (this.value === "") {
                    disabled[1] = true;
                } else {
                    disabled[1] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a2").change(function() {
                if (this.value === "") {
                    disabled[2] = true;
                } else {
                    disabled[2] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a3").change(function() {
                if (this.value === "") {
                    disabled[3] = true;
                } else {
                    disabled[3] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a4").change(function() {
                if (this.value === "") {
                    disabled[4] = true;
                } else {
                    disabled[4] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a5").change(function() {
                if (this.value === "") {
                    disabled[5] = true;
                } else {
                    disabled[5] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a6").change(function() {
                if (this.value === "") {
                    disabled[6] = true;
                } else {
                    disabled[6] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a7").change(function() {
                if (this.value === "") {
                    disabled[7] = true;
                } else {
                    disabled[7] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a8").change(function() {
                if (this.value === "") {
                    disabled[8] = true;
                } else {
                    disabled[8] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a9").change(function() {
                if (this.value === "") {
                    disabled[9] = true;
                } else {
                    disabled[9] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a10").change(function() {
                if (this.value === "") {
                    disabled[10] = true;
                } else {
                    disabled[10] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a11").change(function() {
                if (this.value === "") {
                    disabled[11] = true;
                } else {
                    disabled[11] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a12").change(function() {
                if (this.value === "") {
                    disabled[12] = true;
                } else {
                    disabled[12] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a13").change(function() {
                if (this.value === "") {
                    disabled[13] = true;
                } else {
                    disabled[13] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a14").change(function() {
                if (this.value === "") {
                    disabled[14] = true;
                } else {
                    disabled[14] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
            $("#a15").change(function() {
                if (this.value === "") {
                    disabled[15] = true;
                } else {
                    disabled[15] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a16").change(function() {
                if (this.value === "") {
                    disabled[16] = true;
                } else {
                    disabled[16] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a17").change(function() {
                if (this.value === "") {
                    disabled[17] = true;
                } else {
                    disabled[17] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a18").change(function() {
                if (this.value === "") {
                    disabled[18] = true;
                } else {
                    disabled[18] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });                            
            $("#a19").change(function() {
                if (this.value === "") {
                    disabled[19] = true;
                } else {
                    disabled[19] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });            
            $("#a20").change(function() {
                if (this.value === "") {
                    disabled[20] = true;
                } else {
                    disabled[20] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
            $("#a21").change(function() {
                if (this.value === "") {
                    disabled[21] = true;
                } else {
                    disabled[21] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
             $("#a22").change(function() {
                if (this.value === "") {
                    disabled[22] = true;
                } else {
                    disabled[22] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
             $("#a23").change(function() {
                if (this.value === "") {
                    disabled[23] = true;
                } else {
                    disabled[23] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
             $("#a24").change(function() {
                if (this.value === "") {
                    disabled[24] = true;
                } else {
                    disabled[24] = false;
                }
                allDisabled=true;
                for (i = 0; i < disabled.length; i++) {
                    allDisabled = allDisabled && disabled[i];
                }
                $("#submit-data").prop("disabled", allDisabled);
            });
        </script>
    </body>
</html>