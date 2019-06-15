import React from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Header from './layout/Header';

import Home from './pages/Home';
import Authors from "./pages/Authors";
import AuthorForm from "./pages/AuthorForm";

export default class App extends React.Component {

    render() {
        return (
            <React.Fragment>
                <BrowserRouter>
                    <Header/>
                    <Switch>
                        <Route exact path="/" component={Home}/>
                        <Route exact path="/authors" component={Authors}/>
                        <Route exact path="/authors/:id" component={AuthorForm}/>
                    </Switch>
                </BrowserRouter>
            </React.Fragment>
        )
    }
};