#!/bin/bash
# Moverse a la carpeta donde está este script
cd "$(dirname "$0")"

# Ir a la carpeta de compilados
cd "out/production/Sistema de turnos"

# Ejecutar la clase principal
java labturnos.MainFrame
