<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attention - Motorbike</title>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/three@0.154.0/build/three.min.js"></script>
    <script type="text/javascript" src="resources/js/hello.js"></script>
    <script type="text/javascript" src="resources/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/misc/motorbike.js"></script>

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
</head>
<body>

<div id="stats">
    Tempo: <span id="timeLeft">0</span>s<br>
    Ostacoli: <span id="totalObstacles">0</span><br>
    Evitati: <span id="avoidedObstacles">0</span><br>
    Falsi positivi: <span id="falsePositives">0</span>
</div>

<script>

    // Three.js setup
    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0x87CEEB);

    const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    const renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    document.body.appendChild(renderer.domElement);

    // Lighting
    const light = new THREE.HemisphereLight(0xffffff, 0x444444, 1.2);
    scene.add(light);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
    directionalLight.position.set(-50, 100, -50);
    scene.add(directionalLight);

    // Ground
    const groundGeometry = new THREE.PlaneGeometry(200, 2000);
    const roadMaterial = new THREE.MeshStandardMaterial({ color: 0x343434 });
    const grassMaterial = new THREE.MeshStandardMaterial({ color: 0x2E8B57 });

    const grass = new THREE.Mesh(groundGeometry, grassMaterial);
    grass.rotation.x = -Math.PI / 2;
    grass.position.y = -0.01;
    scene.add(grass);

    const road = new THREE.Mesh(new THREE.PlaneGeometry(10, 2000), roadMaterial);
    road.rotation.x = -Math.PI / 2;
    scene.add(road);

    // Road Lines
    const roadLineGeometry = new THREE.PlaneGeometry(0.2, 5);
    const roadLineMaterial = new THREE.MeshStandardMaterial({ color: 0xFFFFFF });

    const roadLines = [];
    for (let z = -1000; z < 1000; z += 10) {
        const line = new THREE.Mesh(roadLineGeometry, roadLineMaterial);
        line.rotation.x = -Math.PI / 2;
        line.position.set(0, 0.01, z);
        scene.add(line);
        roadLines.push(line);
    }

    // Environment Objects (Houses and Trees)
    const environmentObjects = [];

    const houseGeometry = new THREE.BoxGeometry(4, 4, 4);
    const houseMaterial = new THREE.MeshStandardMaterial({ color: 0x8B0000 });
    const treeGeometry = new THREE.CylinderGeometry(0.5, 1, 3);
    const treeMaterial = new THREE.MeshStandardMaterial({ color: 0x006400 });


    // Create initial environment
    for (let z = -1000; z < 0; z += 40) {
        createEnvironmentRow(z);
    }

    // Motorbike handlebars
    const handlebarGeometry = new THREE.CylinderGeometry(0.3, 0.3, 4);
    const handlebarMaterial = new THREE.MeshStandardMaterial({ color: 0x333333 });
    const leftHandlebar = new THREE.Mesh(handlebarGeometry, handlebarMaterial);
    leftHandlebar.position.set(2, -0.5, -2);
    leftHandlebar.rotation.z = Math.PI / 2;

    const rightHandlebar = new THREE.Mesh(handlebarGeometry, handlebarMaterial);
    rightHandlebar.position.set(4, -0.5, -2);
    rightHandlebar.rotation.z = Math.PI / 2;

    scene.add(leftHandlebar);
    scene.add(rightHandlebar);

    setupPlayerMotorcycle();

    // Obstacles
    const obstacles = [];
    const OBSTACLE_TYPES = ["car", "motorbike", "pedestrian", "deer"];
    const OBSTACLE_SIZES = {
        car: { width: 2, height: 1, color: 0xff0000 },
        motorbike: { width: 1, height: 1, color: 0x0000ff },
        pedestrian: { width: 0.5, height: 1.5, color: 0x00ff00 },
        deer: { width: 2, height: 1.5, color: 0x8b4513 },
    };

    let totalTime = 15;
    // Game variables
    let timeLeft = totalTime;
    let totalObstacles = 0;
    let avoidedObstacles = 0;
    let falsePositives = 0;

    const updateStats = () => {
        document.getElementById("timeLeft").textContent = timeLeft;
        document.getElementById("totalObstacles").textContent = totalObstacles;
        document.getElementById("avoidedObstacles").textContent = avoidedObstacles;
        document.getElementById("falsePositives").textContent = falsePositives;
    };

    const timer = setInterval(() => {
        timeLeft -= 1;
        if (timeLeft <= 0) {
            clearInterval(timer);
            clearInterval(obstacleSpawnInterval);
            calculateResult();
        }
        updateStats();
    }, 1000);

    const obstacleSpawnInterval = setInterval(() => {
        const type = OBSTACLE_TYPES[Math.floor(Math.random() * OBSTACLE_TYPES.length)];
        createObstacle(type);
        totalObstacles += 1;
        updateStats();
    }, 2000);

    const gameLoop = () => {
        requestAnimationFrame(gameLoop);

        // Move environment objects
        for (let i = environmentObjects.length - 1; i >= 0; i--) {
            const envObj = environmentObjects[i];
            envObj.object.position.z += 1;

            // If object passes the player, remove it and create new one at the back
            if (envObj.object.position.z > 50) {
                scene.remove(envObj.object);
                environmentObjects.splice(i, 1);

                // When last object of a row is removed, create new row at the back
                if (i === 0 || i % 4 === 0) {
                    const lastZ = Math.min(...environmentObjects.map(obj => obj.object.position.z));
                    createEnvironmentRow(lastZ - 40);
                }
            }
        }

        // Move obstacles
        for (const obstacle of obstacles) {
            obstacle.position.z += 1;

            if (obstacle.position.z > -5 && obstacle.position.z < 5 && !obstacle.avoided) {
                obstacle.inDangerZone = true;
            } else {
                obstacle.inDangerZone = false;
            }

            if (obstacle.position.z > 9) {
                scene.remove(obstacle);
                // Animazione incidente va qui
                obstacles.splice(obstacles.indexOf(obstacle), 1);
            }
        }

        // Move road lines
        for (const line of roadLines) {
            line.position.z += 1;
            if (line.position.z > 10) {
                line.position.z = -100;
            }
        }
        obstacles.forEach(obstacle => {
            checkCollisionAndAnimate(playerBike, obstacle);
        });


        renderer.render(scene, camera);
    };

    // Handle player input
    document.addEventListener("keydown", (event) => {
        if (event.code === "Space") {
            let dangerDetected = false;

            for (const obstacle of obstacles) {
                if (obstacle.inDangerZone && !obstacle.avoided) {
                    obstacle.avoided = true;
                    obstacle.position.x = 0;
                    avoidedObstacles += 1;
                    dangerDetected = true;
                }
            }

            if (!dangerDetected) {
                falsePositives += 1;
            }
            updateStats();
        }
    });

    // Camera setup
    //camera.position.set(3, 2, 5);
    //camera.lookAt(3, 0, -10);

    setupCamera();

    updateStats();
    gameLoop();


    function calculateResult() {
        let nCorrect = avoidedObstacles;
        let nWrong = totalObstacles - avoidedObstacles;
        let nMissed = falsePositives;
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
                "ptime": timeLeft,
                "maxtime": totalTime
            },
            function(data, status)
            {
                var js=JSON.parse(data);
                var perf = js.perf;
                var passed = js.passed;
                var passedMessage='';
                <c:if test="${difficulty!='training'}">
                    passedMessage = passed ? '<b>Ottimo! Esercizio superato!</b>' : '<b>Mi spiace, esercizio non superato</b>';
                </c:if>

                let dialogMsg = '<h4><b>Tempo</b>: ' + timeLeft + ' secondi' +
                    '<br>' +
                    '<br><b>Risposte esatte</b>: ' + nCorrect +
                    '<br><b>Risposte sbagliate</b>: ' + nWrong +
                    '<br><b>Omissioni</b>: ' + nMissed +
                    '<br>' +
                    '<br><b>Prestazione</b>: ' + Math.round(perf*100) + ' %' +
                    '<br>' +
                    '<br>' + passedMessage + '</h4>';
                alert(dialogMsg);
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
                    time: timeLeft,
                    pTime: timeLeft,
                    pCorrect: nCorrect,
                    pMissed: nMissed,
                    pWrong: nWrong
                }, 'get');
                /*
                bootbox.alert({
                    size:'small',
                    message: dialogMsg,
                    callback:function() {
                        post('reflexes1phase3', {
                            difficulty: '${difficulty}',
                            level: '${level}',
                            patientid: '${patientid}',
                            exerciseid: '${exerciseid}',
                            sessid: '${sessid}',
                            lastexercisepassed: '${lastexercisepassed}',
                            exname: '${exname}',
                            passed: passed,
                            time: timeLeft,
                            pTime: timeLeft,
                            pCorrect: nCorrect,
                            pMissed: nMissed,
                            pWrong: nWrong
                        }, 'get');
                    }
                });
                */
            });
    }
</script>

</body>
</html>