PROJECT_NAME="Sandbox"
LIME_ROOT=../Lime

echo "Generate Engine Sources"
(cd $LIME_ROOT && ./gradlew publishToMavenLocal)
echo "Generate Engine Sources Done...!"

echo "Run ${PROJECT_NAME}"
./gradlew run
echo "Stopped ${PROJECT_NAME}...!"

read -p "Press any key..."