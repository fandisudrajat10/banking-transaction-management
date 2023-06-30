@echo off

:: Tunggu hingga MySQL siap menerima koneksi
:loop
docker-compose exec mysql mysqladmin ping -h mysql -u%MYSQL_USER% -p%MYSQL_PASSWORD% --silent
if errorlevel 1 (
    echo Waiting for MySQL to be ready...
    timeout /t 1 > nul
    goto loop
)

:: Jalankan perintah build
docker-compose build myapp