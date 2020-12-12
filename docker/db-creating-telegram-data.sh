docker exec -it telegram-data-postgres bash
psql -U postgres
CREATE DATABASE telegram_data_postgres;
\q
exit