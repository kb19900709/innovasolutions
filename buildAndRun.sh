mvn clean package -DskipTests
echo "\nExtract project information ..."
PROJECT_ARTIFACTID=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.name -q -DforceStdout)
IMAGE_NAME=$(echo "${PROJECT_ARTIFACTID}:${PROJECT_VERSION}" | awk '{print tolower($0)}')
echo "\n${IMAGE_NAME} is about to be built as a docker image\n"

docker build -t ${IMAGE_NAME} .

EXTERNAL_PORT=$1
if [ -z $EXTERNAL_PORT ]; then
  EXTERNAL_PORT=8080
fi

EXISTING_CONTAINER_ID=$(docker ps -a -q -f name=${PROJECT_NAME})
if [ $EXISTING_CONTAINER_ID ]; then
  docker stop $EXISTING_CONTAINER_ID
  docker rm $EXISTING_CONTAINER_ID
fi

docker run -d --name $PROJECT_NAME -e JAVA_OPT="-Xms128m -Xmx128m" -p $EXTERNAL_PORT:8080 $IMAGE_NAME

echo "\n$PROJECT_NAME is running with $EXTERNAL_PORT port"