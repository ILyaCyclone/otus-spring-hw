import React from 'react';
import {Link} from 'react-router-dom';

export default function Home() {
    return (
        <ul>
            <li><Link to="/authors">Authors</Link></li>
            <li><Link to="/genres">Genres</Link></li>
            <li><Link to="/books">Books</Link></li>
        </ul>
    )
}