import React from "react";
import {Link} from 'react-router-dom';

export default function Header(props) {
    return (
        <header>
            <nav>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/authors">Authors</Link></li>
                    <li><Link to="/genres">Genres</Link></li>
                    <li><Link to="/books">Books</Link></li>
                </ul>
            </nav>
        </header>
    )
}