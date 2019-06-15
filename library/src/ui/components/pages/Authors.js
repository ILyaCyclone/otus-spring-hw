import React from 'react';
import {Link} from 'react-router-dom';

export default class Authors extends React.Component {

    constructor() {
        super();
        this.state = {authors: []};
    }

    componentDidMount() {
        fetch('api/v1/authors')
            .then(response => response.json())
            .then(authors => this.setState({authors}));
    }

    render() {
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
                        this.state.authors.map((author, i) => (
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
}