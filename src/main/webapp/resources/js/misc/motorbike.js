

function createEnvironmentRow(zPosition) {
    // Left side
    const leftHouse = new THREE.Mesh(houseGeometry, houseMaterial);
    leftHouse.position.set(-15, 2, zPosition);
    scene.add(leftHouse);

    const leftTree = new THREE.Mesh(treeGeometry, treeMaterial);
    leftTree.position.set(-20, 1.5, zPosition);
    scene.add(leftTree);

    // Right side
    const rightHouse = new THREE.Mesh(houseGeometry, houseMaterial);
    rightHouse.position.set(15, 2, zPosition + 10);
    scene.add(rightHouse);

    const rightTree = new THREE.Mesh(treeGeometry, treeMaterial);
    rightTree.position.set(20, 1.5, zPosition + 10);
    scene.add(rightTree);

    environmentObjects.push(
        { object: leftHouse, type: 'house' },
        { object: leftTree, type: 'tree' },
        { object: rightHouse, type: 'house' },
        { object: rightTree, type: 'tree' }
    );
}



function createObstacle(type) {
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






function createSimpleMotorbike()
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

// Aggiorna anche la posizione della camera per una migliore visuale
function setupCamera() {
    camera.position.set(3, 2, 5);
    camera.lookAt(3, 0, -10);
}

function setupPlayerMotorcycle() {
    const playerBike = createSimpleMotorbike();
    scene.add(playerBike);
    window.playerBike = playerBike;
}


// Costanti per l'animazione
const COLLISION_ANIMATION = {
    DURATION: 800,      // Durata totale dell'animazione in millisecondi
    BOUNCE_HEIGHT: 1,    // Altezza massima del rimbalzo
    ROTATION_ANGLE: Math.PI / 4,  // Rotazione massima (90 gradi)
    FLASH_DURATION: 200  // Durata del flash della moto in millisecondi
};

function animateCollision(motorcycle)
{
    const startPosition = motorcycle.position.clone();
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

// Funzione per controllare le collisioni e attivare l'animazione
function checkCollisionAndAnimate(motorcycle, obstacle) {
    // Crea bounding box per la moto e l'ostacolo
    const motoBBox = new THREE.Box3().setFromObject(motorcycle);
    const obstacleBBox = new THREE.Box3().setFromObject(obstacle);

    // Controlla la collisione
    if (motoBBox.intersectsBox(obstacleBBox)) {
        // Previeni multiple animazioni contemporanee
        if (!motorcycle.isAnimating) {
            motorcycle.isAnimating = true;

            animateCollision(motorcycle);

            // Ripristina il flag dopo l'animazione
            setTimeout(() => {
                motorcycle.isAnimating = false;
            }, COLLISION_ANIMATION.DURATION);
        }
    }
}