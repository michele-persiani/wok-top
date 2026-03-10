import * as THREE from 'three';



export class Game
{
    static instance = null;

    /**
     * Inizializzazione del singleton.
     * @param {object} options - Configurazioni per l'istanza.
     *                           (es: tempo di gioco, dimensioni della scena, ecc.).
     */
    constructor(options = {}) {
        if (Game.instance) {
            return Game.instance; // Garantisce che esiste solo un'istanza del gioco
        }
        Game.instance = this;

        // Configurazioni personalizzabili, con valori predefiniti
        this.config = {
            totalTime: options.totalTime || 15,
            sceneColor: options.sceneColor || 0x87CEEB,
            cameraPosition: options.cameraPosition || { x: 3, y: 2, z: 5 },
            ...options,
        };

        this.OBSTACLE_TYPES = ["car", "motorbike", "pedestrian", "deer"];
        this.OBSTACLE_SIZES = {
            car: { width: 2, height: 1, color: 0xff0000 },
            motorbike: { width: 1, height: 1, color: 0x0000ff },
            pedestrian: { width: 0.5, height: 1.5, color: 0x00ff00 },
            deer: { width: 2, height: 1.5, color: 0x8b4513 },
        };


        // Inizializza variabili di stato
        this.timeLeft = this.config.totalTime;
        this.totalObstacles = 0;
        this.avoidedObstacles = 0;
        this.falsePositives = 0;

        // Collezioni di oggetti
        this.environmentObjects = [];
        this.obstacles = [];
        this.roadLines = [];

        // Three.js: Crea la scena, la camera, e il renderer
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(this.config.sceneColor);

        this.camera = new THREE.PerspectiveCamera(
            75, window.innerWidth / window.innerHeight, 0.1, 1000
        );
        this.camera.position.set(
            this.config.cameraPosition.x,
            this.config.cameraPosition.y,
            this.config.cameraPosition.z
        );

        this.renderer = new THREE.WebGLRenderer();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        document.body.appendChild(this.renderer.domElement);

        // Inizializzazione della scena
        this.setupLights();
        this.setupEnvironment();
        this.setupCamera();
        this.setupPlayerMotorcycle();

        // Event listener per il controllo del giocatore
        this.setupInput();


        this.obstacleSpawnInterval = setInterval(() => {
            const type = this.OBSTACLE_TYPES[Math.floor(Math.random() * this.OBSTACLE_TYPES.length)];
            this.createObstacle(type);
        }, 2000);

    }

    /**
     * Configura le luci della scena.
     */
    setupLights() {
        const light = new THREE.HemisphereLight(0xffffff, 0x444444, 1.2);
        this.scene.add(light);

        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
        directionalLight.position.set(-50, 100, -50);
        this.scene.add(directionalLight);
    }

    /**
     * Configura l'ambiente: strada, erba, linee stradali, oggetti di scena.
     */
    setupEnvironment() {
        // Materiali
        const roadMaterial = new THREE.MeshStandardMaterial({ color: 0x343434 });
        const grassMaterial = new THREE.MeshStandardMaterial({ color: 0x2E8B57 });
        const groundGeometry = new THREE.PlaneGeometry(200, 2000);

        // Oggetti
        const grass = new THREE.Mesh(groundGeometry, grassMaterial);
        grass.rotation.x = -Math.PI / 2;
        grass.position.y = -0.01;
        this.scene.add(grass);

        const road = new THREE.Mesh(new THREE.PlaneGeometry(10, 2000), roadMaterial);
        road.rotation.x = -Math.PI / 2;
        this.scene.add(road);

        // Linee stradali
        const roadLineGeometry = new THREE.PlaneGeometry(0.2, 5);
        const roadLineMaterial = new THREE.MeshStandardMaterial({ color: 0xFFFFFF });

        for (let z = -1000; z < 1000; z += 10) {
            const line = new THREE.Mesh(roadLineGeometry, roadLineMaterial);
            line.rotation.x = -Math.PI / 2;
            line.position.set(0, 0.01, z);
            this.scene.add(line);
            this.roadLines.push(line);
        }

        // Oggetti ambientali
        for (let z = -1000; z < 0; z += 40) {
            this.createEnvironmentRow(z);
        }
    }

