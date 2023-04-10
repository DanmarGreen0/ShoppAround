import { useContext, useState, useEffect} from "react"
import { useCookies } from "react-cookie";
import { DiscountsBoxModelContext } from "./Discounts"
import "./styles/Model.css";
import FetchResource from "./util/FetchResource";
import { useNavigate } from "react-router";

let Resource = ((cookies, body, resource)=>{
    return({
    resource: resource,
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


export default function DiscountsBoxModel({resource}){
    const [data,setData] = useState("Loading....");
    const navigate = useNavigate();
    const [cookies,] = useCookies();
    const [inputs, setInputs] = useState({});
    const { boxModel, setBoxModel } = useContext(DiscountsBoxModelContext);

    useEffect(()=>{},[data]);

    const HandleCancelButtonClickEvent = ()=> {

        setBoxModel(oldState => ({
            ...oldState,
            display: false
          }));
        
    }

    const HandleChangeEvent = (event) => {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setInputs((values) => ({...values, [name]: value }));
    }

    const HandleSubmitButtonClickEvent = ()=>{

        FetchResource(Resource(cookies, inputs, resource)).then((data1)=>{
            
            setData(data1)
            
            // navigate("/products");
        });

        
    }

    let nameInput = inputs.name || "";
    let descriptionInput = inputs.description || "";
    let productsInput = inputs.products || "";
    let discountPercentInput = inputs.discountPercent || "";
  
    return(
        <div className="model">
            <div className="inner-container">
                <form className="addProduct-form" onSubmit={HandleSubmitButtonClickEvent}>
                    <div id="fields">
                        <div>
                            <label htmlFor="name">Name</label>
                            <input 
                                type="text" 
                                name="name"
                                id="name"
                                defaultValue={nameInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="description">Description</label>
                            <textarea 
                                name="description"
                                id="description"
                                defaultValue={descriptionInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                        <div>
                            <label htmlFor="discountPercent">Discount%</label>
                            <input 
                                type="text" 
                                name="discountPercent"
                                id="discountPercent"
                                defaultValue={discountPercentInput}
                                onChange={HandleChangeEvent}
                            />
                        </div>
                    </div>
                    <div id="buttons">
                        <input type="submit" value="Submit" id="submit" />
                        <button type="button" value="Cancel" id="cancel" onClick={HandleCancelButtonClickEvent}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
}