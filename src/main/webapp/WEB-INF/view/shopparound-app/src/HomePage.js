import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useLocation } from "react-router";
import ProductCard from "./ProductCard";
import ProductsSortMenu from "./ProductsSortMenu";
import "./styles/Products.css";
import "./styles/ProductsSortMenu.css";

function Home() {
  const { state } = useLocation();
  const [content, setContent] = useState([]);
  useEffect(() => {}, [content]);

  let cards = [];

  for (var i = 0; i < 20; i++) {
    cards.push(<ProductCard key={i} />);
  }

  function Screen() {
    return (
      <div className="products-screen">
        <div className="ProductsSortMenu-container">
          <ProductsSortMenu />
        </div>
        <div className="cards-container">{cards}</div>
        <div className="cart-menu">
          <Link id="cart2"> 
            <i className="fas fa-shopping-cart"></i>
          </Link>
        </div>
      </div>
    );
  }

  return <Screen />;
}

export default Home;