    /**
     * Configura la telecamera.
     */
    setupCamera() {
        this.camera.lookAt(3, 0, -10);
    }

    /**
     * Crea una riga di oggetti nell'ambiente.
     * @param {number} zPosition - Posizione lungo l'asse Z.
     */
    createEnvironmentRow(zPosition) {
        const houseGeometry = new THREE.BoxGeometry(4, 4, 4);
        const houseMaterial = new THREE.MeshStandardMaterial({ color: 0x8B0000 });
        const treeGeometry = new THREE.CylinderGeometry(0.5, 1, 3);
        const treeMaterial = new THREE.MeshStandardMaterial({ color: 0x006400 });

        // Sinistra
        const leftHouse = new THREE.Mesh(houseGeometry, houseMaterial);
        leftHouse.position.set(-15, 2, zPosition);
        this.scene.add(leftHouse);

        const leftTree = new THREE.Mesh(treeGeometry, treeMaterial);
        leftTree.position.set(-20, 1.5, zPosition);
        this.scene.add(leftTree);

        // Destra
        const rightHouse = new THREE.Mesh(houseGeometry, houseMaterial);
        rightHouse.position.set(15, 2, zPosition + 10);
        this.scene.add(rightHouse);

        const rightTree = new THREE.Mesh(treeGeometry, treeMaterial);
        rightTree.position.set(20, 1.5, zPosition + 10);
        this.scene.add(rightTree);

        this.environmentObjects.push(
            { object: leftHouse, type: 'house' },
            { object: leftTree, type: 'tree' },
            { object: rightHouse, type: 'house' },
            { object: rightTree, type: 'tree' }
        );
    }

    createSimpleMotorbike()
    {
        const motoGroup = new THREE.Group();

        // Colori
        const bodyColor = 0x2233ff;      // Blu per la carrozzeria
        const tiresColor = 0x222222;     // Nero per le ruote
        const metalColor = 0x888888;     // Grigio per parti metalliche

        // Corpo principale (sella e carrozzeria)
        const bodyGeometry = new THREE.BoxGeometry(2, 0.8, 4);
        const bodyMaterial = new THREE.MeshPhongMaterial({ color: bodyColor });
        const body = new THREE.Mesh(bodyGeometry, bodyMaterial);
        body.position.y = 1;
        motoGroup.add(body);

        // Sella
        const seatGeometry = new THREE.BoxGeometry(1, 0.3, 1.5);
        const seatMaterial = new THREE.MeshPhongMaterial({ color: 0x222222 });
        const seat = new THREE.Mesh(seatGeometry, seatMaterial);
        seat.position.set(0, 1.4, 0);
        motoGroup.add(seat);

        // Ruote
        const wheelGeometry = new THREE.CylinderGeometry(0.5, 0.5, 0.3, 32);
        const wheelMaterial = new THREE.MeshPhongMaterial({ color: tiresColor });

        // Ruota anteriore
        const frontWheel = new THREE.Mesh(wheelGeometry, wheelMaterial);
        frontWheel.rotation.z = Math.PI / 2;
        frontWheel.position.set(0, 0.5, -1.5);
        motoGroup.add(frontWheel);

        // Ruota posteriore
        const backWheel = new THREE.Mesh(wheelGeometry, wheelMaterial);
        backWheel.rotation.z = Math.PI / 2;
        backWheel.position.set(0, 0.5, 1.5);
        motoGroup.add(backWheel);

        // Manubrio più alto e più vicino al giocatore
        const handlebarGeometry = new THREE.CylinderGeometry(0.05, 0.05, 1.5, 16);
        const handlebarMaterial = new THREE.MeshPhongMaterial({ color: metalColor });
        const handlebar = new THREE.Mesh(handlebarGeometry, handlebarMaterial);
        handlebar.position.set(0, 2.0, -1.2);  // Alzato e avvicinato
        handlebar.rotation.z = Math.PI / 2;
        motoGroup.add(handlebar);

        // Forcelle anteriori
        const forkGeometry = new THREE.CylinderGeometry(0.05, 0.05, 1.2, 16);
        const forkMaterial = new THREE.MeshPhongMaterial({ color: metalColor });

        const leftFork = new THREE.Mesh(forkGeometry, forkMaterial);
        leftFork.position.set(0.2, 1.2, -1.5);
        motoGroup.add(leftFork);

        const rightFork = new THREE.Mesh(forkGeometry, forkMaterial);
        rightFork.position.set(-0.2, 1.2, -1.5);
        motoGroup.add(rightFork);

        // Serbatoio più vicino al giocatore
        const tankGeometry = new THREE.BoxGeometry(1.2, 0.8, 1.2);
        const tankMaterial = new THREE.MeshPhongMaterial({ color: bodyColor });
        const tank = new THREE.Mesh(tankGeometry, tankMaterial);
        tank.position.set(0, 1.4, -0.6);  // Spostato più vicino
        motoGroup.add(tank);

        // Motore
        const engineGeometry = new THREE.BoxGeometry(0.8, 0.8, 1);
        const engineMaterial = new THREE.MeshPhongMaterial({ color: metalColor });
        const engine = new THREE.Mesh(engineGeometry, engineMaterial);
        engine.position.set(0, 0.8, 0.2);
        motoGroup.add(engine);

        // Scarico
        const exhaustGeometry = new THREE.CylinderGeometry(0.1, 0.1, 2, 16);
        const exhaustMaterial = new THREE.MeshPhongMaterial({ color: metalColor });
        const exhaust = new THREE.Mesh(exhaustGeometry, exhaustMaterial);
        exhaust.position.set(0.3, 0.5, 1);
        exhaust.rotation.z = Math.PI / 2;
        motoGroup.add(exhaust);

        // Posizionamento finale del gruppo moto
        motoGroup.position.set(3, -1, 4.5);      // Corsia destra, sotto la camera
        //motoGroup.rotation.y = Math.PI / 2;    // Orientata in avanti

        return motoGroup;
    }



