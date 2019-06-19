import React, {useEffect, useState} from 'react';
import {Link, withRouter} from 'react-router-dom';

import Loading from './Loading';
import {throwIfError} from './../utils/errorHandler';
import {alertError, alertMessage} from './../utils/alert';
import PageTitle from "./layout/PageTitle";
import {getFromApi} from "../utils/backendApi";

function GenreForm({match, history}) {

    const apiPath = "/api/v1/genres/";
    const genreId = match.params.id;
    const isNew = genreId === "new";

    const [genre, setGenre] = useState({});
    const [isLoading, setIsLoading] = useState(!isNew);

    useEffect(() => {
        if (!isNew) {
            getFromApi(apiPath + genreId)
                .then(genre => setGenre(genre))
                .finally(() => setIsLoading(false));
        }
    }, [])


    function create(e) {
        e.preventDefault();
        fetch(apiPath, {
            method: "POST",
            body: JSON.stringify(genre),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => response.json())
            .then(created => history.push("/genres/" + created.id))
            .then(response => alertMessage("Genre created"))
            .catch(error => alertError(error));
    }

    function update(e) {
        e.preventDefault();
        fetch(apiPath + genreId, {
            method: "PUT",
            body: JSON.stringify(genre),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => throwIfError(response))
            .then(response => alertMessage("Genre updated"))
            .catch(error => alertError(error));
    }

    function del(e) {
        fetch(apiPath + genreId, {
            method: "DELETE",
        })
            .then(response => throwIfError(response))
            .then(response => history.push("/genres"))
            .then(response => alertMessage("Genre deleted"))
            .catch(error => alertError(error));
    }


    function onTextChange(e) {
        setGenre({...genre, [e.target.name]: e.target.value});
    }

    if (isLoading === true)
        return (<Loading/>)
    else
        return (
            <>
                <PageTitle title={isNew ? "Create genre" : "Edit genre"}/>
                <form onSubmit={isNew ? create : update}>

                    <div className="form-group">
                        <label>Name:</label>
                        <input className="form-control" type="text" name="name" value={genre.name}
                               onChange={onTextChange}/>
                    </div>

                    <div className="btn-group">
                        <input className="btn btn-primary" type="submit" value="Submit"/>
                        <Link className="btn btn-outline-primary" to="/genres">Cancel</Link>
                        {!isNew &&
                        <input type="button" className="btn btn-outline-danger" onClick={del} value="Delete"/>
                        }
                    </div>

                </form>
            </>
        )
}

export default withRouter(GenreForm);