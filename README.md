#  API de Gestion des Utilisateurs – Cires Technologies

Ce projet propose une API REST complète pour la gestion des utilisateurs, l’authentification et le contrôle d’accès basé sur les rôles. L’implémentation repose sur une **architecture horizontale** (par couches) afin de garantir une meilleure **modularité**, une **testabilité facilitée**, et une **maintenabilité à long terme**.

---

##  Architecture

Le projet est structuré en plusieurs couches clairement définies :

- **Contrôleur** : Gère les requêtes HTTP et les réponses.
- **Service** : Contient la logique métier.
- **DTOs & Mappers** : Facilitent le transfert et la transformation des données.
- **Repository** : Communique avec la base de données.
- **Sécurité** : Implémente l’authentification via JWT et le contrôle des accès.

Cette organisation permet une meilleure séparation des responsabilités, une structure évolutive et un code plus facile à tester.

---

##  Tester l’API avec Swagger UI

Tous les endpoints sont **testables directement via Swagger UI**, sans besoin d’outils externes comme Postman.

>  Interface Swagger :  
> `http://localhost:9090/swagger-ui/index.html`

Depuis cette interface, vous pouvez :
- Authentifier un utilisateur et récupérer un token JWT
- Tester les routes sécurisées avec le token
- Générer des utilisateurs fictifs
- Importer des utilisateurs via un fichier JSON
- Accéder aux profils selon le rôle de l’utilisateur connecté

