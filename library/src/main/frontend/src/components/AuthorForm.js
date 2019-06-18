import React, {useEffect, useState} from 'react';
import {Link, withRouter} from 'react-router-dom';

import Loading from './Loading';
import {throwIfError} from './../utils/errorHandler';
import {alertError, alertMessage} from './../utils/alert';

function AuthorForm({match, history}) {

    const apiPath = "/api/v1/authors/";
    const authorId = match.params.id;
    const isNew = authorId === "new";

    const [author, setAuthor] = useState({});
    const [isLoading, setIsLoading] = useState(!isNew);

    useEffect(() => {
        if (!isNew) {
            fetch(apiPath + authorId)
                .then(response => throwIfError(response))
                .then(response => response.json())
                .then(author => setAuthor(author))
                .catch(error => alertError(error))
                .finally(() => setIsLoading(false));
        }
    }, [])


    function create(e) {
        e.preventDefault();
        fetch(apiPath, {
            method: "POST",
            body: JSON.stringify(author),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => response.json())
            .then(created => history.push("/authors/" + created.id))
            .then(response => alertMessage("Author created"))
            .catch(error => alertError(error));
    }

    function update(e) {
        e.preventDefault();
        fetch(apiPath + authorId, {
            method: "PUT",
            body: JSON.stringify(author),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => alertMessage("Author updated"))
            .catch(error => alertError(error));
    }

    function del(e) {
        fetch(apiPath + authorId, {
            method: "DELETE",
        })
            .then(response => throwIfError(response))
            .then(response => history.push("/authors"))
            .then(response => alertMessage("Author deleted"))
            .catch(error => alertError(error));
    }


    function onTextChange(e) {
        setAuthor({...author, [e.target.name]: e.target.value});
    }

    if (isLoading === true)
        return (<Loading/>)
    else
        return (
            <>
                <form onSubmit={isNew ? create : update}>

                    <div className="form-group">
                        <label>Firstname:</label>
                        <input className="form-control" type="text" name="firstname" value={author.firstname}
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Lastname:</label>
                        <input className="form-control" type="text" name="lastname" value={author.lastname}
                               onChange={onTextChange}/>
                    </div>

                    <div className="form-group">
                        <label>Homeland:</label>
                        <input className="form-control" type="text" name="homeland" value={author.homeland}
                               onChange={onTextChange}/>
                    </div>

                    <input className="btn btn-primary" type="submit" value="Submit"/>
                    <Link className="btn btn-outline-primary" to="/authors">Cancel</Link>

                </form>
                <button onClick={del}>Delete</button>
            </>
        )
}

export default withRouter(AuthorForm);
