@echo off
REM Ir a la carpeta donde está el script
cd /d "%~dp0"

REM Ir a la carpeta de compilados desde aquí
cd "out\production\Sistema de turnos"

REM Ejecutar la clase principal
java labturnos.MainFrame

pause
