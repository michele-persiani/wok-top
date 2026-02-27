<%-- 
    Document   : grouplive
    Created on : 24-gen-2017, 10.19.40
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

        <title>Sessione Gruppi Live</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
        
        <link rel="stylesheet" type="text/css" href="resources/css/jquery.dataTables.css">
  
        <script type="text/javascript" charset="utf-8" src="resources/js/jquery.dataTables.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        
        <style>
            .container{
                max-width:100%;
            }
            .short{
                width:50px;
            }
            .red{
                color:#C36900;
            }
            .infodiff{
                padding-left:30px;
            }
        </style>
        <script>
            function add0(x){
                if(x<=9)
                    return "0"+x;
                return x;
            }
            function getFromattedTime(time){
                var q=new Date(time);
                return add0(q.getHours())+":"+add0(q.getMinutes())+" "+add0(q.getDate())+"/"+add0(q.getMonth()+1)+"/"+q.getFullYear();
            }
            function getDirectionIcon(direction){
                if(direction>0)
                    return '<span class="glyphicon glyphicon-arrow-up" aria-hidden="true"></span>';
                if(direction<0)
                    return '<span class="glyphicon glyphicon-arrow-down" aria-hidden="true"></span>';
                if(direction==0)
                    return '<span class="glyphicon glyphicon-resize-horizontal" aria-hidden="true"></span>';
                
                return "";
            }
            function addChangedColor(changed){
                if(changed){
                    return "red";
                }else{
                    return  "";
                }
            }
            function getItaNameDifficulty(diff){
                if(diff=="easy")
                       return "facile";
                if(diff=="medium")
                       return "media";
                if(diff=="difficult")
                       return "difficile";
            }
            function addChangedDifficulty(changed,diff,oldlevel){
                if(changed){
                    return "cambiata da "+oldlevel;
                }else{
                    return  getItaNameDifficulty(diff);
                }
            }
            function sendChangeRequest(level,historyid){
                console.log("entro: "+level+" "+historyid);
                $.post("changelevel", {
                    "level":level,
                    "historyid":historyid
                },
                function(data){
                    console.log("sent");
                    $('input[historyid="'+historyid+'"]').val(data);
                    $('input[historyid="'+historyid+'"]').addClass("red");
                })
                .fail(function() {
                    window.location.href =
                    '/MS-rehab/fatal-error.jsp?back=adminhome&home=adminhome';
                });
             }
             function autoRefresh(){
               setTimeout(function(){
                    $.get("patientgrouplivejson",{"id":${group.id}},function(data){
                            var h=JSON.parse(data);
                            insertHistory(h);
                            autoRefresh();
                    });
               },5000); 
            }
            function predicateBy(prop){
                return function(a,b){
                   if( a[prop] < b[prop]){
                       return 1;
                   }else if( a[prop] > b[prop] ){
                       return -1;
                   }
                   return 0;
                }
             }
            function insertHistory(h){
                if(h.length<=0){
                    $("#infomessage").html("Nessun dato per il gruppo scelto!");
                    $("#infomessage").show();
                }else{
                    $("#infomessage").hide();
                }
                /*$("#history").html('<tr>'+
                         '<th>Nome</th>'+
                        '<th>Nome esercizio</th>'+
                        '<th>Livello di difficolt&agrave;</th>'+
                        '<th>Performance</th>'+
                        '<th>Tempo</th>'+
                    '</tr>');*/
                $("#thistory").html("");
                h.sort( predicateBy("time") );
                for(var i=0;i<h.length;i++){
                    $("#history").append(
                            '<tr >'+
                                '<td class="col-sm-6" alt="'+h[i].id+'" title="'+h[i].id+'" >'+
                                    h[i].username+
                                '</td>'+
                                '<td class="col-sm-6">'+
                                    h[i].name+
                                '</td>'+
                                '<td class="col-sm-6">'+
                                    '<button type="button" class="btn btn-sm btn-danger min" aria-label="Abbassa difficoltà"> <span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>'+
                                    '<input id="" class="short '+addChangedColor(h[i].changed)+'" type="text" readonly value="'+h[i].level+'/'+h[i].maxdiff+'" historyid="'+h[i].historyid+'"/>'+
                                    '<button type="button" class="btn btn-sm btn-success plus" aria-label="Alza difficoltà"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>'+
                                    '<br />'+
                                    '<p class="infodiff">('+addChangedDifficulty(h[i].changed,h[i].difficulty,h[i].oldlevel)+')</p>'+
                                '</td>'+
                                '<td class="col-sm-6">'+
                                    (parseInt(h[i].performance*100))+
                                    "% "+
                                    getDirectionIcon(h[i].direction)+
                                '</td>'+
                                '<td class="col-sm-3">'+
                                    getFromattedTime(h[i].time)+
                                '</td>'+
                            '</div>'
                            )
                }
                 $("#history").DataTable().draw('full-hold');
            }
            var orderMemory = [[ 4, "desc" ]];
            $(document).ready(function(){
                var h = ${history};
                insertHistory(h);
                $("#history").on( 'order.dt',  function () {
                    var order = $("#history").DataTable().order();
                    orderMemory = order;
                    console.log("N vale: "+order[0][0]+" "+order[0][1]);
                } ).DataTable({
                        destroy:true,
                        language: {
                                url: 'resources/assets/localisation/it_IT.json'
                                   },
                        "order": orderMemory
                    });
                    
                $("#history").on("click",".min",function(){
                    var level=$(this).next().val().split("/")[0];
                    var historyid=$(this).next().attr("historyid");
                    historyid=parseInt(historyid);
                    console.log(level);
                    console.log(historyid);
                    level=parseInt(level) - 1;
                    $(this).next().val(level);
                    //$('input[historyid="'+historyid+'"]').removeClass("red");
                    sendChangeRequest(level,historyid);
                });
                $("#history").on("click",".plus",function(){
                    var level=$(this).prev().val().split("/")[0];;
                    var historyid=$(this).prev().attr("historyid");
                    historyid=parseInt(historyid);
                    console.log(level);
                    console.log(historyid);
                    level=parseInt(level) + 1;
                    $(this).prev().val(level);
                    //$('input[historyid="'+historyid+'"]').removeClass("red");
                    sendChangeRequest(level,historyid);
                });
                autoRefresh();
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="showgrouplivelist">                                
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

            <div class="well well-lg">
                <div>
                    <h2>Esercizi assegnati al gruppo: </h2>
                    <h2><b>${group.name}</b></h2>
                </div>
                <div class="row">
                        <div class="col-md-24 morepadding">
                            <hr />
                            <div id="infomessage" class="alert alert-danger" role="alert"></div>
                        </div>
                </div>
                <table id="history" class="table">
                    <thead>
                    <tr>
                         <th>Nome</th>
                        <th>Nome esercizio</th>
                        <th>Livello di difficolt&agrave;</th>
                        <th>Performance</th>
                        <th>Tempo</th>
                    </tr>
                    </thead>
                    <tbody id="thistory">
                    </tbody>
                </table>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-grp-live.jsp"/>                        
        
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
