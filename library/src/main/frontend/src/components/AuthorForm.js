import React, {useEffect, useState} from 'react';
import {Link, withRouter} from 'react-router-dom';

import Loading from './Loading';
import {throwIfError} from './../utils/errorHandler';
import {alertError, alertMessage} from './../utils/alert';
import PageTitle from "./layout/PageTitle";
import {getFromApi} from "../utils/backendApi";

function AuthorForm({match, history}) {

    const apiPath = "/api/v1/authors/";
    const authorId = match.params.id;
    const isNew = authorId === "new";

    const [author, setAuthor] = useState({firstname: "", lastname: "", homeland: ""});
    const [isLoading, setIsLoading] = useState(!isNew);

    useEffect(() => {
        if (!isNew) {
            getFromApi(apiPath + authorId)
                .then(author => setAuthor(author))
                .finally(() => setIsLoading(false));
        }
    }, [])


    function create() {
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

    function update() {
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

    function del() {
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
                <PageTitle title={isNew ? "Create author" : "Edit author"}/>
                <form>

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

                    <div className="btn-group">
                        <input type="button" onClick={isNew ? create : update} value="Submit" className="btn btn-primary"/>
                        <Link to="/authors" className="btn btn-outline-primary">Cancel</Link>
                        {!isNew &&
                        <input type="button" onClick={del} value="Delete" className="btn btn-outline-danger"/>
                        }
                    </div>

                </form>
            </>
        )
}

export default withRouter(AuthorForm);
