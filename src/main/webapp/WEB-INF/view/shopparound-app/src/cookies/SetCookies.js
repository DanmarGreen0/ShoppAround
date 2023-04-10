import { useCookies } from "react-cookie";

export default function SetCookie(values){
    const [cookies,setCookies] = useCookies();

    setCookies(
        values.name, 
        values.value
    );

    return cookies;
}