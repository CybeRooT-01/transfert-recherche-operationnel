# Système de Transfert d'Argent - Architecture et Modélisation

## 1. Architecture Générale du Système

### 1.1 Architecture Multi-Couches

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT (Angular)                           │
│  ┌─────────────────┐ ┌─────────────────┐ ┌───────────────┐  │
│  │   Components    │ │    Services     │ │   Guards      │  │
│  │   - Dashboard   │ │   - Auth        │ │   - Route     │  │
│  │   - Transfer    │ │   - Transaction │ │   - Role      │  │
│  │   - Profile     │ │   - Account     │ │               │  │
│  └─────────────────┘ └─────────────────┘ └───────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │ HTTP/REST API
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                 BACKEND (Spring Boot)                       │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │               PRESENTATION LAYER                        │ │
│ │  ┌─────────────────┐ ┌─────────────────┐ ┌───────────┐  │ │
│ │  │   Controllers   │ │   Security      │ │   DTOs    │  │ │
│ │  │   - Auth        │ │   - JWT         │ │           │  │ │
│ │  │   - Account     │ │   - Filters     │ │           │  │ │
│ │  │   - Transaction │ │                 │ │           │  │ │
│ │  └─────────────────┘ └─────────────────┘ └───────────┘  │ │
│ └─────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │                BUSINESS LAYER                           │ │
│ │  ┌─────────────────┐ ┌─────────────────┐ ┌───────────┐  │ │
│ │  │    Services     │ │   Validators    │ │  Mappers  │  │ │
│ │  │   - User        │ │   - Transaction │ │           │  │ │
│ │  │   - Account     │ │   - Account     │ │           │  │ │
│ │  │   - Transaction │ │                 │ │           │  │ │
│ │  │   - Receipt     │ │                 │ │           │  │ │
│ │  └─────────────────┘ └─────────────────┘ └───────────┘  │ │
│ └─────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │               PERSISTENCE LAYER                         │ │
│ │  ┌─────────────────┐ ┌─────────────────┐ ┌───────────┐  │ │
│ │  │  Repositories   │ │    Entities     │ │    JPA    │  │ │
│ │  │   - User        │ │   - User        │ │           │  │ │
│ │  │   - Account     │ │   - Account     │ │           │  │ │
│ │  │   - Transaction │ │   - Transaction │ │           │  │ │
│ │  └─────────────────┘ └─────────────────┘ └───────────┘  │ │
│ └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    DATABASE (MySQL)                         │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 Architecture monoithique distribuée

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    
│   Frontend  │    │    API      │    │   Backend   │    
│             │◄──►│             │◄──►│             │   
└─────────────┘    └─────────────┘    └─────────────┘   
```

## 2. Diagramme Entité-Relation (ERD)

### 2.1 Entités Principales

```
┌─────────────────────────────────────────────────────────────┐
│                           USERS                             │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ username             │ VARCHAR(50) │ UNIQUE, NOT NULL       │
│ email                │ VARCHAR(100)│ UNIQUE, NULL           │
│ password_hash        │ VARCHAR(255)│ NOT NULL               │
│ first_name           │ VARCHAR(50) │ NOT NULL               │
│ last_name            │ VARCHAR(50) │ NOT NULL               │
│ phone_number         │ VARCHAR(20) │ UNIQUE, NOT NULL       │
│ country              │ VARCHAR(50) │ NOT NULL               │
│ photo_url            │ VARCHAR(255)│ NULL                   │
│ id_number            │ VARCHAR(50) │ UNIQUE, NOT NULL       │
│ id_photo_url         │ VARCHAR(255)│ NULL                   │
│ status               │ ENUM        │ ACTIVE, INACTIVE       │
│ role                 │ ENUM        │ USER, ADMIN            │
│ subscription_type    │ ENUM        │ FREE, PREMIUM          │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
│ updated_at           │ TIMESTAMP   │ NOT NULL               │
└─────────────────────────────────────────────────────────────┘
                               │
                               │ 1:1
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                         ACCOUNTS                            │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ user_id (FK)         │ BIGINT      │ NOT NULL               │
│ account_number       │ VARCHAR(20) │ UNIQUE, NOT NULL       │
│ balance              │ DECIMAL(15,2)│ DEFAULT 0.00          │
│ currency             │ VARCHAR(3)  │ DEFAULT 'XOF'          │
│ status               │ ENUM        │ ACTIVE, FROZEN         │
│ daily_limit          │ DECIMAL(15,2)│ DEFAULT 1000000.00    │
│ monthly_limit        │ DECIMAL(15,2)│ DEFAULT 5000000.00    │
│ pin_hash             │ VARCHAR(255)│ NOT NULL               │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
│ updated_at           │ TIMESTAMP   │ NOT NULL               │
└─────────────────────────────────────────────────────────────┘
                               │
                               │ 1:N
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                      TRANSACTIONS                           │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ transaction_number   │ VARCHAR(20) │ UNIQUE, NOT NULL       │
│ sender_account_id(FK)│ BIGINT      │ NOT NULL               │
│ receiver_account_id(FK)│ BIGINT    │ NOT NULL               │
│ amount               │ DECIMAL(15,2)│ NOT NULL              │
│ fee                  │ DECIMAL(15,2)│ DEFAULT 0.00          │
│ total_amount         │ DECIMAL(15,2)│ NOT NULL              │
│ type                 │ ENUM        │ TRANSFER, DEPOSIT,     │
│                      │             │ WITHDRAWAL             │
│ status               │ ENUM        │ PENDING, COMPLETED,    │
│                      │             │ FAILED, CANCELLED      │
│ description          │ TEXT        │ NULL                   │
│ reference            │ VARCHAR(100)│ NULL                   │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
│ completed_at         │ TIMESTAMP   │ NULL                   │
└─────────────────────────────────────────────────────────────┘
                               │
                               │ 1:1
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                         RECEIPTS                            │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ transaction_id (FK)  │ BIGINT      │ UNIQUE, NOT NULL       │
│ receipt_number       │ VARCHAR(20) │ UNIQUE, NOT NULL       │
│ pdf_url              │ VARCHAR(255)│ NULL                   │
│ email_sent           │ BOOLEAN     │ DEFAULT FALSE          │
│ sms_sent             │ BOOLEAN     │ DEFAULT FALSE          │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    TRANSACTION_FEES                         │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ min_amount           │ DECIMAL(15,2)│ NOT NULL              │
│ max_amount           │ DECIMAL(15,2)│ NOT NULL              │
│ fee_type             │ ENUM        │ FIXED, PERCENTAGE      │
│ fee_value            │ DECIMAL(10,4)│ NOT NULL              │
│ subscription_type    │ ENUM        │ FREE, PREMIUM          │
│ is_active            │ BOOLEAN     │ DEFAULT TRUE           │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                      AUDIT_LOGS (Trackings)                 │
├─────────────────────────────────────────────────────────────┤
│ id (PK)              │ BIGINT      │ AUTO_INCREMENT         │
│ user_id (FK)         │ BIGINT      │ NULL                   │
│ action               │ VARCHAR(100)│ NOT NULL               │
│ entity_type          │ VARCHAR(50) │ NOT NULL               │
│ entity_id            │ BIGINT      │ NULL                   │
│ old_values           │ JSON        │ NULL                   │
│ new_values           │ JSON        │ NULL                   │
│ ip_address           │ VARCHAR(45) │ NULL                   │
│ user_agent           │ VARCHAR(255)│ NULL                   │
│ created_at           │ TIMESTAMP   │ NOT NULL               │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Relations

