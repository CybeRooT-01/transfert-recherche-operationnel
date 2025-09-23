#!/bin/bash

echo "🚀 Installation ArgoCD sur le cluster"
echo "=============================================="
echo "📦 Création du namespace argocd..."
kubectl create namespace argocd

echo "⬇️  Installation des composants ArgoCD..."
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

echo "⏳ Attente du démarrage des pods ArgoCD..."
kubectl wait --for=condition=available --timeout=600s deployment/argocd-server -n argocd
kubectl wait --for=condition=available --timeout=600s deployment/argocd-repo-server -n argocd
kubectl wait --for=condition=available --timeout=600s deployment/argocd-dex-server -n argocd

echo "✅ ArgoCD installé avec succès !"

echo "📊 Status des pods ArgoCD:"
kubectl get pods -n argocd

echo "📊 Status des services ArgoCD:"
kubectl get svc -n argocd

echo ""
echo "🔧 Prochaines étapes:"
echo "1. Appliquer le service NodePort (voir argocd-nodeport-service.yaml)"
echo "2. Récupérer le password admin"
echo "3. Se connecter via l'IP publique du node"