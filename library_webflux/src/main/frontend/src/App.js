import React from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Header from './components/layout/Header';
import Home from "./components/Home";
import Authors from "./components/Authors";
import Genres from "./components/Genres";
import Books from "./components/Books";
import AuthorForm from "./components/AuthorForm";
import GenreForm from "./components/GenreForm";
import BookForm from "./components/BookForm";


export default function App() {
    return (
        <>
            <BrowserRouter>
                <Header/>
                <div className="container">
                    <Switch>
                        <Route exact path="/" component={Home}/>
                        <Route exact path="/authors" component={Authors}/>
                        <Route exact path="/authors/:id" component={AuthorForm}/>

                        <Route exact path="/genres" component={Genres}/>
                        <Route exact path="/genres/:id" component={GenreForm}/>

                        <Route exact path="/books" component={Books}/>
                        <Route exact path="/books/:id" component={BookForm}/>

                        <Route render={() => <h2>No such page</h2>}/>
                        {/*<Route component={Home}/>*/}
                    </Switch>
                </div>
            </BrowserRouter>


        </>
    )
}