import { useEffect } from "react";
import { useCookies } from "react-cookie";

export default function GetCookies(values){
  const [cookies, setCookies] = useCookies();
  
  return cookies;
}