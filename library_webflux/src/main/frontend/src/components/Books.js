import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

import PageTitle from './layout/PageTitle';

import Loading from './Loading';
import {getFromApi} from "../utils/backendApi";

export default function Books() {
    const [books, setBooks] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getFromApi("/api/v1/books")
            .then(books => setBooks(books))
            .finally(() => setIsLoading(false));
    }, []);

    return (
        <>
            <PageTitle title="Books"/>
            {isLoading ?
                <Loading/>
                :
                books.length > 0 ?
                    <>
                        <table className="table table-hover w-75">
                            <thead>
                            <tr>
                                <th>Title</th>
                                <th>Year</th>
                                <th>Author</th>
                                <th>Genre</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                books.map((book, i) => (
                                    <tr key={i}>
                                        <td>
                                            <Link to={`/books/${book.id}`}>{book.title}</Link>
                                        </td>
                                        <td>{book.year}</td>
                                        <td>{book.authorFirstname} {book.authorLastname}</td>
                                        <td>{book.genreName}</td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                    </>
                    :
                    <p>No data available</p>
            }
            <Link className="btn btn-success" to="/books/new">Create new book</Link>
        </>
    )
}