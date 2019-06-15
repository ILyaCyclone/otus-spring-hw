import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

export default function Authors() {
    const [authors, setAuthors] = useState([]);

    useEffect(() => {
        fetch("/api/v1/authors")
            .then(response => response.json())
            .then(authors => setAuthors(authors));
    }, []);

    return (
        <React.Fragment>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
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
        </React.Fragment>
    )
}