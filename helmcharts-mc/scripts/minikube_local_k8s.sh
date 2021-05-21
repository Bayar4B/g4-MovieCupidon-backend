# Pour installer le helm chart
helm upgrade --install -f moviecupidon-helm-minikube/values.yaml moviecupidon-minikube moviecupidon-helm-minikube/
# Les pods demarraient pas du coup on upgrade un coup
helm upgrade -f moviecupidon-helm-minikube/values.yaml moviecupidon-minikube moviecupidon-helm-minikube/
# On applique un nginx controller qui marche avec WSL2, car le addon ne marche pas avec wsl2
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.44.0/deploy/static/provider/cloud/deploy.yaml
# On tunnel vers le loadbalancer ingress
minikube tunnel

## Entrez le mdp pour le tunnel et aller sur localhost.
