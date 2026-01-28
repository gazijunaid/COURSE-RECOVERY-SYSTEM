#!/bin/bash

# Course Recovery System - Run Script

echo "=========================================="
echo "   Course Recovery System (CRS)"
echo "   Educational Institution Management"
echo "=========================================="
echo ""

# Create directories if they don't exist
mkdir -p build/classes data lib

# Check if compiled classes exist
if [ ! -f "build/classes/crs/Main.class" ]; then
    echo "Compiling source files..."
    javac -cp "lib/*:." -d build/classes -sourcepath src src/crs/Main.java src/crs/model/*.java src/crs/service/*.java src/crs/util/*.java src/crs/gui/*.java
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation successful!"
    echo ""
fi

echo "Starting Course Recovery System..."
echo ""
echo "Default Login Credentials:"
echo "  Academic Officer: admin / admin123"
echo "  Course Admin: courseadmin / course123"
echo ""

# Run the application
java -cp "lib/*:build/classes" crs.Main
