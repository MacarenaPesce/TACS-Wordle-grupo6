import React from 'react';
import "./AddMember.css";

function Member({id, username, toAdd } ) {
    return (
        <div className="form-user"
            onClick={() => toAdd(id)}>
            <span className="form-user-id">{id} </span>
            <span className="form-user-name">{username}</span>
      </div>
    );
}

export default Member;