import React from "react";
import {Link} from 'react-router-dom';

export default function Header({currentUserName}) {
    return (
        <div>
            <nav className="navbar navbar-expand navbar-light bg-light">
                <span className="navbar-brand mb-0 h1">Library</span>
                <ul className="navbar-nav mr-auto">
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
                {currentUserName !== "" &&
                <>
                    <span className="navbar-text">
                        Logged user: {currentUserName}
                    </span>
                    <a href="/logout" className="btn btn-sm btn-outline-primary ml-4">Logout</a>
                </>
                }
            </nav>
        </div>
    )
}