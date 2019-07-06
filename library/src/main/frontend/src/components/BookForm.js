import React, {useEffect, useState} from 'react';
import {Link, withRouter} from 'react-router-dom';

import Loading from './Loading';
import {throwIfError} from './../utils/errorHandler';
import {alertError, alertMessage} from './../utils/alert';
import {getFromApi} from './../utils/backendApi';
import PageTitle from "./layout/PageTitle";
import Comments from "./Comments";

function BookForm({match, history}) {

    const apiPath = "/api/v1/books";
    const bookId = match.params.id;
    const isNew = bookId === "new";

    const [book, setBook] = useState({title: "", year: "", authorId: "", genreId: "", commentDtoList: []});
    const [allAuthors, setAllAuthors] = useState([]);
    const [allGenres, setAllGenres] = useState([]);
    const [isLoading, setIsLoading] = useState(!isNew);

    useEffect(() => {
        const fetchUrl = apiPath + "/formdata/" + (isNew ? "new" : bookId);
        getFromApi(fetchUrl)
            .then(bookForm => {
                if (!isNew) {
                    setBook(bookForm.bookDto);
                }
                setAllAuthors(bookForm.allAuthors);
                setAllGenres(bookForm.allGenres);
                if (isNew) {
                    // init select lists with first value
                    setBook({
                        ...book
                        , authorId: bookForm.allAuthors[0].id
                        , genreId: bookForm.allGenres[0].id
                    })
                }
            })
                .finally(() => setIsLoading(false));
    }, [])



    function create() {
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

    function update() {
        fetch(`${apiPath}/${bookId}`, {
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

    function del() {
        fetch(`${apiPath}/${bookId}`, {
            method: "DELETE",
        })
            .then(response => throwIfError(response))
            .then(response => history.push("/books"))
            .then(response => alertMessage("Book deleted"))
            .catch(error => alertError(error));
    }

    function submitComment(comment) {
        return fetch(`${apiPath}/${bookId}/comments/save`, {
            method: "POST",
            body: JSON.stringify(comment),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => {
                setBook({...book, commentDtoList: [...book.commentDtoList, comment]});
                alertMessage("Comment created")
            });
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
                <form>

                    <div className="form-group">
                        <label>Title:</label>
                        <input className="form-control" type="text" name="title" value={book.title} required
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Year:</label>
                        <input className="form-control" type="number" name="year" value={book.year} required
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Author:</label>
                        <select name="authorId" value={book.authorId} onChange={onTextChange}
                                className="form-control">
                            {allAuthors.map((author, i) => <option key={i}
                                                                   value={author.id}>{author.firstname} {author.lastname}</option>)}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Genres:</label>
                        <select name="genreId" value={book.genreId} onChange={onTextChange}
                                className="form-control">
                            {allGenres.map((genre, i) => <option key={i} value={genre.id}>{genre.name}</option>)}
                        </select>
                    </div>

                    <div className="btn-group">
                        <input type="button" onClick={isNew ? create : update} value="Submit" className="btn btn-primary"/>
                        <Link to="/books" className="btn btn-outline-primary">Cancel</Link>
                        {!isNew &&
                        <input type="button" onClick={del} value="Delete" className="btn btn-outline-danger"/>
                        }
                    </div>

                </form>

                {!isNew &&
                <Comments comments={book.commentDtoList} submitComment={submitComment}/>
                }

            </>
        )
}

export default withRouter(BookForm);
