import {useEffect, useState, createContext, useMemo, memo } from "react";
import { useCookies } from "react-cookie";
import FetchResource from "./util/FetchResource";
import Table from "./component/CreateTable";
import DiscountsBoxModel  from "./DiscountsBoxModel";

export const DiscountsBoxModelContext = createContext();

const Resource = ((cookies)=>{
  return({
  resource: "/discounts",
  options: {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + cookies.user.jwt,
    },
  },
  });
});


const TableHeaders = [
  "ID#",
  "Name",
  "Description",
  "Discount%",
  "Products",
  "Created-At",
  "Modified_At"
];


export default function Discount() {
  const [cookies] = useCookies();
  const [data,setData] = useState("Loading....");
  const [boxModel, setBoxModel] = useState( {
    "display": false,
    "submitForm": false
  });

  const boxModelMemo = useMemo(
    ()=> ({boxModel,setBoxModel}),
    [boxModel]
  );
  
  useEffect(()=>{
    FetchResource(Resource(cookies)).then((data)=>{
     
      setData(data);

    });
  },[]);

  
  if((typeof data) === "string"){
    return (<Table 
      tableData={{
        "headerContents": TableHeaders,
        "bodyContents": data
        }}
    />);
  }

  let discounts = data.discounts.content;
  console.log(discounts);
  for(let i = 0; i < discounts.length; i ++){

    delete discounts[i].discount;
    delete discounts[i].links;
  }

  const HandleButtonClick = () => {

    if(boxModel.display){
      setBoxModel(oldState => ({
        ...oldState,
        display: false
      }));
    }else{
      setBoxModel(oldState => ({
        ...oldState,
        display: true,
      }));
    }
  }

  return (
    <>
    <DiscountsBoxModelContext.Provider value={boxModelMemo}>
      <div className="products-table-container">
        {<Table 
          tableData={{
            "headerContents": TableHeaders,
            "bodyContents": data.discounts.content
            }}
        />}
      </div>
      <div id="addProduct-tb-floating-button-container">
        <button type="button" className="btn btn-primary" onClick={HandleButtonClick}>  
          <i className="fas fa-plus-circle"></i> Add Discount
        </button>
      </div>
      {boxModel.display ? <DiscountsBoxModel resource={"/discount"}/> : null}
    </DiscountsBoxModelContext.Provider>
    </>
  );
}
