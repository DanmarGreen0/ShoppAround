import{ useEffect, useRef, useState} from "react";
import { useNavigate } from 'react-router-dom';
import { useCookies } from "react-cookie";
import LoginError from "./LoginError";
import "./styles/Login.css"

 function submitLogin(inputs){

    const url = 'http://localhost:8080/perform_login';
    const options = {
        method: "POST",
        headers: {'Content-Type': 'application/json','Authorization': 'Basic '+ window.btoa(`${inputs.username}:${inputs.password}`)},
        body: JSON.stringify(inputs)
    };
    
    return fetch(
        url,
        options,
    );
}


function Login(prop) {
    const [cookies, setCookies] = useCookies();
    const [user, setUser] = useState({});
    const navigate = useNavigate();
    const [inputs, setInputs] = useState({});
    const [errorInputCount, setErrorInputCount] = useState(0);
    const [loginError, setLoginError] = useState(false);
    const inputErrors = useRef({
                                "usernameInputError": false,
                                "passwordInputError": false,
                                "errorInputCount" : 0
                            });

    useEffect(()=>{}, [errorInputCount]);

    const HandleInputChangeEvent = (event) => {

        const target = event.target;
        const value = target.value;
        const name = target.name;
        
        setInputs(values => ({
            ...values, [name]: value
        }));
    }
    console.log(prop);
    const HandleSubmitFromEvent = (event) => {
       
        if(Object.entries(inputs).length <= 0 || 
            (inputs.username === '' && inputs.password === '')){

            inputErrors.current.usernameError = true; 
            inputErrors.current.passwordError = true;
                       
        }else{

            if(typeof(inputs.username) === 'undefined' || inputs.username === ''){
                inputErrors.current.usernameError = true; 
                inputErrors.current.passwordError = false;  
            }else if(typeof(inputs.password) === 'undefined' || inputs.password === ''){
                inputErrors.current.usernameError = false; 
                inputErrors.current.passwordError = true;  
            }else{
                
                submitLogin(inputs)
                .then((response)=>{
                    if(response.status === 202){
                   
                        response.json().then((result)=>{
                            console.log(result);
                            setCookies(
                               "user",
                               result
                            );
                           setCookies(
                            "needLogin",
                            false
                           );
                           
                        });
                       
                        navigate(-1);
                    }else{
                        if(!loginError){
                            setLoginError(true);
                            inputErrors.current.usernameError = false; 
                            inputErrors.current.passwordError = false;
                        }
                    }
                    
                })
                .catch((err) => console.log(err));
                
            }
           
        }
        
        setErrorInputCount((c)=> c + 1);
        event.preventDefault();
    }

    var usernameInput = inputs.username || "";
    var passwordInput = inputs.password || "";

    return (
        <div className="loginForm-container">
            {(loginError) && <LoginError />}
            <form className="loginForm"  onSubmit={HandleSubmitFromEvent}>
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <div className="password-field-container">
                                    <label htmlFor="username">
                                        Username
                                        <input 
                                            name="username" 
                                            id="username"
                                            type="text"
                                            defaultValue={usernameInput} 
                                            onChange={HandleInputChangeEvent} 
                                        />
                                    
                                    { (inputErrors.current.usernameError) ? <span className="usernameError">Please enter username.</span> : <></>}
                                    </label>
                                    
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div className="password-field-container">
                                    <label htmlFor="password">
                                        Password
                                        <input 
                                            name="password"
                                            id='password'
                                            type="text" 
                                            // autoComplete="off"
                                            defaultValue={passwordInput} 
                                            onChange={HandleInputChangeEvent} 
                                        />    
                                    { inputErrors.current.passwordError ? <span className="passwordError">Please enter password.</span> : <></>}
                                    </label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div className="button-field-container">
                                    <input id="submit-button" type="submit" value="Login" readOnly/>
                                    <input id="cancel-button" type="cancel" value="Cancle" readOnly onClick={()=> navigate('/')} />
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    );

}

export default Login;
