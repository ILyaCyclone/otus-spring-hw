import React, {useEffect, useState} from 'react';
import {Link, withRouter} from 'react-router-dom';

import Loading from './Loading';
import {throwIfError} from './../utils/errorHandler';
import {alertError, alertMessage} from './../utils/alert';
import {getFromApi} from './../utils/backendApi';
import PageTitle from "./layout/PageTitle";

function BookForm({match, history}) {

    const apiPath = "/api/v1/books/";
    const bookId = match.params.id;
    const isNew = bookId === "new";

    const [book, setBook] = useState({});
    const [allAuthors, setAllAuthors] = useState([]);
    const [allGenres, setAllGenres] = useState([]);
    const [isLoading, setIsLoading] = useState(!isNew);

    useEffect(() => {
        if (!isNew) {
            getFromApi(apiPath + bookId)
                .then(book => setBook(book))
                .finally(() => setIsLoading(false));
        }
    }, [])

    useEffect(() => {
        getFromApi("/api/v1/authors")
            .then(authors => setAllAuthors(authors));
    }, [])

    useEffect(() => {
        getFromApi("/api/v1/genres")
            .then(genres => setAllGenres(genres));
    }, [])


    function create(e) {
        e.preventDefault();
        fetch(apiPath, {
            method: "POST",
            body: JSON.stringify(book),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => response.json())
            .then(created => history.push("/books/" + created.id))
            .then(response => alertMessage("Book created"))
            .catch(error => alertError(error));
    }

    function update(e) {
        e.preventDefault();
        fetch(apiPath + bookId, {
            method: "PUT",
            body: JSON.stringify(book),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => alertMessage("Book updated"))
            .catch(error => alertError(error));
    }

    function del(e) {
        fetch(apiPath + bookId, {
            method: "DELETE",
        })
            .then(response => throwIfError(response))
            .then(response => history.push("/books"))
            .then(response => alertMessage("Book deleted"))
            .catch(error => alertError(error));
    }


    function onTextChange(e) {
        setBook({...book, [e.target.name]: e.target.value});
    }

    if (isLoading === true)
        return (<Loading/>)
    else
        return (
            <>
                <PageTitle title={isNew ? "Create book" : "Edit book"}/>
                <form onSubmit={isNew ? create : update}>

                    <div className="form-group">
                        <label>Title:</label>
                        <input className="form-control" type="text" name="title" value={book.title}
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Year:</label>
                        <input className="form-control" type="number" name="year" value={book.year}
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Author:</label>
                        <select className="form-control" name="authorId">
                            {allAuthors.map((author, i) => <option key={i}
                                                                   value={author.id}>{author.firstname} {author.lastname}</option>)}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Genres:</label>
                        <select className="form-control" name="genreId">
                            {allGenres.map((genre, i) => <option key={i} value={genre.id}>{genre.name}</option>)}
                        </select>
                    </div>

                    <div className="btn-group">
                        <input className="btn btn-primary" type="submit" value="Submit"/>
                        <Link className="btn btn-outline-primary" to="/books">Cancel</Link>
                        {!isNew &&
                        <input type="button" className="btn btn-outline-danger" onClick={del} value="Delete"/>
                        }
                    </div>

                </form>

            </>
        )
}

export default withRouter(BookForm);
