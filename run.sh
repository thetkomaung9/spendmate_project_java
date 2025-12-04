#!/bin/bash
# SpendMate Run Script

cd "$(dirname "$0")"

echo "Starting SpendMate Application..."
echo "================================="

# Check if lib directory exists
if [ ! -d "lib" ]; then
    echo "Error: lib directory not found!"
    exit 1
fi

# Check for required JAR files
if [ ! -f "lib/mysql-connector-j-8.3.0.jar" ]; then
    echo "Error: mysql-connector-j-8.3.0.jar not found in lib directory!"
    exit 1
fi

if [ ! -f "lib/slf4j-api-1.7.36.jar" ]; then
    echo "Error: slf4j-api-1.7.36.jar not found in lib directory!"
    exit 1
fi

if [ ! -f "lib/slf4j-simple-1.7.36.jar" ]; then
    echo "Error: slf4j-simple-1.7.36.jar not found in lib directory!"
    exit 1
fi

# Run the application
java -cp "src:lib/mysql-connector-j-8.3.0.jar:lib/slf4j-api-1.7.36.jar:lib/slf4j-simple-1.7.36.jar" app.MainApp

echo ""
echo "Application closed."
