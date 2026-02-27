<%-- 
    Document   : selectPatientStatistics
    Created on : 14-nov-2016, 10.18.25
    Author     : danger
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Floriano Zini">
        <link rel="icon" href="resources/favicon.ico">

         <title>Scegli partecipante</title>
         <!--title>Scegli paziente </title-->

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
        <style>
            .user a{
                color:black;
                 text-shadow: 2px 2px 3px white;
            }
        </style>
        <script>
            function udf(x){
                return x;
            }
            function loadUserList(chose){
                console.log(chose);
                $.get("getusersfromgroup?groupid="+chose,function(data){
                    var js=data;
                    $("#utenti").html("");
                    for(var i=0;i<js.length;i++){
                        $("#utenti").append('<li class="user"><a  href="patientchosegraph?patientid='+js[i].id+'"><img class="img-responsive img-thumbnail" src="'+udf(js[i].photo)+'" /><p>'+js[i].surname+' '+js[i].name+'</p></a></li>');
                    }
                });
            }
            $(document).ready(function(){
                loadUserList(${groups[0].id});
                $("#selectGroup").on("change",function(){
                    var chose=$(this).val();
                    loadUserList(chose);
                });
            });
        </script>
        <style>
            #utenti{
                list-style: none;
                margin-left:0px;
                margin-rigth:0px;
                padding-left:0px;
                padding-righ:0px;
            }
            #utenti li{
                float:left;
                margin-right:5px;
                margin-bottom:5px;
                width:100px;
                height:100px;
                text-align: center;
            }
            li p{
                vertical-align:bottom;
            }
        </style>
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
 
            <div class="well container">
                <div class="col-md-24">
                    
                                      
                    <h2>Scegli il gruppo</h2>
                    <c:if test="${empty groups}">
                        <div id="infomessage" class="alert alert-danger" role="alert">Non sono presenti gruppi nel sistema!</div>
                    </c:if>
                    <form:form class="form-inline" method="get" action="groupgraph">

                        <div class="form-group">
                            <label>Gruppi</label>
                            <select id="selectGroup" class="form-control" name="groupid"
                                <c:if test="${empty groups}">
                                        disabled
                                </c:if>
                            >
                                <c:forEach var="groups" items="${groups}">
                                    <option value="${groups.id}">${groups.name}</option>
                                </c:forEach>
                            </select>
                        </div>                                                                            

                        <div class="form-group">
                            <button type="submit" class="btn btn-warning"
                                <c:if test="${empty groups}">
                                        disabled
                                </c:if>
                            >Visualizza Statistiche</button>
                        </div>
                    </form:form>
                    <br>
                    
                </div>
                <div id="elenco" class="col-md-24">
                    <ul id="utenti">
                        
                    </ul>
                </div>
                
            </div>
          

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-grpstat.jsp"/>                
        
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
