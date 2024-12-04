FROM nginx:alpine

# Copier le fichier de configuration personnalisé
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copier tout le contenu du projet dans Nginx
COPY . /usr/share/nginx/html/

# Exposer le port 80
EXPOSE 80
