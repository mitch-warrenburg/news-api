FROM adoptopenjdk:11-jdk-openj9 as build

WORKDIR /workspace/app

COPY gradlew build.gradle settings.gradle ./
COPY src src/
COPY gradle gradle/

RUN ./gradlew clean build --console rich --info -x test -x spotlessJavaCheck --stacktrace
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/news-api-0.0.1.jar)

FROM adoptopenjdk:11-jdk-openj9

ARG ENCODED_GOOGLE_APPLICATION_CREDENTIALS
ENV GOOGLE_APPLICATION_CREDENTIALS=/tmp/key.json
ENV ENCODED_GOOGLE_APPLICATION_CREDENTIALS=${ENCODED_GOOGLE_APPLICATION_CREDENTIALS}

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp", "app:app/lib/*","com.labelbox.news.newsapi.NewsApiApplication", "-Xshareclasses", "-Xquickstart"]