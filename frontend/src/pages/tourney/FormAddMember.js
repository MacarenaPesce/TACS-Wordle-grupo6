import React, { useState } from 'react';
//import './FormAddMember.css';
import UserService from '../../service/UserService';

function AddMember() {
  const [USERS,setUsers] = useState([]);

  /*UserService.getUsers()
  .then(response => {
          console.log('Response de usuarios obtenida: ')
          console.log(response.data)
          setUsers(response.data)
          console.log("USUARIOS:",USERS)
      })
      .catch(error => {
        console.log(error)
        // todo: falta manejo de error como en tourney create 
    })*/

	const [username, setName] = useState('');
  
	const [foundUsers, setFoundUsers] = useState(USERS);
  
	const filter = (e) => {
    const keyword = e.target.value;

    if (keyword !== '') {
      const results = USERS.filter((user) => {
        return user.username.toLowerCase().startsWith(keyword.toLowerCase());
        // Use the toLowerCase() method to make it case-insensitive
      });
      setFoundUsers(results);
    } else {
      setFoundUsers(USERS);
      // If the text field is empty, show all users
    }

    setName(keyword);
  };

  return (
    <div className="container">
      <input
        type="search"
        value={username}
        onChange={filter}
        className="input"
        placeholder="Filter"
      />

      <div className="user-list">
        {foundUsers && foundUsers.length > 0 ? (
          foundUsers.map((user) => (
            <li key={user.id} className="user">
              <span className="user-id">{user.id} </span>
              <span className="user-name">{user.username}</span>
            </li>
          ))
        ) : (
          <h1>No results found!</h1>
        )}
      </div>
    </div>
  );
}


export default AddMember;