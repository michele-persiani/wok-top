<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attention - Motorbike</title>
    <!--<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/three@0.154.0/build/three.min.js"></script>-->
    <script type="text/javascript" src="resources/js/hello.js"></script>
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="resources/js/bootbox.min.js"></script>
    <!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js"></script>-->


    <script type="importmap">
        {
          "imports": {
            "three": "https://cdn.jsdelivr.net/npm/three@0.154.0/build/three.module.js",
            "three/addons/": "https://cdn.jsdelivr.net/npm/three@0.154.0/examples/jsm/"
          }
        }
    </script>
    <script type="module" src="${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js"></script>


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
            top: 80px;
            left: 10px;
            color: white;
            background: rgba(0, 0, 0, 0.5);
            padding: 10px;
            font-family: Arial, sans-serif;
            border-radius: 5px;
            z-index: 10;
        }
    </style>

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="resources/jumbotron.css">
</head>
<body>

    <div id="stats" class="row">
        Tempo: <span id="timeLeft">0</span>s<br>
        Ostacoli: <span id="totalObstacles">0</span><br>
        Evitati: <span id="avoidedObstacles">0</span><br>
        Non evitati: <span id="notAvoidedObstacles">0</span><br>
        Falsi positivi: <span id="falsePositives">0</span>
    </div>

<div id="startButtonDiv" class="row" style="text-align: center; position: absolute; top: 45%; left:45%">
    <input type="button" id="startButton" value="Caricamento..." style="font-size: 50px; padding: 30px;"/>
</div>

<script type="module">
    import { Game } from '${pageContext.request.contextPath}/resources/js/motorbike/motorbike.js';

    const game = new Game(
        {
            totalTime: ${totalTime},
            decoys: ${decoys},
            obstaclesDelay: ${obstaclesSpawnDelay},
            speed: ${speed},
            changeOfLane: ${changeOfLane},
            fog: ${fog},
            rain: ${rain},
            scale: ${scale},
        }
    );



    function calculateResult() {
        let nObstacles = game.totalObstacles;
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
                    passedMessage = passed ? 'Ottimo! Esercizio superato!\n' : 'Mi spiace, esercizio non superato.\n';
                </c:if>


                bootbox.alert({
                    size:'small',
                    message:'<h4><br><b>Numero ostacoli</b>: ' + nObstacles +
                        '<br><b>Ostacoli evitati</b>: ' + nCorrect +
                        '<br><b>Ostacoli non evitati</b>: ' + nWrong +
                        '<br><b>Falsi positivi</b>: ' + nMissed +
                        '<br>' +
                        '<br><b>Performance</b>: ' + Math.floor(perf * 100) + '%' +
                        '<br><b>Soglia Superamento</b>: ' + Math.floor(thr * 100) + '%' +
                        '<br>' +
                        '<br>' + passedMessage + '</h4>',
                    callback:function() {
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
                            rlagent: '${rlagent}',
                            assignmentid: ${assignmentid}
                        }, 'get');
                    }
                });
            });
    }

    let clickCallback = () => game.performObstacleAvoidance();

    game.onInitialized = () => {
        // Loading button becomes start
        document.getElementById("startButton").value = "Inizia";

        // Setup the start game button
        document.getElementById("startButton").onclick = function() {

            document.getElementById("startButtonDiv").style.visibility = 'hidden';

            // Enter full screen mode
            if(isMobile())
                document.body.requestFullscreen();

            // Use a delay
            setTimeout(() => {
                // Add callback for space key
                document.addEventListener("keydown", (event) => {
                    if (event.code === "Space")
                        game.performObstacleAvoidance();
                });

                // Add click callback to avoid obstacles
                window.addEventListener('click', clickCallback);
            }, 200)

            // Start the game
            game.startGameLoop();
        }
    };

    game.onGameOver = () => {
        // Exit full screen mode
        if(isMobile())
            document.exitFullscreen();
        document.removeEventListener('click', clickCallback)
        calculateResult();
    };
</script>

</body>
</html>