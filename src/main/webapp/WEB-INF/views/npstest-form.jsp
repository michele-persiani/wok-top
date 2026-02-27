<%-- 
    Document   : npstest-form
    Created on : Apr 27, 2017, 09:52:34 AM
    Author     : floriano
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html lang="it">
    <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="resources/favicon.ico">

    <title>NPS test <!--paziente-->partecipante</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="resources/css/bootstrap.min.css">
    
    <!-- font-awesome -->
    <link rel="stylesheet" href="resources/css/font-awesome.min.css">

    <!-- jQuery library -->
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
    
    <script src="resources/js/bootstrap-datepicker.min.js"></script>

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="resources/jumbotron-narrow.css">

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
                            <a href="npstestsel?patientid=${patientid}">                                
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
                <h2>NPS test di ${patientname}</h2>

                <spring:url value="/saveorupdatenpstest" var="userActionUrl" />

                <form:form class="form-horizontal" name="npsTest" method="post" modelAttribute="npstestForm" action="${userActionUrl}">

                    <form:hidden path="id" />

                    <form:hidden path="userid" />

                    <form:hidden path="timestamp" />

                    <spring:bind path="examinator">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Esaminatore</label>
                            <div class="col-sm-20">
                                <form:input path="examinator" type="text" class="form-control " id="examinator" placeholder="* Esaminatore" />
                                <form:errors path="examinator" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>                      
                    
                    <spring:bind path="tmtaraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">TMTA</label>
                            <div class="col-sm-20">
                                <form:input path="tmtaraw" type="text" class="form-control" id="tmtaraw" placeholder="* TMTA" />
                                <form:errors path="tmtaraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="tmtbraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">TMTB</label>
                            <div class="col-sm-20">
                                <form:input path="tmtbraw" type="text" class="form-control" id="tmtbraw" placeholder="* TMTB" />
                                <form:errors path="tmtbraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="mfisp">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">MFISP</label>
                            <div class="col-sm-20">
                                <form:input path="mfisp" type="text" class="form-control" id="mfisp" placeholder="* MFISP" />
                                <form:errors path="mfisp" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="mfisc">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">MFISC</label>
                            <div class="col-sm-20">
                                <form:input path="mfisc" type="text" class="form-control" id="mfisc" placeholder="* MFISC" />
                                <form:errors path="mfisc" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="mfisps">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">MFISPS</label>
                            <div class="col-sm-20">
                                <form:input path="mfisps" type="text" class="form-control" id="mfisps" placeholder="* MFISPS" />
                                <form:errors path="mfisps" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="stai1">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">STAI-1</label>
                            <div class="col-sm-20">
                                <form:input path="stai1" type="text" class="form-control" id="stai1" placeholder="* STAI-1" />
                                <form:errors path="stai1" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="stai2">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">STAI-2</label>
                            <div class="col-sm-20">
                                <form:input path="stai2" type="text" class="form-control" id="stai2" placeholder="* STAI-2" />
                                <form:errors path="stai2" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="beck">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">BECK</label>
                            <div class="col-sm-20">
                                <form:input path="beck" type="text" class="form-control" id="beck" placeholder="* BECK" />
                                <form:errors path="beck" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="msqol54">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-5 control-label">MSQOL-54</label>
                            <div class="col-sm-19">
                                <form:input path="msqol54" type="text" class="form-control" id="msqol54" placeholder="* MSQOL-54" />
                                <form:errors path="msqol54" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="dkefsdescr">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">DKEFSDESCR</label>
                            <div class="col-sm-20">
                                <form:input path="dkefsdescr" type="text" class="form-control" id="dkefsdescr" placeholder="* DKEFS DESCR" />
                                <form:errors path="dkefsdescr" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="dkefscat">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">DKEFSCAT</label>
                            <div class="col-sm-20">
                                <form:input path="dkefscat" type="text" class="form-control" id="dkefscat" placeholder="* DKEFS CAT" />
                                <form:errors path="dkefscat" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="notes">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Note</label>
                            <div class="col-sm-20">
                                <form:textarea path="notes" class="form-control" rows="10" id="notes" placeholder="Note" />
                                <form:errors path="notes" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    <div class="form-group">
                        <div class="col-sm-6">
                        </div>
                        <div class="col-sm-6">
                        </div>
                        <div class="col-sm-6">
                            <button type="reset" class="btn-lg btn-warning pull-right">Annulla</button>
                        </div>
                        <div class="col-sm-6">
                            <button type="button" class="btn-lg btn-success pull-right"
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
                                                            message: '<h4>Vuoi salvare i dati inseriti?</h4>',
                                                            callback: function (result) {
                                                                if (result === true) {
                                                                    document.npsTest.submit();
                                                                }
                                                            }
                                                        });">Salva</button>                        
                        </div>
                    </div>                    
                </form:form>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-npstest-form.jsp"/>        

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
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