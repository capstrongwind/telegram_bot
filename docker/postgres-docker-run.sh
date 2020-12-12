docker run \
--name telegram-data-postgres \
-e POSTGRES_PASSWORD=123 \
-d \
-p 15432:5432 \
-v /home/cosysoft/projects/telegram-data/db-data:/var/lib/postgresql/data \
postgres