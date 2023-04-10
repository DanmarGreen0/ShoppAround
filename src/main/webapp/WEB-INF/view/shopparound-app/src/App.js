import { createContext, useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import {BrowserRouter as Router,Routes, Route, Navigate} from 'react-router-dom';
import './styles/App.css';
import Layout from './Layout';
import HomePage from './HomePage';
import Login from './Login';
import SignUp from './SignUp';
import Profile from './Profile';
import UserProfile from './UserProfile';
import Users from './Users';
import Products from './Products';
import Discounts from './Discounts';
import SetCookies from './cookies/SetCookies';
// import GetCookies from './cookies/GetCookies';
import TestPage from './TestPage';


//https://www.copycat.dev/blog/react-router-redirect/#:~:text=Router%20Dom%20here.-,What%20is%20React%20Router%20Redirect%3F,route%20to%20a%20new%20route.&text=The%20component%20has,%2F%3E%20in%20React%20Router%20v6.

/* none export var */


/* exports var */



function App() {

  const [cookies, setCookies] = useCookies(null);
  let route = null;
 
  useEffect(()=>{},[cookies]);

  if(Object.entries(cookies).length === 0){
    SetCookies({
      "name": "user",
      "value": {},
      "option": {path: '/'}
    });
    SetCookies({
      "name": "needLogin",
      "value": true,
      "option": {path: '/'}
    });
    console.log(cookies);
  }
    
  
  
  return (
    <Router>
      <Routes >
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/userProfile" element={<UserProfile />} />
          <Route path="/users" element={ cookies.needLogin === "true" ? <Navigate replace to="/login" /> : <Users /> } />
          <Route path="/products" element={<Products />} />
          <Route path="/discounts" element={<Discounts />} />
          <Route path="/login"  element={<Login />} />
          <Route path="/signUp"  element={<SignUp />} />
          <Route path="testPage" element={<TestPage />} />
        </Route>
      </Routes>
    </Router>  
  );
}


export default App;