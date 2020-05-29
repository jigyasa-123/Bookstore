# Bookstore


Bookstore is an application that user can use to search and buy books

# Design!

  - Couchbase Database - Book bucket - ISBN as document id 
  - Spring boot app
  - Add a book 
        - if new book is to added, create new book doc in book bucket with isbn as document id
        - if book with isbn no already exist, increment count in db
 - Buy a book
        - if book exist, decrement the count 
        - if count becomes 0, increment the count 
        - if book does not exist, throw an exception

### Tech

* Java 8
* Streams
* N1QL query - couchbase
* key-value operations - couchbase
* couchbase-cli tool
* junit 5
* Docker images
* Rest Template
* Mockito
* Rest Assured
* Error Handling
* JAVAX Validations

### Installation

Clone Git Repository
```sh
  $ git clone https://github.com/jigyasa-123/Bookstore.git
  $ cd bookstore
 ```
   

Setup Couchbase Database with cluster , book bucket and primary index

```sh
 $ cd couchbase
 $ docker build -t couchbasedb .
 $ docker run --rm -d  -p 11207:11207/tcp -p 11210:11210/tcp -p 11211:11211/tcp -p 18091:18091/tcp -p 18092:18092/tcp -p 18093:18093/tcp -p 18094:18094/tcp -p 18095:18095/tcp -p 18096:18096/tcp -p 8091:8091/tcp -p 8092:8092/tcp -p 8093:8093/tcp -p 8094:8094/tcp -p 8095:8095/tcp -p 8096:8096/tcp --name db couchbasedb:latest
```
Couchbase server is up and running at http://localhost:8091

Setup Spring boot App
```sh
$ cd .. OR cd bookstore (root directory of project )
$ docker build -t app .
$ docker run --publish=8080:8080 --name spring-app app:latest
```
Spring boot App  is up and running at http://localhost:8080

### REST API CURL
To hit rest API you can use below postman curl or navigate to bookstore/postman and import postman collection in postman app 
### Add a book
 ```sh
curl -X POST \
  http://localhost:8080/books/ \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: c06505ae-9854-4dbb-83b5-49c001eeaafd' \
  -H 'cache-control: no-cache' \
  -d '{
	"isbn":"99597",
	"author"  : "jigs",
	"title" : "esse",
	"price" : 123
	
}''
```
### Buy a book
 ```sh
curl -X GET \
  http://localhost:8080/books/buy/9959 \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 4ce6aca2-a7c5-42f3-ae52-0b060585af01' \
  -H 'cache-control: no-cache'
```
### Get Book Media Covergae
 ```sh
curl -X GET \
  http://localhost:8080/books/mediacoverage/99597 \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 098c6423-69e1-4c3f-b0bd-58e817b81e51' \
  -H 'cache-control: no-cache'
```

### Search book by Id
 ```sh
curl -X GET \
  http://localhost:8080/books/9959 \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 0de76012-153d-4675-a247-2e15e5893237' \
  -H 'cache-control: no-cache'
```
### Search Book by title
 ```sh
curl -X GET \
  'http://localhost:8080/books/title?name=sheldon' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 9e939483-9544-46cb-bcd9-787c0654f589' \
  -H 'cache-control: no-cache'
```
### Search Book by author
 ```sh
curl -X GET \
  'http://localhost:8080/books/author?name=jigs' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: dbc5eaa1-51be-4570-8ee8-4d0b27821d08' \
  -H 'cache-control: no-cache'
```


### Author
Jigyasa Garg  - https://github.com/jigyasa-123/Bookstore

