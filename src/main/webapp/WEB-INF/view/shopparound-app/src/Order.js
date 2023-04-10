import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { useCookies } from "react-cookie";
import FetchResource from "./util/FetchResource";

const resource = ((cookies)=>{
  return({
  resource: "/users",
  options: {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + cookies.user.jwt,
    },
  },
  });
});

const headerRow = [
  <tr key="hd">
    <th key="1"> ID# </th>
    <th key="2"> Username </th>
    <th key="3"> First_Name </th>
    <th key="4"> Last_Name </th>
    <th key="5"> Password </th>
    <th key="6"> Address </th>
    <th key="7"> DOB </th>
    <th key="7"> Email </th>
    <th key="8"> Phone#</th>
  </tr>,
];

function table(bodyRow){
  return(
    <table className="table">
        <thead>{headerRow}</thead> 
        <tbody>{bodyRow}</tbody>
    </table>
  );
}

function noContentRow(msg){
  return (
    <tr id="msg-row">
      <h1>{msg}</h1>
    </tr>
  );
}

function contentRow(users){

  let count = 0;
  let row = [];
  
  users.forEach((user)=>{
    row.push(
      <tr key={"user" + count++}>
        <td key={0}>{user.id}</td>
        <td key={1}>{user.username}</td>
        <td key={2}>{user.firstName}</td>
        <td key={3}>{user.lastName}</td>
        <td key={4}>*******</td>
        <td key={5}>{user.address}</td>
        <td key={6}>{user.dateOfBirth}</td>
        <td key={7}>{user.email}</td>
        <td key={8}>{user.phoneNo}</td>
      </tr>
    );
  })
  
  return(row);
}

function UsersTableDisplay({users}) {

  let bodyRow = null;

  if(users.length <= 0){

    bodyRow = noContentRow("No User Content");

  }else{
    bodyRow = contentRow(users);
  }

  return(table(bodyRow));
}

//main funtion 
export default function Users() {
  let [cookies] = useCookies();
  const [display, setDisplay] = useState(table(noContentRow("Loading....")));
  useEffect(() => {}, [display]);

  function fetchResponseHandler(response){

    if(response.status === 200) {
     
      response.json()
      .then((data) => {
        console.log(data);
        if (Object.keys(data.users).length > 0) {
          setDisplay((<UsersTableDisplay users={data.users.content} />));
        } else {
          
          setDisplay(noContentRow("No Content"));
          
        }
      });
    }else{
      setDisplay(noContentRow("Access Denied"));
    }
  }

  FetchResource(resource(cookies))
  .then((response) => {
    fetchResponseHandler(response);
  })
  .catch(() => setDisplay(table(noContentRow("Error 404: Page do not exsit"))));

  return display;
}
