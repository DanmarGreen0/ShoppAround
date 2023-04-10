import React, { useEffect, useState, createContext, useMemo, memo } from "react";
import { useCookies } from "react-cookie";
import Model from "./Model";
import FetchResource from "./util/FetchResource";
import CreateTable from "./component/CreateTable";

const paramsObj = {pageNo: 0, sortBy: "name", sortDirection: "asc", pageSize: 2};
const Resource = ((cookies)=>{
  return({
  resource: "/products" + "?" + new URLSearchParams(paramsObj), 
  options: {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + cookies.user.jwt,
    },
  },
  });
});

export const BoxModelContext = createContext();

const TableHeaders = [
  "ID",
  "Name",
  "Description",
  "Sku",
  "Category",
  "Price",
  "Created-At",
  "Modified_At"
];


 function Products() {
  const [data,setData] = useState("Loading....");
  const [cookies] = useCookies();
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
    return (<CreateTable 
      tableData={{
        "headerContents": TableHeaders,
        "bodyContents": data
        }}
    />);
  }

  let products = data.products.content;

  for(let i = 0; i < products.length; i ++){
    delete products[i].discount;
    delete products[i].links;
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
    <BoxModelContext.Provider value={boxModelMemo}>
      <div className="products-table-container">
        {<CreateTable 
          tableData={{
            "headerContents": TableHeaders,
            "bodyContents": products
            }}
        />}
      </div>
      <div id="addProduct-tb-floating-button-container">
        <button type="button" className="btn btn-primary" onClick={HandleButtonClick}>  
          <i className="fas fa-plus-circle"></i> Add Product
        </button>
      </div>
      {boxModel.display ? <Model resource={"/product"}/> : null}
    </BoxModelContext.Provider>
  );
}

export default memo(Products);