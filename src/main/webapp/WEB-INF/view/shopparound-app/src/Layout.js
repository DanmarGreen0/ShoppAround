import { Outlet, NavLink } from "react-router-dom";
import { useCookies } from "react-cookie";
import {FaShoppingCart} from "react-icons/fa";
import {RxAvatar} from "react-icons/rx";
import "./styles/Layout.css"


function Header({cookies}){

    const user = cookies.user;
 console.log(user);
    return(
        <>
            <div id="logo-link-container">
                <NavLink id="logo-link" to="/">logo</NavLink>
            </div>

            <div className="searchBoxContainer">
                <input className="input" type="text" placeholder="Search" aria-label="Search" />
            </div>

            <div className="login-register-menu-link-container">
                {(Object.entries(user).length === 0) ? 
                (<NavLink id="login-register-menu-hover-link" to="/profile"><RxAvatar /></NavLink>) : 
                (<NavLink id="userprofile-menu-hover-link" to="/profile">
                   {user.accountInfo.firstName} 
                </NavLink>) }
            </div>

            <div className= "cart-link-container">
                <NavLink id="cart-hover-link" to="/cart"> 
                    <FaShoppingCart />
                </NavLink>
            </div>
        </>
    );
}

function NavBar(cookies){

    // const[user, setUser] = useContext(UserContext);
    const postMethod = "POST";
    const getMethod = "GET";
    const authorization = "Bearer " + cookies.jwt;

    function createStateObject(resource, options){
        return {
          "resource":resource, 
          "options": options
        };
      }

    return(
        <>
            <div>
                <NavLink id="home-hover-link" to="/">Home</NavLink>
            </div>

            <div>
                <NavLink id="users-hover-link" to="/users" state={createStateObject(
                    "/users",
                    {
                        method: getMethod,
                        headers: {'Content-Type': 'application/json','Authorization': authorization},
                    }
                )}>Users</NavLink>
            </div>

            <div>
                <NavLink id="products-hover-link" to="/products" state={createStateObject(
                    "/products",
                    {
                        method: getMethod,
                        headers: {'Content-Type': 'application/json','Authorization': authorization},
                    }
                )}>Products</NavLink>
            </div>

            {/* <div>
                <NavLink to="/">Orders</NavLink>
            </div> */}

            <div>
                <NavLink id="discounts-hover-link" to="/discounts">Discounts</NavLink>
            </div>

            {/* <div>
                <NavLink to="/">Admin-View</NavLink>
            </div> */}
        </>
    );
}

function Section(){



    return(
        <div className="screen">
            <Outlet />
        </div> 
    );
}

function Footer(){
    return(
        <>
            <div id="section-1">
                <p>social media</p>
            </div>
            <div id="section-2">
                <p>© 2023 Shopp Around</p>
            </div>
        </>
    );
}


function PageLayout(){
    const [cookies] = useCookies();

    return(
    <div className="page">
        <div className="part-1" >
            <header id="main-header" >{<Header cookies={cookies} />}</header>
            <hr id="line-break" />
            <nav id="main-nav" >{<NavBar />}</nav>
        </div>
        <div className="part-2">
            {/* <div className="content-container"> */}
                <section id="main-section" >{<Section />}</section>
                <footer id="main-footer" >{<Footer/>}</footer>
            {/* </div> */}
        </div>
        
    </div>);
}

export default PageLayout;