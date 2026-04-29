# ChâTop

Plateforme de location immobilière — projet OpenClassrooms P3 « Modélisez et implémentez le back-end en utilisant du code Java maintenable ».

Le repo contient :

- **`back/`** — API REST Spring Boot 3 (Java 17) en architecture hexagonale
- **`angular/`** — front Angular 20 (Node 22)
- **`docker-compose.yml`** — stack complète (MariaDB, MinIO, backend, frontend)
- **`ressources/`** — schéma SQL, environnement Mockoon, script d'init MinIO

## Stack technique

| Couche | Techno |
|---|---|
| Back | Spring Boot 3.5, Spring Security (OAuth2 Resource Server), Spring Data JPA, Hibernate |
| Auth | JWT signé HS256 (Nimbus), claims `sub` (email) + `user_id` |
| Base de données | MariaDB 11 |
| Stockage objet | MinIO (S3-compatible) — bucket public `rentals` pour les images |
| Doc API | springdoc-openapi (Swagger UI) |
| Tests | JUnit 5, Spring MockMvc, Mockito |
| Front | Angular 20 |
| Build/run | Maven wrapper, Docker Compose, Makefile |

## Démarrage rapide

### 1. Préparer l'environnement

```bash
cp .env.exemple .env
# éditer .env pour renseigner DB_PASSWORD, DB_ROOT_PASSWORD, JWT_SECRET, etc.
```

### 2. Lancer toute la stack

```bash
make up
```

Ce qui démarre via `docker compose` :

| Service | URL | Notes |
|---|---|---|
| Frontend Angular | http://localhost:3000 | |
| Backend Spring Boot | http://localhost:3001 | |
| Swagger UI | http://localhost:3001/swagger-ui.html | doc OpenAPI |
| MariaDB | `localhost:3306` | schéma chargé via `ressources/sql/script.sql` |
| MinIO API | http://localhost:9000 | |
| MinIO Console | http://localhost:9001 | login = `MINIO_ROOT_USER` / `MINIO_ROOT_PASSWORD` |

Le bucket public `rentals` est créé automatiquement au premier démarrage par le service `minio-init` (script `ressources/minio/create-bucket.sh`).

### Commandes Make utiles

```bash
make up         # démarre la stack en arrière-plan
make rebuild    # rebuild les images puis up
make down       # stoppe et supprime les conteneurs
make logs       # tail les logs de tous les services
make ps         # statut des conteneurs
make clean      # down + suppression images locales + volumes
```

## Développement back en local (hors Docker)

Pré-requis : Java 17, Maven (le wrapper `mvnw` est inclus), MariaDB et MinIO démarrés (par ex. via `docker compose up -d mariadb minio minio-init`).

```bash
cd back
./mvnw spring-boot:run
```

Tests :

```bash
./mvnw test
```

## Architecture du backend

Architecture **hexagonale (ports & adapters)** organisée par **bounded context**.

```
back/src/main/java/com/chatop/back/
├── auth/               JWT issuance (port + adapter Nimbus)
├── user/               domaine User : register, login, me
├── rental/             domaine Rental : list, get, create (multipart), update
├── message/            domaine Message : envoi de messages
├── shared/             réponses transverses (ApiMessage)
└── config/             SecurityConfig, OpenApiConfig, GlobalExceptionHandler
```

Chaque bounded context suit la même découpe :

```
<context>/
├── api/
│   ├── controller/   un controller par action (SRP)
│   ├── request/      DTO d'entrée + validation Jakarta
│   └── response/     DTO de sortie
├── application/
│   ├── command/      records immutables (input des use cases)
│   └── usecase/      orchestration métier, @Transactional
├── domain/
│   ├── entity/       entités (factories statiques, invariants)
│   ├── vo/           value objects (Surface, Price, PictureUpload…)
│   ├── repository/   ports (interfaces)
│   ├── service/      ports métier (PasswordHasher, PictureStorage)
│   └── exception/    exceptions métier
└── infrastructure/
    ├── persistence/  JPA entity + Spring Data + adapter du port
    ├── security/     adapters Spring Security
    └── storage/      adapter MinIO (rental)
```

**Principes retenus**

- Le domaine ne dépend ni de Spring ni de JPA — pure Java + invariants
- Les ports vivent côté domaine ; les adapters Spring/JPA vivent côté infra (package-private = forcent le passage par le port)
- Les value objects portent les invariants (`Surface`, `Price`, `PictureUpload`) et lèvent des exceptions dédiées (`InvalidPriceException`…) gérées en 400 par le `GlobalExceptionHandler`
- Un controller = une action HTTP (cf. `RegisterController`, `ListRentalsController`, `CreateRentalController`…)
- Le JWT porte `sub` (email) + claim `user_id` pour éviter les lookups DB côté rental

## Endpoints

| Méthode | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | non | Inscription, retourne un JWT |
| POST | `/api/auth/login` | non | Connexion, retourne un JWT |
| GET | `/api/auth/me` | oui | Profil de l'utilisateur courant |
| GET | `/api/user/{id}` | oui | Profil d'un utilisateur (propriétaire d'un bien) |
| GET | `/api/rentals` | oui | Liste de toutes les locations |
| GET | `/api/rentals/{id}` | oui | Détail d'une location |
| POST | `/api/rentals` | oui | Créer une location (multipart, image obligatoire) |
| PUT | `/api/rentals/{id}` | oui | Mettre à jour (uniquement par le propriétaire) |
| POST | `/api/messages` | oui | Envoyer un message |

Doc OpenAPI complète sur **http://localhost:3001/swagger-ui.html**.

## Variables d'environnement

Toutes regroupées dans `.env` à la racine. Un exemple commenté est fourni dans `.env.exemple`.

| Variable | Rôle |
|---|---|
| `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_ROOT_PASSWORD`, `DB_HOST`, `DB_PORT` | MariaDB |
| `JWT_SECRET` | secret HS256 (≥ 32 octets) |
| `JWT_EXPIRATION_MINUTES` | durée de vie du token |
| `MINIO_ROOT_USER`, `MINIO_ROOT_PASSWORD` | identifiants admin MinIO |
| `MINIO_ENDPOINT`, `MINIO_PUBLIC_URL` | endpoint API MinIO + base URL publique servie aux clients |
| `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY` | identifiants utilisés par le backend |
| `MINIO_BUCKET` | nom du bucket (créé automatiquement) |
| `CORS_ALLOWED_ORIGINS` | origines autorisées par le back |

## Migration vers AWS S3

Le SDK utilisé (`io.minio:minio`) parle nativement le protocole S3. Pour passer en prod sur AWS, il suffit de :

1. Pointer `MINIO_ENDPOINT` vers `https://s3.<region>.amazonaws.com`
2. Renseigner les credentials IAM dans `MINIO_ACCESS_KEY` / `MINIO_SECRET_KEY`
3. Ajouter `.region(...)` dans `MinioConfig` (AWS exige la région pour SigV4)

Pour exposer publiquement les images en prod, le pattern recommandé est CloudFront + Origin Access Control plutôt qu'un bucket public.

## Ressources

- **Schéma SQL** : `ressources/sql/script.sql`
- **Environnement Mockoon** (spec API de référence) : `ressources/mockoon/rental-oc.json`
- **Script d'init bucket MinIO** : `ressources/minio/create-bucket.sh`
