
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
  <meta name="author" content="Michele Persiani">
  <link rel="icon" href="resources/favicon.ico">

  <title>Download Statistiche</title>

  <!-- Bootstrap core CSS -->
  <link rel="stylesheet" href="resources/css/bootstrap.min.css">

  <!-- font-awesome -->
  <link rel="stylesheet" href="resources/css/font-awesome.min.css">

  <!-- jQuery library -->
  <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">

  <!-- Latest compiled and minified JavaScript -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>

  <!-- Latest compiled JavaScript -->
  <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

  <!-- Custom styles for this template -->
  <link rel="stylesheet" href="resources/css/custom.css">
  <script type="text/javascript" src="resources/js/custom.js"></script>
  <link rel="stylesheet" href="resources/jumbotron.css">
  <!--link rel="stylesheet" href="resources/signin.css"-->

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <!-- Latest compiled bootbox -->
  <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

  <script type="text/javascript" src="resources/js/hello.js"></script>
</head>

<body>
<div class="container">
  <div class="header clearfix">
    <nav>
      <ul class="nav nav-pills pull-right">
        <li role="presentation">
          <a href="patientsstatistics">
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

  <div class="well">
    <div><h2>Download delle statistiche</h2></div>

    <form:form id="downloadForm" class="form-inline" method="post" modelAttribute="downloadForm" action="downloadstatisticsaction">
      <div class="row">
        <div class="col-sm-8">
          <spring:bind path="patients">
            <div class="form-group">
              <label>Pazienti</label>
              <form:select id="selectPatients" path="patients" class="form-control selectpicker" data-style="btn-warning">
                <c:forEach var="patient" items="${patients}">
                  <form:option value="${patient.id}">${patient.name} ${patient.surname}</form:option>
                </c:forEach>
              </form:select>
            </div>
          </spring:bind>
        </div>
        <div class="col-sm-6">
          <spring:bind path="phone">
            <div class="form-group">
              <label>Telefono</label>
              <form:input path="phone" class="form-control" id="phone" placeholder="Telefono" />
            </div>
          </spring:bind>
        </div>
        <div class="col-sm-4">
          <spring:bind path="email">
            <div class="form-group">
              <label>Email</label>
              <form:input path="email" class="form-control" id="email" placeholder="Email" />
            </div>
          </spring:bind>
        </div>
      </div>

      <div class="form-group">
        <button id="submit-data" type="submit" class="btn btn-success">Download</button>
      </div>
    </form:form>
  </div>

  <footer class="footer">
    <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
  </footer>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

<jsp:include page="modal-grp-sess.jsp" />

<script type="text/javascript">


  $('#downloadForm').submit(function (e) {
    e.preventDefault();

    let formData = $('#downloadForm').serializeArray();

    let patientIds = Array.from(formData)
            .filter(field => field.name === 'patients')
            .map(field => Number(field.value));

    let phone = Array.from(formData)
            .find(field => field.name === 'phone')
            .value;

    let email = Array.from(formData)
            .find(field => field.name === 'email')
            .value;

    $.ajax({
      url: "metrics/history",
      data: {
        userId : patientIds,
        email : email,
        phone : phone
      },
      success: function(data){
        let csv = createCSV(data["fields"], data["data"])
        download("statistiche.csv", csv, "text/csv;charset=utf-8;header=present")
      },
      dataType: "json",
      traditional : true
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
  $(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
  });
</script>

</body>
</html>
