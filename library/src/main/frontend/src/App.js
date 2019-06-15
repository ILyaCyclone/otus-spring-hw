import React from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Header from './components/layout/Header';

import Authors from './components/Authors'
import Home from "./components/Home";
import AuthorForm from "./components/AuthorForm";

function App() {
    return (
        <>
            <BrowserRouter>
                <Header/>
                <Switch>
                    <Route exact path="/" component={Home}/>
                    <Route exact path="/authors" component={Authors}/>
                    <Route exact path="/authors/:id" component={AuthorForm}/>
                </Switch>
            </BrowserRouter>
        </>
    )
}

export default App;
