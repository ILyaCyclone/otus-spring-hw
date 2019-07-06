import React, {useState} from 'react';
import {alertError} from "../utils/alert";


export default function Comments(props) {
    const comments = props.comments;
    const emptyComment = {commentator: "", text: ""};
    const [newComment, setNewComment] = useState(emptyComment);

    function submit() {
        props.submitComment(newComment)
            .then(() => setNewComment(emptyComment))
            .catch(error => alertError(error));
    }


    function onTextChange(e) {
        setNewComment({...newComment, [e.target.name]: e.target.value});
    }

    return (
        <section className="my-4">
            <div>
                <h4>Comments</h4>
                {comments.length > 0 ?
                    <ul className="list-group list-group-flush">
                        {comments.map((comment, i) =>
                            <li key={i} className="list-group-item">
                                <strong>{comment.commentator}: </strong>
                                <span>{comment.text}</span>
                            </li>
                        )
                        }
                    </ul>
                    :
                    <p>No comments yet</p>
                }
            </div>

            <div className="mt-4">
                <h4>Leave a comment:</h4>
                <form>

                    <div className="form-group">
                        <input value={newComment.commentator} onChange={onTextChange}
                               name="commentator" type="text" required
                               className="form-control" placeholder="Your name..."
                        />
                    </div>

                    <div className="form-group">
                        <textarea value={newComment.text} onChange={onTextChange}
                                  name="text" required
                                  className="form-control" placeholder="Type a comment..."
                        />
                    </div>

                    <input type="button" onClick={submit} value="Submit" className="btn btn-primary"/>
                </form>
            </div>
        </section>
    )
}