    createObstacle(type)
    {
        const size = OBSTACLE_SIZES[type];
        const geometry = new THREE.BoxGeometry(size.width, size.height, size.width);
        const material = new THREE.MeshStandardMaterial({ color: size.color });
        let obstacle = new THREE.Mesh(geometry, material);


        // Posiziona gli ostacoli solo nella corsia destra
        obstacle.position.set(
            3, // Posizione fissa nella corsia destra
            size.height / 2,
            -300
        );

        obstacle.type = type;
        obstacle.inDangerZone = false;
        obstacle.avoided = false;

        //const loader = new THREE.GLTFLoader();
        //obstacle = loader.load("/resources/gltf/zombie.gltf")

        scene.add(obstacle);
        obstacles.push(obstacle);
    }



    /**
     * Crea una semplice motocicletta e la aggiunge alla scena.
     */
    setupPlayerMotorcycle()
    {
        const motoGroup = this.createSimpleMotorbike();

        // Implementazione del modello della moto
        motoGroup.position.set(3, -1, 4.5); // Posizione iniziale
        this.scene.add(motoGroup);
        this.playerBike = motoGroup; // Referenzia la moto del giocatore
    }



    /**
     * Configura l'input del giocatore.
     */
    setupInput()
    {
        document.addEventListener("keydown", (event) => {
            if (event.code === "Space") {
                // Logica per gestire il tasto spazio
                console.log("Player pressed Space");
            }
        });
    }


// Funzione per controllare le collisioni e attivare l'animazione
    checkCollisionAndAnimate(motorcycle, obstacle)
    {
    // Crea bounding box per la moto e l'ostacolo
    const motoBBox = new THREE.Box3().setFromObject(motorcycle);
    const obstacleBBox = new THREE.Box3().setFromObject(obstacle);

    // Controlla la collisione
    if (motoBBox.intersectsBox(obstacleBBox)) {
        // Previeni multiple animazioni contemporanee
        if (!motorcycle.isAnimating)
        {
            motorcycle.isAnimating = true;

            this.animateCollision(motorcycle);

            // Ripristina il flag dopo l'animazione
            setTimeout(() => {
                motorcycle.isAnimating = false;
            }, COLLISION_ANIMATION.DURATION);
        }
    }
    }