- **Users** (1) ←→ (1) **Accounts** : Un utilisateur possède un compte
- **Accounts** (1) ←→ (N) **Transactions** : Un compte peut avoir plusieurs transactions
- **Transactions** (1) ←→ (1) **Receipts** : Une transaction génère un reçu
- **Users** (1) ←→ (N) **Audit_Logs** : Un utilisateur peut avoir plusieurs logs d'audit

### 2.3 Validation des Transactions
- Vérification du solde avant transaction
- Validation des limites quotidiennes/mensuelles
- Vérification du PIN pour les transactions
- Audit trail pour toutes les opérations

## 4. Modèle Économique

### 4.1 Structure des Frais
- **Utilisateurs Gratuits** : 2% par transaction
- **Utilisateurs Premium** : 1% par transaction
- **Frais fixes** : 100 XOF pour les petites transactions (<10,000 XOF)

### 4.2 Types de Revenus
1. **Frais de Transaction** : Commission sur chaque transfert
2. **Abonnements Premium** : 5,000 XOF/mois pour réduction des frais
3. **Frais de Change** : Pour les transactions internationales
4. **Services Premium** : Priorité dans le traitement, support dédié

## 5. API Endpoints Structure

### 5.1 Authentication
- `POST /api/auth/register` - Inscription 
- `POST /api/auth/login` - Connexion
- `POST /api/auth/refresh` - Rafraîchir le token
- `POST /api/auth/logout` - Déconnexion

### 5.2 Account Management
- `GET /api/accounts/profile` - Profil utilisateur
- `PUT /api/accounts/profile` - Modifier profil
- `GET /api/accounts/balance` - Solde du compte
- `POST /api/accounts/deposit` - Dépôt d'argent
- `POST /api/accounts/withdraw` - Retrait d'argent

### 5.3 Transactions
- `POST /api/transactions/transfer` - Effectuer un transfert
- `GET /api/transactions/history` - Historique des transactions
- `GET /api/transactions/{id}` - Détails d'une transaction
- `GET /api/transactions/{id}/receipt` - Télécharger le reçu

### 5.4 Administration
- `GET /api/admin/users` - Liste des utilisateurs
- `GET /api/admin/transactions` - Toutes les transactions
- `PUT /api/admin/users/{id}/status` - Modifier statut utilisateur

## 7. Technologies et Outils

### 7.1 Backend (Spring Boot)
- **Spring Security** : Authentification et autorisation
- **Spring Data JPA** : Accès aux données
- **Spring Validation** : Validation des données
- **JWT** : Gestion des tokens
- **MySQL** : Base de données
- **Maven** : Gestionnaire de dépendances

### 7.2 Frontend (React)
- **Angular MAterial** : Interface utilisateur
- **Angular guards** : Protection des routes
- **Angular interceptors** : Gestion des requêtes HTTP
- **RxJs** : Programmation réactive (les requetes asynchrone)
- **Chart.js** : Graphiques et statistiques


### 7.3 Outils de Développement & de Déploiement
- **Docker** : Conteneurisation
- **Git** : Contrôle de version
- **Postman** : Test des APIs
- **SonarQube** : Qualité du code
- **Kubernetes** : Infra
- **Jenkins** : CI
- **ArgoCD** : CD
- **Prometheus /grafana** : Monitoring


### 8 Environnements
- **Développement** : Base de données H2, profils Spring
