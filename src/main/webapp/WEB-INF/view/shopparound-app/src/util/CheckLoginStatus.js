import { useEffect } from "react";
import { useNavigate } from "react-router";

 const CheckLoginStatus = ((cookies) => {
  
    const navigate = useNavigate();
    useEffect(()=>{
    if(Object.entries(cookies).length !== 0){
        if(Object.entries(cookies.userProfile).length === 0){
            

                navigate("/");

           
            

        }
    } 
},[]);

});

export default CheckLoginStatus;