    animateCollision(motorcycle)
    {
        const startPosition = this.motorcycle.position.clone();
        const startRotation = motorcycle.rotation.clone();
        let startTime = null;
        let originalMaterials = [];

        // Salva i materiali originali
        motorcycle.traverse((child) => {
            if (child.isMesh) {
                originalMaterials.push({
                    mesh: child,
                    material: child.material.clone()
                });
            }
        });

        // Crea il materiale per il flash
        const flashMaterial = new THREE.MeshPhongMaterial({
            color: 0xff0000,
            emissive: 0xff0000,
            emissiveIntensity: 0.5
        });

        function flash() {
            motorcycle.traverse((child) => {
                if (child.isMesh) {
                    child.material = flashMaterial;
                }
            });

            // Ripristina i materiali originali dopo il flash
            setTimeout(() => {
                originalMaterials.forEach(({ mesh, material }) => {
                    mesh.material = material;
                });
            }, COLLISION_ANIMATION.FLASH_DURATION);
        }

        let direction = Math.random() > 0.5 ? 1 : -1;

        function animate(currentTime) {
            if (!startTime) startTime = currentTime;
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / COLLISION_ANIMATION.DURATION, 1);

            // Funzione di easing per un movimento più naturale
            const easeOutBounce = (x) => {
                const n1 = 7.5625;
                const d1 = 2.75;

                if (x < 1 / d1) {
                    return n1 * x * x;
                } else if (x < 2 / d1) {
                    return n1 * (x -= 1.5 / d1) * x + 0.75;
                } else if (x < 2.5 / d1) {
                    return n1 * (x -= 2.25 / d1) * x + 0.9375;
                } else {
                    return n1 * (x -= 2.625 / d1) * x + 0.984375;
                }
            };

            // Calcola il rimbalzo
            const bounceHeight = COLLISION_ANIMATION.BOUNCE_HEIGHT *
                Math.sin(progress * Math.PI) * (1 - progress);

            // Applica il movimento
            motorcycle.position.y = startPosition.y + bounceHeight;

            // Applica la rotazione
            const rotation = COLLISION_ANIMATION.ROTATION_ANGLE *
                Math.sin(progress * Math.PI) * (1 - easeOutBounce(progress)) *
                direction;
            motorcycle.rotation.z = startRotation.z + rotation;

            // Vibrazione casuale durante la collisione
            if (progress < 0.5) {
                motorcycle.position.x = startPosition.x +
                    (Math.random() - 0.5) * 0.2;
            }

            // Continua l'animazione se non è finita
            if (progress < 1) {
                requestAnimationFrame(animate);
            } else {
                // Ripristina la posizione e rotazione originale
                motorcycle.position.copy(startPosition);
                motorcycle.rotation.copy(startRotation);
            }
        }

        // Avvia il flash
        flash();

        // Avvia l'animazione
        requestAnimationFrame(animate);
    }



    /**
     * Avvia il rendering del gioco.
     */
    startGameLoop()
    {
        let scene = this.scene;
        let environmentObjects = this.environmentObjects;
        let obstacles = this.obstacles;
        let renderer = this.renderer;
        let camera = this.camera;
        let roadLines = this.roadLines;

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
                            this.createEnvironmentRow(lastZ - 40);
                        }
                    }
                }

                // Move obstacles
                for (const obstacle of obstacles)
                {
                    obstacle.position.z += 1;

                    if (obstacle.position.z > -5 && obstacle.position.z < 5 && !obstacle.avoided) {
                        if(!obstacle.inDangerZone)
                            this.totalObstacles += 1;
                        obstacle.inDangerZone = true;
                    } else
                        obstacle.inDangerZone = false;


                    if (obstacle.position.z > 9)
                    {
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


                this.obstacles.forEach(obstacle => {
                    this.checkCollisionAndAnimate(this.playerBike, obstacle);
                });

                renderer.render(scene, camera);
            };

        gameLoop();
    }
}
