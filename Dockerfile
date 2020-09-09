ARG JAVA_VERSION=11
ARG BUILD_DIR=/usr/src/app
ARG WORK_DIR=/opt
ARG THREAD_STACK_SIZE
ARG APPLICATION_NAME

FROM openjdk:${JAVA_VERSION}-jdk-slim as dist
ARG BUILD_DIR
WORKDIR ${BUILD_DIR}
COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew --no-daemon --version
COPY src ./src
RUN ./gradlew --no-daemon bootJar --stacktrace

FROM openjdk:${JAVA_VERSION}-jre-slim
ARG BUILD_DIR
ARG WORK_DIR
ARG APPLICATION_NAME
ARG THREAD_STACK_SIZE
ENV APPLICATION_NAME=${APPLICATION_NAME:-application}
ENV THREAD_STACK_SIZE=${THREAD_STACK_SIZE:-1024k}
ENV MALLOC_ARENA_MAX=4
WORKDIR ${WORK_DIR}
# install JNI wrappers for APR(Apache Portable Runtime) used by Tomcat
RUN apt-get update && \
    apt-get install --yes --no-install-recommends libtcnative-1 && \
    apt-get autoclean && apt-get --purge -y autoremove && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && \
    mkdir ./bin && ln -s /usr/lib/x86_64-linux-gnu/libtcnative-1.so ./bin/
COPY --from=dist ${BUILD_DIR}/build/libs/*.jar ./${APPLICATION_NAME}.jar

ENTRYPOINT exec java -XX:+UseContainerSupport -XX:+AlwaysActAsServerClassMachine -XX:ThreadStackSize=${THREAD_STACK_SIZE} -XX:+UseStringDeduplication -XX:+PerfDisableSharedMem -jar ${APPLICATION_NAME}.jar
EXPOSE 8080
