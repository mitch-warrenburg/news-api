# news-api

A Spring Boot service which interfaces with [NewsCatcher's API](<https://newscatcherapi.com>) for exposing news article
publication data. Try
the [Live Demo](<https://newsapi.app/api/v1/news/search?q=mike trout&languages=en&topic=sport&countries=US,CA>).

### Documentation

View the [OpenAPI Documentation](<https://newsapi.app/swagger-ui/index.html>) to get started

### Build and Run the App

Since this API simply interfaces with [NewsCatcher's API](<https://newscatcherapi.com>), it can easily be extended to
implement more interesting use-cases.

#### Prerequisites

* Ensure Java JDK is installed on your machine. This project requires `Java11`.
* Create a free API key by [registering](<https://app.newscatcherapi.com/auth/register>) your email
  with [NewsCatcher](<https://newscatcherapi.com>)
    * *Note that the free tier allows a maximum of only 50 requests - so use them wisely*

#### Commands

This project uses the Gradle build system. A portable gradle wrapper executable is included, so Gradle is not required
to be installed on your machine.

Clone the repository:

```console
git clone https://github.com/mitch-warrenburg/news-api.git
```

Build the app:

```console
cd news-api
./gradlew build
```

Run just the unit tests:

```console
./gradlew test
```

Run the app:

```console
export NEWS_CATCHER_API_KEY="my-newscatcher-api-key"
./gradlew bootrun

# spring boot process is now active on your localhost at port 8080
```

View the OpenAPI documentation locally in Postman, curl, or your tool of choice to get
started: http://localhost:8080/swagger-ui/index.html
