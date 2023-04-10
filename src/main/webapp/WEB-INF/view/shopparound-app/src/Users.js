import {useEffect, useState} from "react";
import { useCookies} from "react-cookie";
import FetchResource from "./util/FetchResource";
import CreateTable from "./component/CreateTable";

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


const TableHeaders = [
  "ID#",
  "Username",
  "First_Name",
  "Last_Name",
  // "Password",
  "Address",
  "DOB",
  "Email",
  "Phone#",
  "Created-At",
  "Modified_At"
];


export default function Users() {
  const [cookies] = useCookies();
  const [data,setData] = useState("Loading....");
  

  // const navigate = useNavigate();
  
  //   if(Object.entries(cookies).length !== 0){
  //       if(Object.entries(cookies.userProfile).length === 0){
  //           navigate("/");
        
  //           let test = false;
  //       }
  //   } 
  
 

  // let data = FetchResource(resource(cookies)); //fetch users account info from backend server

  useEffect(()=>{
    FetchResource(resource(cookies)).then((data)=>{
     
      setData(data);

    });
  },[cookies]);

  console.log(data);

  if((typeof data) === "string"){
    return (<CreateTable 
      tableData={{
        "headerContents": TableHeaders,
        "bodyContents": data
        }}
    />);
  }

  let users = data.users.content;
  
  for(let i = 0; i < users.length; i++){

    delete users[i].password;
    delete users[i].roles;
    delete users[i].links;
    
  }

  return (
    <>
      <div className="users-table-container">
        {<CreateTable 
          tableData={{
            "headerContents": TableHeaders,
            "bodyContents": users
            }}
        />}
      </div>
      {/* <div id="addProduct-tb-floating-button-container">
        <button type="button" className="btn btn-primary" onClick={HandleButtonClick}>  
          <i className="fas fa-plus-circle"></i> add Product
        </button>
      </div> */}
    </>
  );
}
