<%-- 
    Document   : pddl-pianificazione1b
    Created on : 12-mag-2017, 10.25.30
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
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Daniele Baschieri">
        <link rel="icon" href="resources/favicon.ico">

        <title>Pianificazione</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>
        
        <!--script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script-->
        <!-- Latest compiled and minified JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
        <!--script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script-->
     

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        <link rel="stylesheet" href="resources/css/pddl-default.css" />
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>
        
        <script type="text/javascript" src="resources/js/pddl-default.js" ></script>
        <script type="text/javascript" src="resources/js/pddl-maps.js" ></script>
        <script type="text/javascript" src="resources/js/pddl-logic.js" ></script>
        <style>
            .legendlist{
              list-style-type: none; 
            }
            .legendlist li {
              margin-bottom: 3px;
            }
            .legendlist img{
              width: 30px;
              margin-right: 15px;
            }
            .legendlist img.bigimg{
                width:50px;
            }
        </style>
        <script type="text/javascript" >
            $(document).ready(function(){
                    $(".loader").hide();
                    $.get("getproblemjson",{"id":${idproblem}},function(data){
                        var js=JSON.parse(data);
                        console.log(js.map);
                        idproblem=${idproblem};
                        colorenable=${color};
                        difficulty="${difficulty}";
                        level = ${level};
                        patientid=${patientid};
                        exerciseid=${exerciseid};
                        pTime=0;
                        sessid=${sessid};
                        
                        BELIEF=js.belief;
                        var mappa = JSON.parse(js.map);
                        initMappa("mappa",mappa);
                    });
                });
        </script>
    </head>
    <body>
        <div class="container-fluid shorten">
            <div class="navbar clearfix navbar-fixed-top " style="background-color:white">                
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-24">
                            <!--input type="button" class="btn btn-lg btn-success pull-right" value="Esercizio concluso"-->
                            <c:if test="${difficulty=='training' && patientid=='-1'}">
                                <a href="patienttraining" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty=='training' && patientid!='-1'}">
                                <a href="patientrehabilitation" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty=='demo'}">
                                <a href="patientdemo" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty!='training' && difficulty!='demo'}">
                                <a href="patientrehabilitation" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            
                        </div>                    
                    </div>
                </div>
            </div>

            <div class="starter-template">
                <div class="col-md-16">
                    <canvas id="mappa" width="800" height="600"></canvas>
                    <div id="info">
                                <button id="elenco" class="btn btn-lg btn-primary" type="button" data-toggle="modal" data-target="#myModal" data-whatever="@showInfo">
                                    <span class="glyphicon glyphicon-info-sign">
                            </span> Cose da fare</button>
                    </div>

                    <div id="orario">
                        <span id="tempo">8:00</span>
                        <button id="aspetta" class="btn btn-lg btn-primary">Aspetta</button>
                    </div>
                </div>
                <div class="col-md-4 box">
                  <div class="header">
                      <h2>Inventario <img src="resources/images/locali/map.png" width="50" /></h2>
                  </div>
                      <div id="inventario"></div>
                </div>
                <div class="col-md-4 box">
                  <div class="header">
                      <h2>Posizione attuale</h2>
                      <h3 id="positionname"></h3>
                  </div>
                  <div class="location" id="posizioneattuale"></div>
                </div>
            </div>
            <div class="modalloader"></div> 
            <div class="loader"></div> 
            <div id="shadowImg">
            </div>

        </div><!-- /.container -->

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
              </div>
              <div class="modal-body">
                ...
              </div>
              <div class="modal-footer">
              </div>
            </div>
          </div>
        </div>

        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        
    </body>
</html>
