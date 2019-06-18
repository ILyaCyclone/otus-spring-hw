import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

import Loading from './Loading';

export default function Authors() {
    const [authors, setAuthors] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetch("/api/v1/authors")
            .then(response => {
                setIsLoading(false);
                return response.json();
            })
            .then(authors => setAuthors(authors));
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
                        <th>Homeland</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        authors.map((author, i) => (
                            <tr key={i}>
                                <td>
                                    <Link to={`/authors/${author.id}`}>{author.firstname} {author.lastname}</Link>
                                </td>
                                <td>{author.homeland}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
                <Link to="/authors/new">Create new author</Link>
            </React.Fragment>
        )
    }
}