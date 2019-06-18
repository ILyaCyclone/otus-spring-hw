import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import Loading from "./Loading";
import PageTitle from "./layout/PageTitle";
import {alertError} from "../utils/alert";
import {throwIfError} from "../utils/errorHandler";

export default function Genres() {
    const [genres, setGenres] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetch("/api/v1/genres")
            .then(response => throwIfError(response))
            .then(response => response.json())
            .then(genres => setGenres(genres))
            .catch(error => alertError(error))
            .finally(() => setIsLoading(false))
    }, []);


    return (
        <>
            <PageTitle title="Genres"/>
            {isLoading ?
                <Loading/>
                :
                genres.length > 0 ?
                    <>
                        <table className="table table-hover w-50">
                            <thead>
                            <tr>
                                <th>Name</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                genres.map((genre, i) => (
                                    <tr key={i}>
                                        <td>
                                            <Link to={`/genres/${genre.id}`}>{genre.name}</Link>
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                    </>
                    :
                    <p>No data available</p>

            }
            <Link className="btn btn-success mt-3" to="/genres/new">Create new genre</Link>
        </>
    )
}