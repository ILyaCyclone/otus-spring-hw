import React from 'react';
import {Link} from 'react-router-dom';
import PageTitle from "./layout/PageTitle";

export default function Home() {
    return (
        <>
            <PageTitle title="Home"/>
            <div className="list-group">
                <Link to="/books" className="list-group-item list-group-item-action">Books</Link>
                <Link to="/authors" className="list-group-item list-group-item-action">Authors</Link>
                <Link to="/genres" className="list-group-item list-group-item-action">Genres</Link>
            </div>
        </>
    )
}