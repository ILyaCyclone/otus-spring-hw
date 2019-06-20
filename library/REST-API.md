curl commands for REST API
 
## Books
 
### get all books
`curl http://localhost:8080/api/v1/books`

### get book by {id}
`curl http://localhost:8080/api/v1/books/{id}`

### create book
`curl http://localhost:8080/api/v1/books -X POST -H "Content-Type:application/json" -d "{\"title\":\"New Title\", \"year\": 2000, \"authorId\": \"1\", \"genreId\": \"2\"}" -i`

### update book by {id}
`curl http://localhost:8080/api/v1/books/{id} -X PUT -H "Content-Type:application/json" -d "{\"title\":\"Updated Title\", \"year\": 2000, \"authorId\": \"1\", \"genreId\": \"2\"}" -i`

### delete book by {id}
`curl http://localhost:8080/api/v1/books/{id} -X DELETE -i`

### get book form data by {id}
`curl http://localhost:8080/api/v1/books/formdata/{id}`

### get book form data for new book
`curl http://localhost:8080/api/v1/books/formdata/new`

### leave a comment to book {id}
`curl http://localhost:8080/api/v1/books/{id}/comments/save -X POST -H "Content-Type:application/json" -d "{\"text\":\"comment text\", \"commentator\": \"commentator name\"}" -i`

 
## Authors
 
### get all authors
`curl http://localhost:8080/api/v1/authors`

### get author by {id}
`curl http://localhost:8080/api/v1/authors/{id}`

### create author
`curl http://localhost:8080/api/v1/authors -X POST -H "Content-Type:application/json" -d "{\"firstname\":\"New Firstname\", \"lastname\": \"New Lastname\", \"homeland\": \"New Homeland\"}" -i`

### update author by {id}
`curl http://localhost:8080/api/v1/authors/{id} -X PUT -H "Content-Type:application/json" -d "{\"firstname\":\"New Firstname\", \"lastname\": \"New Lastname\", \"homeland\": \"upd\"}" -i`

### delete author by {id}
`curl http://localhost:8080/api/v1/authors/{id} -X DELETE -i`

 
## Genres
 
### get all genres
`curl http://localhost:8080/api/v1/genres`

### get genre by {id}
`curl http://localhost:8080/api/v1/genres/{id}`

### create genre
`curl http://localhost:8080/api/v1/genres -X POST -H "Content-Type:application/json" -d "{\"name\":\"New Genre\"}" -i`

### update genre by {id}
`curl http://localhost:8080/api/v1/genres/{id} -X PUT -H "Content-Type:application/json" -d "{\"name\":\"Updated Genre\"}" -i`

### delete genre by {id}
`curl http://localhost:8080/api/v1/genres/{id} -X DELETE -i`
