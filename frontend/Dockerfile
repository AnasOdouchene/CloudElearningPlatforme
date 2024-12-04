# Utiliser l'image officielle Nginx
FROM nginx:alpine

# Copier la configuration personnalisée de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copier tous les fichiers frontend (HTML, CSS, JS) dans le répertoire par défaut de Nginx
COPY . /usr/share/nginx/html

# Exposer le port 80
EXPOSE 80
