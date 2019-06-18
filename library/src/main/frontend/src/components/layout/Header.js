import React from "react";
import {Link} from 'react-router-dom';

export default function Header(props) {
    return (
        <div>
            <nav className="navbar navbar-expand navbar-light bg-light">
                <span className="navbar-brand mb-0 h1">Library</span>
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/">Home</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/books">Books</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/authors">Authors</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/genres">Genres</Link>
                    </li>
                </ul>
            </nav>
        </div>
    )
}