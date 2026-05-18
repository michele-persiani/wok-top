import * as THREE from 'three';
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js';



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


        this.totalObstacles = 0;
        this.avoidedObstacles = 0;
        this.notAvoidedObstacles = 0;
        this.falsePositives = 0;


        // Configurazioni personalizzabili, con valori predefiniti
        this.config = {
            totalTime: options.totalTime || 30,
            sceneColor: options.sceneColor || 0x87CEEB,
            cameraPosition: options.cameraPosition || { x: 3, y: 2, z: 5 },
            speed: options.speed || 1,
            obstaclesSpawnDelay: options.obstaclesSpawnDelay || 2000,
            decoys: options.decoys || false,
            decoysSpawnDelay: options.decoysSpawnDelay || 5000,
            changeOfLane: options.changeOfLane || false,
            fog: options.fog || true,
            rain: options.rain || false,
            scale: options.scale || 1,
            ...options,
        };


        this.OBSTACLE_TYPES = ["car", "motorbike", "pedestrian", "deer"];
        this.OBSTACLE_SIZES = {
            car: { width: 2, height: 1, color: 0xff0000 },
            motorbike: { width: 1, height: 1, color: 0x0000ff },
            pedestrian: { width: 0.5, height: 1.5, color: 0x00ff00 },
            deer: { width: 2, height: 1.5, color: 0x8b4513 },
        };



        this.COLLISION_ANIMATION = {
            DURATION: 800,      // Durata totale dell'animazione in millisecondi
            BOUNCE_HEIGHT: 1,    // Altezza massima del rimbalzo
            ROTATION_ANGLE: Math.PI / 4,  // Rotazione massima (90 gradi)
            FLASH_DURATION: 200  // Durata del flash della moto in millisecondi
        };
        this.AVOID_ANIMATION = {
            DURATION: 300
        }


        // Inizializza variabili di stato
        this.timeLeft = this.config.totalTime;
        this.totalObstacles = 0;
        this.avoidedObstacles = 0;
        this.falsePositives = 0;
        this.initialized = false;


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

        this.gameTimer = null;
        this.obstacleSpawnInterval = null;

        this.onInitialized = () => {};
        this.onGameOver = () => {};

        this.initialize();
    }



    initialize()
    {
        // Carico le risorse
        this.loadTextures()
            .then(() =>
            {
                console.log("" + Object.keys(this.textures).length)
                // Inizializzazione della scena
                this.setupLights();
                this.setupCamera();
                this.setupEnvironment();
                this.initialized = true;
                this.onInitialized();
            })
        ;
    }






    /**
     * Function to load gltf elements from resources/gltf
     */
    loadTextures()
    {
        const gltfLoader = new GLTFLoader();

        const game = this;
        game.textures = {};
        let url = null;


        // Obstacles
        url = 'resources/gltf/pedestrian/pedestrian.gltf';
        let pr0 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.pedestrian = gltf.scene;
            });


        url = 'resources/gltf/car/car.gltf';
        let pr1 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.car = gltf.scene;
            })
        ;


        url = 'resources/gltf/motorbike/motorbike.gltf';
        let pr2 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.motorbike = gltf.scene;
                game.playerBike = gltf.scene.clone();
                game.playerBike.position.set(3, 1, 3.8);      // Corsia destra, sotto la camera
                game.playerBike.rotation.set(0, Math.PI, 0);
                game.scene.add(game.playerBike);
            })
        ;


        url = 'resources/gltf/deer/deer.gltf';
        let pr3 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.deer = gltf.scene;
            })
        ;



        // Row elements
        url = 'resources/gltf/house/house.gltf';
        let pr4 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.house = gltf.scene;
            })
        ;


        url = 'resources/gltf/tree/tree.gltf';
        let pr5 = gltfLoader.loadAsync(url)
            .then((gltf) => {
                game.textures.tree = gltf.scene;
            })
        ;

        return Promise.all([pr0, pr1, pr2, pr3, pr4, pr5]);
    }


    updateStats()
    {
        document.getElementById("timeLeft").textContent = this.timeLeft;
        document.getElementById("totalObstacles").textContent = this.totalObstacles;
        document.getElementById("avoidedObstacles").textContent = this.avoidedObstacles;
        document.getElementById("notAvoidedObstacles").textContent = this.totalObstacles - this.avoidedObstacles;
        document.getElementById("falsePositives").textContent = this.falsePositives;
    };



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
    setupEnvironment()
    {
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

        for (let z = -5000; z < 0; z += 10) {
            const line = new THREE.Mesh(roadLineGeometry, roadLineMaterial);
            line.rotation.x = -Math.PI / 2;
            line.position.set(0, 0.01, z);
            this.scene.add(line);
            this.roadLines.push(line);
        }

        // Oggetti ambientali
        for (let z = -5000; z < 0; z += 40) {
            this.createEnvironmentRow(z);
        }

        if(this.config.fog)
            this.addFog()


        if(this.config.rain)
            this.addRain()
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
    createEnvironmentRow(zPosition)
    {
        var spawnProba = 0.85;
        // Sinistra
        if(Math.random() > spawnProba)
        {
            const leftHouse = this.textures.house.clone();
            leftHouse.position.set(-15, 0, zPosition);
            this.scene.add(leftHouse);
            this.environmentObjects.push({ object: leftHouse, type: 'house' });
        }


        if(Math.random() > spawnProba)
        {
            const leftTree = this.textures.tree.clone();
            leftTree.position.set(-20, 0, zPosition);
            leftTree.scale.set(0.5, 0.5, 0.5);
            this.scene.add(leftTree);
            this.environmentObjects.push({ object: leftTree, type: 'tree' });
        }


        // Destra
        if(Math.random() > spawnProba)
        {
            // Destra
            const rightHouse = this.textures.house.clone();
            rightHouse.position.set(15, 0, zPosition + 10);
            this.scene.add(rightHouse);
            this.environmentObjects.push({ object: rightHouse, type: 'house' });
        }

        if(Math.random() > spawnProba)
        {
            const rightTree = this.textures.tree.clone();
            rightTree.position.set(20, 0, zPosition + 10);
            rightTree.scale.set(0.5, 0.5, 0.5);
            this.scene.add(rightTree);
            this.environmentObjects.push({ object: rightTree, type: 'tree' });
        }

    }


    createObstacle(type, decoy)
    {
        let obstacle = null;
        if(type === "pedestrian")
        {
            obstacle = this.textures.pedestrian.clone();
        }
        else if (type === "car")
        {
            obstacle = this.textures.car.clone();
        }
        else if(type === "motorbike")
        {
            obstacle = this.textures.motorbike.clone();
            obstacle.position.y = 1;
        }
        else if(type === "deer")
        {
            obstacle = this.textures.deer.clone();
            obstacle.position.y = 1;
        }
        else
        {
            const size = this.OBSTACLE_SIZES[type];
            const geometry = new THREE.BoxGeometry(size.width, size.height, size.width);
            const material = new THREE.MeshStandardMaterial({color: size.color});
            obstacle = new THREE.Mesh(geometry, material);
        }

        let scale = this.config.scale;
        obstacle.scale.set(scale, scale, scale);


        // Posiziona gli ostacoli solo nella corsia destra

        obstacle.position.x = 3;
        if(decoy)
            obstacle.position.x -= 6;
        obstacle.position.z = -300;


        obstacle.type = type;
        obstacle.inDangerZone = false;
        obstacle.avoided = true;
        obstacle.laneChanged = false;

        obstacle.isDecoy = decoy;
        this.scene.add(obstacle);
        this.obstacles.push(obstacle);
    }


    performObstacleAvoidance()
    {
        if(this.playerBike.isAnimating)
            return;

        let pos = structuredClone(this.playerBike.position);
        let trg = structuredClone(this.playerBike.position);
        trg.x -= 2;
        let game = this;
        if(!this.playerBike.isAnimating)
        {
            let animationDuration = game.AVOID_ANIMATION.DURATION;
            game.playerBike.isAnimating = true;
            this.animateObjectMovement(game.playerBike, game.playerBike.position, trg, 75, () => {
                setTimeout(() => {
                    this.animateObjectMovement(game.playerBike, trg, pos, 75, () => {
                        game.playerBike.isAnimating = false;
                    });
                }, animationDuration)

            })
        }

        let dangerDetected = false;
        for (const obstacle of this.obstacles)
            if (obstacle.inDangerZone)
                dangerDetected = true;


        if (!dangerDetected)
            this.falsePositives += 1;
    }





    // Funzione per controllare le collisioni e attivare l'animazione
    checkCollisionAndAnimate(obstacle)
    {
        // Crea bounding box per la moto e l'ostacolo
        const motoBBox = new THREE.Box3().setFromObject(this.playerBike);
        const obstacleBBox = new THREE.Box3().setFromObject(obstacle);

        motoBBox.min.y = -100;
        motoBBox.max.y = 100;

        // Controlla la collisione
        if (motoBBox.intersectsBox(obstacleBBox) && obstacle.avoided)
        {
            obstacle.avoided = false;
            this.notAvoidedObstacles += 1;
            console.log("Collisione rilevata");
            // Previeni multiple animazioni contemporanee
            this.animateCollision();
        }
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

            if(!this.initialized)
                return;

            let zMovement = this.config.speed;

            // Move environment objects
            for (let i = environmentObjects.length - 1; i >= 0; i--) {
                const envObj = environmentObjects[i];
                envObj.object.position.z += zMovement;

                // If object passes the player, remove it and create new one at the back
                if (envObj.object.position.z > 10) {
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
                obstacle.position.z += zMovement;

                // Change of lane
                if(obstacle.position.z > -100 && !obstacle.laneChanged && this.config.changeOfLane)
                {
                    obstacle.laneChanged = true;
                    if(Math.random() > 0.9)
                    {
                        obstacle.laneChanged = true;
                        var trg = structuredClone(obstacle.position);
                        if(obstacle.position.x > 0)
                        {
                            trg.x -= 6;
                            this.animateObjectMovement(obstacle, obstacle.position, trg, 500)
                        }
                        else
                        {
                            trg.x += 6;
                            this.animateObjectMovement(obstacle, obstacle.position, trg, 500)
                        }
                    }
                }



                // Set danger zone
                // Se ostacolo e' sufficientemente vicino e nella corsia di destra
                if (obstacle.position.z > -20 && obstacle.position.x > 0)
                {
                    if(!obstacle.inDangerZone)
                        this.totalObstacles += 1;
                    obstacle.inDangerZone = true;
                }
                else
                    obstacle.inDangerZone = false;

                // Remove obstacle if out of scene
                if (obstacle.position.z > 5)
                {
                    if(obstacle.avoided && obstacle.position.x > 0)
                        this.avoidedObstacles += 1;

                    console.log("avoided: " + obstacle.avoided)

                    scene.remove(obstacle);
                    // Animazione incidente va qui
                    obstacles.splice(obstacles.indexOf(obstacle), 1);
                }
            }



            // Move road lines
            for (const line of roadLines)
            {
                line.position.z += zMovement;
                if (line.position.z > 10)
                    line.position.z = -100;
            }


            this.obstacles.forEach(obstacle => {
                this.checkCollisionAndAnimate(obstacle);
            });

            renderer.render(scene, camera);
            this.updateRain();
        };


        gameLoop();


        this.gameTimer = setInterval(() => {
            this.timeLeft -= 1;
            if (this.timeLeft <= 0)
            {
                if(this.gameTimer != null)
                    clearInterval(this.gameTimer);

                if(this.obstacleSpawnInterval != null)
                    clearInterval(this.obstacleSpawnInterval);

                this.onGameOver();
            }
            this.updateStats();
        }, 1000);


        this.obstacleSpawnInterval = setInterval(() => {
            const type = this.OBSTACLE_TYPES[Math.floor(Math.random() * this.OBSTACLE_TYPES.length)];
            let isDecoy = this.config.decoys ? Math.random() > 0.5 : false;
            this.createObstacle(type, isDecoy);
        }, this.config.obstaclesSpawnDelay);

    }




    // EFFETTI AMBIENTALI

    /**
     * Applica la nebbia alla scena.
     * @param {number} color - Colore della nebbia, rappresentato come un intero esadecimale (ad esempio 0x87CEEB).
     * @param {number} near - Distanza minima a cui iniziare a rendere visibile la nebbia.
     * @param {number} far - Distanza massima oltre la quale gli oggetti sono completamente avvolti dalla nebbia.
     */
    addFog(color = 0xD3D3D3, near = -500, far = 600) {
        this.scene.fog = new THREE.Fog(color, near, far);
    }


    addRain()
    {
        const rainGeometry = new THREE.BufferGeometry();
        const rainCount = 5000; // Numero di particelle per la pioggia
        const positions = [];

        // Genera le posizioni iniziali delle particelle
        for (let i = 0; i < rainCount; i++) {
            const x = (Math.random() - 0.5) * 100; // Area della pioggia
            const y = Math.random() * 50 + 20;     // Altezza minima e massima della pioggia
            const z = (Math.random() - 0.5) * 100; // Profondità dell'area della pioggia
            positions.push(x, y, z);
        }

        rainGeometry.setAttribute(
            'position',
            new THREE.Float32BufferAttribute(positions, 3)
        );

        const rainMaterial = new THREE.PointsMaterial({
            color: 0xaaaaaa, // Colore della pioggia
            size: 0.2,       // Dimensione delle particelle
            transparent: true,
        });

        // Crea il sistema di particelle
        this.rain = new THREE.Points(rainGeometry, rainMaterial);
        this.scene.add(this.rain);
    }

    /**
     * Aggiorna il movimento della pioggia nel ciclo del gioco.
     */
    updateRain()
    {
        if (this.rain) {
            const positions = this.rain.geometry.attributes.position.array;
            for (let i = 0; i < positions.length; i += 3) {
                positions[i + 1] -= 0.5; // Movimento verso il basso (asse Y)

                // Reinizializza le particelle che escono dalla scena
                if (positions[i + 1] < 0) {
                    positions[i + 1] = Math.random() * 50 + 20;  // Nuova altezza
                    positions[i] = (Math.random() - 0.5) * 100;  // Nuova posizione in X
                    positions[i + 2] = (Math.random() - 0.5) * 100; // Nuova posizione in Z
                }
            }

            // Indica a Three.js che la geometria è stata aggiornata
            this.rain.geometry.attributes.position.needsUpdate = true;
        }
    }




    // ANIMAZIONI


    animateCollision()
    {
        let motorcycle = this.playerBike;
        const startPosition = motorcycle.position.clone();
        const startRotation = motorcycle.rotation.clone();
        let startTime = null;
        let originalMaterials = [];
        const collisionAnimation = this.COLLISION_ANIMATION;


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

        function flash()
        {
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
            }, collisionAnimation.DURATION);
        }


        let direction = Math.random() > 0.5 ? 1 : -1;


        const game = this;

        function animate(currentTime)
        {
            if(game.playerBike ==  null)
                return;


            if (!startTime) startTime = currentTime;
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / collisionAnimation.DURATION, 1);




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
            const bounceHeight = collisionAnimation.BOUNCE_HEIGHT *
                Math.sin(progress * Math.PI) * (1 - progress);

            // Applica il movimento
            game.playerBike.position.y = startPosition.y + bounceHeight;


            // Applica la rotazione
            const rotation = collisionAnimation.ROTATION_ANGLE *
                Math.sin(progress * Math.PI) * (1 - easeOutBounce(progress)) *
                direction;
            game.playerBike.rotation.z = startRotation.z + rotation;

            // Vibrazione casuale durante la collisione
            if (progress < 0.5) {
                game.playerBike.position.x = startPosition.x + (Math.random() - 0.5) * 0.2;
            }

            // Continua l'animazione se non è finita
            if (progress < 1) {
                requestAnimationFrame(animate);
            } else {
                // Ripristina la posizione e rotazione originale
                game.playerBike.position.copy(startPosition);
                game.playerBike.rotation.copy(startRotation);

            }
        }


        // Avvia il flash
        flash();

        if(this.playerBike.isAnimating)
            return;

        this.playerBike.isAnimating = true;
        setTimeout(() => {
            game.playerBike.isAnimating = false;
        }, collisionAnimation.DURATION);
        // Avvia l'animazione
        requestAnimationFrame(animate);
    }



    /**
     * Crea un'animazione per spostare un oggetto tra due punti.
     * @param {THREE.Object3D} object - Oggetto da animare.
     * @param {THREE.Vector3} startPoint - Punto di partenza.
     * @param {THREE.Vector3} endPoint - Punto di arrivo.
     * @param {number} duration - Durata dell'animazione in millisecondi.
     * @param {function} onComplete - Callback da eseguire al completamento dell'animazione (opzionale).
     */
    animateObjectMovement(object, startPoint, endPoint, duration, onComplete = null) {
        if (!object) {
            console.error("Oggetto non valido per l'animazione");
            return;
        }

        const startTime = performance.now();

        const animate = (currentTime) => {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1); // Calcola la frazione completata (0 a 1)
            const easedProgress = this.easeInOutQuad(progress); // Applica easing

            // Aggiorna la posizione dell'oggetto
            object.position.lerpVectors(startPoint, endPoint, easedProgress);

            // Continua l'animazione fino al completamento
            if (progress < 1) {
                requestAnimationFrame(animate);
            } else if (onComplete) {
                onComplete(); // Esegui la funzione di completamento (se presente)
            }
        };

        // Avvia l'animazione
        requestAnimationFrame(animate);
    }

    /**
     * Funzione di easing (ease-in-out).
     * Rallenta l'inizio e la fine dell'animazione per un movimento più naturale.
     * @param {number} t - Il valore di progresso (da 0 a 1).
     * @returns {number} - Valore interpolato.
     */
    easeInOutQuad(t) {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }

}
