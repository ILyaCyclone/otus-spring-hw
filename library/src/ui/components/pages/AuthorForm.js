import React from 'react';

export default class AuthorForm extends React.Component {

    constructor() {
        super();
        this.state = {author: {}};
    }

    componentDidMount() {
        console.log(this.props.match.params.id);
        fetch('/api/v1/authors/' + this.props.match.params.id)
            .then(response => response.json())
            .then(author => this.setState({author}));
    }

    onTextChange(e) {
        console.log(e);
        // var localAuthor = {...this.state.author};
        // localAuthor[e.target.name] = e.target.value;
        // this.setState({author: localAuthor});
        // this.setState({ author: { ...this.state.author, flag: false} });
    }

    //             <h2>{this.props.match.params.id}</h2>

    render() {
        return (
            <form>

                <div className="form-group">
                    <label>Firstname:</label>
                    <input className="form-control" type="text" name="firstname" value={this.state.author.firstname}
                           onChange={this.onTextChange}/>
                </div>

                <div className="form-group">
                    <label>Lastname:</label>
                    <input className="form-control" type="text" name="lastname" value={this.state.author.lastname}/>
                </div>

                <input className="btn btn-primary" type="submit" value="Submit"/>
                <a className="btn btn-outline-primary" href="@{/authors}">Cancel</a>

            </form>
        )
    }
}