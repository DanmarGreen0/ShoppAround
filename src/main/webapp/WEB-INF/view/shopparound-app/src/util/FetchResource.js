import { useEffect, useState, memo} from "react";

//get resources from server
 async function FetchResource(props){
  const url = 'http://localhost:8080' + props.resource;
  const options = {
    method: props.options.method,
    headers: props.options.headers,
    body: props.options.body
  };

  //remove body field from options(object)
  if(options.body === undefined){
    delete options.body;
  }

  // useEffect(()=>{

   
    return fetch(url, options).then((response)=>{
      try {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 401) { //handles error thrown from server
          return(response.status + " Unauthorized access");
        } else if (response.status === 404) {
          return(response.status + " Page not found");
        } else {
          return(response.status + " " + response.statusText);
        }
      } catch {
       
          return ("Error 503: Sorry! Server is Unavailable")
        
      }

    });
   

  // },[]);

  // if(fetchedData === null) 
  //   return("Still loading"); //return if data is still loading


  // return fetchedData; //return if data is loaded
}

export default FetchResource;