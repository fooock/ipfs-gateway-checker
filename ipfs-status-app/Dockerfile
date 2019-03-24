FROM node:8.9.4-alpine as node
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app/
RUN npm run build -- --output-path=./dist/out --configuration=production

FROM nginx:1.15-alpine
COPY --from=node /app/dist/out/ /usr/share/nginx/html
COPY ./deploy/nginx.conf /etc/nginx/conf.d/default.conf
