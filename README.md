## Запуск UI-тестов в Docker

### Одноразовая подготовка

```bash
# 1. Сеть, в которой Selenoid будет запускать браузеры
docker network create selenoid-network

# 2. Образ браузера, который прописан в config/browsers.json
docker pull selenoid/vnc_chrome:128.0
```

Без этих двух шагов `docker compose up` упадёт: сеть `selenoid-network` объявлена как `external`, а Selenoid не сможет
создать сессию, если образа `selenoid/vnc_chrome:128.0` нет локально.

### Запуск

```bash
# Selenoid + UI
docker compose up -d selenoid selenoid-ui

# Тесты (профиль "tests")
docker compose run --rm \
  -e APP_USERNAME=$APP_USERNAME \
  -e APP_PASSWORD=$APP_PASSWORD \
  -e PG_USER=$PG_USER \
  -e PG_PASSWORD=$PG_PASSWORD \
  -e PG_ADDRESS=$PG_ADDRESS \
  -e TARGET_URL=$TARGET_URL \
  tests
```

### Локальный запуск без Docker

```bash
# Selenoid поднят по localhost
./gradlew test -Dselenoid.url=http://localhost:4444/wd/hub

# Или вообще без Selenoid — локальный ChromeDriver
./gradlew test -Dbrowser.remote=false
```

### Переменные окружения

| Переменная      | Назначение                                    | Пример                        |
|-----------------|-----------------------------------------------|-------------------------------|
| `SELENOID_URL`  | URL Selenoid (`/wd/hub`)                      | `http://selenoid:4444/wd/hub` |
| `TARGET_URL`    | URL тестируемого приложения                   | `http://localhost:8080`       |
| `APP_USERNAME`  | Basic-auth логин                              | -                             |
| `APP_PASSWORD`  | Basic-auth пароль                             | -                             |
| `SELENOID_MODE` | `true` — Selenoid, `false` — локальный Chrome | `true`                        |
| `PG_USER`       | Имя пользователя для доступа к бд             | -                             |
| `PG_ADDRESS`    | Полный JDBC адрес для доступа к бд            | -                             |
| `PG_PASSWORD`   | Пароль для доступа к бд                       | -                             |
