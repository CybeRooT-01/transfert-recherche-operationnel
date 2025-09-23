# 💸 Projet de Transfert (Recherche Opérationnelle)

Application **full-stack** développée en **Java Spring Boot** (backend) et **Angular** (frontend), permettant de simuler un système de transfert d’argent avec gestion des comptes, abonnements et sécurité.

---

## 🚀 Fonctionnalités

* ✅ Gestion des **comptes utilisateurs**
* ✅ Opérations : **dépôt**, **retrait**, **transfert**
* ✅ Abonnements : **Free** & **Premium**
* ✅ Sécurité **JWT (authentification stateless)**
* ✅ Simulation d’envoi d’emails avec **smtp4dev (Docker)**

---

## 🛠️ Stack technique

* **Backend** : Java 17, Spring Boot
* **Frontend** : Angular
* **Base de données** : PostgreSQL (Docker)
* **Sécurité** : Spring Security + JWT
* **Mailing** : smtp4dev (conteneurisé)
* **CI/CD** : Jenkins + Kubernetes + ArgoCD (GitOps)
* **Containerisation** : Docker & Docker Compose

---

## 📂 Structure du projet

```
/transfert-back        → Backend Spring Boot
/transfert-front       → Frontend Angular
docker-compose.yml
Jenkinsfile
```

---

## ⚙️ Installation & Lancement local

### 1️⃣ Cloner le dépôt

```bash
git clone https://github.com/CybeRooT-01/transfert-recherche-operationnel.git
cd transfert-back
cd transfert-front
```

### 2️⃣ Lancer avec Docker

```bash
docker-compose up -d
```

### 3️⃣ Accéder aux services en local

* 🌐 **Frontend Angular** → [http://localhost:4200](http://localhost:4200)
* ⚙️ **Backend API (Spring Boot)** → [http://localhost:8080](http://localhost:8080)
* 📧 **SMTP4DEV (UI mails)** → [http://localhost:3000](http://localhost:3000)

---

## 🔐 Sécurité JWT

* Authentification via **email + mot de passe**
* Génération d’un **token JWT** stateless
* Toutes les requêtes protégées nécessitent le header :

```http
Authorization: Bearer <token>
```

---

## 📧 Mailing (smtp4dev) en local

Les emails (confirmation, notifications, etc.) seront visible via

👉 [http://localhost:9081](http://localhost:9081)

---

## ⚙️ Déploiement CI/CD

Le backend du porjet genre l'API est intégré dans un pipeline **CI/CD complet** :

### 🔨 Intégration Continue (CI)

* **Jenkins** :

  * Build du backend Spring Boot et du frontend Angular
  * Exécution des tests unitaires
  * Build & push des images Docker sur un registry self hosted

### 🚀 Déploiement Continu (CD)

* **ArgoCD (GitOps)** :

  * Synchronisation automatique avec le repo Git
  * Déploiement sur un **cluster Kubernetes**

### 🌐 Environnement Kubernetes

Le projet utilise un cluster Kubernetes provisionné avec kubeadm sur AWS (1 master, 2 workers).
La structure des fichiers Kubernetes dans le projet est la suivante :

``` bash
/kubernetes
├─ scripts/  ← Contient les scripts automatisés pour la msie en place du cluster
├─ manifest/   ← Contient tous les fichiers YAML utilisés pour le projet
```

Backend : exposé via un Service et déployé sur le cluster

Frontend : non déployé sur Kubernetes

MySQL & smtp4dev : déployés comme pods stateful/services

## 👨‍💻 Auteurs

Projet réalisé dans le cadre de l'UE **Recherche Opérationnelle**.

* 👤 Participants
 - Thierno Birahim Gueye (Developpeur backend / devops)
 - El Hadj Malick Ndao (Developpeur frontend)
 - Siabatou sane (Developpeur frontend, designer)
* 🎓 Master Ingenieurie logiciel P8 — 2025

