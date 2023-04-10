import "./styles/ProfileMenu.css";

export default function Profile(){

    return(
        <>
            <div className="profile-menu-container">
                <div id="right">
                    Content Here
                </div>
                <div id="middle">
                    <a href="/login">Login</a><br/>
                    <a href="/signUp">SignUp</a>
                </div>
                <div id="left">
                    Content Here
                </div>
            </div>
        </>
    );
}