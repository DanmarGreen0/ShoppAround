import React, { useContext } from "react";
import { useCookies } from "react-cookie";
import {UserContext} from "./App";

function GetFields({cookies}){
    const user = cookies.user;
    let fields = [];
    let count = 0;
    Object.entries(user.accountInfo).forEach(([key,value])=>{
        if(typeof value !== "object")
            fields.push(<li key={count++}>{key}: {value}</li>);

    })

    return (<ul>{fields}</ul>);
}

export default function UserProfile(){
    const[cookies] = useCookies();
    
    
    return (
        <>
        <div> 
            <GetFields cookies={cookies}/>
        </div>
        </>
    );
}