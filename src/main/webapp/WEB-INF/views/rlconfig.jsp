<%--
    Document   : rlconfig
    Created on : Jan 27 2026
    Author     : Michele
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

  <title>Amministratore</title>

  <!-- Bootstrap core CSS -->
  <link rel="stylesheet" href="resources/css/bootstrap.min.css">
  <!-- prova -->
  <link rel="text/javascript" href="resources/js/custom.js">


  <!-- font-awesome -->
  <link rel="stylesheet" href="resources/css/font-awesome.min.css">

  <!-- jQuery library -->
  <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

  <!-- Latest compiled JavaScript -->
  <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

  <!-- Tabulator -->
  <link href="https://unpkg.com/tabulator-tables@6.3.1/dist/css/tabulator.min.css" rel="stylesheet">
  <script type="text/javascript" src="https://unpkg.com/tabulator-tables@6.3.1/dist/js/tabulator.min.js"></script>

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
  <div class="header clearfix">
    <nav>
      <ul class="nav nav-pills pull-right">
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
      <ul class="nav nav-pills pull-right">
        <li>
          <a href="adminhome">
            <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Back"></span>
          </a>
        </li>
      </ul>
    </nav>
  </div>

  <div class="jumbotron">
    <div class="row">
      <div class="col-md-4 col-lg-4" style="text-align: center">
        <img class="img-circle" src="resources/images/logo.jpg" width="120" height="120" alt="logo">
      </div>
    </div>
  </div>


  <div class="row">

    <div><h3>Simula Agente</h3></div>
    <table>
      <tr>
        <th><label for="currentLevel">Livello</label></th>
        <th><label for="currentPerformance">Performance</label></th>
        <th><label for="simulatedAgent">Agente</label></th>
        <th></th>
      </tr>
      <tr>
        <th><input id="currentLevel" type="number" placeholder="1" min="1" max="${maxLevel}" value="1" step="1"></th>
        <th><input id="currentPerformance" type="number" placeholder="${maxLevel}" min="0.0" max="1.0" step="0.01" value="1.0"></th>
        <th>
          <select name="simulatedAgent" id="simulatedAgent">
            <option value="threshold" selected>Soglie</option>
            <option value="level">Livelli</option>
            <option value="incremental">Incrementale</option>
          </select>
        </th>
        <th><button onclick="simulateAgent()">Livello successivo</button></th>
      </tr>
    </table>
    <div id="stateValuesDiv"></div>
  </div>


  <!-- Configurazione agente a soglie -->

  <div class="row">
    <form id="thresholdAgentConfigForm">
      <!-- Campi del form -->
      <div class="form-group">
        <div><h3>Configurazione Agente a Soglie</h3></div>
        <div class="row">
          <div class="col-sm-12">
            <label for="startThreshold">Soglia iniziale</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="startThreshold" name="startThreshold" required min="0" max="1" step="any" value="${thresholdAgentConfig.startThreshold}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="deltaInferiorLevel">Delta Livello Inferiore</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="deltaInferiorLevel" name="deltaInferiorLevel" required min="0" max="1" step="any" value="${deltaInferiorLevel}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="thresholdDeltaPassed">Incremento Soglia Successi</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="thresholdDeltaPassed" name="thresholdDeltaPassed" required min="-1" max="1" step="any" value="${thresholdAgentConfig.thresholdDeltaPassed}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="thresholdDeltaNotPassed">Incremento Soglia Insuccessi</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="thresholdDeltaNotPassed" name="thresholdDeltaNotPassed" required min="-1" max="1" step="any" value="${thresholdAgentConfig.thresholdDeltaNotPassed}">
          </div>
        </div>
      </div>
    </form>

    <div class="row">
      <button onclick="postThresholdAgentParameters()">Aggiorna parametri</button>
    </div>
  </div>


  <!-- Configurazione agente a livelli -->

  <div class="row">
    <form id="levelAgentConfigForm">
      <!-- Campi del form -->
      <div class="form-group">
        <div><h3>Configurazione Agente a Livelli</h3></div>
        <div class="row">
          <div class="col-sm-12">
            <label for="targetPerformance">Performance Obiettivo (tra 0 e 1)</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="targetPerformance" name="targetPerformance" required min="0" max="1" step="any" value="${levelAgentConfig.targetPerformance}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="discountFactor">Discount</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="discountFactor" name="discountFactor" required min="0" max="1" step="any" value="${levelAgentConfig.discountFactor}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="policyEpsilon">Policy Epsilon</label>
          </div>
          <div class="col-sm-12">
            <input type="number" id="policyEpsilon" name="policyEpsilon" required min="0" max="1" step="any" value="${levelAgentConfig.policyEpsilon}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div><h3>Modello a priori dei pazienti</h3></div>
        <div class="row">

          <div class="col-sm-12">
            <label for="priorWeight">Peso modello a priori (tra 0 e 1)</label>
          </div>

          <div class="col-sm-12">
            <input type="number" id="priorWeight" name="priorWeight" required min="0" max="1" step="any" value="${levelAgentConfig.priorWeight}">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <label for="priorType">Tipo modello</label>
          </div>
          <div class="col-sm-12">
            <select name="priorType" id="priorType">
              <option value="linear" ${priorType == "linear" ? 'selected' : ''}>Funzione Lineare</option>
              <option value="sigmoid" ${priorType == "sigmoid" ? 'selected' : ''}>Funzione Sigmoidale</option>
            </select>
          </div>
        </div>
        <div class="row">

          <div class="col-sm-12">
            <label for="priorModelSlope">Slope coefficient (tra -1 e 1)</label>
          </div>

          <div class="col-sm-12">
            <input type="number" id="priorModelSlope" name="priorModelSlope" required min="-1" max="1" step="any" value="${levelAgentConfig.priorSlope}">
          </div>
        </div>
        <div class="row">

          <div class="col-sm-12">
            <label for="priorModelIntercept">Intercept coefficient (tra 0 e 1)</label>
          </div>

          <div class="col-sm-12">
            <input type="number" id="priorModelIntercept" name="priorModelIntercept" required min="-1" max="1" step="any" value="${levelAgentConfig.priorIntercept}">
          </div>
        </div>
      </div>
    </form>

    <div class="row">
      <button onclick="postLevelAgentParameters()">Aggiorna parametri</button>
    </div>


  </div>



  <div class="row">
    <div><h3>Aggiungi livello e performance</h3></div>
    <table>
      <tr>
        <th><label for="newRowLevel">Livello</label></th>
        <th><label for="newRowPerformance">Performance</label></th>
        <th></th>
      </tr>
      <tr>
        <th><input id="newRowLevel" type="number" placeholder="1" min="1" max="${maxLevel}" value="1"></th>
        <th><input id="newRowPerformance" type="number" placeholder="1.0" min="0.0" max="1.0" step="0.1" value="1.0"></th>
        <th><button id="addRowToTable" onclick="addRowToTable()">Aggiungi livello</button></th>
      </tr>
    </table>
  </div>



  <div id="tabulator"></div>


  <footer class="footer">
    <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
  </footer>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

