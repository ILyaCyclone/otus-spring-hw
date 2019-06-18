import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

import Loading from './Loading';

export default function Genres() {
    const [genres, setGenres] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetch("/api/v1/genres")
            .then(response => {
                setIsLoading(false);
                return response.json();
            })
            .then(genres => setGenres(genres));
    }, []);

    if (isLoading === true) {
        return (<Loading/>)
    } else {
        return (
            <React.Fragment>

                <table>
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
                <Link to="/genres/new">Create new genre</Link>
            </React.Fragment>
        )
    }
}