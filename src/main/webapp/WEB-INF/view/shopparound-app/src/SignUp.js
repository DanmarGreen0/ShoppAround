import {useState} from "react";
import { useNavigate } from "react-router";
import { useCookies } from "react-cookie";
import FetchResource from "./util/FetchResource";

let Resource = ((cookies, body)=>{
    return({
    resource: "/user",
    options: {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + cookies.user.jwt,
      },
      body: JSON.stringify(body)
    },
    });
});

export default function SignUp(){
    const navigate = useNavigate();
    const [cookies,] = useCookies();
    const [inputs, setInputs] = useState({});

    const HandleChangeEvent = (event) => {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setInputs((values) => ({...values, [name]: value }));
    }

    const HandleSubmitButtonClickEvent = ()=>{

        FetchResource(Resource(cookies, inputs)).then((data1)=>{
            
            setData(data1)
            
        });

        
    }

    const HandleCancelButtonClickEvent = ()=> {

        navigate(-1);
        
    }


    let usernameInput = inputs.username || "";
    let firstNameInput = inputs.firstName || "";
    let lastNameInput = inputs.lastName || "";
    let passwordInput = inputs.password || "";
    let addressInput = inputs.price || "";
    let dateOfBirthInput = inputs.dateOfBirth || "";
    let emailInput = inputs.email || "";
    let phoneNoInput = inputs.phoneNo || "";

    return(
        <>
        <form className="addProduct-form" onSubmit={HandleSubmitButtonClickEvent}>
                    <div id="fields">
                        <div>
                            <label htmlFor="name">Username:</label>
                            <input 
                                type="text" 
                                name="username"
                                id="username"
                                defaultValue={usernameInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="firstName">First Name:</label>
                            <input 
                                name="firstName"
                                id="firstName"
                                defaultValue={firstNameInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="lastName">Last Name:</label>
                            <input 
                                type="text" 
                                name="lastName"
                                id="lastName"
                                defaultValue={lastNameInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password:</label>
                            <input 
                                type="text" 
                                name="password"
                                id="password"
                                defaultValue={passwordInput}
                                onChange={HandleChangeEvent}
                                />
                        </div>
                        <div>
                            <label htmlFor="address">Address:</label>
                            <input 
                                name="address" 
                                id="address"
                                type="text" 
                                defaultValue={addressInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="dateOfBirth">DOB:</label>
                            <input 
                                name="dateOfBirth" 
                                id="dateOfBirth"
                                type="text" 
                                defaultValue={dateOfBirthInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="email">Email:</label>
                            <input 
                                name="email" 
                                id="email"
                                type="text" 
                                defaultValue={emailInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="phoneNo">PhoneNo:</label>
                            <input 
                                name="phoneNo" 
                                id="phoneNo"
                                type="text" 
                                defaultValue={phoneNoInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                    </div>
                    <div id="buttons">
                        <input type="submit" value="Submit" id="submit" />
                        <button type="button" value="Cancel" id="cancel" onClick={HandleCancelButtonClickEvent}>Cancel</button>
                    </div>
                </form>
        </>
    );
}