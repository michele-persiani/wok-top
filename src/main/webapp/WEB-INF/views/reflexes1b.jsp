<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attention - Motorbike</title>
    <!--<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/three@0.154.0/build/three.min.js"></script>-->
    <script type="text/javascript" src="resources/js/hello.js"></script>
    <script type="text/javascript" src="resources/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>
    <!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js"></script>-->



    <style>
        body {
            margin: 0;
            overflow: hidden;
        }
        canvas {
            display: block;
        }
        #stats {
            position: absolute;
            top: 10px;
            left: 10px;
            color: white;
            background: rgba(0, 0, 0, 0.5);
            padding: 10px;
            font-family: Arial, sans-serif;
            border-radius: 5px;
            z-index: 10;
        }
    </style>

    <script type="importmap">
        {
          "imports": {
            "three": "https://cdn.jsdelivr.net/npm/three@0.154.0/build/three.module.js",
            "three/addons/": "https://cdn.jsdelivr.net/npm/three@0.154.0/examples/jsm/"
          }
        }
    </script>

</head>
<body>

<div id="stats">
    Tempo: <span id="timeLeft">0</span>s<br>
    Ostacoli: <span id="totalObstacles">0</span><br>
    Evitati: <span id="avoidedObstacles">0</span><br>
    Falsi positivi: <span id="falsePositives">0</span>
</div>

<script type="module" src="${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js"></script>
<script type="module">
    import { Game } from '${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js';

    const game = new Game(
        {
            totalTime: 15,
        }
    );



    game.startGameLoop();




    function calculateResult() {
        let nCorrect = game.avoidedObstacles;
        let nWrong = game.totalObstacles - game.avoidedObstacles;
        let nMissed = game.falsePositives;
        let totalTime = game.config.totalTime;
        let elapsedTime = game.config.totalTime - game.timeLeft;
        $.get("getperformance",
            {
                "exerciseid": ${exerciseid},
                "patientid": ${patientid},
                "sessid": ${sessid},
                "difficulty": '${difficulty}',
                "level": ${level},
                "pcorrect": nCorrect,
                "pwrong": nWrong,
                "pmissed": nMissed,
                "ptime": elapsedTime,
                "maxtime": totalTime,
                "assignmentid": ${assignmentid}
            },
            function(data, status)
            {
                var js=JSON.parse(data);
                var perf = js.perf;
                var passed = js.passed;
                var thr = js.thr;
                var passedMessage='Esercizio concluso. Performance=' + perf;
                <c:if test="${difficulty!='training'}">
                passedMessage = passed ? 'Ottimo! Esercizio superato! Performance=' + perf + "/" + thr : 'Mi spiace, esercizio non superato. Performance=' + perf + "/" + thr;
                </c:if>


                alert(passedMessage)

                post('reflexes1phase3', {
                    difficulty: '${difficulty}',
                    level: '${level}',
                    patientid: '${patientid}',
                    exerciseid: '${exerciseid}',
                    sessid: '${sessid}',
                    lastexercisepassed: '${lastexercisepassed}',
                    exname: '${exname}',
                    type: '${type}',
                    passed: passed,
                    time: totalTime,
                    pTime: elapsedTime,
                    pCorrect: nCorrect,
                    pMissed: nMissed,
                    pWrong: nWrong,
                    assignmentid: ${assignmentid}
                }, 'get');
            });
    }
</script>

</body>
</html>