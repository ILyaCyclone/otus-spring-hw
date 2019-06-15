import React, {useEffect, useState} from 'react';

export default function AuthorForm({match}) {

    const [author, setAuthor] = useState({});

    useEffect(() => {
        console.log(match.params.id);
        fetch('/api/v1/authors/' + match.params.id)
            .then(response => response.json())
            .then(author => setAuthor(author));
    }, [])

    function onTextChange(e) {
        console.log(e);
        setAuthor({...author, [e.target.name]: e.target.value});
        // var localAuthor = {...this.state.author};
        // localAuthor[e.target.name] = e.target.value;
        // this.setState({author: localAuthor});
        // this.setState({ author: { ...this.state.author, flag: false} });
    }

    //             <h2>{this.props.match.params.id}</h2>

    return (
        <form>

            <div className="form-group">
                <label>Firstname:</label>
                <input className="form-control" type="text" name="firstname" value={author.firstname}
                       onChange={onTextChange}/>
            </div>

            <div className="form-group">
                <label>Lastname:</label>
                <input className="form-control" type="text" name="lastname" value={author.lastname}
                       onChange={onTextChange}/>
            </div>

            <div className="form-group">
                <label>Homeland:</label>
                <input className="form-control" type="text" name="homeland" value={author.homeland}
                       onChange={onTextChange}/>
            </div>

            <input className="btn btn-primary" type="submit" value="Submit"/>
            <a className="btn btn-outline-primary" href="@{/authors}">Cancel</a>

        </form>
    )
}