<jsp:include page="modal-admin.jsp" />

<script>
  history.pushState(null, null, document.URL);
  window.addEventListener('popstate', function () {
    history.pushState(null, null, document.URL);
  });


  let maxLevel = ${maxLevel};

  let table = new Tabulator("#tabulator", {
    layout: "fitColumns",
    height:"300x",
    columns:[
      {
        title:"Level",
        field:"level",
        sorter:"number",
        validator:["integer", "min:1", "max:"+maxLevel],
        editor:"number", editorParams:{
          min:1,
          max:maxLevel,
          step:1
      }
      },
      {
        title:"Performance",
        field:"performance",
        sorter:"number",
        validator:["float", "min:0", "max:1"],
        editor:"number", editorParams:{
          min:0,
          max:1,
          step:0.01
        }
      }
    ],
  });

  function postLevelAgentParameters()
  {
    let form = document.getElementById("levelAgentConfigForm");
    let formData = new FormData(form);

    // add tabulator data
    //formData.append("performanceData", JSON.stringify(table.getData()));
    let data = {};
    formData.forEach((value, key) => data[key] = value);

    $.ajax({
      type: "POST",
      url: "rl-level-agent-config",
      data: data,
      dataType: "json"
    }).always(function(response) {
      alert(response.responseText);
    });

  }

  function postThresholdAgentParameters()
  {
    let form = document.getElementById("thresholdAgentConfigForm");
    let formData = new FormData(form);

    let data = {};
    formData.forEach((value, key) => data[key] = value);

    $.ajax({
      type: "POST",
      url: "rl-threshold-agent-config",
      data: data,
      dataType: "json"
    }).always(function(response) {
      alert(response.responseText);
    });

  }

  function simulateAgent()
  {
    let currentLevel = document.getElementById("currentLevel");
    let currentPerformance = document.getElementById("currentPerformance");
    let stateValuesDiv = document.getElementById("stateValuesDiv");
    let simulatedAgent = document.getElementById("simulatedAgent");

    if(!currentPerformance.checkValidity() || !currentLevel.checkValidity() || parseInt(currentLevel.value) > ${maxLevel})
    {
      alert("Inserire un valore valido per entrambi i campi di livello e livello massimo");
      return;
    }

    table.addRow({
      level: parseInt(currentLevel.value),
      performance: parseFloat(currentPerformance.value)
    });

    let data = {
      "performanceData" : JSON.stringify(table.getData()),
      "currentLevel" : parseInt(currentLevel.value),
      "simulatedAgent" : simulatedAgent.value,
      "maxLevel" : ${maxLevel}
    }
    $.ajax({
      type: "POST",
      url: "nextlevel",
      data: data,
      dataType: "json"
    }).always(function(response) {
      currentLevel.value = response.nextLevel;
      stateValuesDiv.innerHTML = response.stateValues;
    });

  }

  function addRowToTable()
  {
    let newRowLevel = document.getElementById("newRowLevel");
    let newRowPerformance = document.getElementById("newRowPerformance");
    if(!newRowLevel.checkValidity() || !newRowPerformance.checkValidity())
    {
      alert("Inserire un valore valido per entrambi i campi");
      return;
    }
    table.addRow({
      level: newRowLevel.value,
      performance: newRowPerformance.value
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
